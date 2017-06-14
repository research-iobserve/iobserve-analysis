package org.iobserve.service.generation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.emf.common.util.EList;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * Modifies a PCM AllocationModel
 * 
 * @author Philipp Weimann
 * @author Robert Heinrich
 */
public class AllocationModification {

	private Allocation allocationModel;

	private System systemModel;
	private ResourceEnvironment resEnvModel;
	private ResourceContainer[] resContainer;
	private HashMap<String, List<AllocationContext>> resContainer2AllocationContext;
	private HashMap<String, AllocationContext> assemblyCon2AllocationContext;

	/**
	 * The constructor
	 * 
	 * @param allocationModel
	 *            the allocation model to modify
	 * @param systemModel
	 *            the system model
	 * @param resEnvModel
	 *            the resource environment model
	 */
	public AllocationModification(Allocation allocationModel, System systemModel, ResourceEnvironment resEnvModel) {
		this.allocationModel = allocationModel;
		this.systemModel = systemModel;
		this.resEnvModel = resEnvModel;

		int resContainerCount = resEnvModel.getResourceContainer_ResourceEnvironment().size();
		this.resContainer = resEnvModel.getResourceContainer_ResourceEnvironment().toArray(new ResourceContainer[resContainerCount]);

		this.initResContainer2AllocationContext();
	}

	/*
	 * Inits all support structures
	 */
	private void initResContainer2AllocationContext() {
		this.resContainer2AllocationContext = new HashMap<String, List<AllocationContext>>();
		this.assemblyCon2AllocationContext = new HashMap<String, AllocationContext>();

		for (AllocationContext ac : this.allocationModel.getAllocationContexts_Allocation()) {

			ResourceContainer resCon = ac.getResourceContainer_AllocationContext();
			if (!this.resContainer2AllocationContext.containsKey(resCon.getId())) {
				this.resContainer2AllocationContext.put(resCon.getId(), new LinkedList<AllocationContext>());
			}
			this.resContainer2AllocationContext.get(resCon.getId()).add(ac);

			AssemblyContext assemblyCon = ac.getAssemblyContext_AllocationContext();
			if (!this.assemblyCon2AllocationContext.containsKey(assemblyCon.getId())) {
				this.assemblyCon2AllocationContext.put(assemblyCon.getId(), ac);
			} else {
				throw new RuntimeException("An assembly context was found twice during assembly context analysis!");
			}
		}
	}

	/**
	 * Modifies allocations due to terminated Resource Containers
	 * 
	 * @param terminatedResContainers
	 *            the terminated Resource Containers
	 * @return the amount of modified allocations
	 */
	public int modifyAllocation_FixTerminations(List<ResourceContainer> terminatedResContainers) {

		int migrationsMade = 0;

		for (ResourceContainer terminatedResCon : terminatedResContainers) {
			if (this.resContainer2AllocationContext.containsKey(terminatedResCon.getId()))
				for (AllocationContext allocation : this.resContainer2AllocationContext.get(terminatedResCon.getId())) {
					migrateToRandomResourceContainer(allocation);
					migrationsMade++;
				}
		}

		return migrationsMade;
	}

	/**
	 * Removes Allocations contexts due to deallocated AssemblyContexts
	 * 
	 * @param deallocatedAssemblyContexts
	 *            the deallocated Assembly Contexts
	 */
	public void modifyAllocation_FixDeallocations(List<AssemblyContext> deallocatedAssemblyContexts) {

		for (AssemblyContext deallocatedAssemblyCon : deallocatedAssemblyContexts) {

			if (this.assemblyCon2AllocationContext.containsKey(deallocatedAssemblyCon.getId())) {

				AllocationContext allocationCon = this.assemblyCon2AllocationContext.get(deallocatedAssemblyCon.getId());
				this.allocationModel.getAllocationContexts_Allocation().remove(allocationCon);
			}
		}
	}

	/**
	 * Generates new allocations for the given Assembly Contexts
	 * 
	 * @param allocatedAssemblyContexts
	 *            the Assembly Contexts to allocate
	 */
	public void modifyAllocation_FixAllocations(List<AssemblyContext> allocatedAssemblyContexts) {

		AllocationGeneration allocGen = new AllocationGeneration(this.allocationModel, this.systemModel, this.resEnvModel);
		allocGen.generateAllocation(allocatedAssemblyContexts, "MOD");
	}

	/**
	 * Generates a certain amount of migrations.
	 * 
	 * @param migarions
	 *            the amount of migrations to generates
	 * @return the amount of migrations made
	 */
	public int modifyAllocation_Migrate(int migarions) {
		int migrationsMade = 0;
		Set<AllocationContext> usedAllocationContexts = new HashSet<AllocationContext>();
		EList<AllocationContext> allocationContexts = this.allocationModel.getAllocationContexts_Allocation();

		for (int i = 0; i < migarions; i++) {
			AllocationContext ac = null;
			for (int j = 0; ac != null && j < allocationContexts.size() * 10; j++) {
				int randomIndex = ThreadLocalRandom.current().nextInt(allocationContexts.size());
				if (!usedAllocationContexts.contains(allocationContexts.get(randomIndex))) {
					ac = allocationContexts.get(randomIndex);
				}

				if (ac != null) {
					this.migrateToRandomResourceContainer(ac);
					migrationsMade++;
				}
			}
		}

		return migrationsMade;
	}

	/*
	 * Migration helper
	 */
	private void migrateToRandomResourceContainer(AllocationContext allocation) {
		int randomIndex = ThreadLocalRandom.current().nextInt(this.resContainer.length);
		ResourceContainer replaceResCon = this.resContainer[randomIndex];
		allocation.setResourceContainer_AllocationContext(replaceResCon);
	}

}
