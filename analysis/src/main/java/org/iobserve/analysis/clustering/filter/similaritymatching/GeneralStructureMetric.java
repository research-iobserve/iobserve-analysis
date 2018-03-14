package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;

public class GeneralStructureMetric implements IStructureMetricStrategy {
    @Override
    public double getDistance(final BehaviorModel a, final BehaviorModel b) {
        return 0;
    }
}
