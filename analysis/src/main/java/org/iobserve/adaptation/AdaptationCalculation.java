package org.iobserve.adaptation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.iobserve.adaptation.data.ActionFactory;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.adaptation.data.AssemblyContextActionFactory;
import org.iobserve.adaptation.data.ResourceContainerActionFactory;
import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.analysis.graph.DeploymentNode;
import org.iobserve.analysis.graph.ModelGraph;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.TerminateAction;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class is the inital phase of the adaption filter stage. It compares a
 * runtime PCM to a redeployment PCM and calculates systemadaption
 * {@link Action}s to transform the deployed system towards the redeployment
 * model.
 * 
 * @author Philipp Weimann
 *
 */
public class AdaptationCalculation extends AbstractTransformation<AdaptationData, AdaptationData> {

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
		
		SystemAdaptation.LOG.info("Calculating system adaptation");

		this.init(element);
		this.addRuntimeData(element.getRuntimeGraph());
		this.startComparison(element.getReDeploymentGraph());

		element.setAcActions(this.acActions.stream().collect(Collectors.toList()));
		element.setRcActions(this.rcActions.stream().collect(Collectors.toList()));
		
		this.outputPort.send(element);
	}

	private void addRuntimeData(ModelGraph graph) {
		for (ComponentNode component : graph.getComponents()) {
			this.runtimeComponentNodes.put(component.getAssemblyContextID(), component);
		}

		for (DeploymentNode server : graph.getServers()) {
			if (server.getContainingComponents().size() > 0) {
				// Don't add servers which don't host any components
				this.runtimeDeploymentNodes.put(server.getResourceContainerID(), server);
			}
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
		for (DeploymentNode reDeplServer : servers) {

			if (reDeplServer.getContainingComponents().size() == 0) {
				// If the server dosn't contain any components => IGNORE
				continue;
			}

			DeploymentNode runServer = this.runtimeDeploymentNodes.get(reDeplServer.getResourceContainerID());

			if (runServer == null) {
				// It is an so far unused server!
				AcquireAction action = ResourceContainerActionFactory.generateAcquireAction(reDeplServer);
				this.rcActions.add(action);
			} else {
				// Server was and is still in use
				this.runtimeDeploymentNodes.remove(runServer.getResourceContainerID(), runServer);
			}
		}

		for (DeploymentNode runServer : this.runtimeDeploymentNodes.values()) {
			// AssemblyContext does not exist anymore in redeployment model!
			TerminateAction action = ResourceContainerActionFactory.generateTerminateAction(runServer);
			this.rcActions.add(action);
		}
	}
}
