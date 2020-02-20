/***************************************************************************
 * Copyright 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.check.log.sessions;

import java.util.HashMap;
import java.util.Map;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.session.data.UserSession;

/**
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 *
 */
public class SessionStatisticsStage extends AbstractConsumerStage<UserSession> {

    private final OutputPort<Map<String, Object>> outputPort = this.createOutputPort();

    @Override
    protected void execute(final UserSession session) throws Exception {
        final Map<String, Object> map = new HashMap<>();
        map.put("host", session.getHost());
        map.put("entry", session.getEntryTime());
        map.put("exit", session.getExitTime());
        map.put("session", session.getSessionId());
        map.put("size", session.getEvents().size());
    }

    public OutputPort<Map<String, Object>> getOutputPort() {
        return this.outputPort;
    }

}
