package org.iobserve.adaptation.data;

import org.iobserve.analysis.graph.ModelCollection;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * This class provides a factory for system adaption Actions. It provides all
 * basic functions. Initialize the static fields {@value runtimeModels} and
 * {@value redeploymentModels} before using this class. It is recommended not to
 * use this class directly, but {@link AssemblyContextActionFactory} and
 * {@link ResourceContainerActionFactory} directly.
 * 
 * @author Philipp Weimann
 */
public class ActionFactory {

	/** The runtime PCM */
	public static ModelCollection runtimeModels;
	/** The reDeployment PCM */
	public static ModelCollection redeploymentModels;

	/*
	 * ////////// HELPERS //////////
	 */
	/**
	 * Get the Assembly Context, from the give System-Model.
	 * 
	 * @param contextID
	 *            the requested ID
	 * @param systemModel
	 *            the system model
	 * @return the requested Assembly Context
	 */
	protected static AssemblyContext getAssemblyContext(String contextID, System systemModel) {
		AssemblyContext assemblyContext = systemModel.getAssemblyContexts__ComposedStructure().stream().filter(s -> s.getId().equals(contextID))
				.findFirst().get();
		return assemblyContext;
	}

	/**
	 * Get the Allocation Context, from the give Allocation-Model.
	 * 
	 * @param allocationID
	 *            the requested ID
	 * @param allocationModel
	 *            the allocation model
	 * @return the requested Allocation Context
	 */
	protected static AllocationContext getAllocationContext(String allocationID, Allocation allocationModel) {
		AllocationContext allocationContext = allocationModel.getAllocationContexts_Allocation().stream().filter(s -> s.getId().equals(allocationID))
				.findFirst().get();
		return allocationContext;
	}

	/**
	 * Get the Resource Container, from the give Allocation-Model.
	 * 
	 * @param resourceContainerID
	 *            the requested ID
	 * @param resEnvModel
	 *            the Resource Environment Model
	 * @return the requested Resource Container
	 */
	protected static ResourceContainer getResourceContainer(String resourceContainerID, ResourceEnvironment resEnvModel) {
		ResourceContainer resContainer = resEnvModel.getResourceContainer_ResourceEnvironment().stream()
				.filter(s -> s.getId().equals(resourceContainerID)).findFirst().get();

		return resContainer;
	}
}
