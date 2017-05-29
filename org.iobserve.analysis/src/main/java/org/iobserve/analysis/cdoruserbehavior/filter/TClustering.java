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
package org.iobserve.analysis.cdoruserbehavior.filter;

import java.util.Optional;

import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.IClustering;
import org.iobserve.analysis.userbehavior.data.ClusteringResults;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import weka.core.Instance;
import weka.core.Instances;

/**
 * aggregate the given user behavior
 *
 * @author Christoph Dornieden
 */

public class TClustering extends AbstractConsumerStage<Instances> {
    private final OutputPort<Instances> outputPort = this.createOutputPort();
    private final IClustering clustering;

    /**
     * constructor
     *
     * @param clustering
     *            clustering used
     */
    public TClustering(final IClustering clustering) {
        this.clustering = clustering;
    }

    @Override
    protected void execute(final Instances instances) {

        Optional<ClusteringResults> clusteringResults = Optional.empty();

        for (int i = 0; i < 5; i++) {

            final Optional<ClusteringResults> tempClusteringResults = this.clustering.clusterInstances(instances);
            if (clusteringResults.isPresent() && tempClusteringResults.isPresent()) {
                clusteringResults = tempClusteringResults.get().getClusteringMetrics()
                        .getSumOfSquaredErrors() < clusteringResults.get().getClusteringMetrics()
                                .getSumOfSquaredErrors() ? clusteringResults : tempClusteringResults;
            } else if (tempClusteringResults.isPresent() && !clusteringResults.isPresent()) {
                clusteringResults = tempClusteringResults;
            }
        }
        clusteringResults.ifPresent(this::printInstances);
        clusteringResults.ifPresent(results -> this.outputPort.send(results.getClusteringMetrics().getCentroids()));

    }

    private void printInstances(final ClusteringResults results) {
        results.printClusteringResults();
        final Instances centroids = results.getClusteringMetrics().getCentroids();
        for (int i = 0; i < centroids.numInstances(); i++) {
            System.out.println("***************************");
            System.out.println("Cluster " + i);
            System.out.println("***************************");
            final Instance instance = centroids.instance(i);
            for (int a = 0; a < instance.numAttributes(); a++) {
                System.out.println(centroids.attribute(a).name() + " : " + instance.value(a));

            }
        }
    }

    /**
     * getter
     *
     * @return output port
     */
    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }

}
