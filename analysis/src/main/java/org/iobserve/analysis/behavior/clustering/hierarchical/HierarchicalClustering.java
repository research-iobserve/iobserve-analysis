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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final String outputPath;

    /**
     * Constructor.
     *
     * @param distanceMetric
     *            Used distance metric for hierarchical clustering.
     *
     * @param clusterSelectionMethod
     *            Used method for selecting a "good" number of clusters from clustering.
     * @param linkage
     *            Used linkage for hierarchical clustering.
     * @param outputPath
     *            Output path for CSV file.
     */
    public HierarchicalClustering(final String distanceMetric, final String clusterSelectionMethod,
            final String linkage, final String outputPath) {
        this.setLinkage(linkage);
        this.setDistanceFunction(distanceMetric);
        this.clusterSelectionMethod = clusterSelectionMethod;
        this.outputPath = outputPath;
    }

    @Override
    public Map<Integer, List<Pair<Instance, Double>>> clusterInstances(final Instances instances) {
        Map<Integer, List<Pair<Instance, Double>>> clusteringResults = new HashMap<>(); // NOPMD

        // Create hierarchical clusterer and set its options.
        HierarchicalClustering.hierarchicalClusterer = new HierarchicalClusterer();
        HierarchicalClustering.hierarchicalClusterer.setDistanceFunction(this.distanceFunction);
        HierarchicalClustering.hierarchicalClusterer.setDistanceIsBranchLength(false);
        HierarchicalClustering.hierarchicalClusterer.setLinkType(this.linkage);

        // Find a "good" number of clusters by applying the clusterSelectionMethod.
        final ClustersSelector clusterSelection = new ClustersSelector(this.clusterSelectionMethod,
                HierarchicalClustering.hierarchicalClusterer, instances);
        clusteringResults = clusterSelection.findGoodClustering();

        // Write clusteringResults to a CSV file.
        final CSVSinkFilter csvFilter = new CSVSinkFilter();
        final Map<Double, List<Instance>> clusteringKVs = csvFilter.convertClusteringResultsToKVPair(clusteringResults);
        try {
            csvFilter.createCSVFromClusteringResult(this.outputPath, clusteringKVs);
        } catch (final IOException e) {
            HierarchicalClustering.LOGGER.error("Writing hierarchical clustering results to csv failed.", e);
        }

        HierarchicalClustering.LOGGER.info("Hierarchical clustering done.");
        return clusteringResults;
    }

    /**
     *
     * @param linkageType
     *            Type of linkage used in hierarchical clustering.
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
        default: // Complete linkage as default.
            this.linkage = new SelectedTag(1, HierarchicalClusterer.TAGS_LINK_TYPE);
            break;
        }
    }

    public DistanceFunction getDistanceFunction() {
        return this.distanceFunction;
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
            this.distanceFunction = new EuclideanDistance(); // Euclidean as default.
            break;
        }
    }
}
