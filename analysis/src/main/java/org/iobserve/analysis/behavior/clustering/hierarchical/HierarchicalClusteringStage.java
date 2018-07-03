/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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

package org.iobserve.analysis.behavior.clustering.hierarchical;

import java.util.List;
import java.util.Map;

import org.eclipse.net4j.util.collection.Pair;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author Stephan Lenga
 *
 */
public class HierarchicalClusteringStage extends AbstractConsumerStage<Instances> {

    private final IHierarchicalClustering clustering;
    private final OutputPort<Map<Integer, List<Pair<Instance, Double>>>> outputPort = this.createOutputPort();

    /**
     * constructor.
     *
     * @param clustering
     *            clustering used
     */
    public HierarchicalClusteringStage(final IHierarchicalClustering clustering) {
        this.clustering = clustering;
    }

    @Override
    protected void execute(final Instances instances) {
        final Map<Integer, List<Pair<Instance, Double>>> clusteringResults = this.clustering
                .clusterInstances(instances);
        this.getOutputPort().send(clusteringResults);

        // final String clusteringMethodName =
        // clusteringResults.map(ClusteringResults::getClusteringMethod).orElse(null);
        // final int numOfClusters =
        // clusteringResults.map(ClusteringResults::getNumberOfClusters).orElse(null);
        //
        // final FastVector f = new FastVector();
        // for (int j = 0; j < instance.numAttributes(); j++) {
        // attributes.addElement(instance.attribute(j));
        // }
        //
        // clusteringResults.ifPresent(this::printInstances);
        // clusteringResults
        // .ifPresent(results ->
        // this.getOutputPort().send(results.getClusteringMetrics().getCentroids()));

    }

    /**
     * getter.
     *
     * @return output port
     */
    public OutputPort<Map<Integer, List<Pair<Instance, Double>>>> getOutputPort() {
        return this.outputPort;
    }
}
