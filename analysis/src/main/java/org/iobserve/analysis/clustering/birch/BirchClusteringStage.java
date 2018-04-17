/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.clustering.birch;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;
import teetime.stage.basic.merger.Merger;
import teetime.stage.basic.merger.strategy.BlockingBusyWaitingRoundRobinMergerStrategy;
import teetime.stage.basic.merger.strategy.IMergerStrategy;

import org.iobserve.analysis.clustering.birch.model.ICFComparisonStrategy;

import weka.core.Instances;

/**
 * Composite Stage consisting of the stages unique to the birch clustering algorithm.
 *
 * @author Melf Lorenzen
 */
public class BirchClusteringStage extends CompositeStage {

    private final InputPort<Instances> inputPort;
    private final OutputPort<Instances> outputPort;

    /**
     * Constructor for the BirchClustering composite stage.
     * 
     * @param leafThresholdValue
     *            the merge threshold for the underlying cf tree
     * @param maxLeafSize
     *            the maximum number of entries in a leaf
     * @param maxNodeSize
     *            the maximum number of entries in a node
     * @param maxLeafEntries
     *            the maximum number of leaf entries in the underlying cf tree
     * @param expectedNumberOfClusters
     *            the expected number of clusters in the data
     * @param useClusterNumberMetric
     *            whether to use the expected number or number calculated by the cluster number
     *            metric
     * @param clusterComparisonStrategy
     *            the cluster comparison strategy
     * @param evalStrategy
     *            the strategy for the l-method evaluation graph
     */
    public BirchClusteringStage(final double leafThresholdValue, final int maxLeafSize, final int maxNodeSize,
            final int maxLeafEntries, final int expectedNumberOfClusters, final boolean useClusterNumberMetric,
            final ICFComparisonStrategy clusterComparisonStrategy, final ILMethodEvalStrategy evalStrategy) {
        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        final Distributor<Instances> distributor = new Distributor<>(strategy);
        final IMergerStrategy mergerStrategy = new BlockingBusyWaitingRoundRobinMergerStrategy();
        final Merger<Object> merger = new Merger<>(mergerStrategy);
        final BuildCFTreeStage buildCFTree = new BuildCFTreeStage(leafThresholdValue, maxLeafSize, maxNodeSize,
                clusterComparisonStrategy);
        final RebuildTree rebuildTree = new RebuildTree(maxLeafEntries);
        final ClusterOnTree clusterOnTree = new ClusterOnTree();
        final ClusterSelection clusterSelection = new ClusterSelection(expectedNumberOfClusters, useClusterNumberMetric,
                evalStrategy);
        final Refinement refinement = new Refinement();

        this.inputPort = distributor.getInputPort();
        this.outputPort = refinement.getOutputPort();

        this.connectPorts(distributor.getNewOutputPort(), buildCFTree.getInputPort());
        this.connectPorts(buildCFTree.getOutputPort(), rebuildTree.getInputPort());
        this.connectPorts(rebuildTree.getOutputPort(), clusterOnTree.getInputPort());
        this.connectPorts(clusterOnTree.getOutputPort(), clusterSelection.getInputPort());
        this.connectPorts(clusterSelection.getOutputPort(), merger.getNewInputPort());
        this.connectPorts(distributor.getNewOutputPort(), merger.getNewInputPort());
        this.connectPorts(merger.getOutputPort(), refinement.getInputPort());
    }

    public InputPort<Instances> getInputPort() {
        return this.inputPort;
    }

    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }
}
