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
package org.iobserve.service.behavior.analysis.model.generation;

import java.util.Iterator;
import java.util.List;

import teetime.stage.basic.AbstractTransformation;

import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.service.behavior.analysis.model.BehaviorModelEdge;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class UserSessionToModelConverter extends AbstractTransformation<UserSession, BehaviorModelGED> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionToModelConverter.class);

    @Override
    protected void execute(final UserSession session) throws Exception {

        UserSessionToModelConverter.LOGGER.info("Received user session");
        final BehaviorModelGED model = new BehaviorModelGED();

        session.sortEventsBy(UserSession.SORT_ENTRY_CALL_EVENTS_BY_ENTRY_TIME);
        final List<EntryCallEvent> entryCalls = session.getEvents();

        final Iterator<EntryCallEvent> iterator = entryCalls.iterator();

        BehaviorModelNode currentNode = new BehaviorModelNode("Init");
        model.getNodes().put("Init", currentNode);

        BehaviorModelNode lastNode = currentNode;

        while (iterator.hasNext()) {
            final PayloadAwareEntryCallEvent event = (PayloadAwareEntryCallEvent) iterator.next();

            currentNode = model.getNodes().get(event.getOperationSignature());
            if (currentNode == null) {
                currentNode = new BehaviorModelNode(event.getOperationSignature());
            }

            model.getNodes().put(event.getOperationSignature(), currentNode);

            this.addEdge(event, model, lastNode, currentNode);
            lastNode = currentNode;
        }

        this.outputPort.send(model);
        UserSessionToModelConverter.LOGGER.info("Created BehaviorModelGED");

    }

    private void addEdge(final PayloadAwareEntryCallEvent event, final BehaviorModelGED model,
            final BehaviorModelNode source, final BehaviorModelNode target) {
        final BehaviorModelEdge matchingEdge = source.getOutgoingEdges().get(target);

        if (matchingEdge == null) {
            final BehaviorModelEdge newEdge = new BehaviorModelEdge(event, source, target);
            source.getOutgoingEdges().put(target, newEdge);
            target.getIngoingEdges().put(source, newEdge);
            model.getEdges().add(newEdge);
        } else {
            matchingEdge.addEvent(event);
        }
    }
}
