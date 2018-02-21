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
package org.iobserve.analysis.clustering.filter;

import org.iobserve.analysis.clustering.filter.models.configuration.EntryCallFilterRules;
import org.iobserve.analysis.session.data.UserSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public final class TSessionOperationsFilter extends AbstractConsumerStage<UserSession> {
    private final OutputPort<UserSession> outputPort = this.createOutputPort();

    private final EntryCallFilterRules filter;

    public TSessionOperationsFilter(final EntryCallFilterRules filter) {
        super();
        this.filter = filter;
    }

    @Override
    protected void execute(final UserSession session) throws Exception {
        final UserSession filteredUserSession = new UserSession(session.getHost(), session.getSessionId());
        session.getEvents().stream().filter(this.filter::isAllowed).forEach(filteredUserSession::add);

        this.outputPort.send(filteredUserSession);
    }
}