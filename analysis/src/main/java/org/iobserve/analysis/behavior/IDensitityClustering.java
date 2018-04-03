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
package org.iobserve.analysis.behavior;

import java.util.List;
import java.util.Map;

import org.eclipse.net4j.util.collection.Pair;

import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Marc Adolf
 *
 */
public interface IDensitityClustering extends IClustering {

    /**
     * Computes the clusters and the probabilities of the instances to belong to them. Returns a Map
     * with the clusters, the belonging instances and their probability.
     *
     * @param instances
     *            The instances to be clustered.
     * @return A Map with the clusters as key and lists of instances and their probability belonging
     *         to each cluster. Every instance is only assigned to one cluster.
     */
    @Override
    Map<Integer, List<Pair<Instance, Double>>> clusterInstances(Instances instances);

}
