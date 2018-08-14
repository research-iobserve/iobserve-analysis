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
 *         Elbow method for selecting a "good" number of clusters after clustering.
 */
public class ElbowMethod implements IClusterSelectionMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusterer.class);

    /**
     * @param hierarchicalClusterer
     *            weka-clusterer that performs the hierarchical clustering
     *
     * @param instances
     *            input data that is clustered
     *
     * @return a "good" clustering according to the Elbow method
     */
    @Override
    public Map<Integer, List<Pair<Instance, Double>>> analyze(final HierarchicalClusterer hierarchicalClusterer,
            final Instances instances) {

        ElbowMethod.LOGGER.info("Starting ElbowMethod");
        final int numInstances = instances.numInstances();
        // sum of square error for each number of clusters.
        final List<Double> ess = new ArrayList<>();
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
                ElbowMethod.LOGGER.info("Assignments: " + assignmentString + "\n");

                // Calculate the error sum-of-squares for each cluster.
                double s = 0.0;
                for (int cluster = 0; cluster < numberOfClusters; cluster++) {
                    if (!assignments[cluster].isEmpty()) {
                        s += this.calcESS(assignments[cluster], instances, hierarchicalClusterer);
                    }
                }
                ess.add(i - 1, s);

                ElbowMethod.LOGGER.info(ess.toString() + "\n");

            } catch (final Exception e) {
                ElbowMethod.LOGGER.error("Hierarchical clustering failed.", e);
            }
        }

        final int goodClustereNumber = this.findElbow(ess);

        Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>(); // NOPMD

        try {
            hierarchicalClusterer.setNumClusters(goodClustereNumber);
            hierarchicalClusterer.buildClusterer(instances);
            clusteringResults = ClusteringResultsBuilder.buildClusteringResults(instances, hierarchicalClusterer);
        } catch (final Exception e) {
            ElbowMethod.LOGGER.error("Clustering at ElbowMethod failed.", e);
        }

        // Print clusteringResult
        for (int i = 0; i < clusteringResults.size(); i++) {
            ElbowMethod.LOGGER.info(clusteringResults.get(i).toString() + "\n");
        }

        ElbowMethod.LOGGER.info("ElbowMethod done.");
        return clusteringResults;
    }

    /**
     * Find the Ellbow of the ESS data.
     *
     * @param ess
     *            Error sum-of-squares array for all posslible number of clusters.
     */
    private int findElbow(List<Double> ess) {

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
     * @param instances
     *            Input data that is clustered
     * @param hierarchicalClusterer
     *            Clusterer that performs heirarchical clustering
     * @return ESS
     **/
    public double calcESS(Vector<Integer> cluster, Instances instances, HierarchicalClusterer hierarchicalClusterer) {

        final DistanceFunction distanceFunction = hierarchicalClusterer.getDistanceFunction();
        final double[] sumAttValues = new double[instances.numAttributes()];
        for (int i = 0; i < cluster.size(); i++) {
            final Instance instance = instances.instance(cluster.elementAt(i));
            // Sum up all values of all instances.
            for (int j = 0; j < instances.numAttributes(); j++) {
                sumAttValues[j] += instance.value(j);
            }
        }
        // Get average value of each attribute value.
        for (int j = 0; j < instances.numAttributes(); j++) {
            sumAttValues[j] /= cluster.size();
        }

        /*
         * Create a centroid of this cluster by setting the average attributes of this cluster as
         * its own.
         */
        final Instance centroid = (Instance) instances.instance(cluster.elementAt(0)).copy();
        for (int j = 0; j < instances.numAttributes(); j++) {
            centroid.setValue(j, sumAttValues[j]);
        }
        // Sum up distances of each data point in cluster to centroid to get ESS.
        double clusterESS = 0;
        for (int i = 0; i < cluster.size(); i++) {
            final Instance instance = instances.instance(cluster.elementAt(i));
            clusterESS += distanceFunction.distance(centroid, instance);
        }
        return clusterESS;
    }
}
