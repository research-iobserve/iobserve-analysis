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

import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.GraphLoader;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.junit.Before;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

/**
 * @author Reiner Jung
 *
 * @param <T>
 *            model root type
 *
 */
public abstract class AbstractModelProviderTest<T extends EObject> {

    protected final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    protected final TestModelBuilder testModelBuilder = new TestModelBuilder();

    protected String prefix;
    protected T testModel;
    protected EFactory factory;
    protected Class<T> clazz;

    /**
     * Clears the graph db for the next test.
     */
    @Before
    abstract void setUp();

    /**
     * Checks whether the graph of a given {@link ModelProvider} is empty.
     *
     * @param modelProvider
     *            A model provider, containing a graph
     * @return True if the graph is empty, false otherwise
     */
    protected boolean isGraphEmpty(final ModelProvider<T> modelProvider) {
        boolean isEmpty = false;

        try (Transaction tx = modelProvider.getGraph().getGraphDatabaseService().beginTx()) {
            final ResourceIterator<Node> iterator = modelProvider.getGraph().getGraphDatabaseService().getAllNodes()
                    .iterator();

            isEmpty = !iterator.hasNext();
            tx.success();
        }

        return isEmpty;
    }

    /**
     * Prepare graph for model.
     *
     * @param name
     *            test directory name
     *
     * @return the prepared graph
     */
    protected Graph prepareGraph(final String name) {
        final File graphBaseDir = new File(
                "./testdb/" + this.prefix + "." + this.factory.eClass().getName() + "." + name);

        this.removeDirectory(graphBaseDir);

        final GraphLoader graphLoader = new GraphLoader(graphBaseDir);
        return graphLoader.createModelGraph(this.factory);
    }

    /**
     * Delete directory tree.
     *
     * @param dir
     *            directory
     */
    protected void removeDirectory(final File dir) {
        if (dir.isDirectory()) {
            for (final File file : dir.listFiles()) {
                this.removeDirectory(file);
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }
}
