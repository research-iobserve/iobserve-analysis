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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.model.ModelImporter;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.PrivacyPackage;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;

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

    private File modelDumpDirectory;

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

            /** initialize database. */
            final ModelResource<CorrespondenceModel> correspondenceModelResource = this
                    .loadResourceAndInitDB(CorrespondencePackage.eINSTANCE, modelHandler.getCorrespondenceModel());

            final ModelResource<Repository> repositoryModelResource = this
                    .loadResourceAndInitDB(RepositoryPackage.eINSTANCE, modelHandler.getRepositoryModel());

            final ModelResource<ResourceEnvironment> resourceEnvironmentModelResource = this.loadResourceAndInitDB(
                    ResourceenvironmentPackage.eINSTANCE, modelHandler.getResourceEnvironmentModel());

            final ModelResource<System> systemModelResource = this.loadResourceAndInitDB(SystemPackage.eINSTANCE,
                    modelHandler.getSystemModel());

            final ModelResource<Allocation> allocationModelResource = this
                    .loadResourceAndInitDB(AllocationPackage.eINSTANCE, modelHandler.getAllocationModel());

            final ModelResource<PrivacyModel> privacyModelResource = this
                    .loadResourceAndInitDB(PrivacyPackage.eINSTANCE, modelHandler.getPrivacyModel());

            return new PrivacyViolationDetectionConfiguration(configuration, correspondenceModelResource,
                    repositoryModelResource, resourceEnvironmentModelResource, systemModelResource,
                    allocationModelResource, privacyModelResource, this.warningFile, this.alarmsFile,
                    this.modelDumpDirectory);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    private <T extends EObject> ModelResource<T> loadResourceAndInitDB(final EPackage ePackage, final EObject model) {
        final ModelResource<T> resource = new ModelResource<>(ePackage,
                new File(this.modelDatabaseDirectory, ePackage.getName()));
        resource.clearResource();
        resource.storeModelPartition(model);
        return resource;
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

            this.modelDumpDirectory = new File(
                    configuration.getPathProperty(PrivacyConfigurationsKeys.MODEL_DUMP_DIRECTORY_PATH));
            configurationGood &= CommandLineParameterEvaluation.checkDirectory(this.modelDumpDirectory.getParentFile(),
                    String.format("model dump location (%s)", PrivacyConfigurationsKeys.MODEL_DUMP_DIRECTORY_PATH),
                    commander);

            return configurationGood;
        } catch (final IOException e) {
            AbstractServiceMain.LOGGER.error("Evaluating command line parameter failed.", e);
            return false;
        }
    }

}
