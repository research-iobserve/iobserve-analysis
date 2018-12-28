/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.replayer;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;

import kieker.analysis.common.ConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.tools.common.AbstractService;

import org.iobserve.service.CommandLineParameterEvaluation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class ReplayerMain extends AbstractService<ReplayerTeetimeConfiguration, ReplayerParameter> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplayerMain.class);

    private ReplayerTeetimeConfiguration configuration;
    private final ReplayerParameter parameter = new ReplayerParameter();

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private ReplayerMain() {

    }

    /**
     * Configure and execute the TCP Kieker data collector.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        final ReplayerMain main = new ReplayerMain();
        System.exit(main.run("Replayer", "replayer", args));
    }

    /**
     * Runner method.
     *
     * @param title
     *            application title
     * @param label
     *            logging label
     * @param args
     *            command line arguments
     * @return returns exit code
     */
    public int run(final String title, final String label, final String[] args) {
        final int result = super.run(title, label, args, this.parameter);
        if (this.configuration != null) {
            ReplayerMain.LOGGER.info("Records send {}", this.configuration.getCounter().getCount());
        }
        return result;
    }

    @Override
    protected ReplayerTeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
        this.configuration = new ReplayerTeetimeConfiguration(this.parameter);
        return this.configuration;
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            return CommandLineParameterEvaluation.checkDirectory(this.parameter.getDataLocation(),
                    "Output Kieker directory", commander);
        } catch (final IOException e) {
            throw new ConfigurationException(e);
        }
    }

    @Override
    protected File getConfigurationFile() {
        return null;
    }

    @Override
    protected boolean checkConfiguration(final Configuration kiekerConfiguration, final JCommander commander) {
        return true;
    }

    @Override
    protected void shutdownService() {
        // nothing special to shutdown
    }

}
