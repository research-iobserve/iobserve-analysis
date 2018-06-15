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
import org.neo4j.graphdb.Transaction;
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
public class ResourceEnvironmentModelProviderTest extends AbstractNamedElementModelProviderTest<ResourceEnvironment> { // NOCS
    // no constructor in test

    @Override
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();
        this.testModel = this.testModelBuilder.getResourceEnvironment();
        this.factory = ResourceenvironmentFactory.eINSTANCE;
        this.clazz = ResourceEnvironment.class;
    }

    /**
     * Create model, store then read by type.
     */
    @Override
    @Test
    public void createThenReadByType() {
        final Graph graph = this.prepareGraph("createThenReadByType");

        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(graph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        final ModelProvider<ResourceContainer> modelProviderContainer = new ModelProvider<>(graph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        final List<ResourceContainer> writtenContainers = this.testModel.getResourceContainer_ResourceEnvironment();

        // Create complete model but only read ResourceContainers because ResourceEnvironment itself
        // has no id
        modelProvider.storeModelPartition(this.testModel);

        final List<String> collectedIds = modelProviderContainer.collectAllObjectIdsByType(ResourceContainer.class);

        Assert.assertTrue(collectedIds.size() == writtenContainers.size());

        for (int i = 0; i < collectedIds.size(); i++) {
            boolean foundEqualElem = false;

            for (int j = 0; j < collectedIds.size(); j++) {
                if (writtenContainers.get(i).getId().equals(collectedIds.get(j))) {
                    foundEqualElem = true;
                }
            }

            Assert.assertTrue(foundEqualElem);
        }
    }

    /**
     * Check whether containing references are handled correctly.
     */
    @Override
    @Test
    public void createThenReadContaining() {
        final Graph graph = this.prepareGraph("createThenReadContaining");

        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(graph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        final ResourceContainer writtenContainer = this.testModel.getResourceContainer_ResourceEnvironment().get(0);
        final ResourceEnvironment readModel;

        modelProvider.storeModelPartition(this.testModel);
        readModel = (ResourceEnvironment) modelProvider.readOnlyContainingComponentById(ResourceContainer.class,
                writtenContainer.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

    /**
     * Check is references are resolved correctly.
     */
    @Override
    @Test
    public void createThenReadReferencing() {
        final Graph graph = this.prepareGraph("createThenReadReferencing");

        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(graph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<EObject> readReferencingComponents = modelProvider.readOnlyReferencingComponentsById(
                CommunicationLinkResourceType.class, this.testModelBuilder.getLan1Type().getId());

        // Only the lan1 CommunicationLinkResourceSpecification is referencing the lan1
        // CommunicationLinkResourceType
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(this.equalityHelper.equals(this.testModelBuilder.getLan1Specification(),
                readReferencingComponents.get(0)));

    }

    /**
     * Test whether update works correctly.
     */
    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final Graph graph = this.prepareGraph("createThenUpdateThenReadUpdated");

        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(graph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final ResourceContainer orderServer = this.testModelBuilder.getOrderServer();
        final LinkingResource writtenLan1 = this.testModelBuilder.getLan1();

        // Update the model by replacing the orderServer by two separated servers
        this.testModel.getResourceContainer_ResourceEnvironment().remove(orderServer);
        writtenLan1.getConnectedResourceContainers_LinkingResource().remove(orderServer);

        final ResourceContainer businessOrderServer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        final ProcessingResourceSpecification businessOrderServerSpecification = ResourceenvironmentFactory.eINSTANCE
                .createProcessingResourceSpecification();
        final ProcessingResourceType businessOrderServerType = ResourcetypeFactory.eINSTANCE
                .createProcessingResourceType();

        businessOrderServer.setEntityName("businessOrderServer");
        businessOrderServer.setResourceEnvironment_ResourceContainer(this.testModel);
        businessOrderServer.getActiveResourceSpecifications_ResourceContainer().add(businessOrderServerSpecification);
        businessOrderServerSpecification.setActiveResourceType_ActiveResourceSpecification(businessOrderServerType);
        businessOrderServerType.setEntityName("Cisco Business Server PRO");

        final ResourceContainer privateOrderServer = ResourceenvironmentFactory.eINSTANCE.createResourceContainer();
        final ProcessingResourceSpecification privateOrderServerSpecification = ResourceenvironmentFactory.eINSTANCE
                .createProcessingResourceSpecification();
        final ProcessingResourceType privateOrderServerType = ResourcetypeFactory.eINSTANCE
                .createProcessingResourceType();

        privateOrderServer.setEntityName("privateOrderServer");
        privateOrderServer.setResourceEnvironment_ResourceContainer(this.testModel);
        privateOrderServer.getActiveResourceSpecifications_ResourceContainer().add(privateOrderServerSpecification);
        privateOrderServerSpecification.setActiveResourceType_ActiveResourceSpecification(privateOrderServerType);
        privateOrderServerType.setEntityName("Lenovo High Load Server PRO");

        this.testModel.getResourceContainer_ResourceEnvironment().add(businessOrderServer);
        this.testModel.getResourceContainer_ResourceEnvironment().add(privateOrderServer);
        writtenLan1.getConnectedResourceContainers_LinkingResource().add(businessOrderServer);
        writtenLan1.getConnectedResourceContainers_LinkingResource().add(privateOrderServer);

        modelProvider.updateObject(ResourceEnvironment.class, this.testModel);

        final ResourceEnvironment readModel = modelProvider.readRootNode(ResourceEnvironment.class);

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

    /**
     * Test whether deletions work correctly.
     */
    @Override
    @Test
    public void createThenDeleteObject() {
        final Graph graph = this.prepareGraph("createThenDeleteObject");

        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(graph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        for (final LinkingResource lr : this.testModel.getLinkingResources__ResourceEnvironment()) {
            new ModelProvider<LinkingResource>(graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID)
                    .deleteObjectById(LinkingResource.class, lr.getId());
        }

        for (final ResourceContainer rc : this.testModel.getResourceContainer_ResourceEnvironment()) {
            new ModelProvider<ResourceContainer>(graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID)
                    .deleteObjectById(ResourceContainer.class, rc.getId());
        }

        // Manually delete the root node (as it has no id) and the resource type nodes (as they are
        // no containments anywhere)
        try (Transaction tx = graph.getGraphDatabaseService().beginTx()) {
            graph.getGraphDatabaseService()
                    .execute("MATCH (m:`" + ResourceEnvironment.class.getCanonicalName() + "`), (n:`"
                            + ProcessingResourceType.class.getCanonicalName() + "`), (o:`"
                            + CommunicationLinkResourceType.class.getCanonicalName() + "`) DELETE n, m, o");
            tx.success();
        }

        Assert.assertTrue(this.isGraphEmpty(modelProvider));
    }

    /**
     * Check whether object and data type deletions work.
     */
    @Override
    @Test
    public void createThenDeleteObjectAndDatatypes() {
        final Graph graph = this.prepareGraph("createThenDeleteComponentAndDatatypes");

        final ModelProvider<ResourceEnvironment> modelProvider = new ModelProvider<>(graph,
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        for (final LinkingResource lr : this.testModel.getLinkingResources__ResourceEnvironment()) {
            new ModelProvider<LinkingResource>(graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID)
                    .deleteObjectByIdAndDatatypes(LinkingResource.class, lr.getId(), true);
        }

        for (final ResourceContainer rc : this.testModel.getResourceContainer_ResourceEnvironment()) {
            new ModelProvider<ResourceContainer>(graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID)
                    .deleteObjectByIdAndDatatypes(ResourceContainer.class, rc.getId(), true);
        }

        Assert.assertTrue(this.isGraphEmpty(modelProvider));
    }

}
