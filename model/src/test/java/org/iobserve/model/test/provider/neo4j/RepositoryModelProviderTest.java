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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.io.fs.FileUtils;
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
public class RepositoryModelProviderTest implements IModelProviderTest { // NOCS no constructor in
                                                                         // test
    private static final File GRAPH_DIR = new File("./testdb");

    private static Graph<Repository> graph = new GraphLoader(RepositoryModelProviderTest.GRAPH_DIR)
            .createModelGraph(Repository.class);

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    @Override
    @Before
    public void clearGraph() {
        new ModelProvider<>(RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID)
                .clearGraph();
    }

    @Override
    @Test
    public void createThenCloneThenRead() {
        final ModelProvider<Repository, Repository> modelProvider1 = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final ModelProvider<Repository, Repository> modelProvider2;
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final Repository readModel;
        final Graph<Repository> graph2;

        modelProvider1.createComponent(writtenModel);

        graph2 = modelProvider1.cloneNewGraphVersion(Repository.class);
        modelProvider2 = new ModelProvider<>(graph2, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        readModel = modelProvider2.readOnlyRootComponent(Repository.class);
        graph2.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenClearGraph() {
        final ModelProvider<Repository, Repository> modelProvider = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenReadById() {
        final ModelProvider<Repository, Repository> modelProvider = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final Repository readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyComponentById(Repository.class, writtenModel.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadByName() {
        final ModelProvider<Repository, Repository> modelProvider = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final List<Repository> readModels;

        modelProvider.createComponent(writtenModel);
        readModels = modelProvider.readOnlyComponentByName(Repository.class, writtenModel.getEntityName());

        for (final Repository readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
        }
    }

    @Override
    @Test
    public void createThenReadByType() {
        final ModelProvider<Repository, Repository> modelProvider = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final List<String> readIds;

        modelProvider.createComponent(writtenModel);
        readIds = modelProvider.readComponentByType(Repository.class);

        for (final String readId : readIds) {
            Assert.assertTrue(writtenModel.getId().equals(readId));
        }

    }

    @Override
    @Test
    public void createThenReadRoot() {
        final ModelProvider<Repository, Repository> modelProvider = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final Repository readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyRootComponent(Repository.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelProvider<Repository, Repository> modelProvider = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final OperationInterface writtenInterface = (OperationInterface) writtenModel.getInterfaces__Repository()
                .get(0);
        final Repository readModel;

        modelProvider.createComponent(writtenModel);
        readModel = (Repository) modelProvider.readOnlyContainingComponentById(OperationInterface.class,
                writtenInterface.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelProvider<Repository, Repository> modelProvider = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final Repository writtenModel = testModelBuilder.getRepository();
        final List<EObject> readReferencingComponents;

        modelProvider.createComponent(writtenModel);

        readReferencingComponents = modelProvider.readOnlyReferencingComponentsById(BasicComponent.class,
                testModelBuilder.getCatalogSearchComponent().getId());

        // Only the providedSearchOperation role is referencing the catalogSearch component
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(this.equalityHelper.equals(testModelBuilder.getProvidedSearchOperation(),
                readReferencingComponents.get(0)));

    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final ModelProvider<Repository, Repository> modelProvider = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final Repository writtenModel = testModelBuilder.getRepository();
        final Interface payInterface = testModelBuilder.getPayInterface();
        final RepositoryComponent paymentComponent = testModelBuilder.getPaymentComponent();
        final Repository readModel;

        modelProvider.createComponent(writtenModel);

        // Update the model by renaming and replacing the payment method
        writtenModel.setEntityName("MyVideoOnDemandService");

        final OperationProvidedRole providedPayOperation = RepositoryFactory.eINSTANCE.createOperationProvidedRole();
        providedPayOperation.setEntityName("payPalPayment");
        providedPayOperation.setProvidedInterface__OperationProvidedRole((OperationInterface) payInterface);

        paymentComponent.getProvidedRoles_InterfaceProvidingEntity().clear();
        paymentComponent.getProvidedRoles_InterfaceProvidingEntity().add(providedPayOperation);

        modelProvider.updateComponent(Repository.class, writtenModel);

        readModel = modelProvider.readOnlyRootComponent(Repository.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenDeleteComponent() {
        final ModelProvider<Repository, Repository> modelProvider = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponent(Repository.class, writtenModel.getId());

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteComponentAndDatatypes() {
        final ModelProvider<Repository, Repository> modelProvider = new ModelProvider<>(
                RepositoryModelProviderTest.graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponentAndDatatypes(Repository.class, writtenModel.getId(), true);

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
        RepositoryModelProviderTest.graph.getGraphDatabaseService().shutdown();

        FileUtils.deleteRecursively(RepositoryModelProviderTest.GRAPH_DIR);
    }

}
