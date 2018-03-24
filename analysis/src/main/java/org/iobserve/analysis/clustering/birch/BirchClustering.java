package org.iobserve.analysis.clustering.birch;

import org.iobserve.analysis.clustering.filter.TBehaviorModelCreation;
import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.common.record.ISessionEvent;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;
import teetime.stage.basic.merger.Merger;
import teetime.stage.basic.merger.strategy.BlockingBusyWaitingRoundRobinMergerStrategy;
import teetime.stage.basic.merger.strategy.IMergerStrategy;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;
import weka.core.Instances;

public class BirchClustering extends CompositeStage {

    private final InputPort<Instances> inputPort;
    private final OutputPort<Instances> outputPort;
	
	public BirchClustering(final double leafThresholdValue, final int maxLeafSize, 
			final int maxNodeSize, final int maxLeafEntries) {
        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        final Distributor<Instances> distributor = new Distributor<>(strategy);
        final IMergerStrategy mergerStrategy = new BlockingBusyWaitingRoundRobinMergerStrategy();
        final Merger<Object> merger = new Merger<>(mergerStrategy);
        final BuildCFTree buildCFTree = new BuildCFTree(leafThresholdValue, maxLeafSize, maxNodeSize);
        final RebuildTree rebuildTree = new RebuildTree(maxLeafEntries);
        final ClusterOnTree clusterOnTree = new ClusterOnTree();
        final ClusterSelection clusterSelection = new ClusterSelection();
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
