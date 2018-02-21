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
package org.iobserve.analysis.clustering.behaviormodels;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the user Behavior of a user or a group of users.
 *
 * @author Christoph Dornieden
 *
 */
public class BehaviorModel {
    /** unique name to identify the model. */
    private String name;
    /** all nodes of the behavior graph. */
    private final Map<String, EntryCallNode> nodes;
    /** all edges of the behavior graph. */
    private final Map<String, EntryCallEdge> edges;

    /**
     * constructor.
     */
    public BehaviorModel() {
        this.nodes = new HashMap<>();
        this.edges = new HashMap<>();
    }

    public EntryCallNode[] getNodes() {
        return (EntryCallNode[]) this.nodes.values().toArray();
    }

    public EntryCallEdge[] getEdges() {
        return (EntryCallEdge[]) this.edges.values().toArray();
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Add an edge to the model.
     *
     * @param edge
     *            the edge to be added to the model
     */
    public void addEdge(final EntryCallEdge edge) {
        final String key = edge.getSource().toString() + edge.getTarget().toString();
        final EntryCallEdge matchedEdge = this.edges.get(key);

        if (matchedEdge == null) {
            final EntryCallNode source = this.addNode(edge.getSource());
            final EntryCallNode target = this.addNode(edge.getTarget());

            edge.setSource(source);
            edge.setTarget(target);
            this.edges.put(key, edge);
        } else {
            matchedEdge.addCalls(edge.getCalls());
        }
    }

    /**
     * add a node to the model.
     *
     * @param node
     *            node
     * @return input node if no node with the same signature is added, merged node
     *         else
     */
    public EntryCallNode addNode(final EntryCallNode node) {
        final String key = node.toString();
        final EntryCallNode matchingNode = this.nodes.get(key);

        if (matchingNode == null) {
            this.nodes.put(key, node);
            return node;
        } else {
            matchingNode.mergeInformation(node.getEntryCallInformation());
            return matchingNode;
        }
    }

}
