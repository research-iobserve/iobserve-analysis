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
package org.iobserve.analysis.behavior.clustering.similaritymatching;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;

/**
 * Interface for a strategy to generate a representative model from many models.
 *
 * @author Jannis Kuckei
 *
 */
public interface IModelGenerationStrategy {

    /**
     * Generate one model based on a set of input models.
     *
     * @param models
     *            set of models
     * @return the generated model
     */
    BehaviorModel generateModel(final BehaviorModel[] models);
}
