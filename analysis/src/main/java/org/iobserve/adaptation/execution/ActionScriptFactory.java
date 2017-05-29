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

/**
 * A factory for constructing execution scripts from adaptation actions.
 *
 * @author Tobias Pöppke
 *
 */
public class ActionScriptFactory {
	private static final Logger LOG = LogManager.getLogger();

	private final AdaptationData data;

	/**
	 * Creates a new action script factory with the given shared data.
	 *
	 * @param data
	 *            shared data of the adaptation stage
	 */
	public ActionScriptFactory(AdaptationData data) {
		this.data = data;
	}

	/**
	 * Constructs a new execution script according to the type of the adaptation
	 * action.
	 *
	 * @param action
	 *            the action for which an execution script should be created
	 * @return the execution script
	 * @throws IllegalArgumentException
	 *             if the adaptation action could not be mapped to an execution
	 *             script
	 */
	public ActionScript getExecutionScript(Action action) throws IllegalArgumentException {
		if (action instanceof ChangeRepositoryComponentAction) {
			return this.createChangeRepositoryComponentActionScript((ChangeRepositoryComponentAction) action);
		} else if (action instanceof AllocateAction) {
			return this.createAllocateActionScript((AllocateAction) action);
		} else if (action instanceof DeallocateAction) {
			return this.createDeallocateActionScript((DeallocateAction) action);
		} else if (action instanceof MigrateAction) {
			return this.createMigrateActionScript((MigrateAction) action);
		} else if (action instanceof AcquireAction) {
			return this.createAcquireActionScript((AcquireAction) action);
		} else if (action instanceof TerminateAction) {
			return this.createTerminateActionScript((TerminateAction) action);
		} else if (action instanceof ReplicateAction) {
			return this.createReplicateActionScript((ReplicateAction) action);
		} else {
			String errorMsg = String.format(
					"Could not create action script for adaptationAction '%s', no suitable class could be found",
					action);
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

	private ActionScript createChangeRepositoryComponentActionScript(ChangeRepositoryComponentAction adaptationAction) {
		return new ChangeRepositoryComponentActionScript(this.data, adaptationAction);
	}
}
