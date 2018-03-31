package org.iobserve.analysis.clustering.birch;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.clustering.birch.model.CFTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RebuildTree extends AbstractConsumerStage<CFTree> {
	private int maxLeafEntries;
	
    private final OutputPort<CFTree> outputPort = this.createOutputPort();
    	
    private static final Logger LOGGER = LoggerFactory.getLogger(RebuildTree.class);
    
	public RebuildTree(int maxLeafEntries) {
		super();
		this.maxLeafEntries = maxLeafEntries;
	}
    
	@Override
	protected void execute(CFTree tree) throws Exception {
		RebuildTree.LOGGER.debug("Following tree up for potential rebuilding");
		RebuildTree.LOGGER.debug("Dimension: " + tree.getDimension());
		RebuildTree.LOGGER.debug("Max node entries: " + tree.getNodeSizeConstraint());
		RebuildTree.LOGGER.debug("Max leaf entries: " + tree.getLeafSizeConstraint());
		RebuildTree.LOGGER.debug("Merge threshold: " + tree.getMergeThreshold());
		RebuildTree.LOGGER.debug("Root: " + tree.getRootStringRepresentation());
		RebuildTree.LOGGER.debug("Leaf entries: " + tree.getNumberOfLeafEntries());
		if(tree.getNumberOfLeafEntries() > maxLeafEntries) {
			RebuildTree.LOGGER.debug(tree.getNumberOfLeafEntries() + " entries. Only " + maxLeafEntries + "allowed!");
			double newTheshold = Math.max(tree.getAvgMinimalLeafDistance(), tree.getMergeThreshold() * 1.10);
			RebuildTree.LOGGER.debug("Rebuilding tree with threshold = " + newTheshold);
            CFTree newTree = tree.rebuild(newTheshold);
			//RebuildTree.LOGGER.debug("Rebuilt tree = " + newTree.toString());
			this.execute(newTree);
		} else {
			this.outputPort.send(tree);
		}
	}

	public OutputPort<CFTree> getOutputPort() {
		return outputPort;
	}


	
}
