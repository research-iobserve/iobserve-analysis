package org.iobserve.analysis.privacy.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;

/**
 * This class contains a model for privacy analysis purposes.
 * 
 * @author Philipp Weimann
 *
 */
public class PrivacyAnalysisModel {

	private Set<DeploymentNode> servers;
	private Set<ComponentNode> components;

	public PrivacyAnalysisModel(Collection<DeploymentNode> servers, Collection<ComponentNode> components) {
		this.servers = new HashSet<DeploymentNode>(servers);
		this.components = new HashSet<ComponentNode>(components);

//		this.printGraph();
	}

	public void printGraph() {
		for (DeploymentNode server : getServers()) {
			System.out.println(server.toString());
		}
	}

	/**
	 * @return the servers
	 */
	public Set<DeploymentNode> getServers() {
		return servers;
	}

	/**
	 * @return the components
	 */
	public Set<ComponentNode> getComponents() {
		return components;
	}

}
