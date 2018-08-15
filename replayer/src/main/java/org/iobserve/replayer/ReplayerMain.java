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
import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

import kieker.common.configuration.Configuration;

import org.iobserve.service.AbstractServiceMain;
import org.iobserve.service.CommandLineParameterEvaluation;
import org.iobserve.stages.general.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class ReplayerMain extends AbstractServiceMain<ReplayerConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReplayerMain.class);

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Input data directory.", converter = FileConverter.class)
    private File dataLocation;

    @Parameter(names = { "-p",
            "--port" }, required = true, description = "Output port.", converter = IntegerConverter.class)
    private Integer outputPort;

    @Parameter(names = { "-h",
            "--host" }, required = true, description = "Name or IP address of the host where the data is send to.")
    private String hostname;

    private ReplayerConfiguration configuration;

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
        new ReplayerMain().run("Replayer", "replayer", args);
    }

    @Override
    public void run(final String title, final String label, final String[] args) {
        super.run(title, label, args);
        if (this.configuration != null) {
            ReplayerMain.LOGGER.info("Records send {}", this.configuration.getCounter().getCount());
        }
    }

    @Override
    protected ReplayerConfiguration createConfiguration(final Configuration kiekerConfiguration)
            throws ConfigurationException {
        this.configuration = new ReplayerConfiguration(this.dataLocation, this.hostname, this.outputPort);
        return this.configuration;
    }

    @Override
    protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
        try {
            return CommandLineParameterEvaluation.checkDirectory(this.dataLocation, "Output Kieker directory",
                    commander);
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
