package org.iobserve.adaptation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.adaptation.execution.ActionScript;
import org.iobserve.adaptation.execution.ActionScriptFactory;
import org.iobserve.evaluation.SystemEvaluation;
import org.iobserve.planning.systemadaptation.Action;

import teetime.stage.basic.AbstractTransformation;

/**
 * This stage executes the ordered adaptation {@link Action}s sequence.
 *
 * @author Philipp Weimann
 * @author Tobias Pöppke
 */
public class AdaptationExecution extends AbstractTransformation<AdaptationData, AdaptationData> {

	private final IAdaptationEventListener listener;

	private final URI deployablesFolderURI;

	/**
	 * Create a new adaptation execution stage with the given event listener for
	 * events generated during adaptation.
	 *
	 * If the listener is null and an error occurs, the execution will throw an
	 * exception and exit.
	 *
	 * @param listener
	 *            the event listener
	 */
	public AdaptationExecution(IAdaptationEventListener listener, URI deployablesFolderURI) {
		this.listener = listener;
		this.deployablesFolderURI = deployablesFolderURI;
	}

	@Override
	protected void execute(AdaptationData element) throws Exception {

		element.setDeployablesFolderURI(this.deployablesFolderURI);

		List<ActionScript> notAutoSupported = new ArrayList<>();
		List<ActionScript> actionScripts = new ArrayList<>();

		ActionScriptFactory actionFactory = new ActionScriptFactory(element);

		// TODO Finish, by adding execution. Maybe Async?
		for (Action action : element.getExecutionOrder()) {
			ActionScript script = actionFactory.getExecutionScript(action);
			actionScripts.add(script);
			if (!script.isAutoExecutable()) {
				notAutoSupported.add(script);
			}
		}

		if (notAutoSupported.size() > 0) {
			if (this.listener == null) {
				String unsupportedActionsDesc = notAutoSupported.stream().map(script -> script.getDescription())
						.collect(Collectors.joining("\n"));
				throw new IllegalStateException(
						"Could not execute all actions automatically, aborting.\n Not supported actions were:\n"
								+ unsupportedActionsDesc);
			}

			this.listener.notifyUnsupportedActionsFound(notAutoSupported);
		}

		SystemEvaluation.enableEvaluation(element);

		try {
			actionScripts.forEach(script -> {
				try {
					script.execute();
				} catch (Exception e) {
					if (this.listener == null) {
						throw new IllegalStateException("Could not execute action script '" + script.getDescription()
								+ "' automatically and no listener was present. Aborting!");
					}

					this.listener.notifyExecutionError(script, e);
				}
			});
		} finally {
			SystemEvaluation.disableEvaluation();
		}
	}

}
