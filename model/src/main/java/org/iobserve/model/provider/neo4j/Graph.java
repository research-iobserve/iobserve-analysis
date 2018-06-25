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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EReference;
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
    private final List<EFactory> eFactories = new ArrayList<>();
    private final List<EClass> classes = new ArrayList<>();

    /**
     * Creates a new {@link GraphDatabaseService} at the given location.
     *
     * @param factory
     *            factory for the particular metamodel
     * @param graphDirectory
     *            Directory where the graph database shall be located
     */
    public Graph(final EFactory factory, final File graphDirectory) {
        this.graphDirectory = graphDirectory.getAbsoluteFile();
        if (!this.graphDirectory.exists()) {
            if (!this.graphDirectory.mkdirs()) {
                throw new InternalError("Cannot create directories for path " + this.graphDirectory);
            }
            if (!this.graphDirectory.isDirectory()) {
                throw new InternalError("Path is not a directory " + this.graphDirectory);
            }
        }

        this.graphDatabaseService = new GraphDatabaseFactory().newEmbeddedDatabase(this.graphDirectory);
        this.registerShutdownHook(this.graphDatabaseService);
        this.eFactories.add(factory);
        if (factory.getEPackage() != null) {
            for (final EClassifier classifier : factory.getEPackage().getEClassifiers()) {
                if (classifier instanceof EClass) {
                    this.checkClassForContainmentReferences((EClass) classifier);
                }
            }
        }
    }

    private void checkClassForContainmentReferences(final EClass clazz) {
        if (!this.classes.contains(clazz)) {
            this.classes.add(clazz);
            for (final EReference reference : clazz.getEAllReferences()) {
                final EClass type = reference.getEReferenceType();
                this.addFactoryToCollection(type);
                if (reference.isContainment()) {
                    this.checkClassForContainmentReferences(type);
                }
            }
        }
    }

    private void addFactoryToCollection(final EClass clazz) {
        final EFactory subFactory = clazz.getEPackage().getEFactoryInstance();
        if (!this.eFactories.contains(subFactory)) {
            this.eFactories.add(subFactory);
        }
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

    public List<EFactory> getEFactories() {
        return this.eFactories;
    }
}
