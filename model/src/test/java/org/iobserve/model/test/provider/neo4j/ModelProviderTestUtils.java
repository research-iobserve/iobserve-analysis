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

import org.eclipse.emf.ecore.EPackage;
import org.iobserve.model.persistence.neo4j.ModelProviderUtil;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

/**
 * Functions to support test construction.
 *
 * @author Reiner Jung
 *
 */
public final class ModelProviderTestUtils {

    /** utility class. */
    private ModelProviderTestUtils() {
        // nothing to do here
    }

    /**
     * Prepare resource for a given factory.
     *
     * @param name
     *            test directory name
     * @param prefix
     *            path prefix
     * @param factory
     *            model factory
     *
     * @return the prepared graph
     */
    public static ModelResource prepareResource(final String name, final String prefix, final EPackage ePackage) {
        final File graphBaseDir = new File("./testdb/" + prefix + "." + ePackage.eClass().getName() + "." + name);

        ModelProviderTestUtils.removeDirectory(graphBaseDir);

        return ModelProviderUtil.createModelResource(ePackage, graphBaseDir);
    }

    /**
     * Checks whether the graph of a given {@link ModelProvider} is empty.
     *
     * @param resource
     *            A model resource, containing a model
     * @return True if the graph is empty, false otherwise
     */
    public static boolean isResourceEmpty(final ModelResource resource) {
        boolean isEmpty = false;

        try (Transaction tx = resource.getGraphDatabaseService().beginTx()) {
            final ResourceIterator<Node> iterator = resource.getGraphDatabaseService().getAllNodes().iterator();

            isEmpty = !iterator.hasNext();

            tx.success();
        }

        return isEmpty;
    }

    /**
     * Delete directory tree.
     *
     * @param dir
     *            directory
     */
    private static void removeDirectory(final File dir) {
        if (dir.isDirectory()) {
            for (final File file : dir.listFiles()) {
                ModelProviderTestUtils.removeDirectory(file);
            }
            dir.delete();
        } else {
            dir.delete();
        }
    }

    public static String removePrefix(final String canonicalName) {
        return canonicalName.substring("org.palladiosimulator.".length());
    }
}
