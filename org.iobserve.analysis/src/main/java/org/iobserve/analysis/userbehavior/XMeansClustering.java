/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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

package org.iobserve.analysis.userbehavior;

import org.iobserve.analysis.userbehavior.data.ClusteringMetrics;
import org.iobserve.analysis.userbehavior.data.ClusteringResults;

import weka.clusterers.XMeans;
import weka.core.Instances;
import weka.core.ManhattanDistance;
import weka.core.NormalizableDistance;

/**
 * This class performs the clustering of the user sessions that have been transformed to counts of
 * calls before.
 *
 * @author David Peter, Robert Heinrich
 */
public class XMeansClustering extends AbstractClustering {

    /**
     *
     * @param instances
     *            data to cluster in Weka format
     * @param numberOfUserGroupsFromInputUsageModel
     *            is the input number of clusters
     * @param varianceOfUserGroups
     *            enables the creation of a minimum and maximum number of clusters
     * @param seed
     *            states a random determination of the initial centroids
     * @return the clustering results that contain the number of cluster and the assignments
     */
    public ClusteringResults clusterSessionsWithXMeans(final Instances instances,
            final int numberOfUserGroupsFromInputUsageModel, final int varianceOfUserGroups, final int seed) {

        ClusteringResults xMeansClusteringResults = null;

        try {

            final XMeans xmeans = new XMeans();
            xmeans.setSeed(seed);

            final NormalizableDistance manhattenDistance = new ManhattanDistance();
            manhattenDistance.setDontNormalize(false);
            manhattenDistance.setInstances(instances);
            xmeans.setDistanceF(manhattenDistance);

            int[] clustersize = null;
            final int[] assignments = new int[instances.numInstances()];

            // Determines the range of clusters
            // The X-Means clustering algorithm determines the best fitting number of clusters
            // within this range by itself
            int numberOfClustersMin = numberOfUserGroupsFromInputUsageModel - varianceOfUserGroups;
            int numberOfClustersMax = numberOfUserGroupsFromInputUsageModel + varianceOfUserGroups;
            if (numberOfClustersMax < 2) {
                numberOfClustersMax = 1;
                numberOfClustersMin = 1;
            } else {
                if (numberOfClustersMin < 2) {
                    numberOfClustersMin = 2;
                }
            }

            xmeans.setMinNumClusters(numberOfClustersMin);
            xmeans.setMaxNumClusters(numberOfClustersMax);
            xmeans.buildClusterer(instances);

            clustersize = new int[xmeans.getClusterCenters().numInstances()];
            for (int s = 0; s < instances.numInstances(); s++) {
                assignments[s] = xmeans.clusterInstance(instances.instance(s));
                clustersize[xmeans.clusterInstance(instances.instance(s))]++;
            }

            final ClusteringMetrics clusteringMetrics = new ClusteringMetrics(xmeans.getClusterCenters(), instances,
                    assignments);
            clusteringMetrics.calculateSimilarityMetrics();

            xMeansClusteringResults = new ClusteringResults("X-Means", xmeans.getClusterCenters().numInstances(),
                    assignments, clusteringMetrics);

        } catch (final Exception e) {
            e.printStackTrace();
        }

        return xMeansClusteringResults;
    }
}
