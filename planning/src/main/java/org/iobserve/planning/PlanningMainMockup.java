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
package org.iobserve.planning;

import java.io.File;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;

import org.iobserve.planning.configurations.PlanningConfigurationMockup;

/**
 * A main class for mocking iObserve's planning service.
 *
 * @author Lars Bluemke
 *
 */
public class PlanningMainMockup extends AbstractService<PlanningConfigurationMockup, PlanningMainMockup> {

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
        final PlanningMainMockup main = new PlanningMainMockup();
        System.exit(main.run("Planning Service", "planning", args, main));
    }

    @Override
    protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
            final JCommander commander) {
        boolean configurationGood = true;

        final File runtimeModelDirectory = new File(
                configuration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                PlanningMain.RUNTIMEMODEL_DIRECTORY_NAME);
        configurationGood &= ParameterEvaluationUtils.checkDirectory(runtimeModelDirectory, "Runtime Model Directory",
                commander);

        final File redeploymentModelDirectory = new File(
                configuration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                PlanningMain.REDEPLOYMENTMODEL_DIRECTORY_NAME);
        configurationGood &= ParameterEvaluationUtils.checkDirectory(redeploymentModelDirectory,
                "Redeployment Model Directory", commander);

        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.ADAPTATION_HOSTNAME).isEmpty();
        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.ADAPTATION_RUNTIMEMODEL_INPUTPORT)
                .isEmpty();
        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.ADAPTATION_REDEPLOYMENTMODEL_INPUTPORT)
                .isEmpty();

        return configurationGood;
    }

    @Override
    protected PlanningConfigurationMockup createTeetimeConfiguration() throws ConfigurationException {
        final File runtimeModelDirectory = new File(
                this.kiekerConfiguration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                PlanningMain.RUNTIMEMODEL_DIRECTORY_NAME);
        final File redeploymentModelDirectory = new File(
                this.kiekerConfiguration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                PlanningMain.REDEPLOYMENTMODEL_DIRECTORY_NAME);
        final String adaptationHostname = this.kiekerConfiguration
                .getStringProperty(ConfigurationKeys.ADAPTATION_HOSTNAME);
        final int adaptationRuntimeModelInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.ADAPTATION_RUNTIMEMODEL_INPUTPORT);
        final int adaptationRedeploymentModelInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.ADAPTATION_REDEPLOYMENTMODEL_INPUTPORT);

        return new PlanningConfigurationMockup(runtimeModelDirectory, redeploymentModelDirectory, adaptationHostname,
                adaptationRuntimeModelInputPort, adaptationRedeploymentModelInputPort);
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
