package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.privacy.AbstractLinearComposition;

import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class ModelCreation extends AbstractLinearComposition<URI, URI> {

	public ModelCreation(CandidateCreation candidateCreator, CandidateSelector candidateSelector) {
		super(candidateCreator.getInputPort(), candidateSelector.getOutputPort());
		
		this.connectPorts(candidateCreator.getOutputPort(), candidateSelector.getInputPort());
	}

}
