package org.iobserve.analysis;

import giusa.parser.parameter.Parameter;
import giusa.parser.parameter.ParameterBean;

/**
 * JavaBean to hold the parameter values
 * @author Robert Heinrich
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 * @version 1.0
 *
 */
public class AnalysisMainParameterBean implements ParameterBean {

	private String dirMonitoringData;
	private String dirPcmModels;
	private String dirLogging;
	
	public AnalysisMainParameterBean() {
		// TODO Auto-generated constructor stub
	}
	
	@Parameter(name = "dirLogging")
	public String getDirLogging() {
		return this.dirLogging;
	}
	
	@Parameter(name = "dirMonitoring")
	public String getDirMonitoringData() {
		return this.dirMonitoringData;
	}
	
	@Parameter(name = "dirPcm")
	public String getDirPcmModels() {
		return this.dirPcmModels;
	}
	
	public void setDirLogging(final String dirLogging) {
		this.dirLogging = dirLogging;
	}
	
	public void setDirMonitoringData(final String dirMonitoringData) {
		this.dirMonitoringData = dirMonitoringData;
	}
	
	public void setDirPcmModels(final String dirPcmModels) {
		this.dirPcmModels = dirPcmModels;
	}
}
