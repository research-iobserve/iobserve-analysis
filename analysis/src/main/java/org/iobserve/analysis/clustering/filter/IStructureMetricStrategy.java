package org.iobserve.analysis.clustering.filter;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;

public interface IStructureMetricStrategy {
    public double getDistance(BehaviorModel a, BehaviorModel b);
}