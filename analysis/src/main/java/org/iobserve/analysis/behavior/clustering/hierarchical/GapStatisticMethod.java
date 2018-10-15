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
import java.util.concurrent.ThreadLocalRandom;

import org.eclipse.net4j.util.collection.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.clusterers.HierarchicalClusterer;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;

/**
 * Use Gap Statistic Method for selecting a "good" amount of clusters for a given input data set,
 * and cluster this data accordingly.
 *
 * @author SL
 * @since 0.0.3
 */
public class GapStatisticMethod implements IClusterSelectionMethods {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusterer.class);
    private static double numRefDataSets = 3;

    private final HierarchicalClusterer hierarchicalClusterer;
    private final Instances instances;
    private List<Double> minAttributeValues;
    private List<Double> maxAttributeValues;

    /**
     * Constructor.
     *
     * @param hierarchicalClusterer
     *            Clusterer that performs hierarchical clustering.
     * @param instances
     *            Input data.
     */
    public GapStatisticMethod(final HierarchicalClusterer hierarchicalClusterer, final Instances instances) {
        this.hierarchicalClusterer = hierarchicalClusterer;
        this.instances = instances;
    }

    @Override
    public Map<Integer, List<Pair<Instance, Double>>> analyze() {

        GapStatisticMethod.LOGGER.info("Starting GapStatisticMethod");
        Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>(); // NOPMD
        if (this.instances != null) {

            // Calculate sum of square error for each number of clusters.
            final int maxNumberOfClusters = this.instances.numInstances();
            final List<Double> essList = this.calcESSList(this.instances, maxNumberOfClusters);

            /*
             * Generate reference data sets with a random uniform distribution. For that, find min
             * and max values for each attribute which will be the boundaries of possible values.
             */
            // Find min and max boundary values for each attribute of all instances.
            this.findBoundaryValues();
            /*
             * Create numRefDataSets-many data sets with new instances with random attribute values
             * in range of the boundary values.
             */
            final List<Instances> uniformDistrDatasets = new ArrayList<>();
            // Contains for every uniform distributed data set a ESS list.
            final List<List<Double>> essListForEachUniformDataset = new ArrayList<>();
            for (int i = 0; i < GapStatisticMethod.numRefDataSets; i++) {
                final Instances uniformDistrDataSet = this.createRandomInstancesWithBoundaries();
                uniformDistrDatasets.add(uniformDistrDataSet);
                essListForEachUniformDataset.add(this.calcESSList(uniformDistrDataSet, maxNumberOfClusters));
            }

            // Compute the estimated gap statistic.
            clusteringResults = this.gapStatistics(essList, essListForEachUniformDataset);
        }
        GapStatisticMethod.LOGGER.info("GapStatisticMethod done.");

        return clusteringResults;
    }

    /**
     * Calculate the Gap Statistic for each number of clusters.
     *
     * @param essList
     *            List with sum of square errors for each clustering of the input data.
     * @param essListReference
     *            List with sum of square errors for each clustering of the reference data.
     * @return Clustering result of input data with a "good" amout of clusters.
     */
    private Map<Integer, List<Pair<Instance, Double>>> gapStatistics(final List<Double> essList,
            final List<List<Double>> essListForEachUniformDataset) {

        final List<Double> gapStatistics = new ArrayList<>();
        // Calculate log of each ESS for calculating the Gap Statistic.
        final List<Double> logESS = new ArrayList<>();
        for (final Double entry : essList) {
            if (entry == 0.0) {
                logESS.add(0.0);
            } else {
                logESS.add(Math.log(entry));
            }
        }
        final List<List<Double>> logESSRef = new ArrayList<>();
        for (final List<Double> essListUniform : essListForEachUniformDataset) {
            final List<Double> hlpList = new ArrayList<>();
            for (final Double entry : essListUniform) {
                hlpList.add(Math.log(entry));
            }
            logESSRef.add(hlpList);
        }

        // Calculate the Gap Statistic for each number of clusters.
        final List<Double> wkbs = new ArrayList<>();
        for (int i = 0; i < essList.size(); i++) {
            // within-dispersion measure of ref data for a spesific number of clusters.
            double wkb = 0.0;
            for (final List<Double> l : logESSRef) {
                wkb += l.get(i); // here, i is the number of clusters.
            }
            wkb /= GapStatisticMethod.numRefDataSets;
            wkbs.add(wkb);
            gapStatistics.add(wkb - logESS.get(i));
        }

        // Calculate the standard deviation.
        final List<Double> sks = this.calcStandardDeviation(logESSRef, wkbs);

        // Find good number of clusters.
        final int goodNumberOfClusters = this.findGoodNumberOfClusters(gapStatistics, sks);

        // Cluster instances with the "good" number of clusters.
        Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>();
        try {
            this.hierarchicalClusterer.setNumClusters(goodNumberOfClusters);
            this.hierarchicalClusterer.buildClusterer(this.instances);
            clusteringResults = ClusteringResultsBuilder.buildClusteringResults(this.instances,
                    this.hierarchicalClusterer);
        } catch (final Exception e) { // NOPMD NOCS api dependency
            GapStatisticMethod.LOGGER.error("Clustering at ElbowMethod failed.", e);
        }

        return clusteringResults;
    }

    /**
     * Calculate the standard deviation of a list of values.
     *
     * @param bWkbs
     *            Total within-cluster sum of squares of reference data.
     * @param wkbs
     *            Total within-cluster sum of squares of actual user behavior data.
     * @return List of standard deviations for each number of clusters.
     */
    private List<Double> calcStandardDeviation(final List<List<Double>> bWkbs, final List<Double> wkbs) {
        final List<Double> sks = new ArrayList<>();

        for (int i = 0; i < wkbs.size(); i++) {
            double res = 0.0;

            for (final List<Double> w : bWkbs) {
                double hlp = 0.0;
                hlp = w.get(i) - wkbs.get(i);
                hlp *= hlp;
                res += hlp;
            }
            res /= GapStatisticMethod.numRefDataSets;
            final double sd = Math.sqrt(res);
            final double sk = sd * Math.sqrt(1 + (1 / GapStatisticMethod.numRefDataSets));
            sks.add(sk);
        }

        return sks;
    }

    /**
     * Find the number of clusters with the biggest gap within standard deviation.
     *
     * @param gapStatistics
     *            List of gap statistic for each number of clusters.
     * @param sks
     *            List of standard deviation for each number of clusters.
     * @return goodNumberOfClusters
     */
    private int findGoodNumberOfClusters(final List<Double> gapStatistics, final List<Double> sks) {
        int goodNumberOfClusters = 1;

        for (int k = 0; k < (gapStatistics.size() - 1); k++) {
            final double gapK = gapStatistics.get(k);
            final double gapKPlus1 = gapStatistics.get(k + 1);

            if (gapK >= (gapKPlus1 - sks.get(k + 1))) {
                goodNumberOfClusters = k + 1;
                break;
            }
        }
        return goodNumberOfClusters;
    }

    /**
     * Calculate the sum-of-square error for each each number of clusters.
     *
     * @param instanceList
     *            Instances which are to be clustered.
     * @param maxNumberOfClusters
     *            Max cluster number.
     * @return i-th entry of the list contains the sum-of-square error for i clusters.
     */
    private List<Double> calcESSList(final Instances instanceList, final int maxNumberOfClusters) {
        final List<Double> ess = new ArrayList<>();
        for (int i = 1; i <= maxNumberOfClusters; i++) {

            try {
                final int numberOfClusters = i;
                this.hierarchicalClusterer.setNumClusters(numberOfClusters);
                this.hierarchicalClusterer.buildClusterer(instanceList);

                // Get the assignments of each data point to the clusters.
                final List<ArrayList<Integer>> assignments = this.buildClusterAssignmentsList(numberOfClusters);

                // Calculate the error sum-of-squares for each cluster.
                double s = 0.0;
                for (int cluster = 0; cluster < numberOfClusters; cluster++) {
                    if (!assignments.get(cluster).isEmpty()) {
                        s += this.calcESS(assignments.get(cluster));
                    }
                }
                ess.add(i - 1, s);

            } catch (final Exception e) { // NOPMD NOCS api dependency
                GapStatisticMethod.LOGGER.error("Hierarchical clustering failed.", e);
            }
        }
        return ess;
    }

    /**
     * Creates Instances and each instance has attribute values in the range of the boundary values
     * minAttributeValues and maxAttributeValues.
     *
     * @return Instances with random uniform distribution in range of boundary values.
     */
    private Instances createRandomInstancesWithBoundaries() {
        final Instances randomInstances = new Instances(this.instances);
        for (int i = 0; i < randomInstances.numInstances(); i++) {
            for (int j = 0; j < randomInstances.numAttributes(); j++) {
                final double minAttValue = this.minAttributeValues.get(j);
                final double maxAttValue = this.maxAttributeValues.get(j);
                final long randomAttValue = ThreadLocalRandom.current().nextLong((long) minAttValue,
                        (long) maxAttValue + 1);
                randomInstances.instance(i).setValue(j, randomAttValue);
            }
        }
        return randomInstances;
    }

    /**
     * Finds min and max values for each attribute of the instances.
     */
    private void findBoundaryValues() {
        final List<Double> minBoundaryValues = new ArrayList<>();
        final List<Double> maxBoundaryValues = new ArrayList<>();

        for (int i = 0; i < this.instances.numInstances(); i++) {
            final Instance instance = this.instances.instance(i);
            for (int j = 0; j < instance.numAttributes(); j++) {
                // Initialization of both min and max lists.
                if (minBoundaryValues.size() == j) {
                    minBoundaryValues.add(j, Double.POSITIVE_INFINITY);
                }
                if (maxBoundaryValues.size() == j) {
                    maxBoundaryValues.add(j, Double.NEGATIVE_INFINITY);
                }
                final double minValue = minBoundaryValues.get(j);
                final double maxValue = maxBoundaryValues.get(j);
                final double attributeValue = instance.value(j);
                if (attributeValue < minValue) {
                    minBoundaryValues.remove(j);
                    minBoundaryValues.add(j, attributeValue);
                }
                if (attributeValue >= maxValue) {
                    maxBoundaryValues.remove(j);
                    maxBoundaryValues.add(j, attributeValue);
                }
            }
        }
        this.setMinAttributeValues(minBoundaryValues);
        this.setMaxAttributeValues(maxBoundaryValues);
    }

    // From HierarchicalClusterer
    /**
     * Calculated error sum-of-squares (ESS) for a given cluster.
     *
     * @param cluster
     *            Calculate the ESS for this cluster
     * @return ESS
     **/
    public double calcESS(final List<Integer> cluster) {

        if ((cluster.size() == 0) || (cluster.size() == 1)) {
            return 0.0;
        }
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
        double clusterESS = 0.0;
        for (int i = 0; i < cluster.size(); i++) {
            final Instance instance = this.instances.instance(cluster.get(i));
            clusterESS += distanceFunction.distance(centroid, instance);
        }
        return clusterESS / cluster.size();
    }

    /**
     * Returns a list which i-th entry is the number of the assigned cluster of the i-th instance.
     *
     * @param numberOfClusters
     *            number of total clusters of this clustering
     * @return List that contains Clusters and their assigned instances.
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
                GapStatisticMethod.LOGGER.error("Clustering at GapStatisticMethod failed.", e);
            }
        }
        return assignments;
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
        GapStatisticMethod.LOGGER.info("Assignments: " + assignmentString + "\n");
    }

    private void setMinAttributeValues(final List<Double> minAttributeValues) {
        this.minAttributeValues = minAttributeValues;
    }

    private void setMaxAttributeValues(final List<Double> maxAttributeValues) {
        this.maxAttributeValues = maxAttributeValues;
    }
}
