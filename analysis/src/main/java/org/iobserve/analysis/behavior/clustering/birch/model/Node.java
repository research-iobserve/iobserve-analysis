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
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

/**
 * The node class represent the non-leaf nodes of a CFTree.
 * 
 * @author Melf Lorenzen
 *
 */
public class Node extends AbstractNode {
    private ClusteringFeature clusteringFeature;
    private final List<AbstractNode> entries;

    Node() {
        this.clusteringFeature = new ClusteringFeature(AbstractNode.dimension);
        this.entries = new ArrayList<>();
    }

    Node(final AbstractNode node) {
        this.clusteringFeature = new ClusteringFeature(AbstractNode.dimension);
        this.clusteringFeature.add(node.getCF());
        this.entries = new ArrayList<>();
    }

    @Override
    Optional<AbstractNode> insert(final ClusteringFeature cf) {
        this.clusteringFeature.add(cf);

        final NavigableMap<Double, AbstractNode> ranking = new TreeMap<>();

        for (final AbstractNode node : this.entries) {
            ranking.put(node.getCF().compare(cf), node);
        }

        final AbstractNode nextLevel = ranking.firstEntry().getValue();
        final Optional<AbstractNode> potentialChild = nextLevel.insert(cf);
        if (potentialChild.isPresent()) {
            return this.addEntry(nextLevel, potentialChild.get());
        }
        return Optional.empty();
    }

    /**
     * Adds new node entry split while checking for.
     * 
     * @param origin
     *            child node that has been split
     * @param split
     *            the new child node that has split off from origin
     * @return
     */
    private Optional<AbstractNode> addEntry(final AbstractNode origin, final AbstractNode split) {
        this.entries.add(split);

        if (this.entries.size() > AbstractNode.nodeSizeConstraint) {
            return Optional.of(this.split());
        } else { // Merging refinement if the there is no further split propagation

            final ProximityMatrix pm = new ProximityMatrix(this.getEntries());
            if (!pm.isClosestPair(this.entries.indexOf(split), this.entries.indexOf(origin))) {
                if (this.entries.get(pm.getClosestPair()[0])
                        .refinementMerge(this.entries.get(pm.getClosestPair()[1]))) {
                    this.entries.remove(pm.getClosestPair()[1]); // closest2 has been absorbed by
                                                                 // closest1
                }

            }
        }
        return Optional.empty();
    }

    /// This merge is detailed in the step "A merging refinement"
    @Override
    boolean refinementMerge(final AbstractNode child) {
        // The two nodes are too large to be merged
        if (child.space() < this.size()) {
            this.resplit(child);
            return false;
        } else {
            this.entries.addAll(child.getChildren());
            this.clusteringFeature.add(child.getCF());
        }
        return true;
    }

    private List<ClusteringFeature> getEntries() {
        final List<ClusteringFeature> cfs = new ArrayList<>();
        for (int i = 0; i < this.entries.size(); i++) {
            cfs.add(this.entries.get(i).getCF());
        }
        return cfs;
    }

    private Node split() {
        final ProximityMatrix pm = new ProximityMatrix(this.getEntries());
        /// cfs that are closer to farthest one move to the new leaf, the rest stays put
        final Node second = new Node();
        for (int i = 0; i < this.entries.size(); i++) {
            if (pm.getMatrix()[pm.getFarthestPair()[0]][i] < pm.getMatrix()[pm.getFarthestPair()[1]][i]) {
                second.entries.add(this.entries.get(i));
            }
        }
        this.entries.removeAll(second.entries);
        this.updateSum();
        second.updateSum();
        return second;
    }

    @Override
    void resplit(final AbstractNode child) {
        final Node node = (Node) child;
        this.entries.addAll(node.entries);
        node.entries.clear();
        final ProximityMatrix pm = new ProximityMatrix(this.getEntries());
        /// cfs that are closer to farthest one move to the new leaf, the rest stays put
        for (int i = 0; i < this.getEntries().size(); i++) {
            if (pm.getMatrix()[pm.getFarthestPair()[0]][i] < pm.getMatrix()[pm.getFarthestPair()[1]][i]) {
                node.entries.add(this.entries.get(i));
            }
        }
        this.entries.removeAll(node.entries);
        if (this.space() < 0) {
            final List<AbstractNode> spillover = new ArrayList<>();
            for (int i = AbstractNode.nodeSizeConstraint; i < this.size(); i++) {
                spillover.add(this.entries.get(i));
            }
            this.entries.removeAll(spillover);
            node.entries.addAll(spillover);
        }
        if (node.space() < 0) {
            final List<AbstractNode> spillover = new ArrayList<>();
            for (int i = AbstractNode.nodeSizeConstraint; i < node.size(); i++) {
                spillover.add(node.entries.get(i));
            }
            node.entries.removeAll(spillover);
            this.entries.addAll(spillover);
        }
        this.updateSum();
    }

    @Override
    void updateSum() {
        this.clusteringFeature = this.sum();
    }

    ClusteringFeature sum() {
        final double[] ls = new double[AbstractNode.dimension];
        final double[] ss = new double[AbstractNode.dimension];
        final ClusteringFeature cf = new ClusteringFeature(0, ls, ss);
        for (final AbstractNode ne : this.entries) {
            cf.add(ne.getCF());
        }
        return cf;
    }

    @Override
    public String toString() {
        String res = "N{" + this.entries.size() + ": " + this.clusteringFeature.toString() + "}";
        for (final AbstractNode ne : this.entries) {
            res += "  [" + ne.getCF().toString() + "]  ";
        }
        return res;
    }

    @Override
    List<AbstractNode> getChildren() {
        final List<AbstractNode> ls = new ArrayList<>();
        for (final AbstractNode ne : this.entries) {
            ls.add(ne);
        }
        return ls;
    }

    @Override
    int space() {
        return AbstractNode.nodeSizeConstraint - this.size();
    }

    @Override
    int size() {
        return this.entries.size();
    }

    /**
     * Removes last Entry from the list of nodes.
     */
    void removeLast() {
        this.entries.remove(this.entries.size() - 1);
    }

    void removeFirst() {
        this.entries.remove(0);
    }

    @Override
    public Optional<AbstractNode> getNextLevel() {
        return Optional.of(this.entries.get(0));
    }

    AbstractNode createChild() {
        this.entries.add(new Node());
        return this.entries.get(this.entries.size() - 1);
    }

    void addChild(final AbstractNode node) {
        this.entries.add(node);
    }

    @Override
    Optional<AbstractNode> getChild(final int i) {
        if (this.entries.size() > i) {
            return Optional.of(this.entries.get(i));
        } else {
            return Optional.empty();
        }
    }

    @Override
    int getClosestChildIndex(final ClusteringFeature cf) {
        final NavigableMap<Double, AbstractNode> ranking = new TreeMap<>();

        for (final AbstractNode node : this.entries) {
            ranking.put(node.getCF().compare(cf), node);
        }

        return this.entries.indexOf(ranking.firstEntry().getValue());
    }

    @Override
    ClusteringFeature getCF() {
        return this.clusteringFeature;
    }

}
