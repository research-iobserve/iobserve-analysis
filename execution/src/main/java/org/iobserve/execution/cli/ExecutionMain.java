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
package org.iobserve.execution.cli;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.configuration.Configuration;

import org.eclipse.emf.common.util.URI;
import org.iobserve.execution.configurations.KubernetesExecutionConfiguration;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.provider.file.CorrespondenceModelHandler;
import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;

/**
 * Main class for iObserve's execution service.
 *
 * @author Lars Bluemke
 *
 */
public class ExecutionMain extends AbstractServiceMain<KubernetesExecutionConfiguration> {

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
        new ExecutionMain().run("Execution Service", "execution", args);
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        boolean configurationGood = true;

        try {
            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_INPUTPORT).isEmpty();

            final File executionPlanDirectory = new File(
                    configuration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_DIRECTORY));
            configurationGood &= CommandLineParameterEvaluation.checkDirectory(executionPlanDirectory,
                    "Executionplan Directory", commander);

            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_FILENAME).isEmpty();
            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.KUBERNETES_MASTER_IP).isEmpty();
            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.KUBERNETES_MASTER_PORT).isEmpty();

            final File correspondenceModelFile = new File(
                    configuration.getStringProperty(ConfigurationKeys.CORRESPONDENCE_MODEL_URI));
            configurationGood &= CommandLineParameterEvaluation.isFileReadable(correspondenceModelFile,
                    "Correspondence Model File");

            configurationGood &= !configuration.getStringProperty(ConfigurationKeys.IMAGE_PREFIX).isEmpty();

            return configurationGood;
        } catch (final IOException e) {
            return false;
        }
    }

    @Override
    protected KubernetesExecutionConfiguration createConfiguration(final Configuration configuration)
            throws ConfigurationException {
        final int executionPlanInputPort = configuration.getIntProperty(ConfigurationKeys.EXECUTIONPLAN_INPUTPORT);
        final File executionPlanDirectory = new File(
                configuration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_DIRECTORY));
        final String executionPlanName = configuration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_FILENAME);
        final String kubernetesMasterIp = configuration.getStringProperty(ConfigurationKeys.KUBERNETES_MASTER_IP);
        final String kubernetesMasterPort = configuration.getStringProperty(ConfigurationKeys.KUBERNETES_MASTER_PORT);
        final CorrespondenceModel correspondenceModel = new CorrespondenceModelHandler()
                .load(URI.createFileURI(configuration.getStringProperty(ConfigurationKeys.CORRESPONDENCE_MODEL_URI)));
        final String imagePrefix = configuration.getStringProperty(ConfigurationKeys.IMAGE_PREFIX);

        return new KubernetesExecutionConfiguration(executionPlanInputPort, executionPlanDirectory, executionPlanName,
                kubernetesMasterIp, kubernetesMasterPort, correspondenceModel, imagePrefix);
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
