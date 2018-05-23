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
package org.iobserve.stages.data.trace;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 *
 * @param <T>
 *            value type.
 */
public interface IValueFactory<T> {

    /**
     * Create a new instance of the type <code>T</code>.
     *
     * @since 1.10
     *
     * @return new instance
     */
    T create();
}
