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
import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.privacy.PrivacyModel;
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
    protected static final String VERSION_PREFIX = "_v";

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphLoader.class);

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
     * @param clazz
     *            Type for the cloned model
     * @param <T>
     *            graph type
     * @return The the model graph
     */
    public <T> Graph<T> cloneNewModelGraphVersion(final Class<T> clazz) {
        final String graphTypeDirName = clazz.getCanonicalName();
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

        return new Graph<>(newGraphDir);
    }

    /**
     * Clones and returns a new version from the current newest version of the allocation model
     * graph. If there is none yet an empty graph is returned.
     *
     * @return The allocation model graph
     */
    public Graph<Allocation> cloneNewAllocationModelGraphVersion() {
        return this.cloneNewModelGraphVersion(Allocation.class);
    }

    /**
     * Clones and returns a new version from the current newest version of the repository model
     * graph. If there is none yet an empty graph is returned.
     *
     * @return The repository model graph
     */
    public Graph<Repository> cloneNewRepositoryModelGraphVersion() {
        return this.cloneNewModelGraphVersion(Repository.class);
    }

    /**
     * Clones and returns a new version from the current newest version of the resourceEnvironment
     * model graph. If there is none yet an empty graph is returned.
     *
     * @return The resourceEnvironment model graph
     */
    public Graph<ResourceEnvironment> cloneNewResourceEnvironmentModelGraphVersion() {
        return this.cloneNewModelGraphVersion(ResourceEnvironment.class);
    }

    /**
     * Clones and returns a new version from the current newest version of the system model graph.
     * If there is none yet an empty graph is returned.
     *
     * @return The system model graph
     */
    public Graph<System> cloneNewSystemModelGraphVersion() {
        return this.cloneNewModelGraphVersion(System.class);
    }

    /**
     * Clones and returns a new version from the current newest version of the usage model graph. If
     * there is none yet an empty graph is returned.
     *
     * @return The usage model graph
     */
    public Graph<UsageModel> cloneNewUsageModelGraphVersion() {
        return this.cloneNewModelGraphVersion(UsageModel.class);
    }

    /**
     * Clones and returns a new version from the current newest version of the privacy model graph.
     * If there is none yet an empty graph is returned.
     *
     * @return The privacy model graph
     */
    public Graph<PrivacyModel> cloneNewPrivacyModelGraphVersion() {
        return this.cloneNewModelGraphVersion(PrivacyModel.class);
    }

    /**
     * Clones and returns a new version from the current newest version of the correspondence model
     * graph. If there is none yet an empty graph is returned.
     *
     * @return The privacy model graph
     */
    public Graph<CorrespondenceModel> cloneNewEorrespondenceModelGraphVersion() {
        return this.cloneNewModelGraphVersion(CorrespondenceModel.class);
    }

    /**
     * Helper method for getting graphs: Returns the newest version of the model graph.
     *
     * @param clazz
     *            class type used to define the name of root directory for a certain graph type
     * @param <T>
     *            model type
     * @return The model graph
     */
    public <T extends EObject> Graph<T> createModelGraph(final Class<T> clazz) {
        final String graphTypeDirName = clazz.getCanonicalName();
        final File graphTypeDir = new File(this.baseDirectory, graphTypeDirName);
        int maxVersionNumber = GraphLoaderUtil.getMaxVersionNumber(graphTypeDir.listFiles());

        if (maxVersionNumber == 0) {
            maxVersionNumber = 1; // no version at all so far
        }

        return new Graph<>(new File(graphTypeDir, graphTypeDirName + GraphLoader.VERSION_PREFIX + maxVersionNumber));
    }

    /**
     * Initializes the newest version of a model graph with the given model. Overwrites a potential
     * existing graph in the database directory of this loader.
     *
     * @param clazz
     *            class type of the model
     * @param model
     *            the model to use for initialization
     * @param nameLabel
     *            label for the name attribute in the DB and model
     * @param idLabel
     *            label for the id attribute in the DB and model
     * @param <V>
     *            the type of the root element
     */
    public <V extends EObject> void initializeModelGraph(final Class<V> clazz, final V model, final String nameLabel,
            final String idLabel) {
        final Graph<V> graph = this.createModelGraph(clazz);
        final ModelProvider<V, V> provider = new ModelProvider<>(graph, nameLabel, idLabel);
        provider.clearGraph();
        provider.createComponent(model);
        graph.getGraphDatabaseService().shutdown();
    }

}
