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
import org.iobserve.model.test.data.RepositoryModelDataFactory;
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
import org.palladiosimulator.pcm.repository.RepositoryPackage;

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
        this.testModel = this.repository;
        this.ePackage = RepositoryPackage.eINSTANCE;
        this.clazz = Repository.class;
        this.eClass = RepositoryPackage.Literals.REPOSITORY;
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelResource<Repository> resource = ModelProviderTestUtils.prepareResource("createThenReadContaining",
                this.prefix, this.ePackage);

        final OperationInterface writtenInterface = (OperationInterface) this.testModel.getInterfaces__Repository()
                .get(0);

        resource.storeModelPartition(this.testModel);

        final Repository readModel = (Repository) resource.findContainingObjectById(OperationInterface.class,
                RepositoryPackage.Literals.OPERATION_INTERFACE, resource.getInternalId(writtenInterface));

        Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#collectAllObjectIdsByType(Class)} and asserts that it is equal to the
     * one written to the graph.
     */
    @Override
    @Test
    public final void createThenReadByType() {
        final ModelResource<Repository> resource = ModelProviderTestUtils.prepareResource("createThenReadByType",
                this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        // TODO add actual test
        resource.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelResource<Repository> resource = ModelProviderTestUtils.prepareResource("createThenReadReferencing",
                this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        final RepositoryComponent object = RepositoryModelDataFactory.findComponentByName(this.repository,
                RepositoryModelDataFactory.SEARCH_COMPONENT);

        final OperationProvidedRole providedSearchRole = RepositoryModelDataFactory.findProvidedRole(object,
                RepositoryModelDataFactory.CATALOG_SEARCH_PROVIDED_ROLE);

        final Long id = resource.getInternalId(object);

        final List<EObject> readReferencingObjects = resource.collectReferencingObjectsByTypeAndId(BasicComponent.class,
                RepositoryPackage.Literals.BASIC_COMPONENT, id);

        // Only the providedSearchOperation role is referencing the catalogSearch component
        Assert.assertTrue(readReferencingObjects.size() == 1);

        Assert.assertTrue(this.equalityHelper.comparePartition(providedSearchRole, readReferencingObjects.get(0),
                providedSearchRole.eClass()));

        resource.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() throws NodeLookupException {
        final ModelResource<Repository> resource = ModelProviderTestUtils
                .prepareResource("createThenUpdateThenReadUpdated", this.prefix, this.ePackage);

        final Interface payInterface = RepositoryModelDataFactory.findInterfaceByName(this.repository,
                RepositoryModelDataFactory.PAYMENT_INTERFACE);
        final RepositoryComponent paymentComponent = RepositoryModelDataFactory.findComponentByName(this.repository,
                RepositoryModelDataFactory.PAYMENT_COMPONENT);

        resource.storeModelPartition(this.testModel);

        // Update the model by renaming and replacing the payment method
        this.testModel.setEntityName("MyVideoOnDemandService");

        final OperationProvidedRole providedPayOperation = ((RepositoryFactory) this.ePackage.getEFactoryInstance())
                .createOperationProvidedRole();
        providedPayOperation.setEntityName("payPalPayment");
        providedPayOperation.setProvidedInterface__OperationProvidedRole((OperationInterface) payInterface);

        paymentComponent.getProvidedRoles_InterfaceProvidingEntity().clear();
        paymentComponent.getProvidedRoles_InterfaceProvidingEntity().add(providedPayOperation);

        resource.updatePartition(this.testModel);

        final Repository readModel = resource.getModelRootNode(Repository.class, RepositoryPackage.Literals.REPOSITORY);

        Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, deletes it using
     * {@link ModelProvider#deleteObjectById(Class, String)} and asserts that the graph is empty
     * afterwards.
     */
    @Override
    @Test
    public final void createThenDeleteObject() {
        final ModelResource<Repository> resource = ModelProviderTestUtils.prepareResource("createThenDeleteObject",
                this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        Assert.assertFalse(ModelProviderTestUtils.isResourceEmpty(resource));

        resource.deleteObject(this.testModel);

        Assert.assertTrue(ModelProviderTestUtils.isResourceEmpty(resource));

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#findObjectsByTypeAndName(Class, String)} and asserts that it is equal to
     * the one written to the graph.
     */
    @Test
    public final void createThenReadByName() {
        final ModelResource<Repository> resource = ModelProviderTestUtils.prepareResource("createThenReadByName",
                this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        final List<Repository> readModels = resource.findObjectsByTypeAndName(this.clazz, this.eClass, "entityName",
                this.testModel.getEntityName());

        for (final Repository readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));
        }

        resource.getGraphDatabaseService().shutdown();
    }

    @Override
    @Test
    public void createThenDeleteObjectAndDatatypes() {
        Assert.assertTrue(true);
    }

}
