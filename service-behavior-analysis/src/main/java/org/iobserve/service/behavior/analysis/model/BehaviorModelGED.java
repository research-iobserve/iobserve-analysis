package org.iobserve.service.behavior.analysis.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

public class BehaviorModelGED {
    private final Map<String, BehaviorModelNode> nodes;

    
    public BehaviorModelGED() {
        this.nodes = new HashMap<>();
    }


    public BehaviorModelGED(final Map<String, BehaviorModelNode> nodes) {
        this.nodes = nodes;
    }

    public Map<String, BehaviorModelNode> getNodes() {
        return this.nodes;
    }
}
