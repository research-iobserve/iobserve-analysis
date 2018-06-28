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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryFactory;

/**
 * Test cases for the model provider using a repository model.
 *
 * @author Lars Bluemke RepositoryModelProviderTest.GRAPH_DIR
 *
 * @since 0.0.2
 */
public class RepositoryModelProviderTest extends AbstractEnityModelProviderTest<Repository> { // NOCS
                                                                                              // no
    // constructor in

    @Override
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();
        this.testModel = this.testModelBuilder.getRepository();
        this.factory = RepositoryFactory.eINSTANCE;
        this.clazz = Repository.class;
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelGraph graph = this.prepareGraph("createThenReadContaining");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final OperationInterface writtenInterface = (OperationInterface) this.testModel.getInterfaces__Repository()
                .get(0);

        modelProvider.storeModelPartition(this.testModel);

        final Repository readModel = (Repository) modelProvider
                .readOnlyContainingComponentById(OperationInterface.class, writtenInterface.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#collectAllObjectIdsByType(Class)} and asserts that it is equal to the
     * one written to the graph.
     */
    @Override
    @Test
    public final void createThenReadByType() {
        final ModelGraph graph = this.prepareGraph("createThenReadByType");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<String> collectedIds = modelProvider.collectAllObjectIdsByType(this.clazz);

        for (final String id : collectedIds) {
            Assert.assertTrue(this.testModel.getId().equals(id));
        }

        graph.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelGraph graph = this.prepareGraph("createThenReadReferencing");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<EObject> readReferencingComponents = modelProvider.collectReferencingObjectsByTypeAndId(
                BasicComponent.class, this.testModelBuilder.getCatalogSearchComponent().getId());

        // Only the providedSearchOperation role is referencing the catalogSearch component
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(this.equalityHelper.equals(this.testModelBuilder.getProvidedSearchOperation(),
                readReferencingComponents.get(0)));

        graph.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final ModelGraph graph = this.prepareGraph("createThenUpdateThenReadUpdated");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        final Repository testModel = this.testModelBuilder.getRepository();
        final Interface payInterface = this.testModelBuilder.getPayInterface();
        final RepositoryComponent paymentComponent = this.testModelBuilder.getPaymentComponent();

        modelProvider.storeModelPartition(testModel);

        // Update the model by renaming and replacing the payment method
        testModel.setEntityName("MyVideoOnDemandService");

        final OperationProvidedRole providedPayOperation = ((RepositoryFactory) this.factory)
                .createOperationProvidedRole();
        providedPayOperation.setEntityName("payPalPayment");
        providedPayOperation.setProvidedInterface__OperationProvidedRole((OperationInterface) payInterface);

        paymentComponent.getProvidedRoles_InterfaceProvidingEntity().clear();
        paymentComponent.getProvidedRoles_InterfaceProvidingEntity().add(providedPayOperation);

        modelProvider.updateObject(Repository.class, testModel);

        final Repository readModel = modelProvider.getModelRootNode(Repository.class);

        Assert.assertTrue(this.equalityHelper.equals(testModel, readModel));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, deletes it using
     * {@link ModelProvider#deleteObjectById(Class, String)} and asserts that the graph is empty
     * afterwards.
     */
    @Override
    @Test
    public final void createThenDeleteObject() {
        final ModelGraph graph = this.prepareGraph("createThenDeleteObject");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        modelProvider.deleteObjectByTypeAndId(this.clazz, this.testModel.getId());

        Assert.assertTrue(this.isGraphEmpty(modelProvider));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#getObjectsByTypeAndName(Class, String)} and asserts that it is equal to
     * the one written to the graph.
     */
    @Test
    public final void createThenReadByName() {
        final ModelGraph graph = this.prepareGraph("createThenReadByName");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<Repository> readModels = modelProvider.getObjectsByTypeAndName(this.clazz,
                this.testModel.getEntityName());

        for (final Repository readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
        }

        graph.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenDeleteObjectAndDatatypes() {
        Assert.assertTrue(true);
    }

}
