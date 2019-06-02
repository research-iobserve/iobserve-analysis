package org.iobserve.service.behavior.analysis;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;

import teetime.stage.basic.AbstractTransformation;

public class ExtractClustersFromOptics extends AbstractTransformation<List<OpticsData>, Clustering> {
    private final double clusteringDistance;
    private final int minPTs;

    public ExtractClustersFromOptics(final double clusteringDistance, final int minPTs) {
        this.clusteringDistance = clusteringDistance;
        this.minPTs = minPTs;
    }

    @Override
    protected void execute(final List<OpticsData> opticsResults) throws Exception {

        final Clustering clustering = new Clustering();

        Set<BehaviorModelGED> currentCluster = clustering.getNoise();

        for (final OpticsData model : opticsResults) {
            if ((model.getReachabilityDistance() == OpticsData.Undefined)
                    || (model.getReachabilityDistance() > this.clusteringDistance)) {
                if (model.getCoreDistance() <= this.clusteringDistance) {
                    final Set<BehaviorModelGED> newCluster = new HashSet<>();
                    clustering.addCluster(newCluster);
                    newCluster.add(model.getData());
                    currentCluster = newCluster;
                } else {
                    clustering.getNoise().add(model.getData());
                }
            } else {
                currentCluster.add(model.getData());
            }
        }
        this.getOutputPort().send(clustering);
    }
}
