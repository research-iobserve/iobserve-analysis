package org.iobserve.adaptation.execution;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;

/**
 * Action script for changing the repository component of an assembly context.
 *
 * Currently not supported automatically, so the execute method will raise an
 * exception to trigger operator interaction.
 *
 * @author Tobias Pöppke
 *
 */
public class ChangeRepositoryComponentActionScript extends ActionScript {

	/**
	 * Create a new change repository component action script with the given
	 * data.
	 *
	 * @param data
	 *            the data shared in the adaptation stage
	 * @param action
	 *            the action item to be executed
	 */
	public ChangeRepositoryComponentActionScript(AdaptationData data, ChangeRepositoryComponentAction action) {
		super(data);
	}

	@Override
	public void execute() {
		throw new UnsupportedOperationException(
				"Automated change of repository components currently not possible. Please change the repository component manually!");
	}

}
