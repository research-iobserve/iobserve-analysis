package org.iobserve.analysis.privacyanalysis;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.graph.ModelGraph;
import org.iobserve.analysis.privacy.ComponentClassificationAnalysis;
import org.iobserve.analysis.privacy.DeploymentAnalysis;
import org.iobserve.analysis.utils.TimingHelper;

import teetime.stage.basic.AbstractTransformation;

/**
 * The actual Privacy Analysis stage.
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public class GraphPrivacyAnalysis extends AbstractTransformation<AdaptationData, AdaptationData> {

	@Override
	protected void execute(AdaptationData element) throws Exception {

		if (element == null || element.getRuntimeGraph() == null) {
			PrivacyAnalysis.LOG.error("Privacy Analysis Model is null. Aborting!");
			return;
		}
		PrivacyAnalysis.LOG.info("Analysing graph");

		ModelGraph graph = element.getRuntimeGraph();

		// PrivacyAnalysis.LOG.info("Initial Categorization");
		// PrivacyAnalysis.LOG.info("\n" + graph.printGraph(true));

		ComponentClassificationAnalysis classificationAnalysis = new ComponentClassificationAnalysis(graph);
		classificationAnalysis.start();

		// PrivacyAnalysis.LOG.info("Classification Analysis");
		// PrivacyAnalysis.LOG.info("\n" + graph.printGraph(true));

		DeploymentAnalysis deploymentAnalysis = new DeploymentAnalysis(graph, PrivacyAnalysis.getLegalPersonalGeoLocations());
		String[] legalDeployments = deploymentAnalysis.start();

		TimingHelper.end("End");

		if (legalDeployments.length == 0) {
			
			PrivacyAnalysis.LOG.info("Legal Deployment");
			PrivacyAnalysis.LOG.info("\n" + graph.printGraph(true));
			
		} else {
			PrivacyAnalysis.LOG.error("ILLEGAL Deployment");

			PrivacyAnalysis.LOG.info("\n" + graph.printGraph(true));

			for (String illegalDeployment : legalDeployments) {
				PrivacyAnalysis.LOG.info(illegalDeployment);
			}
		}

		if (legalDeployments.length > 0) {
			 this.outputPort.send(element);
		}
	}

}
