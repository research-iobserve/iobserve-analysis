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
import org.iobserve.model.provider.neo4j.Graph;
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
    // constructor
    // in
    @Override
    @Before
    public void setUp() {
        this.testModel = new TestModelBuilder().getRepository();
        this.factory = RepositoryFactory.eINSTANCE;
        this.clazz = Repository.class;
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final Graph graph = this.prepareGraph("createThenReadContaining");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final OperationInterface writtenInterface = (OperationInterface) this.testModel.getInterfaces__Repository()
                .get(0);

        modelProvider.storeModelPartition(this.testModel);

        final Repository readModel = (Repository) modelProvider
                .readOnlyContainingComponentById(OperationInterface.class, writtenInterface.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#collectAllObjectIdsByType(Class)} and asserts that it is equal to the
     * one written to the graph.
     */
    @Override
    @Test
    public final void createThenReadByType() {
        final Graph graph = this.prepareGraph("createThenClearGraph");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<String> collectedIds = modelProvider.collectAllObjectIdsByType(this.clazz);

        for (final String id : collectedIds) {
            Assert.assertTrue(this.testModel.getId().equals(id));
        }

    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final Graph graph = this.prepareGraph("createThenReadReferencing");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final TestModelBuilder testModelBuilder = new TestModelBuilder();

        final List<EObject> readReferencingComponents = modelProvider.readOnlyReferencingComponentsById(
                BasicComponent.class, testModelBuilder.getCatalogSearchComponent().getId());

        // Only the providedSearchOperation role is referencing the catalogSearch component
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(this.equalityHelper.equals(testModelBuilder.getProvidedSearchOperation(),
                readReferencingComponents.get(0)));

    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final Graph graph = this.prepareGraph("createThenUpdateThenReadUpdated");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final Repository testModel = testModelBuilder.getRepository();
        final Interface payInterface = testModelBuilder.getPayInterface();
        final RepositoryComponent paymentComponent = testModelBuilder.getPaymentComponent();

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

        final Repository readModel = modelProvider.readRootNode(Repository.class);

        Assert.assertTrue(this.equalityHelper.equals(testModel, readModel));
    }

    @Override
    @Test
    void createThenDeleteObject() {
        Assert.assertTrue(true);
    }

    @Override
    void createThenDeleteObjectAndDatatypes() {
        Assert.assertTrue(true);
    }

}
