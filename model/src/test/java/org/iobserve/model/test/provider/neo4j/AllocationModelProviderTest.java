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
import org.iobserve.model.provider.neo4j.ModelGraph;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.model.test.data.AllocationDataFactory;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.iobserve.model.test.data.SystemDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
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
        this.factory = AllocationFactory.eINSTANCE;
        this.clazz = Allocation.class;
    }

    @Override
    @Test
    public void createThenReadByType() {
        final ModelGraph graph = this.prepareGraph(AllocationModelProviderTest.CREATE_THEN_READ_BY_TYPE);

        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<String> collectedIds = modelProvider.collectAllObjectIdsByType(Allocation.class);

        for (final String id : collectedIds) {
            Assert.assertTrue(this.testModel.getId().equals(id));
        }

        graph.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelGraph graph = this.prepareGraph(AllocationModelProviderTest.CREATE_THEN_READ_CONTAINING);

        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        final AllocationContext writtenContext = this.testModel.getAllocationContexts_Allocation().get(0);

        modelProvider.storeModelPartition(this.testModel);

        final Allocation readModel = (Allocation) modelProvider.readOnlyContainingComponentById(AllocationContext.class,
                writtenContext.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));

        graph.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelGraph graph = this.prepareGraph(AllocationModelProviderTest.CREATE_THEN_READ_REFERENCING);

        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final String id = SystemDataFactory.findAssemblyContext(this.system, SystemDataFactory.PAYMENT_ASSEMBLY_CONTEXT)
                .getId();
        final List<EObject> readReferencingComponents = modelProvider
                .collectReferencingObjectsByTypeAndId(AssemblyContext.class, id);

        // Only the payment server allocation context is referencing the payment assembly context
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(this.equalityHelper.equals(
                AllocationDataFactory.findAllocationContext(this.testModel, AllocationDataFactory.PAYMENT_ALLOCATION),
                readReferencingComponents.get(0)));

        graph.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final ModelGraph graph = this.prepareGraph(AllocationModelProviderTest.CREATE_THEN_UPDATE_THEN_READ_UPDATED);

        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        final AllocationContext businessOrderServerAllocationContext = AllocationDataFactory
                .findAllocationContext(this.testModel, AllocationDataFactory.BUSINESS_ORDER_ALLOCATION);
        final AllocationContext privateOrderServerAllocationContext = AllocationDataFactory
                .findAllocationContext(this.testModel, AllocationDataFactory.PRIVATE_ORDER_ALLOCATION);

        modelProvider.storeModelPartition(this.testModel);

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

        modelProvider.updateObject(Allocation.class, this.testModel);

        final Allocation readModel = modelProvider.getModelRootNode(Allocation.class);

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#getObjectsByTypeAndName(Class, String)} and asserts that it is equal to
     * the one written to the graph.
     */
    @Test
    public final void createThenReadByName() {
        final ModelGraph graph = this.prepareGraph(AllocationModelProviderTest.CREATE_THEN_READ_BY_NAME);

        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<Allocation> readModels = modelProvider.getObjectsByTypeAndName(this.clazz,
                this.testModel.getEntityName());

        for (final Allocation readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
        }

        graph.getGraphDatabaseService().shutdown();
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
