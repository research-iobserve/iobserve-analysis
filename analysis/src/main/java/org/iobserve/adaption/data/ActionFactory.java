package org.iobserve.adaption.data;

import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;
import org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl;
import org.iobserve.planning.systemadaptation.impl.systemadaptationFactoryImpl;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.system.System;

public class ActionFactory {

	public static InitializeModelProviders models;

	/**
	 * 
	 * @param runtimeNode
	 * @param reDeploymentNode
	 * @return
	 */
	public static ChangeRepositoryComponentAction generateChangeRepositoryComponentAction(ComponentNode runtimeNode, ComponentNode reDeploymentNode) {
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		ChangeRepositoryComponentAction action = factory.createChangeRepositoryComponentAction();
		
		action.setSourceAssemblyContext(ActionFactory.getAssemblyContext(runtimeNode.getAssemblyContextID()));

		Repository repositoryModel = models.getRepositoryModelProvider().getModel();
		RepositoryComponent repositoryComponent = repositoryModel.getComponents__Repository().stream()
				.filter(s -> s.getId() == reDeploymentNode.getRepositoryComponentID()).findFirst().get();
		action.setNewRepositoryComponent(repositoryComponent);

		return action;
	}
	
	public static MigrateAction generateMigrateAction(ComponentNode runtimeNode, ComponentNode reDeploymentNode)
	{
		systemadaptationFactory factory = systemadaptationFactoryImpl.eINSTANCE;
		MigrateAction action = factory.createMigrateAction();
		
		action.setSourceAssemblyContext(ActionFactory.getAssemblyContext(runtimeNode.getAssemblyContextID()));
		
		action.setNewAllocatinContext(ActionFactory.getAllocationContext(runtimeNode.getAssemblyContextID()));
		return action;
	}
	
	

	/*
	 * ////////// HELPERS //////////
	 */
	private static AssemblyContext getAssemblyContext(String contextID) {
		System systemModel = models.getSystemModelProvider().getModel();
		AssemblyContext assemblyContext = systemModel.getAssemblyContexts__ComposedStructure().stream().filter(s -> s.getId() == contextID)
				.findFirst().get();
		return assemblyContext;
	}
	
	private static AllocationContext getAllocationContext(String allocationID)
	{
		Allocation allocationModel = models.getAllocationModelProvider().getModel();
		AllocationContext allocationContext = allocationModel.getAllocationContexts_Allocation().stream().filter(s -> s.getId() == allocationID)
				.findFirst().get();
		return allocationContext;
	}
}
