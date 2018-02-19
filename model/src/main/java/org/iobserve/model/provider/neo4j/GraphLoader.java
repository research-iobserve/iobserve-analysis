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
import java.io.IOException;

import org.codehaus.plexus.util.FileUtils;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to load a {@link Graph} including each of the different PCM models.
 *
 * @author Lars Bluemke
 *
 */
public class GraphLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphLoader.class);

    protected static final String VERSION_PREFIX = "_v";

    private static final String ALLOCATION_GRAPH_DIR = "allocationmodel";
    private static final String REPOSITORY_GRAPH_DIR = "repositorymodel";
    private static final String RESOURCEENVIRONMENT_GRAPH_DIR = "resourceenvironmentmodel";
    private static final String SYSTEM_GRAPH_DIR = "systemmodel";
    private static final String USAGE_GRAPH_DIR = "usagemodel";

    private final File baseDirectory;

    /**
     * Creates a graph loader for for Neo4j databases in the specified directory.
     *
     * @param baseDirectory
     *            The base directory. Subfolders for the different model types are created in this
     *            directory. The different model versions are stored in each of these subfolders.
     */
    public GraphLoader(final File baseDirectory) {
        this.baseDirectory = baseDirectory;
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
        final File graphTypeDir = new File(this.baseDirectory, graphTypeDirName);
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
                GraphLoader.LOGGER.error("Could not copy old graph version.");
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
    private Graph createModelGraphVersion(final String graphTypeDirName) {
        final File graphTypeDir = new File(this.baseDirectory, graphTypeDirName);
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
    public Graph createAllocationModelGraph() {
        return this.createModelGraphVersion(GraphLoader.ALLOCATION_GRAPH_DIR);
    }

    /**
     * Returns the newest version of the repository model graph. If there is none yet an empty graph
     * is returned.
     *
     * @return The repository model graph
     */
    public Graph createRepositoryModelGraph() {
        return this.createModelGraphVersion(GraphLoader.REPOSITORY_GRAPH_DIR);
    }

    /**
     * Returns the newest version of the resourceEnvironment model graph. If there is none yet an
     * empty graph is returned.
     *
     * @return The resourceEnvironment model graph
     */
    public Graph createResourceEnvironmentModelGraph() {
        return this.createModelGraphVersion(GraphLoader.RESOURCEENVIRONMENT_GRAPH_DIR);
    }

    /**
     * Returns the newest version of the system model graph. If there is none yet an empty graph is
     * returned.
     *
     * @return The system model graph
     */
    public Graph createSystemModelGraph() {
        return this.createModelGraphVersion(GraphLoader.SYSTEM_GRAPH_DIR);
    }

    /**
     * Returns the newest version of the usage model graph. If there is none yet an empty graph is
     * returned.
     *
     * @return The usage model graph
     */
    public Graph createUsageModelGraph() {
        return this.createModelGraphVersion(GraphLoader.USAGE_GRAPH_DIR);
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
        final Graph graph = this.createAllocationModelGraph();
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
        final Graph graph = this.createRepositoryModelGraph();
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
        final Graph graph = this.createResourceEnvironmentModelGraph();
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
        final Graph graph = this.createSystemModelGraph();
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
        final Graph graph = this.createUsageModelGraph();
        final ModelProvider<UsageModel> provider = new ModelProvider<>(graph);
        provider.clearGraph();
        provider.createComponent(usageModel);
        graph.getGraphDatabaseService().shutdown();

        return graph;
    }

}
