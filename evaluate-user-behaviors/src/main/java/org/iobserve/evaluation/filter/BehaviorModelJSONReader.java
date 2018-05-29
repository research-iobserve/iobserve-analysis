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
package org.iobserve.evaluation.filter;

import java.io.File;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import teetime.framework.AbstractProducerStage;

import org.iobserve.analysis.behavior.models.extended.BehaviorModel;
import org.iobserve.analysis.behavior.models.extended.CallInformation;
import org.iobserve.analysis.behavior.models.extended.EntryCallEdge;
import org.iobserve.analysis.behavior.models.extended.EntryCallNode;

/**
 * Read a JSON serialized behavior model.
 *
 * @author Reiner Jung
 *
 */
public class BehaviorModelJSONReader extends AbstractProducerStage<BehaviorModel> {

    private final File inputFile;

    /**
     * Create a JSON reader stage.
     *
     * @param inputFile
     *            JSON file source
     */
    public BehaviorModelJSONReader(final File inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    protected void execute() throws Exception {
        final ObjectMapper mapper = new ObjectMapper();

        /** Have to read the model by hand */
        final JsonNode tree = mapper.readTree(this.inputFile);
        if (!(tree instanceof ObjectNode)) {
            throw new Exception("Wrong format");
        }
        final ObjectNode modelNode = (ObjectNode) tree;

        final BehaviorModel model = new BehaviorModel();

        /** Read name. */
        final JsonNode name = modelNode.get("name");
        if (name.isTextual()) {
            model.setName(name.textValue());
        }

        // Read nodes
        final JsonNode nodesNode = modelNode.get("nodes");
        if (nodesNode.isArray()) {
            final ArrayNode nodesArray = (ArrayNode) nodesNode;
            final Iterator<JsonNode> nodesIter = nodesArray.elements();
            while (nodesIter.hasNext()) {
                final EntryCallNode newECNode = new EntryCallNode();
                final ObjectNode node = (ObjectNode) nodesIter.next();
                newECNode.setSignature(node.get("signature").textValue());

                final ArrayNode callInfos = (ArrayNode) node.get("entryCallInformation");
                final Iterator<JsonNode> callInfoIter = callInfos.elements();
                while (callInfoIter.hasNext()) {
                    final ObjectNode ciNode = (ObjectNode) callInfoIter.next();
                    final CallInformation info = new CallInformation(ciNode.get("informationSignature").textValue(),
                            ciNode.get("informationCode").textValue(), ciNode.get("count").asInt());
                    newECNode.mergeCallInformation(info);
                }

                model.addNode(newECNode, false);
            }
        }

        final ArrayNode edgesNode = (ArrayNode) modelNode.findValue("edges");
        final Iterator<JsonNode> edgesIter = edgesNode.elements();
        while (edgesIter.hasNext()) {
            final ObjectNode edge = (ObjectNode) edgesIter.next();
            final ObjectNode source = (ObjectNode) edge.get("source");
            final ObjectNode target = (ObjectNode) edge.get("target");
            final EntryCallNode sourceECNode = model.findNode(source.get("signature").textValue()).get();
            final EntryCallNode targetECNode = model.findNode(target.get("signature").textValue()).get();
            final EntryCallEdge newECEdge = new EntryCallEdge(sourceECNode, targetECNode, edge.get("calls").asInt());
            model.addEdge(newECEdge, false);
        }

        this.outputPort.send(model);

        this.workCompleted();
    }

}
