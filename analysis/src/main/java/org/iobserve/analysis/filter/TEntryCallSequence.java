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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.iobserve.analysis.data.ExtendedEntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

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
public final class TEntryCallSequence extends AbstractConsumerStage<ExtendedEntryCallEvent> {

    /** threshold for user session elements until their are send to the next filter. */
    private static final int USER_SESSION_THRESHOLD = 0;
    /** time until a session expires. */
    private static final long USER_SESSION_EXPIRATIONTIME = 360000000000L;
    /** map of sessions. */
    private final Map<String, UserSession> sessions = new HashMap<>();
    /** output ports. */
    private final OutputPort<EntryCallSequenceModel> tEntryEventSequenceOutputPort = this.createOutputPort();
    private final OutputPort<EntryCallSequenceModel> tBehaviorModelPreperationOutputPort = this.createOutputPort();

    /**
     * Create this filter.
     *
     */
    public TEntryCallSequence() {
    }

    @Override
    protected void execute(final ExtendedEntryCallEvent event) {
        /**
         * add the event to the corresponding user session in case the user session is not yet
         * available, create one.
         */
        final String userSessionId = UserSession.parseUserSessionId(event);
        UserSession userSession = this.sessions.get(userSessionId);
        if (userSession == null) {
            userSession = new UserSession(event.getHostname(), event.getSessionId());
            this.sessions.put(userSessionId, userSession);
        }
        userSession.add(event, true);

        /**
         * collect all user sessions which have more elements as a defined threshold and send them
         * to the next filter.
         */
        final List<UserSession> listToSend = this.sessions.values().stream()
                .filter(session -> session.size() > TEntryCallSequence.USER_SESSION_THRESHOLD)
                .collect(Collectors.toList());
        if (!listToSend.isEmpty()) {
            this.tEntryEventSequenceOutputPort.send(new EntryCallSequenceModel(listToSend));
        }

        // TODO check for expired sessions and send them to the next filter
        // TODO Is this the right place for that?
        // this.removeExpiredSessions();
    }

    /**
     * removes all expired sessions from the filter and sends them to
     * tBehaviorModelPreperationOutputPort.
     */
    private void removeExpiredSessions() {
        final long timeNow = System.currentTimeMillis() * 1000000;
        final List<UserSession> sessionsToSend = new ArrayList<>();
        final Set<String> sessionsToRemove = new HashSet<>();

        for (final String sessionId : this.sessions.keySet()) {
            final UserSession session = this.sessions.get(sessionId);
            final long exitTime = session.getExitTime();

            final boolean isExpired = (exitTime + TEntryCallSequence.USER_SESSION_EXPIRATIONTIME) < timeNow;

            if (isExpired) {
                sessionsToSend.add(session);
                sessionsToRemove.add(sessionId);
            }
        }
        this.tBehaviorModelPreperationOutputPort.send(new EntryCallSequenceModel(sessionsToSend));
        sessionsToRemove.forEach(sessionId -> this.sessions.remove(sessionId));
    }

    @Override
    public void onTerminating() throws Exception {
        final List<UserSession> listToSend = this.sessions.values().stream()
                .filter(session -> session.size() > TEntryCallSequence.USER_SESSION_THRESHOLD)
                .collect(Collectors.toList());
        if (!listToSend.isEmpty()) {
            this.tBehaviorModelPreperationOutputPort.send(new EntryCallSequenceModel(listToSend));
        }
        super.onTerminating();
    }

    /**
     * @return output port
     */
    public OutputPort<EntryCallSequenceModel> getOutputPort() {
        return this.tEntryEventSequenceOutputPort;
    }

    /**
     * @return output port to behavior model preperation
     */
    public OutputPort<EntryCallSequenceModel> getOutputPortToBehaviorModelPreperation() {
        return this.tBehaviorModelPreperationOutputPort;
    }

}
