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
import java.util.regex.Pattern;

import org.iobserve.analysis.data.PayloadAwareEntryCallEvent;
import org.iobserve.analysis.utils.ExecutionTimeLogger;
import org.iobserve.common.record.EntryLevelBeforeOperationEvent;
import org.iobserve.common.record.ExtendedAfterOperationEvent;
import org.iobserve.common.record.ExtendedBeforeOperationEvent;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

// TODO this filter must be reworked to support plain and extended records.

/**
 * This entry call synthesizer stage processes incoming traces to identify entry calls.
 * This is done based on a regular expression matching one or more nodes of a trace,
 * which is necessary, as the entry might not be identifiable solely based on the entering
 * class, e.g., servlet class. This is especially true for CoCoME.
 * 
 * @author Reiner Jung -- initial contribution, reconstruction based on traces.
 * @author Christoph Dornieden
 * @author Nicolas Boltz
 * @version 1.0
 */
public class TEntryCall extends AbstractConsumerStage<EventBasedTrace> {
    /** logger. */
    private static final Log LOG = LogFactory.getLog(RecordSwitch.class);

    /** added by Alessandro Giusa see EntryCallEvent class for more information. */
    private final Map<Long, TraceMetadata> traceMetaDatas = new HashMap<>();
    /** map of before operation events. */
    private final Map<Long, BeforeOperationEvent> beforeOperationEvents = new HashMap<>();
    
    /** Entry call trace matcher class. */
    IEntryCallTraceMatcher matcher;
    
    /** output port. */
    private final OutputPort<PayloadAwareEntryCallEvent> outputPort = this.createOutputPort();
    private final Pattern openBracket = Pattern.compile("\\[");
    private final Pattern closedBracket = Pattern.compile("\\]");
    private final Pattern emptyBrackets = Pattern.compile("\\[\\]");

    /**
     * Does not need additional information.
     */
    public TEntryCall(IEntryCallTraceMatcher matcher) {
    	this.matcher = matcher;
    }

    /**
     * This method is triggered for every deployment event.
     *
     * @param event
     *            all IFlowRecord like TraceMetadata, BeforeOperationEvent and AfterOperationEvent
     */
    @Override
    protected void execute(final EventBasedTrace event) {
    	for (AbstractTraceEvent traceEvent : event.getTraceEvents()) {
    		if (traceEvent instanceof BeforeOperationEvent) {
    			BeforeOperationEvent beforeEvent = (BeforeOperationEvent)traceEvent;
    			
    			if (this.matcher.stateMatch(event, beforeEvent)) {
					this.outputPort.send(createEntryCall(event.getTraceMetaData()));
					return;
    			}
    		}
    	}
    }
    
    private PayloadAwareEntryCallEvent createEntryCall(TraceMetadata traceMetaData) {
		
		BeforeOperationEvent beforeOperationEvent = this.matcher.getBeforeOperationEvent();
		AfterOperationEvent afterOperationEvent = this.matcher.getAfterOperationEvent();
		
		if (beforeOperationEvent instanceof EntryLevelBeforeOperationEvent) {
			EntryLevelBeforeOperationEvent entryLevelBeforeEvent = (EntryLevelBeforeOperationEvent) beforeOperationEvent;
			return new PayloadAwareEntryCallEvent(beforeOperationEvent.getTimestamp(),
			        afterOperationEvent.getTimestamp(), beforeOperationEvent.getOperationSignature(),
			        beforeOperationEvent.getClassSignature(), traceMetaData.getSessionId(),
			        traceMetaData.getHostname(),
			        entryLevelBeforeEvent.getParameters(), entryLevelBeforeEvent.getValues(), entryLevelBeforeEvent.getRequestType());
		} else {
			return new PayloadAwareEntryCallEvent(beforeOperationEvent.getTimestamp(),
			        afterOperationEvent.getTimestamp(), beforeOperationEvent.getOperationSignature(),
			        beforeOperationEvent.getClassSignature(), traceMetaData.getSessionId(),
			        traceMetaData.getHostname(), new String[0], new String[0], 0);
		}
	}

