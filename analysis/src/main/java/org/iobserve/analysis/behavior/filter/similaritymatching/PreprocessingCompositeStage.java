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
import org.iobserve.analysis.session.IEntryCallAcceptanceMatcher;
import org.iobserve.analysis.session.SessionAcceptanceFilter;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.analysis.traces.EntryCallSequence;
import org.iobserve.analysis.traces.ITraceSignatureCleanupRewriter;
import org.iobserve.analysis.traces.TraceOperationCleanupFilter;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.stages.general.EntryCallStage;
import org.iobserve.stages.general.IEntryCallTraceMatcher;
import org.iobserve.stages.source.TimeTriggerFilter;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

/**
 * Pre-processes monitoring records into user sessions and also provides a timer
 * stage to generate signals at regular intervals
 *
 * @author Jannis Kuckei
 *
 */
public class PreprocessingCompositeStage extends CompositeStage {
    private final InputPort<EventBasedTrace> traceInputPort;
    private final InputPort<ISessionEvent> sessionEventInputPort;

    private final OutputPort<UserSession> sessionOutputPort;
    private final OutputPort<Long> timerOutputPort;

    /**
     * Constructor
     * 
     * @param traceMatcher
     * @param entryCallMatcher
     * @param cleanupRewriter
     * @param filterRulesFactory
     * @param triggerInterval
     */
    public PreprocessingCompositeStage(final IEntryCallTraceMatcher traceMatcher,
            final IEntryCallAcceptanceMatcher entryCallMatcher, final ITraceSignatureCleanupRewriter cleanupRewriter,
            final IModelGenerationFilterFactory filterRulesFactory, final long triggerInterval) {

        /** Create EntryCallStage */
        final EntryCallStage entryCallStage = new EntryCallStage(traceMatcher);
        /** Create EntryCallSequence */
        final EntryCallSequence entryCallSequence = new EntryCallSequence();
        /** Create SessionAcceptanceFilter */
        final SessionAcceptanceFilter sessionAcceptanceFilter = new SessionAcceptanceFilter(entryCallMatcher);
        /** Create TraceOperationsCleanupFilter */
        final TraceOperationCleanupFilter traceOperationCleanupFilter = new TraceOperationCleanupFilter(
                cleanupRewriter);
        /** Create UserSessionOperationsFilter */
        final UserSessionOperationCleanupStage sessionOperationsFilter = new UserSessionOperationCleanupStage(
                filterRulesFactory.createFilter());
        /** Create Clock */
        final TimeTriggerFilter sessionCollectionTimer = new TimeTriggerFilter(triggerInterval);

        /** Connect all ports */
        this.traceInputPort = entryCallStage.getInputPort();
        this.sessionEventInputPort = entryCallSequence.getSessionEventInputPort();

        this.connectPorts(entryCallStage.getOutputPort(), entryCallSequence.getEntryCallInputPort());
        this.connectPorts(entryCallSequence.getUserSessionOutputPort(), sessionAcceptanceFilter.getInputPort());
        this.connectPorts(sessionAcceptanceFilter.getOutputPort(), traceOperationCleanupFilter.getInputPort());
        this.connectPorts(traceOperationCleanupFilter.getOutputPort(), sessionOperationsFilter.getInputPort());

        this.sessionOutputPort = sessionOperationsFilter.getOutputPort();
        this.timerOutputPort = sessionCollectionTimer.getOutputPort();
    }

    public InputPort<EventBasedTrace> getTraceInputPort() {
        return this.traceInputPort;
    }

    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.sessionEventInputPort;
    }

    public OutputPort<UserSession> getSessionOutputPort() {
        return this.sessionOutputPort;
    }

    public OutputPort<Long> getTimerOutputPort() {
        return this.timerOutputPort;
    }
}
