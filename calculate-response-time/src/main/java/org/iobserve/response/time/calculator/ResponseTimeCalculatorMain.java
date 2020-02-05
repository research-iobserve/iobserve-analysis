/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.response.time.calculator;

import java.io.File;
import java.io.FileNotFoundException;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;

import org.iobserve.stages.sink.CSVFileWriter;

/**
 * @author Reiner Jung
 *
 */
public class ResponseTimeCalculatorMain
        extends AbstractService<PiplineConfiguration, Settings> {

    private static final String OUTPUT_FILE = CSVFileWriter.class.getCanonicalName() + ".outputFile";

    /**
     * @param args
     */
    public static void main(final String[] args) {
        java.lang.System.exit(new ResponseTimeCalculatorMain().run("Calculate response time", "response-time", args,
                new Settings()));
    }

    @Override
    protected PiplineConfiguration createTeetimeConfiguration() throws ConfigurationException {
        try {
            return new PiplineConfiguration(this.kiekerConfiguration, this.parameterConfiguration);
        } catch (final FileNotFoundException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected File getConfigurationFile() {
        return this.parameterConfiguration.getConfigurationFile();
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        this.parameterConfiguration.setOutputFile(
                new File(configuration.getStringProperty(ResponseTimeCalculatorMain.OUTPUT_FILE, "result.csv")));
        return ParameterEvaluationUtils.checkDirectory(this.parameterConfiguration.getOutputFile().getParentFile(),
                "output file", commander);
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        return ParameterEvaluationUtils.isFileReadable(this.parameterConfiguration.getConfigurationFile(),
                "configuration file", commander);
    }

    @Override
    protected void shutdownService() {

    }

}
