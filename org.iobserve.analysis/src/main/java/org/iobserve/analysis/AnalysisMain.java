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

import giusa.parser.parameter.MissingParameterException;
import giusa.parser.parameter.ParameterParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.iobserve.analysis.utils.Terminal;

import teetime.framework.Analysis;
import teetime.framework.AnalysisConfiguration;

/**
 * Main calss for starting the iObserve application
 * 
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa alessandrogiusa@gmail.com
 */
public class AnalysisMain {

	public static final String ARG_DIR_MONITORING_DATA = "inDirMonitoringData";
	public static final String ARG_IN_PATH_PCM_REPOSITORY_MODEL = "inPathPcmRepositoryModel";
	public static final String ARG_IN_PATH_PCM_USAGE_MODEL = "inPathPcmUsageModel";
	public static final String ARG_IN_PATH_PROTOCOM_MAPPING_FILE_RAC = "inPathProtocomMappingFile";
	public static final String ARG_OUT_PATH_LOGGING_FILE = "outPathLoggingFile";
	public static final String ARG_OUT_PATH_PCM_UPDATED_USAGE_MODEL = "outPathPcmUpdatedUsageModel";

	/**input parameters*/
	private AnalysisMainParameterBean inputParameter;

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

	/**
	 * Get command line parameter.
	 * @return bean containing them.
	 */
	public AnalysisMainParameterBean getInputParameter() {
		return this.inputParameter;
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

		this.inputParameter = new AnalysisMainParameterBean();
		try {
			paramParser.getParameter(this.inputParameter);


			// create the simple logger
			this.timeMemLogger = new SimpleTimeMemLogger(this.inputParameter.getOutLoggingFile());

			// create the configuration for tee-time framework
			try {
				this.configuration = new ObservationConfiguration(new File(this.inputParameter.getDirMonitoringData()));
			} catch (final IOException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (final ClassNotFoundException e) {
				e.printStackTrace();
				System.exit(1);
			}

		} catch (MissingParameterException e1) {
			e1.printStackTrace();
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
