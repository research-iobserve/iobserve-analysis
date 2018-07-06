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
package org.iobserve.model.persistence.neo4j;

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
 *         TODO: Locking is broken, as the locking should be based on the resource and not on the
 *         provider
 */
public final class ModelProviderSynchronizer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelProviderSynchronizer.class);

    private static ConcurrentHashMap<ModelResource, Object> locks = new ConcurrentHashMap<>();

    private ModelProviderSynchronizer() {
        // private constructor, utility class
    }

    public static void getLock(final ModelResource resource) {
        synchronized (resource) {
            while (ModelProviderSynchronizer.locks.get(resource) != null) {
                try {
                    resource.wait();
                } catch (final InterruptedException e) {
                    if (ModelProviderSynchronizer.LOGGER.isErrorEnabled()) {
                        ModelProviderSynchronizer.LOGGER
                                .error("Thread was interrupted before or while waiting for a notification to "
                                        + "get the database lock.");
                    }
                }
            }
            ModelProviderSynchronizer.locks.put(resource, resource);
        }
    }

    public static void releaseLock(final ModelResource resource) {
        synchronized (resource) {
            if (ModelProviderSynchronizer.locks.get(resource) == resource) {
                ModelProviderSynchronizer.locks.remove(resource);
                resource.notifyAll();
            } else {
                ModelProviderSynchronizer.LOGGER.warn("Cannot release the lock - you are not holding it.");
            }
        }
    }
}
