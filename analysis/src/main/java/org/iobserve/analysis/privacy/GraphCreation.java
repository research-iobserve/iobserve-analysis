package org.iobserve.analysis.privacy;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.GraphFactory;
import org.iobserve.analysis.graph.ModelGraph;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class extracts the required information from the pcm models and re-assembles them to a PrivacyAnalysisModel for privacy analysis purposes.
 * 
 * @author Philipp Weimann
 */
public class GraphCreation extends AbstractTransformation<URI, AdaptationData> {
	
	private GraphFactory graphFactory;
	
	/**
	 * Empty Constructor
	 */
	public GraphCreation() {
		this.graphFactory = new GraphFactory();
	}
	

	@Override
	protected void execute(URI element) throws Exception {
		AdaptationData adaptionData = new AdaptationData();
		adaptionData.setRuntimeModelURI(element);
		
		InitializeModelProviders modelProvider = new InitializeModelProviders(new File(element.toFileString()));
		ModelGraph graph = this.graphFactory.buildGraph(modelProvider);
		adaptionData.setRuntimeGraph(graph);
		
		outputPort.send(adaptionData);
	}
}
