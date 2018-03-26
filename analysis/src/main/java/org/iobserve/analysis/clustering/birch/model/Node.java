package org.iobserve.analysis.clustering.birch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;


public class Node extends AbstractNode {
	
	ArrayList<AbstractNode> entries;

	public Node() {
		this.cf = new ClusteringFeature(AbstractNode.DIMENSION);
		this.entries = new ArrayList<AbstractNode>();
	}
	
	public Node (AbstractNode node) {
		this.cf = new ClusteringFeature(AbstractNode.DIMENSION);
		this.cf.add(node.cf);
		this.entries = new ArrayList<AbstractNode>();
	}
	
	public Optional<AbstractNode> insert(ClusteringFeature cf) {
		this.cf.add(cf);
		
		TreeMap<Double, AbstractNode> ranking = new TreeMap<>();
		
		for (AbstractNode node : this.entries) {
		    ranking.put(node.cf.compare(cf), node);
		}
		
		AbstractNode nextLevel = ranking.firstEntry().getValue();
		Optional<AbstractNode> potentialChild = nextLevel.insert(cf);
		if(potentialChild.isPresent()) {
//			System.out.println("Got a split below...");
			Optional<AbstractNode>  potentialSplit = addEntry(nextLevel, potentialChild.get());
			return potentialSplit;
		}
		return Optional.empty();
	}

// origin: child node that has been split; split: the new child node that has split off from origin	
	private Optional<AbstractNode> addEntry(AbstractNode origin, AbstractNode split) {
		this.entries.add(split);
		///Split the leaf if size is too large
//		System.out.println("Do " + this.entries.size() + " go in " + NODE_SIZE_CONSTRAINT);
		if(this.entries.size() > NODE_SIZE_CONSTRAINT) {
//			System.out.println("No!, Splitting Node");
			return Optional.of(this.split());
		} else { //Merging refinement if the there is no further split propagation
//			System.out.println(this.toString());
//			System.out.println("Yes, refinement merge commencing...");
			ProximityMatrix pm = new ProximityMatrix(this.getEntries());
			if(!pm.isClosestPair(this.entries.indexOf(split), this.entries.indexOf(origin))) {
				if(this.entries.get(pm.closest1).refinementMerge(this.entries.get(pm.closest2)) == 1) {
					this.entries.remove(pm.closest2); //closest2 has been absorbed by closest1
				}

			}
		}	
		return Optional.empty();
	}
	
	///This merge is detailed in the step "A merging refinement"
	public int refinementMerge(AbstractNode child) {
		// The two nodes are too large to be merged
		if (child.space() < this.size()) {
			this.resplit(child);
			return 0;
		} else {
			this.entries.addAll(child.getChildren());
			this.cf.add(child.cf);
		}
		return 1;
	}
	
	private List<ClusteringFeature> getEntries() {
		ArrayList<ClusteringFeature> cfs = new ArrayList<ClusteringFeature>();
		for (int i=0; i<this.entries.size(); i++)
			cfs.add(this.entries.get(i).cf);
		return cfs;
	}
	
	private Node split() {
//		System.out.println("node is too large!");
		ProximityMatrix pm = new ProximityMatrix(this.getEntries());
		///cfs that are closer to farthest one move to the new leaf, the rest stays put
		Node second = new Node();
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
	
	public void resplit(AbstractNode child) {
		Node node = (Node) child;
		this.entries.addAll(node.entries);
		node.entries.clear();
		ProximityMatrix pm = new ProximityMatrix(this.getEntries());
		///cfs that are closer to farthest one move to the new leaf, the rest stays put
		for(int i=0; i<pm.n; i++) {
			if(pm.matrix[pm.farthest1][i] < pm.matrix[pm.farthest2][i]) {
				node.entries.add(this.entries.get(i));
			}
		}
		this.entries.removeAll(node.entries);
		if(this.space() < 0) {
			List<AbstractNode> spillover = new ArrayList<AbstractNode>();
			for(int i = NODE_SIZE_CONSTRAINT; i < this.size(); i++)
				spillover.add(this.entries.get(i));
			this.entries.removeAll(spillover);
			node.entries.addAll(spillover);
		}
		if(node.space() < 0) {
			List<AbstractNode> spillover = new ArrayList<AbstractNode>();
			for(int i = LEAF_SIZE_CONSTRAINT; i < node.size(); i++)
					spillover.add(node.entries.get(i));
			node.entries.removeAll(spillover);
			this.entries.addAll(spillover);
		}
		this.updateSum();
	}
	

	public void updateSum() {
		this.cf = sum();
	}

	public ClusteringFeature sum() {
		double[] ls = new double[AbstractNode.DIMENSION];
		double[] ss = new double[AbstractNode.DIMENSION];
		ClusteringFeature cf = new ClusteringFeature(0, ls, ss);
		for (AbstractNode ne : this.entries) {
			cf.add(ne.cf);
		}
		return cf;
	}

	
	
	public String toString(){
		String res = "N{" + this.entries.size() + ": " + this.cf.toString() +  "}";
		for(AbstractNode ne : this.entries)
			res += "  [" + ne.cf.toString() + "]  ";
		return res;
	}
	
	public List<AbstractNode> getChildren() {
		List<AbstractNode> ls =  new ArrayList<>();
		for (AbstractNode ne : this.entries)
			ls.add(ne);
		return ls;
	}

	public int space() {
		return NODE_SIZE_CONSTRAINT - this.size();
	}

	public int size() {
		return this.entries.size();
	}

	/**
	 * Removes last Entry from the list of nodes.
	 */
	public void removeLast() {
		this.entries.remove(this.entries.size() - 1);
	}

	public void removeFirst() {
		this.entries.remove(0);
	}
	
	@Override
	public Optional<AbstractNode> getNextLevel() {
		return Optional.of(this.entries.get(0));
	}

	public AbstractNode createChild() {
		this.entries.add(new Node());
		return this.entries.get(this.entries.size() - 1);
	}


	@Override
	public Optional<AbstractNode>  getChild(int i) {
		if(this.entries.size() > i)
			return Optional.of(this.entries.get(i));
		else return Optional.empty();
	}

	@Override
	public int getClosestChildIndex(ClusteringFeature cf) {
		TreeMap<Double, AbstractNode> ranking = new TreeMap<>();
		
		for (AbstractNode node : this.entries) {
		    ranking.put(node.cf.compare(cf), node);
		}
	
		//System.out.println("Number of ranked items: " + ranking.size());
	return this.entries.indexOf(ranking.firstEntry().getValue());
	}


}
