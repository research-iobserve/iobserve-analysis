package org.iobserve.service.cli;

import java.io.File;

import org.iobserve.analysis.AbstractObservationConfiguration;
import org.iobserve.analysis.FileObservationConfiguration;
import org.iobserve.analysis.model.ModelProviderPlatform;

import teetime.framework.Configuration;
import teetime.framework.Execution;

/**
 * 
 * @author Reiner Jung
 *
 */
public class AnalysisExecution {
	/** configuration for the analysis. */
	private Configuration configuration;
	
	private ModelProviderPlatform modelProviderPlatform;
	
	public AnalysisExecution(final AnalysisMainParameterBean args) {		
		final String pcmDir = args.getDirPcmModels();
		this.modelProviderPlatform = new ModelProviderPlatform(pcmDir);
		
		File inputDataSource = new File(args.getDirMonitoringData());
		this.configuration = new FileObservationConfiguration(inputDataSource, this.modelProviderPlatform);
	}

	/**
	 * run the analysis application core.
	 */
	public void run() {
		final Execution<Configuration> analysis = new Execution<Configuration>(this.configuration);
		analysis.executeBlocking();
		((AbstractObservationConfiguration) this.configuration).getRecordSwitch().outputStatistics();
	}
		
}
