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
package org.iobserve.evaluate.jss;

import java.io.File;
import java.io.FileNotFoundException;

import com.beust.jcommander.JCommander;

import kieker.analysis.common.ConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluation;

/**
 * Evaluate iobserve logs.
 *
 * @author Reiner Jung
 *
 */
public class EvaluateMain extends AbstractService<EvaluationTeetimeConfiguration, EvaluationParamterConfiguration> {

    private static final String OUTPUT_FILE = EvaluateMain.class.getCanonicalName() + ".outputFile";

    @Override
    protected EvaluationTeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
        try {
            return new EvaluationTeetimeConfiguration(this.kiekerConfiguration, this.parameterConfiguration);
        } catch (final FileNotFoundException e) {
            throw new ConfigurationException(e);
        }
    }

    /**
     * Configure and execute the TCP Kieker data collector.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        java.lang.System.exit(new EvaluateMain().run("JSS Evaluation Service", "privacy", args,
                new EvaluationParamterConfiguration()));
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        this.parameterConfiguration
                .setOutputFile(new File(configuration.getStringProperty(EvaluateMain.OUTPUT_FILE, "result.csv")));
        this.parameterConfiguration.setSourceClassName(configuration.getStringProperty("iobserve.analysis.source"));

        return true;
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        return ParameterEvaluation.isFileReadable(this.parameterConfiguration.getConfigurationFile(),
                "configuration file", commander);
    }

    @Override
    protected File getConfigurationFile() {
        return this.parameterConfiguration.getConfigurationFile();
    }

    @Override
    protected void shutdownService() {

    }

}
