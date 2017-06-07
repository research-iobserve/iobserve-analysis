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

public class AllocationModification {

	private Allocation allocationModel;

	private System systemModel;
	private ResourceEnvironment resEnvModel;
	private ResourceContainer[] resContainer;
	private HashMap<ResourceContainer, List<AllocationContext>> resContainer2AllocationContext;
	private HashMap<AssemblyContext, AllocationContext> assemblyCon2AllocationContext;

	public AllocationModification(Allocation allocationModel, System systemModel, ResourceEnvironment resEnvModel) {
		this.allocationModel = allocationModel;
		this.systemModel = systemModel;
		this.resEnvModel = resEnvModel;

		int resContainerCount = resEnvModel.getResourceContainer_ResourceEnvironment().size();
		this.resContainer = resEnvModel.getResourceContainer_ResourceEnvironment().toArray(new ResourceContainer[resContainerCount]);

		this.initResContainer2AllocationContext();
	}

	private void initResContainer2AllocationContext() {
		this.resContainer2AllocationContext = new HashMap<ResourceContainer, List<AllocationContext>>();
		this.assemblyCon2AllocationContext = new HashMap<AssemblyContext, AllocationContext>();

		for (AllocationContext ac : this.allocationModel.getAllocationContexts_Allocation()) {

			ResourceContainer resCon = ac.getResourceContainer_AllocationContext();
			if (!this.resContainer2AllocationContext.containsKey(resCon)) {
				this.resContainer2AllocationContext.put(resCon, new LinkedList<AllocationContext>());
			}
			this.resContainer2AllocationContext.get(resCon).add(ac);

			AssemblyContext assemblyCon = ac.getAssemblyContext_AllocationContext();
			if (!this.assemblyCon2AllocationContext.containsKey(assemblyCon)) {
				this.assemblyCon2AllocationContext.put(assemblyCon, ac);
			} else {
				throw new RuntimeException("An assembly context was found twice during assembly context analysis!");
			}
		}
	}

	/**
	 * 
	 * @param terminatedResContainers
	 * @return
	 */
	public int modifyAllocation_FixTerminations(List<ResourceContainer> terminatedResContainers) {

		int migrationsMade = 0;

		for (ResourceContainer terminatedResCon : terminatedResContainers) {
			if (this.resContainer2AllocationContext.containsKey(terminatedResCon))
				for (AllocationContext allocation : this.resContainer2AllocationContext.get(terminatedResCon)) {
					migrateToRandomResourceContainer(allocation);
					migrationsMade++;
				}
		}

		return migrationsMade;
	}

	/**
	 * 
	 * @param deallocatedAssemblyContexts
	 */
	public void modifyAllocation_FixDeallocations(List<AssemblyContext> deallocatedAssemblyContexts) {

		for (AssemblyContext deallocatedAssemblyCon : deallocatedAssemblyContexts) {
			if (this.assemblyCon2AllocationContext.containsKey(deallocatedAssemblyCon)) {
				AllocationContext allocationCon = this.assemblyCon2AllocationContext.get(deallocatedAssemblyCon);
				this.allocationModel.getAllocationContexts_Allocation().remove(allocationCon);
			}
		}
	}

	/**
	 * 
	 * @param allocatedAssemblyContexts
	 */
	public void modifyAllocation_FixAllocations(List<AssemblyContext> allocatedAssemblyContexts) {

		AllocationGeneration allocGen = new AllocationGeneration(this.allocationModel, this.systemModel, this.resEnvModel);
		allocGen.generateAllocation(allocatedAssemblyContexts, "MOD");
	}

	/**
	 * 
	 * 
	 * @param migarions
	 * @return
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
	 * 
	 */
	private void migrateToRandomResourceContainer(AllocationContext allocation) {
		int randomIndex = ThreadLocalRandom.current().nextInt(this.resContainer.length);
		ResourceContainer replaceResCon = this.resContainer[randomIndex];
		allocation.setResourceContainer_AllocationContext(replaceResCon);
	}

}
