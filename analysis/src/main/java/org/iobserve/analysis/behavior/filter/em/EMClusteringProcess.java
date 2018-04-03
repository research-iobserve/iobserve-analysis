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
package org.iobserve.analysis.behavior.filter.em;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.IDensitityClustering;
import org.iobserve.analysis.behavior.filter.ClusterMerger;
import org.iobserve.analysis.behavior.filter.TDensityClustering;

import weka.clusterers.EM;
import weka.core.Instances;

/**
 * Combines the non-centroid based clustering algorithm {@link EM} with a merger for reuse in the
 * user behavior modeling.
 *
 * @author Marc Adolf
 *
 */
public class EMClusteringProcess extends CompositeStage {

    private final InputPort<Instances> inputPort;
    private final OutputPort<Instances> outputPort;

    // TODO a Clustering Filter Interface may be useful

    /**
     * constructor for EM clustering process.
     *
     * @param clustering
     *            clustering algorithm
     */
    public EMClusteringProcess(final IDensitityClustering clustering) {
        final TDensityClustering clusteringFilter = new TDensityClustering(clustering);
        this.inputPort = clusteringFilter.getInputPort();
        final ClusterMerger merger = new ClusterMerger();
        this.outputPort = merger.getOutputPort();

        this.connectPorts(clusteringFilter.getOutputPort(), merger.getInputPort());
    }

    public InputPort<Instances> getInputPort() {
        return this.inputPort;
    }

    public OutputPort<Instances> getOutputPort() {
        return this.outputPort;
    }

}
