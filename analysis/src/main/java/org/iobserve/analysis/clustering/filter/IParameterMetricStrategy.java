package org.iobserve.analysis.clustering.filter;

import java.util.Map;
import org.apache.commons.math3.util.Pair;
import org.iobserve.analysis.clustering.filter.models.AggregatedCallInformation;

public interface IParameterMetricStrategy {
    public double getDistance(Map<String, Pair<Integer, AggregatedCallInformation[]>> a, Map<String, Pair<Integer, AggregatedCallInformation[]>> b);
}