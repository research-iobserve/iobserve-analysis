/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ***************************************************************************/
package org.iobserve.analysis.cdoruserbehavior.filter.models.configuration;

import java.util.Optional;
import java.util.Random;

import weka.clusterers.XMeans;
import weka.core.Instances;
import weka.core.NormalizableDistance;

/**
 * xmeans clustering for TClustering
 *
 * @author Christoph Dornieden
 *
 */
public class XMeansClustering implements IClustering {
    private final int minClusters;
    private final int maxClusters;
    private final NormalizableDistance distanceMetric;

    /**
     * constructor
     *
     * @param expectedUserGroups
     *            number of expected user groups
     * @param variance
     *            variance of the expected user groups
     * @param distanceMetric
     *            distance Metric for clustering
     */
    public XMeansClustering(final int expectedUserGroups, final int variance,
            final NormalizableDistance distanceMetric) {
        this.minClusters = (expectedUserGroups - variance) < 2 ? 1 : expectedUserGroups - variance;
        this.maxClusters = (expectedUserGroups + variance) < 2 ? 2 : expectedUserGroups + variance;
        this.distanceMetric = distanceMetric;

    }

    @Override
    public Optional<Instances> getClusterCenters(final Instances instances) {
        final XMeans xMeansClusterer = new XMeans();
        xMeansClusterer.setSeed(new Random().nextInt(Integer.MAX_VALUE));
        xMeansClusterer.setDistanceF(this.distanceMetric);

        xMeansClusterer.setMinNumClusters(this.minClusters);
        xMeansClusterer.setMaxNumClusters(this.maxClusters);

        try {
            xMeansClusterer.buildClusterer(instances);
            return Optional.of(xMeansClusterer.getClusterCenters());

        } catch (final Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
