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
package org.iobserve.reconstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.flow.trace.TraceMetadata;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.SessionEndEvent;
import org.iobserve.common.record.SessionStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter collects all events until it receives an EmptyRecord. Then it flushes out all sessions and
 * adds a proper SessionEndEvent.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class EndSessionDetector extends AbstractConsumerStage<IMonitoringRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndSessionDetector.class);

    private static final long SESSION_TIMEOUT = 1000L * 1000L * 1000L * 60L * 60L * 24L;

    private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort(IMonitoringRecord.class);

    private final Map<Long, TraceEntry> traceIdMap = new HashMap<>();

    private final Map<String, SessionEntry> sessionMap = new HashMap<>();

    @Override
    protected void execute(final IMonitoringRecord event) throws Exception {
        // System.err.println(event.getLoggingTimestamp() + " = " + event.getClass() + ": " +
        // event.toString());
        if (event instanceof SessionStartEvent) {
            this.receiveSessionStartEvent((SessionStartEvent) event);
        } else if (event instanceof TraceMetadata) {
            this.receiveTraceMetadata((TraceMetadata) event);
        } else if (event instanceof ITraceRecord) {
            this.receiveTraceRecord((ITraceRecord) event);
        }

        final long loggingTimeStamp = event.getLoggingTimestamp();
        this.outputPort.send(event);
        this.flushSessions(loggingTimeStamp);
    }

    private void flushSessions(final long loggingTimestamp) {
        final Collection<SessionEntry> sessions = new ArrayList<>();
        sessions.addAll(this.sessionMap.values());
        for (final SessionEntry entry : sessions) {
            // System.err.println(entry.getTimestamp() + " == " + loggingTimestamp + " "
            // + (entry.getTimestamp() + EndSessionDetector.SESSION_TIMEOUT - loggingTimestamp));
            if (entry.getTimestamp() + EndSessionDetector.SESSION_TIMEOUT < loggingTimestamp) {
                this.outputPort
                        .send(new SessionEndEvent(entry.getTimestamp(), entry.getHostname(), entry.getSessionId()));
                this.sessionMap.remove(entry.getSessionId());
                final Collection<TraceEntry> traces = new ArrayList<>();
                traces.addAll(this.traceIdMap.values());
                for (final TraceEntry traceEntry : traces) {
                    if (traceEntry.getSessionId().equals(entry.getSessionId())) {
                        this.traceIdMap.remove(traceEntry.getTraceId());
                    }
                }
            }
        }
    }

    @Override
    protected void onTerminating() {
        for (final SessionEntry entry : this.sessionMap.values()) {
            this.outputPort.send(new SessionEndEvent(entry.getTimestamp(), entry.getHostname(), entry.getSessionId()));
        }
        super.onTerminating();
    }

    private void receiveSessionStartEvent(final SessionStartEvent event) {
        this.sessionMap.put(event.getSessionId(), new SessionEntry(event));
    }

    private void receiveTraceMetadata(final TraceMetadata traceMetadata) {
        if (this.traceIdMap.get(traceMetadata.getTraceId()) == null) {
            this.traceIdMap.put(traceMetadata.getTraceId(), new TraceEntry(traceMetadata));
        }
    }

    private void receiveTraceRecord(final ITraceRecord event) {
        final TraceEntry metadata = this.traceIdMap.get(event.getTraceId());
        if (metadata != null) {
            metadata.setTimestamp(event.getLoggingTimestamp());
            final SessionEntry sessionEntry = this.sessionMap.get(metadata.getSessionId());
            sessionEntry.setTimestamp(metadata.getTimestamp());
        } else {
            EndSessionDetector.LOGGER.error("Event has no trace {} {}", event.getClass(), event.toString());
        }
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

}
