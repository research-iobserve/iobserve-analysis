package org.iobserve.service.cli;

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
	private String pathProtocomMappingFile;
	
	public AnalysisMainParameterBean() {
		// TODO Auto-generated constructor stub
	}
	
	@Parameter(name = "dirMonitoring")
	public String getDirMonitoringData() {
		return this.dirMonitoringData;
	}
	
	@Parameter(name = "dirPcm")
	public String getDirPcmModels() {
		return this.dirPcmModels;
	}
	
	public void setDirMonitoringData(final String dirMonitoringData) {
		this.dirMonitoringData = dirMonitoringData;
	}
	
	public void setDirPcmModels(final String dirPcmModels) {
		this.dirPcmModels = dirPcmModels;
	}

	public String getPathProtocomMappingFile() {
		return pathProtocomMappingFile;
	}
	
	public void setPathProtocomMappingFile(String mappingFile) {
		this.pathProtocomMappingFile = mappingFile;
	}
}