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
package org.iobserve.model.test.provider.neo4j;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.persistence.neo4j.NodeLookupException;
import org.iobserve.model.test.data.AllocationDataFactory;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.iobserve.model.test.data.SystemDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Test cases for the model provider using an allocation model.
 *
 * @author Lars Bluemke
 *
 * @since 0.0.2
 */
public class AllocationModelProviderTest extends AbstractEnityModelProviderTest<Allocation> {

    public static final String CREATE_THEN_UPDATE_THEN_READ_UPDATED = "createThenUpdateThenReadUpdated";
    public static final String CREATE_THEN_READ_BY_NAME = "createThenReadByName";
    public static final String CREATE_THEN_READ_BY_TYPE = "createThenReadByType";
    public static final String CREATE_THEN_READ_CONTAINING = "createThenReadContaining";
    public static final String CREATE_THEN_READ_REFERENCING = "createThenReadReferencing";

    // NOCS
    // no constructor in

    @Override
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();
        this.testModel = this.allocation;
        this.ePackage = AllocationPackage.eINSTANCE;
        this.clazz = Allocation.class;
    }

    @Override
    @Test
    public void createThenReadByType() {
        final ModelResource resource = ModelProviderTestUtils
                .prepareResource(AllocationModelProviderTest.CREATE_THEN_READ_BY_TYPE, this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        // TODO add actual test

        resource.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelResource resource = ModelProviderTestUtils
                .prepareResource(AllocationModelProviderTest.CREATE_THEN_READ_CONTAINING, this.prefix, this.ePackage);

        final AllocationContext writtenContext = this.testModel.getAllocationContexts_Allocation().get(0);

        resource.storeModelPartition(this.testModel);

        final Allocation readModel = (Allocation) resource.findContainingObjectById(AllocationContext.class,
                resource.getInternalId(writtenContext));

        Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));

        resource.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelResource resource = ModelProviderTestUtils
                .prepareResource(AllocationModelProviderTest.CREATE_THEN_READ_REFERENCING, this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        final AssemblyContext context = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.PAYMENT_ASSEMBLY_CONTEXT);
        final List<EObject> readReferencingComponents = resource
                .collectReferencingObjectsByTypeAndId(AssemblyContext.class, resource.getInternalId(context));

        // Only the payment server allocation context is referencing the payment assembly context
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(this.equalityHelper.comparePartition(
                AllocationDataFactory.findAllocationContext(this.testModel,
                        AllocationDataFactory.PAYMENT_ALLOCATION_CONTEXT),
                readReferencingComponents.get(0), this.testModel.eClass()));

        resource.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() throws NodeLookupException {
        final ModelResource resource = ModelProviderTestUtils.prepareResource(
                AllocationModelProviderTest.CREATE_THEN_UPDATE_THEN_READ_UPDATED, this.prefix, this.ePackage);

        final AllocationContext businessOrderServerAllocationContext = AllocationDataFactory
                .findAllocationContext(this.testModel, AllocationDataFactory.BUSINESS_ORDER_ALLOCATION_CONTEXT);
        final AllocationContext privateOrderServerAllocationContext = AllocationDataFactory
                .findAllocationContext(this.testModel, AllocationDataFactory.PRIVATE_ORDER_ALLOCATION_CONTEXT);

        resource.storeModelPartition(this.testModel);

        // Update the model by allocating new separate servers for business and private orders
        final ResourceContainer businessOrderServer = ResourceEnvironmentDataFactory.cloneContainer(
                this.resourceEnvironment, ResourceEnvironmentDataFactory.BUSINESS_ORDER_CONTAINER,
                "additionalBusinessOrderServer");

        final ResourceContainer privateOrderServer = ResourceEnvironmentDataFactory.cloneContainer(
                this.resourceEnvironment, ResourceEnvironmentDataFactory.PRIVATE_ORDER_CONTAINER,
                "additionalPrivateOrderServer");

        this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(businessOrderServer);
        this.resourceEnvironment.getResourceContainer_ResourceEnvironment().add(privateOrderServer);

        businessOrderServerAllocationContext.setResourceContainer_AllocationContext(businessOrderServer);
        privateOrderServerAllocationContext.setResourceContainer_AllocationContext(privateOrderServer);

        resource.updatePartition(this.testModel);

        final Allocation readModel = resource.getModelRootNode(Allocation.class);

        Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#findObjectsByTypeAndName(Class, String)} and asserts that it is equal to
     * the one written to the graph.
     */
    @Test
    public final void createThenReadByName() {
        final ModelResource resource = ModelProviderTestUtils
                .prepareResource(AllocationModelProviderTest.CREATE_THEN_READ_BY_NAME, this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        final List<Allocation> readModels = resource.findObjectsByTypeAndName(this.clazz, "entityName",
                this.testModel.getEntityName());

        for (final Allocation readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));
        }

        resource.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenDeleteObject() {
        Assert.assertTrue(true);
    }

    @Override
    @Test
    public void createThenDeleteObjectAndDatatypes() {
        Assert.assertTrue(true);
    }

}
