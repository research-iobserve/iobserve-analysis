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
package org.iobserve.analysis.service;

import java.io.File;
import java.net.MalformedURLException;

import teetime.framework.Configuration;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.daemon.Daemon;
import org.apache.commons.daemon.DaemonContext;
import org.apache.commons.daemon.DaemonInitException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.snapshot.SnapshotBuilder;

/**
 * @author Reiner Jung
 *
 */
public class AnalysisDaemon implements Daemon {

	private static final String VARIANCE_OF_USER_GROUPS = "variance-of-user-groups";
	private static final String THINK_TIME = "think-time";
	private static final String CLOSED_WORKLOAD = "closed-workload";

	private AnalysisThread thread;
	private boolean running = false;

	/**
	 * Empty default constructor.
	 */
	public AnalysisDaemon() {
	}

	@Override
	public void init(final DaemonContext context) throws DaemonInitException, MalformedURLException, InitializationException {
		final String[] args = context.getArguments();
		final CommandLineParser parser = new DefaultParser();
		try {
			CommandLine commandLine = parser.parse(AnalysisDaemon.createHelpOptions(), args);

			if (commandLine.hasOption("h")) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("iobserve-service", AnalysisDaemon.createOptions());
			} else {
				commandLine = parser.parse(AnalysisDaemon.createOptions(), args);

				/** get configuration parameter. */
				final int listenPort = Integer.parseInt(commandLine.getOptionValue("i"));
				final String outputHostname = commandLine.getOptionValues("o")[0];
				final String outputPort = commandLine.getOptionValues("o")[1];

				final File pcmModelsDirectory = new File(commandLine.getOptionValue("p"));

				final int varianceOfUserGroups = Integer.parseInt(commandLine.getOptionValue(AnalysisDaemon.VARIANCE_OF_USER_GROUPS));
				final int thinkTime = Integer.parseInt(commandLine.getOptionValue(AnalysisDaemon.THINK_TIME));
				final boolean closedWorkload = commandLine.hasOption(AnalysisDaemon.CLOSED_WORKLOAD);

				final String systemId = commandLine.getOptionValue("s");

				/** process parameter. */
				if (pcmModelsDirectory.exists()) {
					if (pcmModelsDirectory.isDirectory()) {
						final InitializeModelProviders modelProvider = new InitializeModelProviders(pcmModelsDirectory);
						SnapshotBuilder.setBaseSnapshotURI(URI.createURI(commandLine.getOptionValue("s")));

						final ICorrespondence correspondenceModel = modelProvider.getCorrespondenceModel();
						final UsageModelProvider usageModelProvider = modelProvider.getUsageModelProvider();
						final RepositoryModelProvider repositoryModelProvider = modelProvider.getRepositoryModelProvider();
						final ResourceEnvironmentModelProvider resourceEvnironmentModelProvider = modelProvider.getResourceEnvironmentModelProvider();
						final AllocationModelProvider allocationModelProvider = modelProvider.getAllocationModelProvider();
						final SystemModelProvider systemModelProvider = modelProvider.getSystemModelProvider();
						final SnapshotBuilder snapshotBuilder = new SnapshotBuilder("Runtime", modelProvider);
						final URI perOpteryxDir = URI.createURI(commandLine.getOptionValue("po"));
						final URI deployablesFolder = URI.createURI(commandLine.getOptionValue("d"));
						final URI lqnsDir = URI.createURI(commandLine.getOptionValue("l"));
						final URI privacyAnalysisFile = URI.createFileURI(commandLine.getOptionValue("geo"));

						final Configuration configuration = new ServiceConfiguration(listenPort, outputHostname, outputPort, systemId,
								varianceOfUserGroups, thinkTime, closedWorkload, correspondenceModel, usageModelProvider, repositoryModelProvider,
								resourceEvnironmentModelProvider, allocationModelProvider, systemModelProvider, snapshotBuilder, perOpteryxDir,
								lqnsDir, privacyAnalysisFile, deployablesFolder);

						this.thread = new AnalysisThread(this, configuration);
					} else {
						throw new DaemonInitException("CLI error: PCM directory " + pcmModelsDirectory.getPath() + " is not a directory.");
					}
				} else {
					throw new DaemonInitException("CLI error: PCM directory " + pcmModelsDirectory.getPath() + " does not exist.");
				}
			}
		} catch (final ParseException exp) {
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("iobserve-analysis", AnalysisDaemon.createOptions());
			throw new DaemonInitException("CLI error: " + exp.getMessage());
		}
	}

	@Override
	public void start() throws Exception {
		this.running = true;
		this.thread.start();
	}

	@Override
	public void stop() throws Exception {
		this.running = false;
		try {
			this.thread.join(1000);
		} catch (final InterruptedException e) {
			System.err.println(e.getMessage());
			throw e;
		}
	}

	@Override
	public void destroy() {
		this.thread = null;
	}

	public boolean isRunning() {
		return this.running;
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
		options.addOption(Option.builder("w").required(true).longOpt("closed-workload").desc("Closed workload").build());
		options.addOption(Option.builder("s").required(true).longOpt("snapshot-location").hasArg().desc("snapshot save location").build());
		options.addOption(Option.builder("po").required(true).longOpt("perOpteryx-headless-location").hasArg()
				.desc("the location of the PerOpteryx headless plugin").build());
		options.addOption(Option.builder("geo").required(true).longOpt("legal-geo-locations").hasArg()
				.desc("the geo-locations, where personal data can be stored").build());
		options.addOption(
				Option.builder("l").required(true).longOpt("lqns-location").hasArg().desc("the location of the LQN Solver for optimization").build());
		options.addOption(Option.builder("d").required(true).longOpt("deployables-folder").hasArg()
				.desc("the location of the deployable/executable scripts for adaptation execution").build());
		options.addOption(
				Option.builder("in").required(true).longOpt("interactive-adaptation").desc("interact with operator during adaptation").build());

		options.addOption(Option.builder("l").required(true).longOpt("legal-geo-locations").hasArg()
				.desc("the geo-locations, where personal data can be stored").build());

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

		/** help */
		options.addOption(Option.builder("h").required(false).longOpt("help").desc("show usage information").build());

		return options;
	}
}
