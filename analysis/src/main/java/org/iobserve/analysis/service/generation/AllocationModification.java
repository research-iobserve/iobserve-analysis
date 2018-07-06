/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.service.generation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
 * ToDo .
 *
 * @author unknown
 *
 * @deprecated
 */
@Deprecated
public class AllocationModification {

    private final Allocation allocationModel;

    private final System systemModel;
    private final ResourceEnvironment resEnvModel;
    private final ResourceContainer[] resContainer;
    private Map<String, List<AllocationContext>> resContainer2AllocationContext;
    private Map<String, AllocationContext> assemblyCon2AllocationContext;

    public AllocationModification(final Allocation allocationModel, final System systemModel,
            final ResourceEnvironment resEnvModel) {
        this.allocationModel = allocationModel;
        this.systemModel = systemModel;
        this.resEnvModel = resEnvModel;

        final int resContainerCount = resEnvModel.getResourceContainer_ResourceEnvironment().size();
        this.resContainer = resEnvModel.getResourceContainer_ResourceEnvironment()
                .toArray(new ResourceContainer[resContainerCount]);

        this.initResContainer2AllocationContext();
    }

    private void initResContainer2AllocationContext() {
        this.resContainer2AllocationContext = new HashMap<>();
        this.assemblyCon2AllocationContext = new HashMap<>();

        for (final AllocationContext ac : this.allocationModel.getAllocationContexts_Allocation()) {

            final ResourceContainer resCon = ac.getResourceContainer_AllocationContext();
            if (!this.resContainer2AllocationContext.containsKey(resCon.getId())) {
                this.resContainer2AllocationContext.put(resCon.getId(), new LinkedList<AllocationContext>());
            }
            this.resContainer2AllocationContext.get(resCon.getId()).add(ac);

            final AssemblyContext assemblyCon = ac.getAssemblyContext_AllocationContext();
            if (!this.assemblyCon2AllocationContext.containsKey(assemblyCon.getId())) {
                this.assemblyCon2AllocationContext.put(assemblyCon.getId(), ac);
            } else {
                throw new RuntimeException("An assembly context was found twice during assembly context analysis!");
            }
        }
    }

    /**
     *
     *
     * @param terminatedResContainers
     * @return
     */
    public int modifyAllocationFixTerminations(final List<ResourceContainer> terminatedResContainers) {

        int migrationsMade = 0;

        for (final ResourceContainer terminatedResCon : terminatedResContainers) {
            if (this.resContainer2AllocationContext.containsKey(terminatedResCon.getId())) {
                for (final AllocationContext allocation : this.resContainer2AllocationContext
                        .get(terminatedResCon.getId())) {
                    this.migrateToRandomResourceContainer(allocation);
                    migrationsMade++;
                }
            }
        }

        return migrationsMade;
    }

    /**
     *
     * @param deallocatedAssemblyContexts
     */
    public void modifyAllocationFixDeallocations(final List<AssemblyContext> deallocatedAssemblyContexts) {

        for (final AssemblyContext deallocatedAssemblyCon : deallocatedAssemblyContexts) {

            if (this.assemblyCon2AllocationContext.containsKey(deallocatedAssemblyCon.getId())) {

                final AllocationContext allocationCon = this.assemblyCon2AllocationContext
                        .get(deallocatedAssemblyCon.getId());
                this.allocationModel.getAllocationContexts_Allocation().remove(allocationCon);
            }
        }
    }

    /**
     *
     * @param allocatedAssemblyContexts
     */
    public void modifyAllocationFixAllocations(final List<AssemblyContext> allocatedAssemblyContexts) {

        final AllocationGeneration allocGen = new AllocationGeneration(this.allocationModel, this.systemModel,
                this.resEnvModel);
        allocGen.generateAllocation(allocatedAssemblyContexts, "MOD");
    }

    /**
     *
     *
     * @param migarions
     * @return
     */
    public int modifyAllocationMigrate(final int migarions) {
        int migrationsMade = 0;
        final Set<AllocationContext> usedAllocationContexts = new HashSet<>();
        final EList<AllocationContext> allocationContexts = this.allocationModel.getAllocationContexts_Allocation();

        for (int i = 0; i < migarions; i++) {
            AllocationContext ac = null;
            for (int j = 0; ac != null && j < allocationContexts.size() * 10; j++) {
                final int randomIndex = ThreadLocalRandom.current().nextInt(allocationContexts.size());
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
    private void migrateToRandomResourceContainer(final AllocationContext allocation) {
        final int randomIndex = ThreadLocalRandom.current().nextInt(this.resContainer.length);
        final ResourceContainer replaceResCon = this.resContainer[randomIndex];
        allocation.setResourceContainer_AllocationContext(replaceResCon);
    }

}
