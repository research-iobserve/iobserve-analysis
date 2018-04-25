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
package org.iobserve.adaptation.stages.transformations;

import java.util.List;

import org.iobserve.adaptation.executionplan.AtomicAction;
import org.iobserve.planning.systemadaptation.ComposedAction;

/**
 * Interface for transformation processors which transform composed to atomic adaptation actions.
 *
 * @author Lars Bluemke
 *
 * @param <T>
 *            Subtypes of {@link ComposedAction}
 *
 */
interface IComposed2AtomicAction<T extends ComposedAction> {

    /**
     * Returns a list of the atomic actions which are necessary to execute a composed action.
     *
     * @param composedAction
     *            The composed action
     * @return The atomic actions
     */
    List<AtomicAction> transform(T composedAction);
}
