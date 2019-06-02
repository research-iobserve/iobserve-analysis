package org.iobserve.service.behavior.analysis.model;

import java.util.HashMap;
import java.util.Map;

import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

public class BehaviorModelNode {
    private final Map<BehaviorModelNode, BehaviorModelEdge> edges;

    public Map<BehaviorModelNode, BehaviorModelEdge> getEdges() {
        return this.edges;
    }

    private final String name;

    public BehaviorModelNode(final String signiture) {
        this.edges = new HashMap<>();
        this.name = signiture;
    }

    public void addEdge(final PayloadAwareEntryCallEvent event, final BehaviorModelNode target) {
        final BehaviorModelEdge matchingEdge = this.edges.get(target);

        if (matchingEdge == null) {
            final BehaviorModelEdge newEdge = new BehaviorModelEdge(event, this, target);
            this.edges.put(target, newEdge);
        } else {
            matchingEdge.addEvent(event);
        }
    }

}
