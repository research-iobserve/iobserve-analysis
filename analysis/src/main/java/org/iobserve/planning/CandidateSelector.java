package org.iobserve.planning;

import java.io.File;

import org.iobserve.adaption.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.GraphFactory;
import org.iobserve.analysis.graph.ModelGraph;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class selects a created candidate and creates all required information
 * for further processing.
 * 
 * @author Philipp Weimann
 */
public class CandidateSelector extends AbstractTransformation<CandidateInformations, AdaptationData> {

	@Override
	protected void execute(CandidateInformations element) throws Exception {
		AdaptationData adapdationData = element.adapdationData;

		GraphFactory factory = new GraphFactory();
		ModelGraph graph = factory.buildGraph(new InitializeModelProviders(new File(adapdationData.getReDeploymentURI().toFileString())));
		element.adapdationData.setReDeploymentGraph(graph);

		this.outputPort.send(element.adapdationData);
	}

}
