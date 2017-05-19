package org.iobserve.adaption;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.iobserve.adaption.data.AdaptationData;
import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.analysis.graph.DeploymentNode;
import org.iobserve.analysis.graph.ModelGraph;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;

import teetime.stage.basic.AbstractTransformation;

public class ModelComparision extends AbstractTransformation<AdaptationData, AdaptationData> {

	private HashMap<String, ComponentNode> runtimeComponentNodes;
	private HashMap<String, DeploymentNode> runtimeDeploymentNodes;

	private HashSet<AssemblyContextAction> acActions;
	private HashSet<ResourceContainerAction> rcActions;

	private void init() {
		this.runtimeComponentNodes = new HashMap<String, ComponentNode>();
		this.runtimeDeploymentNodes = new HashMap<String, DeploymentNode>();

		this.acActions = new HashSet<AssemblyContextAction>();
		this.rcActions = new HashSet<ResourceContainerAction>();
	}

	@Override
	protected void execute(AdaptationData element) throws Exception {

		assert (element.getRuntimeGraph() != null);
		assert (element.getReDeploymentGraph() != null);

		this.init();
		this.addRuntimeData(element.getRuntimeGraph());
	}

	private void addRuntimeData(ModelGraph graph) {
		for (ComponentNode component : graph.getComponents()) {
			this.runtimeComponentNodes.put(component.getAssemblyContextID(), component);
		}

		for (DeploymentNode server : graph.getServers()) {
			this.runtimeDeploymentNodes.put(server.getResourceContainerID(), server);
		}
	}

	private void startComparison(ModelGraph redeploymentGraph) {
		this.compareComponents(redeploymentGraph.getComponents());
		this.compareServers(redeploymentGraph.getServers());
	}
	
	private void compareComponents(Set<ComponentNode> components) {
		for (ComponentNode reDeplComp : components) {
			
			ComponentNode runComp = this.runtimeComponentNodes.get(reDeplComp.getAssemblyContextID());
			
			if (!runComp.equals(reDeplComp))
			{
				//TODO Continue to check wich action is required
			}
			
		}
	}
	
	private void compareServers(Set<DeploymentNode> servers) {
		for (DeploymentNode server : servers) {
			
		}
	}

}
