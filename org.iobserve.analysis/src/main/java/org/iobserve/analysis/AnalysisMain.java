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
package org.iobserve.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.iobserve.analysis.usage.PrototypeUsageModelGenerator;

import teetime.framework.Analysis;
import teetime.framework.AnalysisConfiguration;

/**
 * @author Reiner Jung
 */
public class AnalysisMain {
	private String outputUsageModelURI;

	private String inputUsageModelURI;

	private String repositoryModelURI;

	private AnalysisConfiguration configuration;

	/**
	 * Default constructor.
	 */
	public AnalysisMain() {
		// nothing to do here.
	}

	/**
	 * Main function.
	 *
	 * @param args
	 *            command line arguments.
	 */
	public static void main(final String[] args) {
		try {
			final AnalysisMain application = new AnalysisMain();
			application.parseArguments(args);

			if (application.checkConfiguration()) {
				application.run();
				// TODO Hack to test the UsageModel-Generation;

				TimeLogger.getTimeLogger().close();
			} else {
				TimeLogger.getTimeLogger().close();
				System.exit(1);
			}

		} catch (final UnsupportedEncodingException e) {
			System.out.println("Logging failed " + e.getMessage());
			e.printStackTrace();
		} catch (final FileNotFoundException e) {
			System.out.println("Logging failed " + e.getMessage());
			e.printStackTrace();
		} catch (final IOException e) {
			System.out.println("Logging failed " + e.getMessage());
			e.printStackTrace();
		}
	}

	private void run() {
		final Analysis<AnalysisConfiguration> analysis = new Analysis<AnalysisConfiguration>(this.configuration);
		final PrototypeUsageModelGenerator usageModelGenerator = new PrototypeUsageModelGenerator(this.repositoryModelURI, this.inputUsageModelURI,
				this.outputUsageModelURI);

		analysis.executeBlocking();

		((ObservationConfiguration) this.configuration).getRecordSwitch().outputStatistics();

		final Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
		runtime.gc();
		// Calculate the used memory
		System.out.println("Used memory before transformation: " + (runtime.totalMemory() - runtime.freeMemory()));

		usageModelGenerator.trigger();
		// Run the garbage collector
		runtime.gc();
		// Calculate the used memory
		System.out.println("Used memory after transformation: " + (runtime.totalMemory() - runtime.freeMemory()));
	}

	private boolean checkConfiguration() {
		return true;
	}

	private void parseArguments(final String[] args) throws UnsupportedEncodingException, FileNotFoundException {
		final String logFileName = args[0];
		TimeLogger.getTimeLogger().setPastTime(Long.parseLong(args[1]));
		this.repositoryModelURI = args[2];
		this.inputUsageModelURI = args[3];
		this.outputUsageModelURI = args[4];

		TimeLogger.getTimeLogger().open(args[5]);

		try {
			this.configuration = new ObservationConfiguration(new File(logFileName));
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
