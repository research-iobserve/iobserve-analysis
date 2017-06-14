package org.iobserve.planning;

import java.io.File;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.GraphFactory;
import org.iobserve.analysis.graph.ModelGraph;
import org.iobserve.analysis.privacy.ComponentClassificationAnalysis;
import org.iobserve.analysis.privacy.DeploymentAnalysis;
import org.iobserve.analysis.privacyanalysis.PrivacyAnalysis;
import org.iobserve.planning.data.PlanningData;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class selects a created candidate and creates all required information
 * for further processing.
 * 
 * @author Philipp Weimann
 * @author Tobias Poeppke
 * @author Robert Heinrich
 */
public class CandidateProcessing extends AbstractTransformation<PlanningData, AdaptationData> {

	@Override
	protected void execute(PlanningData element) throws Exception {
		CandidateGeneration.LOG.info("Candiate Processing");
		
		AdaptationData adapdationData = element.getAdaptationData();
		
		// Set adaptation data
		String reDeploymentURIString = adapdationData.getReDeploymentURI().toFileString();
		InitializeModelProviders initModelProvider = new InitializeModelProviders(new File(reDeploymentURIString));
		adapdationData.setReDeploymentModelProviders(initModelProvider);
		
		GraphFactory factory = new GraphFactory();
		ModelGraph graph = factory.buildGraph(initModelProvider.getModelCollection());
		adapdationData.setReDeploymentGraph(graph);
		
		// Evaluate if ReDeploymentModel is privacy compliant
		ComponentClassificationAnalysis classificationAnalysis = new ComponentClassificationAnalysis(graph);
		classificationAnalysis.start();
		DeploymentAnalysis deploymentAnalysis = new DeploymentAnalysis(graph, PrivacyAnalysis.getLegalPersonalGeoLocations());
		String[] legalDeployment = deploymentAnalysis.start();

		if (legalDeployment.length == 0)
			CandidateGeneration.LOG.info("ReDeployment Model is legal!");
		else
			CandidateGeneration.LOG.error("ReDeployment Model is ILLEGAL");
		

		this.outputPort.send(adapdationData);
	}

}
