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
package org.iobserve.analysis.behavior.clustering.birch.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Represents a Clustering Feature tree composed of AbstractNodes. Acts as a face for the package.
 * 
 * @author Melf Lorenzen
 *
 */
public class CFTree {

    private AbstractNode root;

    /**
     * Constructor for the CFTree class.
     * 
     * @param t
     *            the merge threshold for this tree
     * @param leafSizeConstraint
     *            the maximum number of entries in a leaf
     * @param nodeSizeConstraint
     *            the maximum number of entries in a node
     * @param dimension
     *            the dimension of the vectors in this tree
     * @param strategy
     *            the cluster comparison strategy
     */
    public CFTree(final double t, final int leafSizeConstraint, final int nodeSizeConstraint, final int dimension,
            final ICFComparisonStrategy strategy) {
        this.root = new Leaf(dimension);
        ClusteringFeature.setMetric(strategy);
        AbstractNode.dimension = dimension;
        AbstractNode.leafSizeConstraint = leafSizeConstraint;
        AbstractNode.mergeThreshold = t;
        AbstractNode.nodeSizeConstraint = nodeSizeConstraint;
    }

    CFTree(final double t, final int dimension) {
        this.root = new Leaf(dimension);
        AbstractNode.mergeThreshold = t;
    }

    /**
     * Method to insert a new clustering feature in to this tree.
     * 
     * @param cf
     *            the clustering feature to be inserted
     */
    public void insert(final ClusteringFeature cf) {
        final Optional<AbstractNode> split = this.root.insert(cf);
        if (split.isPresent()) {
            final Node newRoot = new Node();
            newRoot.addChild(this.root);
            newRoot.addChild(split.get());
            newRoot.sum();
            this.root = newRoot;
        }

    }

    /**
     * Moves a ClusteringFeature into a new Tree during the rebuilding phase.
     * 
     * @param cf
     *            ClusteringFeature being moved
     * @param newCurrentPath
     *            the default location in the new tree
     */
    private void move(final ClusteringFeature cf, final Path newCurrentPath) {

        // Find newClosestPath
        AbstractNode node = this.root;
        final Path ncp = new Path();
        int index = node.getClosestChildIndex(cf);

        while (index >= 0) {
            ncp.add(index, node);
            node = node.getChild(index).get();
            index = node.getClosestChildIndex(cf);
        }
        ncp.addFinalNode(node);

        final Leaf lf = ncp.getLeaf();
        final boolean canFit = lf.testInsert(cf);
        // Insert in ncp if further ahead and there is place
        if (ncp.compareTo(newCurrentPath) <= 0 && canFit) {
            lf.insert(cf);
            ncp.update();
        } else {
            newCurrentPath.getLeaf().insert(cf);
            newCurrentPath.update();
        }

    }

    /**
     * Rebuilds a new tree with higher merge threshold and moves the leaf entries over to the new
     * tree. The old tree will be empty after rebuilding
     * 
     * @param newThreshold
     *            the merge threshold of the new tree
     * @return the rebuilt tree
     */
    public CFTree rebuild(final double newThreshold) {
        final CFTree newTree = new CFTree(newThreshold, AbstractNode.dimension);

        final Path path = this.getLeftMostPath();
        Path ncp;

        while (!path.isEmpty()) {
            ncp = newTree.addPath(path);
            final Leaf leaf = path.getLeaf();

            for (final ClusteringFeature cf : leaf.getEntries()) {
                newTree.move(cf, ncp);
            }
            newTree.cleanUp(ncp);
            path.next();
        }
        newTree.updateLeafChain();
        return newTree;
    }

    /**
     * Removes empty nodes along the path.
     * 
     * @param path
     *            path that needs cleaning
     */
    private void cleanUp(final Path path) {
        int i = path.size() - 1;

        final Leaf leaf = path.getLeaf();
        boolean isEmpty = leaf.size() == 0;
        i--;

        while (i >= 0 && isEmpty) {
            final Node current = (Node) path.getNodes().get(i);
            current.removeLast();
            isEmpty = current.size() == 0;
            i--;
        }
    }

