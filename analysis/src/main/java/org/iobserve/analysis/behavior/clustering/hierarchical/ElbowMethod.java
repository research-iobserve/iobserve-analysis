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

        // var sse = {};
        // for (var k = 1; k <= maxK; ++k) {
        // sse[k] = 0;
        // clusters = kmeans(dataset, k);
        // clusters.forEach(function(cluster) {
        // mean = clusterMean(cluster);
        // cluster.forEach(function(datapoint) {
        // sse[k] += Math.pow(datapoint - mean, 2);
        // });
        // });
        // }

        final int numInstances = instances.numInstances();

        List<Double> lastSSE = new ArrayList<>();
        for (int i = 1; 1 <= instances.numInstances(); ++i) {

            try {
                final int numberOfClusters = i;
                hierarchicalClusterer.setNumClusters(numberOfClusters);
                hierarchicalClusterer.buildClusterer(instances);

                // Assignments of each data point the clusters.
                final Vector<Integer>[] assignments = new Vector[numberOfClusters];
                // Initialize assignments with empty vectors.
                for (int j = 0; j < numberOfClusters; j++) {
                    assignments[j] = new Vector();
                }
                // Cluster size of each cluster
                final int[] clustersize = new int[numberOfClusters];

                for (int s = 0; s < numInstances; s++) {
                    final int assignedCluster = hierarchicalClusterer.clusterInstance(instances.instance(s));
                    assignments[assignedCluster].add(s);
                    clustersize[hierarchicalClusterer.clusterInstance(instances.instance(s))]++;
                }

                /*
                 * Take a random data point of a cluster as reference point to calculate the total
                 * intra-cluster variation
                 */

                // sum of square error for each cluster
                final List<Double> sse = new ArrayList<>(numberOfClusters);

                // Calculate the intra-cluster variation for each cluster
                for (int cluster = 0; cluster < numberOfClusters; cluster++) {
                    sse.add(cluster, this.calcESS(assignments[cluster], instances, hierarchicalClusterer));
                }

                lastSSE = new ArrayList<>(sse);

                System.out.println(sse);

            } catch (final Exception e) {
                ElbowMethod.LOGGER.error("Hierarchical clustering failed.", e);
            }
        }

        System.out.println("Elbow done");

        return null;
    }

    // From HierarchicalClusterer
    /** calculated error sum-of-squares for instances wrt centroid **/
    public double calcESS(Vector<Integer> cluster, Instances instances, HierarchicalClusterer hierarchicalClusterer) {

        final DistanceFunction distanceFunction = hierarchicalClusterer.getDistanceFunction();

        final double[] fValues1 = new double[instances.numAttributes()];
        for (int i = 0; i < cluster.size(); i++) {
            final Instance instance = instances.instance(cluster.elementAt(i));
            for (int j = 0; j < instances.numAttributes(); j++) {
                fValues1[j] += instance.value(j);
            }
        }
        for (int j = 0; j < instances.numAttributes(); j++) {
            fValues1[j] /= cluster.size();
        }
        // set up two instances for distance function
        final Instance centroid = (Instance) instances.instance(cluster.elementAt(0)).copy();
        for (int j = 0; j < instances.numAttributes(); j++) {
            centroid.setValue(j, fValues1[j]);
        }
        double fESS = 0;
        for (int i = 0; i < cluster.size(); i++) {
            final Instance instance = instances.instance(cluster.elementAt(i));
            fESS += distanceFunction.distance(centroid, instance);
        }
        return fESS / cluster.size();
    } // calcESS

}
