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
package org.iobserve.analysis.behavior.clustering.em;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

import org.iobserve.analysis.behavior.filter.BehaviorModelCreationStage;
import org.iobserve.analysis.behavior.models.data.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.sink.AbstractBehaviorModelOutputSink;
import org.iobserve.analysis.sink.BehaviorModelSink;

import weka.core.Instances;

/**
 *
 * @author Christoph Dornieden
 * @author Reiner Jung
 */
public class EMBehaviorModelAggregation extends CompositeStage {
    private final EMClusteringProcess emClustering;

    /**
     * Create an aggregation stage.
     *
     * @param namePrefix
     *            output name prefix
     * @param visualizationUrl
     *            visualization url or sink path
     * @param signatureCreationStrategy
     *            signature creation strategy
     */
    public EMBehaviorModelAggregation(final String namePrefix, final String visualizationUrl,
            final ISignatureCreationStrategy signatureCreationStrategy) {
        final BehaviorModelCreationStage behaviorModelCreation = new BehaviorModelCreationStage(namePrefix);

        this.emClustering = new EMClusteringProcess(new ExpectationMaximizationClustering());
        this.connectPorts(this.emClustering.getOutputPort(), behaviorModelCreation.getInputPort());

        /** visualization integration. */
        final AbstractBehaviorModelOutputSink tIObserveUBM = new BehaviorModelSink(visualizationUrl, signatureCreationStrategy);

        this.connectPorts(behaviorModelCreation.getOutputPort(), tIObserveUBM.getInputPort());
    }

    /**
     * Get input port.
     *
     * @return input port
     */
    public InputPort<Instances> getInputPort() {
        return this.emClustering.getInputPort();
    }

}
