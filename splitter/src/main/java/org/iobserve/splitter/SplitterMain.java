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

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import teetime.framework.Execution;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class SplitterMain {

    private static final String DATA_DIR_OPTION = "data";
    private static final String INPUT_PORT_OPTION = "port";
    private static final String DATA_DIR_OPTION_SHORT = "d";
    private static final String INPUT_PORT_OPTION_SHORT = "p";

    /**
     * This is a simple main class which does not need to be instantiated.
     */
    private SplitterMain() {

    }

    /**
     * Configure and execute the TCP Kieker data collector.
     *
     * @param args
     *            arguments are ignored
     */
    public static void main(final String[] args) {
        // final CommandLineParser parser = new DefaultParser();
        // try {
        System.out.println("Receiver");
        final SimpleSplitterConfiguration configuration = new SimpleSplitterConfiguration(args[0], args[1], args[2],
                args[3], args[4], args[5], args[6]);
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

        System.out.println("Running analysis");

        analysis.executeBlocking();

        System.out.println("Done");
        // } catch (final ParseException exp) {
        // System.err.println("CLI error: " + exp.getMessage());
        // final HelpFormatter formatter = new HelpFormatter();
        // formatter.printHelp("collector", SplitterMain.createOptions());
        // }
    }

    /**
     * Create the command line parameter setup.
     *
     * @return options for the command line parser
     */
    private static Options createOptions() {
        final Options options = new Options();

        options.addOption(Option.builder(SplitterMain.INPUT_PORT_OPTION_SHORT).required(true)
                .longOpt(SplitterMain.INPUT_PORT_OPTION).hasArg().desc("input TCP port").build());
        options.addOption(Option.builder(SplitterMain.DATA_DIR_OPTION_SHORT).required(true)
                .longOpt(SplitterMain.DATA_DIR_OPTION).hasArg().desc("Kieker directory location").build());

        /** help */
        options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

        return options;
    }

}
