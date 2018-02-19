package org.iobserve.analysis.clustering.filter;

import org.iobserve.analysis.clustering.filter.models.BehaviorModel;

public interface IStructureMetricStrategy {
    public double getDistance(BehaviorModel a, BehaviorModel b);
}