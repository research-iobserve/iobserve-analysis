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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import kieker.common.configuration.Configuration;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.service.behavior.analysis.model.BehaviorModelEdge;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;
import org.iobserve.service.behavior.analysis.model.EventGroup;
import org.iobserve.stages.general.data.PayloadAwareEntryCallEvent;

import mtree.DistanceFunction;

/**
 *
 * @author Lars Jürgensen
 *
 *         This class calculates a custom graph edit distance between two Behavior Models.
 *
 *         The following operations are allowed:
 *
 *         insert/delete nodes; insert/delete edges; insert/delete EventGroups; insert/delete
 *         Events; duplicate/remove duplicate events
 *
 *         Insertion and Deletion always costs the same to satisfy the symmetry property.
 *
 */
public class GraphEditDistance implements DistanceFunction<BehaviorModelGED> {

    // The operation costs. They can be changed by the "setConfiguration" function
    private static double nodeInsertCost = 10;

    private static double edgeInsertCost = 5;
    private static double eventGroupInsertCost = 4;

    /**
     * the WEIGTHING assigns events a insertion and duplication costs
     */
    private static final JPetStoreParameterWeighting WEIGTHING = new JPetStoreParameterWeighting();

    // the models to be compared
    private BehaviorModelGED model1;
    private BehaviorModelGED model2;

    /**
     * Calculates the Graph Edit Distance between two objects
     *
     * @param modelA
     *            The first model.
     * @param modelB
     *            The second model.
     */
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

