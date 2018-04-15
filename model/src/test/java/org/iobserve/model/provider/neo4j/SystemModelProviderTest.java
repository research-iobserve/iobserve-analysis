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
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.neo4j.io.fs.FileUtils;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.system.System;

/**
 * Test cases for the model provider using a System model.
 *
 * @author Lars Bluemke
 *
 * @since 0.0.2
 *
 */
public class SystemModelProviderTest implements IModelProviderTest { // NOCS no constructor in test

    private static final File GRAPH_DIR = new File("./testdb");

    private static final Graph<System> GRAPH = new GraphLoader(SystemModelProviderTest.GRAPH_DIR)
            .createModelGraph(System.class);

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    @Override
    @Before
    public void clearGraph() {
        new ModelProvider<>(SystemModelProviderTest.GRAPH).clearGraph();
    }

    @Override
    @Test
    public void createThenCloneThenRead() {
        final ModelProvider<System, System> modelProvider1 = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final ModelProvider<System, System> modelProvider2;
        final System writtenModel = new TestModelBuilder().getSystem();
        final System readModel;
        final Graph<System> graph2;

        modelProvider1.createComponent(writtenModel);

        graph2 = modelProvider1.cloneNewGraphVersion(System.class);
        modelProvider2 = new ModelProvider<>(graph2);

        readModel = modelProvider2.readOnlyRootComponent(System.class);
        graph2.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenClearGraph() {
        final ModelProvider<System, System> modelProvider = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final System writtenModel = new TestModelBuilder().getSystem();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenReadById() {
        final ModelProvider<System, System> modelProvider = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final System writtenModel = new TestModelBuilder().getSystem();
        final System readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyComponentById(System.class, writtenModel.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadByName() {
        final ModelProvider<System, System> modelProvider = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final System writtenModel = new TestModelBuilder().getSystem();
        final List<System> readModels;

        modelProvider.createComponent(writtenModel);
        readModels = modelProvider.readOnlyComponentByName(System.class, writtenModel.getEntityName());

        for (final System readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
        }
    }

    @Override
    @Test
    public void createThenReadByType() {
        final ModelProvider<System, System> modelProvider = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final System writtenModel = new TestModelBuilder().getSystem();
        final List<String> readIds;

        modelProvider.createComponent(writtenModel);
        readIds = modelProvider.readComponentByType(System.class);

        for (final String readId : readIds) {
            Assert.assertTrue(writtenModel.getId().equals(readId));
        }

    }

    @Override
    @Test
    public void createThenReadRoot() {
        final ModelProvider<System, System> modelProvider = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final System writtenModel = new TestModelBuilder().getSystem();
        final System readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyRootComponent(System.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelProvider<System, System> modelProvider = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final System writtenModel = new TestModelBuilder().getSystem();
        final System readModel;
        final AssemblyContext ac = writtenModel.getAssemblyContexts__ComposedStructure().get(0);

        modelProvider.createComponent(writtenModel);
        readModel = (System) modelProvider.readOnlyContainingComponentById(AssemblyContext.class, ac.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelProvider<System, System> modelProvider = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final System writtenModel = testModelBuilder.getSystem();
        final List<EObject> expectedReferencingComponents = new LinkedList<>();
        final List<EObject> readReferencingComponents;

        expectedReferencingComponents.add(testModelBuilder.getBusinessQueryInputConnector());
        expectedReferencingComponents.add(testModelBuilder.getBusinessPayConnector());

        modelProvider.createComponent(writtenModel);

        readReferencingComponents = modelProvider.readOnlyReferencingComponentsById(AssemblyContext.class,
                testModelBuilder.getBusinessOrderAssemblyContext().getId());

        // Only the businessQueryInputConnector and the businessPayConnector are referencing the
        // businessOrderContext
        Assert.assertTrue(readReferencingComponents.size() == 2);

        Assert.assertTrue(this.equalityHelper.equals(expectedReferencingComponents, readReferencingComponents));

    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final ModelProvider<System, System> modelProvider = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final System writtenModel = testModelBuilder.getSystem();
        final System readModel;

        modelProvider.createComponent(writtenModel);

        // Update the model by renaming and removing the business context
        writtenModel.setEntityName("MyVideoOnDemandService");

        writtenModel.getAssemblyContexts__ComposedStructure()
                .remove(testModelBuilder.getBusinessOrderAssemblyContext());
        writtenModel.getConnectors__ComposedStructure().remove(testModelBuilder.getBusinessQueryInputConnector());
        writtenModel.getConnectors__ComposedStructure().remove(testModelBuilder.getBusinessPayConnector());

        // Replace the business context by a context for groups of people placing an order
        final AssemblyContext sharedOrderContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        final AssemblyConnector sharedQueryInputConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
        final AssemblyConnector sharedPayConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();

        sharedOrderContext.setEntityName("sharedOrderContext_org.myvideoondemandservice.orderComponent");

        sharedQueryInputConnector.setEntityName("sharedQueryInput");
        sharedQueryInputConnector.setProvidedRole_AssemblyConnector(testModelBuilder.getProvidedInputOperation());
        sharedQueryInputConnector.setRequiredRole_AssemblyConnector(testModelBuilder.getRequiredInputOperation());
        sharedQueryInputConnector.setProvidingAssemblyContext_AssemblyConnector(sharedOrderContext);
        sharedQueryInputConnector
                .setRequiringAssemblyContext_AssemblyConnector(testModelBuilder.getQueryInputAssemblyContext());

        sharedPayConnector.setEntityName("sharedPayment");
        sharedPayConnector.setProvidedRole_AssemblyConnector(testModelBuilder.getProvidedPayOperation());
        sharedPayConnector.setRequiredRole_AssemblyConnector(testModelBuilder.getRequiredPayOperation());
        sharedPayConnector.setProvidingAssemblyContext_AssemblyConnector(testModelBuilder.getPaymentAssemblyContext());
        sharedPayConnector.setRequiringAssemblyContext_AssemblyConnector(sharedOrderContext);

        writtenModel.getAssemblyContexts__ComposedStructure().add(sharedOrderContext);
        writtenModel.getConnectors__ComposedStructure().add(sharedQueryInputConnector);
        writtenModel.getConnectors__ComposedStructure().add(sharedPayConnector);

        modelProvider.updateComponent(System.class, writtenModel);

        readModel = modelProvider.readOnlyRootComponent(System.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenDeleteComponent() {
        final ModelProvider<System, System> modelProvider = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final System writtenModel = new TestModelBuilder().getSystem();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponent(System.class, writtenModel.getId());

        // Manually delete the proxy nodes from the repository model
        try (Transaction tx = SystemModelProviderTest.GRAPH.getGraphDatabaseService().beginTx()) {
            SystemModelProviderTest.GRAPH.getGraphDatabaseService()
                    .execute("MATCH (n:OperationProvidedRole), (m:OperationRequiredRole) DELETE n, m");
            tx.success();
        }

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteComponentAndDatatypes() {
        final ModelProvider<System, System> modelProvider = new ModelProvider<>(SystemModelProviderTest.GRAPH);
        final System writtenModel = new TestModelBuilder().getSystem();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponentAndDatatypes(System.class, writtenModel.getId(), true);

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
        SystemModelProviderTest.GRAPH.getGraphDatabaseService().shutdown();
        FileUtils.deleteRecursively(SystemModelProviderTest.GRAPH_DIR);
    }

}
