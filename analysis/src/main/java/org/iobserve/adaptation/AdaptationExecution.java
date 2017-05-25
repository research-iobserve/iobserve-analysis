package org.iobserve.adaptation;

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
 */
public class AdaptationExecution extends AbstractTransformation<AdaptationData, AdaptationData> {

	@Override
	protected void execute(AdaptationData element) throws Exception {

		SystemEvaluation.enableEvaluation(element);

		ActionScriptFactory actionFactory = new ActionScriptFactory(element);

		// TODO Finish, by adding execution. Maybe Async?
		// TODO integrate interaction with operator by catching exceptions from
		// executions
		for (Action action : element.getExecutionOrder()) {
			ActionScript script = actionFactory.getExecutionScript(action);
			script.execute();
		}

		SystemEvaluation.disableEvaluation();
	}

}
