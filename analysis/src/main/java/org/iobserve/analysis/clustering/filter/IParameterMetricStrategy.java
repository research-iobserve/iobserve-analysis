package org.iobserve.analysis.clustering.filter;

import org.iobserve.analysis.clustering.filter.models.BehaviorModel;

public interface IParameterMetricStrategy {
    public double getDistance(BehaviorModel a, BehaviorModel b);
}