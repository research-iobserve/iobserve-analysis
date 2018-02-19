package org.iobserve.analysis.clustering.filter;

import java.util.Map;
import org.apache.commons.math3.util.Pair;
import org.iobserve.analysis.clustering.filter.models.AggregatedCallInformation;
import org.iobserve.analysis.clustering.filter.models.BehaviorModel;

public interface IParameterMetricStrategy {
    public double getDistance(BehaviorModel a, BehaviorModel b);
}