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
package org.iobserve.model.provider.neo4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.neo4j.io.fs.FileUtils;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.resourcetype.CommunicationLinkResourceType;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;

/**
 * Test cases for the model provider using a resource environment model.
 *
 * @author Lars Bluemke
 *
 * @since 0.0.2
 */
public class ResourceEnvironmentModelProviderTest implements IModelProviderTest { // NOCS no
                                                                                  // constructor in
                                                                                  // test
    private static final File GRAPH_DIR = new File("./testdb");

    private static Graph<ResourceEnvironment> graph = new GraphLoader(ResourceEnvironmentModelProviderTest.GRAPH_DIR)
            .createModelGraph(ResourceEnvironment.class);

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    @Override
    @Before
    public void clearGraph() {
        new ModelProvider<>(ResourceEnvironmentModelProviderTest.graph).clearGraph();
    }

    @Override
    @Test
    public void createThenCloneThenRead() {
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider1 = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider2;
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        final ResourceEnvironment readModel;
        final Graph<ResourceEnvironment> graph2;

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
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenReadById() {
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final ModelProvider<ResourceEnvironment, ResourceContainer> modelProvider2 = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
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
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        final List<ResourceEnvironment> readModels;

        modelProvider.createComponent(writtenModel);
        readModels = modelProvider.readOnlyComponentByName(ResourceEnvironment.class, writtenModel.getEntityName());

        Assert.assertTrue(readModels.size() == 1);

        for (final ResourceEnvironment readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
        }
    }

    @Override
    @Test
    public void createThenReadByType() {
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final ModelProvider<ResourceEnvironment, ResourceContainer> modelProvider2 = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        final List<ResourceContainer> writtenContainers = writtenModel.getResourceContainer_ResourceEnvironment();
        final List<String> readIds;

        // Create complete model but only read ResourceContainers because ResourceEnvironment itself
        // has no id
        modelProvider.createComponent(writtenModel);
        readIds = modelProvider2.readComponentByType(ResourceContainer.class);

        Assert.assertTrue(readIds.size() == writtenContainers.size());

        for (int i = 0; i < readIds.size(); i++) {
            boolean foundEqualElem = false;

            for (int j = 0; j < readIds.size(); j++) {
                if (writtenContainers.get(i).getId().equals(readIds.get(j))) {
                    foundEqualElem = true;
                }
            }

            Assert.assertTrue(foundEqualElem);
        }
    }

    @Override
    @Test
    public void createThenReadRoot() {
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        final ResourceEnvironment readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyRootComponent(ResourceEnvironment.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();
        final ResourceContainer writtenContainer = writtenModel.getResourceContainer_ResourceEnvironment().get(0);
        final ResourceEnvironment readModel;

        modelProvider.createComponent(writtenModel);
        readModel = (ResourceEnvironment) modelProvider.readOnlyContainingComponentById(ResourceContainer.class,
                writtenContainer.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final ResourceEnvironment writtenModel = testModelBuilder.getResourceEnvironment();
        final List<EObject> readReferencingComponents;

        modelProvider.createComponent(writtenModel);

        readReferencingComponents = modelProvider.readOnlyReferencingComponentsById(CommunicationLinkResourceType.class,
                testModelBuilder.getLan1Type().getId());

        // Only the lan1 CommunicationLinkResourceSpecification is referencing the lan1
        // CommunicationLinkResourceType
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(
                this.equalityHelper.equals(testModelBuilder.getLan1Specification(), readReferencingComponents.get(0)));

    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final ResourceEnvironment writtenModel = testModelBuilder.getResourceEnvironment();
        final ResourceContainer orderServer = testModelBuilder.getOrderServer();
        final LinkingResource writtenLan1 = testModelBuilder.getLan1();
        final ResourceEnvironment readModel;

        modelProvider.createComponent(writtenModel);

        // Update the model by replacing the orderServer by two separated servers
        writtenModel.getResourceContainer_ResourceEnvironment().remove(orderServer);
        writtenLan1.getConnectedResourceContainers_LinkingResource().remove(orderServer);

        final ResourceContainer businessOrderServer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        final ProcessingResourceSpecification businessOrderServerSpecification = ResourceenvironmentFactory.eINSTANCE
                .createProcessingResourceSpecification();
        final ProcessingResourceType businessOrderServerType = ResourcetypeFactory.eINSTANCE
                .createProcessingResourceType();

        businessOrderServer.setEntityName("businessOrderServer");
        businessOrderServer.setResourceEnvironment_ResourceContainer(writtenModel);
        businessOrderServer.getActiveResourceSpecifications_ResourceContainer().add(businessOrderServerSpecification);
        businessOrderServerSpecification.setActiveResourceType_ActiveResourceSpecification(businessOrderServerType);
        businessOrderServerType.setEntityName("Cisco Business Server PRO");

        final ResourceContainer privateOrderServer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        final ProcessingResourceSpecification privateOrderServerSpecification = ResourceenvironmentFactory.eINSTANCE
                .createProcessingResourceSpecification();
        final ProcessingResourceType privateOrderServerType = ResourcetypeFactory.eINSTANCE
                .createProcessingResourceType();

        privateOrderServer.setEntityName("privateOrderServer");
        privateOrderServer.setResourceEnvironment_ResourceContainer(writtenModel);
        privateOrderServer.getActiveResourceSpecifications_ResourceContainer().add(privateOrderServerSpecification);
        privateOrderServerSpecification.setActiveResourceType_ActiveResourceSpecification(privateOrderServerType);
        privateOrderServerType.setEntityName("Lenovo High Load Server PRO");

        writtenModel.getResourceContainer_ResourceEnvironment().add(businessOrderServer);
        writtenModel.getResourceContainer_ResourceEnvironment().add(privateOrderServer);
        writtenLan1.getConnectedResourceContainers_LinkingResource().add(businessOrderServer);
        writtenLan1.getConnectedResourceContainers_LinkingResource().add(privateOrderServer);

        modelProvider.updateComponent(ResourceEnvironment.class, writtenModel);

        readModel = modelProvider.readOnlyRootComponent(ResourceEnvironment.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenDeleteComponent() {
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        for (final LinkingResource lr : writtenModel.getLinkingResources__ResourceEnvironment()) {
            new ModelProvider<ResourceEnvironment, LinkingResource>(ResourceEnvironmentModelProviderTest.graph)
                    .deleteComponent(LinkingResource.class, lr.getId());
        }

        for (final ResourceContainer rc : writtenModel.getResourceContainer_ResourceEnvironment()) {
            new ModelProvider<ResourceEnvironment, ResourceContainer>(ResourceEnvironmentModelProviderTest.graph)
                    .deleteComponent(ResourceContainer.class, rc.getId());
        }

        // Manually delete the root node (as it has no id) and the resource type nodes (as they are
        // no containments anywhere)
        try (Transaction tx = ResourceEnvironmentModelProviderTest.graph.getGraphDatabaseService().beginTx()) {
            ResourceEnvironmentModelProviderTest.graph.getGraphDatabaseService().execute(
                    "MATCH (m:ResourceEnvironment), (n:ProcessingResourceType), (o:CommunicationLinkResourceType) DELETE n, m, o");
            tx.success();
        }

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteComponentAndDatatypes() {
        final ModelProvider<ResourceEnvironment, ResourceEnvironment> modelProvider = new ModelProvider<>(
                ResourceEnvironmentModelProviderTest.graph);
        final ResourceEnvironment writtenModel = new TestModelBuilder().getResourceEnvironment();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        for (final LinkingResource lr : writtenModel.getLinkingResources__ResourceEnvironment()) {
            new ModelProvider<ResourceEnvironment, LinkingResource>(ResourceEnvironmentModelProviderTest.graph)
                    .deleteComponentAndDatatypes(LinkingResource.class, lr.getId(), true);
        }

        for (final ResourceContainer rc : writtenModel.getResourceContainer_ResourceEnvironment()) {
            new ModelProvider<ResourceEnvironment, ResourceContainer>(ResourceEnvironmentModelProviderTest.graph)
                    .deleteComponentAndDatatypes(ResourceContainer.class, rc.getId(), true);
        }

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
        ResourceEnvironmentModelProviderTest.graph.getGraphDatabaseService().shutdown();
        FileUtils.deleteRecursively(ResourceEnvironmentModelProviderTest.GRAPH_DIR);
    }

}
