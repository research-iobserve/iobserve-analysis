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


	@Parameter(name = AnalysisMain.ARG_DIR_MONITORING_DATA, position = 0, required = true)
	public String getDirMonitoringData() {
		return this.dirMonitoringData;
	}

	@Parameter(name = AnalysisMain.ARG_IN_PATH_PCM_REPOSITORY_MODEL, position = 1, required = true)
	public String getPathPcmRepositoryModel() {
		return this.pathPcmRepositoryModel;
	}

	@Parameter(name = AnalysisMain.ARG_IN_PATH_PCM_USAGE_MODEL, position = 2, required = true)
	public String getPathPcmUsageModel() {
		return this.pathPcmUsageModel;
	}

	@Parameter(name = AnalysisMain.ARG_IN_PATH_PROTOCOM_MAPPING_FILE_RAC, position = 3, required = true)
	public String getPathProtocomMappingFile() {
		return this.pathProtocomMappingFile;
	}

	@Parameter(name = AnalysisMain.ARG_OUT_PATH_LOGGING_FILE, position = 4, required = true)
	public String getOutLoggingFile() {
		return this.outLoggingFile;
	}

	@Parameter(name = AnalysisMain.ARG_OUT_PATH_PCM_UPDATED_USAGE_MODEL, position = 5, required = true)
	public String getOutUpdatedUsageModel() {
		return this.outUpdatedUsageModel;
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
}
