package org.iobserve.adaptation.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.TerminateAction;

public class TerminateActionScript extends ActionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final TerminateAction action;

	public TerminateActionScript(AdaptationData data, TerminateAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
