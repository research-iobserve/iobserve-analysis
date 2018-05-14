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
package org.iobserve.adaptation.cli;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import org.iobserve.adaptation.configurations.AdaptationConfiguration;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;

/**
 * Main class for iObserve's adaptation service.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationMain extends AbstractServiceMain<AdaptationConfiguration> {

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
        new AdaptationMain().run("Adaptation Service", "adaptation", args);
    }

    @Override
    protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
            final JCommander commander) {
        boolean configurationGood = true;

        try {
            configurationGood &= configuration.getStringProperty(ConfigurationKeys.RUNTIMEMODEL_INPUTPORT) != null;
            configurationGood &= configuration.getStringProperty(ConfigurationKeys.REDEPLOYMENTMODEL_INPUTPORT) != null;

            final File runtimeModelDirectory = new File(
                    configuration.getStringProperty(ConfigurationKeys.RUNTIMEMODEL_DIRECTORY));
            configurationGood &= CommandLineParameterEvaluation.checkDirectory(runtimeModelDirectory,
                    "Runtime Model Directory", commander);

            final File redeploymentModelDirectory = new File(
                    configuration.getStringProperty(ConfigurationKeys.REDEPLOYMENTMODEL_DIRECTORY));
            configurationGood &= CommandLineParameterEvaluation.checkDirectory(redeploymentModelDirectory,
                    "Redeployment Model Directory", commander);

            final File executionPlanURI = new File(
                    configuration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_URI));
            configurationGood &= CommandLineParameterEvaluation.isFileReadable(executionPlanURI,
                    "Correspondence Model File");

            configurationGood &= configuration.getStringProperty(ConfigurationKeys.EXECUTION_HOSTNAME) != null;
            configurationGood &= configuration.getStringProperty(ConfigurationKeys.EXECUTION_INPUTPORT) != null;

            return configurationGood;
        } catch (final IOException e) {
            return false;
        }
    }

    @Override
    protected AdaptationConfiguration createConfiguration(final kieker.common.configuration.Configuration configuration)
            throws ConfigurationException {
        final int runtimeModelInputPort = configuration.getIntProperty(ConfigurationKeys.RUNTIMEMODEL_INPUTPORT);
        final int redeploymentModelInputPort = configuration
                .getIntProperty(ConfigurationKeys.REDEPLOYMENTMODEL_INPUTPORT);
        final File runtimeModelDirectory = new File(
                configuration.getStringProperty(ConfigurationKeys.RUNTIMEMODEL_DIRECTORY));
        final File redeploymentModelDirectory = new File(
                configuration.getStringProperty(ConfigurationKeys.REDEPLOYMENTMODEL_DIRECTORY));
        final File executionPlanURI = new File(configuration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_URI));
        final String executionHostname = configuration.getStringProperty(ConfigurationKeys.EXECUTION_HOSTNAME);
        final int executionInputPort = configuration.getIntProperty(ConfigurationKeys.EXECUTION_INPUTPORT);

        return new AdaptationConfiguration(runtimeModelInputPort, redeploymentModelInputPort, runtimeModelDirectory,
                redeploymentModelDirectory, executionPlanURI, executionHostname, executionInputPort);
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
        // no actions on shutdown
    }

    @Override
    protected File getConfigurationFile() {
        return this.configurationFile;
    }

}
