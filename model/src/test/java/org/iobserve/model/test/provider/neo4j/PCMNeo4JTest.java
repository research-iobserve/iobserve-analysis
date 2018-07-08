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
package org.iobserve.model.test.provider.neo4j;

import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.test.data.AllocationDataFactory;
import org.iobserve.model.test.data.DebugHelper;
import org.iobserve.model.test.data.RepositoryModelDataFactory;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.iobserve.model.test.data.SystemDataFactory;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * @author Reiner Jung
 *
 */
public class PCMNeo4JTest {

    private static final String PREFIX = PCMNeo4JTest.class.getCanonicalName();

    private Repository repository;
    private ResourceEnvironment resourceEnvironment;
    private System system;
    private Allocation allocation;

    private final ModelResource repositoryResource = ModelProviderTestUtils.prepareResource("repository",
            PCMNeo4JTest.PREFIX, RepositoryFactory.eINSTANCE);
    private final ModelResource resourceEnvironmentResource = ModelProviderTestUtils
            .prepareResource("resource-environment", PCMNeo4JTest.PREFIX, RepositoryFactory.eINSTANCE);
    private final ModelResource systemResource = ModelProviderTestUtils.prepareResource("system", PCMNeo4JTest.PREFIX,
            RepositoryFactory.eINSTANCE);
    private final ModelResource allocationResource = ModelProviderTestUtils.prepareResource("allocation",
            PCMNeo4JTest.PREFIX, RepositoryFactory.eINSTANCE);

    /**
     * Initialize models.
     */
    @Before
    public void setUp() {
        this.repository = RepositoryModelDataFactory.createBookstoreRepositoryModel();
        this.resourceEnvironment = ResourceEnvironmentDataFactory.createResourceEnvironment();
        this.system = SystemDataFactory.createSystem(this.repository);
        this.allocation = AllocationDataFactory.createAllocation(this.system, this.resourceEnvironment);

    }

    /**
     * Test whether updates work as suggested.
     */
    @Test
    public void testUpdate() {
        /** store model in database. */
        this.repositoryResource.storeModelPartition(this.repository);
        this.resourceEnvironmentResource.storeModelPartition(this.resourceEnvironment);
        this.systemResource.storeModelPartition(this.system);
        this.allocationResource.storeModelPartition(this.allocation);

        final AssemblyContext assemblyContext = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.QUERY_ASSEMBLY_CONTEXT);
        final ResourceContainer container = ResourceEnvironmentDataFactory.findContainer(this.resourceEnvironment,
                ResourceEnvironmentDataFactory.QUERY_CONTAINER_3);

        final Allocation allocationModel = this.allocationResource.getModelRootNode(Allocation.class);

        final AllocationContext newAllocationContext = AllocationFactory.eINSTANCE.createAllocationContext();
        newAllocationContext.setEntityName(AllocationDataFactory.QUERY_ALLOCATION_CONTEXT_3);
        newAllocationContext.setAssemblyContext_AllocationContext(assemblyContext);
        newAllocationContext.setResourceContainer_AllocationContext(container);

        allocationModel.getAllocationContexts_Allocation().add(newAllocationContext);

        this.allocationResource.updatePartition(Allocation.class, allocationModel);

        DebugHelper.printModelPartition(this.allocationResource.getModelRootNode(Allocation.class));

        for (final AllocationContext context : this.allocationResource.collectAllObjectsByType(AllocationContext.class)) {
            DebugHelper.printModelPartition(context);
        }
    }
}