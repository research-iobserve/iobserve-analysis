package org.iobserve.adaption;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.iobserve.adaption.data.ActionFactory;
import org.iobserve.adaption.data.AdaptationData;
import org.iobserve.adaption.data.AssemblyContextActionFactory;
import org.iobserve.analysis.InitializeModelProviders;
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

	private void init(AdaptationData data) {
		this.runtimeComponentNodes = new HashMap<String, ComponentNode>();
		this.runtimeDeploymentNodes = new HashMap<String, DeploymentNode>();

		this.acActions = new HashSet<AssemblyContextAction>();
		this.rcActions = new HashSet<ResourceContainerAction>();
		
		ActionFactory.runtimeModels = data.getRuntimeGraph().getPcmModels();
		ActionFactory.redeploymentModels = data.getReDeploymentGraph().getPcmModels();
	}

	@Override
	protected void execute(AdaptationData element) throws Exception {

		assert (element.getRuntimeGraph() != null);
		assert (element.getReDeploymentGraph() != null);

		this.init(element);
		this.addRuntimeData(element.getRuntimeGraph());
		this.startComparison(element.getReDeploymentGraph());
		
		element.setAcActions(this.acActions.stream().collect(Collectors.toList()));
		element.setRcActions(this.rcActions.stream().collect(Collectors.toList()));
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

			if (runComp == null) {
				// Allocate, since ID does not yet exits
				AssemblyContextAction action = AssemblyContextActionFactory.generateAllocateAction(runComp, reDeplComp);
				this.acActions.add(action);
			} else if (!runComp.equals(reDeplComp)) {
				// Components differ, so check what actions need to be done!
				if (!runComp.getRepositoryComponentID().equals(reDeplComp.getRepositoryComponentID())) {
					// AssemblyContexts contain different RepositoryComponents
					AssemblyContextAction action = AssemblyContextActionFactory.generateChangeRepositoryComponentAction(runComp, reDeplComp);
					this.acActions.add(action);
				}
				if (!runComp.getHostServer().getResourceContainerID().equals(reDeplComp.getHostServer().getResourceContainerID())) {
					// AssemblyContexts are hosted on different Servers
					AssemblyContextAction action = AssemblyContextActionFactory.generateMigrateAction(runComp, reDeplComp);
					this.acActions.add(action);
				}
			}

			this.runtimeComponentNodes.remove(reDeplComp.getAssemblyContextID(), runComp);
		}

		for (ComponentNode runComp : this.runtimeComponentNodes.values()) {
			// AssemblyContext does not exist anymore in redeployment model!
			AssemblyContextAction action = AssemblyContextActionFactory.generateDeallocateAction(runComp);
			this.acActions.add(action);
		}
	}

	private void compareServers(Set<DeploymentNode> servers) {
		for (DeploymentNode server : servers) {

		}
	}
}
