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
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.neo4j.ModelGraphFactory;
import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.iobserve.model.persistence.neo4j.NodeLookupException;
import org.iobserve.model.test.data.ResourceEnvironmentDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.resourceenvironment.CommunicationLinkResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ProcessingResourceSpecification;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.resourcetype.CommunicationLinkResourceType;
import org.palladiosimulator.pcm.resourcetype.ProcessingResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;

/**
 * Test cases for the model provider using a resource environment model.
 *
 * @author Lars Bluemke
 *
 * @since 0.0.2
 */
@Ignore
public class ResourceEnvironmentModelProviderTest extends AbstractNamedElementModelProviderTest<ResourceEnvironment> { // NOCS
    // no constructor in test

    @Override
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();
        this.testModel = this.resourceEnvironment;
        this.ePackage = ResourceenvironmentPackage.eINSTANCE;
        this.clazz = ResourceEnvironment.class;
        this.eClass = ResourceenvironmentPackage.Literals.RESOURCE_ENVIRONMENT;
    }

    /**
     * Create model, store then read by type.
     *
     * @throws DBException
     */
    @Override
    @Test
    public void createThenReadByType() throws DBException {
        final Neo4JModelResource<ResourceEnvironment> resource = ModelProviderTestUtils
                .prepareResource("createThenReadByType", this.prefix, this.ePackage);

        final List<ResourceContainer> writtenContainers = this.testModel.getResourceContainer_ResourceEnvironment();

        // Create complete model but only read ResourceContainers because ResourceEnvironment itself
        // has no id
        resource.storeModelPartition(this.testModel);

        // TODO add actual test

    }

    /**
     * Check is references are resolved correctly.
     *
     * @throws DBException
     */
    @Override
    @Test
    public void createThenReadReferencing() throws DBException {
        final Neo4JModelResource<ResourceEnvironment> resource = ModelProviderTestUtils
                .prepareResource("createThenReadReferencing", this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        final LinkingResource linkingResource = ResourceEnvironmentDataFactory
                .findLinkingResource(this.resourceEnvironment, ResourceEnvironmentDataFactory.LAN_1);

        final CommunicationLinkResourceSpecification resourceSpecification = linkingResource
                .getCommunicationLinkResourceSpecifications_LinkingResource();

        final CommunicationLinkResourceType lanType = resourceSpecification
                .getCommunicationLinkResourceType_CommunicationLinkResourceSpecification();

        final List<EObject> readReferencingComponents = resource.collectReferencingObjectsByTypeAndProperty(
                CommunicationLinkResourceType.class, ResourcetypePackage.Literals.COMMUNICATION_LINK_RESOURCE_TYPE,
                ModelGraphFactory.getIdentification(lanType));

        // Only the lan1 CommunicationLinkResourceSpecification is referencing the lan1
        // CommunicationLinkResourceType
        Assert.assertTrue(readReferencingComponents.size() == 1);

        Assert.assertTrue(this.equalityHelper.compareObject(resourceSpecification, readReferencingComponents.get(0)));

    }

    /**
     * Test whether update works correctly.
     *
     * @throws NodeLookupException
     * @throws DBException
     */
    @Override
    @Test
    public void createThenUpdateThenReadUpdated() throws NodeLookupException, DBException {
        final Neo4JModelResource<ResourceEnvironment> resource = ModelProviderTestUtils
                .prepareResource("createThenUpdateThenReadUpdated", this.prefix, this.ePackage);

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

        resource.updatePartition(this.testModel);

        final ResourceEnvironment readModel = resource.getModelRootNode(ResourceEnvironment.class, this.eClass);

        Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));
    }

    /**
     * Test whether deletions work correctly.
     *
     * @throws DBException
     */
    @Override
    @Test
    public void createThenDeleteObject() throws DBException {
        final Neo4JModelResource<ResourceEnvironment> resource = ModelProviderTestUtils
                .prepareResource("createThenDeleteObject", this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        Assert.assertFalse(ModelProviderTestUtils.isResourceEmpty(resource));

        for (final LinkingResource lr : this.testModel.getLinkingResources__ResourceEnvironment()) {
            resource.deleteObject(lr);
        }

        for (final ResourceContainer rc : this.testModel.getResourceContainer_ResourceEnvironment()) {
            resource.deleteObject(rc);
        }

        // Manually delete the root node (as it has no id) and the resource type nodes (as they are
        // no containments anywhere)
        try (Transaction tx = resource.getGraphDatabaseService().beginTx()) {
            resource.getGraphDatabaseService().execute("MATCH (m:`"
                    + ModelProviderTestUtils.removePrefix(ResourceEnvironment.class.getCanonicalName()) + "`), (n:`"
                    + ModelProviderTestUtils.removePrefix(ProcessingResourceType.class.getCanonicalName()) + "`), (o:`"
                    + ModelProviderTestUtils.removePrefix(CommunicationLinkResourceType.class.getCanonicalName())
                    + "`) DELETE n, m, o");
            tx.success();
        }

        Assert.assertTrue(ModelProviderTestUtils.isResourceEmpty(resource));
    }

    @Override
    protected void createThenReadContaining() throws DBException {
        // TODO Auto-generated method stub

    }

    @Override
    void createThenDeleteObjectAndDatatypes() throws DBException {
        // TODO Auto-generated method stub

    }

}
