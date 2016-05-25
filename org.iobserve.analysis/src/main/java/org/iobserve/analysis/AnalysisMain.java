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
import java.io.IOException;

import org.iobserve.analysis.model.ModelProviderPlatform;

import teetime.framework.Execution;
import teetime.framework.Configuration;

/**
 * Main class for starting the iObserve application
 * 
 * @author Reiner Jung
 * @author Robert Heinrich
 * @author Alessandro Giusa
 */
public class AnalysisMain {

	/**input parameters*/
	private AnalysisMainParameterBean inputParameter; 

	/** configuration for the analysis. */
	private Configuration configuration;
	
	private ModelProviderPlatform modelProviderPlatform;
	
	private SimpleTimeMemLogger timeMemLogger;

	/**
	 * Default constructor.
	 */
	private AnalysisMain() {
		// do nothing here
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
	// CONFIGURATION
	// *****************************************************************
	
	/**
	 * Initialization.
	 * @param args arguments
	 */
	private void init(final AnalysisMainParameterBean args) {
		System.out.println("init running");
		this.inputParameter = args;
		this.createLogger();
		this.createModelProviderPlatform();
		this.createObservationConfiguration();
	}
	
	// *****************************************************************
	// RUN
	// *****************************************************************

	private void run() {
		Runtime.getRuntime().gc(); // initial gc call
		final Analysis<AnalysisConfiguration> analysis = new Analysis<AnalysisConfiguration>(this.configuration);
		analysis.executeBlocking();
		((ObservationConfiguration) this.configuration).getRecordSwitch().outputStatistics();
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
	
	/**
	 * Get model provider platform.
	 * @return
	 */
	public ModelProviderPlatform getModelProviderPlatform() {
		return this.modelProviderPlatform;
	}

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
	// CREATION
	// *****************************************************************
	
	/**
	 * Creates the {@link ModelProviderPlatform}.
	 */
	private void createModelProviderPlatform() {
		final String pcmDir = this.getInputParameter().getDirPcmModels();
		this.modelProviderPlatform = new ModelProviderPlatform(pcmDir);
	}
	
	private void createLogger() {
		this.timeMemLogger = new SimpleTimeMemLogger(this.inputParameter.getDirLogging());
	}
	
	private void createObservationConfiguration() {
		try {
			this.configuration = new ObservationConfiguration(new File(this.inputParameter.getDirMonitoringData()));
		} catch (final IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
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
		// parse parameter
		final ParameterParser paramParser = new ParameterParser();
		paramParser.parse(args);
		final AnalysisMainParameterBean params = new AnalysisMainParameterBean();
		try {
			paramParser.getParameter(params);
		} catch (MissingParameterException e1) {
			e1.printStackTrace();
			System.exit(1);
		}
		
		// create and run application
		final AnalysisMain application = AnalysisMain.getInstance();
		application.init(params);
		try {
			application.run();
		} catch(Exception e) {
			e.printStackTrace();

			//dispatch the exception here
		} finally {
			application.closeLogger();
		}
	}
}

