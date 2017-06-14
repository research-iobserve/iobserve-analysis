package org.iobserve.analysis.privacyanalysis;

import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.analysis.graph.ModelGraph;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.privacy.ComponentClassificationAnalysis;
import org.iobserve.analysis.privacy.DeploymentAnalysis;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyContextPrivacy;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

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
		ComponentClassificationAnalysis classificationAnalysis = new ComponentClassificationAnalysis(graph);
		classificationAnalysis.start();

		DeploymentAnalysis deploymentAnalysis = new DeploymentAnalysis(graph, PrivacyAnalysis.getLegalPersonalGeoLocations());
		String[] legalDeployments = deploymentAnalysis.start();

		if (legalDeployments.length == 0) {
			PrivacyAnalysis.LOG.info("Legal Deployment");
		} else {
			PrivacyAnalysis.LOG.error("ILLEGAL Deployment");

			PrivacyAnalysis.LOG.info("\n" + graph.printGraph(true));
			
			for (String illegalDeployment : legalDeployments) {
				PrivacyAnalysis.LOG.info(illegalDeployment);
			}
		}

		if (legalDeployments.length > 0)
			this.outputPort.send(element);
	}


}
