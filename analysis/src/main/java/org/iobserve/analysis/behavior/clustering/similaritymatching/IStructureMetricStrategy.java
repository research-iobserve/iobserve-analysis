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
 * Represents a distance function between two behavior models based on their general structure.
 *
 * @author Jannis Kuckei
 *
 */
public interface IStructureMetricStrategy {

    /**
     * Compute the distance between two models.
     *
     * @param a
     *            first model
     * @param b
     *            second model
     * @return returns the distance between both models
     */
    double getDistance(BehaviorModel a, BehaviorModel b);
}