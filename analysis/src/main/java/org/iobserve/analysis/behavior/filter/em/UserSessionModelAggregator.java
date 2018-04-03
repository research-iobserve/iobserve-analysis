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
package org.iobserve.analysis.behavior.filter.em;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.iobserve.analysis.session.CollectUserSessionsFilter;
import org.iobserve.common.record.ISessionEvent;
import org.iobserve.stages.source.TimeTriggerFilter;

/**
 * @author Reiner Jung
 *
 */
public class UserSessionModelAggregator extends CompositeStage {

    private final InputPort<ISessionEvent> sessionInputPort = null;
    private final InputPort<EventBasedTrace> traceInputPort = null;
    private final CollectUserSessionsFilter collectUserSessions;

    public UserSessionModelAggregator() {
        this.collectUserSessions = new CollectUserSessionsFilter(100000, 1);
        final TimeTriggerFilter timeTrigger = new TimeTriggerFilter(1000);

        this.connectPorts(timeTrigger.getOutputPort(), this.collectUserSessions.getTimeTriggerInputPort());
    }

    public InputPort<EventBasedTrace> getTraceInputPort() {
        return this.traceInputPort;
    }

    public InputPort<ISessionEvent> getSessionEventInputPort() {
        return this.sessionInputPort;
    }

    public OutputPort<UserSessionCollectionModel> getOutputPort() {
        return this.collectUserSessions.getOutputPort();
    }

}
