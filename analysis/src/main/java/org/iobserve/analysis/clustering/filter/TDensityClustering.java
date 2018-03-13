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

import java.util.List;
import java.util.Map;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.eclipse.net4j.util.collection.Pair;
import org.iobserve.analysis.clustering.IDensitityClustering;

import weka.core.Instance;
import weka.core.Instances;

/**
 * Filter for different density based clustering algorithms. Intended to be used with a following
 * cluster merger stage.
 *
 * @author Marc Adolf
 *
 */
public class TDensityClustering extends AbstractConsumerStage<Instances> {

    private final OutputPort<Map<Integer, List<Pair<Instance, Double>>>> outputPort = this.createOutputPort();
    private final IDensitityClustering clustering;

    /**
     * Density clustering filter.
     *
     * @param clustering
     *            density clustering helper
     */
    public TDensityClustering(final IDensitityClustering clustering) {
        this.clustering = clustering;
    }

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractConsumerStage#execute(java.lang.Object)
     */
    @Override
    protected void execute(final Instances instances) throws Exception {
        final Map<Integer, List<Pair<Instance, Double>>> clusteringResults = this.clustering
                .clusterInstances(instances);
        this.outputPort.send(clusteringResults);

    }

    public OutputPort<Map<Integer, List<Pair<Instance, Double>>>> getOutputPort() {
        return this.outputPort;
    }

}
