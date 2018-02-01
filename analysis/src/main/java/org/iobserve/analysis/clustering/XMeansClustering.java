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
package org.iobserve.analysis.clustering;

import java.util.Optional;
import java.util.Random;

import org.iobserve.analysis.userbehavior.data.ClusteringMetrics;
import org.iobserve.analysis.userbehavior.data.ClusteringResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.clusterers.XMeans;
import weka.core.Instances;
import weka.core.NormalizableDistance;

/**
 * X-means clustering for TClustering.
 *
 * @author Christoph Dornieden
 *
 */
public class XMeansClustering implements IVectorQuantizationClustering {
    private static final Logger LOGGER = LoggerFactory.getLogger(XMeansClustering.class);

    private final int minClusters;
    private final int maxClusters;
    private final NormalizableDistance distanceMetric;

    /**
     * constructor.
     *
     * @param expectedUserGroups
     *            number of expected user groups
     * @param variance
     *            variance of the expected user groups
     * @param distanceMetric
     *            distance Metric for clustering
     */
    public XMeansClustering(final int expectedUserGroups, final int variance,
            final NormalizableDistance distanceMetric) {
        this.minClusters = expectedUserGroups - variance < 2 ? 1 : expectedUserGroups - variance;
        this.maxClusters = expectedUserGroups + variance < 2 ? 2 : expectedUserGroups + variance;
        this.distanceMetric = distanceMetric;
    }

    @Override
    public Optional<ClusteringResults> clusterInstances(final Instances instances) {
        Optional<ClusteringResults> clusteringResults = Optional.empty();

        /** Cluster multiple times to reduce the impact of the initial k of the k-means. */
        for (int i = 0; i < 5; i++) {

            final Optional<ClusteringResults> tempClusteringResults = this.getClusteringResults(instances);
            if (clusteringResults.isPresent() && tempClusteringResults.isPresent()) {
                clusteringResults = tempClusteringResults.get().getClusteringMetrics()
                        .getSumOfSquaredErrors() < clusteringResults.get().getClusteringMetrics()
                                .getSumOfSquaredErrors() ? tempClusteringResults : clusteringResults;
            } else if (tempClusteringResults.isPresent() && !clusteringResults.isPresent()) {
                clusteringResults = tempClusteringResults;
            }
        }
        return clusteringResults;
    }

    private Optional<ClusteringResults> getClusteringResults(final Instances instances) {
        final XMeans xMeansClusterer = new XMeans();

        xMeansClusterer.setSeed(new Random().nextInt(Integer.MAX_VALUE));
        xMeansClusterer.setDistanceF(this.distanceMetric);

        xMeansClusterer.setMinNumClusters(this.minClusters);
        xMeansClusterer.setMaxNumClusters(this.maxClusters);

        try {
            xMeansClusterer.buildClusterer(instances);

            /**
             * Code used from org.iobserve.analysis.userbehavior.XMeansClustering to use
             * org.iobserve.analysis.userbehavior.ClusteringResults
             */
            int[] clustersize = null;
            final int[] assignments = new int[instances.numInstances()];
            clustersize = new int[xMeansClusterer.getClusterCenters().numInstances()];
            for (int s = 0; s < instances.numInstances(); s++) {
                assignments[s] = xMeansClusterer.clusterInstance(instances.instance(s));
                clustersize[xMeansClusterer.clusterInstance(instances.instance(s))]++;
            }

            final ClusteringMetrics clusteringMetrics = new ClusteringMetrics(xMeansClusterer.getClusterCenters(),
                    instances, assignments);
            clusteringMetrics.calculateSimilarityMetrics();

            final ClusteringResults xMeansClusteringResults = new ClusteringResults("X-Means",
                    xMeansClusterer.getClusterCenters().numInstances(), assignments, clusteringMetrics);

            return Optional.of(xMeansClusteringResults);

        } catch (final Exception e) {
            XMeansClustering.LOGGER.error("Clustering failed.", e);
        }

        return Optional.empty();
    }

}
