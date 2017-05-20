package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaption.data.AdaptationData;
import org.iobserve.analysis.utils.AbstractLinearComposition;

public class ModelCreation extends AbstractLinearComposition<AdaptationData, AdaptationData> {

	public ModelCreation(CandidateCreation candidateCreator, CandidateSelector candidateSelector) {
		super(candidateCreator.getInputPort(), candidateSelector.getOutputPort());
		
		this.connectPorts(candidateCreator.getOutputPort(), candidateSelector.getInputPort());
	}

}
