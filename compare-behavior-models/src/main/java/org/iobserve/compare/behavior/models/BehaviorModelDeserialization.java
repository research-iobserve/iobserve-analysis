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
package org.iobserve.compare.behavior.models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.iobserve.service.behavior.analysis.model.BehaviorModelEdge;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.iobserve.service.behavior.analysis.model.EventGroup;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class BehaviorModelDeserialization {

    private final String input;

    public BehaviorModelDeserialization(final String input) {
        this.input = input;
    }

    public BehaviorModelGED deserialize() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();

        final JsonNode tree = mapper.readTree(this.input);
        if (!(tree instanceof ObjectNode)) {
            throw new IOException("Wrong format");
        }

        final ObjectNode jsonModel = (ObjectNode) tree;

        final BehaviorModelGED model = new BehaviorModelGED();

        // Read nodes
        final JsonNode jsonNode = jsonModel.get("nodes");

        final Iterator<String> nodeIterator = jsonNode.fieldNames();

        while (nodeIterator.hasNext()) {
            final String operationSignature = nodeIterator.next();
            model.getNodes().put(operationSignature, new BehaviorModelNode(operationSignature));
        }

        // Read edges
        final JsonNode jsonEdges = jsonModel.get("edges");

        final Iterator<JsonNode> edgeIterator = jsonEdges.elements();

        while (edgeIterator.hasNext()) {
            final JsonNode jsonEdge = edgeIterator.next();
            final String source = jsonEdge.get("source").get("name").asText();
            final String target = jsonEdge.get("target").get("name").asText();

            final BehaviorModelNode sourceMatch = model.getNodes().get(source);
            final BehaviorModelNode targetMatch = model.getNodes().get(target);

            final BehaviorModelEdge newEdge = new BehaviorModelEdge(sourceMatch, targetMatch);
            model.getEdges().add(newEdge);
            sourceMatch.getOutgoingEdges().put(targetMatch, newEdge);
            targetMatch.getIngoingEdges().put(sourceMatch, newEdge);

            // Read event groups
            final JsonNode jsonEventGroups = jsonEdge.get("eventGroups");

            final Iterator<JsonNode> eventGroupIterator = jsonEventGroups.elements();

            while (eventGroupIterator.hasNext()) {
                final JsonNode jsonEventGroup = eventGroupIterator.next();
                final Iterator<JsonNode> eventIterator = jsonEventGroup.get("events").elements();

                final List<PayloadAwareEntryCallEvent> events = new ArrayList<>();
                while (eventIterator.hasNext()) {
                    final JsonNode jsonEvent = eventIterator.next();

                    final String operationSignature = jsonEvent.get("operationSignature").asText();
                    final String[] parameters = mapper.treeToValue(jsonEvent.get("parameters"), String[].class);
                    final String[] values = mapper.treeToValue(jsonEvent.get("values"), String[].class);
                    final PayloadAwareEntryCallEvent event = new PayloadAwareEntryCallEvent(0, 0, operationSignature,
                            operationSignature, "", "", parameters, values, 0);
                    events.add(event);

                }

                final EventGroup group = new EventGroup(events.get(0).getParameters());
                group.getEvents().addAll(events);

                newEdge.getEventGroups().add(group);
            }

        }

        return model;

    }
}
