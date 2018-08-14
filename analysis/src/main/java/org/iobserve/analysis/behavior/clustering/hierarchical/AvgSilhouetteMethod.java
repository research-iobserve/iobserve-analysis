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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.net4j.util.collection.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.clusterers.HierarchicalClusterer;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author SL
 *
 */
public class AvgSilhouetteMethod implements IClusterSelectionMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusterer.class);

    @Override
    public Map<Integer, List<Pair<Instance, Double>>> analyze(final HierarchicalClusterer hierarchicalClusterer,
            final Instances instances) {

        AvgSilhouetteMethod.LOGGER.info("Starting AvgSilhouetteMethod");

        final int numInstances = instances.numInstances();
        // Average silhouette for each cluster.
        final List<Double> avgSilhouettes = new ArrayList<>();
        for (int i = 1; i <= instances.numInstances(); ++i) {

            try {
                final int numberOfClusters = i;
                hierarchicalClusterer.setNumClusters(numberOfClusters);
                hierarchicalClusterer.buildClusterer(instances);

                // Assignments of each data point the clusters.
                final Vector<Integer>[] assignments = new Vector[numberOfClusters];
                // Initialize assignments with empty vectors.
                for (int j = 0; j < numberOfClusters; j++) {
                    assignments[j] = new Vector<>();
                }
                // Cluster size of each cluster (not needed)
                final int[] clustersize = new int[numberOfClusters];

                for (int s = 0; s < numInstances; s++) {
                    final int assignedCluster = hierarchicalClusterer.clusterInstance(instances.instance(s));
                    assignments[assignedCluster].add(s);
                    clustersize[hierarchicalClusterer.clusterInstance(instances.instance(s))]++;
                }

                String assignmentString = "";
                for (final Vector<Integer> v : assignments) {
                    assignmentString += v.toString();
                }
                AvgSilhouetteMethod.LOGGER.info("Assignments: " + assignmentString + "\n");

                /*
                 * Calculate the average dissimilarity of all data points in a cluster for all
                 * clusters.
                 */

                avgSilhouettes.add(this.calcAvgSilhouettes(assignments, instances, hierarchicalClusterer));

                AvgSilhouetteMethod.LOGGER.info(avgSilhouettes.toString() + "\n");

            } catch (final Exception e) {
                AvgSilhouetteMethod.LOGGER.error("Hierarchical clustering failed.", e);
            }
        }

        final int goodClustereNumber = this.findMaxAvgSilhouetteIndex(avgSilhouettes);

        Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>(); // NOPMD

        try {
            hierarchicalClusterer.setNumClusters(goodClustereNumber);
            hierarchicalClusterer.buildClusterer(instances);
            clusteringResults = ClusteringResultsBuilder.buildClusteringResults(instances, hierarchicalClusterer);
        } catch (final Exception e) {
            AvgSilhouetteMethod.LOGGER.error("Clustering at AvgSilhouette failed.", e);
        }

        // Print clusteringResult
        for (int i = 0; i < clusteringResults.size(); i++) {
            AvgSilhouetteMethod.LOGGER.info(clusteringResults.get(i).toString() + "\n");
        }

        AvgSilhouetteMethod.LOGGER.info("AvgSilhouetteMethod done.");
        return clusteringResults;
    }

    /**
     * Calculate the average silhouettes of each cluster.
     *
     * @param clusters
     *            Resulting clusters from hierarchical clustering.
     * @param instances
     *            Input data
     * @param hierarchicalClusterer
     *            Clusterer that performs the hierarchical clustering.
     * @return average silhouettes of the clusters.
     */
    public double calcAvgSilhouettes(final Vector<Integer>[] clusters, final Instances instances,
            final HierarchicalClusterer hierarchicalClusterer) {

        final DistanceFunction distanceFunction = hierarchicalClusterer.getDistanceFunction();
        double avgSilhouette = 0.0;
        final List<Double> avgSilhouetteList = new ArrayList<>();

        if (clusters.length == 1) {
            // Default silhouette value is 0 there is only one cluster.
            return avgSilhouette;
        } else {
            final List<Double> silhouetteList = new ArrayList<>();
            // final double[] silhouetteList = new double[cluster.size()];
            for (final Vector<Integer> cluster : clusters) {
                for (int i = 0; i < cluster.size(); i++) {
                    /*
                     * Calculate the average dissimilarity of instance i to all other instances of
                     * the same cluster.
                     */
                    final Instance instanceI = instances.instance(cluster.elementAt(i));
                    double sumDistanceFromI = 0.0;
                    for (int j = 0; j < cluster.size(); j++) {
                        if (j != i) {
                            final Instance otherInstance = instances.instance(cluster.elementAt(j));
                            sumDistanceFromI += distanceFunction.distance(instanceI, otherInstance);
                        }
                    }
                    final double avgDissimFromISameCluster = sumDistanceFromI / (cluster.size() - 1);

                    /*
                     * For each other cluster that instance i is not part of, calculate the average
                     * dissimilarity of instance i to all instances of these other clusters.
                     */
                    double minAvgDissimFromIToOtherClusters = Double.POSITIVE_INFINITY;
                    for (final Vector<Integer> otherCluster : clusters) {
                        if (!otherCluster.equals(cluster)) {
                            double sumDistanceFromIOtherClusters = 0.0;
                            for (int j = 0; j < otherCluster.size(); j++) {
                                final Instance otherInstance = instances.instance(otherCluster.elementAt(j));
                                sumDistanceFromIOtherClusters += distanceFunction.distance(instanceI, otherInstance);
                            }
                            final double avgDissimFromIOtherCluster = sumDistanceFromIOtherClusters
                                    / otherCluster.size();
                            minAvgDissimFromIToOtherClusters = Math.min(minAvgDissimFromIToOtherClusters,
                                    avgDissimFromIOtherCluster);
                        }
                    }

                    /*
                     * Calculate the silhouette value of instance i. Instances with a large
                     * silhouette (almost 1) are very well clustered.
                     */
                    double silhouetteI = 0.0;
                    silhouetteI = (minAvgDissimFromIToOtherClusters - avgDissimFromISameCluster)
                            / Math.max(avgDissimFromISameCluster, minAvgDissimFromIToOtherClusters);

                    // Add silhouette of i to silhouette list in order to calculate the average.
                    silhouetteList.add(silhouetteI);
                }

                // // Calculate the average silhouette of the cluster.
                // double avgSilhouetteOfCluster = 0.0;
                // for (final double silhouette : silhouetteList) {
                // avgSilhouetteOfCluster += silhouette;
                // }
                // avgSilhouetteOfCluster /= cluster.size();
                // avgSilhouetteList.add(avgSilhouetteOfCluster);
            }

            avgSilhouette = this.calculateAverage(silhouetteList);
        }

        return avgSilhouette;
    }

    /**
     * Find the maximum value of the average silhouettes.
     *
     * @param avgSilhouettes
     *            List of all average silhouettes of every cluster.
     * @return Maximum of the average silhouettes.
     */
    private int findMaxAvgSilhouetteIndex(List<Double> avgSilhouettes) {
        int maxIndex = 0;
        double maxValue = 0;
        for (int i = 0; i < avgSilhouettes.size(); i++) {
            if (avgSilhouettes.get(i) != null) {
                final double oldMaxValue = maxValue;
                maxValue = Math.max(maxValue, avgSilhouettes.get(i));
                if (maxValue > oldMaxValue) {
                    maxIndex = i;
                }
            }
        }
        return maxIndex;
    }

    // Calculates the average value of a List<Double>.
    private double calculateAverage(List<Double> list) {
        double sum = 0;
        if (!list.isEmpty()) {
            for (final double entry : list) {
                sum += entry;
            }
            return sum / list.size();
        }
        return sum;
    }
}
