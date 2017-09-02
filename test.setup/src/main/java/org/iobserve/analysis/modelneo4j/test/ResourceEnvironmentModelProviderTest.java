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

import org.iobserve.analysis.modelneo4j.Graph;
import org.iobserve.analysis.modelneo4j.GraphLoader;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.io.fs.FileUtils;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * Test cases for the model provider using a resource environment model.
 *
 * @author Lars Bluemke
 *
 */
public class ResourceEnvironmentModelProviderTest implements IModelProviderTest {
    private static final File GRAPH_DIR = new File("/Users/LarsBlumke/Desktop/testdb");
    private static final Graph GRAPH = new GraphLoader(ResourceEnvironmentModelProviderTest.GRAPH_DIR)
            .getResourceEnvironmentModelGraph();

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    @Override
    @Before
    public void clearGraph() {
        new ModelProvider<>(ResourceEnvironmentModelProviderTest.GRAPH).clearGraph();
    }

    @Override
    @Test
    public void createThenCloneThenRead() {
        final ModelProvider<ResourceEnvironment> modelProvider1 = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.GRAPH);
        final ModelProvider<ResourceEnvironment> modelProvider2;
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        final ResourceEnvironment readModel;
        final Graph graph2;

        modelProvider1.createComponent(writtenModel);

        graph2 = modelProvider1.cloneNewGraphVersion(ResourceEnvironment.class);
        modelProvider2 = new ModelProvider<>(graph2);

