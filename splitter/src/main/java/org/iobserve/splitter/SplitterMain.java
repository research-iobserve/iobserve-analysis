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
package org.iobserve.splitter;

import java.io.File;
import java.io.IOException;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.FileConverter;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.Execution;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class SplitterMain {

    private static final Log LOG = LogFactory.getLog(SplitterMain.class);

    @Parameter(names = { "-i",
            "--input" }, required = true, description = "Input directory.", converter = FileConverter.class)
    private File sourceLocation;

    @Parameter(names = { "-o",
            "--output" }, required = true, description = "Output directory.", converter = FileConverter.class)
    private File targetLocation;

    @Parameter(names = { "-H", "--hosts" }, required = true, description = "List of hosts.")
    private String[] hostnames;

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private SplitterMain() {

    }

    /**
     * Configure and execute the splitter.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {

        SplitterMain.LOG.debug("Splitter");

        final SplitterMain main = new SplitterMain();
        final JCommander commander = new JCommander(main);
        try {
            commander.parse(args);
            main.execute(commander);
        } catch (final ParameterException e) {
            SplitterMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        } catch (final IOException e) {
            SplitterMain.LOG.error(e.getLocalizedMessage());
            commander.usage();
        }
    }

    private void execute(final JCommander commander) throws IOException {
        this.checkDirectory(this.sourceLocation, "Source", commander);
        this.checkDirectory(this.targetLocation, "Target", commander);

        final SimpleSplitterConfiguration configuration = new SimpleSplitterConfiguration(this.sourceLocation,
                this.targetLocation, this.hostnames);
        final Execution<SimpleSplitterConfiguration> analysis = new Execution<>(configuration);

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

        SplitterMain.LOG.debug("Running analysis");

        analysis.executeBlocking();

        SplitterMain.LOG.debug("Done");

    }

    private void checkDirectory(final File location, final String locationLabel, final JCommander commander)
            throws IOException {
        if (!location.exists()) {
            SplitterMain.LOG.error(locationLabel + " path " + location.getCanonicalPath() + " does not exist.");
            commander.usage();
            System.exit(1);
        }
        if (!location.isDirectory()) {
            SplitterMain.LOG.error(locationLabel + " path " + location.getCanonicalPath() + " is not a directory.");
            commander.usage();
            System.exit(1);
        }

    }

}
