package org.iobserve.adaption.data;

import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.graph.ComponentNode;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.systemadaptationFactory;
import org.iobserve.planning.systemadaptation.impl.ChangeRepositoryComponentActionImpl;
import org.iobserve.planning.systemadaptation.impl.systemadaptationFactoryImpl;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

public class ActionFactory {

	protected static InitializeModelProviders runtimeModels;
	protected static InitializeModelProviders redeploymentModels;

	/*
	 * ////////// HELPERS //////////
	 */
	protected static AssemblyContext getAssemblyContext(String contextID, System systemModel) {
		AssemblyContext assemblyContext = systemModel.getAssemblyContexts__ComposedStructure().stream().filter(s -> s.getId().equals(contextID))
				.findFirst().get();
		return assemblyContext;
	}

	protected static AllocationContext getAllocationContext(String allocationID, Allocation allocationModel) {
		AllocationContext allocationContext = allocationModel.getAllocationContexts_Allocation().stream().filter(s -> s.getId().equals(allocationID))
				.findFirst().get();
		return allocationContext;
	}

	protected static ResourceContainer getResourceContainer(String resourceContainerID, ResourceEnvironment resEnvModel) {
		ResourceContainer resContainer = resEnvModel.getResourceContainer_ResourceEnvironment().stream()
				.filter(s -> s.getId().equals(resourceContainerID)).findFirst().get();

		return resContainer;
	}
}
