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
import org.iobserve.model.test.data.RepositoryModelDataFactory;
import org.iobserve.model.test.data.SystemDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.core.composition.Connector;
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
        this.testModel = this.system;
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

        final Connector businessQueryConnector = SystemDataFactory.findConnector(this.system,
                SystemDataFactory.BUSINESS_QUERY_CONNECTOR);
        final Connector businessPayConnector = SystemDataFactory.findConnector(this.system,
                SystemDataFactory.BUSINESS_PAY_CONNECTOR);

        expectedReferencingComponents.add(businessQueryConnector);
        expectedReferencingComponents.add(businessPayConnector);

        final AssemblyContext context = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.BUSINESS_ORDER_ASSEMBLY_CONTEXT);

        readReferencingComponents = modelProvider.collectReferencingObjectsByTypeAndId(AssemblyContext.class,
                context.getId());

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

        final AssemblyContext businessQueryAssemblyContext = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.BUSINESS_ORDER_ASSEMBLY_CONTEXT);
        final Connector businessQueryConnector = SystemDataFactory.findConnector(this.system,
                SystemDataFactory.BUSINESS_QUERY_CONNECTOR);
        final Connector businessPayConnector = SystemDataFactory.findConnector(this.system,
                SystemDataFactory.BUSINESS_PAY_CONNECTOR);

        this.testModel.getAssemblyContexts__ComposedStructure().remove(businessQueryAssemblyContext);
        this.testModel.getConnectors__ComposedStructure().remove(businessQueryConnector);
        this.testModel.getConnectors__ComposedStructure().remove(businessPayConnector);

        // Replace the business context by a context for groups of people placing an order
        final AssemblyContext sharedOrderContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        sharedOrderContext.setEntityName("sharedOrderContext_org.myvideoondemandservice.orderComponent");
        sharedOrderContext.setEncapsulatedComponent__AssemblyContext(RepositoryModelDataFactory
                .findComponentByName(this.repository, RepositoryModelDataFactory.ORDER_COMPONENT));

        final AssemblyContext queryInputAssemblyContext = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.QUERY_ASSEMBLY_CONTEXT);
        final OperationProvidedRole providedInputRole = RepositoryModelDataFactory.findProvidedRole(
                sharedOrderContext.getEncapsulatedComponent__AssemblyContext(),
                RepositoryModelDataFactory.QUERY_PROVIDED_ROLE);
        final OperationRequiredRole requiredInputRole = RepositoryModelDataFactory.findRequiredRole(
                queryInputAssemblyContext.getEncapsulatedComponent__AssemblyContext(),
                RepositoryModelDataFactory.QUERY_REQUIRED_ROLE);

        final AssemblyConnector sharedQueryInputConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
        sharedQueryInputConnector.setEntityName("sharedQueryInput");
        sharedQueryInputConnector.setProvidedRole_AssemblyConnector(providedInputRole);
        sharedQueryInputConnector.setRequiredRole_AssemblyConnector(requiredInputRole);
        sharedQueryInputConnector.setProvidingAssemblyContext_AssemblyConnector(sharedOrderContext);
        sharedQueryInputConnector.setRequiringAssemblyContext_AssemblyConnector(queryInputAssemblyContext);

        final AssemblyContext paymentAssemblyContext = SystemDataFactory.findAssemblyContext(this.system,
                SystemDataFactory.PAYMENT_ASSEMBLY_CONTEXT);
        final OperationProvidedRole providedPayRole = RepositoryModelDataFactory.findProvidedRole(
                paymentAssemblyContext.getEncapsulatedComponent__AssemblyContext(),
                RepositoryModelDataFactory.PAYMENT_PROVIDED_ROLE);
        final OperationRequiredRole requiredPayRole = RepositoryModelDataFactory.findRequiredRole(
                sharedOrderContext.getEncapsulatedComponent__AssemblyContext(),
                RepositoryModelDataFactory.PAYMENT_REQUIRED_ROLE);

        final AssemblyConnector sharedPayConnector = CompositionFactory.eINSTANCE.createAssemblyConnector();
        sharedPayConnector.setEntityName("sharedPayment");

        sharedPayConnector.setProvidedRole_AssemblyConnector(providedPayRole);
        sharedPayConnector.setRequiredRole_AssemblyConnector(requiredPayRole);
        sharedPayConnector.setProvidingAssemblyContext_AssemblyConnector(paymentAssemblyContext);
        sharedPayConnector.setRequiringAssemblyContext_AssemblyConnector(sharedOrderContext);

        this.testModel.getAssemblyContexts__ComposedStructure().add(sharedOrderContext);
        this.testModel.getConnectors__ComposedStructure().add(sharedQueryInputConnector);
        this.testModel.getConnectors__ComposedStructure().add(sharedPayConnector);

        modelProvider.updateObject(System.class, this.testModel);

        final System readModel = modelProvider.getModelRootNode(System.class);

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

    @Override
    @Test
    public void createThenDeleteObject() {
        final ModelGraph graph = this.prepareGraph("createThenDeleteObject");

        final ModelProvider<System> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final System writtenModel = this.system;

        modelProvider.storeModelPartition(writtenModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        modelProvider.deleteObjectByTypeAndId(System.class, writtenModel.getId());

        final ModelProvider<System> checkModelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        final List<String> collection = checkModelProvider.collectAllObjectIdsByType(System.class);

        Assert.assertEquals("The system should be deleted.", 0, collection.size());
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
     * {@link ModelProvider#getObjectsByTypeAndName(Class, String)} and asserts that it is equal to
     * the one written to the graph.
     */
    @Test
    public final void createThenReadByName() {
        final ModelGraph graph = this.prepareGraph("createThenReadByName");

        final ModelProvider<System> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<System> readModels = modelProvider.getObjectsByTypeAndName(this.clazz,
                this.testModel.getEntityName());

        for (final System readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
        }

        graph.getGraphDatabaseService().shutdown();
    }

}
