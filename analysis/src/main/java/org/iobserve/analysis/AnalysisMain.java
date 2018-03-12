/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.configuration.Configuration;

import org.iobserve.analysis.configurations.AnalysisConfiguration;
import org.iobserve.analysis.configurations.ConfigurationKeys;
import org.iobserve.model.PCMModelHandler;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Main class for starting the iObserve application.
 *
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 * @since 0.0.1
 */
public final class AnalysisMain extends AbstractServiceMain<AnalysisConfiguration> {

    @Parameter(names = "--help", help = true)
    private boolean help; // NOPMD access through reflection

    @Parameter(names = { "-c",
            "--configuration" }, required = true, description = "Configuration file.", converter = FileConverter.class)
    private File configurationFile;

    private File modelInitDirectory;

    private File modelDatabaseDirectory;

    private URL containerManagementVisualizationBaseUrl;

    /**
     * Default constructor.
     */
    private AnalysisMain() {
        // do nothing here
    }

    /**
     * Main function.
     *
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        new AnalysisMain().run("Analysis Service", "analysis", args);
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        /** process configuration parameter. */
        this.modelInitDirectory = new File(configuration.getStringProperty(ConfigurationKeys.PCM_MODEL_INIT_DIRECTORY));
        this.modelDatabaseDirectory = new File(
                configuration.getStringProperty(ConfigurationKeys.PCM_MODEL_DB_DIRECTORY));

        this.containerManagementVisualizationBaseUrl = CommandLineParameterEvaluation.createURL(
                configuration.getStringProperty(ConfigurationKeys.IOBSERVE_VISUALIZATION_URL),
                "Management visualization URL");

        try {
            return CommandLineParameterEvaluation.checkDirectory(this.modelInitDirectory, "PCM startup model",
                    commander)
                    && CommandLineParameterEvaluation.checkDirectory(this.modelDatabaseDirectory,
                            "PCM database directory", commander)
                    && this.containerManagementVisualizationBaseUrl != null;
        } catch (final IOException e) {
            return false;
        }
    }

    @Override
    protected AnalysisConfiguration createConfiguration(final Configuration configuration)
            throws ConfigurationException {

        /** Configure model handling. */
        // old model providers without neo4j
        final PCMModelHandler modelFileHandler = new PCMModelHandler(this.modelInitDirectory);

        final ICorrespondence correspondenceModel = modelFileHandler.getCorrespondenceModel();

        /** initialize neo4j graphs. */
        final GraphLoader graphLoader = new GraphLoader(this.modelDatabaseDirectory);

        Graph repositoryModelGraph = graphLoader.initializeRepositoryModelGraph(modelFileHandler.getRepositoryModel());
        Graph resourceEnvironmentModelGraph = graphLoader
                .initializeResourceEnvironmentModelGraph(modelFileHandler.getResourceEnvironmentModel());
        Graph allocationModelGraph = graphLoader.initializeAllocationModelGraph(modelFileHandler.getAllocationModel());
        Graph systemModelGraph = graphLoader.initializeSystemModelGraph(modelFileHandler.getSystemModel());
        Graph usageModelGraph = graphLoader.initializeUsageModelGraph(modelFileHandler.getUsageModel());

        /** load neo4j graphs. */
        repositoryModelGraph = graphLoader.createRepositoryModelGraph();
        resourceEnvironmentModelGraph = graphLoader.createResourceEnvironmentModelGraph();
        allocationModelGraph = graphLoader.createAllocationModelGraph();
        systemModelGraph = graphLoader.createSystemModelGraph();
        usageModelGraph = graphLoader.createUsageModelGraph();

        /** new graphModelProvider. */
        final IModelProvider<Repository> repositoryModelProvider = new ModelProvider<>(repositoryModelGraph);
        final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider = new ModelProvider<>(
                resourceEnvironmentModelGraph);
        final IModelProvider<Allocation> allocationModelProvider = new ModelProvider<>(allocationModelGraph);
        final IModelProvider<org.palladiosimulator.pcm.system.System> systemModelProvider = new ModelProvider<>(
                systemModelGraph);
        final IModelProvider<UsageModel> usageModelProvider = new ModelProvider<>(usageModelGraph);

        // get systemId
        final org.palladiosimulator.pcm.system.System systemModel = systemModelProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);

        final String systemId = systemModel.getId();

        try {
            /** URLs for sending updates to the deployment visualization. */

            final InitializeDeploymentVisualization deploymentVisualization = new InitializeDeploymentVisualization(
                    this.containerManagementVisualizationBaseUrl, systemId, allocationModelProvider,
                    systemModelProvider, resourceEnvironmentModelProvider);

            deploymentVisualization.initialize();

            return new AnalysisConfiguration(configuration, repositoryModelProvider, resourceEnvironmentModelProvider,
                    allocationModelProvider, systemModelProvider, usageModelProvider, correspondenceModel);

        } catch (final MalformedURLException e) {
            AbstractServiceMain.LOGGER.debug("URL construction for deployment visualization failed.", e);
            return null;
        } catch (final IOException e) {
            AbstractServiceMain.LOGGER.debug("Deployment visualization could not connect to visualization service.", e);
            return null;
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            return CommandLineParameterEvaluation.isFileReadable(this.configurationFile, "Configuration File");
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected void shutdownService() {
        // No additional shutdown hooks necessary.
        // In case runtime data must be serialized, this would be the right place to trigger
        // serialization
    }

    @Override
    protected File getConfigurationFile() {
        return this.configurationFile;
    }

}
