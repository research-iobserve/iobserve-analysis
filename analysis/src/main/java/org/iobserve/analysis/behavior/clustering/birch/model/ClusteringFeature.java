/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.behavior.clustering.birch.model;

import weka.core.Instance;

/**
 * A ClusteringFeature is a cluster condensed into three principal quantities: The number of vectors
 * in the cluster, their linear sum and the sum of the squared vectors.
 *
 * @author Melf Lorenzen
 *
 */
public class ClusteringFeature {

    private static ICFComparisonStrategy metric = new CFCompareD2Strategy();
    private int number;
    private final double[] linearSum;
    private final double[] squareSum;

    /**
     * Constructor for empty clustering feature.
     *
     * @param n
     *            dimension of the clustering feature
     */
    public ClusteringFeature(final int n) {
        this.number = 0;
        this.linearSum = new double[n];
        this.squareSum = new double[n];
    }

    /**
     * Constructor for a clustering feature representing an instance vector.
     *
     * @param instance
     *            the instance to represent.
     */
    public ClusteringFeature(final Instance instance) {
        this.number = 1;
        this.linearSum = new double[instance.numAttributes()];
        this.squareSum = new double[instance.numAttributes()];

        for (int i = 0; i < instance.numAttributes(); i++) {
            this.linearSum[i] = instance.value(i);
            this.squareSum[i] = this.linearSum[i] * this.linearSum[i];
        }
    }

    /**
     * Constructor for clustering feature.
     *
     * @param number
     *            number of elements in the cluster
     * @param linearSum
     *            linear sum of the cluster elements
     * @param squareSum
     *            square sum of the cluster elements
     */
    public ClusteringFeature(final int number, final double[] linearSum, final double[] squareSum) {
        this.number = number;
        this.linearSum = linearSum;
        this.squareSum = squareSum;
    }

    /**
     * Constructor for clustering feature containing one vector.
     *
     * @param linearSum
     *            the vector to base the cf off of
     */
    public ClusteringFeature(final Double[] linearSum) {
        this.number = 1;

        final int n = linearSum.length;

        this.linearSum = new double[n];
        this.squareSum = new double[n];

        for (int i = 0; i < n; i++) {
            this.linearSum[i] = linearSum[i];
            this.squareSum[i] = linearSum[i] * linearSum[i];
        }

    }

    /**
     * Constructs a new clustering feature from the sum of two clustering features.
     *
     * @param cf1
     *            clustering feature to be summed up
     * @param cf2
     *            clustering feature to be summed up
     */
    public ClusteringFeature(final ClusteringFeature cf1, final ClusteringFeature cf2) {
        this.number = cf1.number + cf2.number;

        final int n = cf1.linearSum.length;

        this.linearSum = new double[n];
        this.squareSum = new double[n];

        for (int i = 0; i < n; i++) {
            this.linearSum[i] = cf1.linearSum[i] + cf2.linearSum[i];
            this.squareSum[i] = cf1.squareSum[i] + cf2.squareSum[i];
        }
    }

    public int getDimension() {
        return this.linearSum.length;
    }

    /**
     * Adds a clustering feature to this clustering feature.
     *
     * @param cf
     *            the clustering feature to be added
     */
    public void add(final ClusteringFeature cf) {
        this.number += cf.number;

        final int n = this.linearSum.length;

        for (int i = 0; i < n; i++) {
            this.linearSum[i] += cf.linearSum[i];
            this.squareSum[i] += cf.squareSum[i];
        }
    }

    /**
     * Returns the distance between this clustering feature and the given clustering feature.
     *
     * @param cf
     *            clustering feature to compare to
     * @return the distance
     */
    public double compare(final ClusteringFeature cf) {
        return ClusteringFeature.metric.getDistance(this, cf);
    }

    double compareD0(final ClusteringFeature cf) {
        double res = 0;
        for (int i = 0; i < this.linearSum.length; i++) {
            res += Math.pow(this.linearSum[i] / (1.0 * this.number) - cf.linearSum[i] / (1.0 * cf.number), 2.0);
        }

        return Math.sqrt(res);
    }

    double compareD2(final ClusteringFeature cf) {
        double square = 0.0;
        double linear = 0.0;

        for (int i = 0; i < this.linearSum.length; i++) {
            square += this.squareSum[i] * cf.number + cf.squareSum[i] * this.number;
            linear += this.linearSum[i] * cf.linearSum[i];
        }
        return Math.sqrt((square - 2.0 * linear) / (this.number * cf.number));
    }

    double compareD4(final ClusteringFeature cf) {
        final int dimension = this.linearSum.length;
        final double[] alpha = new double[dimension];
        final double[] beta = new double[dimension];
        final double[] gamma = new double[dimension];
        double res = 0;

        for (int i = 0; i < this.linearSum.length; i++) {
            alpha[i] = (this.linearSum[i] + cf.linearSum[i]) / (this.number + cf.number);
            beta[i] = this.linearSum[i] / this.number;
            gamma[i] = cf.linearSum[i] / cf.number;
        }

        for (int i = 0; i < this.linearSum.length; i++) {
            res += 2.0 * this.linearSum[i] * (beta[i] - alpha[i]) + 2.0 * cf.linearSum[i] * (gamma[i] - alpha[i])
                    + (cf.number + this.number) * alpha[i] * alpha[i] - this.number * beta[i] * beta[i]
                    - cf.number * gamma[i] * gamma[i];
        }

        return Math.sqrt(res);
    }

    boolean isBelowThreshold(final double t) {
        return this.getDiameter() <= t;
    }

    /**
     * Returns the sum of squared errors of elements in this cluster.
     *
     * @return the sum of squared errors
     */
    public double getSquareSumError() {
        double sse = 0.0;
        for (int i = 0; i < this.linearSum.length; i++) {
            sse += this.squareSum[i] - 2.0 * this.linearSum[i] * this.linearSum[i] / this.number
                    + this.number * Math.pow(this.linearSum[i] / this.number, 2);
        }
        return sse;
    }

    /**
     * Returns the radius of this clustering feature.
     *
     * @return the radius
     */
    public double getRadius() {
        return Math.sqrt(this.getSquareSumError() / this.number);
    }

    /**
     * Returns the diameter of this clustering feature.
     *
     * @return the diameter
     */
    public double getDiameter() {
        if (this.number <= 1) {
            return 0.0;
        }

        double square = 0.0;
        double linear = 0.0;

        for (int i = 0; i < this.linearSum.length; i++) {
            square += this.squareSum[i];
            linear += this.linearSum[i] * this.linearSum[i];
        }
        return Math.sqrt((this.number * 2.0 * square - 2.0 * linear) / (this.number * (this.number - 1.0)));
    }

    /**
     * Returns the centroid of the cluster represented by this clustering feature.
     *
     * @return the centroid
     */
    public double[] getCentroid() {
        final double[] centroid = new double[this.getDimension()];
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] = this.linearSum[i] / (1.0 * this.number);
        }
        return centroid;
    }

    public int getNumber() {
        return this.number;
    }

    public static void setMetric(final ICFComparisonStrategy metric) {
        ClusteringFeature.metric = metric;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + this.number + "@" + this.hashCode() + "]";
    }

}
