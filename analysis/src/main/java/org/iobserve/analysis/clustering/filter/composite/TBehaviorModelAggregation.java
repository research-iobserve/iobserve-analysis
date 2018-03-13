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
package org.iobserve.analysis.clustering.filter.composite;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

import org.iobserve.analysis.clustering.ExpectationMaximizationClustering;
import org.iobserve.analysis.clustering.filter.TBehaviorModelCreation;
import org.iobserve.analysis.clustering.filter.TBehaviorModelVisualization;
import org.iobserve.analysis.clustering.filter.TVectorQuantizationClustering;
import org.iobserve.analysis.clustering.filter.models.configuration.BehaviorModelConfiguration;
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

    private TVectorQuantizationClustering tClustering;

    private EMClusteringProcess emClustering;

    /**
     * Constructor configuration of the aggregation filters.
     *
     * @param configuration
     *            filter configuration
     */
    public TBehaviorModelAggregation(final BehaviorModelConfiguration configuration) {
        final TBehaviorModelCreation tBehaviorModelCreation = new TBehaviorModelCreation(configuration.getNamePrefix());

        switch (configuration.getAggregationType()) {
        case X_MEANS_CLUSTERING:
            this.tClustering = new TVectorQuantizationClustering(configuration.getClustering());
            this.connectPorts(this.tClustering.getOutputPort(), tBehaviorModelCreation.getInputPort());
            break;
        case EM_CLUSTERING:
            this.emClustering = new EMClusteringProcess(new ExpectationMaximizationClustering());
            this.connectPorts(this.emClustering.getOutputPort(), tBehaviorModelCreation.getInputPort());
            break;
        default:
            TBehaviorModelAggregation.LOGGER.error("Unknown clustering method {}.", configuration.getAggregationType());
            break;
        }

        /** visualization integration. */
        AbstractModelOutputSink tIObserveUBM = null;
        switch (configuration.getOutputMode()) {
        case UBM_VISUALIZATION:
            tIObserveUBM = new TBehaviorModelVisualization(configuration.getVisualizationUrl(),
                    configuration.getSignatureCreationStrategy());
            break;
        case FILE_OUTPUT:
            tIObserveUBM = new BehaviorModelSink(configuration.getVisualizationUrl(),
                    configuration.getSignatureCreationStrategy());
            break;
        default:
            TBehaviorModelAggregation.LOGGER.error("Unknown visualization method {}.", configuration.getOutputMode());
            break;
        }

        this.connectPorts(tBehaviorModelCreation.getOutputPort(), tIObserveUBM.getInputPort());
    }

    /**
     * Get input port.
     *
     * @return input port
     */
    public InputPort<Instances> getInputPort() {
        if (this.tClustering != null) {
            return this.tClustering.getInputPort();
        } else if (this.emClustering != null) {
            return this.emClustering.getInputPort();
        } else {
            return null;
        }
    }

}
