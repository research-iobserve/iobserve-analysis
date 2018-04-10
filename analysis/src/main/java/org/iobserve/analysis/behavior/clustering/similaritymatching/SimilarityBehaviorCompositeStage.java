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
package org.iobserve.analysis.behavior.clustering.similaritymatching;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.general.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configurable composite stage combining pre-processing of monitoring records, aggregation and
 * generation of behavior models, and outputting them into files.
 *
 * @author Jannis Kuckei
 *
 */
@ReceiveUnfilteredConfiguration
public class SimilarityBehaviorCompositeStage extends CompositeStage implements IBehaviorCompositeStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimilarityBehaviorCompositeStage.class);

    private final PreprocessingCompositeStage preprocessingCompositeStage;

    /**
     * Create an behavior composite stage.
     *
     * @param configuration
     *            configuration for the behavior
     * @throws ConfigurationException
     *             on errors with the configuration
     */
    public SimilarityBehaviorCompositeStage(final Configuration configuration) throws ConfigurationException {
        /** Instantiate configurable objects/properties for stages */

        /** Get base URL for BehaviourModelSink */
        final String baseURL = configuration.getStringProperty(ConfigurationKeys.SINK_BASE_URL);
        if (baseURL.isEmpty()) {
            SimilarityBehaviorCompositeStage.LOGGER.error("Initialization incomplete: No sink base URL specified.");
            throw new ConfigurationException("Initialization incomplete: No sink base URL specified.");
        }

        /** Instantiate IClassificationStage */
        final String classificationStageClassName = configuration
                .getStringProperty(ConfigurationKeys.CLASSIFICATION_STAGE);
        if (classificationStageClassName.isEmpty()) {
            SimilarityBehaviorCompositeStage.LOGGER.error("Initialization incomplete: No classification stage specified.");
            throw new ConfigurationException("Initialization incomplete: No classification stage specified.");
        }
        final IClassificationStage classificationStage = InstantiationFactory
                .createWithConfiguration(IClassificationStage.class, classificationStageClassName, configuration);

        /** Create remaining stages and connect them */
        this.preprocessingCompositeStage = new PreprocessingCompositeStage(configuration);
        final BehaviorModelCompositeSinkStage sinkStage = new BehaviorModelCompositeSinkStage(baseURL);

        this.connectPorts(this.preprocessingCompositeStage.getSessionOutputPort(),
                classificationStage.getSessionInputPort());
        this.connectPorts(this.preprocessingCompositeStage.getTimerOutputPort(),
                classificationStage.getTimerInputPort());
        this.connectPorts(classificationStage.getOutputPort(), sinkStage.getInputPort());
    }

    @Override
    public InputPort<EventBasedTrace> getEventBasedTracePort() {
        return this.preprocessingCompositeStage.getTraceInputPort();
    }

    @Override
    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.preprocessingCompositeStage.getSessionEventInputPort();
    }

}
