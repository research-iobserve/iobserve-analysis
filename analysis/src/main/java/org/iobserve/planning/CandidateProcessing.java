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
		CandidateGeneration.LOG.info("Candiate Processing");
		AdaptationData adapdationData = element.getAdaptationData();
		
		GraphFactory factory = new GraphFactory();
		ModelGraph graph = factory.buildGraph(new InitializeModelProviders(new File(adapdationData.getReDeploymentURI().toFileString())));
		element.getAdaptationData().setReDeploymentGraph(graph);

		this.outputPort.send(element.getAdaptationData());
	}

}
