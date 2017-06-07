package org.iobserve.analysis.privacy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iobserve.analysis.graph.ComponentEdge;
import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.analysis.graph.ModelGraph;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;

/**
 * This class analyses the given {@link PrivacyAnalysModel} for
 * "JoiningDataStreams" and adjusts the ComponentNodes DataPrivacyLevel
 * according to the analysis.
 * 
 * @author Philipp Weimann
 *
 */
public class ComponentClassificationAnalysis {

	private ModelGraph model;

	private List<ComponentNode> personalComponents;
	private HashSet<ComponentNode> dePersonalComponents;

	private ComponentNode startingNode;
	private Set<ComponentEdge> usedEges;

	/**
	 * The Constructor, does not start the analysis.
	 * 
	 * @param model
	 */
	public ComponentClassificationAnalysis(ModelGraph model) {
		this.model = model;

		this.personalComponents = new ArrayList<ComponentNode>();
		this.dePersonalComponents = new HashSet<ComponentNode>();
		this.usedEges = new HashSet<ComponentEdge>();
	}

	/**
	 * Starts the component classification analysis and adjusts the
	 * DataPrivacyLvl.
	 */
	public void start() {

		this.extractComponents();
		this.serachForJoiningDataSreams();

	}

	/*
	 * Extracts and groups the required ComponentNodes
	 */
	private void extractComponents() {
		// add nodes according to privacy lvl
		for (ComponentNode node : model.getComponents()) {
			int test = 0;
			if (node.getPrivacyLvl() == null) {
				test = 1;
			}

			switch (node.getPrivacyLvl()) {
			case PERSONAL:
				this.personalComponents.add(node);
				break;
			case DEPERSONALIZED:
				this.dePersonalComponents.add(node);
				break;
			default:
				// Ignore "anonymized" components
			}
		}
	}

	/*
	 * Starts the analysis
	 */
	private void serachForJoiningDataSreams() {

		for (int i = 0; i < personalComponents.size(); i++) {
			ComponentNode currentComponent = personalComponents.get(i);
			this.usedEges.clear();
			this.traverseNode(currentComponent);
			this.startingNode = null;
		}
	}

	/*
	 * Check if a joiningDataStream is found
	 */
	private ComponentNode traverseNode(ComponentNode currentComp) {
		// System.out.println("Traversing: " + currentComp.getAssemblyName());
		ComponentNode reachingPersonalComponent = null;

		if (this.startingNode == null) {
			// Set the start component
			this.startingNode = currentComp;
		} else if (currentComp.getPrivacyLvl() == DataPrivacyLvl.PERSONAL) {
			// Found a second privacy data source
			// System.out.println("---Joining Data Streams!!!!!");
			return currentComp;
		}

		ComponentEdge[] edges = currentComp.getEdges();
		for (ComponentEdge edge : edges) {
			// System.out.println("-Analysing Edge: " +
			// edge.getAssemblyConnectorName());

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
					// A second privacy data source was found! Adjust the
					// privacy level!
					currentComp.setPrivacyLvl(DataPrivacyLvl.PERSONAL);
					// Add as potential privacy data source
					this.personalComponents.add(currentComp);
					break;
				}
			}
		}

		// return the other joining data stream source
		return reachingPersonalComponent;
	}
}
