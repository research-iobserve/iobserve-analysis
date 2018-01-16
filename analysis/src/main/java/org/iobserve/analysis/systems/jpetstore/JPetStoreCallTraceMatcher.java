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
package org.iobserve.analysis.systems.jpetstore;

import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

import org.iobserve.stages.general.IEntryCallTraceMatcher;

/**
 * This matcher detects entry calls for JPetStore. Please note that this is a superficial
 * implementation, which does not really identify internal methods.
 *
 * @author Reiner Jung
 *
 */
public class JPetStoreCallTraceMatcher implements IEntryCallTraceMatcher {

    private BeforeOperationEvent beforeOperationEvent;

    private AfterOperationEvent afterOperationEvent;

    @Override
    public boolean stateMatch(final EventBasedTrace trace, final BeforeOperationEvent beforeEvent) {
        if (beforeEvent.getOrderIndex() == 0) {
            this.beforeOperationEvent = beforeEvent;
            for (final AbstractTraceEvent event : trace.getTraceEvents()) {
                if (event instanceof AfterOperationEvent) {
                    final AfterOperationEvent afterEvent = (AfterOperationEvent) event;
                    if (afterEvent.getClassSignature().equals(beforeEvent.getClassSignature())
                            && afterEvent.getOperationSignature().equals(beforeEvent.getOperationSignature())) {
                        this.afterOperationEvent = afterEvent;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public BeforeOperationEvent getBeforeOperationEvent() {
        return this.beforeOperationEvent;
    }

    @Override
    public AfterOperationEvent getAfterOperationEvent() {
        return this.afterOperationEvent;
    }

}
