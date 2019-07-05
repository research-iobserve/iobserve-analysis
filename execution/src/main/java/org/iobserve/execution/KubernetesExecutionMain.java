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
package org.iobserve.execution;

import java.io.File;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;

import org.iobserve.execution.configurations.KubernetesExecutionConfiguration;

/**
 * Main class for iObserve's execution service.
 *
 * @author Lars Bluemke
 *
 */
public class KubernetesExecutionMain
        extends AbstractService<KubernetesExecutionConfiguration, KubernetesExecutionMain> {
    private static final String RUNTIMEMODEL_DIRECTORY_NAME = "runtimemodel";
    private static final String REDEPLOYMENTMODEL_DIRECTORY_NAME = "redeploymentmodel";

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
        final KubernetesExecutionMain main = new KubernetesExecutionMain();
        System.exit(main.run("Execution Service", "execution", args, main));
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        boolean configurationGood = true;

        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.EXECUTIONPLAN_INPUTPORT).isEmpty();
        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.RUNTIMEMODEL_INPUTPORT).isEmpty();
        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.REDEPLOYMENTMODEL_INPUTPORT).isEmpty();

        final File workingDirectory = new File(configuration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY));
        configurationGood &= ParameterEvaluationUtils.checkDirectory(workingDirectory, "Executionplan Directory",
                commander);
        final File correspondenceModelFile = new File(workingDirectory,
                configuration.getStringProperty(ConfigurationKeys.CORRESPONDENCEMODEL_NAME));
        configurationGood &= ParameterEvaluationUtils.isFileReadable(correspondenceModelFile,
                "Correspondence Model File", commander);

        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.APP_IMAGE_LOCATOR).isEmpty();
        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.KUBERNETES_NAMESPACE).isEmpty();
        configurationGood &= !configuration.getStringProperty(ConfigurationKeys.APP_SUBDOMAIN).isEmpty();

        return configurationGood;
    }

    @Override
    protected KubernetesExecutionConfiguration createTeetimeConfiguration() throws ConfigurationException {
        final int executionPlanInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.EXECUTIONPLAN_INPUTPORT);
        final int runtimeModelInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.RUNTIMEMODEL_INPUTPORT);
        final int redeploymentModelInputPort = this.kiekerConfiguration
                .getIntProperty(ConfigurationKeys.REDEPLOYMENTMODEL_INPUTPORT);
        final File workingDirectory = new File(
                this.kiekerConfiguration.getStringProperty(ConfigurationKeys.WORKING_DIRECTORY));
        final File runtimeModelDirectory = new File(workingDirectory,
                KubernetesExecutionMain.RUNTIMEMODEL_DIRECTORY_NAME);
        final File redeploymentModelDirectory = new File(workingDirectory,
                KubernetesExecutionMain.REDEPLOYMENTMODEL_DIRECTORY_NAME);
        final File correspondenceModelFile = new File(workingDirectory,
                this.kiekerConfiguration.getStringProperty(ConfigurationKeys.CORRESPONDENCEMODEL_NAME));
        final String imageLocator = this.kiekerConfiguration.getStringProperty(ConfigurationKeys.APP_IMAGE_LOCATOR);
        final String subdomain = this.kiekerConfiguration.getStringProperty(ConfigurationKeys.APP_SUBDOMAIN);
        final String namespace = this.kiekerConfiguration.getStringProperty(ConfigurationKeys.KUBERNETES_NAMESPACE);

        runtimeModelDirectory.mkdir();
        redeploymentModelDirectory.mkdir();

        return new KubernetesExecutionConfiguration(executionPlanInputPort, runtimeModelInputPort,
                redeploymentModelInputPort, workingDirectory, runtimeModelDirectory, redeploymentModelDirectory,
                correspondenceModelFile, imageLocator, subdomain, namespace);
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
