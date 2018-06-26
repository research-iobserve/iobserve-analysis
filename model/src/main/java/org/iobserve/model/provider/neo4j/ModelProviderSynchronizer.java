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

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Can be used to synchronize access to a graph database to prevent scenarios where two threads read
 * a model from the same graph database, edit the model independently and then both write back to
 * the database ignoring the possible changes made by the other thread.
 *
 * @author Lars Bluemke
 *
 */
public final class ModelProviderSynchronizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelProviderSynchronizer.class);

    private static ConcurrentHashMap<ModelGraph, ModelProvider<?>> locks = new ConcurrentHashMap<>();

    private ModelProviderSynchronizer() {
        // private constructor, utility class
    }

    /**
     * Gets the lock for the graph database of the calling {@link ModelProvider}. Blocks if the lock
     * is currently hold by another provider.
     *
     * @param modelProvider
     *            The model provider trying for the lock
     */
    public static void getLock(final ModelProvider<?> modelProvider) {
        final ModelGraph graph = modelProvider.getGraph();

        synchronized (graph) {
            while (ModelProviderSynchronizer.locks.get(graph) != null
                    && ModelProviderSynchronizer.locks.get(graph) != modelProvider) {
                try {
                    graph.wait();
                } catch (final InterruptedException e) {
                    if (ModelProviderSynchronizer.LOGGER.isErrorEnabled()) {
                        ModelProviderSynchronizer.LOGGER
                                .error("Thread was interrupted before or while waiting for a notification to "
                                        + "get the database lock.");
                    }
                }
            }
            ModelProviderSynchronizer.locks.put(graph, modelProvider);
        }
    }

    /**
     * Releases the lock for the graph database of the calling {@link ModelProvider}.
     *
     * @param modelProvider
     *            The model provider trying to release the lock
     */
    public static void releaseLock(final ModelProvider<?> modelProvider) {
        final ModelGraph graph = modelProvider.getGraph();

        synchronized (graph) {
            if (ModelProviderSynchronizer.locks.get(graph) == modelProvider) {
                ModelProviderSynchronizer.locks.remove(graph);
                graph.notify();
            } else {
                ModelProviderSynchronizer.LOGGER.warn("Cannot release the lock - you are not holding it.");
            }
        }
    }
}
