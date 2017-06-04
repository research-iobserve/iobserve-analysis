package org.iobserve.analysis.graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class contains a model for privacy analysis purposes.
 * 
 * @author Philipp Weimann
 *
 */
public class ModelGraph {

	private Set<DeploymentNode> servers;
	private Set<ComponentNode> components;
	private ModelCollection pcmModels;

	public ModelGraph(Collection<DeploymentNode> servers, Collection<ComponentNode> components, ModelCollection pcmModels) {
		this.servers = new HashSet<DeploymentNode>(servers);
		this.components = new HashSet<ComponentNode>(components);
		this.pcmModels = pcmModels;
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
	public ModelCollection getPcmModels() {
		return pcmModels;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ModelGraph) {
			ModelGraph compObj = (ModelGraph) obj;

			//Compare used servers
			boolean equalServers = true;
			for (DeploymentNode server : this.servers) {
				if (server.getContainingComponents().size() == 0) {
					continue;
				}

				DeploymentNode compServer = compObj.servers.stream().filter(s -> s.getResourceContainerID().equals(server.getResourceContainerID()))
						.findFirst().get();
				equalServers = server.equals(compServer);
				if (!equalServers)
					return false;
			}

			//Compare components
			boolean equalComponents = true;
			for (ComponentNode component : this.components) {
				ComponentNode compComponent = compObj.components.stream().filter(s -> s.getAssemblyContextID().equals(component.getAssemblyContextID()))
						.findFirst().get();
				equalComponents = component.equals(compComponent);
				if (!equalComponents)
					return false;
			}
		}
		return true;
	}
}
