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

	private List<ComponentNode> personalComponents;
	private HashSet<ComponentNode> dePersonalComponents;

	private void init() {
		this.personalComponents = new ArrayList<ComponentNode>();
		this.dePersonalComponents = new HashSet<ComponentNode>();

	}

	@Override
	protected void execute(PrivacyAnalysisModel element) throws Exception {

		if (element == null) {
			System.err.println("Privacy Analysis Model is null. Aborting!");
			return;
		}
		System.out.println("Starting Privacy Analysis ...");

		this.init();
		element.printGraph();
		System.out.println("Communication analysis ...");
		this.startPrivacyAnalysis(element);
		// System.out.println("Deployment analysis ...");
		element.printGraph();
	}

	private void startPrivacyAnalysis(PrivacyAnalysisModel model) {
		// add nodes according to privacy lvl
		for (ComponentNode node : model.getComponents()) {
			switch (node.getPrivacyLvl()) {
			case PERSONAL:
				this.personalComponents.add(node);
				break;
			case DEPERSONALIZED:
				this.dePersonalComponents.add(node);
				break;
			default:
			}
		}

		for (int i = 0; i < personalComponents.size(); i++) {
			ComponentNode currentComponent = personalComponents.get(i);
			usedEges = new HashSet<ComponentEdge>();
			this.traverseNode(currentComponent);
			this.startingNode = null;
		}
	}

	private ComponentNode startingNode;
	private Set<ComponentEdge> usedEges;

	private ComponentNode traverseNode(ComponentNode currentComp) {
//		System.out.println("Traversing: " + currentComp.getAssemblyName());
		ComponentNode reachingPersonalComponent = null;

		if (this.startingNode == null) {
			this.startingNode = currentComp;
		} else if (currentComp.getPrivacyLvl() == DataPrivacyLvl.PERSONAL) {
//			System.out.println("---Joining Data Streams!!!!!");
			return currentComp;
		}

		ComponentEdge[] edges = currentComp.getEdges();
		for (ComponentEdge edge : edges) {
//			System.out.println("-Analysing Edge: " + edge.getAssemblyConnectorName());

			// Jump over edge if
			// 1. anonymous connection
			// 2. personal edge, while in personal component
			if (edge.getPrivacyLvl() == DataPrivacyLvl.ANONYMIZED
					|| (currentComp.getPrivacyLvl() == DataPrivacyLvl.PERSONAL && edge.getPrivacyLvl() == DataPrivacyLvl.PERSONAL)) {
				continue;
			}

			// Don't use already used edges!
			if (!usedEges.contains(edge)) {
				this.usedEges.add(edge);
				reachingPersonalComponent = this.traverseNode(edge.getEdgePartner(currentComp));

				if (reachingPersonalComponent != null && currentComp.getPrivacyLvl() != DataPrivacyLvl.PERSONAL) {
					currentComp.setPrivacyLvl(DataPrivacyLvl.PERSONAL);
					this.personalComponents.add(currentComp);
					break;
				}
			}
		}

		return reachingPersonalComponent;
	}

}
