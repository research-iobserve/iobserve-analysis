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

import java.awt.Container;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.eclipse.net4j.util.collection.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.clusterers.HierarchicalClusterer;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instance;
import weka.core.Instances;
import weka.gui.hierarchyvisualizer.HierarchyVisualizer;

/**
 * @author Stephan Lenga
 *
 */
public class HierarchicalClustering implements IHierarchicalClustering {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusterer.class);

    private static HierarchicalClusterer hierarchicalClusterer;
    private String[] linkage;
    private DistanceFunction distanceFunction;

    public HierarchicalClustering() {
        this.linkage = new String[] { "-L", "COMPLETE" };
        this.distanceFunction = new EuclideanDistance();
    }

    @Override
    public Map<Integer, List<Pair<Instance, Double>>> clusterInstances(final Instances instances) {
        final Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>(); // NOPMD
        HierarchicalClustering.hierarchicalClusterer = new HierarchicalClusterer();
        HierarchicalClustering.hierarchicalClusterer.setDistanceFunction(this.distanceFunction);
        HierarchicalClustering.hierarchicalClusterer.setDistanceIsBranchLength(false);
        HierarchicalClustering.hierarchicalClusterer.setNumClusters(1);

        try {
            HierarchicalClustering.hierarchicalClusterer.buildClusterer(instances);

            // // Print Membership-Matrix
            // double[] arr;
            // for (int i = 0; i < instances.numInstances(); i++) {
            //
            // arr =
            // HierarchicalClustering.hierarchicalClusterer.distributionForInstance(instances.instance(i));
            // for (final double element : arr) {
            // System.out.print(element + ",");
            // }
            // System.out.println();
            // }

            // ##### TESTING
            // Print Newick
            System.out.println("#### NEWICK ####");
            HierarchicalClustering.hierarchicalClusterer.setPrintNewick(true);
            System.out.println(HierarchicalClustering.hierarchicalClusterer.toString());

            // Show clustered data
            final JFrame mainFrame = new JFrame("Weka Test");
            mainFrame.setSize(600, 400);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            final Container content = mainFrame.getContentPane();
            content.setLayout(new GridLayout(1, 1));

            final HierarchyVisualizer visualizer = new HierarchyVisualizer(
                    HierarchicalClustering.hierarchicalClusterer.toString());
            content.add(visualizer);

            mainFrame.setVisible(true);

            // // Evaluation
            // final ClusterEvaluation eval = new ClusterEvaluation();
            // eval.setClusterer(HierarchicalClustering.hierarchicalClusterer); // the cluster to
            // // evaluate
            // eval.evaluateClusterer(newInstances); // data to evaluate the clusterer on
            // System.out.println("# of clusters: " + eval.getNumClusters()); // output # of
            // clusters

            // ##### END TESTING

            /**
             * Code used from org.iobserve.analysis.userbehavior.XMeansClustering to use
             * org.iobserve.analysis.userbehavior.ClusteringResults
             */

            int[] clustersize = null;
            final int[] assignments = new int[instances.numInstances()];
            clustersize = new int[HierarchicalClustering.hierarchicalClusterer.getNumClusters()];
            for (int s = 0; s < instances.numInstances(); s++) {
                assignments[s] = HierarchicalClustering.hierarchicalClusterer.clusterInstance(instances.instance(s));
                clustersize[HierarchicalClustering.hierarchicalClusterer.clusterInstance(instances.instance(s))]++;
            }

            for (int i = 0; i < instances.numInstances(); i++) {
                final Instance currentInstance = instances.instance(i);
                final int cluster = HierarchicalClustering.hierarchicalClusterer.clusterInstance(currentInstance);
                final double probability = HierarchicalClustering.hierarchicalClusterer
                        .distributionForInstance(currentInstance)[cluster];
                if (clusteringResults.get(cluster) == null) {
                    clusteringResults.put(cluster, new LinkedList<Pair<Instance, Double>>());
                }
                clusteringResults.get(cluster).add(new Pair<>(currentInstance, probability));

            }

            // clusteringResults = Optional.of(new ClusteringResults("Hierarchical",
            // HierarchicalClustering.hierarchicalClusterer.getNumClusters(), assignments, null));

        } catch (final Exception e) {
            HierarchicalClustering.LOGGER.error("Hierarchical clustering failed.", e);
        }

        return clusteringResults;
    }

    public String[] getLinkage() {
        return this.linkage;
    }

    public void setLinkage(final String linkageType) {
        switch (linkageType) {
        case "single":
            this.linkage = new String[] { "-L", "SINGLE" };
        case "average":
            this.linkage = new String[] { "-L", "AVERAGE" };
        case "complete":
            this.linkage = new String[] { "-L", "COMPLETE" };
        }
    }

    public DistanceFunction getDistanceFunction() {
        return this.distanceFunction;
    }

    public void setDistanceFunction(final DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

}
