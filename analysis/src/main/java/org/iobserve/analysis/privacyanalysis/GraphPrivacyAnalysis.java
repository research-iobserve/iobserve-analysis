package org.iobserve.analysis.privacyanalysis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iobserve.analysis.privacy.graph.ComponentEdge;
import org.iobserve.analysis.privacy.graph.ComponentNode;
import org.iobserve.analysis.privacy.graph.ComponentNode;
import org.iobserve.analysis.privacy.graph.PrivacyAnalysisModel;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;

import teetime.stage.basic.AbstractTransformation;

public class GraphPrivacyAnalysis extends AbstractTransformation<PrivacyAnalysisModel, Boolean> {

	@Override
	protected void execute(PrivacyAnalysisModel element) throws Exception {

		if (element == null) {
			System.err.println("Privacy Analysis Model is null. Aborting!");
			return;
		}
		
//		element.printGraph();

		System.out.print("Starting Privacy Analysis ... ");
		System.out.print("Component PrivacyLvl analysis ...");
		
		ComponentClassificationAnalysis classificationAnalysis = new ComponentClassificationAnalysis(element);
		classificationAnalysis.start();

		System.out.println("Deployment analysis ... ");
		DeploymentAnalysis deploymentAnalysis = new DeploymentAnalysis(element);
		boolean legalDeployment = deploymentAnalysis.start();

		if (legalDeployment)
			System.out.println("The deployment is LEGAL");
		else
			System.err.println("The deployment is ILLEGAL");

		element.printGraph();
	}

}
