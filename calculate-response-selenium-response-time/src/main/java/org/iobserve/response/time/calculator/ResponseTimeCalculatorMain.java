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
import kieker.tools.common.ParameterEvaluation;

/**
 * @author Reiner Jung
 *
 */
public class ResponseTimeCalculatorMain
        extends AbstractService<ResponseTimeConfiguration, ResponseTimeCalculatorSettings> {

    /**
     * @param args
     */
    public static void main(final String[] args) {
        java.lang.System.exit(new ResponseTimeCalculatorMain().run("Calculate response time", "response-time", args,
                new ResponseTimeCalculatorSettings()));
    }

    @Override
    protected ResponseTimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
        try {
            return new ResponseTimeConfiguration(this.kiekerConfiguration, this.parameterConfiguration);
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
        return true;
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        return ParameterEvaluation.isFileReadable(this.parameterConfiguration.getConfigurationFile(),
                "configuration file", commander);
    }

    @Override
    protected void shutdownService() {

    }

}
