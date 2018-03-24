package org.iobserve.analysis.clustering.birch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Leaf extends AbstractNode {

	ArrayList<ClusteringFeature> entries;
	
	private Leaf prev;

	private Leaf next;
	
	public int getSize() {
		return this.entries.size();
	}
	
	public Leaf getPrev() {
		return prev;
	}

	public void setPrev(Leaf prev) {
		this.prev = prev;
	}

	public Leaf getNext() {
		return next;
	}

	public void setNext(Leaf next) {
		this.next = next;
	}

	public Leaf(int n) {
		this.cf = new ClusteringFeature(n);
		this.entries = new ArrayList<ClusteringFeature>();
	}
	
	public Leaf() {
		this.cf = new ClusteringFeature(AbstractNode.DIMENSION);
		this.entries = new ArrayList<ClusteringFeature>();
	}
	
	public Leaf(Leaf lf) {
		this.cf = new ClusteringFeature(AbstractNode.DIMENSION);
		this.entries = new ArrayList<ClusteringFeature>();
		this.cf.add(lf.cf);
	}
	
	private Leaf split() {
		System.out.println("leaf is too large!");
		ProximityMatrix pm = new ProximityMatrix(this.entries);
		///cfs that are closer to farthest one move to the new leaf, the rest stays put
		Leaf second = new Leaf();
		for(int i=0; i<pm.n; i++) {
			if(pm.matrix[pm.farthest1][i] < pm.matrix[pm.farthest2][i]) {
				second.entries.add(this.entries.get(i));
			}
		}
		this.entries.removeAll(second.entries);
		this.updateSum();
		second.updateSum();
		return second;
	}
	
	public Optional<AbstractNode> insert(ClusteringFeature cf) {
		
		
		TreeMap<Double, ClusteringFeature> ranking = new TreeMap<Double, ClusteringFeature>();
		
		this.cf.add(cf);
		
		for (ClusteringFeature entry : this.entries) {
		    ranking.put(entry.compare(cf), entry);
//		    System.out.println(entry.compare(cf));
		}
		
		if(!ranking.isEmpty()) {		
			ClusteringFeature closest = ranking.firstEntry().getValue();
			ClusteringFeature merge = new ClusteringFeature(closest, cf);
//			System.out.println("merge: " + merge.toString());
//			System.out.println("Merge Diameter: " + merge.getDiameter());
			///Merge the Clustering Features if the threshold requirements remains satisfied
			if(merge.isBelowThreshold(MERGE_THRESHOLD)) {
//				System.out.println("Merging");
				closest.add(cf);
			}
			else {
				// If merge is not possible, add the Clustering Feature to the leaf
//					System.out.println("Adding because MT was exceeded");
					return(this.addEntry(cf));
			}
		}
		else {
		// If Leaf is empty, simply add
//			System.out.println("Leaf empty: Adding");
			return(this.addEntry(cf));
		}
		
		return Optional.empty();
	}
	
	private Optional<AbstractNode> addEntry(ClusteringFeature cf) {
		this.entries.add(cf);
			
		///Split the leaf if size is too large
		if(this.entries.size() > LEAF_SIZE_CONSTRAINT) {
			return Optional.of(this.split());
		}
		return Optional.empty();
	}
	
	public String toString(){
		String res = "L{" + this.entries.size() + ": " + this.cf.toString() + "}";
		for(ClusteringFeature cf : this.entries)
			res += "  [" + cf.toString() + "]  ";
		return res;
	}

	
	public void updateSum() {
		this.cf = this.sum();
	}
	
	/* Sums up the node's members */
	public ClusteringFeature sum() {
		double[] ls = new double[this.cf.linearSum.length];
		double[] ss = new double[this.cf.linearSum.length];
		ClusteringFeature ncf = new ClusteringFeature(0, ls, ss);
		for (ClusteringFeature cf : this.entries) {
			ncf.add(cf);
		}
		return ncf;
	}


	public List<AbstractNode> getChildren() {
		return new ArrayList<AbstractNode>();
	}


	public int space() {
		return LEAF_SIZE_CONSTRAINT - this.size();
	}


	public int size() {
		return this.entries.size();
	}


	public void resplit(AbstractNode  child) {
		Leaf leaf = (Leaf) child;
		this.entries.addAll(leaf.entries);
		leaf.entries.clear();
		ProximityMatrix pm = new ProximityMatrix(this.entries);
		///cfs that are closer to farthest one move to the new leaf, the rest stays put
		for(int i=0; i<pm.n; i++) {
			if(pm.matrix[pm.farthest1][i] < pm.matrix[pm.farthest2][i]) {
				leaf.entries.add(this.entries.get(i));
			}
		}
		this.entries.removeAll(leaf.entries);
		if(this.space() < 0) {
			List<ClusteringFeature> spillover = new ArrayList<ClusteringFeature>();
			for(int i = LEAF_SIZE_CONSTRAINT; i < this.size(); i++)
					spillover.add(this.entries.get(i));
			this.entries.removeAll(spillover);
			leaf.entries.addAll(spillover);
		}
		if(leaf.space() < 0) {
			List<ClusteringFeature> spillover = new ArrayList<ClusteringFeature>();
			for(int i = LEAF_SIZE_CONSTRAINT; i < leaf.size(); i++)
					spillover.add(leaf.entries.get(i));
			leaf.entries.removeAll(spillover);
			this.entries.addAll(spillover);
		}
		this.updateSum();
	}


	@Override
	public int refinementMerge(AbstractNode child) {
		// The two nodes are too large to be merged
//		System.out.println("Refinement merge on...");
//		System.out.println(this.toString());
//		System.out.println(child.toString());
		
		if (child.space() < this.size()) {
			this.resplit(child);
			return 0;
		} else {
			this.entries.addAll(((Leaf) child).entries);
			this.cf.add(child.cf);
		}
		return 1;
	}


	@Override
	public Optional<AbstractNode> getNextLevel() {
		return Optional.empty();
	}


	@Override
	public Optional<AbstractNode> getChild(int i) {
		return Optional.empty();
	}


	@Override
	public int getClosestChildIndex(ClusteringFeature cf) {
		return -1;
	}

	public void removeFirst() {
		this.entries.remove(0);
	}

	public boolean testInsert(ClusteringFeature cf) {
		TreeMap<Double, ClusteringFeature> ranking = new TreeMap<Double, ClusteringFeature>();
			
		for (ClusteringFeature entry : this.entries) {
		    ranking.put(entry.compare(cf), entry);
//		    System.out.println(entry.compare(cf));
		}
		
		if(!ranking.isEmpty()) {		
			ClusteringFeature closest = ranking.firstEntry().getValue();
			ClusteringFeature merge = new ClusteringFeature(closest, cf);
//			System.out.println("merge: " + merge.toString());
//			System.out.println("Merge Diameter: " + merge.getDiameter());
			///Merge the Clustering Features if the threshold requirements remains satisfied
			if(merge.isBelowThreshold(MERGE_THRESHOLD)) {
				return true;
			}
			else {
				// If merge is not possible, add the Clustering Feature to the leaf
					//System.out.println("Adding because MT was exceeded");
					return this.entries.size() < LEAF_SIZE_CONSTRAINT;
			}
		}
		else {
		// If Leaf is empty, simply add
			return(true);
		}

	}

	public double getMinimalLeafDistance() {
		ProximityMatrix pm = new ProximityMatrix(this.entries);
		return pm.matrix[pm.closest1][pm.closest2];
	}

}

