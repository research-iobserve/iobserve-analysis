package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.utils.AbstractLinearComposition;

/**
 * This class encapsulates the system planning filter in the teetime framework.
 * It generates and selects a re-deployment PCM model. The sub-stages are a
 * candidate creator and a candidate selector.
 * 
 * @author Philipp Weimann
 */
public class ModelCreation extends AbstractLinearComposition<URI, AdaptationData> {

	/**
	 * The constructor for the model creation part.
	 * 
	 * @param candidateCreator
	 *            the creator
	 * @param candidateSelector
	 *            the selector
	 */
	public ModelCreation(CandidateCreation candidateCreator, CandidateSelector candidateSelector) {
		super(candidateCreator.getInputPort(), candidateSelector.getOutputPort());

		this.connectPorts(candidateCreator.getOutputPort(), candidateSelector.getInputPort());
	}

}
