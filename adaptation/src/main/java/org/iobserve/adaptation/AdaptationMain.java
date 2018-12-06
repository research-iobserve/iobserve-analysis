/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.adaptation;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;

import kieker.analysis.common.ConfigurationException;
import kieker.tools.common.AbstractService;

import org.iobserve.adaptation.configurations.AdaptationConfiguration;
import org.iobserve.service.CommandLineParameterEvaluation;

/**
 * Main class for iObserve's adaptation service.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationMain extends AbstractService<AdaptationConfiguration, AdaptationSettings> {
    protected static final String RUNTIMEMODEL_DIRECTORY_NAME = "runtimemodel";
    protected static final String REDEPLOYMENTMODEL_DIRECTORY_NAME = "redeploymentmodel";

    /**
     * Main function.
     *
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        System.exit(new AdaptationMain().run("Adaptation Service", "adaptation", args, new AdaptationSettings()));
    }

    @Override
    protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
            final JCommander commander) {
        boolean configurationGood = true;

        try {
            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.RUNTIMEMODEL_INPUTPORT).isEmpty();
            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.REDEPLOYMENTMODEL_INPUTPORT)
                    .isEmpty();

            final File workingDirectory = new File(
                    configuration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY));
            configurationGood &= CommandLineParameterEvaluation.checkDirectory(workingDirectory,
                    "Runtime Model Directory", commander);

            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_NAME).isEmpty();

            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTION_HOSTNAME).isEmpty();
            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTION_PLAN_INPUTPORT).isEmpty();
            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTION_RUNTIMEMODEL_INPUTPORT)
                    .isEmpty();
            configurationGood &= !configuration
                    .getStringProperty(ConfigurationKeys.EXECUTION_REDEPLOYMENTMODEL_INPUTPORT).isEmpty();

            return configurationGood;
        } catch (final IOException e) {
            return false;
        }
    }

    @Override
    protected AdaptationConfiguration createTeetimeConfiguration() throws ConfigurationException {
        final int runtimeModelInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.RUNTIMEMODEL_INPUTPORT);
        final int redeploymentModelInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.REDEPLOYMENTMODEL_INPUTPORT);
        final File runtimeModelDirectory = new File(
                this.kiekerConfiguration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                AdaptationMain.RUNTIMEMODEL_DIRECTORY_NAME);
        final File redeploymentModelDirectory = new File(
                this.kiekerConfiguration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                AdaptationMain.REDEPLOYMENTMODEL_DIRECTORY_NAME);
        final File executionPlanURI = new File(
                this.kiekerConfiguration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                this.kiekerConfiguration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_NAME));
        final String executionHostname = this.kiekerConfiguration
                .getStringProperty(ConfigurationKeys.EXECUTION_HOSTNAME);
        final int executionPlanInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.EXECUTION_PLAN_INPUTPORT);
        final int executionRuntimeModelInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.EXECUTION_RUNTIMEMODEL_INPUTPORT);
        final int executionRedeploymentModelInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.EXECUTION_REDEPLOYMENTMODEL_INPUTPORT);

        runtimeModelDirectory.mkdir();
        redeploymentModelDirectory.mkdir();

        return new AdaptationConfiguration(runtimeModelInputPort, redeploymentModelInputPort, runtimeModelDirectory,
                redeploymentModelDirectory, executionPlanURI, executionHostname, executionPlanInputPort,
                executionRuntimeModelInputPort, executionRedeploymentModelInputPort);
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
        // no actions on shutdown
    }

    @Override
    protected File getConfigurationFile() {
        return this.parameterConfiguration.getConfigurationFile();
    }
}
