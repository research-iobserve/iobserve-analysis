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
        /** Get base URL for BehaviourVisualization */
        final String visualizationURL = this.getRequiredStringProperty(configuration,
                ConfigurationKeys.BEHAVIOR_VISUALIZATION_URL, "No sink visualization URL specified.");
        configuration.getStringProperty(ConfigurationKeys.BEHAVIOR_VISUALIZATION_URL);

        /** Get base URL for BehaviourModelSink */
        final String baseURL = this.getRequiredStringProperty(configuration, ConfigurationKeys.SINK_BASE_URL,
                "No sink base URL specified.");

        /** Get keep time for user sessions */
        final long keepTime = this.getRequiredLongProperty(configuration, ConfigurationKeys.KEEP_TIME,
                "No keep time interval specified.", -1);

        final int minCollectionSize = this.getRequiredIntProperty(configuration, ConfigurationKeys.MIN_SIZE,
                "No min size for user sessions specified.", -1);

        final double leafThresholdValue = this.getRequiredDoubleProperty(configuration, ConfigurationKeys.LEAF_TH,
                "No threshold for leafs specified.", -1);

        final int maxLeafSize = this.getRequiredIntProperty(configuration, ConfigurationKeys.MAX_LEAF_SIZE,
                "No max leaf size specified.", 7);

        final int maxNodeSize = this.getRequiredIntProperty(configuration, ConfigurationKeys.MAX_NODE_SIZE,
                "No max node size specified.", -1);

        final int maxLeafEntries = this.getRequiredIntProperty(configuration, ConfigurationKeys.MAX_LEAF_ENTRIES,
                "No max number of leaf entries specified.", -1);
        ClassificationCompositeStage.LOGGER.debug("Max leaf entries: {}", maxLeafEntries);

        final int expectedNumberOfClusters = this.getRequiredIntProperty(configuration,
                ConfigurationKeys.EXP_NUM_OF_CLUSTERS, "No expected numbers of clusters specified.", -1);

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

        this.connectPorts(userSessionGeneratorCompositeStage.getSessionOutputPort(),
                classificationStage.getSessionInputPort());
        /** reconnect once SessionsToInstances filter has been modified */
        this.connectPorts(userSessionGeneratorCompositeStage.getTimerOutputPort(),
                classificationStage.getTimerInputPort());
        this.connectPorts(classificationStage.getOutputPort(), behaviorModelSinkStage.getInputPort());
    }

    private String getRequiredStringProperty(final Configuration configuration, final String key, final String message)
            throws ConfigurationException {
        final String value = configuration.getStringProperty(key);
        if (value.isEmpty()) {
            ClassificationCompositeStage.LOGGER.error("Initialization incomplete: {}", message);
            throw new ConfigurationException("Initialization incomplete: " + message);
        } else {
            return value;
        }
    }

    private int getRequiredIntProperty(final Configuration configuration, final String key, final String message,
            final int defaultValue) throws ConfigurationException {
        final int value = configuration.getIntProperty(key, defaultValue);
        if (value < 0) {
            ClassificationCompositeStage.LOGGER.error("Initialization incomplete: {}", message);
            throw new ConfigurationException("Initialization incomplete: " + message);
        } else {
            return value;
        }
    }

    private long getRequiredLongProperty(final Configuration configuration, final String key, final String message,
            final long defaultValue) throws ConfigurationException {
        final long value = configuration.getLongProperty(key, defaultValue);
        if (value < 0) {
            ClassificationCompositeStage.LOGGER.error("Initialization incomplete: {}", message);
            throw new ConfigurationException("Initialization incomplete: " + message);
        } else {
            return value;
        }
    }

    private double getRequiredDoubleProperty(final Configuration configuration, final String key, final String message,
            final double defaultValue) throws ConfigurationException {
        final double value = configuration.getDoubleProperty(key, defaultValue);
        if (value < 0) {
            ClassificationCompositeStage.LOGGER.error("Initialization incomplete: {}", message);
            throw new ConfigurationException("Initialization incomplete: " + message);
        } else {
            return value;
        }
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