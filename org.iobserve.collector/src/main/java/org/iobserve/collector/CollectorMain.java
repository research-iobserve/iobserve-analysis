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

import teetime.framework.Execution;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 */
public final class CollectorMain {

    private static final String DATA_DIR_OPTION = "data";
    private static final String INPUT_PORT_OPTION = "port";
    private static final String DATA_DIR_OPTION_SHORT = "d";
    private static final String INPUT_PORT_OPTION_SHORT = "p";

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
        final CommandLineParser parser = new DefaultParser();
        try {
            final CommandLine commandLine = parser.parse(CollectorMain.createOptions(), args);
            final String dataLocation = commandLine.getOptionValue(CollectorMain.DATA_DIR_OPTION);
            final int inputPort = Integer.parseInt(commandLine.getOptionValue(CollectorMain.INPUT_PORT_OPTION));
            System.out.println("Receiver");
            final SimpleBridgeConfiguration configuration = new SimpleBridgeConfiguration(dataLocation, inputPort);
            final Execution<SimpleBridgeConfiguration> analysis = new Execution<>(configuration);

            System.out.println("Running analysis");

            analysis.executeBlocking();

            System.out.println("Counts " + configuration.getCounter().getCount());

            System.out.println("Done");
        } catch (final ParseException exp) {
            System.err.println("CLI error: " + exp.getMessage());
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("collector", CollectorMain.createOptions());
        }
    }

    /**
     * Create the command line parameter setup.
     *
     * @return options for the command line parser
     */
    private static Options createOptions() {
        final Options options = new Options();

        options.addOption(Option.builder(CollectorMain.INPUT_PORT_OPTION_SHORT).required(true)
                .longOpt(CollectorMain.INPUT_PORT_OPTION).hasArg().desc("input TCP port").build());
        options.addOption(Option.builder(CollectorMain.DATA_DIR_OPTION_SHORT).required(true)
                .longOpt(CollectorMain.DATA_DIR_OPTION).hasArg().desc("Kieker directory location").build());

        /** help */
        options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

        return options;
    }

}
