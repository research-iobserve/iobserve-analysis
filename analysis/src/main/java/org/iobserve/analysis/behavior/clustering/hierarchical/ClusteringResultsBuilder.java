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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.net4j.util.collection.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.clusterers.HierarchicalClusterer;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author SL
 *
 *         Utility class for building clustering results to write into a file.
 */
public final class ClusteringResultsBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusterer.class);

    private ClusteringResultsBuilder() {
        throw new UnsupportedOperationException();
    }

    /**
     *
     * @param instances
     *            input data that is clustered
     *
     * @param hierarchicalClusterer
     *            weka-clusterer that performs the hierarchical clustering
     *
     * @return clusteringResults
     * @throws Exception
     */
    public static Map<Integer, List<Pair<Instance, Double>>> buildClusteringResults(final Instances instances,
            final HierarchicalClusterer hierarchicalClusterer) {
        /*
         * An entry of the map looks like this: <clusterID, <instance, 1.0>>. The 1,0 is the
         * probability that the instance is in this cluster. It is needed for unity of clustering
         * output data.
         */
        final Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>(); // NOPMD;

        for (int i = 0; i < instances.numInstances(); i++) {
            final Instance currentInstance = instances.instance(i);
            try {
                final int cluster = hierarchicalClusterer.clusterInstance(currentInstance);
                final double probability = hierarchicalClusterer.distributionForInstance(currentInstance)[cluster];
                if (clusteringResults.get(cluster) == null) {
                    clusteringResults.put(cluster, new LinkedList<Pair<Instance, Double>>());
                }
                clusteringResults.get(cluster).add(new Pair<>(currentInstance, probability));
            } catch (final Exception e) { // NOPMD NOCS api dependency
                ClusteringResultsBuilder.LOGGER.error("Failed building hierarchical clustering.", e);
            }

        }

        return clusteringResults;
    }
}
