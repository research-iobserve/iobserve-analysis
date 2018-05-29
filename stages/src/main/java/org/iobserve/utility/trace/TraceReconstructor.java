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
package org.iobserve.utility.trace;

import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;

import org.iobserve.stages.data.trace.ConcurrentHashMapWithCreate;
import org.iobserve.stages.data.trace.EventBasedTrace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class with methods containing the logic for trace reconstruction.
 *
 * @author Nelson Tavares de Sousa
 *
 */
public class TraceReconstructor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraceReconstructor.class);

    private final ConcurrentHashMapWithCreate<Long, EventBasedTrace> traceId2trace;
    private final ISendTraceBuffer sender;

    /**
     * Create new reconstructor with the given hashmap.
     *
     * @param traceId2trace
     *            the hash map
     * @param sender
     *            sender object
     */
    public TraceReconstructor(final ConcurrentHashMapWithCreate<Long, EventBasedTrace> traceId2trace,
            final ISendTraceBuffer sender) {
        this.traceId2trace = traceId2trace;
        this.sender = sender;
    }

    private Long reconstructTrace(final IFlowRecord record) {
        Long traceId = null;
        if (record instanceof TraceMetadata) {
            traceId = ((TraceMetadata) record).getTraceId();
            final EventBasedTrace eventTrace = this.traceId2trace.getOrCreate(traceId);

            eventTrace.setTrace((TraceMetadata) record);
        } else if (record instanceof AbstractTraceEvent) {
            traceId = ((AbstractTraceEvent) record).getTraceId();
            final EventBasedTrace eventTrace = this.traceId2trace.getOrCreate(traceId);

            eventTrace.insertEvent((AbstractTraceEvent) record);
        }

        return traceId;
    }

    private void sendTrace(final Long traceId, final boolean onlyIfFinished) {
        final EventBasedTrace eventTrace = this.traceId2trace.get(traceId);
        if (eventTrace != null) { // null-check to check whether the trace has already been sent
                                  // and removed
            final boolean shouldSend;
            if (onlyIfFinished) {
                shouldSend = eventTrace.isFinished();
            } else {
                shouldSend = true;
            }

            if (shouldSend) {
                final boolean removed = null != this.traceId2trace.remove(traceId);
                if (removed) {
                    this.sender.sendTraceBuffer(eventTrace);
                }
            }
        }
    }

    /**
     * On terminating the reconstructor.
     */
    public void terminate() {
        TraceReconstructor.LOGGER.info("Incomplete traces {}", this.traceId2trace.size());
        for (final Long traceId : this.traceId2trace.keySet()) {
            this.sendTrace(traceId, false);
        }
    }

    /**
     * Execute record.
     *
     * @param record
     *            record to be processed
     */
    public void execute(final IFlowRecord record) {
        final Long traceId = this.reconstructTrace(record);
        if (traceId != null) {
            this.sendTrace(traceId, true);
        }
    }

    public ConcurrentHashMapWithCreate<Long, EventBasedTrace> getTraceId2trace() {
        return this.traceId2trace;
    }
}
