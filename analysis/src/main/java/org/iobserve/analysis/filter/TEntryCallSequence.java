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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.utils.ExecutionTimeLogger;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Represents the TEntryCallSequence Transformation in the paper <i>Run-time Architecture Models for
 * Dynamic Adaptation and Evolution of Cloud Applications</i>.
 *
 * @author Robert Heinrich
 * @author Alessandro Guisa
 *
 * @version 1.0
 */
public final class TEntryCallSequence extends AbstractConsumerStage<EntryCallEvent> {

    /** reference to the correspondence model. */
    private final ICorrespondence correspondenceModel;

    /** threshold for user session elements until their are send to the next filter. */
    private static final int USER_SESSION_THRESHOLD = 0;
    /** map of sessions. */
    private final Map<String, UserSession> sessions = new HashMap<>();
    /** output port. */
    private final OutputPort<EntryCallSequenceModel> outputPort = this.createOutputPort();

    /**
     * Create this filter.
     *
     * @param correspondenceModel
     *            reference to the correspondence model
     */
    public TEntryCallSequence(final ICorrespondence correspondenceModel) {
        this.correspondenceModel = correspondenceModel;
    }

    @Override
    protected void execute(final EntryCallEvent event) {
        ExecutionTimeLogger.getInstance().startLogging(event);
        /** check if operationEvent is from an known object */
        if (this.correspondenceModel.containsCorrespondent(event.getClassSignature(), event.getOperationSignature())) {

            // add the event to the corresponding user session
            // in case the user session is not yet available, create one
            final String userSessionId = UserSession.parseUserSessionId(event);
            UserSession userSession = this.sessions.get(userSessionId);
            if (userSession == null) {
                userSession = new UserSession(event.getHostname(), event.getSessionId());
                this.sessions.put(userSessionId, userSession);
            }
            // do not sort since TEntryEventSequence will sort any ways
            userSession.add(event, false);

            // collect all user sessions which have more elements as a defined threshold and send
            // them
            // to the next filter
            final List<UserSession> listToSend = this.sessions.values().stream()
                    .filter(session -> session.size() > TEntryCallSequence.USER_SESSION_THRESHOLD)
                    .collect(Collectors.toList());

            ExecutionTimeLogger.getInstance().stopLogging(event);

            if (!listToSend.isEmpty()) {
                this.outputPort.send(new EntryCallSequenceModel(listToSend));
            }
        }
    }

    /**
     * @return output port
     */
    public OutputPort<EntryCallSequenceModel> getOutputPort() {
        return this.outputPort;
    }
}
