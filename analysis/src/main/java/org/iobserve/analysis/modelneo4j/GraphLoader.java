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
package org.iobserve.analysis.modelneo4j;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Used to load a {@link GraphDatabaseService} including each of the different PCM models.
 *
 * @author Lars Bluemke
 *
 */
public class GraphLoader {

    private static String allocationGraphDir = "allocationmodel";
    private static String repositoryGraphDir = "repositorymodel";
    private static String resourceEnvironmentGraphDir = "resourceenvironmentmodel";
    private static String systemGraphDir = "systemmodel";
    private static String usageGraphDir = "usagemodel";

    private final File pcmModelsNeo4jDirectory;

    /**
     * Creates a graph loader for for Neo4j databases in the specified directory.
     *
     * @param pcmModelsN4jDirectory
     *            The directory
     */
    public GraphLoader(final File pcmModelsN4jDirectory) {
        this.pcmModelsNeo4jDirectory = pcmModelsN4jDirectory;
    }

    /**
     * Returns the allocation model graph. If there is none yet an empty graph is returned.
     *
     * @return The allocation model graph
     */
    public GraphDatabaseService getAllocationModelGraph() {
        final GraphDatabaseService graph = new GraphDatabaseFactory()
                .newEmbeddedDatabase(new File(this.pcmModelsNeo4jDirectory, GraphLoader.allocationGraphDir));
        this.registerShutdownHook(graph);
        return graph;
    }

    /**
     * Returns the repository model graph. If there is none yet an empty graph is returned.
     *
     * @return The repository model graph
     */
    public GraphDatabaseService getRepositoryModelGraph() {
        final GraphDatabaseService graph = new GraphDatabaseFactory()
                .newEmbeddedDatabase(new File(this.pcmModelsNeo4jDirectory, GraphLoader.repositoryGraphDir));
        this.registerShutdownHook(graph);
        return graph;
    }

    /**
     * Returns the resourceEnvironment model graph. If there is none yet an empty graph is returned.
     *
     * @return The resourceEnvironment model graph
     */
    public GraphDatabaseService getResourceEnvironmentModelGraph() {
        final GraphDatabaseService graph = new GraphDatabaseFactory()
                .newEmbeddedDatabase(new File(this.pcmModelsNeo4jDirectory, GraphLoader.resourceEnvironmentGraphDir));
        this.registerShutdownHook(graph);
        return graph;
    }

    /**
     * Returns the system model graph. If there is none yet an empty graph is returned.
     *
     * @return The system model graph
     */
    public GraphDatabaseService getSystemModelGraph() {
        final GraphDatabaseService graph = new GraphDatabaseFactory()
                .newEmbeddedDatabase(new File(this.pcmModelsNeo4jDirectory, GraphLoader.systemGraphDir));
        this.registerShutdownHook(graph);
        return graph;
    }

    /**
     * Returns the usage model graph. If there is none yet an empty graph is returned.
     *
     * @return The usage model graph
     */
    public GraphDatabaseService getUsageModelGraph() {
        final GraphDatabaseService graph = new GraphDatabaseFactory()
                .newEmbeddedDatabase(new File(this.pcmModelsNeo4jDirectory, GraphLoader.usageGraphDir));
        this.registerShutdownHook(graph);
        return graph;
    }

    /**
     * Initializes an allocation model graph with the given model. Overwrites a possibly existing
     * graph in the database directory of this loader.
     *
     * @param allocationModel
     *            The model
     */
    public void initializeAllocationModelGraph(final Allocation allocationModel) {
        final GraphDatabaseService graph = new GraphDatabaseFactory()
                .newEmbeddedDatabase(new File(this.pcmModelsNeo4jDirectory, GraphLoader.allocationGraphDir));
        this.registerShutdownHook(graph);
        final ModelProvider<Allocation> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(allocationModel);
        graph.shutdown();
    }

    /**
     * Initializes a repository model graph with the given model. Overwrites a possibly existing
     * graph in the database directory of this loader.
     *
     * @param repositoryModel
     *            The model
     */
    public void initializeRepositoryModelGraph(final Repository repositoryModel) {
        final GraphDatabaseService graph = new GraphDatabaseFactory()
                .newEmbeddedDatabase(new File(this.pcmModelsNeo4jDirectory, GraphLoader.repositoryGraphDir));
        this.registerShutdownHook(graph);
        final ModelProvider<Repository> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(repositoryModel);
        graph.shutdown();
    }

    /**
     * Initializes a resource environment model graph with the given model. Overwrites a possibly
     * existing graph in the database directory of this loader.
     *
     * @param resourceEnvironmentModel
     *            The model
     */
    public void initializeResourceEnvironmentModelGraph(final ResourceEnvironment resourceEnvironmentModel) {
        final GraphDatabaseService graph = new GraphDatabaseFactory()
                .newEmbeddedDatabase(new File(this.pcmModelsNeo4jDirectory, GraphLoader.resourceEnvironmentGraphDir));
        this.registerShutdownHook(graph);
        final ModelProvider<ResourceEnvironment> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(resourceEnvironmentModel);
        graph.shutdown();
    }

    /**
     * Initializes a system model graph with the given model. Overwrites a possibly existing graph
     * in the database directory of this loader.
     *
     * @param systemModel
     *            The model
     */
    public void initializeSystemModelGraph(final System systemModel) {
        final GraphDatabaseService graph = new GraphDatabaseFactory()
                .newEmbeddedDatabase(new File(this.pcmModelsNeo4jDirectory, GraphLoader.systemGraphDir));
        this.registerShutdownHook(graph);
        final ModelProvider<System> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(systemModel);
        graph.shutdown();
    }

    /**
     * Initializes an usage model graph with the given model. Overwrites a possibly existing graph
     * in the database directory of this loader.
     *
     * @param usageModel
     *            The model
     */
    public void initializeUsageModelGraph(final UsageModel usageModel) {
        final GraphDatabaseService graph = new GraphDatabaseFactory()
                .newEmbeddedDatabase(new File(this.pcmModelsNeo4jDirectory, GraphLoader.usageGraphDir));
        this.registerShutdownHook(graph);
        final ModelProvider<UsageModel> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(usageModel);
        graph.shutdown();
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
