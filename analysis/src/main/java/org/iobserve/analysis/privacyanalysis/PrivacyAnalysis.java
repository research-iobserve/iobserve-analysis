package org.iobserve.analysis.privacyanalysis;

import org.eclipse.emf.common.util.URI;

import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.AbstractTransformation;

public class PrivacyAnalysis extends AbstractLinearComposition<URI, Boolean> {

	private AbstractTransformation<URI, Boolean>[] compositionStages;

	public PrivacyAnalysis(GraphCreation creation, GraphPrivacyAnalysis analysis) {
		super(creation.getInputPort(), analysis.getOutputPort());

		this.connectPorts(creation.getOutputPort(), analysis.getInputPort());

		this.compositionStages = new AbstractTransformation[] { creation, analysis };
	}

}
