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

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.provider.neo4j.ModelGraph;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.system.SystemFactory;

/**
 * Test cases for the model provider using a System model.
 *
 * @author Lars Bluemke
 *
 * @since 0.0.2
 *
 */
public class SystemModelProviderTest extends AbstractEnityModelProviderTest<System> { // NOCS no
                                                                                      // constructor
                                                                                      // in test

    @Override
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();
        this.testModel = this.testModelBuilder.getSystem();
        this.factory = SystemFactory.eINSTANCE;
        this.clazz = System.class;
    }

    @Override
    @Test
    public void createThenReadByType() {
        final ModelGraph graph = this.prepareGraph("createThenReadByType");

        final ModelProvider<System> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<String> collectedIds = modelProvider.collectAllObjectIdsByType(System.class);

        for (final String id : collectedIds) {
            Assert.assertTrue(this.testModel.getId().equals(id));
        }

    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelGraph graph = this.prepareGraph("createThenReadContaining");

        final ModelProvider<System> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final AssemblyContext ac = this.testModel.getAssemblyContexts__ComposedStructure().get(0);

        modelProvider.storeModelPartition(this.testModel);

        final System readModel = (System) modelProvider.readOnlyContainingComponentById(AssemblyContext.class,
                ac.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelGraph graph = this.prepareGraph("createThenReadReferencing");

        final ModelProvider<System> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<EObject> expectedReferencingComponents = new LinkedList<>();
        final List<EObject> readReferencingComponents;

        expectedReferencingComponents.add(this.testModelBuilder.getBusinessQueryInputConnector());
        expectedReferencingComponents.add(this.testModelBuilder.getBusinessPayConnector());

        readReferencingComponents = modelProvider.readOnlyReferencingComponentsById(AssemblyContext.class,
                this.testModelBuilder.getBusinessOrderAssemblyContext().getId());

        // Only the businessQueryInputConnector and the businessPayConnector are referencing the
        // businessOrderContext
        Assert.assertTrue(readReferencingComponents.size() == 2);

        Assert.assertTrue(this.equalityHelper.equals(expectedReferencingComponents, readReferencingComponents));

    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final ModelGraph graph = this.prepareGraph("createThenUpdateThenReadUpdated");

        final ModelProvider<System> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        // Update the model by renaming and removing the business context
        this.testModel.setEntityName("MyVideoOnDemandService");

        this.testModel.getAssemblyContexts__ComposedStructure()
                .remove(this.testModelBuilder.getBusinessOrderAssemblyContext());
        this.testModel.getConnectors__ComposedStructure()
                .remove(this.testModelBuilder.getBusinessQueryInputConnector());
        this.testModel.getConnectors__ComposedStructure().remove(this.testModelBuilder.getBusinessPayConnector());

        // Replace the business context by a context for groups of people placing an order
        final AssemblyContext sharedOrderContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        final AssemblyConnector sharedQueryInputConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
        final AssemblyConnector sharedPayConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();

        sharedOrderContext.setEntityName("sharedOrderContext_org.myvideoondemandservice.orderComponent");

        sharedQueryInputConnector.setEntityName("sharedQueryInput");
        sharedQueryInputConnector.setProvidedRole_AssemblyConnector(this.testModelBuilder.getProvidedInputOperation());
        sharedQueryInputConnector.setRequiredRole_AssemblyConnector(this.testModelBuilder.getRequiredInputOperation());
        sharedQueryInputConnector.setProvidingAssemblyContext_AssemblyConnector(sharedOrderContext);
        sharedQueryInputConnector
                .setRequiringAssemblyContext_AssemblyConnector(this.testModelBuilder.getQueryInputAssemblyContext());

        sharedPayConnector.setEntityName("sharedPayment");
        sharedPayConnector.setProvidedRole_AssemblyConnector(this.testModelBuilder.getProvidedPayOperation());
        sharedPayConnector.setRequiredRole_AssemblyConnector(this.testModelBuilder.getRequiredPayOperation());
        sharedPayConnector
                .setProvidingAssemblyContext_AssemblyConnector(this.testModelBuilder.getPaymentAssemblyContext());
        sharedPayConnector.setRequiringAssemblyContext_AssemblyConnector(sharedOrderContext);

        this.testModel.getAssemblyContexts__ComposedStructure().add(sharedOrderContext);
        this.testModel.getConnectors__ComposedStructure().add(sharedQueryInputConnector);
        this.testModel.getConnectors__ComposedStructure().add(sharedPayConnector);

        modelProvider.updateObject(System.class, this.testModel);

        final System readModel = modelProvider.readRootNode(System.class);

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

    @Override
    @Test
    public void createThenDeleteObject() {
        final ModelGraph graph = this.prepareGraph("createThenDeleteObject");

        final ModelProvider<System> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final System writtenModel = this.testModelBuilder.getSystem();

        modelProvider.storeModelPartition(writtenModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        modelProvider.deleteObjectById(System.class, writtenModel.getId());

        // Manually delete the proxy nodes from the repository model
        try (Transaction tx = graph.getGraphDatabaseService().beginTx()) {
            graph.getGraphDatabaseService().execute("MATCH (n:`" + OperationProvidedRole.class.getCanonicalName()
                    + "`), (m:`" + OperationRequiredRole.class.getCanonicalName() + "`) DELETE n, m");
            tx.success();
        }

        Assert.assertTrue(this.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteObjectAndDatatypes() {
        final ModelGraph graph = this.prepareGraph("createThenDeleteObjectAndDatatypes");

        final ModelProvider<System> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        modelProvider.deleteObjectByIdAndDatatypes(System.class, this.testModel.getId(), true);

        Assert.assertTrue(this.isGraphEmpty(modelProvider));
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#readObjectsByName(Class, String)} and asserts that it is equal to the
     * one written to the graph.
     */
    @Test
    public final void createThenReadByName() {
        final ModelGraph graph = this.prepareGraph("createThenReadByName");

        final ModelProvider<System> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<System> readModels = modelProvider.readObjectsByName(this.clazz, this.testModel.getEntityName());

        for (final System readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
        }

        graph.getGraphDatabaseService().shutdown();
    }

}
