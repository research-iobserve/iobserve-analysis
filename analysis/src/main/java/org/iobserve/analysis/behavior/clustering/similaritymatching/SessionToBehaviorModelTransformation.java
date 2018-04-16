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
package org.iobserve.analysis.behavior.clustering.similaritymatching;

import java.util.Iterator;
import java.util.List;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.behavior.models.extended.CallInformation;
import org.iobserve.analysis.behavior.models.extended.EntryCallEdge;
import org.iobserve.analysis.behavior.models.extended.EntryCallNode;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts all arriving user sessions into behavior models.
 *
 * @author Jannis Kuckei
 *
 */
public class SessionToBehaviorModelTransformation extends AbstractConsumerStage<UserSession> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionToBehaviorModelTransformation.class);

    private final OutputPort<BehaviorModel> outputPort = this.createOutputPort();

    private final String prefix;

    /**
     * Create a behavior model from a session filter.
     *
     * @param prefix
     *            name prefix for all models
     */
    public SessionToBehaviorModelTransformation(final String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void execute(final UserSession session) {
        this.outputPort.send(this.convert(session));
    }

    public OutputPort<BehaviorModel> getOutputPort() {
        return this.outputPort;
    }

    /**
     * Convert a user session into a behavior model.
     *
     * @param session
     *            user session
     * @return returns a behavior model
     */
    public BehaviorModel convert(final UserSession session) {
        final BehaviorModel model = new BehaviorModel();
        model.setName(this.prefix);

        final List<EntryCallEvent> entryCalls = session.getEvents();
        final Iterator<EntryCallEvent> iterator = entryCalls.iterator();

        // Assume list has at least one element
        if (iterator.hasNext()) {
            EntryCallNode lastNode = this.createNode((PayloadAwareEntryCallEvent) iterator.next());
            EntryCallNode currentNode;

            while (iterator.hasNext()) {
                currentNode = this.createNode((PayloadAwareEntryCallEvent) iterator.next());

                model.addNode(currentNode, true);
                model.addEdge(new EntryCallEdge(lastNode, currentNode), false);
                lastNode = currentNode;
            }

            return model;
        } else {
            SessionToBehaviorModelTransformation.LOGGER.error("Empty user session creates empty model");
            return model;
        }
    }

    private EntryCallNode createNode(final PayloadAwareEntryCallEvent event) {
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