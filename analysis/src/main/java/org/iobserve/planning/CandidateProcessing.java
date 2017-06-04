package org.iobserve.planning;

import java.io.File;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.GraphFactory;
import org.iobserve.analysis.graph.ModelGraph;
import org.iobserve.planning.data.PlanningData;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class selects a created candidate and creates all required information
 * for further processing.
 * 
 * @author Philipp Weimann
 */
public class CandidateProcessing extends AbstractTransformation<PlanningData, AdaptationData> {

	@Override
	protected void execute(PlanningData element) throws Exception {
		AdaptationData adapdationData = element.getAdaptationData();

		InitializeModelProviders initModelProvider = new InitializeModelProviders(new File(adapdationData.getReDeploymentURI().toFileString()));
		adapdationData.setReDeploymentModelProviders(initModelProvider);
		
		GraphFactory factory = new GraphFactory();
		ModelGraph graph = factory.buildGraph(initModelProvider.getModelCollection());
		element.getAdaptationData().setReDeploymentGraph(graph);

		this.outputPort.send(adapdationData);
	}

}
