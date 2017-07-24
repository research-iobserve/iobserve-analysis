/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 ***************************************************************************/

package org.iobserve.analysis.cdoruserbehavior.filter;

import java.util.List;
import java.util.stream.Collectors;

import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.EntryCallFilterRules;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Filters entry-calls from EntryCallSequenceModels
 *
 * @author Christoph Dornieden
 *
 */

public final class TEntryCallSequenceFilter extends AbstractConsumerStage<EntryCallSequenceModel> {
    /** logger. */
    private static final Log LOG = LogFactory.getLog(TEntryCallSequenceFilter.class);
    private final OutputPort<EntryCallSequenceModel> outputPort = this.createOutputPort();
    final EntryCallFilterRules filter;

    public TEntryCallSequenceFilter(final EntryCallFilterRules filter) {
        super();
        this.filter = filter;
    }

    @Override
    protected void execute(final EntryCallSequenceModel entryCallSequenceModel) throws Exception {

        final List<UserSession> sessions = entryCallSequenceModel.getUserSessions().stream().map(this::filterSession)
                .collect(Collectors.toList());

        final EntryCallSequenceModel filteredEntryCallSequenceModel = new EntryCallSequenceModel(sessions);

        this.outputPort.send(filteredEntryCallSequenceModel);
    }

    /**
     * getter
     *
     * @return the outputPort
     */
    public OutputPort<EntryCallSequenceModel> getOutputPort() {
        return this.outputPort;
    }

    private UserSession filterSession(final UserSession session) {
        final UserSession filteredUserSession = new UserSession(session.getHost(), session.getSessionId());
        session.getEvents().stream().filter(this.filter::isAllowed).forEach(filteredUserSession::add);
        return filteredUserSession;

    }

}