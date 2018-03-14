package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;

public class JPetStoreParameterMetric implements IParameterMetricStrategy {
    @Override
    public double getDistance(final BehaviorModel a, final BehaviorModel b) {
        return 0;
    }
}
