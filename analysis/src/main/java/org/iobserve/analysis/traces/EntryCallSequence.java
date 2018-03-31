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
package org.iobserve.analysis.traces;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.clustering.birch.BuildCFTree;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.common.record.SessionEndEvent;
import org.iobserve.common.record.SessionStartEvent;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the TEntryCallSequence Transformation in the paper <i>Run-time Architecture Models for
 * Dynamic Adaptation and Evolution of Cloud Applications</i>.
 *
 * @author Robert Heinrich
 * @author Alessandro Guisa
 * @author Christoph Dornieden
 *
 * @version 1.0
 */
public final class EntryCallSequence extends AbstractStage {
	private static final Logger LOGGER = LoggerFactory.getLogger(EntryCallSequence.class);
    /** time until a session expires. */
    private static final long USER_SESSION_EXPIRATIONTIME = 360000000000L;
    /** map of sessions. */
    private final Map<String, UserSession> sessions = new HashMap<>();
    /** output ports. */
    private final OutputPort<UserSession> userSessionOutputPort = this.createOutputPort();

    private final InputPort<PayloadAwareEntryCallEvent> entryCallInputPort = this.createInputPort();
    private final InputPort<ISessionEvent> sessionEventInputPort = this.createInputPort();
    private int cntOverall = 0;
    private int cntSE = 0;
    private int cntPAECE = 0;
    /**
     * Create this filter.
     *
     */
    public EntryCallSequence() {
        // TODO make expiration time configurable
    }

    @Override
    protected void execute() {
        final ISessionEvent sessionEvent = this.sessionEventInputPort.receive();
        cntOverall++;
        if (sessionEvent != null) {
        	cntSE++;
            if (sessionEvent instanceof SessionStartEvent) {
                this.sessions.put(UserSession.createUserSessionId(sessionEvent),
                        new UserSession(sessionEvent.getHostname(), sessionEvent.getSessionId()));
            }
            if (sessionEvent instanceof SessionEndEvent) {
                final UserSession session = this.sessions.get(UserSession.createUserSessionId(sessionEvent));
                if (session != null) {
                    this.userSessionOutputPort.send(session);
                    this.sessions.remove(sessionEvent.getSessionId());
                }
            }
        }

        final PayloadAwareEntryCallEvent event = this.entryCallInputPort.receive();

        if (event != null) {
        	cntPAECE++;
            /**
             * add the event to the corresponding user session in case the user session is not yet
             * available, create one.
             */
            final String userSessionId = UserSession.createUserSessionId(event);
            UserSession userSession = this.sessions.get(userSessionId);
            if (userSession == null) {
                userSession = new UserSession(event.getHostname(), event.getSessionId());
                this.sessions.put(userSessionId, userSession);
                // TODO this should trigger a warning.
            }
            userSession.add(event, true);
        }

        //this.removeExpiredSessions();
    }

    /**
     * removes all expired sessions from the filter and sends them to
     * tBehaviorModelPreperationOutputPort.
     */
    private void removeExpiredSessions() {
        final long timeNow = System.currentTimeMillis() * 1000000;
        final Set<String> sessionsToRemove = new HashSet<>();

        for (final String sessionId : this.sessions.keySet()) {
            final UserSession session = this.sessions.get(sessionId);
            final long exitTime = session.getExitTime();

            final boolean isExpired = exitTime + EntryCallSequence.USER_SESSION_EXPIRATIONTIME < timeNow;

            if (isExpired) {
                this.userSessionOutputPort.send(session);
                sessionsToRemove.add(sessionId);
            }
        }
        //EntryCallSequence.LOGGER.debug("Removing " + sessionsToRemove.size() + " expired sessions.");
        for (final String sessionId : sessionsToRemove) {
            this.sessions.remove(sessionId);
        }
    }

    @Override
    public void onTerminating() {
    	EntryCallSequence.LOGGER.debug("Received " + cntOverall + " events.");
    	EntryCallSequence.LOGGER.debug(cntSE + " SessionEvents were not null.");
    	EntryCallSequence.LOGGER.debug(cntPAECE + " cntPAECE were not null.");
    	EntryCallSequence.LOGGER.debug("About to send " + this.sessions.size() + " user sessions.");
        for (final UserSession session : this.sessions.values()) {
            this.userSessionOutputPort.send(session);
        }
        super.onTerminating();
    }

    /**
     * @return output port
     */
    public OutputPort<UserSession> getUserSessionOutputPort() {
        return this.userSessionOutputPort;
    }

    public InputPort<PayloadAwareEntryCallEvent> getEntryCallInputPort() {
        return this.entryCallInputPort;
    }

    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.sessionEventInputPort;
    }

}
