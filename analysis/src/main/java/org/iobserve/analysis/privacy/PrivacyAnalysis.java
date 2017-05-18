package org.iobserve.analysis.privacy;

import org.eclipse.emf.common.util.URI;

import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.AbstractTransformation;


public class PrivacyAnalysis extends AbstractLinearComposition<URI, Boolean> {

	private AbstractTransformation<?, ?>[] compositionStages;

	public PrivacyAnalysis(GraphCreation creation, GraphPrivacyAnalysis analysis) {
		super(creation.getInputPort(), analysis.getOutputPort());
		
		this.connectPorts(creation.getOutputPort(), analysis.getInputPort());
	}

}
