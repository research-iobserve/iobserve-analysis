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

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;

import org.iobserve.model.ModelImporter;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.PrivacyPackage;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

/**
 * Main class for starting the iObserve application.
 *
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 *
 * @since 0.0.1
 */
public final class AnalysisMain extends AbstractService<AnalysisConfiguration, AnalysisSettings> {

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
        java.lang.System.exit(new AnalysisMain().run("Analysis Service", "analysis", args, new AnalysisSettings()));
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        boolean configurationGood = true;
        try {
            this.parameterConfiguration.setPcmFeature(configuration.getBooleanProperty(ConfigurationKeys.PCM_FEATURE));
            if (this.parameterConfiguration.isPcmFeature()) {
                /** process configuration parameter. */
                this.parameterConfiguration.setModelInitDirectory(
                        new File(configuration.getStringProperty(ConfigurationKeys.PCM_MODEL_INIT_DIRECTORY)));
                if (this.parameterConfiguration.getModelInitDirectory() == null) {
                    this.logger.info("Reuse PCM model in database.");
                } else {
                    configurationGood &= CommandLineParameterEvaluation.checkDirectory(
                            this.parameterConfiguration.getModelInitDirectory(), "PCM startup model", commander);
                }

                this.parameterConfiguration.setModelDatabaseDirectory(
                        new File(configuration.getStringProperty(ConfigurationKeys.PCM_MODEL_DB_DIRECTORY)));
                configurationGood &= CommandLineParameterEvaluation.checkDirectory(
                        this.parameterConfiguration.getModelDatabaseDirectory(), "PCM database directory", commander);
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
    protected AnalysisConfiguration createTeetimeConfiguration() throws ConfigurationException {

        // TODO fix ModelResource types add Types for generics
        /** Configure model handling. */
        if (this.parameterConfiguration.isPcmFeature()) {
            try {
                final ModelImporter modelHandler = new ModelImporter(
                        this.parameterConfiguration.getModelInitDirectory());

                /** initialize neo4j graphs. */
                final Neo4JModelResource<CorrespondenceModel> correspondenceModelResource = new Neo4JModelResource<>(
                        CorrespondencePackage.eINSTANCE,
                        new File(this.parameterConfiguration.getModelDatabaseDirectory(), "correspondence"));
                correspondenceModelResource.storeModelPartition(modelHandler.getCorrespondenceModel());

                final Neo4JModelResource<Repository> repositoryModelResource = new Neo4JModelResource<>(
                        RepositoryPackage.eINSTANCE,
                        new File(this.parameterConfiguration.getModelDatabaseDirectory(), "repository"));
                repositoryModelResource.storeModelPartition(modelHandler.getRepositoryModel());

                final Neo4JModelResource<ResourceEnvironment> resourceEnvironmentModelResource = new Neo4JModelResource<>(
                        ResourceenvironmentPackage.eINSTANCE,
                        new File(this.parameterConfiguration.getModelDatabaseDirectory(), "resourceenvironment")); // add

                resourceEnvironmentModelResource.storeModelPartition(modelHandler.getResourceEnvironmentModel());

                final Neo4JModelResource<System> systemModelResource = new Neo4JModelResource<>(SystemPackage.eINSTANCE,
                        new File(this.parameterConfiguration.getModelDatabaseDirectory(), "system"));
                systemModelResource.storeModelPartition(modelHandler.getSystemModel());

                final Neo4JModelResource<Allocation> allocationModelResource = new Neo4JModelResource<>(
                        AllocationPackage.eINSTANCE,
                        new File(this.parameterConfiguration.getModelDatabaseDirectory(), "allocation"));
                allocationModelResource.storeModelPartition(modelHandler.getAllocationModel());

                final Neo4JModelResource<UsageModel> usageModelResource = new Neo4JModelResource<>(
                        UsagemodelPackage.eINSTANCE,
                        new File(this.parameterConfiguration.getModelDatabaseDirectory(), "usageModel"));
                usageModelResource.storeModelPartition(modelHandler.getUsageModel());

                final Neo4JModelResource<PrivacyModel> privacyModelResource = new Neo4JModelResource<>(
                        PrivacyPackage.eINSTANCE,
                        new File(this.parameterConfiguration.getModelDatabaseDirectory(), "privacy"));
                privacyModelResource.storeModelPartition(modelHandler.getPrivacyModel());

                // get systemId
                final System systemModel = systemModelResource.getModelRootNode(System.class,
                        SystemPackage.Literals.SYSTEM);

                this.kiekerConfiguration.setProperty(ConfigurationKeys.SYSTEM_ID, systemModel.getId());

                return new AnalysisConfiguration(this.kiekerConfiguration, repositoryModelResource,
                        resourceEnvironmentModelResource, systemModelResource, allocationModelResource,
                        usageModelResource, correspondenceModelResource);
            } catch (final IOException e) {
                java.lang.System.err.println("Cannot load all models " + e.getLocalizedMessage());
                return null;
            } catch (final DBException e) {
                java.lang.System.err.println("Cannot store all models in DB " + e.getLocalizedMessage());
                return null;
            }
        } else {
            return new AnalysisConfiguration(this.kiekerConfiguration);
        }
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            return CommandLineParameterEvaluation.isFileReadable(this.getConfigurationFile(), "Configuration File");
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
        return this.parameterConfiguration.getConfigurationFile();
    }

}
