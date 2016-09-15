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

import giusa.parser.parameter.Parameter;
import giusa.parser.parameter.ParameterBean;

/**
 * JavaBean to hold the parameter values. This bean is used to parse the command
 * line arguments. In order to add an argument just add a field with the given
 * type (int, long, float, double, boolean or String) and add a setter along
 * with a getter according to the Java-Bean convention. Use annotation
 * {@link Parameter} to make the field available to the parser.
 * 
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @version 1.0
 *
 */
public final class AnalysisMainParameterBean implements ParameterBean {

	/**dirMonitoring arg.*/
	private String dirMonitoringData;
	/**dirPcm arg.*/
	private String dirPcmModels;
	/**dirLogging arg.*/
	private String dirLogging;
	
	/**
	 * Empty constructor.
	 */
	public AnalysisMainParameterBean() {
		// nothing to do here :-)
	}
	
	/**
	 * @return dirLogging argument
	 */
	@Parameter(name = "dirLogging")
	public String getDirLogging() {
		return this.dirLogging;
	}
	
	/**
	 * @return dirMonitoring argument
	 */
	@Parameter(name = "dirMonitoring")
	public String getDirMonitoringData() {
		return this.dirMonitoringData;
	}
	
	/**
	 * @return dirPcm argument
	 */
	@Parameter(name = "dirPcm")
	public String getDirPcmModels() {
		return this.dirPcmModels;
	}
	
	/**
	 * Do not use this method. Called by reflection in order to set the value.
	 * @param theDirLogging logging path
	 */
	public void setDirLogging(final String theDirLogging) {
		this.dirLogging = theDirLogging;
	}
	
	/**
	 * Do not use this method. Called by reflection in order to set the value.
	 * @param theDirMonitoringData monitoring data path
	 */
	public void setDirMonitoringData(final String theDirMonitoringData) {
		this.dirMonitoringData = theDirMonitoringData; 
	}
	
	/**
	 * Do not use this method. Called by reflection in order to set the value.
	 * @param theDirPcmModels pcm models path
	 */
	public void setDirPcmModels(final String theDirPcmModels) {
		this.dirPcmModels = theDirPcmModels;
	}
}
