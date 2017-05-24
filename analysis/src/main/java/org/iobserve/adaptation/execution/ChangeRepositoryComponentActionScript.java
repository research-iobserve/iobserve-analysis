package org.iobserve.adaptation.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;

public class ChangeRepositoryComponentActionScript extends ExecutionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final ChangeRepositoryComponentAction action;

	public ChangeRepositoryComponentActionScript(ChangeRepositoryComponentAction action) {
		this.action = action;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
