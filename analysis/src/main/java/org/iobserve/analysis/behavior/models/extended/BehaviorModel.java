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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the user Behavior of a user or a group of users.
 *
 * @author Christoph Dornieden
 * @author Jannis Kuckei
 *
 */
public class BehaviorModel {
    /**
     * Edges are stored in a map where they key is the signature of the source and target, separated
     * by the following string (models can later be stored as JSON, this is for readability
     * reasons).
     */
    private static final String EDGE_KEY_SEPARATOR = "->";

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

    public Collection<EntryCallNode> getNodes() {
        return this.nodes.values();
    }

    public Collection<EntryCallEdge> getEdges() {
        return this.edges.values();
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
     * @param mergeNodes
     *            If true, will merge already existing nodes. Otherwise, it will ignore them.
     */
    public void addEdge(final EntryCallEdge edge, final boolean mergeNodes) {
        final String key = edge.getSource().getSignature() + BehaviorModel.EDGE_KEY_SEPARATOR
                + edge.getTarget().getSignature();
        final EntryCallEdge matchedEdge = this.edges.get(key);

        if (matchedEdge == null) {
            final EntryCallNode source = this.addNode(edge.getSource(), mergeNodes);
            final EntryCallNode target = this.addNode(edge.getTarget(), mergeNodes);

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
     * @param merge
     *            If true, will merge call information with already existing node. If false, will
     *            have no side effects.
     * @return input node if no node with the same signature is added, merged node else
     */
    public EntryCallNode addNode(final EntryCallNode node, final boolean merge) {
        final String key = node.getSignature();
        final EntryCallNode matchingNode = this.nodes.get(key);

        if (matchingNode == null) {
            this.nodes.put(key, node);
            return node;
        } else {
            if (merge) {
                matchingNode.mergeInformation(node.getEntryCallInformation());
            }
            return matchingNode;
        }
    }

    /**
     * Finds a node with a specific signature and returns an Optional of it.
     *
     * @param signature
     *            The signature of the node to find
     * @return Returns an optional with the search result
     */
    public Optional<EntryCallNode> findNode(final String signature) {
        final EntryCallNode result = this.nodes.get(signature);
        return Optional.ofNullable(result);
    }

    /**
     * Finds an edge using the signature of its source and target node and returns an Optional of
     * it.
     *
     * @param sourceSignature
     *            The signature of the edge's source node
     * @param targetSignature
     *            The signature of the edge's target node
     * @return Returns an optional with the search result
     */
    public Optional<EntryCallEdge> findEdge(final String sourceSignature, final String targetSignature) {
        final EntryCallEdge result = this.edges
                .get(sourceSignature + BehaviorModel.EDGE_KEY_SEPARATOR + targetSignature);
        return Optional.ofNullable(result);
    }
}
