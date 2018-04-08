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
package org.iobserve.analysis.behavior.models.extended;

import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class providing method to convert UserSession to BehaviorModel objects
 *
 * @author Jannis Kuckei
 *
 */
public class UserSessionToBehaviorModelConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserSessionToBehaviorModelConverter.class);

    public static BehaviorModel convert(final UserSession session) {
        final BehaviorModel model = new BehaviorModel();

        final List<EntryCallEvent> entryCalls = session.getEvents();
        final Iterator<EntryCallEvent> iterator = entryCalls.iterator();

        // Assume list has at least one element
        if (iterator.hasNext()) {
            EntryCallNode lastNode = UserSessionToBehaviorModelConverter
                    .createNode((PayloadAwareEntryCallEvent) iterator.next());
            EntryCallNode currentNode;

            while (iterator.hasNext()) {
                currentNode = UserSessionToBehaviorModelConverter
                        .createNode((PayloadAwareEntryCallEvent) iterator.next());

                model.addNode(currentNode, true);
                model.addEdge(new EntryCallEdge(lastNode, currentNode), false);
                lastNode = currentNode;
            }

            return model;
        } else {
            UserSessionToBehaviorModelConverter.LOGGER.error("Empty user session creates empty model");
            return model;
        }
    }

    private static EntryCallNode createNode(final PayloadAwareEntryCallEvent event) {
        final String signature = event.getOperationSignature();
        final EntryCallNode node = new EntryCallNode(signature);

        final String[] parameters = event.getParameters();
        final String[] values = event.getValues();
        for (int i = 0; i < parameters.length; i++) {
            node.mergeInformation(new CallInformation(parameters[i], values[i]));
        }

        return node;
    }
}