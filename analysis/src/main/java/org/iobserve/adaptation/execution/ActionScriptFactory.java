package org.iobserve.adaptation.execution;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.TerminateAction;

public class ActionScriptFactory {
	private static final Logger LOG = LogManager.getLogger();

	private final AdaptationData data;

	public ActionScriptFactory(AdaptationData data) {
		this.data = data;
	}

	public ActionScript getExecutionScript(Action adaptationAction) {
		if (adaptationAction instanceof ChangeRepositoryComponentAction) {
			return this.createChangeRepositoryComponentActionScript((ChangeRepositoryComponentAction) adaptationAction);
		} else if (adaptationAction instanceof AllocateAction) {
			return this.createAllocateActionScript((AllocateAction) adaptationAction);
		} else if (adaptationAction instanceof DeallocateAction) {
			return this.createDeallocateActionScript((DeallocateAction) adaptationAction);
		} else if (adaptationAction instanceof MigrateAction) {
			return this.createMigrateActionScript((MigrateAction) adaptationAction);
		} else if (adaptationAction instanceof AcquireAction) {
			return this.createAcquireActionScript((AcquireAction) adaptationAction);
		} else if (adaptationAction instanceof TerminateAction) {
			return this.createTerminateActionScript((TerminateAction) adaptationAction);
		} else if (adaptationAction instanceof ReplicateAction) {
			return this.createReplicateActionScript((ReplicateAction) adaptationAction);
		} else {
			String errorMsg = String.format(
					"Could not create action script for adaptationAction '%s', no suitable class could be found",
					adaptationAction);
			LOG.error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
	}

	private ActionScript createReplicateActionScript(ReplicateAction adaptationAction) {
		return new ReplicateActionScript(this.data, adaptationAction);
	}

	private ActionScript createTerminateActionScript(TerminateAction adaptationAction) {
		return new TerminateActionScript(this.data, adaptationAction);
	}

	private ActionScript createAcquireActionScript(AcquireAction adaptationAction) {
		return new AcquireActionScript(this.data, adaptationAction);
	}

	private ActionScript createMigrateActionScript(MigrateAction adaptationAction) {
		return new MigrateActionScript(this.data, adaptationAction);
	}

	private ActionScript createDeallocateActionScript(DeallocateAction adaptationAction) {
		return new DeallocateActionScript(this.data, adaptationAction);
	}

	private ActionScript createAllocateActionScript(AllocateAction adaptationAction) {
		return new AllocateActionScript(this.data, adaptationAction);
	}

	private ActionScript createChangeRepositoryComponentActionScript(
			ChangeRepositoryComponentAction adaptationAction) {
		return new ChangeRepositoryComponentActionScript(this.data, adaptationAction);
	}
}
