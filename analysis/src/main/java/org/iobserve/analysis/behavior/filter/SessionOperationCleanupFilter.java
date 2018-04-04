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
package org.iobserve.analysis.behavior.filter;

import java.util.List;
import java.util.stream.Collectors;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.data.configuration.EntryCallFilterRules;
import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.iobserve.analysis.session.data.UserSession;

/**
 * Removes unwanted events from sessions.
 *
 * @author Christoph Dornieden
 *
 */

public final class SessionOperationCleanupFilter extends AbstractConsumerStage<UserSessionCollectionModel> {

    private final OutputPort<UserSessionCollectionModel> outputPort = this.createOutputPort();
    private final EntryCallFilterRules filter;

    /**
     * Constructor.
     *
     * @param filter
     *            filter rules to optimize data
     */
    public SessionOperationCleanupFilter(final EntryCallFilterRules filter) {
        super();
        this.filter = filter;
    }

    @Override
    protected void execute(final UserSessionCollectionModel entryCallSequenceModel) throws Exception {

        final List<UserSession> sessions = entryCallSequenceModel.getUserSessions().stream().map(this::filterSession)
                .collect(Collectors.toList());

        final UserSessionCollectionModel filteredEntryCallSequenceModel = new UserSessionCollectionModel(sessions);

        this.outputPort.send(filteredEntryCallSequenceModel);
    }

    /**
     * getter.
     *
     * @return the outputPort
     */
    public OutputPort<UserSessionCollectionModel> getOutputPort() {
        return this.outputPort;
    }

    private UserSession filterSession(final UserSession session) {
        final UserSession filteredUserSession = new UserSession(session.getHost(), session.getSessionId());
        session.getEvents().stream().filter(this.filter::isAllowed).forEach(filteredUserSession::add);
        return filteredUserSession;

    }

}