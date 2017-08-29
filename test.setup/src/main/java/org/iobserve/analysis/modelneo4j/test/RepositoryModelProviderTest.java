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
package org.iobserve.analysis.modelneo4j.test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.modelneo4j.Graph;
import org.iobserve.analysis.modelneo4j.ModelProvider;
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
 *
 * @author Lars Bluemke
 *
 */
public class RepositoryModelProviderTest implements IModelProviderTest {
    protected static final File GRAPH_DIR = new File("/Users/LarsBlumke/Desktop/testdb");
    protected static final Graph GRAPH = new Graph(
            new File(RepositoryModelProviderTest.GRAPH_DIR + "/repositorymodel/repositorymodel_v1"));

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();
    private Repository model = TestModelBuilder.createReposiory();

    // @Override
    @Override
    @Before
    public void clearGraph() {
        new ModelProvider<>(RepositoryModelProviderTest.GRAPH).clearGraph();
    }

    // @Override
    @Override
    @Before
    public void createModel() {
        this.model = TestModelBuilder.createReposiory();
    }

    @Override
    @Test
    public void createThenCloneGraph() {
        final ModelProvider<Repository> modelProvider1 = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);
        final ModelProvider<Repository> modelProvider2;
        final Graph graph2;
        final Repository readModel;

        modelProvider1.createComponent(this.model);

        graph2 = modelProvider1.cloneNewGraphVersion(Repository.class);
        modelProvider2 = new ModelProvider<>(graph2);

        readModel = modelProvider2.readOnlyRootComponent(Repository.class);
        graph2.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(this.model, readModel));
    }

    @Override
    @Test
    public void createThenClearGraph() {
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenReadById() {
        final Repository readModel;
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);
        readModel = modelProvider.readOnlyComponentById(Repository.class, this.model.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.model, readModel));
    }

    @Override
    @Test
    public void createThenReadByName() {
        final List<Repository> readModels;
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);
        readModels = modelProvider.readOnlyComponentByName(Repository.class, this.model.getEntityName());

        for (final Repository readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(this.model, readModel));
        }
    }

    @Override
    @Test
    public void createThenReadByType() {
        final List<String> readIds;
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);
        readIds = modelProvider.readComponentByType(Repository.class);

        for (final String readId : readIds) {
            Assert.assertTrue(this.model.getId().equals(readId));
        }

    }

    @Override
    @Test
    public void createThenReadRoot() {
        final Repository readModel;
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);
        readModel = modelProvider.readOnlyRootComponent(Repository.class);

        Assert.assertTrue(this.equalityHelper.equals(this.model, readModel));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final Repository readModel;
        final OperationInterface inter = (OperationInterface) this.model.getInterfaces__Repository().get(0);
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);
        readModel = (Repository) modelProvider.readOnlyContainingComponentById(OperationInterface.class, inter.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.model, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);
        BasicComponent catalogSearchComp = null;
        OperationProvidedRole providedSearchOperation = null;
        List<EObject> readReferencingComponents;

        for (final RepositoryComponent c : this.model.getComponents__Repository()) {
            if (c.getEntityName().equals("org.mybookstore.orderComponent.catologSearchComponent")) {
                catalogSearchComp = (BasicComponent) c;
                providedSearchOperation = (OperationProvidedRole) catalogSearchComp
                        .getProvidedRoles_InterfaceProvidingEntity().get(0);
            }
        }

        modelProvider.createComponent(this.model);

        readReferencingComponents = modelProvider.readOnlyReferencingComponentsById(BasicComponent.class,
                catalogSearchComp.getId());

        // Only the providedSearchOperation role is referencing the catalogSearch component
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(this.equalityHelper.equals(providedSearchOperation, readReferencingComponents.get(0)));

    }

    @Override
    @Test
    public void updateThenReadUpdated() {
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);
        final Repository updateModel = TestModelBuilder.createReposiory();
        Interface payInterface = null;
        RepositoryComponent paymentComponent = null;
        Repository readModel;

        modelProvider.createComponent(updateModel);

        // Update the model by renaming and replacing payment the method
        updateModel.setEntityName("MyVideoOnDemandService");

        for (final Interface i : updateModel.getInterfaces__Repository()) {
            if (i.getEntityName().equals("IPay")) {
                payInterface = i;
            }
        }

        for (final RepositoryComponent c : updateModel.getComponents__Repository()) {
            if (c.getEntityName().equals("org.mybookstore.paymentComponent")) {
                paymentComponent = c;
            }
        }

        final OperationProvidedRole providedPayOperation = RepositoryFactory.eINSTANCE.createOperationProvidedRole();
        providedPayOperation.setEntityName("payPalPayment");
        providedPayOperation.setProvidedInterface__OperationProvidedRole((OperationInterface) payInterface);

        paymentComponent.getProvidedRoles_InterfaceProvidingEntity().clear();
        paymentComponent.getProvidedRoles_InterfaceProvidingEntity().add(providedPayOperation);

        modelProvider.updateComponent(Repository.class, updateModel);

        readModel = modelProvider.readOnlyRootComponent(Repository.class);

        Assert.assertTrue(this.equalityHelper.equals(updateModel, readModel));
    }

    @Override
    @Test
    public void createThenDeleteComponent() {
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponent(Repository.class, this.model.getId());

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteComponentAndDatatypes() {
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.deleteComponentAndDatatypes(Repository.class, this.model.getId());

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @AfterClass
    public static void cleanUp() throws IOException {
        RepositoryModelProviderTest.GRAPH.getGraphDatabaseService().shutdown();
        FileUtils.deleteRecursively(RepositoryModelProviderTest.GRAPH_DIR);
    }

}
