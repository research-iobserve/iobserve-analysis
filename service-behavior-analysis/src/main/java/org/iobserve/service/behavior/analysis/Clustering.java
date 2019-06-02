package org.iobserve.service.behavior.analysis;

import java.util.HashSet;
import java.util.Set;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;

public class Clustering {
    private Set<BehaviorModelGED> noise = new HashSet<>();

    private Set<Set<BehaviorModelGED>> clusters = new HashSet<>();

    public Set<BehaviorModelGED> getNoise() {
        return this.noise;
    }

    public void setNoise(final Set<BehaviorModelGED> noise) {
        this.noise = noise;
    }

    public Set<Set<BehaviorModelGED>> getClusters() {
        return this.clusters;
    }

    public void setClusters(final Set<Set<BehaviorModelGED>> clusters) {
        this.clusters = clusters;
    }

    public void addCluster(final Set<BehaviorModelGED> cluster) {
        this.clusters.add(cluster);
    }
}
