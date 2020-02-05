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
package org.iobserve.resources.calculator;

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
public class ResourcesCalculatorMain extends AbstractService<TeetimePipline, Settings> {

    private static final String CPU_UTILIZATION_OUTPUT_FILE = CSVFileWriter.class.getCanonicalName()
            + ".outputCpuUtilizationFile";
    private static final String MEM_UTILIZATION_OUTPUT_FILE = CSVFileWriter.class.getCanonicalName()
            + ".outputMemUtilizationFile";

    /**
     * @param args
     */
    public static void main(final String[] args) {
        java.lang.System.exit(
                new ResourcesCalculatorMain().run("Calculate response time", "response-time", args, new Settings()));
    }

    @Override
    protected TeetimePipline createTeetimeConfiguration() throws ConfigurationException {
        try {
            return new TeetimePipline(this.kiekerConfiguration, this.parameterConfiguration);
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
        this.parameterConfiguration.setCpuUtilizationOutputFile(new File(configuration
                .getStringProperty(ResourcesCalculatorMain.CPU_UTILIZATION_OUTPUT_FILE, "cpu-result.csv")));
        this.parameterConfiguration.setMemUtilizationOutputFile(new File(configuration
                .getStringProperty(ResourcesCalculatorMain.MEM_UTILIZATION_OUTPUT_FILE, "mem-result.csv")));

        return ParameterEvaluationUtils.checkDirectory(
                this.parameterConfiguration.getCpuUtilizationOutputFile().getParentFile(),
                "cpu utilization output file", commander)
                && ParameterEvaluationUtils.checkDirectory(
                        this.parameterConfiguration.getMemUtilizationOutputFile().getParentFile(),
                        "mem utilization output file", commander);
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
