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
package org.iobserve.planning.service;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import org.iobserve.planning.configurations.PlanningConfiguration;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;

/**
 * Main class for iObserve's planning service.
 *
 * @author Lars Bluemke
 *
 */
public class PlanningMain extends AbstractServiceMain<PlanningConfiguration> {
    protected static final String RUNTIMEMODEL_DIRECTORY_NAME = "runtimemodel";
    protected static final String REDEPLOYMENTMODEL_DIRECTORY_NAME = "redeploymentmodel";

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
        new PlanningMain().run("Planning Service", "planning", args);
    }

    @Override
    protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
            final JCommander commander) {
        boolean configurationGood = true;

        try {
            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.RUNTIMEMODEL_INPUTPORT).isEmpty();

            final File runtimeModelDirectory = new File(
                    configuration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY));
            configurationGood &= CommandLineParameterEvaluation.checkDirectory(runtimeModelDirectory,
                    "Runtime Model Directory", commander);

            final File perOpteryxHeadlessDir = new File(
                    configuration.getStringProperty(ConfigurationKeys.PEROPTERYX_HEADLESS_DIRECTORY));
            configurationGood &= CommandLineParameterEvaluation.checkDirectory(perOpteryxHeadlessDir,
                    "PerOpteryx RCP Executable Directory", commander);

            // LQNS directory is not mandatory
            if (!configuration.getStringProperty(ConfigurationKeys.LQNS_DIRECTORY).isEmpty()) {
                final File lqnsDir = new File(configuration.getStringProperty(ConfigurationKeys.LQNS_DIRECTORY));
                configurationGood &= CommandLineParameterEvaluation.checkDirectory(lqnsDir, "LQNS Executable Directory",
                        commander);
            }

            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.ADAPTATION_HOSTNAME).isEmpty();
            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.ADAPTATION_RUNTIMEMODEL_INPUTPORT)
                    .isEmpty();
            configurationGood &= !configuration
                    .getStringProperty(ConfigurationKeys.ADAPTATION_REDEPLOYMENTMODEL_INPUTPORT).isEmpty();

            return configurationGood;
        } catch (final IOException e) {
            return false;
        }
    }

    @Override
    protected PlanningConfiguration createConfiguration(final kieker.common.configuration.Configuration configuration)
            throws ConfigurationException {
        final int runtimeModelInputPort = configuration.getIntProperty(ConfigurationKeys.RUNTIMEMODEL_INPUTPORT);
        final File runtimeModelDirectory = new File(
                configuration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY),
                PlanningMain.RUNTIMEMODEL_DIRECTORY_NAME);
        final File perOpteryxHeadlessDir = new File(
                configuration.getStringProperty(ConfigurationKeys.PEROPTERYX_HEADLESS_DIRECTORY));
        final File lqnsDir = new File(configuration.getStringProperty(ConfigurationKeys.LQNS_DIRECTORY));
        final String adaptationHostname = configuration.getStringProperty(ConfigurationKeys.ADAPTATION_HOSTNAME);
        final int adaptationRuntimeModelInputPort = configuration
                .getIntProperty(ConfigurationKeys.ADAPTATION_RUNTIMEMODEL_INPUTPORT);
        final int adaptationRedeploymentModelInputPort = configuration
                .getIntProperty(ConfigurationKeys.ADAPTATION_REDEPLOYMENTMODEL_INPUTPORT);

        return new PlanningConfiguration(runtimeModelInputPort, runtimeModelDirectory, perOpteryxHeadlessDir, lqnsDir,
                adaptationHostname, adaptationRuntimeModelInputPort, adaptationRedeploymentModelInputPort);
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
