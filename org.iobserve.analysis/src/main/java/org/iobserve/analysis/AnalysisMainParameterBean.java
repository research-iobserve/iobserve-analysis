package org.iobserve.analysis;

import giusa.parser.parameter.Parameter;
import giusa.parser.parameter.ParameterBean;

/**
 * JavaBean to hold the parameter values
 * @author Alessandro Giusa, alessandrogiusa@gmail.com
 * @author Robert Heinrich
 * @version 1.0
 *
 */
public class AnalysisMainParameterBean implements ParameterBean {

	private String dirMonitoringData;
	private String pathPcmRepositoryModel;
	private String pathPcmUsageModel;
	private String pathProtocomMappingFile;
	private String outLoggingFile;
	private String outUpdatedUsageModel;
	private String pathPcmAllocationModel;
	private String pathPcmSystemModel;
	private String pathPcmResEnvModel;
	private String outUpdatedAllocationModel;


	@Parameter(name = AnalysisMain.ARG_DIR_MONITORING_DATA)
	public String getDirMonitoringData() {
		return this.dirMonitoringData;
	}

	@Parameter(name = AnalysisMain.ARG_IN_PATH_PCM_REPOSITORY_MODEL)
	public String getPathPcmRepositoryModel() {
		return this.pathPcmRepositoryModel;
	}

	@Parameter(name = AnalysisMain.ARG_IN_PATH_PCM_USAGE_MODEL)
	public String getPathPcmUsageModel() {
		return this.pathPcmUsageModel;
	}

	@Parameter(name = AnalysisMain.ARG_IN_PATH_PROTOCOM_MAPPING_FILE_RAC)
	public String getPathProtocomMappingFile() {
		return this.pathProtocomMappingFile;
	}

	@Parameter(name = AnalysisMain.ARG_OUT_PATH_LOGGING_FILE)
	public String getOutLoggingFile() {
		return this.outLoggingFile;
	}

	@Parameter(name = AnalysisMain.ARG_OUT_PATH_PCM_UPDATED_USAGE_MODEL)
	public String getOutUpdatedUsageModel() {
		return this.outUpdatedUsageModel;
	}
	
	@Parameter(name = AnalysisMain.ARG_IN_PATH_ALLOCATION_MODEL)
	public String getPathPcmAllocationModel() {
		return this.pathPcmAllocationModel;
	}
	
	@Parameter(name = AnalysisMain.ARG_IN_PATH_SYSTEM_MODEL)
	public String getPathPcmSystemModel() {
		return this.pathPcmSystemModel;
	}
	
	@Parameter(name = AnalysisMain.ARG_IN_PATH_RES_ENV_MODEL)
	public String getPathPcmResEnvModel() { 
		return this.pathPcmResEnvModel;
	}
	
	@Parameter(name = AnalysisMain.ARG_OUT_PATH_PCM_UPDATED_ALLOCATION_USAGE_MODEL)
	public String getOutUpdatedAllocationModel() {
		return this.outUpdatedAllocationModel;
	}

	public void setDirMonitoringData(final String dirMonitoringData) {
		this.dirMonitoringData = dirMonitoringData;
	}

	public void setPathPcmRepositoryModel(final String pathPcmRepositoryModel) {
		this.pathPcmRepositoryModel = pathPcmRepositoryModel;
	}

	public void setPathPcmUsageModel(final String pathPcmUsageModel) {
		this.pathPcmUsageModel = pathPcmUsageModel;
	}

	public void setPathProtocomMappingFile(final String pathProtocomMappingFile) {
		this.pathProtocomMappingFile = pathProtocomMappingFile;
	}

	public void setOutLoggingFile(final String outLoggingFile) {
		this.outLoggingFile = outLoggingFile;
	}

	public void setOutUpdatedUsageModel(final String outUpdatedUsageModel) {
		this.outUpdatedUsageModel = outUpdatedUsageModel;
	}
	
	public void setPathPcmAllocationModel(final String pathPcmAllocationModel) {
		this.pathPcmAllocationModel = pathPcmAllocationModel;
	}
	
	public void setPathPcmSystemModel(final String pathPcmSystemModel) {
		this.pathPcmSystemModel = pathPcmSystemModel;
	}
	
	public void setPathPcmResEnvModel(final String pathPcmResEnvModel) {
		this.pathPcmResEnvModel = pathPcmResEnvModel;
	}
	
	public void setOutUpdatedAllocationModel(final String outUpdatedAllocationModel) {
		this.outUpdatedAllocationModel = outUpdatedAllocationModel;
	}
}
