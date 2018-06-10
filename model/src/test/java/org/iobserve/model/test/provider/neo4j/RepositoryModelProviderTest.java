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
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.junit.Assert;
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
public class RepositoryModelProviderTest implements IModelProviderTest { // NOCS no constructor in
                                                                         // test

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    private Graph prepareGraph(final String name) {
        final File graphBaseDir = new File("./testdb/" + name);

        this.removeDirectory(graphBaseDir);

        final GraphLoader graphLoader = new GraphLoader(graphBaseDir);
        return graphLoader.createModelGraph(RepositoryFactory.eINSTANCE);
    }

    private void removeDirectory(final File dir) {
        if (dir.isDirectory()) {
            for (final File file : dir.listFiles()) {
                this.removeDirectory(file);
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

    @Override
    @Test
    public void createThenCloneThenRead() {
        final Repository testModel = new TestModelBuilder().getRepository();

        final Graph storeGraph = this.prepareGraph("createThenCloneThenRead");

        final ModelProvider<Repository> storeModelProvider = new ModelProvider<>(storeGraph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        storeModelProvider.storeModelPartition(testModel);

        final Graph cloneGraph = storeModelProvider.cloneNewGraphVersion(RepositoryFactory.eINSTANCE);

        final ModelProvider<Repository> cloneModelProvider = new ModelProvider<>(cloneGraph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        final Repository clonedModel = cloneModelProvider.readOnlyRootComponent(Repository.class);
        cloneGraph.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(testModel, clonedModel));
    }

    @Override
    @Test
    public void createThenClearGraph() {
        final Graph graph = this.prepareGraph("createThenClearGraph");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();

        modelProvider.storeModelPartition(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenReadById() {
        final Graph graph = this.prepareGraph("createThenClearGraph");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final Repository readModel;

        modelProvider.storeModelPartition(writtenModel);
        readModel = modelProvider.readOnlyComponentById(Repository.class, writtenModel.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadByName() {
        final Graph graph = this.prepareGraph("createThenClearGraph");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final List<Repository> readModels;

        modelProvider.storeModelPartition(writtenModel);
        readModels = modelProvider.readOnlyComponentByName(Repository.class, writtenModel.getEntityName());

        for (final Repository readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
        }
    }

    @Override
    @Test
    public void createThenReadByType() {
        final Graph graph = this.prepareGraph("createThenClearGraph");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final List<String> readIds;

        modelProvider.storeModelPartition(writtenModel);
        readIds = modelProvider.readComponentByType(Repository.class);

        for (final String readId : readIds) {
            Assert.assertTrue(writtenModel.getId().equals(readId));
        }

    }

    @Override
    @Test
    public void createThenReadRoot() {
        final Graph graph = this.prepareGraph("createThenClearGraph");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final Repository readModel;

        modelProvider.storeModelPartition(writtenModel);
        readModel = modelProvider.readOnlyRootComponent(Repository.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final Graph graph = this.prepareGraph("createThenReadContaining");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();
        final OperationInterface writtenInterface = (OperationInterface) writtenModel.getInterfaces__Repository()
                .get(0);
        final Repository readModel;

        modelProvider.storeModelPartition(writtenModel);
        readModel = (Repository) modelProvider.readOnlyContainingComponentById(OperationInterface.class,
                writtenInterface.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final Graph graph = this.prepareGraph("createThenReadReferencing");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final Repository writtenModel = testModelBuilder.getRepository();
        final List<EObject> readReferencingComponents;

        modelProvider.storeModelPartition(writtenModel);

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
        final Graph graph = this.prepareGraph("createThenUpdateThenReadUpdated");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final Repository writtenModel = testModelBuilder.getRepository();
        final Interface payInterface = testModelBuilder.getPayInterface();
        final RepositoryComponent paymentComponent = testModelBuilder.getPaymentComponent();
        final Repository readModel;

        modelProvider.storeModelPartition(writtenModel);

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
        final Graph graph = this.prepareGraph("createThenDeleteComponent");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();

        modelProvider.storeModelPartition(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponent(Repository.class, writtenModel.getId());

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteComponentAndDatatypes() {
        final Graph graph = this.prepareGraph("createThenDeleteComponentAndDatatypes");

        final ModelProvider<Repository> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final Repository writtenModel = new TestModelBuilder().getRepository();

        modelProvider.storeModelPartition(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponentAndDatatypes(Repository.class, writtenModel.getId(), true);

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    public void clearGraph() {
        // nothing to do
    }

}
