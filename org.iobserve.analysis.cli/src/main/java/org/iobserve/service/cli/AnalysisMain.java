/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.service.cli;

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

/**
 * Main class for starting the iObserve application.
 *
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public class AnalysisMain {

	/**
	 * Default constructor.
	 */
	private AnalysisMain() {
		// do nothing here
	}


	/**
	 * Main function.
	 *
	 * @param args command line arguments.
	 */
	public static void main(final String[] args) {
		final CommandLineParser parser = new DefaultParser();
		try {
			CommandLine commandLine = parser.parse(createHelpOptions(), args);

			if (commandLine.hasOption("h")) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp( "iobserve-analysis", createOptions() );
			} else {
				commandLine = parser.parse(createOptions(), args);

				final File monitoringDataDirectory = new File(commandLine.getOptionValue("i"));
				if (monitoringDataDirectory.isDirectory()) {
					final String correspondenceMappingFile = commandLine.getOptionValue("c");
					final String pcmModelsDirectory = commandLine.getOptionValue("p");

					/** create and run application */
					Collection<File> monitoringDataDirectories = new ArrayList<File>();
					findDirectories(monitoringDataDirectory.listFiles(), monitoringDataDirectories);
					
					final AnalysisExecution application = new AnalysisExecution(monitoringDataDirectories,
							correspondenceMappingFile, pcmModelsDirectory);
					application.run();
				} else {
					System.err.println("CLI error: " + monitoringDataDirectory.getName() + " is not a directory.");
				}
			}
		} catch(final ParseException exp) {
			System.err.println("CLI error: " + exp.getMessage());
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("iobserve-analysis", createOptions());
		}
	}


	private static void findDirectories(File[] listFiles, Collection<File> monitoringDataDirectories) {
		for (File file : listFiles) {
			if (file.isDirectory()) {
				monitoringDataDirectories.add(file);
				findDirectories(file.listFiles(), monitoringDataDirectories);
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

		options.addOption(Option.builder("i").required(true).longOpt("input").hasArg().
				desc("a Kieker logfile directory").build());
		options.addOption(Option.builder("c").required(true).longOpt("correspondence").hasArg().
				desc("correspondence model").build());
		options.addOption(Option.builder("p").required(true).longOpt("pcm").hasArg().
				desc("directory containing all PCM models").build());

		/** help */
		options.addOption(Option.builder("h").required(false).longOpt("help").
				desc("show usage information").build());

		return options;
	}

	/**
	 * Create a command line setup with only the help option.
	 *
	 * @return returns simplified options
	 */
	private static Options createHelpOptions() {
		final Options options = new Options();

		options.addOption(Option.builder("i").required(false).longOpt("input").hasArg().
				desc("a Kieker logfile directory").build());
		options.addOption(Option.builder("c").required(false).longOpt("correspondence").hasArg().
				desc("correspondence model").build());
		options.addOption(Option.builder("p").required(false).longOpt("pcm").hasArg().
				desc("directory containing all PCM models").build());
		
		/** help */
		options.addOption(Option.builder("h").required(false).longOpt("help").
				desc("show usage information").build());

		return options;
	}
}

