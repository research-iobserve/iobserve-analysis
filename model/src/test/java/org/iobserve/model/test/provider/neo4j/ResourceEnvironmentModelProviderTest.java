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
import org.iobserve.model.test.data.DebugHelper;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
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
        this.testModel = this.resourceEnvironment;
        this.factory = ResourceenvironmentFactory.eINSTANCE;
        this.clazz = ResourceEnvironment.class;
    }

    /**
     * Create model, store then read by type.
     */
    @Override
    @Test
    public void createThenReadByType() {
        final ModelResource resource = ModelProviderTestUtils.prepareResource("createThenReadByType", this.prefix,
                this.factory);

        final List<ResourceContainer> writtenContainers = this.testModel.getResourceContainer_ResourceEnvironment();

        // Create complete model but only read ResourceContainers because ResourceEnvironment itself
        // has no id
        resource.storeModelPartition(this.testModel);

        // TODO add actual test

    }

    /**
     * Check whether containing references are handled correctly.
     */
    @Override
    @Test
    public void createThenReadContaining() {
        final ModelResource resource = ModelProviderTestUtils.prepareResource("createThenReadContaining", this.prefix,
                this.factory);

        final ResourceContainer writtenContainer = this.testModel.getResourceContainer_ResourceEnvironment().get(0);
        final ResourceEnvironment readModel;

        resource.storeModelPartition(this.testModel);
        readModel = (ResourceEnvironment) resource.readOnlyContainingObjectById(ResourceContainer.class,
                writtenContainer.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

    /**
     * Check is references are resolved correctly.
     */
    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelResource resource = ModelProviderTestUtils.prepareResource("createThenReadReferencing", this.prefix,
                this.factory);

        resource.storeModelPartition(this.testModel);

        final LinkingResource linkingResource = ResourceEnvironmentDataFactory
                .findLinkingResource(this.resourceEnvironment, ResourceEnvironmentDataFactory.LAN_1);

        final CommunicationLinkResourceSpecification resourceSpecification = linkingResource
                .getCommunicationLinkResourceSpecifications_LinkingResource();

        final CommunicationLinkResourceType lanType = resourceSpecification
                .getCommunicationLinkResourceType_CommunicationLinkResourceSpecification();

        final List<EObject> readReferencingComponents = resource
                .collectReferencingObjectsByTypeAndId(CommunicationLinkResourceType.class, lanType.getId());

        // Only the lan1 CommunicationLinkResourceSpecification is referencing the lan1
        // CommunicationLinkResourceType
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(this.equalityHelper.equals(resourceSpecification, readReferencingComponents.get(0)));

    }

    /**
     * Test whether update works correctly.
     */
    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final ModelResource resource = ModelProviderTestUtils.prepareResource("createThenUpdateThenReadUpdated",
                this.prefix, this.factory);

        resource.storeModelPartition(this.testModel);

        final ResourceContainer orderServer = ResourceEnvironmentDataFactory.findContainer(this.resourceEnvironment,
                ResourceEnvironmentDataFactory.BUSINESS_ORDER_CONTAINER);

        final LinkingResource writtenLan1 = ResourceEnvironmentDataFactory.findLinkingResource(this.resourceEnvironment,
                ResourceEnvironmentDataFactory.LAN_1);

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

        resource.updatePartition(ResourceEnvironment.class, this.testModel);

        final ResourceEnvironment readModel = resource.getModelRootNode(ResourceEnvironment.class);

        DebugHelper.printModelPartition(this.testModel);
        DebugHelper.printModelPartition(readModel);

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

    /**
     * Test whether deletions work correctly.
     */
    @Override
    @Test
    public void createThenDeleteObject() {
        final ModelResource resource = ModelProviderTestUtils.prepareResource("createThenDeleteObject", this.prefix,
                this.factory);

        resource.storeModelPartition(this.testModel);

        Assert.assertFalse(ModelProviderTestUtils.isResourceEmpty(resource));

        for (final LinkingResource lr : this.testModel.getLinkingResources__ResourceEnvironment()) {
            resource.deleteObjectByTypeAndId(LinkingResource.class, lr.getId());
        }

        for (final ResourceContainer rc : this.testModel.getResourceContainer_ResourceEnvironment()) {
            resource.deleteObjectByTypeAndId(ResourceContainer.class, rc.getId());
        }

        // Manually delete the root node (as it has no id) and the resource type nodes (as they are
        // no containments anywhere)
        try (Transaction tx = resource.getGraphDatabaseService().beginTx()) {
            resource.getGraphDatabaseService()
                    .execute("MATCH (m:`" + ResourceEnvironment.class.getCanonicalName() + "`), (n:`"
                            + ProcessingResourceType.class.getCanonicalName() + "`), (o:`"
                            + CommunicationLinkResourceType.class.getCanonicalName() + "`) DELETE n, m, o");
            tx.success();
        }

        Assert.assertTrue(ModelProviderTestUtils.isResourceEmpty(resource));
    }

    /**
     * Check whether object and data type deletions work.
     */
    @Override
    @Test
    public void createThenDeleteObjectAndDatatypes() {
        final ModelResource resource = ModelProviderTestUtils.prepareResource("createThenDeleteComponentAndDatatypes",
                this.prefix, this.factory);

        resource.storeModelPartition(this.testModel);

        Assert.assertFalse(ModelProviderTestUtils.isResourceEmpty(resource));

        for (final LinkingResource lr : this.testModel.getLinkingResources__ResourceEnvironment()) {
            resource.deleteObjectByIdAndDatatype(LinkingResource.class, lr.getId(), true);
        }

        for (final ResourceContainer rc : this.testModel.getResourceContainer_ResourceEnvironment()) {
            resource.deleteObjectByIdAndDatatype(ResourceContainer.class, rc.getId(), true);
        }

        Assert.assertTrue(ModelProviderTestUtils.isResourceEmpty(resource));
    }

}
