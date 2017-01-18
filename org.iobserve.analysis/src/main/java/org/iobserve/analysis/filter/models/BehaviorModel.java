/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.filter.models;

import java.util.List;

/**
 * Represents the user Behavior of a user or a group of users
 *
 * @author Christoph Dornieden
 *
 */
public class BehaviorModel {

    private EntryCallNode rootEntryCall;
    private List<EntryCallNode> entryCallNodes;
    private List<EntryCallEdge> entryCallEdges;
    private List<UserSession> userSessions;

    public EntryCallNode getRootEntryCall() {
        return this.rootEntryCall;
    }

    public void setRootEntryCall(final EntryCallNode rootEntryCall) {
        this.rootEntryCall = rootEntryCall;
    }

    public List<EntryCallNode> getEntryCallNodes() {
        return this.entryCallNodes;
    }

    public void setEntryCallNodes(final List<EntryCallNode> entryCallNodes) {
        this.entryCallNodes = entryCallNodes;
    }

    public List<EntryCallEdge> getEntryCallEdges() {
        return this.entryCallEdges;
    }

    public void setEntryCallEdges(final List<EntryCallEdge> entryCallEdges) {
        this.entryCallEdges = entryCallEdges;
    }

    public List<UserSession> getUserSessions() {
        return this.userSessions;
    }

    public void setUserSessions(final List<UserSession> userSessions) {
        this.userSessions = userSessions;
    }

}
