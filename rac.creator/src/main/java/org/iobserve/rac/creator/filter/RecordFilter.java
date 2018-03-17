/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.rac.creator.filter;

import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.flow.trace.operation.object.AfterOperationObjectEvent;
import kieker.common.record.flow.trace.operation.object.BeforeOperationObjectEvent;
import kieker.common.record.flow.trace.operation.object.BeforeOperationObjectInterfaceEvent;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Only let record pass which are of a certain type.
 *
 * @author Reiner Jung
 *
 */
public class RecordFilter extends AbstractConsumerStage<IFlowRecord> {

    private static final String ILLEGAL_STRING = "cloud-web-frontend";

    private final OutputPort<AbstractOperationEvent> outputPort = this.createOutputPort();

    public RecordFilter() {
        // empty default constructor
    }

    @Override
    protected void execute(final IFlowRecord element) throws Exception {
        /** only allow the following types to be processed. */
        if (element instanceof BeforeOperationObjectInterfaceEvent || element instanceof BeforeOperationObjectEvent
                || element instanceof AfterOperationObjectEvent) {
            final AbstractOperationEvent event = (AbstractOperationEvent) element;
            if (!event.getClassSignature().contentEquals(RecordFilter.ILLEGAL_STRING)) {
                this.outputPort.send(event);
            }
        }

    }

    public OutputPort<AbstractOperationEvent> getOutputPort() {
        return this.outputPort;
    }

}
