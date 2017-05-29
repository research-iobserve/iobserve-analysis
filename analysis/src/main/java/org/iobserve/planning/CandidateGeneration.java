package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.utils.AbstractLinearComposition;

/**
 * This class encapsulates the system planning filter in the teetime framework.
 * It generates and selects a re-deployment PCM model. The sub-stages are a
 * model processor, a model optimizer and a candidate processor.
 *
 * @author Philipp Weimann
 * @author Tobias Pöppke
 */
public class CandidateGeneration extends AbstractLinearComposition<URI, AdaptationData> {

	/**
	 * The constructor for the model creation part.
	 *
	 * @param modelPreProcessor
	 *            the pre processing stage to prepare the model for optimization
	 * @param modelOptimizer
	 *            the optimizer
	 * @param candidateProcessor
	 *            the processor for the generated candidate
	 */
	public CandidateGeneration(ModelProcessing modelPreProcessor, ModelOptimization modelOptimizer,
			CandidateProcessing candidateProcessor) {
		super(modelPreProcessor.getInputPort(), candidateProcessor.getOutputPort());

		this.connectPorts(modelPreProcessor.getOutputPort(), modelOptimizer.getInputPort());
		this.connectPorts(modelOptimizer.getOutputPort(), candidateProcessor.getInputPort());
	}

}
