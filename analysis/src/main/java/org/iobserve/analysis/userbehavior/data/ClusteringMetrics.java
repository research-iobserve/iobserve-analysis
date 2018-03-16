/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.userbehavior.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.DistanceFunction;
import weka.core.EuclideanDistance;
import weka.core.Instances;

/**
 * Calculates the quality of a clustering to value a clustering. Therefore, the sum of squared
 * errors is used
 *
 * @author David Peter
 * @author Robert Heinrich
 * @author Christoph Dornieden
 */
public class ClusteringMetrics {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusteringMetrics.class);

    // Distance between the centroids and the assignments of the cluster
    private double sumOfSquaredErrors = 0;

    private final Instances centroids;
    private final Instances instances;
    private final int[] assignments;

    /**
     * Construct cluster metrics.
     *
     * @param centroids
     *            instances of something
     * @param instances
     *            other instances
     * @param assignments
     *            assignments
     */
    public ClusteringMetrics(final Instances centroids, final Instances instances, final int[] assignments) {
        this.centroids = centroids;
        this.instances = instances;
        this.assignments = assignments;
    }

    /**
     * Compute the metrics.
     */
    public void calculateSimilarityMetrics() {
        this.sumOfSquaredErrors = this.calculateSumOfSquaredErrors();
    }

    // Calculates the similarity between the data of a cluster
    // States the quality of a clustering
    private double calculateSumOfSquaredErrors() {

        final DistanceFunction euclideanDistance = new EuclideanDistance();
        euclideanDistance.setInstances(this.instances);

        final double numberOfCentroids = this.centroids.numInstances();

        this.sumOfSquaredErrors = 0;

        for (int i = 0; i < numberOfCentroids; i++) {
            for (int j = 0; j < this.instances.numInstances(); j++) {
                if (this.assignments[j] == i) {
                    this.sumOfSquaredErrors += Math
                            .pow(euclideanDistance.distance(this.instances.instance(j), this.centroids.instance(i)), 2);
                }
            }
        }

        return this.sumOfSquaredErrors;
    }

    /**
     * Prints the calculated metrics.
     */
    public void printSimilarityMetrics() {
        if (this.sumOfSquaredErrors == 0) {
            ClusteringMetrics.LOGGER.debug("Metrics have not been calculated");
        } else {
            ClusteringMetrics.LOGGER.debug("Sum of squared errors: {}", this.sumOfSquaredErrors);
        }
    }

    public double getSumOfSquaredErrors() {
        return this.sumOfSquaredErrors;
    }

    /**
     * Get cluster centroids.
     *
     * @return cluster centroids
     */
    public Instances getCentroids() {
        return this.centroids;
    }

}
