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

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;

import org.apache.commons.io.FileUtils;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.model.ModelImporter;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.IModelResource;
import org.iobserve.model.persistence.memory.MemoryModelResource;
import org.iobserve.model.privacy.DataProtectionModel;
import org.iobserve.model.privacy.PrivacyPackage;
import org.iobserve.stages.data.ExperimentLoggingUtils;
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
public final class PrivacyViolationDetectionServiceMain extends AbstractService<PipelineConfiguration, Settings> {

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
        ExperimentLoggingUtils.createSamplers();
        java.lang.System.exit(new PrivacyViolationDetectionServiceMain().run("Privacy Violation Detection Service",
                "privacy", args, new Settings()));
    }

    @Override
    protected PipelineConfiguration createTeetimeConfiguration() throws ConfigurationException {
        /** load models. */
        try {
            final ModelImporter modelHandler = new ModelImporter(this.parameterConfiguration.getModelInitDirectory());

            /** initialize database. */
            final IModelResource<CorrespondenceModel> correspondenceModelResource = this
                    .loadResourceAndInitDB(CorrespondencePackage.eINSTANCE, modelHandler.getCorrespondenceModel());

            final IModelResource<Repository> repositoryModelResource = this
                    .loadResourceAndInitDB(RepositoryPackage.eINSTANCE, modelHandler.getRepositoryModel());

            final IModelResource<ResourceEnvironment> resourceEnvironmentModelResource = this.loadResourceAndInitDB(
                    ResourceenvironmentPackage.eINSTANCE, modelHandler.getResourceEnvironmentModel());

            final IModelResource<System> systemModelResource = this.loadResourceAndInitDB(SystemPackage.eINSTANCE,
                    modelHandler.getSystemModel());

            final IModelResource<Allocation> allocationModelResource = this
                    .loadResourceAndInitDB(AllocationPackage.eINSTANCE, modelHandler.getAllocationModel());

            final IModelResource<DataProtectionModel> privacyModelResource = this
                    .loadResourceAndInitDB(PrivacyPackage.eINSTANCE, modelHandler.getPrivacyModel());

            return new PipelineConfiguration(this.kiekerConfiguration, correspondenceModelResource,
                    repositoryModelResource, resourceEnvironmentModelResource, systemModelResource,
                    allocationModelResource, privacyModelResource, this.parameterConfiguration.getWarningFile(),
                    this.parameterConfiguration.getAlarmsFile(), this.parameterConfiguration.getModelDumpDirectory());
        } catch (final DBException | IOException e) {
            throw new ConfigurationException(e);
        }
    }

    private <T extends EObject> IModelResource<T> loadResourceAndInitDB(final EPackage ePackage, final T model)
            throws DBException, IOException {
        final File file = new File(this.parameterConfiguration.getModelDatabaseDirectory(), ePackage.getName());
        if (file.exists()) {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else {
                throw new IOException(String.format("%s is not a directory.", file.getAbsolutePath()));
            }
        }
        // final IModelResource<T> resource = new Neo4JModelResource<>(ePackage, file);
        final IModelResource<T> resource = new MemoryModelResource<>(ePackage);
        resource.clearResource();
        resource.storeModelPartition(model);
        return resource;
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        return ParameterEvaluationUtils.isFileReadable(this.parameterConfiguration.getConfigurationFile(),
                "Configuration File", commander);
    }

    @Override
    protected void shutdownService() {
        // empty, no special shutdown required
    }

    @Override
    protected File getConfigurationFile() {
        return this.parameterConfiguration.getConfigurationFile();
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        boolean configurationGood = true;

        /** process configuration parameter. */
        final String modelInitDirectoryName = configuration
                .getStringProperty(ConfigurationKeys.PCM_MODEL_INIT_DIRECTORY);
        if (modelInitDirectoryName == null) {
            this.logger.info("Reuse PCM model in database.");
        } else {
            this.parameterConfiguration.setModelInitDirectory(new File(modelInitDirectoryName));
            configurationGood &= ParameterEvaluationUtils.checkDirectory(
                    this.parameterConfiguration.getModelInitDirectory(),
                    String.format("PCM startup model (%s)", ConfigurationKeys.PCM_MODEL_INIT_DIRECTORY), commander);
        }

        this.parameterConfiguration.setModelDatabaseDirectory(
                new File(configuration.getStringProperty(ConfigurationKeys.PCM_MODEL_DB_DIRECTORY)));
        configurationGood &= ParameterEvaluationUtils.checkDirectory(
                this.parameterConfiguration.getModelDatabaseDirectory(),
                String.format("PCM database directory (%s)", ConfigurationKeys.PCM_MODEL_DB_DIRECTORY), commander);

        this.parameterConfiguration
                .setAlarmsFile(new File(configuration.getPathProperty(PrivacyConfigurationsKeys.ALARM_FILE_PATH)));
        configurationGood &= ParameterEvaluationUtils.checkDirectory(
                this.parameterConfiguration.getAlarmsFile().getParentFile(),
                String.format("alarm location (%s)", PrivacyConfigurationsKeys.ALARM_FILE_PATH), commander);

        this.parameterConfiguration
                .setWarningFile(new File(configuration.getPathProperty(PrivacyConfigurationsKeys.WARNING_FILE_PATH)));
        configurationGood &= ParameterEvaluationUtils.checkDirectory(
                this.parameterConfiguration.getWarningFile().getParentFile(),
                String.format("warnings location (%s)", PrivacyConfigurationsKeys.WARNING_FILE_PATH), commander);

        final String modelDumpPath = configuration.getPathProperty(PrivacyConfigurationsKeys.MODEL_DUMP_DIRECTORY_PATH);
        if (modelDumpPath != null && !modelDumpPath.isEmpty()) {
            this.parameterConfiguration.setModelDumpDirectory(
                    new File(configuration.getPathProperty(PrivacyConfigurationsKeys.MODEL_DUMP_DIRECTORY_PATH)));
            configurationGood &= ParameterEvaluationUtils.checkDirectory(
                    this.parameterConfiguration.getModelDumpDirectory().getParentFile(),
                    String.format("model dump location (%s)", PrivacyConfigurationsKeys.MODEL_DUMP_DIRECTORY_PATH),
                    commander);
        }
        return configurationGood;
    }

}
