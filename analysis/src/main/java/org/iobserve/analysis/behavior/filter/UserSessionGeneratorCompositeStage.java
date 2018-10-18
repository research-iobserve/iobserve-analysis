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
package org.iobserve.analysis.behavior.filter;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;
import teetime.stage.basic.distributor.strategy.IDistributorStrategy;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.analysis.behavior.clustering.similaritymatching.UserSessionOperationCleanupStage;
import org.iobserve.analysis.behavior.models.data.configuration.IModelGenerationFilterFactory;
import org.iobserve.analysis.session.IEntryCallAcceptanceMatcher;
import org.iobserve.analysis.session.SessionAcceptanceFilter;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.analysis.traces.EntryCallSequence;
import org.iobserve.analysis.traces.ITraceSignatureCleanupRewriter;
import org.iobserve.analysis.traces.TraceOperationCleanupFilter;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.service.InstantiationFactory;
import org.iobserve.stages.data.trace.EventBasedTrace;
import org.iobserve.stages.general.ConfigurationException;
import org.iobserve.stages.general.EntryCallStage;
import org.iobserve.stages.general.IEntryCallTraceMatcher;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;
import org.iobserve.stages.source.SynthesizedTimeTriggerStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Processes session events and traces and generates user sessions. In addition it also encapsulates
 * the timer stage to generate signals at regular intervals.
 *
 * Inputs are: @{link ISessionEvent}s and @{link EventBasedTrace}s
 *
 * Outputs are: @{link Long}s representing timestamps and @{link UserSession}s
 *
 * @author Jannis Kuckei
 *
 */
@ReceiveUnfilteredConfiguration
public class UserSessionGeneratorCompositeStage extends CompositeStage {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionGeneratorCompositeStage.class);

    private final SynthesizedTimeTriggerStage sessionCollectionTimer;
    private final UserSessionOperationCleanupStage userSessionOperationCleanupStage;
    private final EntryCallStage entryCallStage;
    private final EntryCallSequence entryCallSequence;

    /**
     * Create the user session generator stage.
     *
     * @param configuration
     *            containing all configuration parameters
     * @throws ConfigurationException
     *             on errors in configuration
     */
    public UserSessionGeneratorCompositeStage(final Configuration configuration) throws ConfigurationException {
        /**
         * Create Clock. Default value of -1 was selected because the method getLongProperty states
         * it will return "null" if no value was specified, but long is a primitive type ...
         */
        final Long triggerInterval = configuration.getLongProperty(ConfigurationKeys.TRIGGER_INTERVAL, -1);
        if (triggerInterval < 0) {
            UserSessionGeneratorCompositeStage.LOGGER
                    .error("Initialization incomplete: No time trigger interval specified.");
            throw new ConfigurationException("Initialization incomplete: No time trigger interval specified.");
        }

        /** For EntryCallStage. */
        final IEntryCallTraceMatcher traceMatcher = this.createFromConfiguration(IEntryCallTraceMatcher.class,
                configuration, ConfigurationKeys.TRACE_MATCHER, "No trace matcher specified.");
        /** For SessionAcceptanceFilter. */
        final IEntryCallAcceptanceMatcher entryCallMatcher = this.createFromConfiguration(
                IEntryCallAcceptanceMatcher.class, configuration, ConfigurationKeys.ENTRY_CALL_ACCEPTANCE_MATCHER,
                "No entry call acceptance matcher specified.");

        /** For TraceOperationsCleanupFilter */
        final ITraceSignatureCleanupRewriter cleanupRewriter = this.createFromConfiguration(
                ITraceSignatureCleanupRewriter.class, configuration, ConfigurationKeys.CLEANUP_REWRITER,
                "No signature cleanup rewriter specified.");

        /** For TSessionOperationsFilter */
        final IModelGenerationFilterFactory filterRulesFactory = this.createFromConfiguration(
                IModelGenerationFilterFactory.class, configuration, ConfigurationKeys.ENTRY_CALL_FILTER_RULES_FACTORY,
                "No entry call filter rules factory specified.");

        /** multi or single event mode. */
        final boolean singleEventMode = configuration.getBooleanProperty(ConfigurationKeys.SINGLE_EVENT_MODE, false);

        /** -- create stages. -- */
        /** Create EntryCallStage */
        this.entryCallStage = new EntryCallStage(traceMatcher);
        /** Create EntryCallSequence */
        this.entryCallSequence = new EntryCallSequence();
        /** Create SessionAcceptanceFilter */
        final SessionAcceptanceFilter sessionAcceptanceFilter = new SessionAcceptanceFilter(entryCallMatcher);
        /** Create TraceOperationsCleanupFilter */
        final TraceOperationCleanupFilter traceOperationCleanupFilter = new TraceOperationCleanupFilter(
                cleanupRewriter);
        /** Create UserSessionOperationsFilter */
        this.userSessionOperationCleanupStage = new UserSessionOperationCleanupStage(filterRulesFactory.createFilter());

        /** Create Clock */
        // TODO the following code snippet uses a synthesized clock
        this.sessionCollectionTimer = new SynthesizedTimeTriggerStage(triggerInterval);
        // new TimeTriggerStage(triggerInterval, singleEventMode);
        final IDistributorStrategy strategy = new CopyByReferenceStrategy();
        final Distributor<PayloadAwareEntryCallEvent> distributor = new Distributor<>(strategy);

        /** Connect all ports */
        this.connectPorts(this.entryCallStage.getOutputPort(), distributor.getInputPort());
        this.connectPorts(distributor.getNewOutputPort(), this.sessionCollectionTimer.getInputPort());

        this.connectPorts(distributor.getNewOutputPort(), this.entryCallSequence.getEntryCallInputPort());
        this.connectPorts(this.entryCallSequence.getUserSessionOutputPort(), sessionAcceptanceFilter.getInputPort());
        this.connectPorts(sessionAcceptanceFilter.getOutputPort(), traceOperationCleanupFilter.getInputPort());
        this.connectPorts(traceOperationCleanupFilter.getOutputPort(),
                this.userSessionOperationCleanupStage.getInputPort());
    }

    private <T> T createFromConfiguration(final Class<T> clazz, final Configuration configuration, final String key,
            final String errorMessage) throws ConfigurationException {
        final String className = configuration.getStringProperty(key);
        if (className.isEmpty()) {
            UserSessionGeneratorCompositeStage.LOGGER.error("Initialization incomplete: {}", errorMessage);
            throw new ConfigurationException(String.format("Initialization incomplete: %s", errorMessage));
        }
        return InstantiationFactory.create(clazz, className, null);
    }

    public InputPort<EventBasedTrace> getTraceInputPort() {
        return this.entryCallStage.getInputPort();
    }

    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.entryCallSequence.getSessionEventInputPort();
    }

    public OutputPort<UserSession> getSessionOutputPort() {
        return this.userSessionOperationCleanupStage.getOutputPort();
    }

    public OutputPort<Long> getTimerOutputPort() {
        return this.sessionCollectionTimer.getOutputPort();
    }
}
