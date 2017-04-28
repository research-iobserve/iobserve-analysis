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
package org.iobserve.service.cli;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import teetime.framework.Configuration;
import teetime.framework.Execution;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.FileObservationConfiguration;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.snapshot.SnapshotBuilder;

/**
 * Main class for starting the iObserve application.
 *
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public final class AnalysisMain {

	private static final String VARIANCE_OF_USER_GROUPS = "variance-of-user-groups";
	private static final String THINK_TIME = "think-time";
	private static final String CLOSED_WORKLOAD = "closed-workload";

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

				/** get configuration parameter. */
				final int varianceOfUserGroups = Integer.parseInt(commandLine.getOptionValue(AnalysisMain.VARIANCE_OF_USER_GROUPS));
				final int thinkTime = Integer.parseInt(commandLine.getOptionValue(AnalysisMain.THINK_TIME));
				final boolean closedWorkload = commandLine.hasOption(AnalysisMain.CLOSED_WORKLOAD);

				/** process parameter. */
				final File monitoringDataDirectory = new File(commandLine.getOptionValue("i"));

				if (monitoringDataDirectory.isDirectory()) {
					final File pcmModelsDirectory = new File(commandLine.getOptionValue("p"));
					if (pcmModelsDirectory.exists()) {
						/** create and run application */
						final Collection<File> monitoringDataDirectories = new ArrayList<>();
						AnalysisMain.findDirectories(monitoringDataDirectory.listFiles(), monitoringDataDirectories);

						final InitializeModelProviders modelProviderPlatform = new InitializeModelProviders(pcmModelsDirectory);

						final ICorrespondence correspondenceModel = modelProviderPlatform.getCorrespondenceModel();
						final RepositoryModelProvider repositoryModelProvider = modelProviderPlatform.getRepositoryModelProvider();
						final UsageModelProvider usageModelProvider = modelProviderPlatform.getUsageModelProvider();
						final ResourceEnvironmentModelProvider resourceEvnironmentModelProvider = modelProviderPlatform
								.getResourceEnvironmentModelProvider();
						final AllocationModelProvider allocationModelProvider = modelProviderPlatform.getAllocationModelProvider();
						final SystemModelProvider systemModelProvider = modelProviderPlatform.getSystemModelProvider();
						
						String snapshotPath = commandLine.getOptionValue("s");
						final SnapshotBuilder snapshotBuilder = new SnapshotBuilder(URI.createFileURI(snapshotPath), modelProviderPlatform);

						final Configuration configuration = new FileObservationConfiguration(monitoringDataDirectories, correspondenceModel,
								usageModelProvider, repositoryModelProvider, resourceEvnironmentModelProvider, allocationModelProvider,
								systemModelProvider, snapshotBuilder, varianceOfUserGroups, thinkTime, closedWorkload);

						System.out.println("Analysis configuration");
						final Execution<Configuration> analysis = new Execution<>(configuration);
						System.out.println("Analysis start");
						analysis.executeBlocking();
						System.out.println("Anaylsis complete");
					} else {
						System.err.println(String.format("the pcm dir %s does not exist?!", pcmModelsDirectory));
					}
				} else {
					System.err.println("CLI error: " + monitoringDataDirectory.getName() + " is not a directory.");
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

		options.addOption(Option.builder("i").required(true).longOpt("input").hasArg().desc("a Kieker logfile directory").build());
		options.addOption(Option.builder("p").required(true).longOpt("pcm").hasArg().desc("directory containing all PCM models").build());
		options.addOption(Option.builder("V").required(true).longOpt(AnalysisMain.VARIANCE_OF_USER_GROUPS).hasArg()
				.desc("Variance of user groups for the clustering").build());
		options.addOption(Option.builder("t").required(true).longOpt(AnalysisMain.THINK_TIME).hasArg()
				.desc("Variance of user groups for the clustering").build());
		options.addOption(Option.builder("w").required(true).longOpt("closed-workload").desc("Closed workload").build());
		options.addOption(Option.builder("s").required(true).longOpt("snapshot-location").hasArg().desc("snapshot save location").build());

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

		options.addOption(Option.builder("i").required(false).longOpt("input").hasArg().desc("a Kieker logfile directory").build());
		options.addOption(Option.builder("p").required(false).longOpt("pcm").hasArg().desc("directory containing all PCM models").build());
		options.addOption(Option.builder("V").required(true).longOpt("variance-of-user-groups").hasArg()
				.desc("Variance of user groups for the clustering").build());
		options.addOption(
				Option.builder("t").required(true).longOpt("think-time").hasArg().desc("Variance of user groups for the clustering").build());
		options.addOption(Option.builder("w").required(true).longOpt("closed-workload").desc("Closed workload").build());
		options.addOption(Option.builder("s").required(false).longOpt("snapshot-location").hasArg().desc("snapshot save location").build());
		
		/** help */
		options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

		return options;
	}
}
