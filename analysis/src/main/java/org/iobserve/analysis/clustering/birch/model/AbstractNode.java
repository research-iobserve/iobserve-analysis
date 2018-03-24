package org.iobserve.analysis.clustering.birch.model;

import java.util.List;
import java.util.Optional;

public abstract class AbstractNode {
	static public int DIMENSION;
	static public int NODE_SIZE_CONSTRAINT;
	static public int LEAF_SIZE_CONSTRAINT;
	public static double MERGE_THRESHOLD;
	
	public ClusteringFeature cf;
	
	public abstract Optional<AbstractNode> insert(ClusteringFeature cf);
	
	public abstract List<AbstractNode> getChildren();
	
	public abstract int space();
	
	public abstract int size();

	public abstract void resplit(AbstractNode child);

	public abstract int refinementMerge(AbstractNode child);

	public abstract Optional<AbstractNode> getNextLevel();
	
	public abstract Optional<AbstractNode> getChild(int i);
	
	public abstract int getClosestChildIndex(ClusteringFeature cf);
	
	public abstract void updateSum();
}
