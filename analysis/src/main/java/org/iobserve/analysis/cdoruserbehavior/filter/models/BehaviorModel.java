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
package org.iobserve.analysis.cdoruserbehavior.filter.models;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.iobserve.analysis.cdoruserbehavior.util.SingleOrNoneCollector;

/**
 * Represents the user Behavior of a user or a group of users
 *
 * @author Christoph Dornieden
 *
 */
public class BehaviorModel {
    private String name;
    private final Set<EntryCallNode> entryCallNodes;
    private final Set<EntryCallEdge> entryCallEdges;

    /**
     * constructor
     */
    public BehaviorModel() {
        this.entryCallNodes = new HashSet<>();
        this.entryCallEdges = new HashSet<>();
    }

    /**
     * add an edge to the model
     *
     * @param edge
     */
    public void addEdge(final EntryCallEdge edge) {

        // edge already existing?
        final Optional<EntryCallEdge> matchedEdge = this.entryCallEdges.stream().filter(edge::equals)
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
            this.entryCallEdges.add(edge);
        }
    }

    /**
     * Checks if a similar node is already present in the behavior model and returns it
     *
     * @param node
     *            node to check
     * @return matching node if present
     */
    public Optional<EntryCallNode> findNode(final EntryCallNode node) {
        final Optional<EntryCallNode> matchingNode = this.entryCallNodes.stream().filter(node::equals)
                .collect(new SingleOrNoneCollector<>());
        return matchingNode;
    }

    /**
     * add a node to the model
     *
     * @param node
     *            node
     * @return input node if no node with the same signature is added, merged node else
     */
    public EntryCallNode addNode(final EntryCallNode node) {
        return this.mergeNode(node);
    }

    /**
     * getter
     *
     * @return entry call nodes
     */
    public Set<EntryCallNode> getEntryCallNodes() {
        return this.entryCallNodes;
    }

    /**
     * getter
     *
     * @return entry call edges
     */
    public Set<EntryCallEdge> getEntryCallEdges() {
        return this.entryCallEdges;
    }

    /**
     * getter
     *
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * setter
     *
     * @param name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * merge node into entryCallNodes
     *
     * @param node
     *            node
     * @return merged node
     */
    private EntryCallNode mergeNode(final EntryCallNode node) {
        if (this.entryCallNodes.isEmpty()) {
            this.entryCallNodes.add(node);
            return node;

        } else {
            final Optional<EntryCallNode> matchingNode = this.entryCallNodes.stream().filter(node::equals)
                    .collect(new SingleOrNoneCollector<>());

            matchingNode.ifPresent(match -> match.mergeInformation(node.getEntryCallInformation()));
            final EntryCallNode mergedNode = matchingNode.isPresent() ? matchingNode.get() : node;

            this.entryCallNodes.add(mergedNode);

            return mergedNode;
        }
    }

}
