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

import org.iobserve.model.ModelImporter;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.privacy.PrivacyPackage;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;
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
public final class AnalysisMain extends AbstractServiceMain<AnalysisConfiguration> {

    private static final String PALLADIO_PREFIX = "org.palladiosimulator";

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

        /** Configure model handling. */
        if (this.pcmFeature) {
            try {
                final ModelImporter modelHandler = new ModelImporter(this.modelInitDirectory);

                /** initialize neo4j graphs. */
                final ModelResource correspondenceModelResource = new ModelResource(CorrespondencePackage.eINSTANCE,
                        new File(this.modelDatabaseDirectory, "correspondence"));
                correspondenceModelResource.storeModelPartition(modelHandler.getCorrespondenceModel());

                final ModelResource repositoryModelResource = new ModelResource(RepositoryPackage.eINSTANCE,
                        new File(this.modelDatabaseDirectory, "repository"));
                repositoryModelResource.storeModelPartition(modelHandler.getRepositoryModel());

                final ModelResource resourceEnvironmentModelResource = new ModelResource(
                        ResourceenvironmentPackage.eINSTANCE,
                        new File(this.modelDatabaseDirectory, "resourceenvironment")); // add

                resourceEnvironmentModelResource.storeModelPartition(modelHandler.getResourceEnvironmentModel());

                final ModelResource systemModelResource = new ModelResource(SystemPackage.eINSTANCE,
                        new File(this.modelDatabaseDirectory, "system"));
                systemModelResource.storeModelPartition(modelHandler.getSystemModel());

                final ModelResource allocationModelResource = new ModelResource(AllocationPackage.eINSTANCE,
                        new File(this.modelDatabaseDirectory, "allocation"));
                allocationModelResource.storeModelPartition(modelHandler.getAllocationModel());

                final ModelResource usageModelResource = new ModelResource(UsagemodelPackage.eINSTANCE,
                        new File(this.modelDatabaseDirectory, "usageModel"));
                usageModelResource.storeModelPartition(modelHandler.getUsageModel());

                final ModelResource privacyModelResource = new ModelResource(PrivacyPackage.eINSTANCE,
                        new File(this.modelDatabaseDirectory, "privacy"));
                privacyModelResource.storeModelPartition(modelHandler.getPrivacyModel());

                // get systemId
                final System systemModel = systemModelResource.getModelRootNode(System.class);

                configuration.setProperty(ConfigurationKeys.SYSTEM_ID, systemModel.getId());

                return new AnalysisConfiguration(configuration, repositoryModelResource,
                        resourceEnvironmentModelResource, systemModelResource, allocationModelResource,
                        usageModelResource, correspondenceModelResource);
            } catch (final IOException e) {
                java.lang.System.err.println("Canot load all models " + e.getLocalizedMessage());
                return null;
            }
        } else {
            return new AnalysisConfiguration(configuration);
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
        // In case runtime data must be serialized, this would be the right place to
        // trigger
        // serialization
    }

    @Override
    protected File getConfigurationFile() {
        return this.configurationFile;
    }

}
