/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.behavior.clustering.similaritymatching;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.session.data.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Reiner Jung
 *
 */
public class DummyClassificationStage extends AbstractStage {
    private static final Logger LOGGER = LoggerFactory.getLogger(DummyClassificationStage.class);

    private final InputPort<UserSession> sessionInputPort = this.createInputPort(UserSession.class);
    private final InputPort<Long> timerInputPort = this.createInputPort(Long.class);
    private final OutputPort<BehaviorModel[]> outputPort = this.createOutputPort();

    public DummyClassificationStage() {
        this.declareActive();
    }

    @Override
    protected void execute() throws Exception {
        final UserSession session = this.sessionInputPort.receive();
        final Long timestamp = this.timerInputPort.receive();

        if (session != null) {
            DummyClassificationStage.LOGGER.debug("session: host={} id={} time=[{}:{}] events={}", session.getHost(),
                    session.getSessionId(), session.getEntryTime(), session.getExitTime(), session.getEvents().size());
        }
        if (timestamp != null) {
            DummyClassificationStage.LOGGER.debug("time {}", timestamp);
        }
    }

    public InputPort<UserSession> getSessionInputPort() {
        return this.sessionInputPort;
    }

    public InputPort<Long> getTimerInputPort() {
        return this.timerInputPort;
    }

    public OutputPort<BehaviorModel[]> getOutputPort() {
        return this.outputPort;
    }

}
