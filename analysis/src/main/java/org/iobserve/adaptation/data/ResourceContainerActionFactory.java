package org.iobserve.adaptation.data;

import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.analysis.graph.DeploymentNode;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.TerminateAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;
import org.iobserve.planning.systemadaptation.impl.systemadaptationFactoryImpl;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * This class provides a factory for system adaption Actions. It provides all
 * basic functions. Initialize the static fields {@value runtimeModels} and
 * {@value redeploymentModels} before using this class.
 * 
 * @author Philipp Weimann
 */
public class ResourceContainerActionFactory extends ActionFactory {

	private static ResourceContainerAction setSourceResourceContainer(ResourceContainerAction action, String resourceContainerID) {
		ResourceEnvironment resEnvModel = ActionFactory.runtimeModels.getResourceEnvironmentModel();
		ResourceContainer resourceContainer = ActionFactory.getResourceContainer(resourceContainerID, resEnvModel);
		action.setSourceResourceContainer(resourceContainer);
		return action;
	}

	public static TerminateAction generateTerminateAction(DeploymentNode runtimeServer) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		TerminateAction action = factory.createTerminateAction();

		ResourceContainerActionFactory.setSourceResourceContainer(action, runtimeServer.getResourceContainerID());

		return action;
	}
	
	public static AcquireAction generateAcquireAction(DeploymentNode reDeploymentServer) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		AcquireAction action = factory.createAcquireAction();

		ResourceEnvironment reDeplResEnvModel = ActionFactory.redeploymentModels.getResourceEnvironmentModel();
		ResourceContainer resourceContainer = ActionFactory.getResourceContainer(reDeploymentServer.getResourceContainerID(), reDeplResEnvModel);
		action.setSourceResourceContainer(resourceContainer);

		return action;
	}

	public static ReplicateAction generateReplicateAction(DeploymentNode runtimeServer, DeploymentNode reDeploymentServer) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		ReplicateAction action = factory.createReplicateAction();

		ResourceContainerActionFactory.setSourceResourceContainer(action, runtimeServer.getResourceContainerID());

		Allocation runtimeAllocModel = ActionFactory.runtimeModels.getAllocationModel();
		for (ComponentNode component : runtimeServer.getContainingComponents()) {
			AllocationContext oldAllocationContext = ActionFactory.getAllocationContext(component.getAllocationContextID(), runtimeAllocModel);
			action.getSourceAllocationContext().add(oldAllocationContext);
		}

		Allocation reDeplAllocModel = ActionFactory.redeploymentModels.getAllocationModel();
		for (ComponentNode component : reDeploymentServer.getContainingComponents()) {
			AllocationContext newAllocationContext = ActionFactory.getAllocationContext(component.getAllocationContextID(), reDeplAllocModel);
			action.getSourceAllocationContext().add(newAllocationContext);
		}

		ResourceEnvironment resEnvModel = redeploymentModels.getResourceEnvironmentModel();
		ResourceContainer newResourceContainer = ActionFactory.getResourceContainer(reDeploymentServer.getResourceContainerID(), resEnvModel);
		action.setNewResourceContainer(newResourceContainer);

		return action;
	}

}
