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
package org.iobserve.analysis.behavior.filter.similaritymatching;

import org.iobserve.analysis.behavior.filter.models.configuration.IModelGenerationFilterFactory;
import org.iobserve.analysis.configurations.ConfigurationKeys;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.analysis.session.IEntryCallAcceptanceMatcher;
import org.iobserve.analysis.traces.ITraceSignatureCleanupRewriter;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.general.ConfigurationException;
import org.iobserve.stages.general.IEntryCallTraceMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;
import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

/**
 * Configurable composite stage combining pre-processing of monitoring records,
 * aggregation and generation of behavior models, and outputting them into files
 *
 * @author Jannis Kuckei
 *
 */
@ReceiveUnfilteredConfiguration
public class BehaviorCompositeStage extends CompositeStage implements IBehaviorCompositeStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(BehaviorCompositeStage.class);

    private final InputPort<EventBasedTrace> eventBasedTraceInputPort;
    private final InputPort<ISessionEvent> sessionEventInputPort;

    public BehaviorCompositeStage(final Configuration configuration) throws ConfigurationException {
        /** Instantiate configurable objects/properties for stages */

        /** For EntryCallStage */
        final String traceMatcherClassName = configuration.getStringProperty(ConfigurationKeys.TRACE_MATCHER);
        if (traceMatcherClassName.isEmpty()) {
            BehaviorCompositeStage.LOGGER.error("Initialization incomplete: No trace matcher specified.");
            throw new ConfigurationException("Initialization incomplete: No trace matcher specified.");
        }
        final IEntryCallTraceMatcher traceMatcher = InstantiationFactory.create(IEntryCallTraceMatcher.class,
                traceMatcherClassName, null);

        /** For SessionAcceptanceFilter */
        final String entryCallMatcherClassName = configuration
                .getStringProperty(ConfigurationKeys.ENTRY_CALL_ACCEPTANCE_MATCHER);
        if (entryCallMatcherClassName.isEmpty()) {
            BehaviorCompositeStage.LOGGER
                    .error("Initialization incomplete: No entry call acceptance matcher specified.");
            throw new ConfigurationException("Initialization incomplete: No entry call acceptance matcher specified.");
        }
        final IEntryCallAcceptanceMatcher entryCallMatcher = InstantiationFactory
                .create(IEntryCallAcceptanceMatcher.class, entryCallMatcherClassName, null);

        /** For TraceOperationsCleanupFilter */
        final String cleanupRewriterClassName = configuration.getStringProperty(ConfigurationKeys.CLEANUP_REWRITER);
        if (cleanupRewriterClassName.isEmpty()) {
            BehaviorCompositeStage.LOGGER.error("Initialization incomplete: No signature cleanup rewriter specified.");
            throw new ConfigurationException("Initialization incomplete: No signature cleanup rewriter specified.");
        }
        final ITraceSignatureCleanupRewriter cleanupRewriter = InstantiationFactory
                .create(ITraceSignatureCleanupRewriter.class, cleanupRewriterClassName, null);

        /** For TSessionOperationsFilter */
        final String entryCallFilterFactoryClassName = configuration
                .getStringProperty(ConfigurationKeys.ENTRY_CALL_FILTER_RULES_FACTORY);
        if (entryCallFilterFactoryClassName.isEmpty()) {
            BehaviorCompositeStage.LOGGER
                    .error("Initialization incomplete: No entry call filter rules factory specified.");
            throw new ConfigurationException(
                    "Initialization incomplete: No entry call filter rules factory specified.");
        }
        final IModelGenerationFilterFactory filterRulesFactory = InstantiationFactory
                .create(IModelGenerationFilterFactory.class, entryCallFilterFactoryClassName, null);

        /**
         * Create Clock. Default value of -1 was selected because the method
         * getLongProperty states it will return "null" if no value was specified, but
         * long is a primitive type ...
         */
        final Long triggerInterval = configuration.getLongProperty(ConfigurationKeys.TRIGGER_INTERVAL, -1);
        if (triggerInterval < 0) {
            BehaviorCompositeStage.LOGGER.error("Initialization incomplete: No time trigger interval specified.");
            throw new ConfigurationException("Initialization incomplete: No time trigger interval specified.");
        }

        /** Get base URL for BehaviourModelSink */
        final String baseURL = configuration.getStringProperty(ConfigurationKeys.SINK_BASE_URL);
        if (baseURL.isEmpty()) {
            BehaviorCompositeStage.LOGGER.error("Initialization incomplete: No sink base URL specified.");
            throw new ConfigurationException("Initialization incomplete: No sink base URL specified.");
        }

        /** Instantiate IClassificationStage */

        final String classificationStageClassName = configuration
                .getStringProperty(ConfigurationKeys.CLASSIFICATION_STAGE);
        if (classificationStageClassName.isEmpty()) {
            BehaviorCompositeStage.LOGGER.error("Initialization incomplete: No classification stage specified.");
            throw new ConfigurationException("Initialization incomplete: No classification stage specified.");
        }
        final IClassificationStage classificationStage = InstantiationFactory
                .createWithConfiguration(IClassificationStage.class, classificationStageClassName, configuration);

        /** Create remaining stages and connect them */
        final PreprocessingCompositeStage preStage = new PreprocessingCompositeStage(traceMatcher, entryCallMatcher,
                cleanupRewriter, filterRulesFactory, triggerInterval);
        final BehaviorModelCompositeSinkStage sinkStage = new BehaviorModelCompositeSinkStage(baseURL);

        this.eventBasedTraceInputPort = preStage.getTraceInputPort();
        this.sessionEventInputPort = preStage.getSessionEventInputPort();

        this.connectPorts(preStage.getSessionOutputPort(), classificationStage.getSessionInputPort());
        this.connectPorts(preStage.getTimerOutputPort(), classificationStage.getTimerInputPort());
        this.connectPorts(classificationStage.getOutputPort(), sinkStage.getInputPort());
    }

    @Override
    public InputPort<EventBasedTrace> getEventBasedTracePort() {
        return this.eventBasedTraceInputPort;
    }

    @Override
    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.sessionEventInputPort;
    }

}
