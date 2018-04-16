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
package org.iobserve.analysis.clustering.shared;

import java.util.ArrayList;
import java.util.List;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.iobserve.analysis.session.data.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The collect user sessions filter collects incoming user sessions and sends the collection as an
 * model to the next stage based on a time trigger. Therefore, the stage has two input ports
 * userSessionInputPort and timeTriggerInputPort, and one output port. The collection to be send
 * includes all UserSessions (exitTime) older an interval (keepTime) or at least the last
 * (minCollectionSize) UserSessions.
 *
 * @author Melf Lorenzen
 *
 * @since 0.0.2
 */
public class UserSessionCollector extends AbstractConsumerStage<UserSession> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionCollector.class);

    private final OutputPort<UserSessionCollectionModel> outputPort = this.createOutputPort();

    private final List<UserSession> userSessions = new ArrayList<>();

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
    public UserSessionCollector(final int minCollectionSize) {
        this.minCollectionSize = minCollectionSize;
    }

    @Override
    protected void execute(final UserSession userSession) throws Exception {
        this.userSessions.add(userSession);
    }

    @Override
    public void onTerminating() {
        UserSessionCollector.LOGGER.debug("Received " + this.userSessions.size() + " !");
        /** collect all sessions. */
        final UserSessionCollectionModel model = new UserSessionCollectionModel(this.userSessions);

        if (this.userSessions.size() > this.minCollectionSize) {
            this.outputPort.send(model);
        }
        super.onTerminating();
    }

    public OutputPort<UserSessionCollectionModel> getOutputPort() {
        return this.outputPort;
    }

}
