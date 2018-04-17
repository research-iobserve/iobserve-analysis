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
package org.iobserve.analysis.clustering.shared;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.analysis.behavior.filter.IClassificationStage;
import org.iobserve.analysis.behavior.filter.UserSessionGeneratorCompositeStage;
import org.iobserve.analysis.behavior.models.data.configuration.GetLastXSignatureStrategy;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.analysis.sink.BehaviorModelSink;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.data.trace.EventBasedTrace;
import org.iobserve.stages.general.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configurable composite stage combining pre-processing of monitoring records, aggregation and
 * generation of behavior models, and outputting them into files.
 *
 * @author Melf Lorenzen
 *
 */
@ReceiveUnfilteredConfiguration
public class ClassificationCompositeStage extends CompositeStage implements IBehaviorCompositeStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClassificationCompositeStage.class);

    private final InputPort<EventBasedTrace> eventBasedTraceInputPort;
    private final InputPort<ISessionEvent> sessionEventInputPort;

    /**
     * Create a classification composite stage.
     *
     * @param configuration
     *            configuration object for birch classification
     * @throws ConfigurationException
     *             if the configuration does not contain all necessary keys
     */
    public ClassificationCompositeStage(final Configuration configuration) throws ConfigurationException {
        /** Instantiate configurable objects/properties for stages */

        /**
         * Create Clock. Default value of -1 was selected because the method getLongProperty states
         * it will return "null" if no value was specified, but long is a primitive type ...
         */
        final Long triggerInterval = configuration.getLongProperty(ConfigurationKeys.TRIGGER_INTERVAL, -1);
        if (triggerInterval < 0) {
            ClassificationCompositeStage.LOGGER.error("Initialization incomplete: No time trigger interval specified.");
            throw new ConfigurationException("Initialization incomplete: No time trigger interval specified.");
        }
        /** Get base URL for BehaviourVisualization */
        final String visualizationURL = configuration.getStringProperty(ConfigurationKeys.BEHAVIOR_VISUALIZATION_URL);
        if (visualizationURL.isEmpty()) {
            ClassificationCompositeStage.LOGGER
                    .error("Initialization incomplete: No sink visualization URL specified.");
            throw new ConfigurationException("Initialization incomplete: No sink visualization URL specified.");
        }

        /** Get base URL for BehaviourModelSink */
        final String baseURL = configuration.getStringProperty(ConfigurationKeys.SINK_BASE_URL);
        if (baseURL.isEmpty()) {
            ClassificationCompositeStage.LOGGER.error("Initialization incomplete: No sink base URL specified.");
            throw new ConfigurationException("Initialization incomplete: No sink base URL specified.");
        }

        /** Get keep time for user sessions */
        final long keepTime = configuration.getLongProperty(ConfigurationKeys.KEEP_TIME, -1);
        if (keepTime < 0) {
            ClassificationCompositeStage.LOGGER.error("Initialization incomplete: No keep time interval specified.");
            throw new ConfigurationException("Initialization incomplete: No keep time interval specified.");
        }

        final int minCollectionSize = configuration.getIntProperty(ConfigurationKeys.MIN_SIZE, -1);
        if (minCollectionSize < 0) {
            ClassificationCompositeStage.LOGGER
                    .error("Initialization incomplete: No min size for user sessions specified.");
            throw new ConfigurationException("Initialization incomplete: No min size for user sessions specified.");
        }

        final double leafThresholdValue = configuration.getDoubleProperty(ConfigurationKeys.LEAF_TH, -1.0);
        if (leafThresholdValue < 0) {
            ClassificationCompositeStage.LOGGER.error("Initialization incomplete: No threshold for leafs specified.");
            throw new ConfigurationException("Initialization incomplete: No threshold for leafs specified.");
        }

        final int maxLeafSize = configuration.getIntProperty(ConfigurationKeys.MAX_LEAF_SIZE, 7);
        if (maxLeafSize < 0) {
            ClassificationCompositeStage.LOGGER.error("Initialization incomplete: No max leaf size specified.");
            throw new ConfigurationException("Initialization incomplete: No max leaf size specified.");
        }

        final int maxNodeSize = configuration.getIntProperty(ConfigurationKeys.MAX_NODE_SIZE, -1);
        if (maxNodeSize < 0) {
            ClassificationCompositeStage.LOGGER.error("Initialization incomplete: No max node size specified.");
            throw new ConfigurationException("Initialization incomplete: No max node size specified..");
        }

        final int maxLeafEntries = configuration.getIntProperty(ConfigurationKeys.MAX_LEAF_ENTRIES, -1);
        if (maxLeafEntries < 0) {
            ClassificationCompositeStage.LOGGER
                    .error("Initialization incomplete: No max number of leaf entries specified.");
            throw new ConfigurationException("Initialization incomplete: No max number of leaf entries specified.");
        }
        ClassificationCompositeStage.LOGGER.debug("Max leaf entries: " + maxLeafEntries);
        final int expectedNumberOfClusters = configuration.getIntProperty(ConfigurationKeys.EXP_NUM_OF_CLUSTERS, -1);
        if (expectedNumberOfClusters < 0) {
            ClassificationCompositeStage.LOGGER
                    .error("Initialization incomplete: No expected numbers of clusters specified.");
            throw new ConfigurationException("Initialization incomplete: No expected numbers of clusters specified.");
        }

        /** Create remaining stages and connect them */
        final UserSessionGeneratorCompositeStage userSessionGeneratorCompositeStage = new UserSessionGeneratorCompositeStage(
                configuration);

        final BehaviorModelSink behaviorModelSinkStage = new BehaviorModelSink(baseURL,
                new GetLastXSignatureStrategy(Integer.MAX_VALUE));

        final String classificationStageName = configuration.getStringProperty(ConfigurationKeys.CLASS_STAGE);

        final IClassificationStage classificationStage = InstantiationFactory
                .createWithConfiguration(IClassificationStage.class, classificationStageName, configuration);

        this.eventBasedTraceInputPort = userSessionGeneratorCompositeStage.getTraceInputPort();
        this.sessionEventInputPort = userSessionGeneratorCompositeStage.getSessionEventInputPort();

        this.connectPorts(userSessionGeneratorCompositeStage.getSessionOutputPort(), classificationStage.getSessionInputPort());
        /** reconnect once SessionsToInstances filter has been modified */
        this.connectPorts(userSessionGeneratorCompositeStage.getTimerOutputPort(), classificationStage.getTimerInputPort());
        this.connectPorts(classificationStage.getOutputPort(), behaviorModelSinkStage.getInputPort());
    }

    @Override
    public InputPort<EventBasedTrace> getEventBasedTraceInputPort() {
        return this.eventBasedTraceInputPort;
    }

    @Override
    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.sessionEventInputPort;
    }

}