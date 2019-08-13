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
package org.iobserve.compare.behavior.models;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.service.behavior.analysis.model.BehaviorModelEdge;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.iobserve.service.behavior.analysis.model.EventGroup;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class BehaviorModelDifference {

    private final List<BehaviorModelNode> referenceNodes = new ArrayList<>();
    private final List<BehaviorModelNode> testModelNodes = new ArrayList<>();

    private final List<BehaviorModelEdge> referenceEdges = new ArrayList<>();
    private final List<BehaviorModelEdge> testModelEdges = new ArrayList<>();

    private final List<EventGroup> referenceEventGroup = new ArrayList<>();
    private final List<EventGroup> testModelEventGroup = new ArrayList<>();

    private final List<PayloadAwareEntryCallEvent> referenceEvents = new ArrayList<>();
    private final List<PayloadAwareEntryCallEvent> testModelEvents = new ArrayList<>();

    private double graphEditDistance = -1;

    /**
     * Default constructor.
     */
    public BehaviorModelDifference() {
        // empty
    }

    public List<BehaviorModelNode> getReferenceNodes() {
        return this.referenceNodes;
    }

    public List<BehaviorModelNode> getTestModelNodes() {
        return this.testModelNodes;
    }

    public List<BehaviorModelEdge> getReferenceEdges() {
        return this.referenceEdges;
    }

    public List<BehaviorModelEdge> getTestModelEdges() {
        return this.testModelEdges;
    }

    public List<EventGroup> getReferenceEventGroup() {
        return this.referenceEventGroup;
    }

    public List<EventGroup> getTestModelEventGroup() {
        return this.testModelEventGroup;
    }

    public List<PayloadAwareEntryCallEvent> getReferenceEvents() {
        return this.referenceEvents;
    }

    public List<PayloadAwareEntryCallEvent> getTestModelEvents() {
        return this.testModelEvents;
    }

    public double getGraphEditDistance() {
        return this.graphEditDistance;
    }

    public void setGraphEditDistance(final double graphEditDistance) {
        this.graphEditDistance = graphEditDistance;
    }

}
