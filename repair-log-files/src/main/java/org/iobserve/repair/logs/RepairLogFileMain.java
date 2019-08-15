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
package org.iobserve.repair.logs;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;

/**
 * @author Reiner Jung
 *
 */
public class RepairLogFileMain extends AbstractService<PipelineConfiguration, Settings> {

    /**
     * Create the log file repairer.
     */
    public RepairLogFileMain() {
        super();
    }

    /**
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        final RepairLogFileMain main = new RepairLogFileMain();
        System.exit(main.run("Repair Logs", "repair-logs", args, new Settings()));
    }

    @Override
    protected PipelineConfiguration createTeetimeConfiguration() throws ConfigurationException {
        try {
            return new PipelineConfiguration(this.parameterConfiguration.getInput());
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected File getConfigurationFile() {
        return null;
    }

    @Override
    protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
        return true;
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        return ParameterEvaluationUtils.isFileReadable(this.parameterConfiguration.getInput(), "Log input file",
                commander);
    }

    @Override
    protected void shutdownService() {
        // nothing special to be done here
    }

}
