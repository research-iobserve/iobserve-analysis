package org.iobserve.analysis.clustering.birch.model;

import java.util.ArrayList;
import java.util.Optional;

public class Path implements Comparable {
	
	//Leerer Pfad: Ist schon leaf
	
	ArrayList<Integer> indices;
	ArrayList<AbstractNode> nodes;
	
	///             W
	///            /
	///           L
	/// 
	/// => Pfad: indices = (0), nodes = &W, &L
	
	///             L
	///            
	///           
	/// 
	/// => Pfad: indices = , nodes = &L
	
	// n[k](i[k]) = n[k+1]
	
	
	@Override
	public int compareTo(Object arg) {
		Path path = (Path) arg;
		
		int n = Math.min(this.indices.size(), path.indices.size());
		int i = 0;
		int result = 0;
		while(i < n && result == 0) {
			result = this.indices.get(i).compareTo(path.indices.get(i));
			i++;
		}
		return result;
	}

	public Path(ArrayList<Integer> indices) {
		this.nodes = new ArrayList<AbstractNode> ();
		this.indices = indices;
	}
	
	public void add(Integer index, AbstractNode node) {
		this.nodes.add(node);
		this.indices.add(index);
	}
	
	public void addFinalNode(AbstractNode node) {
		this.nodes.add(node);
	}
	
	
	public int size() {
		return this.indices.size();
	}
	
	public boolean isEmpty() {
		return this.nodes.isEmpty();
	}
	
	public Leaf getLeaf() {
		return (Leaf) this.nodes.get(this.nodes.size() - 1);
	}
	
	public Path() {
		this.nodes = new ArrayList<AbstractNode> ();
		this.indices = new ArrayList<Integer> ();
	}

	private void deleteLastNode() {
		this.nodes.remove(this.nodes.size() - 1);
	}
	
	private void deleteLastIndex() {
		this.indices.remove(this.indices.size() - 1);
	}
	
	public void moveRight( ) {
		int i = this.nodes.size() - 1; // index of leaf level
		if(i > 0) { // Path is not empty and tree != leaf
			int nextIndex = 0;
			Optional<AbstractNode> next = Optional.empty();
			while(!next.isPresent() && i > 0) {
				nextIndex = this.indices.get(i - 1) + 1;
				this.deleteLastIndex();
				this.deleteLastNode();
				next = this.nodes.get(i - 1).getChild(nextIndex);
				i--;
			}
			if(next.isPresent()) {
				AbstractNode current = next.get();
				this.add(nextIndex, current);
				
				next = current.getNextLevel();
				while(next.isPresent()) {
					current = next.get();
					next = current.getNextLevel();
					this.add(0, current);
				}
			}
		}
	}
	
	/**
	 * Removes the last transferred leaf from the tree and sets the path to point at the next one.
	 */
	public void next() {
		int i = this.size() - 1; //last index; i+1: Number of nodes
		//System.out.println("Offending path:");
		//System.out.println(this.toString());
		// gerade geleertes Blatt löschen
		this.deleteLastNode();
		boolean isEmpty = false;
		if(i >= 0) {
			((Node) this.nodes.get(i)).removeFirst();
			isEmpty = this.nodes.get(i).size() == 0;
		} 

		
		//Remove empty nodes from tree and path
		while(i >= 1 && isEmpty) {
			//System.out.println("Reached loop for deleting intermediate nodes");
			Node current = (Node) this.nodes.get(i-1);
			current.removeFirst();
			this.indices.remove(i);
			isEmpty = current.entries.size() == 0;
			i--;
			this.deleteLastNode();
		}
		
		if(this.nodes.size() > 0) {
			assert(this.indices.size() > 0);
			int n = this.indices.size() - 1;
			this.indices.set(n, this.indices.get(n) + 1);
			Optional<AbstractNode> next = this.nodes.get(n).getChild(0);
			if(!next.isPresent()) {
				this.nodes.clear();
			}
			while(next.isPresent()) {
				this.indices.add(0);
				this.nodes.add(next.get());
				next = next.get().getChild(0);
			}
			this.indices.remove(this.indices.size() - 1);
		}
	}
	
	public String toString() {
		String res = "(";
		for (AbstractNode node : this.nodes) {
			res += node.cf.toString();
			res += ",";
		}
		res+= ") \n(";
		for (Integer i : this.indices) {
			res += i;
			res += ",";
		}
		res+= ") \n" ;
		return res;

	}

	public void update() {
		for(int i = this.nodes.size() - 1; i >=0; i--)
			this.nodes.get(i).updateSum();
	}
	
}
