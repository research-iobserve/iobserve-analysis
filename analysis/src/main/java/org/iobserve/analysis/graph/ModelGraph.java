package org.iobserve.analysis.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.iobserve.analysis.InitializeModelProviders;

/**
 * This class contains a model for privacy analysis purposes.
 * 
 * @author Philipp Weimann
 *
 */
public class ModelGraph {

	private Set<DeploymentNode> servers;
	private Set<ComponentNode> components;
	private InitializeModelProviders pcmModels;

	public ModelGraph(Collection<DeploymentNode> servers, Collection<ComponentNode> components, InitializeModelProviders pcmModels) {
		this.servers = new HashSet<DeploymentNode>(servers);
		this.components = new HashSet<ComponentNode>(components);
		this.pcmModels = pcmModels;
		// this.printGraph();
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

	/**
	 * @return the pcmModels
	 */
	public InitializeModelProviders getPcmModels() {
		return pcmModels;
	}

}
