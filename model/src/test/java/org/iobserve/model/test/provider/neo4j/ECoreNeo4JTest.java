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
import org.iobserve.model.persistence.neo4j.ModelProviderUtil;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.test.storage.one.EnumValueExample;
import org.iobserve.model.test.storage.one.OneFactory;
import org.iobserve.model.test.storage.one.Other;
import org.iobserve.model.test.storage.one.OtherInterface;
import org.iobserve.model.test.storage.one.Root;
import org.iobserve.model.test.storage.one.SpecialA;
import org.iobserve.model.test.storage.one.SpecialB;
import org.iobserve.model.test.storage.two.Link;
import org.iobserve.model.test.storage.two.Two;
import org.iobserve.model.test.storage.two.TwoFactory;
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
    private OneFactory oneFactory;
    private TwoFactory twoFactory;

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    /**
     * Setup test.
     *
     * @throws Exception
     *             on error
     */
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();

        this.createModelOne();
        this.createModelTwo();

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

    private void createModelTwo() {
        this.twoFactory = TwoFactory.eINSTANCE;

        this.modelTwo = this.twoFactory.createTwo();

        this.modelTwo.getLinks().add(this.createLink(this.modelOne.getOthers().get(0)));
    }

    private Link createLink(final Other other) {
        final Link link = this.twoFactory.createLink();
        link.setReference(other);
        return link;
    }

    private void createModelOne() {
        this.oneFactory = OneFactory.eINSTANCE;

        this.modelOne = this.oneFactory.createRoot();
        this.modelOne.setEnumerate(EnumValueExample.B);
        this.modelOne.setFixed("fixed value");
        this.modelOne.setName("root name");

        this.modelOne.getLabels().add("label 1");
        this.modelOne.getLabels().add("label 2");
        this.modelOne.getLabels().add("label 3");

        final Other firstOther = this.createOther("first other");
        this.modelOne.getOthers().add(firstOther);
        this.modelOne.getOthers().add(this.createOther("second other"));

        this.modelOne.getIfaceOthers().add(this.createSpecialA(firstOther));
        this.modelOne.getIfaceOthers().add(this.createSpecialB());
    }

    private OtherInterface createSpecialB() {
        final SpecialA subspecial = this.oneFactory.createSpecialA();

        final SpecialB special = this.oneFactory.createSpecialB();
        special.setNext(subspecial);
        return special;
    }

    private OtherInterface createSpecialA(final Other other) {
        final SpecialB subspecial = this.oneFactory.createSpecialB();

        final SpecialA special = this.oneFactory.createSpecialA();
        special.setNext(subspecial);
        special.setRelate(other);
        return special;
    }

    private Other createOther(final String string) {
        final Other other = this.oneFactory.createOther();
        other.setName(string);

        return other;
    }

    /**
     * Test whether the model is stored correctly.
     */
    @Test
    public void testStoreResourceCreate() {
        final ModelResource resource = ModelProviderTestUtils.prepareResource("testStoreGraphCreate", this.prefix,
                this.oneFactory);

        resource.storeModelPartition(this.modelOne);

        Assert.assertFalse(ModelProviderTestUtils.isResourceEmpty(resource));

        resource.clearResource();

        Assert.assertTrue(ModelProviderTestUtils.isResourceEmpty(resource));

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Test whether the model is stored correctly.
     */
    @Test
    public void testStoreResourceAndRead() {
        final ModelResource resource = ModelProviderTestUtils.prepareResource("testStoreGraphAndRead", this.prefix,
                this.oneFactory);

        resource.storeModelPartition(this.modelOne);

        final List<Root> readModel = resource.findObjectsByTypeAndName(Root.class, "name", this.modelOne.getName());

        Assert.assertTrue(this.equalityHelper.equals(this.modelOne, readModel.get(0)));

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Test create, clone and read sequence.
     */
    @Test
    public void createThenCloneThenRead() {
        final ModelResource storeResource = ModelProviderTestUtils.prepareResource("createThenCloneThenRead",
                this.prefix, this.oneFactory);

        storeResource.storeModelPartition(this.modelOne);
        storeResource.getGraphDatabaseService().shutdown();

        final ModelResource newVersionResource = ModelProviderUtil.createNewModelResourceVersion(OneFactory.eINSTANCE,
                storeResource);

        final Root clonedModel = newVersionResource.getModelRootNode(Root.class);
        newVersionResource.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(this.modelOne, clonedModel));
    }

    @Test
    public void createWithReferenceThenUpdate() throws Exception {
        final ModelResource oneResource = ModelProviderTestUtils.prepareResource("createWithReferenceThenUpdate-one",
                this.prefix, this.oneFactory);
        final ModelResource twoResource = ModelProviderTestUtils.prepareResource("createWithReferenceThenUpdate-two",
                this.prefix, this.twoFactory);

        oneResource.storeModelPartition(this.modelOne);
        twoResource.storeModelPartition(this.modelTwo);

        this.modelTwo.getLinks().add(this.createLink(this.modelOne.getOthers().get(1)));

        twoResource.updatePartition(Two.class, this.modelTwo);

        final Two two = twoResource.getModelRootNode(Two.class);

        Assert.assertThat("Different number of links", two.getLinks().size(), Is.is(this.modelTwo.getLinks().size()));
        for (int i = 0; i < two.getLinks().size(); i++) {
            final Link newLink = two.getLinks().get(i);
            final Link oldLink = this.modelTwo.getLinks().get(i);

            oneResource.resolve(newLink);

            Assert.assertThat("Links differ", newLink.getReference().getName(),
                    Is.is(oldLink.getReference().getName()));
        }
    }

}
