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

import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.iobserve.model.persistence.neo4j.NodeLookupException;
import org.iobserve.model.test.data.AllocationDataFactory;
import org.iobserve.model.test.data.RepositoryModelDataFactory;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.iobserve.model.test.data.SystemDataFactory;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemPackage;

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

    private final Neo4JModelResource<Repository> repositoryResource = ModelProviderTestUtils.prepareResource("repository",
            PCMNeo4JTest.PREFIX, RepositoryPackage.eINSTANCE);
    private final Neo4JModelResource<ResourceEnvironment> resourceEnvironmentResource = ModelProviderTestUtils
            .prepareResource("resource-environment", PCMNeo4JTest.PREFIX, ResourceenvironmentPackage.eINSTANCE);
    private final Neo4JModelResource<System> systemResource = ModelProviderTestUtils.prepareResource("system",
            PCMNeo4JTest.PREFIX, SystemPackage.eINSTANCE);
    private final Neo4JModelResource<Allocation> allocationResource = ModelProviderTestUtils.prepareResource("allocation",
            PCMNeo4JTest.PREFIX, AllocationPackage.eINSTANCE);

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
     *
     * @throws NodeLookupException
     *             on node lookup errors
     * @throws DBException
     *             on db errors
     */
    @Test
    public void testUpdate() throws NodeLookupException, DBException {
        /** store model in database. */
        this.repositoryResource.storeModelPartition(this.repository);
        this.resourceEnvironmentResource.storeModelPartition(this.resourceEnvironment);
        this.systemResource.storeModelPartition(this.system);
        this.allocationResource.storeModelPartition(this.allocation);

        final AssemblyContext assemblyContext = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.QUERY_ASSEMBLY_CONTEXT);
        final ResourceContainer container = ResourceEnvironmentDataFactory.findContainer(this.resourceEnvironment,
                ResourceEnvironmentDataFactory.QUERY_CONTAINER_3);

        final Allocation allocationModel = this.allocationResource.getModelRootNode(Allocation.class,
                AllocationPackage.Literals.ALLOCATION);

        final AllocationContext newAllocationContext = AllocationFactory.eINSTANCE.createAllocationContext();
        newAllocationContext.setEntityName(AllocationDataFactory.QUERY_ALLOCATION_CONTEXT_3);
        newAllocationContext.setAssemblyContext_AllocationContext(assemblyContext);
        newAllocationContext.setResourceContainer_AllocationContext(container);

        allocationModel.getAllocationContexts_Allocation().add(newAllocationContext);

        this.allocationResource.updatePartition(allocationModel);

        for (final AllocationContext context : this.allocationResource.collectAllObjectsByType(AllocationContext.class,
                AllocationPackage.Literals.ALLOCATION_CONTEXT)) { // NOCS
            // TODO
        }
    }
}
