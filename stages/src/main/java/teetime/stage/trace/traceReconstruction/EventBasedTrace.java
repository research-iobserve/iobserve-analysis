/**
 * Copyright (C) 2015 Christian Wulf, Nelson Tavares de Sousa (http://teetime-framework.github.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package teetime.stage.trace.traceReconstruction;

import java.util.ArrayList;
import java.util.List;

import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The TraceBuffer is synchronized to prevent problems with concurrent access.
 *
 * @author Jan Waller
 */
public final class EventBasedTrace {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventBasedTrace.class);

    private final List<AbstractTraceEvent> events = new ArrayList<>(128);

    private TraceMetadata traceMetaData;
    private long traceId = -1;

    private boolean closeable;
    private boolean damaged;
    private int openEvents;
    private int maxOrderIndex = -1;

    /**
     * Creates a new instance of this class.
     */
    public EventBasedTrace() {
        // default empty constructor
    }

    public void insertEvent(final AbstractTraceEvent event) {
        final long myTraceId = event.getTraceId();
        synchronized (this) {
            if (this.traceId == -1) {
                this.traceId = myTraceId;
            } else if (this.traceId != myTraceId) {
                EventBasedTrace.LOGGER.error("Invalid traceId! Expected: " + this.traceId + " but found: " + myTraceId
                        + " in event " + event.toString());
                this.damaged = true;
            }

            final int orderIndex = event.getOrderIndex();
            if (orderIndex > this.maxOrderIndex) {
                this.maxOrderIndex = orderIndex;
            }
            if (event instanceof BeforeOperationEvent) {
                if (orderIndex == 0) {
                    this.closeable = true;
                }
                this.openEvents++;
            } else if (event instanceof AfterOperationEvent) {
                this.openEvents--;
            } else if (event instanceof AfterOperationFailedEvent) {
                this.openEvents--;
            }

            this.events.add(event);
        }
    }

    public void setTrace(final TraceMetadata traceMetaData) {
        final long myTraceId = traceMetaData.getTraceId();
        synchronized (this) {
            if (this.traceId == -1) {
                this.traceId = myTraceId;
            } else if (this.traceId != myTraceId) {
                EventBasedTrace.LOGGER.error("Invalid traceId! Expected: " + this.traceId + " but found: " + myTraceId
                        + " in trace " + traceMetaData.toString());
                this.damaged = true;
            }

            if (this.traceMetaData == null) {
                this.traceMetaData = traceMetaData;
            } else {
                EventBasedTrace.LOGGER.error("Duplicate Trace entry for traceId " + myTraceId);
                this.damaged = true;
            }
        }
    }

    public boolean isFinished() {
        synchronized (this) {
            return this.closeable && !this.isInvalid();
        }
    }

    /**
     * Indicates if the trace is invalid, i.e., damaged in some way.
     *
     * @return true for invalid traces
     */
    public boolean isInvalid() {
        synchronized (this) {
            return this.traceMetaData == null || this.damaged || this.openEvents != 0
                    || this.maxOrderIndex + 1 != this.events.size() || this.events.isEmpty();
        }
    }

    /**
     * @deprecated Will be removed in 1.1 due to performance issues
     *
     * @return the trace meta data and the trace events
     */
    @Deprecated
    public TraceEventRecords toTraceEvents() {
        synchronized (this) {
            // BETTER do not create a new array but use the events directly. Perhaps, we should
            // refactor the events to an array.
            final AbstractTraceEvent[] traceEvents = this.events.toArray(new AbstractTraceEvent[this.events.size()]);
            return new TraceEventRecords(this.traceMetaData, traceEvents);
        }
    }

    public List<AbstractTraceEvent> getTraceEvents() {
        return this.events;
    }

    public TraceMetadata getTraceMetaData() {
        return this.traceMetaData;
    }

}
