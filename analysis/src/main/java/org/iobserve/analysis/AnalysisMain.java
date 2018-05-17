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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.configuration.Configuration;

import org.iobserve.model.PCMModelHandler;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
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

    private boolean pcmFeature;

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
        boolean configurationGood = true;
        try {
            this.pcmFeature = configuration.getBooleanProperty(ConfigurationKeys.PCM_FEATURE);
            if (this.pcmFeature) {
                /** process configuration parameter. */
                this.modelInitDirectory = new File(
                        configuration.getStringProperty(ConfigurationKeys.PCM_MODEL_INIT_DIRECTORY));
                if (this.modelInitDirectory == null) {
                    AbstractServiceMain.LOGGER.info("Reuse PCM model in database.");
                } else {
                    configurationGood &= CommandLineParameterEvaluation.checkDirectory(this.modelInitDirectory,
                            "PCM startup model", commander);
                }

                this.modelDatabaseDirectory = new File(
                        configuration.getStringProperty(ConfigurationKeys.PCM_MODEL_DB_DIRECTORY));
                configurationGood &= CommandLineParameterEvaluation.checkDirectory(this.modelDatabaseDirectory,
                        "PCM database directory", commander);
            }

            if (configuration.getBooleanProperty(ConfigurationKeys.CONTAINER_MANAGEMENT_VISUALIZATION_FEATURE)) {
                configurationGood &= CommandLineParameterEvaluation.createURL(
                        configuration.getStringProperty(ConfigurationKeys.IOBSERVE_VISUALIZATION_URL),
                        "Management visualization URL") != null;
            }

            return configurationGood;
        } catch (final IOException e) {
            return false;
        }
    }

    @Override
    protected AnalysisConfiguration createConfiguration(final Configuration configuration)
            throws ConfigurationException {

        IModelProvider<Repository> repositoryModelProvider = null;
        IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider = null;
        IModelProvider<Allocation> allocationModelProvider = null;
        IModelProvider<AllocationContext> allocationContextModelProvider = null;
        IModelProvider<System> systemModelProvider = null;
        IModelProvider<AssemblyContext> assemblyContextProvicer = null;
        IModelProvider<UsageModel> usageModelProvider = null;
        IModelProvider<AssemblyEntry> assemblyEntryCorrespondenceModelProvider = null;

        /** Configure model handling. */
        if (this.pcmFeature) {
            final PCMModelHandler modelFileHandler = new PCMModelHandler(this.modelInitDirectory);

            /** initialize neo4j graphs. */
            final GraphLoader graphLoader = new GraphLoader(this.modelDatabaseDirectory);

            graphLoader.initializeModelGraph(Repository.class, modelFileHandler.getRepositoryModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(ResourceEnvironment.class, modelFileHandler.getResourceEnvironmentModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(Allocation.class, modelFileHandler.getAllocationModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(System.class, modelFileHandler.getSystemModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            // graphLoader.initializeModelGraph(UsageModel.class, modelFileHandler.getUsageModel(),
            // ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(CorrespondenceModel.class, modelFileHandler.getCorrespondenceModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

            /** load neo4j graphs. */
            final Graph<Repository> repositoryModelGraph = graphLoader.createModelGraph(Repository.class);
            final Graph<ResourceEnvironment> resourceEnvironmentGraph = graphLoader
                    .createModelGraph(ResourceEnvironment.class);
            final Graph<Allocation> allocationModelGraph = graphLoader.createModelGraph(Allocation.class);
            final Graph<System> systemModelGraph = graphLoader.createModelGraph(System.class);
            final Graph<UsageModel> usageModelGraph = graphLoader.createModelGraph(UsageModel.class);
            final Graph<CorrespondenceModel> correspondenceModelGraph = graphLoader
                    .createModelGraph(CorrespondenceModel.class);

            /** new graphModelProvider. */
            repositoryModelProvider = new ModelProvider<>(repositoryModelGraph, ModelProvider.PCM_ENTITY_NAME,
                    ModelProvider.PCM_ID);
            resourceEnvironmentModelProvider = new ModelProvider<>(resourceEnvironmentGraph,
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            allocationModelProvider = new ModelProvider<>(allocationModelGraph, ModelProvider.PCM_ENTITY_NAME,
                    ModelProvider.PCM_ID);
            allocationContextModelProvider = new ModelProvider<>(allocationModelGraph, ModelProvider.PCM_ENTITY_NAME,
                    ModelProvider.PCM_ID);
            systemModelProvider = new ModelProvider<>(systemModelGraph, ModelProvider.PCM_ENTITY_NAME,
                    ModelProvider.PCM_ID);
            assemblyContextProvicer = new ModelProvider<>(systemModelGraph, ModelProvider.PCM_ENTITY_NAME,
                    ModelProvider.PCM_ID);
            usageModelProvider = new ModelProvider<>(usageModelGraph, ModelProvider.PCM_ENTITY_NAME,
                    ModelProvider.PCM_ID);
            assemblyEntryCorrespondenceModelProvider = new ModelProvider<>(correspondenceModelGraph,
                    ModelProvider.IMPLEMENTATION_ID, null);

            // get systemId
            final System systemModel = systemModelProvider.readOnlyRootComponent(System.class);

            configuration.setProperty(ConfigurationKeys.SYSTEM_ID, systemModel.getId());
        }

        return new AnalysisConfiguration(configuration, repositoryModelProvider, resourceEnvironmentModelProvider,
                allocationModelProvider, allocationContextModelProvider, systemModelProvider, usageModelProvider,
                assemblyEntryCorrespondenceModelProvider);
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
        // In case runtime data must be serialized, this would be the right place to
        // trigger
        // serialization
    }

    @Override
    protected File getConfigurationFile() {
        return this.configurationFile;
    }

}
