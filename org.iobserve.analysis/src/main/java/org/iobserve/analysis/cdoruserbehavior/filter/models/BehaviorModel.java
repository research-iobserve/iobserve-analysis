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
import org.iobserve.analysis.filter.models.UserSession;

/**
 * Represents the user Behavior of a user or a group of users
 *
 * @author Christoph Dornieden
 *
 */
public class BehaviorModel {

    private EntryCallNode rootEntryCall;
    private final Set<EntryCallNode> entryCallNodes;
    private final Set<EntryCallEdge> entryCallEdges;
    private final Set<UserSession> userSessions;

    /**
     * constructor
     */
    public BehaviorModel() {
        this.rootEntryCall = null;
        this.entryCallNodes = new HashSet<>();
        this.entryCallEdges = new HashSet<>();
        this.userSessions = new HashSet<>();
    }

    /**
     * add an edge to the model
     *
     * @param edge
     */
    public void addEdge(EntryCallEdge edge) {

        // edge already existing?
        final Optional<EntryCallEdge> matchedEdge = this.entryCallEdges.stream().filter(edge::equals)
                .collect(new SingleOrNoneCollector<>());

        if (matchedEdge.isPresent()) {
            final long currentCalls = matchedEdge.get().getCalls();
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
     * add a node to the model
     *
     * @param node
     *            node
     */
    public void addNode(final EntryCallNode node) {
        this.mergeNode(node);
    }

    /**
     * getter
     *
     * @return root entry call node
     */
    public EntryCallNode getRootEntryCall() {
        return this.rootEntryCall;
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
     * @return user sessions
     */
    public Set<UserSession> getUserSessions() {
        return this.userSessions;
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
            this.rootEntryCall = node;
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
