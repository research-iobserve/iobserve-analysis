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

import teetime.framework.Analysis;
import teetime.framework.AnalysisConfiguration;

/**
 * @author Reiner Jung
 */
public class AnalysisMain {
	
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
	public AnalysisMain() {
		// nothing to do here.
		INSTANCE = this;
	}
	
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
	//
	// *****************************************************************
	
	private static AnalysisMain INSTANCE;
	
	public static AnalysisMain getInstance() {
		return INSTANCE; //can be null but for now just for testing this app
	}
	
	// *****************************************************************
	// LOGGING STUFF
	// *****************************************************************
	
	private SimpleTimeMemLogger timeMemLogger;

	public SimpleTimeMemLogger getTimeMemLogger() {
		return this.timeMemLogger;
	}
	
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
		final AnalysisMain application = new AnalysisMain();
		try {
			application.parseArguments(args);
			if (application.checkConfiguration()) {
				application.run();
			} else {
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
		return true;
	}

	/**
	 * Parse console arguments
	 * @param args console arguments
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 */
	private void parseArguments(final String[] args) throws UnsupportedEncodingException, FileNotFoundException {
		final String dirMonitoringData = args[0];
		final String loggingOutput = args[1];
		this.repositoryModelURI = args[2];
		this.inputUsageModelURI = args[3];
		this.outputUsageModelURI = args[4];
		this.mappingFileRac = args[5];
		
		this.timeMemLogger = new SimpleTimeMemLogger(loggingOutput);

		try {
			this.configuration = new ObservationConfiguration(new File(dirMonitoringData));
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void usage() {
		final String usage = "<dirMonitoringData> <loggingOutputPath> "
				+ "<pathPcmRepositoryModel> <pathPcmUsageModel> <pathOutputUpdatedPcmUsageModel>"
				+ " <pathProtocomMappingXml>";
		System.out.println(usage);
	}

}
