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

/**
 * Todo .
 * 
 * @author unknown
 *
 */
public class AllocationGeneration {

    private static final AllocationFactory ALLOCATION_FACTORY = AllocationFactory.eINSTANCE;
    private final Allocation allocationModel;

    private final System systemModel;
    private final ResourceEnvironment resEnvModel;
    private final ResourceContainer[] resContainer;

    public AllocationGeneration(final System systemModel, final ResourceEnvironment resEnvModel) {
        this.systemModel = systemModel;
        this.resEnvModel = resEnvModel;

        // this.hostingServer = new HashMap<String, ResourceContainer>();
        final int resContainerCount = resEnvModel.getResourceContainer_ResourceEnvironment().size();
        this.resContainer = resEnvModel.getResourceContainer_ResourceEnvironment()
                .toArray(new ResourceContainer[resContainerCount]);

        this.allocationModel = AllocationGeneration.ALLOCATION_FACTORY.createAllocation();
        this.allocationModel.setEntityName(systemModel.getEntityName());
        this.allocationModel.setSystem_Allocation(this.systemModel);
        this.allocationModel.setTargetResourceEnvironment_Allocation(this.resEnvModel);
    }

    public AllocationGeneration(final Allocation allocationModel, final System systemModel,
            final ResourceEnvironment resEnvModel) {
        this.systemModel = systemModel;
        this.resEnvModel = resEnvModel;
        this.allocationModel = allocationModel;

        // this.hostingServer = new HashMap<String, ResourceContainer>();
        final int resContainerCount = resEnvModel.getResourceContainer_ResourceEnvironment().size();
        this.resContainer = resEnvModel.getResourceContainer_ResourceEnvironment()
                .toArray(new ResourceContainer[resContainerCount]);
    }

    /**
     *
     * @return
     */
    public Allocation generateAllocation() {
        for (final AssemblyContext assemblyContext : this.systemModel.getAssemblyContexts__ComposedStructure()) {

            final AllocationContext allocationContext = this.generateAllocationContext(assemblyContext);
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
    public Allocation generateAllocation(final List<AssemblyContext> newAssemblyContexts, final String prefix) {

        for (final AssemblyContext assemblyContext : newAssemblyContexts) {

            final AllocationContext allocationContext = this.generateAllocationContext(assemblyContext);
            allocationContext.setEntityName(prefix + ": " + allocationContext.getEntityName());
            this.allocationModel.getAllocationContexts_Allocation().add(allocationContext);
        }

        return this.allocationModel;
    }

    /*
     *
     */
    private AllocationContext generateAllocationContext(final AssemblyContext assemblyContext) {
        final AllocationContext allocationContext = AllocationGeneration.ALLOCATION_FACTORY.createAllocationContext();
        allocationContext.setAssemblyContext_AllocationContext(assemblyContext);

        final int randomIndex = ThreadLocalRandom.current().nextInt(this.resContainer.length);

        allocationContext.setResourceContainer_AllocationContext(this.resContainer[randomIndex]);
        allocationContext.setEntityName(
                assemblyContext.getEntityName() + " @ " + this.resContainer[randomIndex].getEntityName());
        return allocationContext;
    }

}
