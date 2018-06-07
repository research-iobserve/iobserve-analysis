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
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
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
     * @param factory
     *            the factory for the particular metamodel (partition)
     * @param <T>
     *            graph type
     * @return The the model graph
     */
    public <T extends EObject> Graph cloneNewModelGraphVersion(final EFactory factory) {
        final String graphTypeDirName = this.fullyQualifiedPackageName(factory.getEPackage());
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

        return new Graph(factory, newGraphDir);
    }

    /**
     * Helper method for getting graphs: Returns the newest version of the model graph.
     *
     * @param factory
     *            the factory for the particular metamodel (partition)
     * @return The model graph
     */
    public Graph createModelGraph(final EFactory factory) {
        final String graphTypeDirName = this.fullyQualifiedPackageName(factory.getEPackage());
        final File graphTypeDir = new File(this.baseDirectory, graphTypeDirName);
        int maxVersionNumber = GraphLoaderUtil.getMaxVersionNumber(graphTypeDir.listFiles());

        if (maxVersionNumber == 0) {
            maxVersionNumber = 1; // no version at all so far
        }

        return new Graph(factory,
                new File(graphTypeDir, graphTypeDirName + GraphLoader.VERSION_PREFIX + maxVersionNumber));
    }

    /**
     * Initializes the newest version of a model graph with the given model. Overwrites a potential
     * existing graph in the database directory of this loader.
     *
     * @param factory
     *            the factory for the particular metamodel (partition)
     * @param model
     *            the model to use for initialization
     * @param nameLabel
     *            label for the name attribute in the DB and model
     * @param idLabel
     *            label for the id attribute in the DB and model
     * @param <V>
     *            the type of the root element
     */
    public <V extends EObject> void initializeModelGraph(final EFactory factory, final V model, final String nameLabel,
            final String idLabel) {
        final Graph graph = this.createModelGraph(factory);
        final ModelProvider<V> provider = new ModelProvider<>(graph, nameLabel, idLabel);
        provider.clearGraph();
        provider.createComponent(model);
        graph.getGraphDatabaseService().shutdown();
    }

    private String fullyQualifiedPackageName(final EPackage ePackage) {
        if (ePackage.getESuperPackage() != null) {
            return this.fullyQualifiedPackageName(ePackage.getESuperPackage()) + "." + ePackage.getName();
        } else {
            return ePackage.getName();
        }
    }
}
