package org.iobserve.planning;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaption.data.AdapdationData;
import org.iobserve.analysis.utils.AbstractLinearComposition;

import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class ModelCreation extends AbstractLinearComposition<AdapdationData, AdapdationData> {

	public ModelCreation(CandidateCreation candidateCreator, CandidateSelector candidateSelector) {
		super(candidateCreator.getInputPort(), candidateSelector.getOutputPort());
		
		this.connectPorts(candidateCreator.getOutputPort(), candidateSelector.getInputPort());
	}

}
