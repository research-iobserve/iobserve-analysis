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
package org.iobserve.model.provider.neo4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.neo4j.io.fs.FileUtils;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;

/**
 * Test cases for the model provider using an allocation model.
 *
 * @author Lars Bluemke
 *
 */
public class AllocationModelProviderTest implements IModelProviderTest {
    private static final File GRAPH_DIR = new File("./testdb");

    private static Graph graph = new GraphLoader(AllocationModelProviderTest.GRAPH_DIR).createAllocationModelGraph();

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    @Override
    @Before
    public void clearGraph() {
        new ModelProvider<>(AllocationModelProviderTest.graph).clearGraph();
    }

    @Override
    @Test
    public void createThenCloneThenRead() {
        final ModelProvider<Allocation> modelProvider1 = new ModelProvider<>(AllocationModelProviderTest.graph);
        final ModelProvider<Allocation> modelProvider2;
        final Allocation writtenModel = new TestModelBuilder().getAllocation();
        final Allocation readModel;
        final Graph graph2;

        modelProvider1.createComponent(writtenModel);

        graph2 = modelProvider1.cloneNewGraphVersion(Allocation.class);
        modelProvider2 = new ModelProvider<>(graph2);

        readModel = modelProvider2.readOnlyRootComponent(Allocation.class);
        graph2.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenClearGraph() {
        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(AllocationModelProviderTest.graph);
        final Allocation writtenModel = new TestModelBuilder().getAllocation();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenReadById() {
        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(AllocationModelProviderTest.graph);
        final Allocation writtenModel = new TestModelBuilder().getAllocation();
        final Allocation readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyComponentById(Allocation.class, writtenModel.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadByName() {
        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(AllocationModelProviderTest.graph);
        final Allocation writtenModel = new TestModelBuilder().getAllocation();
        final List<Allocation> readModels;

        modelProvider.createComponent(writtenModel);
        readModels = modelProvider.readOnlyComponentByName(Allocation.class, writtenModel.getEntityName());

        for (final Allocation readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
        }
    }

    @Override
    @Test
    public void createThenReadByType() {
        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(AllocationModelProviderTest.graph);
        final Allocation writtenModel = new TestModelBuilder().getAllocation();
        final List<String> readIds;

        modelProvider.createComponent(writtenModel);
        readIds = modelProvider.readComponentByType(Allocation.class);

        for (final String readId : readIds) {
            Assert.assertTrue(writtenModel.getId().equals(readId));
        }

    }

    @Override
    @Test
    public void createThenReadRoot() {
        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(AllocationModelProviderTest.graph);
        final Allocation writtenModel = new TestModelBuilder().getAllocation();
        final Allocation readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyRootComponent(Allocation.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(AllocationModelProviderTest.graph);
        final Allocation writtenModel = new TestModelBuilder().getAllocation();
        final Allocation readModel;
        final AllocationContext writtenContext = writtenModel.getAllocationContexts_Allocation().get(0);

        modelProvider.createComponent(writtenModel);
        readModel = (Allocation) modelProvider.readOnlyContainingComponentById(AllocationContext.class,
                writtenContext.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(AllocationModelProviderTest.graph);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final Allocation writtenModel = testModelBuilder.getAllocation();
        final List<EObject> readReferencingComponents;

        modelProvider.createComponent(writtenModel);

        readReferencingComponents = modelProvider.readOnlyReferencingComponentsById(AssemblyContext.class,
                testModelBuilder.getPaymentAssemblyContext().getId());

        // Only the payment server allocation context is referencing the payment assembly context
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(this.equalityHelper.equals(testModelBuilder.getPaymentServerAllocationContext(),
                readReferencingComponents.get(0)));

    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(AllocationModelProviderTest.graph);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final Allocation writtenModel = testModelBuilder.getAllocation();
        final AllocationContext businessOrderServerAllocationContext = testModelBuilder
                .getBusinessOrderServerAllocationContext();
        final AllocationContext privateOrderServerAllocationContext = testModelBuilder
                .getPrivateOrderServerAllocationContext();

        final Allocation readModel;

        modelProvider.createComponent(writtenModel);

        // Update the model by allocating new separate servers for business and private orders
        final ResourceEnvironment resourceEnvironment = testModelBuilder.getResourceEnvironment();
        final ResourceContainer businessOrderServer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        businessOrderServer.setEntityName("businessOrderServer");
        businessOrderServer.setResourceEnvironment_ResourceContainer(testModelBuilder.getResourceEnvironment());
        businessOrderServer.getActiveResourceSpecifications_ResourceContainer()
                .add(testModelBuilder.getOrderServerSpecification());

        final ResourceContainer privateOrderServer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        privateOrderServer.setEntityName("privateOrderServer");
        privateOrderServer.setResourceEnvironment_ResourceContainer(testModelBuilder.getResourceEnvironment());
        privateOrderServer.getActiveResourceSpecifications_ResourceContainer()
                .add(testModelBuilder.getOrderServerSpecification());

        resourceEnvironment.getResourceContainer_ResourceEnvironment().add(businessOrderServer);
        resourceEnvironment.getResourceContainer_ResourceEnvironment().add(privateOrderServer);

        businessOrderServerAllocationContext.setResourceContainer_AllocationContext(businessOrderServer);
        privateOrderServerAllocationContext.setResourceContainer_AllocationContext(privateOrderServer);

        modelProvider.updateComponent(Allocation.class, writtenModel);

        readModel = modelProvider.readOnlyRootComponent(Allocation.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenDeleteComponent() {
        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(AllocationModelProviderTest.graph);
        final Allocation writtenModel = new TestModelBuilder().getAllocation();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponent(Allocation.class, writtenModel.getId());

        // Manually delete proxy nodes (as they are no containments in this graph)
        try (Transaction tx = AllocationModelProviderTest.graph.getGraphDatabaseService().beginTx()) {
            AllocationModelProviderTest.graph.getGraphDatabaseService().execute(
                    "MATCH (m:ResourceEnvironment), (n:System), (o:AssemblyContext), (p:ResourceContainer) DELETE n, m, o, p");
            tx.success();
        }

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteComponentAndDatatypes() {
        final ModelProvider<Allocation> modelProvider = new ModelProvider<>(AllocationModelProviderTest.graph);
        final Allocation writtenModel = new TestModelBuilder().getAllocation();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponentAndDatatypes(Allocation.class, writtenModel.getId(), true);

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    /**
     * Remove database directory.
     *
     * @throws IOException
     *             When an error occurs while deleting
     */
    @AfterClass
    public static void cleanUp() throws IOException {
        AllocationModelProviderTest.graph.getGraphDatabaseService().shutdown();
        FileUtils.deleteRecursively(AllocationModelProviderTest.GRAPH_DIR);
    }

}
