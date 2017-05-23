package org.iobserve.adaptation;



import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.evaluation.SystemEvaluation;

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
		// TODO Finish, by adding execution. Maybe Async?
//		SystemEvaluation.disableEvaluation();
	}

}
