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
 * @author SL
 *
 *         Elbow method for selecting a "good" number of clusters after clustering.
 */
public class ElbowMethod implements IClusterSelectionMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusterer.class);
    private final HierarchicalClusterer hierarchicalClusterer;
    private final Instances instances;

    /**
     * Constructor.
     *
     * @param hierarchicalClusterer
     *            Clusterer that performs hierarchical clustering
     * @param instances
     *            Input data
     */
    public ElbowMethod(final HierarchicalClusterer hierarchicalClusterer, final Instances instances) {
        this.hierarchicalClusterer = hierarchicalClusterer;
        this.instances = instances;
    }

    /**
     * Find a "good" clustering for the input data.
     *
     * @return a "good" clustering according to the Elbow method
     */
    @Override
    public Map<Integer, List<Pair<Instance, Double>>> analyze() {

        ElbowMethod.LOGGER.info("Starting ElbowMethod");
        // sum of square error for each number of clusters.
        final List<Double> ess = new ArrayList<>();
        for (int i = 1; i <= this.instances.numInstances(); i++) {

            try {
                final int numberOfClusters = i;
                this.hierarchicalClusterer.setNumClusters(numberOfClusters);
                this.hierarchicalClusterer.buildClusterer(this.instances);

                // Get the assignments of each data point to the clusters.
                final List<ArrayList<Integer>> assignments = this.buildClusterAssignmentsList(numberOfClusters);
                // Print AssignmentsString for debugging.
                this.printAssignmentString(assignments);

                // Calculate the error sum-of-squares for each cluster.
                double s = 0.0;
                for (int cluster = 0; cluster < numberOfClusters; cluster++) {
                    if (!assignments.get(cluster).isEmpty()) {
                        s += this.calcESS(assignments.get(cluster));
                    }
                }
                ess.add(i - 1, s);

                ElbowMethod.LOGGER.info(ess.toString() + "\n");

            } catch (final Exception e) { // NOPMD NOCS api dependency
                ElbowMethod.LOGGER.error("Hierarchical clustering failed.", e);
            }
        }

        // Find a "good" number of clusters by finding the elbow of the ESS.
        final int goodClustereNumber = this.findElbow(ess);

        // Cluster instances with the "good" number of clusters.
        Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>();
        try {
            this.hierarchicalClusterer.setNumClusters(goodClustereNumber);
            this.hierarchicalClusterer.buildClusterer(this.instances);
            clusteringResults = ClusteringResultsBuilder.buildClusteringResults(this.instances,
                    this.hierarchicalClusterer);
        } catch (final Exception e) { // NOPMD NOCS api dependency
            ElbowMethod.LOGGER.error("Clustering at ElbowMethod failed.", e);
        }

        // Print clusteringResult
        this.printClusteringResults(clusteringResults);

        ElbowMethod.LOGGER.info("ElbowMethod done.");
        return clusteringResults;
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
                ElbowMethod.LOGGER.error("Clustering at ElbowMethod failed.", e);
            }
        }
        return assignments;
    }

    /**
     * Find the Elbow of the ESS data.
     *
     * @param ess
     *            Error sum-of-squares array for all possible number of clusters.
     */
    private int findElbow(final List<Double> ess) {

        // ess = new ArrayList<>();
        // ess.add(7.0);
        // ess.add(3.0);
        // ess.add(2.0);
        // ess.add(1.5);
        // ess.add(1.0);
        // ess.add(0.5);

        final int essSize = ess.size();
        final double start = ess.get(0);
        final double end = ess.get(essSize - 1);
        final double decline = (end - start) / (essSize - 1);

        double maxDistance = 0;
        int elbowIndex = 1;
        for (int i = 0; i < (essSize - 1); i++) {
            final double referencePoint = start + ((i + 1) * decline);
            final double oldMaxDistance = maxDistance;
            maxDistance = Math.max(maxDistance, referencePoint - ess.get(i + 1));
            if (maxDistance > oldMaxDistance) {
                elbowIndex = i + 1;
            }
        }

        System.out.println(maxDistance);
        System.out.println(elbowIndex);

        return elbowIndex;
    }

    // From HierarchicalClusterer
    /**
     * Calculated error sum-of-squares for a given cluster.
     *
     * @param cluster
     *            Calculate the ESS for this cluster
     * @return ESS
     **/
    public double calcESS(final List<Integer> cluster) {

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
        for (int j = 0; j < this.instances.numAttributes(); j++) {
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
        // Sum up distances of each data point in cluster to centroid to get ESS.
        double clusterESS = 0;
        for (int i = 0; i < cluster.size(); i++) {
            final Instance instance = this.instances.instance(cluster.get(i));
            clusterESS += distanceFunction.distance(centroid, instance);
        }
        return clusterESS;
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
