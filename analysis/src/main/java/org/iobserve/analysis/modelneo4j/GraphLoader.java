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
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
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

    protected static final String VERSION_PREFIX = "_v";

    private static final String ALLOCATION_GRAPH_DIR = "allocationmodel";
    private static final String REPOSITORY_GRAPH_DIR = "repositorymodel";
    private static final String RESOURCEENVIRONMENT_GRAPH_DIR = "resourceenvironmentmodel";
    private static final String SYSTEM_GRAPH_DIR = "systemmodel";
    private static final String USAGE_GRAPH_DIR = "usagemodel";

    private final File neo4jPcmModelDirectory;

    /**
     * Creates a graph loader for for Neo4j databases in the specified directory.
     *
     * @param neo4jPcmModelDirectory
     *            The directory
     */
    public GraphLoader(final File neo4jPcmModelDirectory) {
        this.neo4jPcmModelDirectory = neo4jPcmModelDirectory;
    }

    /**
     * Helper method for cloning: Clones and returns a new version from the current newest version
     * of the model graph.
     *
     * @param graphTypeDirName
     *            Name of root directory for a certain graph type
     * @return The the model graph
     */
    private Graph cloneNewModelGraphVersion(final String graphTypeDirName) {
        final File graphTypeDir = new File(this.neo4jPcmModelDirectory, graphTypeDirName);
        final int maxVersionNumber = GraphLoaderUtil.getMaxVersionNumber(graphTypeDir.listFiles());
        final File newGraphDir = new File(graphTypeDir,
                graphTypeDirName + GraphLoader.VERSION_PREFIX + (maxVersionNumber + 1));

        // Copy old graph files
        if (maxVersionNumber != 0) {
            final File currentGraphDir = new File(graphTypeDir,
                    graphTypeDirName + GraphLoader.VERSION_PREFIX + maxVersionNumber);
            try {
                FileUtils.copyDirectory(currentGraphDir, newGraphDir);
            } catch (final IOException e) {
                java.lang.System.err.println("Could not copy old graph version.");
            }
        }

        return new Graph(newGraphDir);
    }

    /**
     * Clones and returns a new version from the current newest version of the allocation model
     * graph. If there is none yet an empty graph is returned.
     *
     * @return The allocation model graph
     */
    public Graph cloneNewAllocationModelGraphVersion() {
        return this.cloneNewModelGraphVersion(GraphLoader.ALLOCATION_GRAPH_DIR);
    }

    /**
     * Clones and returns a new version from the current newest version of the repository model
     * graph. If there is none yet an empty graph is returned.
     *
     * @return The repository model graph
     */
    public Graph cloneNewRepositoryModelGraphVersion() {
        return this.cloneNewModelGraphVersion(GraphLoader.REPOSITORY_GRAPH_DIR);
    }

    /**
     * Clones and returns a new version from the current newest version of the resourceEnvironment
     * model graph. If there is none yet an empty graph is returned.
     *
     * @return The resourceEnvironment model graph
     */
    public Graph cloneNewResourceEnvironmentModelGraphVersion() {
        return this.cloneNewModelGraphVersion(GraphLoader.RESOURCEENVIRONMENT_GRAPH_DIR);
    }

    /**
     * Clones and returns a new version from the current newest version of the system model graph.
     * If there is none yet an empty graph is returned.
     *
     * @return The system model graph
     */
    public Graph cloneNewSystemModelGraphVersion() {
        return this.cloneNewModelGraphVersion(GraphLoader.SYSTEM_GRAPH_DIR);
    }

    /**
     * Clones and returns a new version from the current newest version of the usage model graph. If
     * there is none yet an empty graph is returned.
     *
     * @return The usage model graph
     */
    public Graph cloneNewUsageModelGraphVersion() {
        return this.cloneNewModelGraphVersion(GraphLoader.USAGE_GRAPH_DIR);
    }

    /**
     * Helper method for getting graphs: Returns the newest version of the model graph.
     *
     * @param graphTypeDirName
     *            Name of root directory for a certain graph type
     * @return The model graph
     */
    private Graph getModelGraphVersion(final String graphTypeDirName) {
        final File graphTypeDir = new File(this.neo4jPcmModelDirectory, graphTypeDirName);
        int maxVersionNumber = GraphLoaderUtil.getMaxVersionNumber(graphTypeDir.listFiles());

        if (maxVersionNumber == 0) {
            maxVersionNumber = 1; // no version at all so far
        }

        return new Graph(new File(graphTypeDir, graphTypeDirName + GraphLoader.VERSION_PREFIX + maxVersionNumber));
    }

    /**
     * Returns the newest version of the allocation model graph. If there is none yet an empty graph
     * is returned.
     *
     * @return The allocation model graph
     */
    public Graph getAllocationModelGraph() {
        return this.getModelGraphVersion(GraphLoader.ALLOCATION_GRAPH_DIR);
    }

    /**
     * Returns the newest version of the repository model graph. If there is none yet an empty graph
     * is returned.
     *
     * @return The repository model graph
     */
    public Graph getRepositoryModelGraph() {
        return this.getModelGraphVersion(GraphLoader.REPOSITORY_GRAPH_DIR);
    }

    /**
     * Returns the newest version of the resourceEnvironment model graph. If there is none yet an
     * empty graph is returned.
     *
     * @return The resourceEnvironment model graph
     */
    public Graph getResourceEnvironmentModelGraph() {
        return this.getModelGraphVersion(GraphLoader.RESOURCEENVIRONMENT_GRAPH_DIR);
    }

    /**
     * Returns the newest version of the system model graph. If there is none yet an empty graph is
     * returned.
     *
     * @return The system model graph
     */
    public Graph getSystemModelGraph() {
        return this.getModelGraphVersion(GraphLoader.SYSTEM_GRAPH_DIR);
    }

    /**
     * Returns the newest version of the usage model graph. If there is none yet an empty graph is
     * returned.
     *
     * @return The usage model graph
     */
    public Graph getUsageModelGraph() {
        return this.getModelGraphVersion(GraphLoader.USAGE_GRAPH_DIR);
    }

    /**
     * Initializes the newest version of the allocation model graph with the given model. Overwrites
     * a possibly existing graph in the database directory of this loader.
     *
     * @param allocationModel
     *            The allocation model
     * @return The allocation model graph
     */
    public Graph initializeAllocationModelGraph(final Allocation allocationModel) {
        final Graph graph = this.getAllocationModelGraph();
        final ModelProvider<Allocation> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(allocationModel);
        graph.getGraphDatabaseService().shutdown();

        return graph;
    }

    /**
     * Initializes the newest version of the repository model graph with the given model. Overwrites
     * a possibly existing graph in the database directory of this loader.
     *
     * @param repositoryModel
     *            The repository model
     * @return The repository model graph
     */
    public Graph initializeRepositoryModelGraph(final Repository repositoryModel) {
        final Graph graph = this.getRepositoryModelGraph();
        final ModelProvider<Repository> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(repositoryModel);
        graph.getGraphDatabaseService().shutdown();

        return graph;
    }

    /**
     * Initializes the newest version of the resource environment model graph with the given model.
     * Overwrites a possibly existing graph in the database directory of this loader.
     *
     * @param resourceEnvironmentModel
     *            The resource environment model
     * @return The resource environment model graph
     */
    public Graph initializeResourceEnvironmentModelGraph(final ResourceEnvironment resourceEnvironmentModel) {
        final Graph graph = this.getResourceEnvironmentModelGraph();
        final ModelProvider<ResourceEnvironment> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(resourceEnvironmentModel);
        graph.getGraphDatabaseService().shutdown();

        return graph;
    }

    /**
     * Initializes the newest version of the system model graph with the given model. Overwrites a
     * possibly existing graph in the database directory of this loader.
     *
     * @param systemModel
     *            The system model
     * @return The system model graph
     */
    public Graph initializeSystemModelGraph(final System systemModel) {
        final Graph graph = this.getSystemModelGraph();
        final ModelProvider<System> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(systemModel);
        graph.getGraphDatabaseService().shutdown();

        return graph;
    }

    /**
     * Initializes the newest version of the usage model graph with the given model. Overwrites a
     * possibly existing graph in the database directory of this loader.
     *
     * @param usageModel
     *            The usage model
     * @return The usage model graph
     */
    public Graph initializeUsageModelGraph(final UsageModel usageModel) {
        final Graph graph = this.getUsageModelGraph();
        final ModelProvider<UsageModel> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(usageModel);
        graph.getGraphDatabaseService().shutdown();

        return graph;
    }

}
