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
package org.iobserve.analysis.behavior.filter.xmeans;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

import org.iobserve.analysis.behavior.filter.TBehaviorModelCreation;
import org.iobserve.analysis.behavior.filter.TVectorQuantizationClustering;
import org.iobserve.analysis.behavior.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.sink.AbstractModelOutputSink;
import org.iobserve.analysis.sink.BehaviorModelSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.core.Instances;

/**
 *
 * @author Christoph Dornieden
 * @author Reiner Jung
 */
public class TBehaviorModelAggregation extends CompositeStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(TBehaviorModelAggregation.class);

    private final TVectorQuantizationClustering tClustering;

    /**
     * Constructor configuration of the aggregation filters.
     *
     * @param configuration
     *            filter configuration
     */
    public TBehaviorModelAggregation(final BehaviorModelConfiguration configuration) {
        final TBehaviorModelCreation tBehaviorModelCreation = new TBehaviorModelCreation(configuration.getNamePrefix());

        this.tClustering = new TVectorQuantizationClustering(configuration.getClustering());
        this.connectPorts(this.tClustering.getOutputPort(), tBehaviorModelCreation.getInputPort());

        /** visualization integration. */
        final AbstractModelOutputSink tIObserveUBM = new BehaviorModelSink(configuration.getVisualizationUrl(),
                configuration.getSignatureCreationStrategy());

        this.connectPorts(tBehaviorModelCreation.getOutputPort(), tIObserveUBM.getInputPort());
    }

    /**
     * Get input port.
     *
     * @return input port
     */
    public InputPort<Instances> getInputPort() {
        return this.tClustering.getInputPort();
    }

}
