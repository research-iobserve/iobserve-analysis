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
package org.iobserve.service.behavior.analysis.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A behavior model representing a user session. It is designed as a graph, where the edges contain
 * the events.
 *
 * @author Lars Jürgensen
 *
 */
public class BehaviorModelGED {
    private Map<String, BehaviorModelNode> nodes = new HashMap<>();

    private Set<BehaviorModelEdge> edges = new HashSet<>();

    public Map<String, BehaviorModelNode> getNodes() {
        return this.nodes;
    }

    public Set<BehaviorModelEdge> getEdges() {
        return this.edges;
    }

    public void setNodes(final Map<String, BehaviorModelNode> nodes) {
        this.nodes = nodes;
    }

    public void setEdges(final Set<BehaviorModelEdge> edges) {
        this.edges = edges;
    }

}
