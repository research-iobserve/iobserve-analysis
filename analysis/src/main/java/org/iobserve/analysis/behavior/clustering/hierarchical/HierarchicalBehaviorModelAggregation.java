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

import org.iobserve.analysis.behavior.filter.BehaviorModelCreationStage;
import org.iobserve.analysis.behavior.models.data.configuration.ISignatureCreationStrategy;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import weka.core.Instances;

/**
 * @author Stephan Lenga
 *
 */
public class HierarchicalBehaviorModelAggregation extends CompositeStage {
    // private final VectorQuantizationClusteringStage clustering;

    /**
     * Constructor configuration of the aggregation filters.
     *
     * @param namePrefix
     *            name prefix
     * @param visualizationUrl
     *            path or url for the sink
     * @param signatureCreationStrategy
     *            signature creation strategy
     */
    public HierarchicalBehaviorModelAggregation(final String namePrefix, final String visualizationUrl,
            final ISignatureCreationStrategy signatureCreationStrategy) {
        final BehaviorModelCreationStage behaviorModelCreationStage = new BehaviorModelCreationStage(namePrefix);

        // NEED HierarchicalClusteringStage in analysis.behavior.filter
        // this.clustering = new HierarchicalClusteringStage(new HierarchicalClustering()); //
        // Arguments
        // may be
        // necessary
        // this.connectPorts(this.clustering.getOutputPort(),
        // behaviorModelCreationStage.getInputPort());

        /** visualization integration. */
        // final AbstractBehaviorModelOutputSink tIObserveUBM = new
        // BehaviorModelSink(visualizationUrl,
        // signatureCreationStrategy);

        // this.connectPorts(behaviorModelCreationStage.getOutputPort(),
        // tIObserveUBM.getInputPort());
    }

    /**
     * Get input port.
     *
     * @return input port
     */
    public InputPort<Instances> getInputPort() {
        return null; // this.clustering.getInputPort();
    }
}
