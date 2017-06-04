package org.iobserve.evaluation;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.GraphFactory;
import org.iobserve.analysis.graph.ModelGraph;

import teetime.stage.basic.AbstractFilter;
import teetime.stage.basic.AbstractTransformation;

public class ModelComparer extends AbstractTransformation<URI, Boolean> {
	
	private AdaptationData adaptationData;

	@Override
	protected void execute(URI element) throws Exception {
		
		boolean equalGraphs = false;
		if (adaptationData != null)
		{
			//TODO finish
			InitializeModelProviders modelProviders = new InitializeModelProviders(new File(element.toFileString()));
			
			GraphFactory graphFactory = new GraphFactory();
			ModelGraph runtimeGraph = graphFactory.buildGraph(modelProviders.getModelCollection());
			
			if (runtimeGraph.equals(adaptationData.getReDeploymentGraph()) && adaptationData.getReDeploymentGraph().equals(runtimeGraph)) {
				equalGraphs = true;
				SystemEvaluation.disableEvaluation();
			}
		}
		this.outputPort.send(new Boolean(equalGraphs));
	}

	public void setBaseData(AdaptationData adaptationData) {
		this.adaptationData = adaptationData;
	}

}
