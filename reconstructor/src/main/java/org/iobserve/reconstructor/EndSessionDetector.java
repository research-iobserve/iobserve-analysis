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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.misc.EmptyRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.SessionEndEvent;
import org.iobserve.common.record.SessionStartEvent;

/**
 * Filter collects all events until it receives an EmptyRecord. Then it flushes out all sessions and
 * adds a proper SessionEndEvent.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class EndSessionDetector extends AbstractConsumerStage<IMonitoringRecord> {

    private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort(IMonitoringRecord.class);

    private final Map<String, List<IMonitoringRecord>> sessionRecords = new HashMap<>();
    private final Map<Long, String> traceIdMap = new HashMap<>();
    private final List<IMonitoringRecord> allRecords = new ArrayList<>();

    private final Map<Long, List<IMonitoringRecord>> stash = new HashMap<>();

    @Override
    protected void execute(final IMonitoringRecord event) throws Exception {
        if (event instanceof SessionStartEvent) {
            this.receiveSessionStartEvent((SessionStartEvent) event);
        } else if (event instanceof TraceMetadata) {
            this.receiveTraceMetadata((TraceMetadata) event);
        } else if (event instanceof ITraceRecord) {
            this.receiveTraceRecord((ITraceRecord) event);
        } else if (event instanceof EmptyRecord) {
            this.flushAllRecords();
        }

        this.allRecords.add(event);
    }

    private void receiveSessionStartEvent(final SessionStartEvent event) {
        List<IMonitoringRecord> recordList = this.sessionRecords.get(event.getSessionId());
        if (recordList == null) { /** new session. */
            recordList = new ArrayList<>();
        }
        recordList.add(event);
        this.sessionRecords.put(event.getSessionId(), recordList);
    }

    private void receiveTraceMetadata(final TraceMetadata traceMetadata) {
        if (this.traceIdMap.get(traceMetadata.getTraceId()) == null) {
            this.traceIdMap.put(traceMetadata.getTraceId(), traceMetadata.getSessionId());

            List<IMonitoringRecord> recordList = this.sessionRecords.get(traceMetadata.getSessionId());
            if (recordList == null) {
                recordList = new ArrayList<>();
            }
            recordList.add(traceMetadata);

            /** copy stash. */
            final List<IMonitoringRecord> stashedRecords = this.stash.get(traceMetadata.getTraceId());

            if (stashedRecords != null) {
                recordList.addAll(stashedRecords);
                this.stash.remove(traceMetadata.getTraceId());
            }

            this.sessionRecords.put(traceMetadata.getSessionId(), recordList);
        }
    }

    private void receiveTraceRecord(final ITraceRecord event) {
        final String sessionId = this.traceIdMap.get(event.getTraceId());

        if (sessionId != null) {
            this.sessionRecords.get(sessionId).add(event);
        } else {
            /** session and trace did not start yet. Stash element. */
            final Long traceId = event.getTraceId();
            List<IMonitoringRecord> stashedRecords = this.stash.get(traceId);
            if (stashedRecords == null) {
                stashedRecords = new ArrayList<>();
            }
            stashedRecords.add(event);

            this.stash.put(traceId, stashedRecords);
        }

    }

    private void flushAllRecords() {
        for (final IMonitoringRecord event : this.allRecords) {
            if (event instanceof ITraceRecord) {
                final ITraceRecord traceEvent = (ITraceRecord) event;

                final Pair firstLast = this.getFirstLast(traceEvent);

                this.outputPort.send(event);

                if (firstLast != null) {
                    if (firstLast.getLast() == traceEvent) {
                        if (firstLast.getFirst() instanceof SessionStartEvent) {
                            final SessionStartEvent sessionStartEvent = (SessionStartEvent) firstLast.getFirst();
                            this.outputPort.send(new SessionEndEvent(firstLast.getLast().getLoggingTimestamp(),
                                    sessionStartEvent.getHostname(), sessionStartEvent.getSessionId()));
                        } else if (firstLast.getFirst() instanceof TraceMetadata) {
                            final TraceMetadata metadata = (TraceMetadata) firstLast.getFirst();
                            this.outputPort.send(new SessionEndEvent(firstLast.getLast().getLoggingTimestamp(),
                                    metadata.getHostname(), metadata.getSessionId()));
                        }

                    }
                }
            } else {
                this.outputPort.send(event);
            }
        }
    }

    private Pair getFirstLast(final ITraceRecord traceEvent) {
        final String sessionId = this.traceIdMap.get(traceEvent.getTraceId());

        if (sessionId != null) {
            final List<IMonitoringRecord> records = this.sessionRecords.get(sessionId);

            return new Pair(records.get(0), (ITraceRecord) records.get(records.size() - 1));
        } else {
            return null;
        }
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

}
