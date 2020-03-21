/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.filter;

import java.util.HashMap;
import java.util.Map;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.utils.ExecutionTimeLogger;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * TEntryCall filters and maps related call events, to recreate calls which are present in the monitoring data.
 * Once an {@link AfterOperationObjectEvent} is mapped to a previously processed {@link BeforeOperationObjectEvent} 
 * and {@link TraceMetaData}, an EntryCallEvent, representing the finished call, is forwarded to {@link TEntryCallSequence}.
 * 
 * @author Robert Heinrich
 * @author Reiner Jung
 * @author Nicolas Boltz
 */
public class TEntryCall extends AbstractConsumerStage<IFlowRecord> {
	
    /** logger. */
    private static final Log LOG = LogFactory.getLog(RecordSwitch.class);
    /** map of trace meta data events. */
    private final Map<Long, TraceMetadata> traceMetaDatas = new HashMap<>();
    /** map of before operation events. */
    private final Map<Long, BeforeOperationEvent> beforeOperationEvents = new HashMap<>();
    /** output port. */
    private final OutputPort<EntryCallEvent> outputPort = this.createOutputPort();

    /**
     * Creates new TEntryCall filter.
     */
    public TEntryCall() { }

    /**
     * This method is triggered for every deployment event.
     *
     * @param event
     *            All IFlowRecord like TraceMetadata, BeforeOperationEvent and AfterOperationEvent
     */
    @Override
    protected void execute(final IFlowRecord event) {
        if (event instanceof TraceMetadata) {
            final TraceMetadata metaData = (TraceMetadata) event;
            /** only recognize traces which no parent trace (i.e. would be internal traces) */
            if (metaData.getParentTraceId() < 0) {
                this.traceMetaDatas.put(metaData.getTraceId(), metaData);
            }
        } else if (event instanceof BeforeOperationEvent) {
            final BeforeOperationEvent operationEvent = (BeforeOperationEvent) event;
            final TraceMetadata metaData = this.traceMetaDatas.get(operationEvent.getTraceId());
            if (metaData != null) {
                /** actually this is a valid trace */
                /** Check whether the record is an entry call */
                if (operationEvent.getOrderIndex() == 0) {
                    this.beforeOperationEvents.put(metaData.getTraceId(), operationEvent);
                }
            }
        } else if (event instanceof AfterOperationEvent) {
            final AfterOperationEvent afterOperationEvent = (AfterOperationEvent) event;

            ExecutionTimeLogger.getInstance().startLogging(afterOperationEvent);

            final TraceMetadata metaData = this.traceMetaDatas.get(afterOperationEvent.getTraceId());
            if (metaData != null) {
                /** actually this is a valid trace */
                final BeforeOperationEvent beforeOperationEvent = this.beforeOperationEvents.get(metaData.getTraceId());
                if (beforeOperationEvent != null) {
                    /** check whether it matches an before operation event. */
                    if (beforeOperationEvent.getClassSignature().equals(afterOperationEvent.getClassSignature())
                            && beforeOperationEvent.getOperationSignature()
                                    .equals(afterOperationEvent.getOperationSignature())) {
                        /**
                         * if afterOperationEvent has a beforeOperationEvent counterpart remove
                         * beforeOperationEvent so a second afterOperationEvent with the same
                         * traceId does not send a call two times
                         */
                        this.beforeOperationEvents.remove(metaData.getTraceId(), beforeOperationEvent);
                        EntryCallEvent sendEvent = new EntryCallEvent(beforeOperationEvent.getTimestamp(),
                                afterOperationEvent.getTimestamp(), beforeOperationEvent.getOperationSignature(),
                                beforeOperationEvent.getClassSignature(), metaData.getSessionId(),
                                metaData.getHostname());
                        ExecutionTimeLogger.getInstance().stopLogging(afterOperationEvent);
                        
                        this.outputPort.send(sendEvent);
                    }
                }
            }
        } else {
            TEntryCall.LOG.warn("Unsuppored flow event type " + event.getClass().getCanonicalName());
        }
    }

    public OutputPort<EntryCallEvent> getOutputPort() {
        return this.outputPort;
    }
}