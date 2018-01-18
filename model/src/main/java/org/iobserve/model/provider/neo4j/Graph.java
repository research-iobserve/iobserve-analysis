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
package org.iobserve.model.provider.neo4j;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * Wrapper class for a {@link GraphDatabaseService} together with the {@link File} directory where
 * it is stored.
 *
 * @author Lars Bluemke
 *
 */
public class Graph {

    private final File graphDirectory;
    private final GraphDatabaseService graphDatabaseService;

    /**
     * Creates a new {@link GraphDatabaseService} at the given location.
     *
     * @param graphDirectory
     *            Directory where the graph database shall be located
     */
    public Graph(final File graphDirectory) {
        this.graphDirectory = graphDirectory;
        this.graphDatabaseService = new GraphDatabaseFactory().newEmbeddedDatabase(this.graphDirectory);
        this.registerShutdownHook(this.graphDatabaseService);
    }

    /**
     * Returns the graph databases directory.
     *
     * @return The directory
     */
    public File getGraphDirectory() {
        return this.graphDirectory;
    }

    /**
     * Returns the graph database service.
     *
     * @return The graph database service
     */
    public GraphDatabaseService getGraphDatabaseService() {
        return this.graphDatabaseService;
    }

    /**
     * Registers a shutdown hook for the Neo4j instance so that it shuts down when the VM exits
     * (even if you "Ctrl-C" the running application).
     *
     * @param graph
     *            The Neo4j graph instance
     */
    private void registerShutdownHook(final GraphDatabaseService graph) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graph.shutdown();
            }
        });
    }
}
