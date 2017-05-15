package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.privacyanalysis.AbstractLinearComposition;

import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class ModelCreation extends AbstractLinearComposition<Boolean, URI> {

	public ModelCreation(CandidateCreation candidateCreator, CandidateSelector candidateSelector) {
		super(candidateCreator.getInputPort(), candidateSelector.getOutputPort());
		// TODO Auto-generated constructor stub
	}

}
