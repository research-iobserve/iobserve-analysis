/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.response.time.calculator;

import java.util.HashMap;
import java.util.Map;

import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Reiner Jung
 *
 */
public class CalculateResponseTimeStage extends AbstractConsumerStage<IFlowRecord> {

    private final OutputPort<Map<String, Object>> outputPort = this.createOutputPort();

    private final Map<Long, Long> map = new HashMap<>();

    private final Map<String, Object> output = new HashMap<>();

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractConsumerStage#execute(java.lang.Object)
     */
    @Override
    protected void execute(final IFlowRecord element) throws Exception {
        if (element instanceof BeforeOperationEvent) {
            final BeforeOperationEvent event = (BeforeOperationEvent) element;
            this.map.put(event.getTraceId(), event.getTimestamp());
        } else if (element instanceof AfterOperationEvent) {
            final AfterOperationEvent event = (AfterOperationEvent) element;
            final long responseTime = event.getTimestamp() - this.map.remove(event.getTraceId());
            this.output.clear();
            this.output.put("operation", event.getOperationSignature());
            this.output.put("response-time", responseTime);
            this.outputPort.send(this.output);
        }
    }

    public OutputPort<Map<String, Object>> getOutputPort() {
        return this.outputPort;
    }

}
