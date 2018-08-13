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
import weka.core.ManhattanDistance;
import weka.core.SelectedTag;
import weka.gui.hierarchyvisualizer.HierarchyVisualizer;

/**
 * @author Stephan Lenga
 *
 */
public class HierarchicalClustering implements IHierarchicalClustering {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusterer.class);

    private static HierarchicalClusterer hierarchicalClusterer;
    private SelectedTag linkage;
    private DistanceFunction distanceFunction;
    private final String clusterSelectionMethod;

    /**
     * constructor.
     */
    public HierarchicalClustering(final String distanceMetric, final String clusterSelectionMethod,
            final String linkage) {
        this.setLinkage(linkage);
        this.setDistanceFunction(distanceMetric);
        this.clusterSelectionMethod = clusterSelectionMethod;
    }

    @Override
    public Map<Integer, List<Pair<Instance, Double>>> clusterInstances(final Instances instances) {
        Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>(); // NOPMD

        // Create hierarchical clusterer and set its options
        HierarchicalClustering.hierarchicalClusterer = new HierarchicalClusterer();
        HierarchicalClustering.hierarchicalClusterer.setDistanceFunction(this.distanceFunction);
        HierarchicalClustering.hierarchicalClusterer.setDistanceIsBranchLength(false);
        HierarchicalClustering.hierarchicalClusterer.setLinkType(this.linkage);

        // Find a "good" number of clusters by applying the clusterSelectionMethod
        final NumOfClustersSelector clusterSelection = new NumOfClustersSelector(this.clusterSelectionMethod,
                HierarchicalClustering.hierarchicalClusterer, instances);
        clusterSelection.findOptimalNumOfClusters();

        // HierarchicalClustering.hierarchicalClusterer.setNumClusters(2);

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
            HierarchicalClustering.LOGGER.info("#### NEWICK ####");
            HierarchicalClustering.hierarchicalClusterer.setPrintNewick(true);
            HierarchicalClustering.LOGGER.info(HierarchicalClustering.hierarchicalClusterer.toString());

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

            clusteringResults = ClusteringResultsBuilder.buildClusteringResults(instances,
                    HierarchicalClustering.hierarchicalClusterer);

            // for (int i = 0; i < instances.numInstances(); i++) {
            // final Instance currentInstance = instances.instance(i);
            // final int cluster =
            // HierarchicalClustering.hierarchicalClusterer.clusterInstance(currentInstance);
            // final double probability = HierarchicalClustering.hierarchicalClusterer
            // .distributionForInstance(currentInstance)[cluster];
            // if (clusteringResults.get(cluster) == null) {
            // clusteringResults.put(cluster, new LinkedList<Pair<Instance, Double>>());
            // }
            // clusteringResults.get(cluster).add(new Pair<>(currentInstance, probability));
            //
            // }

            // clusteringResults = Optional.of(new ClusteringResults("Hierarchical",
            // HierarchicalClustering.hierarchicalClusterer.getNumClusters(), assignments, null));

        } catch (final Exception e) { // NOPMD NOCS api dependency
            HierarchicalClustering.LOGGER.error("Hierarchical clustering failed.", e);
        }

        return clusteringResults;
    }

    /**
     *
     * @param linkageType
     *            type of likage used in hierarchical clustering
     */
    private final void setLinkage(final String linkageType) {
        switch (linkageType) {
        case "single":
            this.linkage = new SelectedTag(0, HierarchicalClusterer.TAGS_LINK_TYPE);
            break;
        case "complete":
            this.linkage = new SelectedTag(1, HierarchicalClusterer.TAGS_LINK_TYPE);
            break;
        case "average":
            this.linkage = new SelectedTag(2, HierarchicalClusterer.TAGS_LINK_TYPE);
            break;
        default: // Complete linkage as default
            this.linkage = new SelectedTag(1, HierarchicalClusterer.TAGS_LINK_TYPE);
            break;
        }
    }

    public DistanceFunction getDistanceFunction() {
        return this.distanceFunction;
    }

    private final void setDistanceFunction(final DistanceFunction distanceFunction) {
        this.distanceFunction = distanceFunction;
    }

    private void setDistanceFunction(final String distanceType) {
        switch (distanceType) {
        case "manhatten":
            this.distanceFunction = new ManhattanDistance();
            break;
        case "euclidean":
            this.distanceFunction = new EuclideanDistance();
            break;
        default:
            this.distanceFunction = new ManhattanDistance(); // Manhattan as default
            break;
        }
    }

}
