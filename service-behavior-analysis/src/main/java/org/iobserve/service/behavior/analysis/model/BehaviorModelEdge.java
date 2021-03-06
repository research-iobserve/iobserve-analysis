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

import java.util.ArrayList;
import java.util.List;

import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

/**
 * The edge between two nodes of a BehaviorModel.
 *
 * @author Lars Jürgensen
 *
 */
public class BehaviorModelEdge {

    private final List<EventGroup> eventGroups;

    private final BehaviorModelNode source;

    private final BehaviorModelNode target;

    /**
     * Create a new edge between two nodes.
     *
     * @param source
     *            the source node of the edge
     * @param target
     *            the target node of the edge
     */
    public BehaviorModelEdge(final BehaviorModelNode source, final BehaviorModelNode target) {
        this.eventGroups = new ArrayList<>();
        this.source = source;
        this.target = target;
    }

    /**
     * Create a new edge between two nodes.
     *
     * @param event
     *            the event which is represented by the edge
     * @param source
     *            the source node of the edge
     * @param target
     *            the target node of the edge
     */
    public BehaviorModelEdge(final PayloadAwareEntryCallEvent event, final BehaviorModelNode source,
            final BehaviorModelNode target) {
        this.eventGroups = new ArrayList<>();
        this.addEvent(event);
        this.source = source;
        this.target = target;
    }

    /**
     * Add a new event to the edge
     *
     * @param event
     *            the event which should be added to the edge
     */
    public void addEvent(final PayloadAwareEntryCallEvent event) {
        boolean success = false;
        for (final EventGroup eventGroup : this.eventGroups) {
            if (eventGroup.hasSameParameters(event)) {
                eventGroup.getEvents().add(event);
                success = true;
                break;
            }
        }
        if (!success) {
            final EventGroup newEventGroup = new EventGroup(event.getParameters());
            newEventGroup.getEvents().add(event);
            this.eventGroups.add(newEventGroup);
        }
    }

    public BehaviorModelNode getSource() {
        return this.source;
    }

    public BehaviorModelNode getTarget() {
        return this.target;
    }

    public List<EventGroup> getEventGroups() {
        return this.eventGroups;
    }

}