    /**
     * Adds the the path to the new Tree.
     * 
     * @param path
     *            the path to add
     */
    private Path addPath(final Path path) {
        final Path ncp = new Path();

        // Leaf at root
        if (path.size() == 0) {
            ncp.addFinalNode(this.root);
            return ncp;
        }

        // nicht leer => ist root eine Node? nein => zur node machen
        if (this.root.getChildren().size() == 0) {
            this.root = new Node();
        }

        Node node = (Node) this.root;
        for (int i = 0; i < path.size() - 1; i++) {
            if (node.size() <= path.getIndices().get(i)) {
                final Node add = new Node(path.getNodes().get(i + 1));
                ncp.add(node.size(), node);
                node.addChild(add);
                node = add;
            } else {
                ncp.add(path.getIndices().get(i), node);
                node = (Node) node.getChild(path.getIndices().get(i)).get();
            }
        }
        if (node.size() <= path.getIndices().get(path.size() - 1)) {
            final Leaf add = new Leaf(path.getLeaf());
            ncp.add(node.size(), node);
            node.addChild(add);
            ncp.addFinalNode(add);
        } else {
            final Leaf lf = (Leaf) node.getChild(path.getIndices().get(path.size() - 1)).get();
            ncp.add(node.size(), node);
            ncp.addFinalNode(lf);
        }

        return ncp;
    }

    Path getLeftMostPath() {
        final Path p = new Path();
        AbstractNode current = this.root;
        Optional<AbstractNode> next = current.getNextLevel();
        while (next.isPresent()) {
            p.add(0, current);
            current = next.get();
            next = current.getNextLevel();
        }
        p.addFinalNode(current);
        return p;
    }

    /**
     * Updates the references at leaf level to include their two neighboring leafs.
     */
    public void updateLeafChain() {
        final Path path = this.getLeftMostPath();
        Leaf last = path.getLeaf();
        Leaf current;
        path.moveRight();
        while (path.size() > 0) { // path size == 0 => no more leafs in tree
            current = path.getLeaf();
            last.setNext(current);
            current.setPrev(last);
            path.moveRight();
            last = current;
        }
    }

    /**
     * Returns all the trees leaf entries in a list.
     * 
     * @return list of the tree's leaf entries
     */
    public List<ClusteringFeature> getLeafEntries() {
        final List<ClusteringFeature> leafEntries = new ArrayList<>();

        Leaf lf = this.getLeftMostPath().getLeaf();
        leafEntries.addAll(lf.getEntries());

        while (lf.getNext() != null) {
            lf = lf.getNext();
            leafEntries.addAll(lf.getEntries());
        }

        return leafEntries;

    }

    /**
     * Calculates the tree average minimal distance between leaf entries.
     * 
     * @return the avg minimal leaf distance
     */
    public double getAvgMinimalLeafDistance() {
        Leaf lf = this.getLeftMostPath().getLeaf();
        int cnt = 1;
        double addedMinDistance = lf.getMinimalLeafDistance();
        while (lf.getNext() != null) {
            lf = lf.getNext();
            cnt++;
            addedMinDistance += lf.getMinimalLeafDistance();

        }
        return addedMinDistance / (cnt * 1.0);
    }

    /**
     * Returns the total number of leaf entries in this tree.
     * 
     * @return number of leaf entries
     */
    public int getNumberOfLeafEntries() {
        Leaf lf = this.getLeftMostPath().getLeaf();
        int cnt = lf.getSize();
        while (lf.getNext() != null) {
            lf = lf.getNext();
            cnt += lf.getSize();
        }
        return cnt;
    }

    public int getDimension() {
        return AbstractNode.dimension;
    }

    public int getNodeSizeConstraint() {
        return AbstractNode.nodeSizeConstraint;
    }

    public int getLeafSizeConstraint() {
        return AbstractNode.leafSizeConstraint;
    }

    public double getMergeThreshold() {
        return AbstractNode.mergeThreshold;
    }

    public AbstractNode getRootStringRepresentation() {
        return this.root;
    }

    @Override
    public String toString() {
        String treeRepresentation = "";
        final List<AbstractNode> currentLevel = new ArrayList<>();
        final List<AbstractNode> nextLevel = new ArrayList<>();
        currentLevel.add(this.root);
        while (!currentLevel.isEmpty()) {
            for (final AbstractNode n : currentLevel) {
                treeRepresentation += n.toString() + "  <>   ";
            }
            treeRepresentation += "\n";
            for (final AbstractNode n : currentLevel) {
                nextLevel.addAll(n.getChildren());
            }

            currentLevel.clear();
            currentLevel.addAll(nextLevel);
            nextLevel.clear();
        }
        return treeRepresentation;
    }

}
