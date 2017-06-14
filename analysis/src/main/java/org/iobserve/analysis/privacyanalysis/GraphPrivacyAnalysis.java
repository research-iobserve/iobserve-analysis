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
			System.err.println("Privacy Analysis Model is null. Aborting!");
			return;
		}

		PrivacyAnalysis.LOG.info("Analysing graph");

		ModelGraph graph = element.getRuntimeGraph();

		// System.out.print("Starting Privacy Analysis ... ");
		// System.out.println("Component PrivacyLvl analysis ...");
		// graph.printGraph();

		ComponentClassificationAnalysis classificationAnalysis = new ComponentClassificationAnalysis(graph);
		classificationAnalysis.start();

		// System.out.println("Deployment analysis ... ");
		DeploymentAnalysis deploymentAnalysis = new DeploymentAnalysis(graph, PrivacyAnalysis.getLegalPersonalGeoLocations());
		String[] legalDeployment = deploymentAnalysis.start();

		if (legalDeployment.length == 0)
			PrivacyAnalysis.LOG.info("Legal Deployment");
		// System.out.println("The deployment is LEGAL");
		else
			PrivacyAnalysis.LOG.error("ILLEGAL Deployment");
		// System.err.println("The deployment is ILLEGAL");

		// graph.printGraph();

		// TODO Fix @ generation!
		// this.writeComponentClassificationToPCM(graph, element.getRuntimeModelProviders().getSystemModelProvider());

		if (legalDeployment.length > 0)
			this.outputPort.send(element);
	}

	/*
	 * Writes the DataPrivacyLevel into the AssemblyContextPrivacy PCM element.
	 */
	private void writeComponentClassificationToPCM(ModelGraph modelGraph, SystemModelProvider systemModelProviders) {
		EList<AssemblyContext> acs = modelGraph.getPcmModels().getSystemModel().getAssemblyContexts__ComposedStructure();

		for (AssemblyContext ac : acs) {
			if (ac instanceof AssemblyContextPrivacy) {
				AssemblyContextPrivacy acp = (AssemblyContextPrivacy) ac;

				Optional<ComponentNode> optCom = modelGraph.getComponents().stream().filter(s -> s.getAssemblyContextID().equals(acp.getId()))
						.findFirst();
				if (optCom.isPresent())
					acp.setPrivacyLevel(optCom.get().getPrivacyLvl());
				else
					System.err.println("No ComponentNode equivalent to AssemblyContext: " + acp.getId() + " was found!");
			} else
				System.err.println("AssemblyContext: " + ac.getId() + " is not of type AssemblyContextPrivacy!");
		}

		if (modelGraph.getPcmModels().getSystemModel() == systemModelProviders.getModel())
			systemModelProviders.save();
		else
			throw new IllegalArgumentException("The model graph and the system model provider don't mach. They represent different models!");
	}

}