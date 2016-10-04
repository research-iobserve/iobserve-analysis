package org.iobserve.analysis.service;

import java.net.MalformedURLException;

import org.iobserve.analysis.model.ModelProviderPlatform;

import teetime.framework.Configuration;
import teetime.framework.Execution;

public class AnalysisThread extends Thread {

    private final AnalysisDaemon daemon;

    private final Configuration configuration;

    public AnalysisThread(final AnalysisDaemon daemon, final int inputPort, final String outputHostname,
            final String outputPort, final String systemId, final int varianceOfUserGroups, final int thinkTime,
            final boolean closedWorkload, final ModelProviderPlatform platform) throws MalformedURLException {
        this.daemon = daemon;
        this.configuration = new ServiceConfiguration(inputPort, outputHostname, outputPort, systemId,
                varianceOfUserGroups, thinkTime, closedWorkload, platform);
    }

    @Override
    public void run() {
        if (this.daemon.isRunning()) {
            final Execution<Configuration> analysis = new Execution<>(this.configuration);
            analysis.executeBlocking();
        }
    }

}
