package org.iobserve.analysis.clustering.birch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CFTree {
	
	public AbstractNode root;
	
	
	public CFTree(double t, int leafSizeConstraint,
			int nodeSizeConstraint, int dimension) {
		this.root = new Leaf(dimension);
		AbstractNode.DIMENSION = dimension;
		AbstractNode.LEAF_SIZE_CONSTRAINT = leafSizeConstraint;
		AbstractNode.MERGE_THRESHOLD = t;
		AbstractNode.NODE_SIZE_CONSTRAINT = nodeSizeConstraint;
	}

	public void insert(ClusteringFeature cf) {
		///Egal ob Leaf oder Node: Nach Insert müssen möglicherweise zwei Nodes in einer Node zusammengefasst werden
		Optional<AbstractNode> split = this.root.insert(cf);
		if(split.isPresent()) {
			System.out.println("Split reached root");
			Node newRoot = new Node();
			newRoot.entries.add(this.root);
			newRoot.entries.add(split.get());
			newRoot.cf = newRoot.sum();
			this.root = newRoot;
		}
		
	}
	
	/**
	 * Moves a ClusteringFeature into a new Tree during the rebuilding phase.
	 * @param cf ClusteringFeature being moved
	 * @param newCurrentPath the default location in the new tree
	 */
	private void move(ClusteringFeature cf, Path newCurrentPath) {
		
		//Find newClosestPath
		AbstractNode node = this.root;
		Path ncp = new Path();
		int index = node.getClosestChildIndex(cf);
		
		while(index >= 0) {
//			System.out.println(this.toString());
			ncp.add(index, node);
			node = node.getChild(index).get();
			index = node.getClosestChildIndex(cf); 
		}
		ncp.addFinalNode(node);
		
		Leaf lf = ncp.getLeaf();
		boolean canFit = lf.testInsert(cf);
//		System.out.println("Can fit? " + canFit);
//		System.out.println("newCurrentPath:");
//		System.out.println(newCurrentPath.toString());
//		System.out.println("newClosestPath:");
//		System.out.println(ncp.toString());
		//Insert in ncp if further ahead and there is place
		if(ncp.compareTo(newCurrentPath) < 0 && canFit) {
			//System.out.println("newClosestPath comes first:");
			//assert(!lf.insert(cf).isPresent());
			lf.insert(cf);
			ncp.update();
		} else {
			//System.out.println("Inserting in newCurrentPath ");
			//assert(!newCurrentPath.getLeaf().insert(cf).isPresent());
			newCurrentPath.getLeaf().insert(cf);
			newCurrentPath.update();
			
		}
		
	}
	
	public CFTree rebuild(double newThreshold) {
		CFTree newTree = new CFTree(newThreshold, AbstractNode.LEAF_SIZE_CONSTRAINT, AbstractNode.NODE_SIZE_CONSTRAINT, AbstractNode.DIMENSION);
		
		Path path = this.getLeftMostPath();
		Path ncp;
		
		while(!path.isEmpty()) {
//			System.out.println("addPath Stuff...");
			ncp = newTree.addPath(path);
//			System.out.println("Path, ncp:");
//			System.out.println(path.toString());
//			System.out.println(ncp.toString());
			Leaf leaf = path.getLeaf();
			
//			System.out.println("Tree after adding Path");
//			System.out.println(newTree.toString());
			
			for(ClusteringFeature cf : leaf.entries) {
//				System.out.println("moving");				
				newTree.move(cf, ncp);
			}
//			System.out.println("Tree after moving entries");
//			System.out.println(newTree.toString());
			
			
			newTree.cleanUp(ncp);
			
//			System.out.println("Tree after moving and cleanup:");
//			System.out.println(newTree.toString());
			path.next();
//			System.out.println("Old Tree after navigating to next path");
//			System.out.println(this.toString());
			
		}
		
		newTree.updateLeafChain();
		return newTree;
	}
	
	
	/** Removes empty nodes along the path
	 * @param path
	 */
	private void cleanUp(Path path) {
		int i = path.size() - 1;
		
////		System.out.println("ncp before getting leaf:");
//		System.out.println(path.toString());
		Leaf leaf = path.getLeaf();
		boolean isEmpty = leaf.size() == 0;
		i--;
		
		while(i >= 0 && isEmpty) {
			Node current = (Node) path.nodes.get(i);
			current.removeLast();
			isEmpty = current.entries.size() == 0;
			i--;
		}	
	}

	/** Adds the the path to the new Tree
	 * @param path the path to add
	 */
	private Path addPath(Path path) {
	Path ncp = new Path();
	
	//Leaf at root
	if (path.size() == 0) {
		System.out.println("Spezialfall: Tree = Leaf");
		ncp.addFinalNode(this.root);
		return ncp;
	}
	
	// nicht leer => ist root eine Node? nein => zur node machen
	if(this.root.getChildren().size() == 0) {
		this.root = new Node();
	}
	
	Node node = (Node) this.root;
	//Schleife: Ist aktueller Index in aktueller Node vorhanden => ja, runtergehen, nein => erzeugen, runtergehen
	for(int i = 0; i < path.size() - 1; i++) {
		if(node.size() <= path.indices.get(i)) {
			Node add = new Node(path.nodes.get(i+1));
			ncp.add(node.size(), node);
			node.entries.add(add);
			node = add;
		} else {
//			System.out.println("Index: " + i);
//			System.out.println("Pfad: " + path.toString());
//			System.out.println(path.indices.get(i));
			ncp.add(path.indices.get(i), node);
			node = (Node) node.getChild(path.indices.get(i)).get();
		} 
		// Spezialfall Blatt
	}
	//System.out.println("Node size " + node.size() + " <= " + path.indices);
	if(node.size() <= path.indices.get(path.size() - 1)) {	
//		System.out.println("new leaf");
		Leaf add = new Leaf(path.getLeaf());
		ncp.add(node.size(), node);
		node.entries.add(add);
		ncp.addFinalNode(add);
	} else {
//		System.out.println("Index: " + (path.size() - 1));
//		System.out.println("Pfad: " + path.toString());
//		System.out.println(path.indices.get(path.size() - 1));
		Leaf lf = (Leaf) node.getChild(path.indices.get(path.size() - 1)).get();
		ncp.add(node.size(), node);
		ncp.addFinalNode(lf);
	}
	
	return ncp;
	}

	public Path getLeftMostPath() {
		// Pfad leer = Root = Blatt
		Path p = new Path();
		AbstractNode current = this.root;
		Optional<AbstractNode> next = current.getNextLevel();
		while(next.isPresent()) {
			p.add(0, current);
			current = next.get();
			next = current.getNextLevel();
		}
		p.addFinalNode(current);
		return p;
	}
	

	/** Updates the references at leaf level to 
	 * include their two neighboring leafs
	 */
	public void updateLeafChain() {
		Path path = this.getLeftMostPath();
		Leaf last = path.getLeaf();
		Leaf current;
		path.moveRight();
		int i = 0;
		while(path.size() > 0) { // path size == 0 => no more leafs in tree
			//System.out.println("Leaf Chain size "  + i);
			current = path.getLeaf();
			last.setNext(current);
			current.setPrev(last);
			path.moveRight();
			last = current;
			i++;
		}
	}
	
	public List<ClusteringFeature> getLeafEntries() {
		List<ClusteringFeature> leafEntries = new ArrayList<ClusteringFeature>();
		
		Leaf lf = this.getLeftMostPath().getLeaf();
		leafEntries.addAll(lf.entries);
		
		while(lf.getNext() != null) {
			lf = lf.getNext();
			leafEntries.addAll(lf.entries);
		}
		
		
		return leafEntries;
		
	}
	
	public double getAvgMinimalLeafDistance() {
		Leaf lf = this.getLeftMostPath().getLeaf();
		int cnt = 1;
		double addedMinDistance = lf.getMinimalLeafDistance();
		while(lf.getNext() != null) {
			lf = lf.getNext();
			cnt ++;
			addedMinDistance += lf.getMinimalLeafDistance();
			
		}
		return addedMinDistance / (cnt * 1.0);
	}
	
	public int getNumberOfLeafEntries() {
		Leaf lf = this.getLeftMostPath().getLeaf();
		int cnt = lf.getSize();
		while(lf.getNext() != null) {
			lf = lf.getNext();
			cnt += lf.getSize();
		}
		return cnt;
	}
	
	public String toString() {
		String treeRepresentation = "";
		//ArrayList<ICFNode> allNodes = new ArrayList<ICFNode> ();
		ArrayList<AbstractNode> currentLevel = new ArrayList<> ();
		ArrayList<AbstractNode> nextLevel = new ArrayList<> ();
		currentLevel.add(this.root);
		while(!currentLevel.isEmpty()) {
			for(AbstractNode n : currentLevel)
				treeRepresentation += n.toString() + "  <>   ";
			treeRepresentation += "\n";
			for(AbstractNode n : currentLevel) {
				nextLevel.addAll(n.getChildren()); 
			}
			
			currentLevel.clear();
			currentLevel.addAll(nextLevel);
			nextLevel.clear();
		}
		return treeRepresentation;
	}

	
}
