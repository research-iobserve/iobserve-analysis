/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.stages.data.trace;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author teetime
 * @author Reiner Jung -- modified interfaces
 *
 * @param <K>
 *            key type
 * @param <V>
 *            value type
 *
 */
public final class ConcurrentHashMapWithCreate<K, V> extends ConcurrentHashMap<K, V> {

    private static final long serialVersionUID = 199185976241037967L;

    private final IValueFactory<V> valueFactory;

    private int maxElements;

    /**
     * Create new hash map.
     *
     * @param valueFactory
     *            factory for values to be created
     */
    public ConcurrentHashMapWithCreate(final IValueFactory<V> valueFactory) {
        this.valueFactory = valueFactory;
    }

    public V getOrCreate(final K key) {
        V value = this.get(key);
        if (value == null) {
            synchronized (this) {
                value = this.get(key);
                if (value == null) { // NOCS (DCL)
                    value = this.valueFactory.create();
                    this.put(key, value);
                    this.maxElements++;
                }
            }
        }
        return value;
    }

    public int getMaxElements() {
        return this.maxElements;
    }
}
