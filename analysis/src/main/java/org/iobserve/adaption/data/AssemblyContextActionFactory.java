package org.iobserve.adaption.data;

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

public class AssemblyContextActionFactory extends ActionFactory {

	private static AssemblyContextAction setSourceAssemblyContext(AssemblyContextAction action, String assemblyContextID) {
		org.palladiosimulator.pcm.system.System systemModel = ActionFactory.runtimeModels.getSystemModelProvider().getModel();
		AssemblyContext assemblyContext = ActionFactory.getAssemblyContext(assemblyContextID, systemModel);
		action.setSourceAssemblyContext(assemblyContext);
		return action;
	}

	public static ChangeRepositoryComponentAction generateChangeRepositoryComponentAction(ComponentNode runtimeNode, ComponentNode reDeploymentNode) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		ChangeRepositoryComponentAction action = factory.createChangeRepositoryComponentAction();

		AssemblyContextActionFactory.setSourceAssemblyContext(action, runtimeNode.getAssemblyContextID());

		Repository repositoryModel = redeploymentModels.getRepositoryModelProvider().getModel();
		RepositoryComponent repositoryComponent = repositoryModel.getComponents__Repository().stream()
				.filter(s -> s.getId() == reDeploymentNode.getRepositoryComponentID()).findFirst().get();
		action.setNewRepositoryComponent(repositoryComponent);

		return action;
	}

	public static MigrateAction generateMigrateAction(ComponentNode runtimeNode, ComponentNode reDeploymentNode) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		MigrateAction action = factory.createMigrateAction();

		AssemblyContextActionFactory.setSourceAssemblyContext(action, runtimeNode.getAssemblyContextID());

		Allocation allocation = ActionFactory.redeploymentModels.getAllocationModelProvider().getModel();
		action.setNewAllocatinContext(ActionFactory.getAllocationContext(runtimeNode.getAssemblyContextID(), allocation));
		return action;
	}

	public static DeallocateAction generateDeallocateAction(ComponentNode runtimeNode) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		DeallocateAction action = factory.createDeallocateAction();

		AssemblyContextActionFactory.setSourceAssemblyContext(action, runtimeNode.getAssemblyContextID());

		return action;
	}

	public static AllocateAction generateAllocateAction(ComponentNode runtimeNode, ComponentNode reDeploymentNode) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		AllocateAction action = factory.createAllocateAction();

		// Allcotaion has no runtime component
		// AssemblyContextActionFactory.setSourceAssemblyContext(action, runtimeNode.getAssemblyContextID());

		Allocation allocation = ActionFactory.redeploymentModels.getAllocationModelProvider().getModel();
		action.setNewAllocatinContext(ActionFactory.getAllocationContext(runtimeNode.getAssemblyContextID(), allocation));
		return action;
	}
}
