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
import java.util.Map.Entry;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.model.persistence.neo4j.ModelGraph;
import org.iobserve.model.persistence.neo4j.ModelProvider;
import org.iobserve.model.test.storage.EnumValueExample;
import org.iobserve.model.test.storage.Other;
import org.iobserve.model.test.storage.OtherInterface;
import org.iobserve.model.test.storage.Root;
import org.iobserve.model.test.storage.SpecialA;
import org.iobserve.model.test.storage.SpecialB;
import org.iobserve.model.test.storage.StorageFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.Transaction;

/**
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class ECoreNeo4JTest extends AbstractModelProviderTest<Root> {

    /**
     * Setup test.
     *
     * @throws Exception
     *             on error
     */
    @Override
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();

        final StorageFactory storageFactory = StorageFactory.eINSTANCE;
        this.factory = storageFactory;
        this.clazz = Root.class;

        this.testModel = storageFactory.createRoot();
        this.testModel.setEnumerate(EnumValueExample.B);
        this.testModel.setFixed("fixed value");
        this.testModel.setName("root name");

        this.testModel.getLabels().add("label 1");
        this.testModel.getLabels().add("label 2");
        this.testModel.getLabels().add("label 3");

        final Other firstOther = this.createOther(storageFactory, "first other");
        this.testModel.getOthers().add(firstOther);
        this.testModel.getOthers().add(this.createOther(storageFactory, "second other"));

        this.testModel.getIfaceOthers().add(this.createSpecialA(storageFactory, firstOther));
        this.testModel.getIfaceOthers().add(this.createSpecialB(storageFactory));

        final Registry resourceRegistry = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        try {
            final ResourceSet resourceSet = new ResourceSetImpl();
            resourceSet.setResourceFactoryRegistry(resourceRegistry);
            final Resource resource = resourceSet.createResource(URI.createFileURI("storage-example.storage"));
            resource.getContents().add(this.testModel);
            resource.save(null);
        } catch (final IOException e) {
            Assert.fail(e.getLocalizedMessage());
        }
    }

    private OtherInterface createSpecialB(final StorageFactory factory) {
        final SpecialA subspecial = factory.createSpecialA();

        final SpecialB special = factory.createSpecialB();
        special.setNext(subspecial);
        return special;
    }

    private OtherInterface createSpecialA(final StorageFactory factory, final Other other) {
        final SpecialB subspecial = factory.createSpecialB();

        final SpecialA special = factory.createSpecialA();
        special.setNext(subspecial);
        special.setRelate(other);
        return special;
    }

    private Other createOther(final StorageFactory factory, final String string) {
        final Other other = factory.createOther();
        other.setName(string);

        return other;
    }

    /**
     * Test whether the model is stored correctly.
     */
    @Test
    public void testStoreGraphCreate() {
        final ModelGraph graph = this.prepareGraph("testStoreGraphCreate");

        final ModelProvider<Root> modelProvider = new ModelProvider<>(graph, "name", null);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(this.isGraphEmpty(modelProvider));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Test whether the model is stored correctly.
     */
    @Test
    public void testStoreGraphAndRead() {
        final ModelGraph graph = this.prepareGraph("testStoreGraphAndRead");

        final ModelProvider<Root> modelProvider = new ModelProvider<>(graph, "name", null);

        modelProvider.storeModelPartition(this.testModel);

        final GraphDatabaseService service = modelProvider.getGraph().getGraphDatabaseService();

        try (Transaction tx = graph.getGraphDatabaseService().beginTx()) {
            final ResourceIterable<Node> nodes = service.getAllNodes();

            for (final Node node : nodes) {
                System.err.println("id " + node.getId());
                for (final Entry<String, Object> property : node.getAllProperties().entrySet()) {
                    System.err.println("\tp " + property.getKey() + " = " + property.getValue());
                }
                for (final Label label : node.getLabels()) {
                    System.err.println("\tlabel " + label.toString());
                }
                for (final Relationship relationship : node.getRelationships()) {
                    System.err.println("\trel " + relationship.getId() + " " + relationship.getEndNodeId() + " "
                            + relationship.getStartNodeId());
                }
            }

            tx.success();
        }

        final List<Root> readModel = modelProvider.getObjectsByTypeAndName(Root.class, this.testModel.getName());

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel.get(0)));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Test create, clone and read sequence.
     */
    @Test
    public void createThenCloneThenRead() {
        final ModelGraph storeGraph = this.prepareGraph("createThenCloneThenRead");

        final ModelProvider<Root> storeModelProvider = new ModelProvider<>(storeGraph, "name", null);

        storeModelProvider.storeModelPartition(this.testModel);
        storeGraph.getGraphDatabaseService().shutdown();

        final ModelGraph cloneGraph = storeModelProvider.cloneNewGraphVersion(StorageFactory.eINSTANCE);

        final ModelProvider<Root> cloneModelProvider = new ModelProvider<>(cloneGraph, "name", null);

        final Root clonedModel = cloneModelProvider.getModelRootNode(Root.class);
        cloneGraph.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, clonedModel));
    }

}
