package org.iobserve.adaptation.data;

import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;
import org.iobserve.planning.systemadaptation.impl.systemadaptationFactoryImpl;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 * This class provides a factory for system adaption Actions. It provides all
 * basic functions. Initialize the static fields {@value runtimeModels} and
 * {@value redeploymentModels} before using this class.
 *
 * @author Philipp Weimann
 */
public class AssemblyContextActionFactory extends ActionFactory {

	private static AssemblyContextAction setSourceAssemblyContext(AssemblyContextAction action,
			String assemblyContextID) {
		org.palladiosimulator.pcm.system.System systemModel = ActionFactory.runtimeModels.getSystemModel();
		AssemblyContext assemblyContext = ActionFactory.getAssemblyContext(assemblyContextID, systemModel);
		action.setSourceAssemblyContext(assemblyContext);
		return action;
	}

	public static ChangeRepositoryComponentAction generateChangeRepositoryComponentAction(ComponentNode runtimeNode,
			ComponentNode reDeploymentNode) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		ChangeRepositoryComponentAction action = factory.createChangeRepositoryComponentAction();

		AssemblyContextActionFactory.setSourceAssemblyContext(action, runtimeNode.getAssemblyContextID());

		Repository repositoryModel = redeploymentModels.getRepositoryModel();
		RepositoryComponent repositoryComponent = repositoryModel.getComponents__Repository().stream()
				.filter(s -> s.getId().equals(reDeploymentNode.getRepositoryComponentID())).findFirst().get();
		action.setNewRepositoryComponent(repositoryComponent);

		return action;
	}

	public static MigrateAction generateMigrateAction(ComponentNode runtimeNode, ComponentNode reDeploymentNode) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		MigrateAction action = factory.createMigrateAction();

		AssemblyContextActionFactory.setSourceAssemblyContext(action, runtimeNode.getAssemblyContextID());

		Allocation runAllocation = ActionFactory.runtimeModels.getAllocationModel();
		action.setSourceAllocationContext(
				ActionFactory.getAllocationContext(runtimeNode.getAllocationContextID(), runAllocation));

		Allocation reDeplAllocation = ActionFactory.redeploymentModels.getAllocationModel();
		action.setNewAllocationContext(
				ActionFactory.getAllocationContext(reDeploymentNode.getAllocationContextID(), reDeplAllocation));
		return action;
	}

	public static DeallocateAction generateDeallocateAction(ComponentNode runtimeNode) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		DeallocateAction action = factory.createDeallocateAction();

		AssemblyContextActionFactory.setSourceAssemblyContext(action, runtimeNode.getAssemblyContextID());

		Allocation runAllocation = ActionFactory.runtimeModels.getAllocationModel();
		action.setOldAllocationContext(
				ActionFactory.getAllocationContext(runtimeNode.getAllocationContextID(), runAllocation));

		return action;
	}

	public static AllocateAction generateAllocateAction(ComponentNode runtimeNode, ComponentNode reDeploymentNode) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		AllocateAction action = factory.createAllocateAction();

		// Allcotaion has no runtime component
		// AssemblyContextActionFactory.setSourceAssemblyContext(action,
		// runtimeNode.getAssemblyContextID());

		org.palladiosimulator.pcm.system.System reDeplSystem = ActionFactory.redeploymentModels.getSystemModel();
		action.setSourceAssemblyContext(
				ActionFactory.getAssemblyContext(reDeploymentNode.getAssemblyContextID(), reDeplSystem));

		Allocation reDeplAllocation = ActionFactory.redeploymentModels.getAllocationModel();
		action.setNewAllocationContext(
				ActionFactory.getAllocationContext(reDeploymentNode.getAllocationContextID(), reDeplAllocation));
		return action;
	}
}
