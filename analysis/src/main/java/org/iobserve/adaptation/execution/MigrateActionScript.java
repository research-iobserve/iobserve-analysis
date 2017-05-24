package org.iobserve.adaptation.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.MigrateAction;

public class MigrateActionScript extends ExecutionScript {
	private static final Logger LOG = LogManager.getLogger();

	private final MigrateAction action;

	public MigrateActionScript(AdaptationData data, MigrateAction action) {
		super(data);
		this.action = action;
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub

	}

}
