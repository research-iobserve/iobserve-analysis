/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.behavior.clustering.em;

import java.util.ArrayList;
import java.util.List;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.iobserve.analysis.session.data.UserSession;

/**
 * This filter collects user sessions until a time trigger event. All events passed the sliding
 * window are removed from the session pool.
 *
 * The idea of the filter is to adapt Christophs X-Means and Marcs EM to the updated framework.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class UserSessionModelAggregator extends AbstractStage {

    private static final long SLIDING_WINDOW = 600; // time in minutes

    private final InputPort<UserSession> userSessionInputPort = this.createInputPort(UserSession.class);
    private final List<UserSession> userSessions = new ArrayList<>();
    private final InputPort<Long> timeTriggerInputPort = this.createInputPort(Long.class);

    private final OutputPort<UserSessionCollectionModel> outputPort = this
            .createOutputPort(UserSessionCollectionModel.class);

    @Override
    protected void execute() throws Exception {
        final UserSession newUserSession = this.userSessionInputPort.receive();
        if (newUserSession != null) {
            this.userSessions.add(newUserSession);
        }
        final Long timeTrigger = this.timeTriggerInputPort.receive();
        if (timeTrigger != null) {
            // TODO please note this is a temporary measure, in future, we might just send the list
            // or stream of sessions.
            for (int i = 0; i < this.userSessions.size(); i++) {
                final UserSession userSession = this.userSessions.get(i);
                if (userSession != null) {
                    final Long entryTime = userSession.getEntryTime();
                    final Long lowerBoundary = timeTrigger - UserSessionModelAggregator.SLIDING_WINDOW;
                    if (entryTime < lowerBoundary) {
                        this.userSessions.remove(i);
                    }
                }
            }

            final UserSessionCollectionModel model = new UserSessionCollectionModel(this.userSessions);
            this.outputPort.send(model);
        }
    }

    public OutputPort<UserSessionCollectionModel> getOutputPort() {
        return this.outputPort;
    }

    public InputPort<UserSession> getUserSessionInputPort() {
        return this.userSessionInputPort;
    }

    public InputPort<Long> getTimeTriggerInputPort() {
        return this.timeTriggerInputPort;
    }

}
