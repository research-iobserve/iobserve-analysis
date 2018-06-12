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
import java.util.Optional;

import javax.swing.JFrame;

import org.iobserve.analysis.behavior.karlsruhe.data.ClusteringResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.clusterers.HierarchicalClusterer;
import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;
import weka.gui.hierarchyvisualizer.HierarchyVisualizer;

/**
 * @author Stephan Lenga
 *
 */
public class HierarchicalClustering implements IHierarchicalClustering {

    private static final Logger LOGGER = LoggerFactory.getLogger(HierarchicalClusterer.class);

    private static HierarchicalClusterer hieraricalClusterer;
    private String[] linkage;
    private DistanceFunction distanceFunction;

    public HierarchicalClustering() {
        this.linkage = new String[] { "-L", "COMPLETE" };
        this.distanceFunction = new EuclideanDistance();
    }

    @Override
    public Optional<ClusteringResults> clusterInstances(final Instances instances) {
        final Optional<ClusteringResults> clusteringResults = Optional.empty();
        HierarchicalClustering.hieraricalClusterer = new HierarchicalClusterer();
        HierarchicalClustering.hieraricalClusterer.setDistanceFunction(this.distanceFunction);
        HierarchicalClustering.hieraricalClusterer.setDistanceIsBranchLength(false);

        try {
            HierarchicalClustering.hieraricalClusterer.buildClusterer(instances);

            // Print normal
            HierarchicalClustering.hieraricalClusterer.setPrintNewick(false);
            System.out.println(HierarchicalClustering.hieraricalClusterer.graph());

            // Print Newick
            // HierarchicalClustering.hieraricalClusterer.setPrintNewick(true);
            // System.out.println(HierarchicalClustering.hieraricalClusterer.graph());

            // Show clustered data
            final JFrame mainFrame = new JFrame("Weka Test");
            mainFrame.setSize(600, 400);
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            final Container content = mainFrame.getContentPane();
            content.setLayout(new GridLayout(1, 1));

            final HierarchyVisualizer visualizer = new HierarchyVisualizer(
                    HierarchicalClustering.hieraricalClusterer.graph());
            content.add(visualizer);

            mainFrame.setVisible(true);

            // // Evaluation
            // final ClusterEvaluation eval = new ClusterEvaluation();
            // eval.setClusterer(HierarchicalClustering.hieraricalClusterer); // the cluster to
            // // evaluate
            // eval.evaluateClusterer(newInstances); // data to evaluate the clusterer on
            // System.out.println("# of clusters: " + eval.getNumClusters()); // output # of
            // // clusters

            return clusteringResults;

        } catch (final Exception e) {
            HierarchicalClustering.LOGGER.error("Hierarchical clustering failed.", e);
        }

        return Optional.empty();
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