            if (match == null) { // node only occurs in one objects => must be inserted
                distance += this.nodeInsertionCost(node);
            } else { // node occurs in both objects => must be compared
                distance += this.nodeDistance(node, match);
            }
        }

        // check if nodes from model2 are in model1
        for (final Map.Entry<String, BehaviorModelNode> pair : this.model2.getNodes().entrySet()) {
            final String signature = pair.getKey();
            final BehaviorModelNode node = pair.getValue();

            final BehaviorModelNode match = this.model1.getNodes().get(signature);

            // node only occurs in one objects => must be inserted
            if (match == null) {
                distance += this.nodeInsertionCost(node);
            }
        }
        return distance;
    }

    /**
     * Calculates the distance between two nodes. This includes the distance between the ingoing
     * edges
     */
    private double nodeDistance(final BehaviorModelNode node1, final BehaviorModelNode node2) {
        double distance = 0;
        for (final BehaviorModelEdge edge : node1.getIngoingEdges().values()) {
            final BehaviorModelNode source1 = edge.getSource();
            final BehaviorModelNode source2 = this.model2.getNodes().get(source1.getName());

            final BehaviorModelEdge match = node2.getIngoingEdges().get(source2);

            if (match == null) { // edge only occurs in one node => must be inserted
                distance += this.edgeInsertionCost(edge);
            } else { // edge occurs in both nodes => must be compared
                distance += this.edgeDistance(edge, match);
            }
        }
        for (final BehaviorModelEdge edge : node2.getIngoingEdges().values()) {
            final BehaviorModelNode source2 = edge.getSource();
            final BehaviorModelNode source1 = this.model1.getNodes().get(source2.getName());
            final BehaviorModelEdge match = node1.getIngoingEdges().get(source1);

            // edge only occurs in one node => must be inserted
            if (match == null) {
                distance += this.edgeInsertionCost(edge);
            }
        }
        return distance;
    }

    /**
     * Calculates the distance between two edges. This includes the distance between the contained
     * eventgroups
     *
     */
    private double edgeDistance(final BehaviorModelEdge edge1, final BehaviorModelEdge edge2) {
        double distance = 0;

        for (final EventGroup eventGroup1 : edge1.getEventGroups()) {
            boolean success = false;
            for (final EventGroup eventGroup2 : edge2.getEventGroups()) {
                if (eventGroup1.hasSameParameters(eventGroup2)) {
                    // matching eventGroup found => must be compared
                    distance += this.eventGroupDistance(eventGroup1, eventGroup2);
                    success = true;
                    break;
                }
            }
            // no matching eventGroup found => must be inserted
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
            // if not necessary, as success should always be false
            if (!success) {
                // no matching eventGroup found => must be inserted
                distance += this.eventGroupInsertionCost(eventGroup2);
            }
        }

        return distance;
    }

    /**
     * Calculates the distance between two event groups. This includes the distance between the
     * contained events
     *
     */
    private double eventGroupDistance(final EventGroup eventGroup1, final EventGroup eventGroup2) {
        double distance = 0;

        // at first all events are unvisited
        final Queue<PayloadAwareEntryCallEvent> unvisitedEvents1 = new LinkedList<>(eventGroup1.getEvents());
        final Queue<PayloadAwareEntryCallEvent> unvisitedEvents2 = new LinkedList<>(eventGroup2.getEvents());

        while (!unvisitedEvents1.isEmpty()) {

            final List<PayloadAwareEntryCallEvent> matches1 = new ArrayList<>();
            final List<PayloadAwareEntryCallEvent> matches2 = new ArrayList<>();

            final PayloadAwareEntryCallEvent event = unvisitedEvents1.poll();
            matches1.add(event);

            // find equal events in same event group
            for (final PayloadAwareEntryCallEvent potentialMatch : unvisitedEvents1) {

                if (GraphEditDistance.haveSameValues(event, potentialMatch)) {

                    matches1.add(potentialMatch);
                }
            }
            unvisitedEvents1.removeAll(matches1);

            // find equal events in other event group
            for (final PayloadAwareEntryCallEvent potentialMatch : unvisitedEvents2) {
                if (GraphEditDistance.haveSameValues(event, potentialMatch)) {
                    matches2.add(potentialMatch);
                }
            }
            unvisitedEvents2.removeAll(matches2);

            // events have to be created in other eventgroup
            if (matches2.isEmpty()) {
                distance += GraphEditDistance.WEIGTHING.getInsertCost(event.getParameters());
                distance += GraphEditDistance.WEIGTHING.getDuplicateCost(event.getParameters()) * (matches1.size() - 1);
            } else { // event occurs in both groups, so it has to be duplicated, till amount is the
                     // same
                final int amountDifference = Math.abs(matches1.size() - matches2.size());
                distance += GraphEditDistance.WEIGTHING.getInsertCost(event.getParameters()) * amountDifference;
            }

        }

        // maybe some events are left in the second group, which weren't in the first group
        while (!unvisitedEvents2.isEmpty()) {

            final PayloadAwareEntryCallEvent event1 = unvisitedEvents2.poll();

            final List<PayloadAwareEntryCallEvent> equalEvents = new ArrayList<>();

            for (final PayloadAwareEntryCallEvent event2 : unvisitedEvents2) {
                if (GraphEditDistance.haveSameValues(event1, event2)) {

                    equalEvents.add(event2);
                }
            }
            unvisitedEvents2.removeAll(equalEvents);
            distance += equalEvents.size() * GraphEditDistance.WEIGTHING.getDuplicateCost(event1.getParameters());
            distance += GraphEditDistance.WEIGTHING.getInsertCost(event1.getParameters());

        }
        return distance;
    }

    /**
     * calculates the insertion cost of a node including the insertion cost of the ingoing edges
     */
    private double nodeInsertionCost(final BehaviorModelNode node) {
        double distance = GraphEditDistance.nodeInsertCost;

        for (final BehaviorModelEdge edge : node.getIngoingEdges().values()) {
            distance += this.edgeInsertionCost(edge);
        }
        return distance;
    }

    /**
     * calculates the insertion cost of an edge including the insertion cost of the contained
     * eventgroups
     */
    private double edgeInsertionCost(final BehaviorModelEdge edge) {
        double distance = GraphEditDistance.edgeInsertCost;
        for (final EventGroup eventGroup : edge.getEventGroups()) {
            distance += this.eventGroupInsertionCost(eventGroup);
        }
        return distance;
    }

    /**
     * calculates the insertion cost of an eventgroup including the insertion cost of the events
     */
    private double eventGroupInsertionCost(final EventGroup eventGroup) {
        double distance = GraphEditDistance.eventGroupInsertCost;

        final Queue<PayloadAwareEntryCallEvent> queue = new LinkedList<>(eventGroup.getEvents());

        final List<PayloadAwareEntryCallEvent> equalEvents = new ArrayList<>();

        while (!queue.isEmpty()) {
            final PayloadAwareEntryCallEvent event1 = queue.poll();
            // look for equal events, as they can be duplicated, instead of created
            for (final PayloadAwareEntryCallEvent event2 : queue) {
                if (GraphEditDistance.haveSameValues(event1, event2)) {
                    equalEvents.add(event2);

                }
            }
            queue.removeAll(equalEvents);
            distance += equalEvents.size() * GraphEditDistance.WEIGTHING.getDuplicateCost(event1.getParameters());
            distance += GraphEditDistance.WEIGTHING.getInsertCost(event1.getParameters());

        }

        return distance;
    }

    /**
     * checks if two events share the same values
     *
     * @return True, if both events share the exact same values, false otherwise
     */
    private static boolean haveSameValues(final PayloadAwareEntryCallEvent event1,
            final PayloadAwareEntryCallEvent event2) {
        return Arrays.equals(event1.getValues(), event2.getValues());
    }

    /**
     * This can be used, to set the node, edge and eventgroup insertion cost for the Graph Edit
     * Distance Algorithm to the values in the configuration
     *
     * @param configuration
     *            A configuration object. If the costs are not defined, default values are used.
     */
    public static void setConfiguration(final Configuration configuration) {
        GraphEditDistance.nodeInsertCost = configuration.getDoubleProperty(ConfigurationKeys.NODE_INSERTION_COST, 10);

        GraphEditDistance.edgeInsertCost = configuration.getDoubleProperty(ConfigurationKeys.EDGE_INSERTION_COST, 5);

        GraphEditDistance.eventGroupInsertCost = configuration
                .getDoubleProperty(ConfigurationKeys.EVENT_GROUP_INSERTION_COST, 4);

    }
}