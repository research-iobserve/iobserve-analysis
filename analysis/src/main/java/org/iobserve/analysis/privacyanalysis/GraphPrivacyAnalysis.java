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

		this.init();
		element.printGraph();
		this.startPrivacyAnalysis(element);
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
		ComponentNode reachingPersonalComponent = null;

		if (this.startingNode == null) {
			this.startingNode = currentComp;
		} else if (currentComp.getPrivacyLvl() == DataPrivacyLvl.PERSONAL) {
			return currentComp;
		}

		ComponentEdge[] edges = currentComp.getEdges();
		for (ComponentEdge edge : edges) {

			if (edge.getPrivacyLvl() == DataPrivacyLvl.ANONYMIZED
					|| (currentComp.getPrivacyLvl() == DataPrivacyLvl.PERSONAL && edge.getPrivacyLvl() == DataPrivacyLvl.PERSONAL)) {
				continue;
			}

			if (!usedEges.contains(edge)) {
				this.usedEges.add(edge);
				reachingPersonalComponent = this.traverseNode(edge.getEdgePartner(currentComp));

				if (reachingPersonalComponent != null) {
					currentComp.setPrivacyLvl(DataPrivacyLvl.PERSONAL);
					this.personalComponents.add(currentComp);
					break;
				}
			}
		}

		return reachingPersonalComponent;
	}

}
