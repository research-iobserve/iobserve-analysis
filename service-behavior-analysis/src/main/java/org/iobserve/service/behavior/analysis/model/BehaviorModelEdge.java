package org.iobserve.service.behavior.analysis.model;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

public class BehaviorModelEdge {

    private final List<PayloadAwareEntryCallEvent> events;
    private final BehaviorModelNode source;
    private final BehaviorModelNode target;

    public BehaviorModelEdge(final PayloadAwareEntryCallEvent event, final BehaviorModelNode source,
            final BehaviorModelNode target) {
        this.events = new ArrayList<>();
        this.events.add(event);
        this.source = source;
        this.target = target;
    }

    public void addEvent(final PayloadAwareEntryCallEvent event) {
        this.events.add(event);
    }

}
