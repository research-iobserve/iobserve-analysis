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
package org.iobserve.analysis.session;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.analysis.traces.EntryCallSequence;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tests whether a trace contains only operations which are considered valid trace elements. In
 * effect it ignores invalid sessions.
 *
 * @author Reiner Jung
 *
 */
public class SessionAcceptanceFilter extends AbstractConsumerStage<UserSession> {
	private static final Logger LOGGER = LoggerFactory.getLogger(SessionAcceptanceFilter.class);
    private final OutputPort<UserSession> outputPort = this.createOutputPort();
    private final IEntryCallAcceptanceMatcher matcher;
    private int re = 0;
    private int se = 0;
    /**
     * Create an acceptance filter with an external matcher.
     *
     * @param matcher
     *            a acceptance matcher
     */
    public SessionAcceptanceFilter(final IEntryCallAcceptanceMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    protected void execute(final UserSession session) throws Exception {
    	re++;
        for (final EntryCallEvent call : session.getEvents()) {
            if (!this.matcher.match(call)) {
                return;
            }
        }
        this.outputPort.send(session);
        se++;
    }

    @Override
    public void onTerminating() {
    	SessionAcceptanceFilter.LOGGER.debug("Received " + re + " user sessions.");
    	SessionAcceptanceFilter.LOGGER.debug("Sent " + se + " user sessions.");
        super.onTerminating();
    }

    public OutputPort<UserSession> getOutputPort() {
        return this.outputPort;
    }

}
