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

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;

import org.iobserve.adaptation.configurations.AdaptationConfigurationMockup;

/**
 * A main class for mocking iObserve's adaptation service.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationMainMockup extends AbstractService<AdaptationConfigurationMockup, AdaptationSettings> {
    @Parameter(names = "--help", help = true)
    private boolean help; // NOPMD access through reflection

    @Parameter(names = { "-c",
            "--configuration" }, required = true, description = "Configuration file.", converter = FileConverter.class)
    private File configurationFile;

    /**
     * Main function.
     *
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        System.exit(new AdaptationMainMockup().run("Adaptation Service", "adaptation", args, new AdaptationSettings()));
    }

    @Override
    protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
            final JCommander commander) {
        boolean configurationGood = true;

        final File executionPlan = new File(configuration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                configuration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_NAME));
        configurationGood &= ParameterEvaluationUtils.isFileReadable(executionPlan, "Execution Plan File", commander);

        final File runtimeModelDirectory = new File(
                configuration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                AdaptationMain.RUNTIMEMODEL_DIRECTORY_NAME);
        configurationGood &= ParameterEvaluationUtils.checkDirectory(runtimeModelDirectory, "Runtimemodel Directory",
                commander);

        final File redeploymentModelDirectory = new File(
                configuration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                AdaptationMain.REDEPLOYMENTMODEL_DIRECTORY_NAME);
        configurationGood &= ParameterEvaluationUtils.checkDirectory(redeploymentModelDirectory,
                "Redeploymentmodel Directory", commander);

        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTION_HOSTNAME).isEmpty();
        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTION_PLAN_INPUTPORT).isEmpty();
        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTION_RUNTIMEMODEL_INPUTPORT)
                .isEmpty();
        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTION_REDEPLOYMENTMODEL_INPUTPORT)
                .isEmpty();

        return configurationGood;
    }

    @Override
    protected AdaptationConfigurationMockup createTeetimeConfiguration() throws ConfigurationException {
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
        final int executionInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.EXECUTION_PLAN_INPUTPORT);
        final int executionRuntimeModelInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.EXECUTION_RUNTIMEMODEL_INPUTPORT);
        final int executionRedeploymentModelInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.EXECUTION_REDEPLOYMENTMODEL_INPUTPORT);

        return new AdaptationConfigurationMockup(runtimeModelDirectory, redeploymentModelDirectory, executionPlanURI,
                executionHostname, executionInputPort, executionRuntimeModelInputPort,
                executionRedeploymentModelInputPort);
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        return ParameterEvaluationUtils.isFileReadable(this.configurationFile, "Configuration File", commander);
    }

    @Override
    protected void shutdownService() {
        // no actions on shutdown
    }

    @Override
    protected File getConfigurationFile() {
        return this.configurationFile;
    }
}
