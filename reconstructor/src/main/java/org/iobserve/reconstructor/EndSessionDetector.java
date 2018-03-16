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
import java.util.Map.Entry;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.misc.EmptyRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.SessionEndEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class EndSessionDetector extends AbstractConsumerStage<IMonitoringRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EndSessionDetector.class);

    private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort(IMonitoringRecord.class);

    private final Map<String, List<IMonitoringRecord>> sessionRecords = new HashMap<>();
    private final Map<String, Long> traceIdMap = new HashMap<>();
    private final List<IMonitoringRecord> allRecords = new ArrayList<>();

    private final Map<Long, List<IMonitoringRecord>> stash = new HashMap<>();

    @Override
    protected void execute(final IMonitoringRecord event) throws Exception {
        if (event instanceof TraceMetadata) {
            final TraceMetadata traceMetadata = (TraceMetadata) event;
            if (!(this.traceIdMap.get(traceMetadata.getSessionId()) != null)) {
                this.traceIdMap.put(traceMetadata.getSessionId(), traceMetadata.getTraceId());

                final List<IMonitoringRecord> recordList = new ArrayList<>();
                recordList.add(event);

                /** copy stash. */
                final List<IMonitoringRecord> stashedRecords = this.stash.get(traceMetadata.getTraceId());
                if (stashedRecords != null) {
                    recordList.addAll(stashedRecords);
                    this.stash.remove(traceMetadata.getTraceId());
                }

                this.sessionRecords.put(traceMetadata.getSessionId(), recordList);
            }
        } else if (event instanceof ITraceRecord) {
            final String sessionId = this.getSessionId((ITraceRecord) event);

            if (sessionId != null) {
                this.sessionRecords.get(sessionId).add(event);
            } else {
                /** session and trace did not start yet. Stash element. */

                final Long traceId = ((ITraceRecord) event).getTraceId();
                List<IMonitoringRecord> stashedRecords = this.stash.get(traceId);
                if (stashedRecords == null) {
                    stashedRecords = new ArrayList<>();
                }
                stashedRecords.add(event);
                this.stash.put(traceId, stashedRecords);
            }
        } else if (event instanceof EmptyRecord) {
            this.flushAllRecords();
        }

        this.allRecords.add(event);
    }

    private void flushAllRecords() {
        for (final IMonitoringRecord event : this.allRecords) {
            if (event instanceof ITraceRecord) {
                final ITraceRecord traceEvent = (ITraceRecord) event;

                final Pair firstLast = this.getFirstLast(traceEvent);

                this.outputPort.send(event);

                if (firstLast != null) {
                    if (firstLast.getLast() == traceEvent) {
                        System.err.println("Session end " + firstLast.getFirst().getSessionId());
                        this.outputPort.send(new SessionEndEvent(firstLast.getLast().getLoggingTimestamp(),
                                firstLast.getFirst().getHostname(), firstLast.getFirst().getSessionId()));
                    }
                }
            } else {
                this.outputPort.send(event);
            }
        }
    }

    private Pair getFirstLast(final ITraceRecord traceEvent) {
        final String sessionId = this.getSessionId(traceEvent);

        if (sessionId != null) {
            final List<IMonitoringRecord> records = this.sessionRecords.get(sessionId);

            return new Pair((TraceMetadata) records.get(0), (ITraceRecord) records.get(records.size() - 1));
        } else {
            return null;
        }
    }

    private String getSessionId(final ITraceRecord traceEvent) {
        for (final Entry<String, Long> entry : this.traceIdMap.entrySet()) {
            if (entry.getValue() == traceEvent.getTraceId()) {
                return entry.getKey();
            }
        }

        EndSessionDetector.LOGGER.info("TraceId without metadata, needs stashing {} ", traceEvent.getTraceId());

        return null;
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

}
