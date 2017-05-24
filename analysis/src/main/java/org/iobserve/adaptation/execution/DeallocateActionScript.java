package org.iobserve.adaptation.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.planning.systemadaptation.DeallocateAction;

public class DeallocateActionScript extends ExecutionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final DeallocateAction action;

	public DeallocateActionScript(DeallocateAction action) {
		this.action = action;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
