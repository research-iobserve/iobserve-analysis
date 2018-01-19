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
package org.iobserve.analysis.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.ConfigurationException;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.model.PCMModelHandler;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Main class for starting the iObserve application.
 *
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class AnalysisMain extends AbstractServiceMain<ServiceConfiguration> {
    private static final String PREFIX = AnalysisMain.class.getCanonicalName();

    private static final String IOBSERVE_VISUALIZATION_URL = AnalysisMain.PREFIX + ".visualization.url";

    private static final String SNAPSHOT_PATH = AnalysisMain.PREFIX + ".snapshot";

    private static final String PER_OPTERYX_URI_PATH = AnalysisMain.PREFIX + ".perOpteryxUriPath";

    private static final String LQNS_URI_PATH = AnalysisMain.PREFIX + ".lqnsUriPath";

    private static final String DEPLOYABLES_FOLDER_PATH = AnalysisMain.PREFIX + ".deployables_folder_path";

    private static final String BEHAVIOR_VISUALIZATION_URL = AnalysisMain.PREFIX + ".behavior.visualization.url";

    private static final String BEHAVIOR_CLOSED_WORKLOAD = AnalysisMain.PREFIX + ".behavior.closed.workload";

    private static final String BEHAVIOR_THINK_TIME = AnalysisMain.PREFIX + ".behavior.think.time";

    private static final String BEHAVIOR_VARIANCE_OF_USER_GROUPS = ".behavior.variance.of.user.groups";

    // TODO these two need a better place
    private static final String PREFIX2 = "org.iobserve.model";

    public static final String PCM_MODEL_DB_DIRECTORY = AnalysisMain.PREFIX2 + ".pcm.directory.db";

    public static final String PCM_MODEL_INIT_DIRECTORY = AnalysisMain.PREFIX2 + ".pcm.directory.init";

    @Parameter(names = "--help", help = true)
    private boolean help;

    @Parameter(names = { "-c",
            "--configuration" }, required = true, description = "Configuration file.", converter = FileConverter.class)
    private File configurationFile;

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
    protected ServiceConfiguration createConfiguration() throws ConfigurationException {
        /** process configuration parameter. */
        final Configuration configuration = ConfigurationFactory
                .createConfigurationFromFile(this.configurationFile.getAbsolutePath());

        // TODO move all properties to their respective filters
        final String snapshotPath = configuration.getStringProperty(AnalysisMain.SNAPSHOT_PATH);
        final String perOpteryxUriPath = configuration.getStringProperty(AnalysisMain.PER_OPTERYX_URI_PATH);
        final String lqnsUriPath = configuration.getStringProperty(AnalysisMain.LQNS_URI_PATH);
        final String deployablesFolderPath = configuration.getStringProperty(AnalysisMain.DEPLOYABLES_FOLDER_PATH);
        final String behaviorVisualizationServiceURL = configuration
                .getStringProperty(AnalysisMain.BEHAVIOR_VISUALIZATION_URL);
        final File modelInitDirectory = new File(
                configuration.getStringProperty(AnalysisMain.PCM_MODEL_INIT_DIRECTORY));
        final File modelDatabaseDirectory = new File(
                configuration.getStringProperty(AnalysisMain.PCM_MODEL_DB_DIRECTORY));

        // special property for a behavior filter
        final boolean closedWorkload = configuration.getBooleanProperty(AnalysisMain.BEHAVIOR_CLOSED_WORKLOAD, false);
        final int thinkTime = configuration.getIntProperty(AnalysisMain.BEHAVIOR_THINK_TIME, 1);
        final int varianceOfUserGroups = configuration.getIntProperty(AnalysisMain.BEHAVIOR_VARIANCE_OF_USER_GROUPS, 1);

        /** Configure model handling. */
        // old model providers without neo4j
        final PCMModelHandler modelProvider = new PCMModelHandler(modelInitDirectory);

        final ICorrespondence correspondenceModel = modelProvider.getCorrespondenceModel();

        /** initialize neo4j graphs. */
        final GraphLoader graphLoader = new GraphLoader(modelDatabaseDirectory);

        Graph repositoryModelGraph = graphLoader.initializeRepositoryModelGraph(modelProvider.getRepositoryModel());
        Graph resourceEnvironmentModelGraph = graphLoader
                .initializeResourceEnvironmentModelGraph(modelProvider.getResourceEnvironmentModel());
        Graph allocationModelGraph = graphLoader.initializeAllocationModelGraph(modelProvider.getAllocationModel());
        Graph systemModelGraph = graphLoader.initializeSystemModelGraph(modelProvider.getSystemModel());
        Graph usageModelGraph = graphLoader.initializeUsageModelGraph(modelProvider.getUsageModel());

        /** load neo4j graphs. */
        // TODO is this necessary to reload all repositories?
        repositoryModelGraph = graphLoader.getRepositoryModelGraph();
        resourceEnvironmentModelGraph = graphLoader.getResourceEnvironmentModelGraph();
        allocationModelGraph = graphLoader.getAllocationModelGraph();
        systemModelGraph = graphLoader.getSystemModelGraph();
        usageModelGraph = graphLoader.getUsageModelGraph();

        /** new graphModelProvider. */
        final ModelProvider<Repository> repositoryModelProvider = new ModelProvider<>(repositoryModelGraph);
        final ModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider = new ModelProvider<>(
                resourceEnvironmentModelGraph);
        final ModelProvider<ResourceContainer> resourceContainerModelProvider = new ModelProvider<>(
                resourceEnvironmentModelGraph);
        final ModelProvider<Allocation> allocationModelProvider = new ModelProvider<>(allocationModelGraph);
        final ModelProvider<AssemblyContext> assemblyContextModelProvider = new ModelProvider<>(allocationModelGraph);
        final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelProvider = new ModelProvider<>(
                systemModelGraph);
        final ModelProvider<UsageModel> usageModelProvider = new ModelProvider<>(usageModelGraph);

        // get systemId
        final org.palladiosimulator.pcm.system.System systemModel = systemModelProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);

        final String systemId = systemModel.getId();

        try {
            /** URLs for sending updates to the deployment visualization. */
            final URL containerManagementVisualizationBaseUrl = new URL(
                    configuration.getStringProperty(AnalysisMain.IOBSERVE_VISUALIZATION_URL));

            final InitializeDeploymentVisualization deploymentVisualization = new InitializeDeploymentVisualization(
                    containerManagementVisualizationBaseUrl, systemId, allocationModelProvider, systemModelProvider,
                    resourceEnvironmentModelProvider, usageModelProvider);

            deploymentVisualization.initialize();

            SnapshotBuilder snapshotBuilder = null;
            URI perOpteryxUri = null;
            URI lqnsUri = null;
            URI deployablesFolder = null;
            if (!snapshotPath.isEmpty() && !perOpteryxUriPath.isEmpty() && !lqnsUriPath.isEmpty()
                    && !deployablesFolderPath.isEmpty()) {
                SnapshotBuilder.setBaseSnapshotURI(URI.createFileURI(snapshotPath));
                try {
                    snapshotBuilder = new SnapshotBuilder("Runtime", modelProvider);
                    perOpteryxUri = URI.createFileURI(perOpteryxUriPath);
                    lqnsUri = URI.createFileURI(lqnsUriPath);
                    deployablesFolder = URI.createFileURI(deployablesFolderPath);
                } catch (final InitializationException e) {
                    throw new ConfigurationException(e);
                }

            }

            return new ServiceConfiguration(configuration, containerManagementVisualizationBaseUrl, systemId,
                    varianceOfUserGroups, thinkTime, closedWorkload, correspondenceModel, usageModelProvider,
                    repositoryModelProvider, resourceEnvironmentModelProvider, allocationModelProvider,
                    systemModelProvider, resourceContainerModelProvider, assemblyContextModelProvider,
                    behaviorVisualizationServiceURL, snapshotBuilder, perOpteryxUri, lqnsUri, deployablesFolder);

        } catch (final MalformedURLException e) {
            AbstractServiceMain.LOG.debug("URL construction for deployment visualization failed.", e);
            return null;
        } catch (final IOException e) {
            AbstractServiceMain.LOG.debug("Deployment visualization could not connect to visualization service.", e);
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

}
