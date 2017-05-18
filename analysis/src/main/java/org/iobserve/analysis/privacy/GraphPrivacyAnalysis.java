package org.iobserve.analysis.privacy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.graph.ComponentEdge;
import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.analysis.graph.PrivacyAnalysisModel;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyContextPrivacy;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

import teetime.stage.basic.AbstractTransformation;

public class GraphPrivacyAnalysis extends AbstractTransformation<PrivacyAnalysisModel, URI> {

	@Override
	protected void execute(PrivacyAnalysisModel element) throws Exception {

		if (element == null) {
			System.err.println("Privacy Analysis Model is null. Aborting!");
			return;
		}

		System.out.print("Starting Privacy Analysis ... ");
		System.out.println("Component PrivacyLvl analysis ...");
		element.printGraph();

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
		
		this.writeComponentClassificationToPCM(element);
		
		if (!legalDeployment)
			this.outputPort.send(element.getPcmModelsURI());
	}

	private void writeComponentClassificationToPCM(PrivacyAnalysisModel model) {
		EList<AssemblyContext> acs = model.getPcmModels().getSystemModelProvider().getModel().getAssemblyContexts__ComposedStructure();

		for (AssemblyContext ac : acs) {
			if (ac instanceof AssemblyContextPrivacy) {
				AssemblyContextPrivacy acp = (AssemblyContextPrivacy) ac;

				Optional<ComponentNode> optCom = model.getComponents().stream().filter(s -> s.getAssemblyContextID().equals(acp.getId())).findFirst();
				if (optCom.isPresent())
					acp.setPrivacyLevel(optCom.get().getPrivacyLvl());
				else
					System.err.println("No ComponentNode equivalent to AssemblyContext: " + acp.getId() + " was found!");
			} else
				System.err.println("AssemblyContext: " + ac.getId() + " is not of type AssemblyContextPrivacy!");
		}
		
		model.getPcmModels().getSystemModelProvider().save();
	}

}
