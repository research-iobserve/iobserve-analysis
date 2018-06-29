/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.test.data;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * @author Reiner Jung
 *
 */
public final class AllocationDataFactory {

    public static final String QUERY_ALLOCATION_1 = "Allocation1_" + SystemDataFactory.QUERY_ASSEMBLY_CONTEXT;
    public static final String QUERY_ALLOCATION_2 = "Allocation2_" + SystemDataFactory.QUERY_ASSEMBLY_CONTEXT;
    public static final String BUSINESS_ORDER_ALLOCATION = "Allocation1_"
            + SystemDataFactory.BUSINESS_ORDER_ASSEMBLY_CONTEXT;
    public static final String PRIVATE_ORDER_ALLOCATION = "Allocation2_"
            + SystemDataFactory.PRIVATE_ORDER_ASSMEBLY_CONTEXT;
    public static final String PAYMENT_ALLOCATION = "Allocation_" + SystemDataFactory.PAYMENT_ASSEMBLY_CONTEXT;

    /**
     * Empty factory.
     */
    private AllocationDataFactory() {
        // empty factory constructor.
    }

    /**
     * Create a new allocation model and link it with {@code system} and {@code resourceEnvironment}
     * model
     * 
     * @param system
     *            system model
     * @param resourceEnvironment
     *            resource environment model
     *
     * @return allocation model
     */
    public static Allocation createAllocation(final System system, final ResourceEnvironment resourceEnvironment) {
        final Allocation allocation = AllocationFactory.eINSTANCE.createAllocation();
        allocation.setEntityName("defaultAllocation");
        allocation.setSystem_Allocation(system);
        allocation.setTargetResourceEnvironment_Allocation(resourceEnvironment);

        // Allocation contexts
        final AllocationContext client1AllocationContext = AllocationDataFactory.createAllocationContext(system,
                resourceEnvironment, AllocationDataFactory.QUERY_ALLOCATION_1, SystemDataFactory.QUERY_ASSEMBLY_CONTEXT,
                ResourceEnvironmentDataFactory.QUERY_CONTAINER_1);

        final AllocationContext client2AllocationContext = AllocationDataFactory.createAllocationContext(system,
                resourceEnvironment, AllocationDataFactory.QUERY_ALLOCATION_2, SystemDataFactory.QUERY_ASSEMBLY_CONTEXT,
                ResourceEnvironmentDataFactory.QUERY_CONTAINER_2);

        final AllocationContext businessOrderServerAllocationContext = AllocationDataFactory.createAllocationContext(
                system, resourceEnvironment, AllocationDataFactory.BUSINESS_ORDER_ALLOCATION,
                SystemDataFactory.BUSINESS_ORDER_ASSEMBLY_CONTEXT,
                ResourceEnvironmentDataFactory.BUSINESS_ORDER_CONTAINER);

        final AllocationContext privateOrderServerAllocationContext = AllocationDataFactory.createAllocationContext(
                system, resourceEnvironment, AllocationDataFactory.PRIVATE_ORDER_ALLOCATION,
                SystemDataFactory.PRIVATE_ORDER_ASSMEBLY_CONTEXT,
                ResourceEnvironmentDataFactory.PRIVATE_ORDER_CONTAINER);

        final AllocationContext paymentServerAllocationContext = AllocationDataFactory.createAllocationContext(system,
                resourceEnvironment, AllocationDataFactory.PAYMENT_ALLOCATION,
                SystemDataFactory.PAYMENT_ASSEMBLY_CONTEXT, ResourceEnvironmentDataFactory.PAYMENT_CONTAINER);

        allocation.getAllocationContexts_Allocation().add(client1AllocationContext);
        allocation.getAllocationContexts_Allocation().add(client2AllocationContext);
        allocation.getAllocationContexts_Allocation().add(businessOrderServerAllocationContext);
        allocation.getAllocationContexts_Allocation().add(privateOrderServerAllocationContext);
        allocation.getAllocationContexts_Allocation().add(paymentServerAllocationContext);

        return allocation;
    }

    private static AllocationContext createAllocationContext(final System system,
            final ResourceEnvironment resourceEnvironment, final String allocation, final String assemblyContextName,
            final String containerName) {
        final AllocationContext context = AllocationFactory.eINSTANCE.createAllocationContext();

        final AssemblyContext assemblyContext = SystemDataFactory.findAssemblyContext(system, assemblyContextName);
        final ResourceContainer container = ResourceEnvironmentDataFactory.findContainer(resourceEnvironment,
                containerName);

        context.setEntityName(allocation);
        context.setAssemblyContext_AllocationContext(assemblyContext);
        context.setResourceContainer_AllocationContext(container);

        return context;
    }

    public static AllocationContext findAllocationContext(final Allocation allocation, final String allocationName) {
        for (final AllocationContext context : allocation.getAllocationContexts_Allocation()) {
            if (context.getEntityName().equals(allocationName)) {
                return context;
            }
        }
        return null;
    }
}
