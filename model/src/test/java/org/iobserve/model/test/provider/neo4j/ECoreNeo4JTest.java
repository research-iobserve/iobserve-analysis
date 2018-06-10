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

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.model.provider.neo4j.ModelProvider;
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
public class ECoreNeo4JTest {

    private static final File GRAPH_DIR = new File("./testdb");

    private static Graph graph = new GraphLoader(ECoreNeo4JTest.GRAPH_DIR).createModelGraph(StorageFactory.eINSTANCE);

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    private Root model = null;

    /**
     * Setup test.
     *
     * @throws Exception
     *             on error
     */
    @Before
    public void setUp() throws Exception {
        final StorageFactory factory = StorageFactory.eINSTANCE;
        this.model = factory.createRoot();
        this.model.setEnumerate(EnumValueExample.B);
        this.model.setFixed("fixed value");
        this.model.setName("root name");

        this.model.getLabels().add("label 1");
        this.model.getLabels().add("label 2");
        this.model.getLabels().add("label 3");

        final Other firstOther = this.createOther(factory, "first other");
        this.model.getOthers().add(firstOther);
        this.model.getOthers().add(this.createOther(factory, "second other"));

        this.model.getIfaceOthers().add(this.createSpecialA(factory, firstOther));
        this.model.getIfaceOthers().add(this.createSpecialB(factory));

        final Registry resourceRegistry = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = resourceRegistry.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resourceSet = new ResourceSetImpl();
        resourceSet.setResourceFactoryRegistry(resourceRegistry);
        final Resource resource = resourceSet.createResource(URI.createFileURI("storage-example.storage"));
        resource.getContents().add(this.model);
        resource.save(null);

        this.clearGraph();
    }

    private void clearGraph() {
        new ModelProvider<>(ECoreNeo4JTest.graph, "name", null).clearGraph();
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
        final ModelProvider<Root> modelProvider = new ModelProvider<>(ECoreNeo4JTest.graph, "name", null);

        modelProvider.storeModelPartition(this.model);

        Assert.assertFalse(ECoreNeo4JTest.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));

    }

    /**
     * Test whether the model is stored correctly.
     */
    @Test
    public void testStoreGraphAndRead() {
        final ModelProvider<Root> modelProvider = new ModelProvider<>(ECoreNeo4JTest.graph, "name", null);

        modelProvider.storeModelPartition(this.model);

        final GraphDatabaseService service = modelProvider.getGraph().getGraphDatabaseService();

        try (Transaction tx = ECoreNeo4JTest.graph.getGraphDatabaseService().beginTx()) {
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

        final List<Root> readModel = modelProvider.readOnlyComponentByName(Root.class, this.model.getName());

        Assert.assertTrue(this.equalityHelper.equals(this.model, readModel.get(0)));
    }

    /**
     * Checks whether the graph of a given {@link ModelProvider} is empty.
     *
     * @param modelProvider
     *            A model provider, containing a graph
     * @return True if the graph is empty, false otherwise
     */
    static boolean isGraphEmpty(final IModelProvider<Root> modelProvider) {
        boolean isEmpty = false;

        try (Transaction tx = modelProvider.getGraph().getGraphDatabaseService().beginTx()) {
            isEmpty = !modelProvider.getGraph().getGraphDatabaseService().getAllNodes().iterator().hasNext();
            tx.success();
        }

        return isEmpty;
    }

}
