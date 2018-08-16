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

package org.iobserve.analysis.behavior.clustering.hierarchical;

import java.util.List;
import java.util.Map;

import org.eclipse.net4j.util.collection.Pair;

import weka.core.Instance;

/**
 * @author SL
 *
 *         Interface for ClusterSelectionMethods, that select a "good" number of clusters of a
 *         hierarchical clustering.
 */
public interface IClusterSelectionMethods {

    /**
     *
     * @param hierarchicalClusterer
     *            Performs hierarchical clustering.
     *
     * @param instances
     *            Input data that is clustered.
     *
     * @return clustered data with a "good" number of clusters.
     */
    Map<Integer, List<Pair<Instance, Double>>> analyze();
}
