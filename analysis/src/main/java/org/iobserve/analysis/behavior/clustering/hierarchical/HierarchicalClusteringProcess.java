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

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.filter.ClusterMerger;

import weka.core.Instances;

/**
 * Process class for the Hierarchical clustering.
 *
 * @author SL
 * @since 0.0.3
 */
public class HierarchicalClusteringProcess extends CompositeStage {
    private final InputPort<Instances> inputPort;
    private final OutputPort<Instances> outputPort;

    /**
     * Constructor.
     *
     * @param clustering
     *            Chosen cluster selection method.
     */
    public HierarchicalClusteringProcess(final IHierarchicalClustering clustering) {
        final HierarchicalClusteringStage clusteringFilter = new HierarchicalClusteringStage(clustering);
        this.inputPort = clusteringFilter.getInputPort();
        final ClusterMerger merger = new ClusterMerger(); // reuse ClusterMerger
        this.outputPort = merger.getOutputPort();

        this.connectPorts(clusteringFilter.getOutputPort(), merger.getInputPort());
    }

    /**
     * Getter for input port.
     *
     * @return input port
     */
    public InputPort<Instances> getInputPort() {
        return this.inputPort;
    }

    /**
     * Getter for output port.
     *
     * @return output port
     */
    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }
}
