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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.model.test.data.TestModelData;
import org.iobserve.model.test.storage.one.OnePackage;
import org.iobserve.model.test.storage.one.Other;
import org.iobserve.model.test.storage.one.Root;
import org.iobserve.model.test.storage.two.Link;
import org.iobserve.model.test.storage.two.Two;
import org.iobserve.model.test.storage.two.TwoPackage;
import org.junit.Test;

/**
 * @author Reiner Jung
 *
 */
public class EvalProxyTest {

    private static final String MODEL_ONE = "/tmp/model-one.xml";
    private static final String MODEL_X_ONE = "/tmp/xmodel-one.xml";
    private static final String MODEL_TWO = "/tmp/model-two.xml";

    @Test
    public void test() throws IOException {
        new File(EvalProxyTest.MODEL_ONE).delete();
        new File(EvalProxyTest.MODEL_X_ONE).delete();

        // create model
        final Root modelOne = TestModelData.createModelOne();
        final Two modelTwo = TestModelData.createModelTwo();

        final Registry resourceRegistry = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.setResourceFactoryRegistry(resourceRegistry);

        // put in resources
        final Resource resourceOne = resourceSet.createResource(URI.createFileURI(EvalProxyTest.MODEL_ONE));
        resourceOne.getContents().add(modelOne);

        final Resource resourceTwo = resourceSet.createResource(URI.createFileURI(EvalProxyTest.MODEL_TWO));
        resourceTwo.getContents().add(modelTwo);

        // save models
        resourceOne.save(null);
        resourceTwo.save(null);

        this.rename(EvalProxyTest.MODEL_ONE, EvalProxyTest.MODEL_X_ONE);

        TwoPackage.eINSTANCE.eClass();
        OnePackage.eINSTANCE.eClass();
        // read model two
        final Registry resourceRegistry2 = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map2 = resourceRegistry2.getExtensionToFactoryMap();
        map2.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resourceSet2 = new ResourceSetImpl();
        resourceSet2.setResourceFactoryRegistry(resourceRegistry2);

        final Resource resourceTwo2 = resourceSet2.getResource(URI.createFileURI(EvalProxyTest.MODEL_TWO), true);

        resourceTwo2.load(resourceSet2.getLoadOptions());

        // check proxies
        this.checkProxies("A", resourceTwo2);

        // read model one
        this.rename(EvalProxyTest.MODEL_X_ONE, EvalProxyTest.MODEL_ONE);

        final Resource resourceOne2 = resourceSet2.getResource(URI.createFileURI(EvalProxyTest.MODEL_ONE), true);
        resourceTwo2.load(resourceSet2.getLoadOptions());

        resourceOne2.load(resourceSet2.getLoadOptions());

        // check proxies
        this.checkProxies("B", resourceTwo2);

        // resolve proxies
        for (final Resource a : resourceSet2.getResources()) {
            System.out.println("resource " + a.getURI());
        }

        EcoreUtil.resolveAll(resourceTwo2);

        // check proxies
        this.checkProxies("C", resourceTwo2);

        // do it again
        final Registry resourceRegistry3 = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map3 = resourceRegistry2.getExtensionToFactoryMap();
        map3.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resourceSet3 = new ResourceSetImpl();
        resourceSet3.setResourceFactoryRegistry(resourceRegistry3);

        final Resource resourceTwo3 = resourceSet3.getResource(URI.createFileURI(EvalProxyTest.MODEL_TWO), true);

        resourceTwo3.load(resourceSet3.getLoadOptions());

        // check proxies
        this.checkProxies("D", resourceTwo3);

        final Resource resourceOne3 = resourceSet3.getResource(URI.createFileURI(EvalProxyTest.MODEL_ONE), true);
        resourceOne3.load(resourceSet3.getLoadOptions());

        // check proxies
        this.checkProxies("E", resourceTwo3);

    }

    private void rename(final String filename1, final String filename2) throws IOException {
        // File (or directory) with old name
        final File file = new File(filename1);

        // File (or directory) with new name
        final File file2 = new File(filename2);

        if (file2.exists()) {
            throw new IOException("file exists " + filename2);
        }

        // Rename file (or directory)
        file.renameTo(file2);
    }

    private void checkProxies(final String label, final Resource resource) {
        System.out.println("Step " + label);
        for (final EObject o : resource.getContents()) {
            if (o instanceof Two) {
                for (final EObject l : ((Two) o).getLinks()) {
                    if (l instanceof Link) {
                        final Other ref = ((Link) l).getReference();
                        System.out.println("Link " + ref.getName() + " " + ref.eIsProxy() + " -- "
                                + ((BasicEObjectImpl) ref).eProxyURI());
                    }
                }
            }
        }
    }

}
