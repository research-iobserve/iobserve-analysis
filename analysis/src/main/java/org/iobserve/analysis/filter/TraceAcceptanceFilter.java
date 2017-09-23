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
package org.iobserve.analysis.filter;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.UserSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Tests whether a trace contains only operations which are considered valid trace elements.
 *
 * @author Reiner Jung
 *
 */
public class TraceAcceptanceFilter extends AbstractConsumerStage<UserSession> {

    private final OutputPort<UserSession> outputPort = this.createOutputPort();
    private final ITraceAcceptanceMatcher matcher;

    /**
     * Create an acceptance filter with an external matcher.
     *
     * @param matcher
     *            a acceptance matcher
     */
    public TraceAcceptanceFilter(final ITraceAcceptanceMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    protected void execute(final UserSession session) throws Exception {
        for (final EntryCallEvent call : session.getEvents()) {
            if (!this.matcher.match(call)) {
                return;
            }
        }

        this.outputPort.send(session);
    }

    public OutputPort<UserSession> getOutputPort() {
        return this.outputPort;
    }

}
