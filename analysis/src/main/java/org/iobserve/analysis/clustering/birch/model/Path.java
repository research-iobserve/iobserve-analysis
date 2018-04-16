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
package org.iobserve.analysis.clustering.birch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The path is sequence of nodes in a CFTree from
 * root to leaf.
 * @author Melf Lorenzen
 *
 */
class Path implements Comparable<Path> {
	
	private List<Integer> indices;
	private List<AbstractNode> nodes;
	
	///             R
	///            /
	///           N
	///            \
	///             L
	/// => Pfad: indices = (0,1), nodes = &R, &N, &L
	
	///             L
	///            
	///           
	/// 
	/// => Pfad: indices = , nodes = &L
	// n[k](i[k]) = n[k+1]
	
	Path() {
		this.nodes = new ArrayList<AbstractNode>();
		this.indices = new ArrayList<Integer>();
	}
	
	Path(final List<Integer> indices) {
		this.nodes = new ArrayList<AbstractNode>();
		this.indices = indices;
	}
	
	@Override
	public int compareTo(final Path path) {
		
		final int n = Math.min(this.getIndices().size(), path.getIndices().size());
		int i = 0;
		int result = 0;
		while (i < n && result == 0) {
			result = this.getIndices().get(i).compareTo(path.getIndices().get(i));
			i++;
		}
		return result;
	}


	
	void add(final Integer index, final AbstractNode node) {
		this.nodes.add(node);
		this.getIndices().add(index);
	}
	
	void addFinalNode(final AbstractNode node) {
		this.nodes.add(node);
	}
	
	
	int size() {
		return this.getIndices().size();
	}
	
	boolean isEmpty() {
		return this.nodes.isEmpty();
	}
	
	Leaf getLeaf() {
		return (Leaf) this.nodes.get(this.nodes.size() - 1);
	}

	private void deleteLastNode() {
		this.nodes.remove(this.nodes.size() - 1);
	}
	
	private void deleteLastIndex() {
		this.getIndices().remove(this.getIndices().size() - 1);
	}
	
	void moveRight() {
		int i = this.nodes.size() - 1; // index of leaf level
		if (i > 0) { // Path is not empty and tree != leaf
			int nextIndex = 0;
			Optional<AbstractNode> next = Optional.empty();
			while (!next.isPresent() && i > 0) {
				nextIndex = this.getIndices().get(i - 1) + 1;
				this.deleteLastIndex();
				this.deleteLastNode();
				next = this.nodes.get(i - 1).getChild(nextIndex);
				i--;
			}
			if (next.isPresent()) {
				AbstractNode current = next.get();
				this.add(nextIndex, current);
				
				next = current.getNextLevel();
				while (next.isPresent()) {
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
	void next() {
		int i = this.size() - 1; //last index; i+1: Number of nodes
		this.deleteLastNode();
		boolean isEmpty = false;
		if (i >= 0) {
			((Node) this.nodes.get(i)).removeFirst();
			isEmpty = this.nodes.get(i).size() == 0;
		} 


		//Remove empty nodes from tree and path
		while (i >= 1 && isEmpty) {
			final Node current = (Node) this.nodes.get(i - 1);
			current.removeFirst();
			this.getIndices().remove(i);
			isEmpty = current.size() == 0;
			i--;
			this.deleteLastNode();
		}
		
		if (this.nodes.size() > 0) {
			final int n = this.getIndices().size() - 1;
			this.getIndices().set(n, this.getIndices().get(n) + 1);
			Optional<AbstractNode> next = this.nodes.get(n).getChild(0);
			if (!next.isPresent()) {
				this.nodes.clear();
			}
			while (next.isPresent()) {
				this.getIndices().add(0);
				this.nodes.add(next.get());
				next = next.get().getChild(0);
			}
			this.getIndices().remove(this.getIndices().size() - 1);
		}
	}
	
	@Override
	public String toString() {
		String res = "(";
		for (AbstractNode node : this.nodes) {
			res += node.getCF().toString();
			res += ",";
		}
		res += ") \n(";
		for (Integer i : this.getIndices()) {
			res += i;
			res += ",";
		}
		res += ") \n";
		return res;

	}

	void update() {
		for (int i = this.nodes.size() - 1; i >= 0; i--) {
			this.nodes.get(i).updateSum();
		}
	}

	
	
	List<AbstractNode> getNodes() {
		return nodes;
	}

	List<Integer> getIndices() {
		return indices;
	}

	
}
