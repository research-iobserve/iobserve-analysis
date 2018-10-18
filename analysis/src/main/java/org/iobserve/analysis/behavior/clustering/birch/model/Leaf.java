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
 * Class for the leafs of CFTree.
 * 
 * @author Melf Lorenzen
 *
 */
class Leaf extends AbstractNode {
    private ClusteringFeature clusteringFeature;
    private final List<ClusteringFeature> entries;
    private Leaf prev;
    private Leaf next;

    public Leaf(final int n) {
        this.clusteringFeature = new ClusteringFeature(n);
        this.entries = new ArrayList<>();
    }

    public Leaf() {
        this.clusteringFeature = new ClusteringFeature(AbstractNode.dimension);
        this.entries = new ArrayList<>();
    }

    public Leaf(final Leaf lf) {
        this.clusteringFeature = new ClusteringFeature(AbstractNode.dimension);
        this.entries = new ArrayList<>();
        this.clusteringFeature.add(lf.clusteringFeature);
    }

    public List<ClusteringFeature> getEntries() {
        return this.entries;
    }

    public int getSize() {
        return this.entries.size();
    }

    public Leaf getPrev() {
        return this.prev;
    }

    public void setPrev(final Leaf prev) {
        this.prev = prev;
    }

    public Leaf getNext() {
        return this.next;
    }

    public void setNext(final Leaf next) {
        this.next = next;
    }

    private Leaf split() {
        final ProximityMatrix pm = new ProximityMatrix(this.entries);
        /// cfs that are closer to farthest one move to the new leaf, the rest stays put
        final Leaf second = new Leaf();
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
    Optional<AbstractNode> insert(final ClusteringFeature cf) {

        final NavigableMap<Double, ClusteringFeature> ranking = new TreeMap<>();

        this.clusteringFeature.add(cf);

        for (final ClusteringFeature entry : this.entries) {
            ranking.put(entry.compare(cf), entry);
        }

        if (!ranking.isEmpty()) {
            final ClusteringFeature closest = ranking.firstEntry().getValue();
            final ClusteringFeature merge = new ClusteringFeature(closest, cf);
            /// Merge the Clustering Features if the threshold requirements remains satisfied
            if (merge.isBelowThreshold(AbstractNode.mergeThreshold)) {
                closest.add(cf);
            } else {
                // If merge is not possible, add the Clustering Feature to the leaf
                return this.addEntry(cf);
            }
        } else {
            // If Leaf is empty, simply add
            return this.addEntry(cf);
        }

        return Optional.empty();
    }

    private Optional<AbstractNode> addEntry(final ClusteringFeature cf) {
        this.entries.add(cf);

        /// Split the leaf if size is too large
        if (this.entries.size() > AbstractNode.leafSizeConstraint) {
            return Optional.of(this.split());
        }
        return Optional.empty();
    }

    @Override
    public String toString() {
        String res = "L{" + this.entries.size() + ": " + this.clusteringFeature.toString() + "}";
        for (final ClusteringFeature cf : this.entries) {
            res += "  [" + cf.toString() + "]  ";
        }
        return res;
    }

    @Override
    void updateSum() {
        this.clusteringFeature = this.sum();
    }

    /* Sums up the node's members */
    ClusteringFeature sum() {
        final double[] ls = new double[this.clusteringFeature.getDimension()];
        final double[] ss = new double[this.clusteringFeature.getDimension()];
        final ClusteringFeature ncf = new ClusteringFeature(0, ls, ss);
        for (final ClusteringFeature cf : this.entries) {
            ncf.add(cf);
        }
        return ncf;
    }

    @Override
    public List<AbstractNode> getChildren() {
        return new ArrayList<>();
    }

    @Override
    int space() {
        return AbstractNode.leafSizeConstraint - this.size();
    }

    @Override
    int size() {
        return this.entries.size();
    }

    @Override
    void resplit(final AbstractNode child) {
        final Leaf leaf = (Leaf) child;
        this.entries.addAll(leaf.entries);
        leaf.entries.clear();
        final ProximityMatrix pm = new ProximityMatrix(this.entries);
        /// cfs that are closer to farthest one move to the new leaf, the rest stays put
        for (int i = 0; i < this.entries.size(); i++) {
            if (pm.getMatrix()[pm.getFarthestPair()[0]][i] < pm.getMatrix()[pm.getFarthestPair()[1]][i]) {
                leaf.entries.add(this.entries.get(i));
            }
        }
        this.entries.removeAll(leaf.entries);
        if (this.space() < 0) {
            final List<ClusteringFeature> spillover = new ArrayList<>();
            for (int i = AbstractNode.leafSizeConstraint; i < this.size(); i++) {
                spillover.add(this.entries.get(i));
            }
            this.entries.removeAll(spillover);
            leaf.entries.addAll(spillover);
        }
        if (leaf.space() < 0) {
            final List<ClusteringFeature> spillover = new ArrayList<>();
            for (int i = AbstractNode.leafSizeConstraint; i < leaf.size(); i++) {
                spillover.add(leaf.entries.get(i));
            }
            leaf.entries.removeAll(spillover);
            this.entries.addAll(spillover);
        }
        this.updateSum();
    }

    @Override
    boolean refinementMerge(final AbstractNode child) {
        if (child.space() < this.size()) {
            this.resplit(child);
            return false;
        } else {
            this.entries.addAll(((Leaf) child).entries);
            this.clusteringFeature.add(child.getCF());
        }
        return true;
    }

    @Override
    Optional<AbstractNode> getNextLevel() {
        return Optional.empty();
    }

    @Override
    Optional<AbstractNode> getChild(final int i) {
        return Optional.empty();
    }

    @Override
    int getClosestChildIndex(final ClusteringFeature cf) {
        return -1;
    }

    void removeFirst() {
        this.entries.remove(0);
    }

    boolean testInsert(final ClusteringFeature cf) {
        final NavigableMap<Double, ClusteringFeature> ranking = new TreeMap<>();

        for (final ClusteringFeature entry : this.entries) {
            ranking.put(entry.compare(cf), entry);
        }

        if (!ranking.isEmpty()) {
            final ClusteringFeature closest = ranking.firstEntry().getValue();
            final ClusteringFeature merge = new ClusteringFeature(closest, cf);
            /// Merge the Clustering Features if the threshold requirements remains satisfied
            if (merge.isBelowThreshold(AbstractNode.mergeThreshold)) {
                return true;
            } else {
                // If merge is not possible, add the Clustering Feature to the leaf
                return this.entries.size() < AbstractNode.leafSizeConstraint;
            }
        } else {
            // If Leaf is empty, simply add
            return true;
        }

    }

    double getMinimalLeafDistance() {
        final ProximityMatrix pm = new ProximityMatrix(this.entries);
        return pm.getMatrix()[pm.getClosestPair()[0]][pm.getClosestPair()[1]];
    }

    @Override
    ClusteringFeature getCF() {
        return this.clusteringFeature;
    }

}
