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
package org.iobserve.analysis.behavior.models.basic;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.iobserve.analysis.behavior.SingleOrNoneCollector;

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
    private final Set<EntryCallNode> nodes;
    /** all edges of the behavior graph. */
    private final Set<EntryCallEdge> edges;

    /**
     * constructor.
     */
    public BehaviorModel() {
        this.nodes = new HashSet<>();
        this.edges = new HashSet<>();
    }

    public Set<EntryCallNode> getNodes() {
        return this.nodes;
    }

    public Set<EntryCallEdge> getEdges() {
        return this.edges;
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

        // edge already existing?
        final Optional<EntryCallEdge> matchedEdge = this.edges.stream().filter(edge::equals)
                .collect(new SingleOrNoneCollector<>());

        if (matchedEdge.isPresent()) {
            final double currentCalls = matchedEdge.get().getCalls();
            matchedEdge.get().setCalls(currentCalls + edge.getCalls());
        } else {
            // add edge and check for doubled nodes
            final EntryCallNode source = this.mergeNode(edge.getSource());
            final EntryCallNode target = this.mergeNode(edge.getTarget());

            edge.setSource(source);
            edge.setTarget(target);
            this.edges.add(edge);
        }
    }

    /**
     * Checks if a similar node is already present in the behavior model and returns it.
     *
     * @param node
     *            node to check
     * @return matching node if present
     */
    public Optional<EntryCallNode> findNode(final EntryCallNode node) {
        final Optional<EntryCallNode> matchingNode = this.nodes.stream().filter(node::equals)
                .collect(new SingleOrNoneCollector<>());
        return matchingNode;
    }

    /**
     * add a node to the model.
     *
     * @param node
     *            node
     * @return input node if no node with the same signature is added, merged node else
     */
    public EntryCallNode addNode(final EntryCallNode node) {
        return this.mergeNode(node);
    }

    /**
     * merge node into nodes.
     *
     * @param node
     *            node
     * @return merged node
     */
    private EntryCallNode mergeNode(final EntryCallNode node) {
        if (this.nodes.isEmpty()) {
            this.nodes.add(node);
            return node;

        } else {
            final Optional<EntryCallNode> matchingNode = this.nodes.stream().filter(node::equals)
                    .collect(new SingleOrNoneCollector<>());

            matchingNode.ifPresent(match -> match.mergeInformation(node.getEntryCallInformation()));
            final EntryCallNode mergedNode = matchingNode.isPresent() ? matchingNode.get() : node;

            this.nodes.add(mergedNode);

            return mergedNode;
        }
    }

}
