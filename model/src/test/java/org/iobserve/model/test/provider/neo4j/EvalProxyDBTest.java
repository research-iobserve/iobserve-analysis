/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
import java.io.IOException;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.ModelNeo4JUtil;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.test.data.TestModelData;
import org.iobserve.model.test.storage.one.Other;
import org.iobserve.model.test.storage.one.Root;
import org.iobserve.model.test.storage.two.Link;
import org.iobserve.model.test.storage.two.Two;
import org.iobserve.model.test.storage.two.TwoPackage;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Reiner Jung
 *
 */
public class EvalProxyDBTest {
    private static final String MODEL_ONE = "/tmp/model-one.xml";
    private static final String MODEL_TWO = "/tmp/model-two.xml";

    private String prefix;

    /**
     * Setup test.
     *
     * @throws Exception
     *             on error
     */
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();
    }

    @Test
    public void test() throws IOException, DBException {
        new File(EvalProxyDBTest.MODEL_ONE).delete();

        // create model
        final Root modelOne = TestModelData.createModelOne();
        final Two modelTwo = TestModelData.createModelTwo();

        final Registry resourceRegistry = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.setResourceFactoryRegistry(resourceRegistry);

        // put in resources
        final Resource resourceOne = resourceSet.createResource(URI.createFileURI(EvalProxyDBTest.MODEL_ONE));
        resourceOne.getContents().add(modelOne);

        final Resource resourceTwo = resourceSet.createResource(URI.createFileURI(EvalProxyDBTest.MODEL_TWO));
        resourceTwo.getContents().add(modelTwo);

        // save models
        resourceOne.save(null);
        resourceTwo.save(null);

        // store in DB
        final ModelResource<Root> dbResourceOne = ModelProviderTestUtils.prepareResource("testStoreGraphCreate1",
                this.prefix, TwoPackage.eINSTANCE);

        dbResourceOne.storeModelPartition(modelOne);

        final ModelResource<Two> dbResourceTwo = ModelProviderTestUtils.prepareResource("testStoreGraphCreate2",
                this.prefix, TwoPackage.eINSTANCE);

        dbResourceTwo.storeModelPartition(modelTwo);

        // retrieve model two
        final Two retrievedModelTwo = dbResourceTwo.getModelRootNode(Two.class, TwoPackage.Literals.TWO);

        this.checkProxies("A", retrievedModelTwo);
        ModelNeo4JUtil.printModel(retrievedModelTwo);
    }

    private void checkProxies(final String label, final Two retrievedModelTwo) {
        System.out.println("Step " + label);
        for (final Link l : retrievedModelTwo.getLinks()) {
            final Other ref = l.getReference();
            System.out.println(
                    "Link " + ref.getName() + " " + ref.eIsProxy() + " -- " + ((BasicEObjectImpl) ref).eProxyURI());
        }
    }

}
