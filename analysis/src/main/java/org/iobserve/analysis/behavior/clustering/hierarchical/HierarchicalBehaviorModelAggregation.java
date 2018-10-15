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

import org.iobserve.analysis.behavior.filter.BehaviorModelCreationStage;
import org.iobserve.analysis.behavior.models.data.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.sink.AbstractBehaviorModelOutputSink;
import org.iobserve.analysis.sink.BehaviorModelSink;

import weka.core.Instances;

/**
 * Model Aggregatioon for the Hierarchical Clustering Behavior.
 *
 * @author SL
 * @since 0.0.3
 */
public class HierarchicalBehaviorModelAggregation extends CompositeStage {
    private final HierarchicalClusteringProcess hierClustering;

    /**
     * Constructor configuration of the aggregation filters.
     *
     * @param namePrefix
     *            Name prefix.
     * @param visualizationUrl
     *            Path or url for the sink.
     * @param signatureCreationStrategy
     *            Signature creation strategy.
     * @param distanceMetric
     *            Used distance metric for hierarchical clustering.
     * @param clusterSelectionMethod
     *            Used method for selecting a "good" number of clusters from clustering.
     * @param linkage
     *            Used linkage for hierarchical clustering.
     */
    public HierarchicalBehaviorModelAggregation(final String namePrefix, final String visualizationUrl,
            final ISignatureCreationStrategy signatureCreationStrategy, final String distanceMetric,
            final String clusterSelectionMethod, final String linkage) {
        final BehaviorModelCreationStage behaviorModelCreationStage = new BehaviorModelCreationStage(namePrefix);

        this.hierClustering = new HierarchicalClusteringProcess(
                new HierarchicalClustering(distanceMetric, clusterSelectionMethod, linkage, visualizationUrl));
        this.connectPorts(this.hierClustering.getOutputPort(), behaviorModelCreationStage.getInputPort());

        /** visualization integration. */
        final AbstractBehaviorModelOutputSink tIObserveUBM = new BehaviorModelSink(visualizationUrl,
                signatureCreationStrategy);

        this.connectPorts(behaviorModelCreationStage.getOutputPort(), tIObserveUBM.getInputPort());
    }

    /**
     * Get input port.
     *
     * @return input port
     */
    public InputPort<Instances> getInputPort() {
        return this.hierClustering.getInputPort();
    }
}
