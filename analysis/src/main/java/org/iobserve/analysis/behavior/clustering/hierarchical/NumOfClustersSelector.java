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

import weka.clusterers.HierarchicalClusterer;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author SL
 *
 */
public class NumOfClustersSelector {

    // Different available methods to find a "good" number of clusters.
    private static final String ELBOW = "elbow";
    private static final String AVG_SILHOUETTE = "avgsil";
    private static final String GAP_STATISTIC = "gap";

    private final String strategy;
    private final HierarchicalClusterer hierarchicalClusterer;
    private final Instances instances;

    /**
     *
     * @param strategy
     *            strategy which will be used to find the optimal number of clusters for the
     *            initialCluster
     *
     * @param hierarchicalClusterer
     *            weka-clusterer that performs the hierarchical clustering
     *
     * @param instances
     *            input data that is clustered
     */
    public NumOfClustersSelector(final String strategy, final HierarchicalClusterer hierarchicalClusterer,
            final Instances instances) {
        this.strategy = strategy;
        this.hierarchicalClusterer = hierarchicalClusterer;
        this.instances = instances;
    }

    /**
     * Finds a "good" number of clusters for clustering the input data and clusters the input data.
     *
     * @return clustering results with a "good" number of clusters.
     */
    public Map<Integer, List<Pair<Instance, Double>>> findGoodClustering() {
        Map<Integer, List<Pair<Instance, Double>>> resultClusters = null;
        final IClusterSelectionMethods clusteringMethod;

        switch (this.strategy) {
        case ELBOW:
            clusteringMethod = new ElbowMethod(this.hierarchicalClusterer, this.instances);
            break;
        case AVG_SILHOUETTE:
            clusteringMethod = new AvgSilhouetteMethod(this.hierarchicalClusterer, this.instances);
            break;
        case GAP_STATISTIC:
            clusteringMethod = new GapStatisticMethod(this.hierarchicalClusterer, this.instances);
            break;
        default: // default is ElbowMethod
            clusteringMethod = new ElbowMethod(this.hierarchicalClusterer, this.instances);
            break;
        }
        resultClusters = clusteringMethod.analyze();

        return resultClusters;
    }
}
