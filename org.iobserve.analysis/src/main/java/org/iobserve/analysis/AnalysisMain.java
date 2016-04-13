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

import org.iobserve.analysis.utils.ParameterParser;
import org.iobserve.analysis.utils.Terminal;

import teetime.framework.Analysis;
import teetime.framework.AnalysisConfiguration;

/**
 * Main calss for starting the iObserve application
 * 
 * @author Reiner Jung
 * @author Alessandro Giusa alessandrogiusa@gmail.com
 */
public class AnalysisMain {

	private static final int NUMBER_ARGUMENTS = 6;
	private static final String ARG_DIR_MONITORING_DATA = "inDirMonitoringData";
	private static final String ARG_IN_PATH_PCM_REPOSITORY_MODEL = "inPathPcmRepositoryModel";
	private static final String ARG_IN_PATH_PCM_USAGE_MODEL = "inPathPcmUsageModel";
	private static final String ARG_IN_PATH_PROTOCOM_MAPPING_FILE_RAC = "inPathProtocomMappingXml";
	private static final String ARG_OUT_PATH_LOGGING_FILE = "outPathLoggingFile";
	private static final String ARG_OUT_PATH_PCM_UPDATED_USAGE_MODEL = "outPathPcmUpdatedUsageModel";

	/** uri for the output usage model. */
	private String outputUsageModelURI;

	/** uri for the input usage model. */
	private String inputUsageModelURI;

	/** uri for the repository model. */
	private String repositoryModelURI;

	/**path to the mapping file used by the rac*/
	private String mappingFileRac;

	/** configuration for the analysis. */
	private AnalysisConfiguration configuration;

	/**
	 * Default constructor.
	 */
	private AnalysisMain() {
		// do nothing here
	}

	// *****************************************************************
	// GETTER
	// *****************************************************************

	public String getInputUsageModelURI() {
		return this.inputUsageModelURI;
	}

	public String getOutputUsageModelURI() {
		return this.outputUsageModelURI;
	}

	public String getRepositoryModelURI() {
		return this.repositoryModelURI;
	}

	public String getMappingFileRac() {
		return this.mappingFileRac;
	}

	// *****************************************************************
	// SINGLETON PART
	// *****************************************************************

	/**singleton reference*/
	private static AnalysisMain INSTANCE;

	/**
	 * Get singleton instance of {@link AnalysisMain}
	 * 
	 * @return singleton instance of {@link AnalysisMain}
	 */
	public static AnalysisMain getInstance() {
		if (AnalysisMain.INSTANCE == null) {
			AnalysisMain.INSTANCE = new AnalysisMain();
		}
		return AnalysisMain.INSTANCE;
	}

	// *****************************************************************
	// SIMPLE LOGGER
	// *****************************************************************

	private SimpleTimeMemLogger timeMemLogger;

	/**
	 * Get simple logger for timing and memory usage
	 * @return the logger
	 */
	public SimpleTimeMemLogger getTimeMemLogger() {
		return this.timeMemLogger;
	}

	/**
	 * Close the logger
	 */
	public void closeLogger() {
		this.timeMemLogger.close();
	}

	// *****************************************************************
	// ACTUAL ANALYSIS MAIN STUFF
	// *****************************************************************

	/**
	 * Main function.
	 *
	 * @param args command line arguments.
	 */
	public static void main(final String[] args) {
		final AnalysisMain application = AnalysisMain.getInstance();
		try {
			application.parseArguments(args);
			if (application.checkConfiguration()) {
				application.run();
			} else {
				Terminal.println("configuration check failed!");
				System.exit(1);

			}
		} catch(Exception e) {
			application.usage();
			e.printStackTrace();

			//dispatch the exception here
		} finally {
			application.closeLogger();
		}
	}

	private void run() {
		Runtime.getRuntime().gc(); // initial gc call
		final Analysis<AnalysisConfiguration> analysis = new Analysis<AnalysisConfiguration>(this.configuration);
		analysis.executeBlocking();
		((ObservationConfiguration) this.configuration).getRecordSwitch().outputStatistics();
	}


	private boolean checkConfiguration() {
		return true; //TODO what does this do?
	}

	// *****************************************************************
	// PARAMETER PARSING
	// *****************************************************************

	/**
	 * Parse console arguments
	 * @param args console arguments
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	private void parseArguments(final String[] args) 
			throws UnsupportedEncodingException, FileNotFoundException {

		// parse all parameters
		final ParameterParser paramParser = new ParameterParser();
		paramParser.parse(args);

		final String dirMonitoringData = paramParser.getParameterString(
				AnalysisMain.ARG_DIR_MONITORING_DATA, 0, true);
		this.repositoryModelURI = paramParser.getParameterString(
				AnalysisMain.ARG_IN_PATH_PCM_REPOSITORY_MODEL, 1, true);
		this.inputUsageModelURI = paramParser.getParameterString(
				AnalysisMain.ARG_IN_PATH_PCM_USAGE_MODEL, 2, true);
		this.mappingFileRac = paramParser.getParameterString(
				AnalysisMain.ARG_IN_PATH_PROTOCOM_MAPPING_FILE_RAC, 3, true);
		final String loggingPath = paramParser.getParameterString(
				AnalysisMain.ARG_OUT_PATH_LOGGING_FILE, 4, true);
		this.outputUsageModelURI = paramParser.getParameterString(
				AnalysisMain.ARG_OUT_PATH_PCM_UPDATED_USAGE_MODEL, 5, true);

		// create the simple logger
		this.timeMemLogger = new SimpleTimeMemLogger(loggingPath);

		// create the configuration for tee-time framework
		try {
			this.configuration = new ObservationConfiguration(new File(dirMonitoringData));
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void usage() {
		Terminal.printlnf("Usage of arguments: -%s, -%s, -%s, -%s, -%s, -%s",
				AnalysisMain.ARG_DIR_MONITORING_DATA,
				AnalysisMain.ARG_IN_PATH_PCM_REPOSITORY_MODEL,
				AnalysisMain.ARG_IN_PATH_PCM_USAGE_MODEL,
				AnalysisMain.ARG_IN_PATH_PROTOCOM_MAPPING_FILE_RAC,
				AnalysisMain.ARG_OUT_PATH_LOGGING_FILE,
				AnalysisMain.ARG_OUT_PATH_PCM_UPDATED_USAGE_MODEL);
	}

}
