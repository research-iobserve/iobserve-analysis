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
package org.iobserve.stages.general;

import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.stage.trace.traceReconstruction.EventBasedTrace;

/**
 * Interface for matchers to identify the proper events from a trace which represent an entry level
 * event.
 *
 * @author Reiner Jung
 *
 */
public interface IEntryCallTraceMatcher {

    /**
     * Checks whether the given beforeEvent is the expected before event in this sequence.
     *
     * @param trace
     *            the complete trace
     * @param beforeEvent
     *            the next before event
     * @return returns true when the beforeEvent was expected
     */
    boolean stateMatch(final EventBasedTrace trace, final BeforeOperationEvent beforeEvent);

    /**
     * The matched before event.
     *
     * @return returns the matched before event
     */
    BeforeOperationEvent getBeforeOperationEvent();

    /**
     * The matched after event..
     *
     * @return returns the corresponding after event to the matched before event.
     */
    AfterOperationEvent getAfterOperationEvent();

}
