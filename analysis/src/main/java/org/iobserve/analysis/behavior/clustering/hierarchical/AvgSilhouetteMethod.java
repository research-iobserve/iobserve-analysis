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

import org.eclipse.net4j.util.collection.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.clusterers.HierarchicalClusterer;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author SL
 *
 */
public class AvgSilhouetteMethod implements IClusterSelectionMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusterer.class);
    private final HierarchicalClusterer hierarchicalClusterer;
    private final Instances instances;

    /**
     * Constructor.
     *
     * @param hierarchicalClusterer
     *            Clusterer that performs hierarchical clustering.
     * @param instances
     *            Input data
     */
    public AvgSilhouetteMethod(final HierarchicalClusterer hierarchicalClusterer, final Instances instances) {
        this.hierarchicalClusterer = hierarchicalClusterer;
        this.instances = instances;
    }

    @Override
    public Map<Integer, List<Pair<Instance, Double>>> analyze() {

        AvgSilhouetteMethod.LOGGER.info("Starting AvgSilhouetteMethod");

        // Average silhouette for each cluster.
        final List<Double> avgSilhouettes = new ArrayList<>();
        for (int i = 1; i <= this.instances.numInstances(); ++i) {

            try {
                final int numberOfClusters = i;
                this.hierarchicalClusterer.setNumClusters(numberOfClusters);
                this.hierarchicalClusterer.buildClusterer(this.instances);

                // Assignments of each data point the clusters.
                final List<ArrayList<Integer>> assignments = this.buildClusterAssignmentsList(numberOfClusters);

                // Print assignments for debugging.
                this.printAssignmentString(assignments);

                /*
                 * Calculate the average dissimilarity of all data points in a cluster for all
                 * clusters.
                 */
                avgSilhouettes.add(this.calcAvgSilhouettes(assignments));

                AvgSilhouetteMethod.LOGGER.info(avgSilhouettes.toString() + "\n");

            } catch (final Exception e) { // NOPMD NOCS api dependency
                AvgSilhouetteMethod.LOGGER.error("Hierarchical clustering failed.", e);
            }
        }

        final int goodClustereNumber = this.findMaxAvgSilhouetteIndex(avgSilhouettes);

        Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>(); // NOPMD

        try {
            this.hierarchicalClusterer.setNumClusters(goodClustereNumber);
            this.hierarchicalClusterer.buildClusterer(this.instances);
            clusteringResults = ClusteringResultsBuilder.buildClusteringResults(this.instances,
                    this.hierarchicalClusterer);
        } catch (final Exception e) { // NOPMD NOCS api dependency
            AvgSilhouetteMethod.LOGGER.error("Clustering at AvgSilhouette failed.", e);
        }

        // Print clusteringResult
        this.printClusteringResults(clusteringResults);

        AvgSilhouetteMethod.LOGGER.info("AvgSilhouetteMethod done.");
        return clusteringResults;
    }

    /**
     * Calculate the average silhouettes of each cluster.
     *
     * @param assignments
     *            Resulting clusters from hierarchical clustering.
     * @return average silhouettes of the clusters.
     */
    public double calcAvgSilhouettes(final List<ArrayList<Integer>> assignments) {

        double avgSilhouette = 0.0;

        if (assignments.size() <= 1) {
            // Default silhouette value is 0 there is only one cluster.
            return avgSilhouette;
        } else {
            final List<Double> silhouetteList = new ArrayList<>();
            for (final List<Integer> cluster : assignments) {
                for (int i = 0; i < cluster.size(); i++) {
                    /*
                     * Calculate the average dissimilarity of instance i to all other instances of
                     * the same cluster.
                     */
                    final Instance instanceI = this.instances.instance(cluster.get(i));
                    final double avgDissimFromISameCluster = this.calcAvgDissimSameCluster(i, instanceI, cluster);

                    /*
                     * For each other cluster that instance i is not part of, calculate the average
                     * dissimilarity of instance i to all instances of these other clusters and
                     * search the minimum.
                     */
                    final double minAvgDissimFromIToOtherClusters = this.calcMinAvgDissimOtherClusters(instanceI,
                            assignments, cluster);

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
            }
            avgSilhouette = this.calculateAverage(silhouetteList);
        }
        return avgSilhouette;
    }

    /**
     * Calculate the minimum average dissimilarity of instance i to all other instances of different
     * clusters.
     *
     * @param instanceI
     * @param assignments
     * @param cluster
     * @return
     */
    private double calcMinAvgDissimOtherClusters(final Instance instanceI, final List<ArrayList<Integer>> assignments,
            final List<Integer> cluster) {
        double minAvgDissimFromIToOtherClusters = Double.POSITIVE_INFINITY;
        for (final List<Integer> otherCluster : assignments) {
            if (!otherCluster.equals(cluster)) {
                double sumDistanceFromIOtherClusters = 0.0;
                for (int j = 0; j < otherCluster.size(); j++) {
                    final Instance otherInstance = this.instances.instance(otherCluster.get(j));
                    sumDistanceFromIOtherClusters += this.hierarchicalClusterer.getDistanceFunction()
                            .distance(instanceI, otherInstance);
                }
                final double avgDissimFromIOtherCluster = sumDistanceFromIOtherClusters / otherCluster.size();
                minAvgDissimFromIToOtherClusters = Math.min(minAvgDissimFromIToOtherClusters,
                        avgDissimFromIOtherCluster);
            }
        }
        return minAvgDissimFromIToOtherClusters;
    }

    /**
     * Calculates the average dissimilarity of instance i to all other instances of the same
     * cluster.
     *
     * @param i
     * @param instanceI
     * @param cluster
     * @return
     */
    private double calcAvgDissimSameCluster(final int i, final Instance instanceI, final List<Integer> cluster) {
        double sumDistanceFromI = 0.0;
        for (int j = 0; j < cluster.size(); j++) {
            if (j != i) {
                final Instance otherInstance = this.instances.instance(cluster.get(j));
                sumDistanceFromI += this.hierarchicalClusterer.getDistanceFunction().distance(instanceI, otherInstance);
            }
        }
        return sumDistanceFromI / (cluster.size() - 1);
    }

    /**
     * Find the maximum value of the average silhouettes.
     *
     * @param avgSilhouettes
     *            List of all average silhouettes of every cluster.
     * @return Maximum of the average silhouettes.
     */
    private int findMaxAvgSilhouetteIndex(final List<Double> avgSilhouettes) {
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
    private double calculateAverage(final List<Double> list) {
        double sum = 0;
        if (!list.isEmpty()) {
            for (final double entry : list) {
                sum += entry;
            }
            return sum / list.size();
        }
        return sum;
    }

    /**
     * Builds a String of the instance-to-cluster assignments for debugging.
     *
     * @param assignments
     * @return
     */
    private void printAssignmentString(final List<ArrayList<Integer>> assignments) {
        String assignmentString = "";
        for (final List<Integer> v : assignments) {
            assignmentString += v.toString();
        }
        AvgSilhouetteMethod.LOGGER.info("Assignments: " + assignmentString + "\n");
    }

    /**
     * Returns a list which i-th entry is the number of the assigned cluster of the i-th instance.
     *
     * @param numberOfClusters
     *            number of total clusters of this clustering
     * @return
     */
    private List<ArrayList<Integer>> buildClusterAssignmentsList(final int numberOfClusters) {
        // Assignments of each data point the clusters.
        // final List<Integer>[] assignments = new ArrayList[numberOfClusters];
        final List<ArrayList<Integer>> assignments = new ArrayList<>();
        // Initialize assignments with empty vectors.
        for (int j = 0; j < numberOfClusters; j++) {
            // assignments[j] = new ArrayList<>();
            assignments.add(j, new ArrayList<>());
        }

        for (int s = 0; s < this.instances.numInstances(); s++) {
            try {
                final int assignedCluster = this.hierarchicalClusterer.clusterInstance(this.instances.instance(s));
                // assignments[assignedCluster].add(s);
                assignments.get(assignedCluster).add(s);
            } catch (final Exception e) { // NOPMD NOCS api dependency
                AvgSilhouetteMethod.LOGGER.error("Clustering at AvgSilhouetteMethod failed.", e);
            }
        }
        return assignments;
    }

    /**
     * Print clustering results for debugging.
     *
     * @param clusteringResults
     *            Clustered input data
     */
    public void printClusteringResults(final Map<Integer, List<Pair<Instance, Double>>> clusteringResults) {
        for (int i = 0; i < clusteringResults.size(); i++) {
            AvgSilhouetteMethod.LOGGER.info(clusteringResults.get(i).toString() + "\n");
        }
    }
}
