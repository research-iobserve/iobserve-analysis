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
package org.iobserve.analysis.clustering.filter;

import java.util.Optional;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.clustering.IVectorQuantizationClustering;
import org.iobserve.analysis.userbehavior.data.ClusteringResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.Instance;
import weka.core.Instances;

/**
 * aggregate the given user behavior.
 *
 * @author Christoph Dornieden
 */

public class TVectorQuantizationClustering extends AbstractConsumerStage<Instances> {
    private static final Logger LOGGER = LoggerFactory.getLogger(TVectorQuantizationClustering.class);
    private final IVectorQuantizationClustering clustering;
    private final OutputPort<Instances> outputPort = this.createOutputPort();

    /**
     * constructor.
     *
     * @param clustering
     *            clustering used
     */
    public TVectorQuantizationClustering(final IVectorQuantizationClustering clustering) {
        this.clustering = clustering;
    }

    @Override
    protected void execute(final Instances instances) {
        final Optional<ClusteringResults> clusteringResults = this.clustering.clusterInstances(instances);
        clusteringResults.ifPresent(this::printInstances);
        clusteringResults
                .ifPresent(results -> this.getOutputPort().send(results.getClusteringMetrics().getCentroids()));

    }

    private void printInstances(final ClusteringResults results) {
        results.printClusteringResults();
        final Instances centroids = results.getClusteringMetrics().getCentroids();
        for (int i = 0; i < centroids.numInstances(); i++) {
            String logString = "";
            logString += "***************************";
            logString += "Cluster " + i;
            logString += "***************************";
            final Instance instance = centroids.instance(i);
            for (int a = 0; a < instance.numAttributes(); a++) {
                logString += centroids.attribute(a).name() + " : " + instance.value(a);
            }
            TVectorQuantizationClustering.LOGGER.info(logString);
        }
    }

    /**
     * getter.
     *
     * @return output port
     */
    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }

}
