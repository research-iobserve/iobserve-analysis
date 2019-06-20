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
package org.iobserve.service.behavior.analysis.clustering;

import java.util.Map;

import org.iobserve.service.behavior.analysis.model.BehaviorModelEdge;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.iobserve.service.behavior.analysis.model.EventGroup;

import mtree.DistanceFunction;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class GraphEditDistance implements DistanceFunction<BehaviorModelGED> {

    private static final double NODE_INSERT_COST = 10;

    private static final double EDGE_INSERT_COST = 5;

    private static final double EVENT_GROUP_INSERT_COST = 4;

    private static final double EVENT_INSERT_COST = 1;

    private BehaviorModelGED model1;
    private BehaviorModelGED model2;

    // costs to convert model1 into model2
    @Override
    public double calculate(final BehaviorModelGED modelA, final BehaviorModelGED modelB) {
        this.model1 = modelA;
        this.model2 = modelB;

        double distance = 0;

        // check if nodes from model1 are in model2
        for (final Map.Entry<String, BehaviorModelNode> pair : this.model1.getNodes().entrySet()) {
            final String signature = pair.getKey();
            final BehaviorModelNode node = pair.getValue();

            final BehaviorModelNode match = this.model2.getNodes().get(signature);

            if (match == null) {
                distance += this.nodeInsertionCost(node);
            } else {
                distance += this.nodeDistance(node, match);
            }
        }

        // check if nodes from model2 are in model1
        for (final Map.Entry<String, BehaviorModelNode> pair : this.model2.getNodes().entrySet()) {
            final String signature = pair.getKey();
            final BehaviorModelNode node = pair.getValue();

            final BehaviorModelNode match = this.model1.getNodes().get(signature);

            if (match == null) {
                distance += this.nodeInsertionCost(node);
            }
        }
        return distance;
    }

    private double nodeDistance(final BehaviorModelNode node1, final BehaviorModelNode node2) {
        double distance = 0;
        for (final BehaviorModelEdge edge : node1.getIngoingEdges().values()) {
            final BehaviorModelNode source1 = edge.getSource();
            final BehaviorModelNode source2 = this.model2.getNodes().get(source1.getName());

            final BehaviorModelEdge match = node2.getIngoingEdges().get(source2);
            if (match == null) {
                distance += this.edgeInsertionCost(edge);
            } else {
                distance += this.edgeDistance(edge, match);
            }
        }
        for (final BehaviorModelEdge edge : node2.getIngoingEdges().values()) {
            final BehaviorModelNode source2 = edge.getSource();
            final BehaviorModelNode source1 = this.model1.getNodes().get(source2.getName());
            final BehaviorModelEdge match = node1.getIngoingEdges().get(source1);
            if (match == null) {
                distance += this.edgeInsertionCost(edge);
            }
        }
        return distance;
    }

    private double edgeDistance(final BehaviorModelEdge edge1, final BehaviorModelEdge edge2) {
        double distance = 0;
        // TODO this method can be more efficient
        for (final EventGroup eventGroup1 : edge1.getEventGroups()) {
            boolean success = false;
            for (final EventGroup eventGroup2 : edge2.getEventGroups()) {
                if (eventGroup1.hasSameParameters(eventGroup2)) {
                    distance += this.eventGroupDistance(eventGroup1, eventGroup2);
                    success = true;
                    break;
                }
            }
            if (!success) {
                distance += this.eventGroupInsertionCost(eventGroup1);
            }
        }

        for (final EventGroup eventGroup2 : edge2.getEventGroups()) {
            boolean success = false;
            for (final EventGroup eventGroup1 : edge1.getEventGroups()) {
                if (eventGroup1.hasSameParameters(eventGroup2)) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                distance += this.eventGroupInsertionCost(eventGroup2);
            }
        }

        return distance;
    }

    private double eventGroupDistance(final EventGroup eventGroup1, final EventGroup eventGroup2) {
        final int amountDifference = Math.abs(eventGroup1.getEvents().size() - eventGroup2.getEvents().size());

        final double distance = amountDifference * GraphEditDistance.EVENT_INSERT_COST;
        return distance;
    }

    private double nodeInsertionCost(final BehaviorModelNode node) {
        double distance = GraphEditDistance.NODE_INSERT_COST;

        for (final BehaviorModelEdge edge : node.getOutgoingEdges().values()) {
            distance += this.edgeInsertionCost(edge);
        }
        return distance;
    }

    private double edgeInsertionCost(final BehaviorModelEdge edge) {
        double distance = GraphEditDistance.EDGE_INSERT_COST;
        for (final EventGroup eventGroup : edge.getEventGroups()) {
            distance += this.eventGroupInsertionCost(eventGroup);
        }
        return distance;
    }

    private double eventGroupInsertionCost(final EventGroup eventGroup) {
        double distance = GraphEditDistance.EVENT_GROUP_INSERT_COST;
        distance += eventGroup.getEvents().size() * GraphEditDistance.EVENT_INSERT_COST;
        return distance;
    }
}