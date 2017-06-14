package org.iobserve.evaluation;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.analysis.utils.AbstractLinearComposition;

/**
 * The System Evaluation conceptual filter stage
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public class SystemEvaluation extends AbstractLinearComposition<URI, Boolean> {

	private static ModelComparer modelComparer;
	
	/**
	 * The Constructor
	 * 
	 * @param comparer the Model Comparer
	 */
	public SystemEvaluation(ModelComparer comparer) {
		super(comparer.getInputPort(), comparer.getOutputPort());
		
		SystemEvaluation.modelComparer = comparer;
	}

	/**
	 * Starts the model evaluation and reroutes the snapshot events to the SystemEvaluation stage
	 * 
	 * @param adaptationData the adaptation data for comparison
	 */
	public static void enableEvaluation(AdaptationData adaptationData) {
		SystemEvaluation.modelComparer.setBaseData(adaptationData);
		SnapshotBuilder.setEvaluationMode(true);
	}
	
	/**
	 * Ends the model evaluation and stops the reroutings if the snapshot events
	 */
	public static void disableEvaluation()
	{
		SystemEvaluation.modelComparer.setBaseData(null);
		SnapshotBuilder.setEvaluationMode(false);
	}

}
