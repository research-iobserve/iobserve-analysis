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
package org.iobserve.analysis.traces;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.data.UserSession;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * The collect user sessions filter collects incoming user sessions and sends the collection as an
 * model to the next stage based on a time trigger. Therefore, the stage has two input ports
 * userSessionInputPort and timeTriggerInputPort, and one output port. The collection to be send
 * includes all UserSessions (exitTime) older an interval (keepTime) or at least the last
 * (minCollectionSize) UserSessions.
 *
 * @author Reiner Jung
 *
 * @version 0.0.2
 */
public class CollectUserSessionsFilter extends AbstractStage {

    private final InputPort<UserSession> userSessionInputPort = this.createInputPort();
    private final InputPort<Long> timeTriggerInputPort = this.createInputPort();

    private final OutputPort<EntryCallSequenceModel> outputPort = this.createOutputPort();

    private final List<UserSession> userSessions = new ArrayList<>();

    private final long keepTime;
    private final int minCollectionSize;

    /**
     * Collect user sessions and send them based on an external time trigger to an aggegation and
     * clustering stage.
     *
     * @param keepTime
     *            the time interval to keep user sessions
     * @param minCollectionSize
     *            minimal number of collected user session
     */
    public CollectUserSessionsFilter(final long keepTime, final int minCollectionSize) {
        this.keepTime = keepTime;
        this.minCollectionSize = minCollectionSize;
    }

    @Override
    protected void execute() throws Exception {
        final UserSession userSession = this.userSessionInputPort.receive();

        if (userSession != null) {
            this.userSessions.add(userSession);
        }

        final Long triggerTime = this.timeTriggerInputPort.receive();
        if (triggerTime != null) {
            /** collect all sessions. */
            final EntryCallSequenceModel model = new EntryCallSequenceModel(this.userSessions);
            /** remove expired sessions. */
            if (this.userSessions.size() > this.minCollectionSize) {
                for (int i = 0; this.userSessions.size() > i; i++) {
                    if ((this.userSessions.get(i).getExitTime() + this.keepTime) < triggerTime) {
                        this.userSessions.remove(i);
                    }
                }
            }
            this.outputPort.send(model);
        }

    }

    public InputPort<UserSession> getUserSessionInputPort() {
        return this.userSessionInputPort;
    }

    public InputPort<Long> getTimeTriggerInputPort() {
        return this.timeTriggerInputPort;
    }

    public OutputPort<EntryCallSequenceModel> getOutputPort() {
        return this.outputPort;
    }

}
