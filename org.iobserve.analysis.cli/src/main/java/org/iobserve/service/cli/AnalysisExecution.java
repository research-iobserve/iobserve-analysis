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
	private final Configuration configuration;

	private final ModelProviderPlatform modelProviderPlatform;

	public AnalysisExecution(final File monitoringDataDirectory, String correspondenceFile, String pcmModelsDirectory) {
		this.modelProviderPlatform = new ModelProviderPlatform(pcmModelsDirectory);

		this.configuration = new FileObservationConfiguration(monitoringDataDirectory, this.modelProviderPlatform);
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
