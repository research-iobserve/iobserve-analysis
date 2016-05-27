package org.iobserve.analysis.service;

import java.net.MalformedURLException;

import org.iobserve.analysis.model.ModelProviderPlatform;

import teetime.framework.Configuration;
import teetime.framework.Execution;

public class AnalysisThread extends Thread {

	// TODO this must be changed to a configurable system id
	private final static String SYSTEM_ID = "CoCoME";

	private final AnalysisDaemon daemon;

	private final Configuration configuration;

	public AnalysisThread(final AnalysisDaemon daemon, final int inputPort,
			final String outputHostname, final String outputPort, ModelProviderPlatform platform) throws MalformedURLException {
		this.daemon = daemon;
		this.configuration = new ServiceConfiguration(inputPort, outputHostname, outputPort, SYSTEM_ID, platform);
	}

	@Override
	public void run() {
		if (this.daemon.isRunning()) {
			final Execution<Configuration> analysis = new Execution<Configuration>(this.configuration);
			analysis.executeBlocking();
		}
	}



}
