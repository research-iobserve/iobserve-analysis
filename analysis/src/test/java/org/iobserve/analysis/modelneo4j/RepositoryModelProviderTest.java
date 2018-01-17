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
package org.iobserve.analysis.modelneo4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.io.fs.FileUtils;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryFactory;

import kieker.common.configuration.Configuration;

/**
 * Test cases for the model provider using a repository model.
 *
 * @author Lars Bluemke RepositoryModelProviderTest.GRAPH_DIR
 */
public class RepositoryModelProviderTest implements IModelProviderTest {
    private static final String GRAPH_DIR = "./testdb";

    private Graph graph;

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    @BeforeClass
    public void before() {
        final Configuration configuration = new Configuration();
        configuration.setProperty(InitializeModelProviders.PCM_MODEL_DIRECTORY, RepositoryModelProviderTest.GRAPH_DIR);
        this.graph = new GraphLoader(configuration).getRepositoryModelGraph();
    }

    @Override
    @Before
    public void clearGraph() {
        new ModelProvider<>(this.graph).clearGraph();
    }

    @Override
    @Test
    public void createThenCloneThenRead() {
        final ModelProvider<Repository> modelProvider1 = new ModelProvider<>(this.graph);
        final ModelProvider<Repository> modelProvider2;
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final Repository readModel;
        final Graph graph2;

        modelProvider1.createComponent(writtenModel);

        graph2 = modelProvider1.cloneNewGraphVersion(Repository.class);
        modelProvider2 = new ModelProvider<>(graph2);

        readModel = modelProvider2.readOnlyRootComponent(Repository.class);
        graph2.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenClearGraph() {
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
        final Repository writtenModel = new TestModelBuilder().getRepository();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenReadById() {
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final Repository readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyComponentById(Repository.class, writtenModel.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadByName() {
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
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
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
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
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final Repository readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyRootComponent(Repository.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
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
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final Repository writtenModel = testModelBuilder.getRepository();
        List<EObject> readReferencingComponents;

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
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final Repository writtenModel = testModelBuilder.getRepository();
        final Interface payInterface = testModelBuilder.getPayInterface();
        final RepositoryComponent paymentComponent = testModelBuilder.getPaymentComponent();
        Repository readModel;

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
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
        final Repository writtenModel = new TestModelBuilder().getRepository();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponent(Repository.class, writtenModel.getId());

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteComponentAndDatatypes() {
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(this.graph);
        final Repository writtenModel = new TestModelBuilder().getRepository();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponentAndDatatypes(Repository.class, writtenModel.getId(), true);

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @After
    public void after() {
        this.graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Remove database directory.
     *
     * @throws IOException
     *             When an error occurs while deleting
     */
    @AfterClass
    public static void cleanUp() throws IOException {
        FileUtils.deleteRecursively(new File(RepositoryModelProviderTest.GRAPH_DIR));
    }

}
