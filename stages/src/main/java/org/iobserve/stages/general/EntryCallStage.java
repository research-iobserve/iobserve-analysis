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
package org.iobserve.stages.general;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.EntryLevelBeforeOperationEvent;
import org.iobserve.common.record.ExtendedAfterOperationEvent;
import org.iobserve.stages.data.trace.EventBasedTrace;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

//TODO: this filter must be reworked to support plain and extended records, Maybe code from earlier versions can be useful.

/**
 * This entry call synthesizer stage processes incoming traces to identify entry calls. This is done
 * based on a regular expression matching one or more nodes of a trace, which is necessary, as the
 * entry might not be identifiable solely based on the entering class, e.g., servlet class. This is
 * especially true for CoCoME.
 *
 * @author Reiner Jung -- initial contribution, reconstruction based on traces.
 * @author Christoph Dornieden
 * @author Nicolas Boltz
 * @version 1.0
 */
public class EntryCallStage extends AbstractConsumerStage<EventBasedTrace> {
    /** Entry call trace matcher class. */
    private final IEntryCallTraceMatcher matcher;

    /** output port. */
    private final OutputPort<PayloadAwareEntryCallEvent> outputPort = this.createOutputPort();

    /**
     * Entry call filter.
     *
     * @param matcher
     *            matcher to test whether the entry call is relevant.
     */
    public EntryCallStage(final IEntryCallTraceMatcher matcher) {
        this.matcher = matcher;
    }

    /**
     * This method is triggered for every deployment event.
     *
     * @param event
     *            all IFlowRecord like TraceMetadata, BeforeOperationEvent and AfterOperationEvent
     * @throws IOException
     * @throws JsonProcessingException
     */
    @Override
    protected void execute(final EventBasedTrace event) throws JsonProcessingException, IOException {
        for (final AbstractTraceEvent traceEvent : event.getTraceEvents()) {
            if (traceEvent instanceof BeforeOperationEvent) {
                final BeforeOperationEvent beforeEvent = (BeforeOperationEvent) traceEvent;

                if (this.matcher.stateMatch(event, beforeEvent)) {
                    this.outputPort.send(this.createEntryCall(event.getTraceMetaData()));
                    return;
                }
            }
        }
    }

    /**
     * This method is triggered for every deployment event.
     *
     * @param event
     *            all IFlowRecord like TraceMetadata, BeforeOperationEvent and AfterOperationEvent
     * @throws IOException
     * @throws JsonProcessingException
     */
    private PayloadAwareEntryCallEvent createEntryCall(final TraceMetadata traceMetaData)
            throws JsonProcessingException, IOException {

        final BeforeOperationEvent beforeOperationEvent = this.matcher.getBeforeOperationEvent();
        final AfterOperationEvent afterOperationEvent = this.matcher.getAfterOperationEvent();

        if (beforeOperationEvent instanceof EntryLevelBeforeOperationEvent) {
            final EntryLevelBeforeOperationEvent entryLevelBeforeEvent = (EntryLevelBeforeOperationEvent) beforeOperationEvent;
            return new PayloadAwareEntryCallEvent(beforeOperationEvent.getTimestamp(),
                    afterOperationEvent.getTimestamp(), beforeOperationEvent.getOperationSignature(),
                    beforeOperationEvent.getClassSignature(), traceMetaData.getSessionId(), traceMetaData.getHostname(),
                    entryLevelBeforeEvent.getParameters(), entryLevelBeforeEvent.getValues(),
                    entryLevelBeforeEvent.getRequestType());
        } else if (afterOperationEvent instanceof ExtendedAfterOperationEvent) {
            final ExtendedAfterOperationEvent entryLevelAfterEvent = (ExtendedAfterOperationEvent) afterOperationEvent;

            final String parameters = entryLevelAfterEvent.getInformations();

            final ObjectMapper mapper = new ObjectMapper();
            final JsonNode actualObj = mapper.readTree(parameters);

            final List<String> parameterNames = new ArrayList<>();
            final List<String> parameterValues = new ArrayList<>();

            if (actualObj instanceof ArrayNode) {
                final ArrayNode values = (ArrayNode) actualObj;
                final Iterator<JsonNode> valuesIterator = values.elements();
                while (valuesIterator.hasNext()) {
                    final JsonNode entry = valuesIterator.next();
                    if (entry instanceof ObjectNode) {
                        final ObjectNode objectNode = (ObjectNode) entry;
                        final Iterator<String> fieldNames = objectNode.fieldNames();
                        String fieldName;
                        if (fieldNames.hasNext()) {
                            fieldName = fieldNames.next();
                            parameterNames.add(objectNode.get(fieldName).textValue());
                        }
                        if (fieldNames.hasNext()) {
                            fieldName = fieldNames.next();
                            parameterValues.add(objectNode.get(fieldName).textValue());
                        }
                    }
                }
            }

            return new PayloadAwareEntryCallEvent(beforeOperationEvent.getTimestamp(),
                    afterOperationEvent.getTimestamp(), beforeOperationEvent.getOperationSignature(),
                    beforeOperationEvent.getClassSignature(), traceMetaData.getSessionId(), traceMetaData.getHostname(),
                    parameterNames.toArray(new String[parameterNames.size()]),
                    parameterValues.toArray(new String[parameterValues.size()]), 0);
        } else {
            return new PayloadAwareEntryCallEvent(beforeOperationEvent.getTimestamp(),
                    afterOperationEvent.getTimestamp(), beforeOperationEvent.getOperationSignature(),
                    beforeOperationEvent.getClassSignature(), traceMetaData.getSessionId(), traceMetaData.getHostname(),
                    new String[0], new String[0], 0);
        }
    }

    /**
     * @return output port
     */
    public OutputPort<PayloadAwareEntryCallEvent> getOutputPort() {
        return this.outputPort;
    }

}