	private void ex(final IFlowRecord event) {
        if (event instanceof TraceMetadata) {
            final TraceMetadata metaData = (TraceMetadata) event;
            /** only recognize traces which no parent trace (i.e. would be internal traces) */
            // TODO how to detect proper original traces?
            if (metaData.getParentOrderId() < 0) {
                // TODO this might be the better alternative if (metaData.getParentTraceId() < 0) {
                this.traceMetaDatas.put(metaData.getTraceId(), metaData);
            }
        } else if (event instanceof BeforeOperationEvent) {
            final BeforeOperationEvent operationEvent = (BeforeOperationEvent) event;
            final TraceMetadata metaData = this.traceMetaDatas.get(operationEvent.getTraceId());
            
            if (metaData != null) {
                /** actually this is a valid trace. */
                /** Check whether the record is an entry call. */
                if (operationEvent.getOrderIndex() == 0) {
                    this.beforeOperationEvents.put(metaData.getTraceId(), operationEvent);
                }
            }
        } else if (event instanceof AfterOperationEvent) {
            final AfterOperationEvent afterOperationEvent = (AfterOperationEvent) event;

            ExecutionTimeLogger.getInstance().startLogging(afterOperationEvent);

            final TraceMetadata metaData = this.traceMetaDatas.get(afterOperationEvent.getTraceId());
            if (afterOperationEvent.getOperationSignature().matches(".*startSale.*")) {
                System.out.println(afterOperationEvent.getOperationSignature());

            }

            if (metaData != null) {
                /** actually this is a valid trace */
                final BeforeOperationEvent beforeOperationEvent = this.beforeOperationEvents.get(metaData.getTraceId());
                if (beforeOperationEvent != null) {
                    /** check whether it matches an before operation event. */
                    if (beforeOperationEvent.getClassSignature().equals(afterOperationEvent.getClassSignature())
                            && beforeOperationEvent.getOperationSignature()
                                    .equals(afterOperationEvent.getOperationSignature())) {

                        /** check for extended events **/
                        String callInformations = "[]";

                        if (beforeOperationEvent instanceof ExtendedBeforeOperationEvent) {
                            final ExtendedBeforeOperationEvent extendedBeforeOperationEvent = (ExtendedBeforeOperationEvent) beforeOperationEvent;
                            callInformations = extendedBeforeOperationEvent.getInformations();

                        }

                        if (afterOperationEvent instanceof ExtendedAfterOperationEvent) {
                            final ExtendedAfterOperationEvent extendedAfterOperationEvent = (ExtendedAfterOperationEvent) afterOperationEvent;
                            final String newInformations = extendedAfterOperationEvent.getInformations();

                            callInformations = this.emptyBrackets.matcher(callInformations).matches() ? newInformations
                                    : this.closedBracket.matcher(callInformations).replaceAll(",")
                                            + this.openBracket.matcher(newInformations).replaceAll("");

                        }

                        /**
                         * if afterOperationEvent has a beforeOperationEvent counterpart remove
                         * beforeOperationEvent so a second afterOperationEvent with the same
                         * traceId does not send a call two times
                         */
                        this.beforeOperationEvents.remove(metaData.getTraceId(), beforeOperationEvent);

                        // TODO this is only a hack for debugging purposes and could probably be
                        // removed
                        if (afterOperationEvent.getOperationSignature().matches(".*startSale.*")) {
                            System.out.println(afterOperationEvent.getOperationSignature());

                        }
                        
                        String operationSignature = beforeOperationEvent.getOperationSignature().replaceAll("jpetstore\\.actions\\.", "");

                        //this.outputPort.send(new ExtendedEntryCallEvent(beforeOperationEvent.getTimestamp(),
                        //        afterOperationEvent.getTimestamp(), operationSignature,
                        //        beforeOperationEvent.getClassSignature(), metaData.getSessionId(),
                        //        metaData.getHostname(), callInformations));

                    }
                }
            }

        } else {
            TEntryCall.LOG.warn("Unsuppored flow event type " + event.getClass().getCanonicalName());
        }
    }

    /**
     * @return output port
     */
    public OutputPort<PayloadAwareEntryCallEvent> getOutputPort() {
        return this.outputPort;
    }

}
