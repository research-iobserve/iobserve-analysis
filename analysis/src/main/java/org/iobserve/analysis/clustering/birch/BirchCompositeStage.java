
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
package org.iobserve.analysis.clustering.birch;

import org.iobserve.analysis.clustering.filter.models.configuration.GetLastXSignatureStrategy;
import org.iobserve.analysis.clustering.filter.models.configuration.IModelGenerationFilterFactory;
import org.iobserve.analysis.clustering.filter.models.configuration.IRepresentativeStrategy;
import org.iobserve.analysis.clustering.filter.models.configuration.examples.JPetstoreStrategy;
import org.iobserve.analysis.clustering.shared.PreprocessingCompositeStage;
import org.iobserve.analysis.configurations.ConfigurationKeys;
import org.iobserve.analysis.feature.IBehaviorCompositeStage;
import org.iobserve.analysis.session.IEntryCallAcceptanceMatcher;
import org.iobserve.analysis.sink.BehaviorModelSink;
import org.iobserve.analysis.traces.ITraceSignatureCleanupRewriter;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.general.ConfigurationException;
import org.iobserve.stages.general.IEntryCallTraceMatcher;
import org.iobserve.stages.source.TimeTriggerFilter;
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
 * @author Melf Lorenzen
 *
 */
@ReceiveUnfilteredConfiguration
public class BirchCompositeStage extends CompositeStage implements IBehaviorCompositeStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(BirchCompositeStage.class);

    private final InputPort<EventBasedTrace> eventBasedTraceInputPort;
    private final InputPort<ISessionEvent> sessionEventInputPort;

    public BirchCompositeStage(final Configuration configuration) throws ConfigurationException {
        /** Instantiate configurable objects/properties for stages */

        /** For EntryCallStage */
        final String traceMatcherClassName = configuration.getStringProperty(ConfigurationKeys.TRACE_MATCHER);
        if (traceMatcherClassName.isEmpty()) {
            BirchCompositeStage.LOGGER.error("Initialization incomplete: No trace matcher specified.");
            throw new ConfigurationException("Initialization incomplete: No trace matcher specified.");
        }
        final IEntryCallTraceMatcher traceMatcher = InstantiationFactory.create(IEntryCallTraceMatcher.class,
                traceMatcherClassName, null);

        /** For SessionAcceptanceFilter */
        final String entryCallMatcherClassName = configuration
                .getStringProperty(ConfigurationKeys.ENTRY_CALL_ACCEPTANCE_MATCHER);
        if (entryCallMatcherClassName.isEmpty()) {
            BirchCompositeStage.LOGGER
                    .error("Initialization incomplete: No entry call acceptance matcher specified.");
            throw new ConfigurationException("Initialization incomplete: No entry call acceptance matcher specified.");
        }
        final IEntryCallAcceptanceMatcher entryCallMatcher = InstantiationFactory
                .create(IEntryCallAcceptanceMatcher.class, entryCallMatcherClassName, null);

        /** For TraceOperationsCleanupFilter */
        final String cleanupRewriterClassName = configuration.getStringProperty(ConfigurationKeys.CLEANUP_REWRITER);
        if (cleanupRewriterClassName.isEmpty()) {
            BirchCompositeStage.LOGGER.error("Initialization incomplete: No signature cleanup rewriter specified.");
            throw new ConfigurationException("Initialization incomplete: No signature cleanup rewriter specified.");
        }
        final ITraceSignatureCleanupRewriter cleanupRewriter = InstantiationFactory
                .create(ITraceSignatureCleanupRewriter.class, cleanupRewriterClassName, null);

        /** For TSessionOperationsFilter */
        final String entryCallFilterFactoryClassName = configuration
                .getStringProperty(ConfigurationKeys.ENTRY_CALL_FILTER_RULES_FACTORY);
        if (entryCallFilterFactoryClassName.isEmpty()) {
            BirchCompositeStage.LOGGER
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
            BirchCompositeStage.LOGGER.error("Initialization incomplete: No time trigger interval specified.");
            throw new ConfigurationException("Initialization incomplete: No time trigger interval specified.");
        }

        /** Get base URL for BehaviourModelSink */
        final String baseURL = configuration.getStringProperty(ConfigurationKeys.SINK_BASE_URL);
        if (baseURL.isEmpty()) {
            BirchCompositeStage.LOGGER.error("Initialization incomplete: No sink base URL specified.");
            throw new ConfigurationException("Initialization incomplete: No sink base URL specified.");
        }

        /** Instantiate IClassificationStage */

        ///final String classificationStageClassName = configuration
        ///        .getStringProperty(ConfigurationKeys.CLASSIFICATION_STAGE);
        ///if (classificationStageClassName.isEmpty()) {
        ///    BirchCompositeStage.LOGGER.error("Initialization incomplete: No classification stage specified.");
        ///    throw new ConfigurationException("Initialization incomplete: No classification stage specified.");
        ///}
        /// Jannis spezifisch?
        ///final IClassificationStage classificationStage = InstantiationFactory
        ///        .createWithConfiguration(IClassificationStage.class, classificationStageClassName, configuration);
        
        /** Get keep time for user sessions*/
        final long keepTime = configuration.getLongProperty(ConfigurationKeys.KEEP_TIME, -1);
        if (keepTime < 0) {
            BirchCompositeStage.LOGGER.error("Initialization incomplete: No keep time interval specified.");
            throw new ConfigurationException("Initialization incomplete: No keep time interval specified.");
        }
        
        final int minCollectionSize = configuration.getIntProperty(ConfigurationKeys.MIN_SIZE, -1);
        if (minCollectionSize < 0) {
            BirchCompositeStage.LOGGER.error("Initialization incomplete: No min size for user sessions specified.");
            throw new ConfigurationException("Initialization incomplete: No min size for user sessions specified.");
        }
        
		final double leafThresholdValue = configuration.getDoubleProperty(ConfigurationKeys.LEAF_TH, -1.0);
        if (leafThresholdValue < 0) {
            BirchCompositeStage.LOGGER.error("Initialization incomplete: No threshold for leafs specified.");
            throw new ConfigurationException("Initialization incomplete: No threshold for leafs specified.");
        }
        
		final int maxLeafSize = configuration.getIntProperty(ConfigurationKeys.MAX_LEAF_SIZE, 7);
        if (maxLeafSize < 0) {
            BirchCompositeStage.LOGGER.error("Initialization incomplete: No max leaf size specified.");
            throw new ConfigurationException("Initialization incomplete: No max leaf size specified.");
        }
        
		final int maxNodeSize = configuration.getIntProperty(ConfigurationKeys.MAX_NODE_SIZE, -1);
        if (maxNodeSize < 0) {
            BirchCompositeStage.LOGGER.error("Initialization incomplete: No max node size specified.");
            throw new ConfigurationException("Initialization incomplete: No max node size specified..");
        }
        
		final int maxLeafEntries= configuration.getIntProperty(ConfigurationKeys.MAX_LEAF_ENTRIES, -1);
        if (maxLeafEntries < 0) {
            BirchCompositeStage.LOGGER.error("Initialization incomplete: No max number of leaf entries specified.");
            throw new ConfigurationException("Initialization incomplete: No max number of leaf entries specified.");
        }
        
        /** Todo: incoperate to config */
		final IRepresentativeStrategy representativeStrategy = new JPetstoreStrategy();
		
		final boolean keepEmptyTransitions = configuration.getBooleanProperty(ConfigurationKeys.KEEP_EMPTY_TRANS, true);
        
        
        /** Create remaining stages and connect them */
        final PreprocessingCompositeStage preStage = new PreprocessingCompositeStage(traceMatcher, entryCallMatcher,
                cleanupRewriter, filterRulesFactory, triggerInterval);
        
        final BirchClassificaton classificationStage = new BirchClassificaton(keepTime, minCollectionSize, 
        		representativeStrategy, keepEmptyTransitions, leafThresholdValue, maxLeafSize, maxNodeSize,
        		maxLeafEntries);
        /// Jannis spezifisch?
//        final BehaviorModelCompositeSinkStage sinkStage = new BehaviorModelCompositeSinkStage(baseURL);
        BehaviorModelSink sinkStage = new BehaviorModelSink(baseURL, 
        		new GetLastXSignatureStrategy(Integer.MAX_VALUE));

        
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