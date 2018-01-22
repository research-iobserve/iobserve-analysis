package org.iobserve.analysis.clustering.filter;

public interface IStructureMetricStrategy {
    public double getDistance(Integer[][] a, Integer[][] b);
}