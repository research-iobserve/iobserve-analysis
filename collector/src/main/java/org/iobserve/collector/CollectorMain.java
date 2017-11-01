/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.collector;

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
public final class CollectorMain {
    private static final Log LOG = LogFactory.getLog(CollectorMain.class);

    @Parameter(names = { "-d",
            "--data" }, required = true, description = "Output data directory.", converter = FileConverter.class)
    private File dataLocation;

    @Parameter(names = { "-p",
            "--port" }, required = true, description = "Input port.", converter = IntegerConverter.class)
    private Integer inputPort;

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private CollectorMain() {

    }

    /**
     * Configure and execute the TCP Kieker data collector.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        final CollectorMain main = new CollectorMain();
        final JCommander commander = new JCommander(main);
        try {
            commander.parse(args);
            main.execute(commander);
        } catch (final ParameterException e) {
            CollectorMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        } catch (final IOException e) {
            CollectorMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) throws IOException {
        this.checkDirectory(this.dataLocation, "Output Kieker directory", commander);

        CollectorMain.LOG.debug("Receiver");
        final SimpleBridgeConfiguration configuration = new SimpleBridgeConfiguration(
                this.dataLocation.getCanonicalPath(), this.inputPort);
        final Execution<SimpleBridgeConfiguration> analysis = new Execution<>(configuration);

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

        CollectorMain.LOG.debug("Running analysis");

        analysis.executeBlocking();

        CollectorMain.LOG.debug("Counts " + configuration.getCounter().getCount());

        CollectorMain.LOG.debug("Done");

    }

    private void checkDirectory(final File location, final String locationLabel, final JCommander commander)
            throws IOException {
        if (!location.exists()) {
            CollectorMain.LOG.error(locationLabel + " path " + location.getCanonicalPath() + " does not exist.");
            commander.usage();
            System.exit(1);
        }
        if (!location.isDirectory()) {
            CollectorMain.LOG.error(locationLabel + " path " + location.getCanonicalPath() + " is not a directory.");
            commander.usage();
            System.exit(1);
        }

    }
}
