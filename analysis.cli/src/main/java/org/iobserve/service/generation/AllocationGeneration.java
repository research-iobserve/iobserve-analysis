package org.iobserve.service.generation;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

public class AllocationGeneration {

	private static AllocationFactory ALLOCATION_FACTORY = AllocationFactory.eINSTANCE;
	private Allocation allocationModel;

	private System systemModel;
	private ResourceEnvironment resEnvModel;
	private ResourceContainer[] resContainer;
	// private HashMap<String, ResourceContainer> hostingServer;

	public AllocationGeneration(System systemModel, ResourceEnvironment resEnvModel) {
		this.systemModel = systemModel;
		this.resEnvModel = resEnvModel;

		// this.hostingServer = new HashMap<String, ResourceContainer>();
		int resContainerCount = resEnvModel.getResourceContainer_ResourceEnvironment().size();
		this.resContainer = resEnvModel.getResourceContainer_ResourceEnvironment().toArray(new ResourceContainer[resContainerCount]);

		this.allocationModel = ALLOCATION_FACTORY.createAllocation();
		this.allocationModel.setEntityName(systemModel.getEntityName());
		this.allocationModel.setSystem_Allocation(this.systemModel);
		this.allocationModel.setTargetResourceEnvironment_Allocation(this.resEnvModel);
	}

	public AllocationGeneration(Allocation allocationModel, System systemModel, ResourceEnvironment resEnvModel) {
		this.systemModel = systemModel;
		this.resEnvModel = resEnvModel;
		this.allocationModel = allocationModel;

		// this.hostingServer = new HashMap<String, ResourceContainer>();
		int resContainerCount = resEnvModel.getResourceContainer_ResourceEnvironment().size();
		this.resContainer = resEnvModel.getResourceContainer_ResourceEnvironment().toArray(new ResourceContainer[resContainerCount]);
	}

	/**
	 * 
	 * @return
	 */
	public Allocation generateAllocation() {
		for (AssemblyContext assemblyContext : this.systemModel.getAssemblyContexts__ComposedStructure()) {

			AllocationContext allocationContext = generateAllocationContext(assemblyContext);
			this.allocationModel.getAllocationContexts_Allocation().add(allocationContext);
		}

		return this.allocationModel;
	}

	/**
	 * 
	 * @param newAssemblyContexts
	 * @param prefix
	 * @return
	 */
	public Allocation generateAllocation(List<AssemblyContext> newAssemblyContexts, String prefix) {

		for (AssemblyContext assemblyContext : newAssemblyContexts) {

			AllocationContext allocationContext = generateAllocationContext(assemblyContext);
			allocationContext.setEntityName(prefix + ": " + allocationContext.getEntityName());
			this.allocationModel.getAllocationContexts_Allocation().add(allocationContext);
		}

		return this.allocationModel;
	}

	/*
	 * 
	 */
	private AllocationContext generateAllocationContext(AssemblyContext assemblyContext) {
		AllocationContext allocationContext = ALLOCATION_FACTORY.createAllocationContext();
		allocationContext.setAssemblyContext_AllocationContext(assemblyContext);

		int randomIndex = ThreadLocalRandom.current().nextInt(resContainer.length);

		allocationContext.setResourceContainer_AllocationContext(this.resContainer[randomIndex]);
		allocationContext.setEntityName(assemblyContext.getEntityName() + " @ " + this.resContainer[randomIndex].getEntityName());
		return allocationContext;
	}

}
