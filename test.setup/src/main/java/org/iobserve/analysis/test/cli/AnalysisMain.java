/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.test.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.iobserve.analysis.utils.ExecutionTimeLogger;

import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * Main class for starting the iObserve application.
 *
 * @author Reiner Jung
 */
public final class AnalysisMain {

    /**
     * Default constructor.
     */
    private AnalysisMain() {
        // do nothing here
    }

    /**
     * Main function.
     *
     * @param args
     *            command line arguments.
     */
    public static void main(final String[] args) {
        final CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(AnalysisMain.createHelpOptions(), args);

            if (commandLine.hasOption("h")) {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("iobserve-analysis", AnalysisMain.createOptions());
            } else {
                commandLine = parser.parse(AnalysisMain.createOptions(), args);

                /** process parameter. */
                final File monitoringDataDirectory = new File(commandLine.getOptionValue("i"));
                final File outputLocation = new File(commandLine.getOptionValue("o"));

                if (outputLocation.isDirectory()) {
                    if (monitoringDataDirectory.isDirectory()) {
                        /** create and run application */
                        final Collection<File> monitoringDataDirectories = new ArrayList<>();
                        AnalysisMain.findDirectories(monitoringDataDirectory.listFiles(), monitoringDataDirectories);

                        final Configuration configuration = new ObservationConfiguration(monitoringDataDirectories,
                                outputLocation);

                        System.out.println("Analysis configuration");
                        final Execution<Configuration> analysis = new Execution<>(configuration);
                        System.out.println("Analysis start");
                        analysis.executeBlocking();
                        System.out.println("Anaylsis complete");
                        ExecutionTimeLogger.getInstance().exportAsCsv();
                    } else {
                        System.err.println(
                                "CLI error: Input path " + monitoringDataDirectory.getName() + " is not a directory.");
                    }
                } else {
                    System.err.println("CLI error: Output path " + outputLocation.getName() + " is not a directory.");
                }
            }
        } catch (final ParseException exp) {
            System.err.println("CLI error: " + exp.getMessage());
            final HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("iobserve-analysis", AnalysisMain.createOptions());
        }
    }

    private static void findDirectories(final File[] listFiles, final Collection<File> monitoringDataDirectories) {
        for (final File file : listFiles) {
            if (file.isDirectory()) {
                monitoringDataDirectories.add(file);
                AnalysisMain.findDirectories(file.listFiles(), monitoringDataDirectories);
            }
        }
    }

    /**
     * Create the command line parameter setup.
     *
     * @return options for the command line parser
     */
    private static Options createOptions() {
        final Options options = new Options();

        options.addOption(Option.builder("i").required(true).longOpt("input").hasArg()
                .desc("a Kieker logfile directory").build());
        options.addOption(
                Option.builder("o").required(true).longOpt("output").hasArg().desc("the output directory").build());
        /** help */
        options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

        return options;
    }

    /**
     * Create a command line setup with only the help option.
     *
     * @return returns simplified options
     */
    private static Options createHelpOptions() {
        final Options options = new Options();

        options.addOption(Option.builder("i").required(false).longOpt("input").hasArg()
                .desc("a Kieker logfile directory").build());
        options.addOption(
                Option.builder("o").required(false).longOpt("output").hasArg().desc("the output directory").build());
        /** help */
        options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

        return options;
    }
}
