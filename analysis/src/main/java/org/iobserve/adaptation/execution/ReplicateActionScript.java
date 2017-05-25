package org.iobserve.adaptation.execution;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.ReplicateAction;

/**
 * Action script for replicating an assembly context.
 *
 * Currently not supported automatically, so the execute method will raise an
 * exception to trigger operator interaction.
 *
 * @author Tobias Pöppke
 *
 */
public class ReplicateActionScript extends ActionScript {

	/**
	 * Create a new replicate action script with the given data.
	 *
	 * @param data
	 *            the data shared in the adaptation stage
	 * @param action
	 *            the action item to be executed
	 */
	public ReplicateActionScript(AdaptationData data, ReplicateAction action) {
		super(data);
	}

	@Override
	public void execute() {
		throw new UnsupportedOperationException(
				"Automated replication is currently not possible. Please use the acquire and allocate actions to replicate components!");
	}

}
