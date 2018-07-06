/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.privacy.violation;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import kieker.common.configuration.Configuration;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.model.ModelImporter;
import org.iobserve.model.correspondence.CorrespondenceFactory;
import org.iobserve.model.persistence.neo4j.IModelProvider;
import org.iobserve.model.persistence.neo4j.ModelGraph;
import org.iobserve.model.persistence.neo4j.ModelResourceLoader;
import org.iobserve.model.persistence.neo4j.ModelProvider;
import org.iobserve.model.privacy.PrivacyFactory;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemFactory;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class PrivacyViolationDetectionServiceMain
        extends AbstractServiceMain<PrivacyViolationDetectionConfiguration> {

    @Parameter(names = { "-c", "--configuration" }, required = true, description = "Configuration file")
    private File configurationFile;

    private File modelInitDirectory;

    private File warningFile;

    private File alarmsFile;

    private File modelDatabaseDirectory;

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private PrivacyViolationDetectionServiceMain() {

    }

    /**
     * Configure and execute the TCP Kieker data collector.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        new PrivacyViolationDetectionServiceMain().run("Privacy Violation Detection Service", "privacy", args);
    }

    @Override
    protected PrivacyViolationDetectionConfiguration createConfiguration(final Configuration configuration)
            throws ConfigurationException {

        /** load models. */
        try {
            final ModelImporter modelHandler = new ModelImporter(this.modelInitDirectory);
            final ModelResourceLoader graphLoader = new ModelResourceLoader(this.modelDatabaseDirectory);

            /** initialize database. */
            graphLoader.initializeModelGraph(RepositoryFactory.eINSTANCE, modelHandler.getRepositoryModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(ResourceenvironmentFactory.eINSTANCE,
                    modelHandler.getResourceEnvironmentModel(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(AllocationFactory.eINSTANCE, modelHandler.getAllocationModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(SystemFactory.eINSTANCE, modelHandler.getSystemModel(),
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            graphLoader.initializeModelGraph(CorrespondenceFactory.eINSTANCE, modelHandler.getCorrespondenceModel(),
                    null, ModelProvider.IMPLEMENTATION_ID);
            graphLoader.initializeModelGraph(PrivacyFactory.eINSTANCE, modelHandler.getPrivacyModel(), null,
                    ModelProvider.PCM_ID);

            /** load neo4j graphs. */
            final ModelGraph repositoryGraph = graphLoader.createModelResource(RepositoryFactory.eINSTANCE);
            final ModelGraph resourceEnvironmentGraph = graphLoader
                    .createModelResource(ResourceenvironmentFactory.eINSTANCE);
            final ModelGraph allocationModelGraph = graphLoader.createModelResource(AllocationFactory.eINSTANCE);
            final ModelGraph systemModelGraph = graphLoader.createModelResource(SystemFactory.eINSTANCE);
            final ModelGraph correspondenceModelGraph = graphLoader.createModelResource(CorrespondenceFactory.eINSTANCE);
            final ModelGraph privacyModelGraph = graphLoader.createModelResource(PrivacyFactory.eINSTANCE);

            /** model provider. */
            final IModelProvider<Repository> repositoryModelProvider = new ModelProvider<>(repositoryGraph,
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

            final ModelProvider<Allocation> allocationModelProvider = new ModelProvider<>(allocationModelGraph,
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
            final IModelProvider<AllocationContext> allocationContextModelProvider = new ModelProvider<>(
                    allocationModelGraph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider = new ModelProvider<>(
                    resourceEnvironmentGraph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

            final ModelProvider<System> systemModelProvider = new ModelProvider<>(systemModelGraph,
                    ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

            return new PrivacyViolationDetectionConfiguration(configuration, correspondenceModelGraph,
                    repositoryModelProvider, resourceEnvironmentModelProvider, allocationModelProvider,
                    allocationContextModelProvider, systemModelProvider, privacyModelGraph, this.warningFile,
                    this.alarmsFile);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
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
        // empty, no special shutdown required
    }

    @Override
    protected File getConfigurationFile() {
        return this.configurationFile;
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        boolean configurationGood = true;
        try {
            /** process configuration parameter. */
            final String modelInitDirectoryName = configuration
                    .getStringProperty(ConfigurationKeys.PCM_MODEL_INIT_DIRECTORY);
            if (modelInitDirectoryName == null) {
                AbstractServiceMain.LOGGER.info("Reuse PCM model in database.");
            } else {
                this.modelInitDirectory = new File(modelInitDirectoryName);
                configurationGood &= CommandLineParameterEvaluation.checkDirectory(this.modelInitDirectory,
                        String.format("PCM startup model (%s)", ConfigurationKeys.PCM_MODEL_INIT_DIRECTORY), commander);
            }

            this.modelDatabaseDirectory = new File(
                    configuration.getStringProperty(ConfigurationKeys.PCM_MODEL_DB_DIRECTORY));
            configurationGood &= CommandLineParameterEvaluation.checkDirectory(this.modelDatabaseDirectory,
                    String.format("PCM database directory (%s)", ConfigurationKeys.PCM_MODEL_DB_DIRECTORY), commander);

            this.alarmsFile = new File(configuration.getPathProperty(PrivacyConfigurationsKeys.ALARM_FILE_PATH));
            configurationGood &= CommandLineParameterEvaluation.checkDirectory(this.alarmsFile.getParentFile(),
                    String.format("alarm location (%s)", PrivacyConfigurationsKeys.ALARM_FILE_PATH), commander);

            this.warningFile = new File(configuration.getPathProperty(PrivacyConfigurationsKeys.WARNING_FILE_PATH));
            configurationGood &= CommandLineParameterEvaluation.checkDirectory(this.warningFile.getParentFile(),
                    String.format("warnings location (%s)", PrivacyConfigurationsKeys.WARNING_FILE_PATH), commander);

            return configurationGood;
        } catch (final IOException e) {
            AbstractServiceMain.LOGGER.error("Evaluating command line parameter failed.", e);
            return false;
        }
    }

}
