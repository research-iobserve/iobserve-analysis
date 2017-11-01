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
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.IntegerConverter;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.Execution;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class ReplayerMain {

    private static final Log LOG = LogFactory.getLog(ReplayerMain.class);

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Input data directory.", converter = FileConverter.class)
    private File dataLocation;

    @Parameter(names = { "-p",
            "--port" }, required = true, description = "Output port.", converter = IntegerConverter.class)
    private Integer outputPort;

    @Parameter(names = { "-h",
            "--host" }, required = true, description = "Name or IP address of the host where the data is send to.")
    private String hostname;

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
        final JCommander commander = new JCommander(main);
        try {
            commander.parse(args);
            main.execute(commander);
        } catch (final ParameterException e) {
            ReplayerMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        } catch (final IOException e) {
            ReplayerMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) throws IOException {
        this.checkDirectory(this.dataLocation, "Output Kieker directory", commander);

        ReplayerMain.LOG.debug("Receiver");
        final ReplayerConfiguration configuration = new ReplayerConfiguration(this.dataLocation, this.hostname,
                this.outputPort);

        if (configuration.isOutputConnected()) {

            final Execution<ReplayerConfiguration> analysis = new Execution<>(configuration);

            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (analysis) {
                            analysis.abortEventually();
                        }
                    } catch (final Exception e) { // NOCS

                    }
                }
            }));

            ReplayerMain.LOG.info("Running analysis");

            analysis.executeBlocking();

            ReplayerMain.LOG.info("Records send " + configuration.getCounter().getCount());

            ReplayerMain.LOG.info("Done");
        } else {
            ReplayerMain.LOG.info("Cannot connect to host.");
        }

    }

    private void checkDirectory(final File location, final String locationLabel, final JCommander commander)
            throws IOException {
        if (!location.exists()) {
            ReplayerMain.LOG.error(locationLabel + " path " + location.getCanonicalPath() + " does not exist.");
            commander.usage();
            System.exit(1);
        }
        if (!location.isDirectory()) {
            ReplayerMain.LOG.error(locationLabel + " path " + location.getCanonicalPath() + " is not a directory.");
            commander.usage();
            System.exit(1);
        }

    }
}
