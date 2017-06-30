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

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

/**
 * Used to load a {@link Graph} including each of the different PCM models.
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
    public Graph getAllocationModelGraph() {
        return new Graph(new File(this.pcmModelsNeo4jDirectory, GraphLoader.allocationGraphDir));
    }

    /**
     * Returns the repository model graph. If there is none yet an empty graph is returned.
     *
     * @return The repository model graph
     */
    public Graph getRepositoryModelGraph() {
        return new Graph(new File(this.pcmModelsNeo4jDirectory, GraphLoader.repositoryGraphDir));
    }

    /**
     * Returns the resourceEnvironment model graph. If there is none yet an empty graph is returned.
     *
     * @return The resourceEnvironment model graph
     */
    public Graph getResourceEnvironmentModelGraph() {
        return new Graph(new File(this.pcmModelsNeo4jDirectory, GraphLoader.resourceEnvironmentGraphDir));
    }

    /**
     * Returns the system model graph. If there is none yet an empty graph is returned.
     *
     * @return The system model graph
     */
    public Graph getSystemModelGraph() {
        return new Graph(new File(this.pcmModelsNeo4jDirectory, GraphLoader.systemGraphDir));
    }

    /**
     * Returns the usage model graph. If there is none yet an empty graph is returned.
     *
     * @return The usage model graph
     */
    public Graph getUsageModelGraph() {
        return new Graph(new File(this.pcmModelsNeo4jDirectory, GraphLoader.usageGraphDir));
    }

    /**
     * Initializes an allocation model graph with the given model. Overwrites a possibly existing
     * graph in the database directory of this loader.
     *
     * @param allocationModel
     *            The model
     */
    public void initializeAllocationModelGraph(final Allocation allocationModel) {
        final Graph graph = new Graph(new File(this.pcmModelsNeo4jDirectory, GraphLoader.allocationGraphDir));
        final ModelProvider<Allocation> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(allocationModel);
        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Initializes a repository model graph with the given model. Overwrites a possibly existing
     * graph in the database directory of this loader.
     *
     * @param repositoryModel
     *            The model
     */
    public void initializeRepositoryModelGraph(final Repository repositoryModel) {
        final Graph graph = new Graph(new File(this.pcmModelsNeo4jDirectory, GraphLoader.repositoryGraphDir));
        final ModelProvider<Repository> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(repositoryModel);
        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Initializes a resource environment model graph with the given model. Overwrites a possibly
     * existing graph in the database directory of this loader.
     *
     * @param resourceEnvironmentModel
     *            The model
     */
    public void initializeResourceEnvironmentModelGraph(final ResourceEnvironment resourceEnvironmentModel) {
        final Graph graph = new Graph(new File(this.pcmModelsNeo4jDirectory, GraphLoader.resourceEnvironmentGraphDir));
        final ModelProvider<ResourceEnvironment> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(resourceEnvironmentModel);
        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Initializes a system model graph with the given model. Overwrites a possibly existing graph
     * in the database directory of this loader.
     *
     * @param systemModel
     *            The model
     */
    public void initializeSystemModelGraph(final System systemModel) {
        final Graph graph = new Graph(new File(this.pcmModelsNeo4jDirectory, GraphLoader.systemGraphDir));
        final ModelProvider<System> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(systemModel);
        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Initializes an usage model graph with the given model. Overwrites a possibly existing graph
     * in the database directory of this loader.
     *
     * @param usageModel
     *            The model
     */
    public void initializeUsageModelGraph(final UsageModel usageModel) {
        final Graph graph = new Graph(new File(this.pcmModelsNeo4jDirectory, GraphLoader.usageGraphDir));
        final ModelProvider<UsageModel> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(usageModel);
        graph.getGraphDatabaseService().shutdown();
    }

}
