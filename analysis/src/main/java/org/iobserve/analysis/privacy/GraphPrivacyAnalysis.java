package org.iobserve.analysis.privacy;

import java.util.Optional;

import org.eclipse.emf.common.util.EList;
import org.iobserve.adaption.data.AdapdationData;
import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.analysis.graph.ModelGraph;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyContextPrivacy;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

import teetime.stage.basic.AbstractTransformation;

public class GraphPrivacyAnalysis extends AbstractTransformation<AdapdationData, AdapdationData> {

	@Override
	protected void execute(AdapdationData element) throws Exception {

		if (element == null || element.getRuntimeGraph() == null) {
			System.err.println("Privacy Analysis Model is null. Aborting!");
			return;
		}
		
		ModelGraph graph = element.getRuntimeGraph();
		

		System.out.print("Starting Privacy Analysis ... ");
		System.out.println("Component PrivacyLvl analysis ...");
		graph.printGraph();

		ComponentClassificationAnalysis classificationAnalysis = new ComponentClassificationAnalysis(graph);
		classificationAnalysis.start();

		System.out.println("Deployment analysis ... ");
		DeploymentAnalysis deploymentAnalysis = new DeploymentAnalysis(graph);
		boolean legalDeployment = deploymentAnalysis.start();

		if (legalDeployment)
			System.out.println("The deployment is LEGAL");
		else
			System.err.println("The deployment is ILLEGAL");

		graph.printGraph();
		
		this.writeComponentClassificationToPCM(graph);
		
		if (!legalDeployment)
			this.outputPort.send(element);
	}

	private void writeComponentClassificationToPCM(ModelGraph model) {
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