        readModel = modelProvider2.readOnlyRootComponent(ResourceEnvironment.class);
        graph2.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenClearGraph() {
        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.GRAPH);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenReadById() {
        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.GRAPH);
        final ModelProvider<ResourceContainer> modelProvider2 = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.GRAPH);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        final ResourceContainer writtenContainer = writtenModel.getResourceContainer_ResourceEnvironment().get(0);
        final ResourceContainer readContainer;

        // Create complete model but only read a ResourceContainer, because ResourceEnvironment
        // itself has no id
        modelProvider.createComponent(writtenModel);
        readContainer = modelProvider2.readOnlyComponentById(ResourceContainer.class, writtenContainer.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenContainer, readContainer));
    }

    @Override
    @Test
    public void createThenReadByName() {
        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.GRAPH);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        final List<ResourceEnvironment> readModels;

        modelProvider.createComponent(writtenModel);
        readModels = modelProvider.readOnlyComponentByName(ResourceEnvironment.class, writtenModel.getEntityName());

        Assert.assertTrue(readModels.size() == 1);

        // final LinkingResource writtenLink =
        // writtenModel.getLinkingResources__ResourceEnvironment().get(0);
        // final LinkingResource readLink =
        // readModels.get(0).getLinkingResources__ResourceEnvironment().get(0);
        // for (int i = 0; i < writtenLink.getConnectedResourceContainers_LinkingResource().size();
        // i++) {
        // System.out.println(
        // i + " " +
        // writtenLink.getConnectedResourceContainers_LinkingResource().get(i).getEntityName());
        // System.out.println(
        // i + " " +
        // readLink.getConnectedResourceContainers_LinkingResource().get(i).getEntityName());
        // }

        for (final ResourceEnvironment readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
        }
    }

    @Override
    @Test
    public void createThenReadByType() {
        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.GRAPH);
        final ModelProvider<ResourceContainer> modelProvider2 = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.GRAPH);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        final List<ResourceContainer> writtenContainers = writtenModel.getResourceContainer_ResourceEnvironment();
        final List<String> readIds;

        // Create complete model but only read ResourceContainers because ResourceEnvironment itself
        // has no id
        modelProvider.createComponent(writtenModel);
        readIds = modelProvider2.readComponentByType(ResourceContainer.class);

        Assert.assertTrue(readIds.size() == writtenContainers.size());

        for (int i = 0; i < readIds.size(); i++) {
            Assert.assertTrue(writtenContainers.get(i).getId().equals(readIds.get(i)));
        }

    }

    @Override
    @Test
    public void createThenReadRoot() {
        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.GRAPH);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        final ResourceEnvironment readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyRootComponent(ResourceEnvironment.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        // final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(
        // ResourceEnvironmentModelProviderTest.GRAPH);
        // final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        // final ResourceEnvironment readModel;
        // final OperationInterface inter = (OperationInterface)
        // writtenModel.getInterfaces__ResourceEnvironment().get(0);
        //
        // modelProvider.createComponent(writtenModel);
        // readModel = (ResourceEnvironment)
        // modelProvider.readOnlyContainingComponentById(OperationInterface.class,
        // inter.getId());
        //
        // Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        // final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(
        // ResourceEnvironmentModelProviderTest.GRAPH);
        // final TestModelBuilder testModelBuilder = new TestModelBuilder();
        // final ResourceEnvironment writtenModel = testModelBuilder.getResourceEnvironment();
        // List<EObject> readReferencingComponents;
        //
        // modelProvider.createComponent(writtenModel);
        //
        // readReferencingComponents =
        // modelProvider.readOnlyReferencingComponentsById(BasicComponent.class,
        // testModelBuilder.getCatalogSearchComponent().getId());
        //
        // // Only the providedSearchOperation role is referencing the catalogSearch component
        // Assert.assertTrue(readReferencingComponents.size() == 1);
        //
        // Assert.assertTrue(this.equalityHelper.equals(testModelBuilder.getProvidedSearchOperation(),
        // readReferencingComponents.get(0)));

    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        // final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(
        // ResourceEnvironmentModelProviderTest.GRAPH);
        // final TestModelBuilder testModelBuilder = new TestModelBuilder();
        // final ResourceEnvironment writtenModel = testModelBuilder.getResourceEnvironment();
        // final Interface payInterface = testModelBuilder.getPayInterface();
        // final ResourceEnvironmentComponent paymentComponent =
        // testModelBuilder.getPaymentComponent();
        // ResourceEnvironment readModel;
        //
        // modelProvider.createComponent(writtenModel);
        //
        // // Update the model by renaming and replacing the payment method
        // writtenModel.setEntityName("MyVideoOnDemandService");
        //
        // final OperationProvidedRole providedPayOperation = ResourceEnvironmentFactory.eINSTANCE
        // .createOperationProvidedRole();
        // providedPayOperation.setEntityName("payPalPayment");
        // providedPayOperation.setProvidedInterface__OperationProvidedRole((OperationInterface)
        // payInterface);
        //
        // paymentComponent.getProvidedRoles_InterfaceProvidingEntity().clear();
        // paymentComponent.getProvidedRoles_InterfaceProvidingEntity().add(providedPayOperation);
        //
        // modelProvider.updateComponent(ResourceEnvironment.class, writtenModel);
        //
        // readModel = modelProvider.readOnlyRootComponent(ResourceEnvironment.class);
        //
        // Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenDeleteComponent() {
        // final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(
        // ResourceEnvironmentModelProviderTest.GRAPH);
        // final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        //
        // modelProvider.createComponent(writtenModel);
        //
        // Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));
        //
        // modelProvider.deleteComponent(ResourceEnvironment.class, writtenModel.getId());
        //
        // Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteComponentAndDatatypes() {
        // final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(
        // ResourceEnvironmentModelProviderTest.GRAPH);
        // final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        //
        // modelProvider.createComponent(writtenModel);
        //
        // Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));
        //
        // modelProvider.deleteComponentAndDatatypes(ResourceEnvironment.class,
        // writtenModel.getId());
        //
        // Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    // @AfterClass
    public static void cleanUp() throws IOException {
        ResourceEnvironmentModelProviderTest.GRAPH.getGraphDatabaseService().shutdown();
        FileUtils.deleteRecursively(ResourceEnvironmentModelProviderTest.GRAPH_DIR);
    }

}
