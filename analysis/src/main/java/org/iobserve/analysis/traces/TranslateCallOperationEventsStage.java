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
package org.iobserve.analysis.traces;

import java.util.ArrayList;
import java.util.List;

import kieker.common.record.flow.IEventRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Reiner Jung
 *
 */
public class TranslateCallOperationEventsStage extends AbstractConsumerStage<IFlowRecord> {

    private final OutputPort<IFlowRecord> outputPort = this.createOutputPort(IFlowRecord.class);

    private final List<AfterOperationEvent> waitingList = new ArrayList<>();

    @Override
    protected void execute(final IFlowRecord event) throws Exception {
        if (event instanceof CallOperationEvent) {
            final CallOperationEvent call = (CallOperationEvent) event;
            this.outputPort.send(new BeforeOperationEvent(call.getTimestamp(), call.getTraceId(), call.getOrderIndex(),
                    call.getCalleeOperationSignature(), call.getCalleeClassSignature()));
            this.waitingList.add(new AfterOperationEvent(call.getTimestamp() + 1000 * 1000, call.getTraceId(),
                    call.getOrderIndex(), call.getCalleeOperationSignature(), call.getCalleeClassSignature()));
            this.sendWaiting(call.getTimestamp());
        } else if (event instanceof IEventRecord) {
            this.sendWaiting(((IEventRecord) event).getTimestamp());
            this.outputPort.send(event);
        } else {
            this.outputPort.send(event);
        }
    }

    private void sendWaiting(final long timestamp) {
        for (int i = 0; i < this.waitingList.size(); i++) {
            final AfterOperationEvent afterEvent = this.waitingList.get(i);
            if (timestamp > afterEvent.getTimestamp()) {
                this.outputPort.send(afterEvent);
                this.waitingList.remove(afterEvent);
                i--; // NOCS necessary, as we delete elements from a list
            }
        }

    }

    public OutputPort<IFlowRecord> getOutputPort() {
        return this.outputPort;
    }

}
