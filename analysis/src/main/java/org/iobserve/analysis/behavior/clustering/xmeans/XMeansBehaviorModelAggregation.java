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
package org.iobserve.analysis.behavior.clustering.xmeans;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

import org.iobserve.analysis.behavior.filter.BehaviorModelCreationStage;
import org.iobserve.analysis.behavior.filter.VectorQuantizationClusteringStage;
import org.iobserve.analysis.behavior.models.data.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.sink.AbstractBehaviorModelOutputSink;
import org.iobserve.analysis.sink.BehaviorModelSink;

import weka.core.Instances;
import weka.core.ManhattanDistance;

/**
 *
 * @author Christoph Dornieden
 * @author Reiner Jung
 */
public class XMeansBehaviorModelAggregation extends CompositeStage {
    private final VectorQuantizationClusteringStage clustering;

    /**
     * Constructor configuration of the aggregation filters.
     *
     * @param namePrefix
     *            name prefix
     * @param visualizationUrl
     *            path or url for the sink
     * @param signatureCreationStrategy
     *            signature creation strategy
     * @param expectedUserGroups
     *            expected user groups
     * @param variance
     *            variance
     */
    public XMeansBehaviorModelAggregation(final String namePrefix, final String visualizationUrl,
            final ISignatureCreationStrategy signatureCreationStrategy, final int expectedUserGroups,
            final int variance) {
        final BehaviorModelCreationStage behaviorModelCreationStage = new BehaviorModelCreationStage(namePrefix);

        this.clustering = new VectorQuantizationClusteringStage(
                new XMeansClustering(expectedUserGroups, variance, new ManhattanDistance()));
        this.connectPorts(this.clustering.getOutputPort(), behaviorModelCreationStage.getInputPort());

        /** visualization integration. */
        final AbstractBehaviorModelOutputSink tIObserveUBM = new BehaviorModelSink(visualizationUrl, signatureCreationStrategy);

        this.connectPorts(behaviorModelCreationStage.getOutputPort(), tIObserveUBM.getInputPort());
    }

    /**
     * Get input port.
     *
     * @return input port
     */
    public InputPort<Instances> getInputPort() {
        return this.clustering.getInputPort();
    }

}
