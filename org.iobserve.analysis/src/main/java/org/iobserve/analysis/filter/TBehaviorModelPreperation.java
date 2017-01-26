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

package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.BehaviorModelTable;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;

import teetime.framework.AbstractConsumerStage;

/**
 * Prepares EntryCallSequenceModels for Clustering
 *
 * @author Christoph Dornieden
 *
 */

public final class TBehaviorModelPreperation extends AbstractConsumerStage<EntryCallSequenceModel> {

    @Override
    protected void execute(final EntryCallSequenceModel entryCallSequenceModel) {

        final List<UserSession> userSessions = entryCallSequenceModel.getUserSessions();

        final List<String> filterBlackList = new ArrayList<>();
        filterBlackList.add("(jpetstore\\.images).*\\)");
        filterBlackList.add("(jpetstore\\.css).*\\)");

        final BehaviorModelTable modelTable = new BehaviorModelTable(filterBlackList, true);

        for (final UserSession userSession : userSessions) {
            final List<EntryCallEvent> entryCalls = userSession.getEvents();

            EntryCallEvent lastCall = null;
            for (final EntryCallEvent eventCall : entryCalls) {
                boolean isAllowed = true;

                if (lastCall != null) {

                    if (!modelTable
                            .isAllowedSignature(lastCall.getClassSignature() + lastCall.getOperationSignature())) {
                        lastCall = eventCall;
                        continue;
                    }

                    isAllowed = modelTable.addTransition(lastCall, eventCall);
                }

                lastCall = isAllowed ? eventCall : lastCall;
            }
        }

        System.out.println(modelTable);

    }

}