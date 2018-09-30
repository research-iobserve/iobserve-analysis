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
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Use Elbow Method for selecting a "good" amount of clusters for a given input data set, and
 * cluster this data accordingly.
 *
 * @author SL
 * @since 0.0.3
 */
public class ElbowMethod implements IClusterSelectionMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusterer.class);
    private final HierarchicalClusterer hierarchicalClusterer;
    private final Instances instances;

    /**
     * Constructor.
     *
     * @param hierarchicalClusterer
     *            Clusterer that performs hierarchical clustering.
     * @param instances
     *            Input data to be clustered.
     */
    public ElbowMethod(final HierarchicalClusterer hierarchicalClusterer, final Instances instances) {
        this.hierarchicalClusterer = hierarchicalClusterer;
        this.instances = instances;
    }

    /**
     * Find a "good" clustering for the input data.
     *
     * @return a "good" clustering according to the Elbow method.
     */
    @Override
    public Map<Integer, List<Pair<Instance, Double>>> analyze() {

        ElbowMethod.LOGGER.info("Starting ElbowMethod");
        Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>(); // NOPMD
        if (this.instances != null) {
            // Calculate sum of square error for each number of clusters.
            final List<Double> wss = new ArrayList<>();
            for (int i = 0; i < this.instances.numInstances(); i++) {

                try {
                    final int numberOfClusters = i + 1;
                    this.hierarchicalClusterer.setNumClusters(numberOfClusters);
                    this.hierarchicalClusterer.buildClusterer(this.instances);

                    // Get the assignments of each data point to the clusters.
                    final List<ArrayList<Integer>> assignments = this.buildClusterAssignmentsList(numberOfClusters);

                    // Calculate the within-cluster sum-of-square for each cluster.
                    double s = 0.0;
                    for (int cluster = 0; cluster < numberOfClusters; cluster++) {
                        if (!assignments.get(cluster).isEmpty()) {
                            s += this.calcWSS(assignments.get(cluster));
                        }
                    }
                    wss.add(i, s);

                } catch (final Exception e) { // NOPMD NOCS api dependency
                    ElbowMethod.LOGGER.error("Hierarchical clustering failed.", e);
                }
            }

            // Find a "good" number of clusters by finding the elbow of the WSS.
            final int goodClustereNumber = this.findElbow(wss);

            // Cluster instances with the "good" number of clusters.
            try {
                this.hierarchicalClusterer.setNumClusters(goodClustereNumber);
                this.hierarchicalClusterer.buildClusterer(this.instances);
                clusteringResults = ClusteringResultsBuilder.buildClusteringResults(this.instances,
                        this.hierarchicalClusterer);
            } catch (final Exception e) { // NOPMD NOCS api dependency
                ElbowMethod.LOGGER.error("Clustering at ElbowMethod failed.", e);
            }
        }
        ElbowMethod.LOGGER.info("ElbowMethod done.");
        return clusteringResults;
    }

    /**
     * Returns a list which i-th entry is the number of the assigned cluster of the i-th instance.
     *
     * @param numberOfClusters
     *            Number of total clusters of this clustering.
     * @return List of clusteres and their assigned instances.
     */
    private List<ArrayList<Integer>> buildClusterAssignmentsList(final int numberOfClusters) {
        // Assignments of each data point the clusters.
        final List<ArrayList<Integer>> assignments = new ArrayList<>();
        // Initialize assignments with empty vectors.
        for (int j = 0; j < numberOfClusters; j++) {
            assignments.add(j, new ArrayList<>());
        }

        for (int s = 0; s < this.instances.numInstances(); s++) {
            try {
                final int assignedCluster = this.hierarchicalClusterer.clusterInstance(this.instances.instance(s));
                assignments.get(assignedCluster).add(s);
            } catch (final Exception e) { // NOPMD NOCS api dependency
                ElbowMethod.LOGGER.error("Clustering at ElbowMethod failed.", e);
            }
        }
        return assignments;
    }

    /**
     * Find the Elbow of the WSS data.
     *
     * @param wss
     *            Error sum-of-squares array for all possible number of clusters.
     */
    private int findElbow(final List<Double> wss) {

        // Filter zero values from the list.
        // wss.removeAll(Collections.singleton(0.0));

        final int wssSize = wss.size();
        final double startX = 1.0;
        final double endX = wssSize;
        final double startY = wss.get(0);
        final double endY = wss.get(wssSize - 1);

        double maxDistance = 0.0;
        int elbowIndex = 1;
        for (int i = 0; i < (wssSize - 1); i++) {
            final double refPointX = i + 1;
            final double refPointY = wss.get(i);
            // Calculate distance of a point to a line defined by two points (equation from wiki).
            final double distance = (Math
                    .abs(((((endY - startY) * refPointX) - ((endX - startX) * refPointY)) + (endX * startY))
                            - (endY * startX)))
                    / (Math.sqrt(((endY - startY) * (endY - startY)) + ((endX - startX) * (endX - startX))));

            final double oldMaxDistance = maxDistance;
            maxDistance = Math.max(distance, maxDistance);
            if (maxDistance > oldMaxDistance) {
                elbowIndex = i + 1;
            }
        }

        return elbowIndex;
    }

    // From HierarchicalClusterer
    /**
     * Calculate within-cluster sum-of-square (WSS) for a given cluster.
     *
     * @param cluster
     *            Calculate the WSS for this cluster.
     * @return WSS
     **/
    public double calcWSS(final List<Integer> cluster) {

        final DistanceFunction distanceFunction = this.hierarchicalClusterer.getDistanceFunction();
        final double[] sumAttValues = new double[this.instances.numAttributes()];
        for (int i = 0; i < cluster.size(); i++) {
            final Instance instance = this.instances.instance(cluster.get(i));
            // Sum up all values of all instances.
            for (int j = 0; j < this.instances.numAttributes(); j++) {
                sumAttValues[j] += instance.value(j);
            }
        }
        // Get average value of each attribute value.
        for (int j = 0; j < sumAttValues.length; j++) {
            sumAttValues[j] /= cluster.size();
        }

        /*
         * Create a centroid of this cluster by setting the average attributes of this cluster as
         * its own.
         */
        final Instance centroid = (Instance) this.instances.instance(cluster.get(0)).copy();
        for (int j = 0; j < this.instances.numAttributes(); j++) {
            centroid.setValue(j, sumAttValues[j]);
        }
        // Sum up distances of each data point in cluster to centroid to get WSS.
        double clusterWSS = 0.0;
        for (int i = 0; i < cluster.size(); i++) {
            final Instance instance = this.instances.instance(cluster.get(i));
            clusterWSS += Math.pow(distanceFunction.distance(centroid, instance), 2);
        }
        return clusterWSS;
    }

    /**
     * Builds a String of the instance-to-cluster assignments for debugging.
     *
     * @param assignments
     *            List of clusters and their assigned instances.
     */
    private void printAssignmentString(final List<ArrayList<Integer>> assignments) {
        String assignmentString = "";
        for (final List<Integer> v : assignments) {
            assignmentString += v.toString();
        }
        ElbowMethod.LOGGER.info("Assignments: " + assignmentString + "\n");
    }

    /**
     * Print clustering results for debugging.
     *
     * @param clusteringResults
     *            Clustering results that should be printed.
     */
    public void printClusteringResults(final Map<Integer, List<Pair<Instance, Double>>> clusteringResults) {
        for (int i = 0; i < clusteringResults.size(); i++) {
            ElbowMethod.LOGGER.info(clusteringResults.get(i).toString() + "\n");
        }
    }
}
