/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.hamcrest.core.Is;
import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.ModelProviderUtil;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.test.data.TestModelData;
import org.iobserve.model.test.storage.one.OnePackage;
import org.iobserve.model.test.storage.one.Root;
import org.iobserve.model.test.storage.two.Link;
import org.iobserve.model.test.storage.two.Two;
import org.iobserve.model.test.storage.two.TwoPackage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class ECoreNeo4JTest {

    private String prefix;
    private Root modelOne;
    private Two modelTwo;

    private final CompareModelPartitions equalityHelper = new CompareModelPartitions();

    /**
     * Setup test.
     *
     * @throws Exception
     *             on error
     */
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();

        this.modelOne = TestModelData.createModelOne();
        this.modelTwo = TestModelData.createModelTwo();

        final Registry resourceRegistry = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        try {
            final ResourceSet resourceSet = new ResourceSetImpl();
            resourceSet.setResourceFactoryRegistry(resourceRegistry);
            final Resource resource = resourceSet.createResource(URI.createFileURI("storage-example.storage"));
            resource.getContents().add(this.modelOne);
            resource.save(null);
        } catch (final IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    /**
     * Test whether the model is stored correctly.
     * 
     * @throws DBException
     */
    @Test
    public void testStoreResourceCreate() throws DBException {
        final ModelResource<Root> resource = ModelProviderTestUtils.prepareResource("testStoreGraphCreate", this.prefix,
                TwoPackage.eINSTANCE);

        resource.storeModelPartition(this.modelOne);

        Assert.assertFalse(ModelProviderTestUtils.isResourceEmpty(resource));

        resource.clearResource();

        Assert.assertTrue(ModelProviderTestUtils.isResourceEmpty(resource));

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Test whether the model is stored correctly.
     *
     * @throws DBException
     */
    @Test
    public void testStoreResourceAndRead() throws DBException {
        final ModelResource<Root> resource = ModelProviderTestUtils.prepareResource("testStoreGraphAndRead",
                this.prefix, OnePackage.eINSTANCE);

        resource.storeModelPartition(this.modelOne);

        final List<Root> readModel = resource.findObjectsByTypeAndName(Root.class, OnePackage.Literals.ROOT, "name",
                this.modelOne.getName());

        Assert.assertTrue(
                this.equalityHelper.comparePartition(this.modelOne, readModel.get(0), this.modelOne.eClass()));

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Test create, clone and read sequence.
     *
     * @throws DBException
     */
    @Test
    public void createThenCloneThenRead() throws DBException {
        final ModelResource<Root> storeResource = ModelProviderTestUtils.prepareResource("createThenCloneThenRead",
                this.prefix, OnePackage.eINSTANCE);

        storeResource.storeModelPartition(this.modelOne);
        storeResource.getGraphDatabaseService().shutdown();

        final ModelResource<Root> newVersionResource = ModelProviderUtil
                .createNewModelResourceVersion(OnePackage.eINSTANCE, storeResource);

        final Root clonedModel = newVersionResource.getModelRootNode(Root.class, OnePackage.Literals.ROOT);
        newVersionResource.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.comparePartition(this.modelOne, clonedModel, clonedModel.eClass()));
    }

    @Test
    public void createWithReferenceThenUpdate() throws Exception {
        final ModelResource<Root> oneResource = ModelProviderTestUtils
                .prepareResource("createWithReferenceThenUpdate-one", this.prefix, OnePackage.eINSTANCE);
        final ModelResource<Two> twoResource = ModelProviderTestUtils
                .prepareResource("createWithReferenceThenUpdate-two", this.prefix, TwoPackage.eINSTANCE);

        oneResource.storeModelPartition(this.modelOne);
        twoResource.storeModelPartition(this.modelTwo);

        this.modelTwo.getLinks().add(TestModelData.createLink(TestModelData.SECOND_OTHER));

        twoResource.updatePartition(this.modelTwo);

        final Two two = twoResource.getModelRootNode(Two.class, TwoPackage.Literals.TWO);

        Assert.assertThat("Different number of links", two.getLinks().size(), Is.is(this.modelTwo.getLinks().size()));
        for (int i = 0; i < two.getLinks().size(); i++) {
            final Link newLink = two.getLinks().get(i);
            final Link oldLink = this.modelTwo.getLinks().get(i);

            oneResource.resolve(newLink.getReference());

            Assert.assertThat("Links differ", newLink.getReference().getName(),
                    Is.is(oldLink.getReference().getName()));
        }
    }

}
