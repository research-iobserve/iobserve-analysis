package org.iobserve.service.cli;

import java.io.File;
import java.util.Collection;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
	
	private static final Logger LOGGER = LogManager.getLogger(AnalysisExecution.class);
	
    /** configuration for the analysis. */
    private final Configuration configuration;

    private final ModelProviderPlatform modelProviderPlatform;

    public AnalysisExecution(final Collection<File> monitoringDataDirectories, final String correspondenceFile,
            final String pcmModelsDirectory) {
        this.modelProviderPlatform = new ModelProviderPlatform(pcmModelsDirectory);

        this.configuration = new FileObservationConfiguration(monitoringDataDirectories, this.modelProviderPlatform);
    }

    /**
     * run the analysis application core.
     */
    public void run() {
        LOGGER.info("Execute Analysis");
        final Execution<Configuration> analysis = new Execution<>(this.configuration);
        analysis.executeBlocking();
        LOGGER.info("Analysis Complete");
    }

}
