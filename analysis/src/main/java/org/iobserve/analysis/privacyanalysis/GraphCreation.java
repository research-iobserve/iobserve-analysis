package org.iobserve.analysis.privacyanalysis;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.GraphFactory;
import org.iobserve.analysis.graph.ModelGraph;
import org.iobserve.analysis.utils.TimingHelper;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class extracts the required information from the pcm models and
 * re-assembles them to a PrivacyAnalysisModel for privacy analysis purposes.
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public class GraphCreation extends AbstractTransformation<URI, AdaptationData> {

	private GraphFactory graphFactory;

	/**
	 * The GraphCreation constructor.
	 */
	public GraphCreation() {
		this.graphFactory = new GraphFactory();
	}

	@Override
	protected void execute(URI element) throws Exception {
		PrivacyAnalysis.LOG.info("Creating graph \tModel: " + element.toFileString());
		TimingHelper.start("Start - Reading Model");

		AdaptationData adaptionData = new AdaptationData();
		adaptionData.setRuntimeModelURI(element);

		PrivacyAnalysis.LOG.info("Creating Model Providers");
		InitializeModelProviders initModelProvider = new InitializeModelProviders(new File(element.toFileString()));
		PrivacyAnalysis.LOG.info("DONE!");
		adaptionData.setRuntimeModelProviders(initModelProvider);
		TimingHelper.createRound("Building Graph");

		ModelGraph graph = this.graphFactory.buildGraph(initModelProvider.getModelCollection());
		adaptionData.setRuntimeGraph(graph);

		outputPort.send(adaptionData);
	}
}
