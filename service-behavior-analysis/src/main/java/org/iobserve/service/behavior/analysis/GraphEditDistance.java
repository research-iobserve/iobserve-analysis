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
package org.iobserve.service.behavior.analysis;

import java.util.Map;

import org.iobserve.service.behavior.analysis.model.BehaviorModelEdge;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;

import mtree.DistanceFunction;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class GraphEditDistance implements DistanceFunction<BehaviorModelGED> {

    private static final double NODE_INSERT_COST = 5;
    private static final double NODE_DELETE_COST = GraphEditDistance.NODE_INSERT_COST;

    private static final double EDGE_INSERT_COST = 1;
    private static final double EDGE_DELETE_COST = GraphEditDistance.EDGE_INSERT_COST;

    // costs to convert model1 into model2
    @Override
    public double calculate(final BehaviorModelGED model1, final BehaviorModelGED model2) {

        double distance = 0;

        // check if nodes from model1 are in model2
        for (final Map.Entry<String, BehaviorModelNode> pair : model1.getNodes().entrySet()) {
            final String signature = pair.getKey();
            final BehaviorModelNode node = pair.getValue();

            final BehaviorModelNode match = model2.getNodes().get(signature);

            if (match == null) {
                distance += this.nodeInsertionCost(node);
            }

            else {
                distance += this.nodeDistance(node, match);
            }
        }

        // check if nodes from model2 are in model1
        for (final Map.Entry<String, BehaviorModelNode> pair : model2.getNodes().entrySet()) {
            final String signature = pair.getKey();
            final BehaviorModelNode node = pair.getValue();

            final BehaviorModelNode match = model1.getNodes().get(signature);

            if (match == null) {
                distance += this.nodeDeletionCost(node);
            }
        }

        return distance;
    }

    private double nodeDistance(final BehaviorModelNode node1, final BehaviorModelNode node2) {
        final double distance = 0;

        return distance;
    }

    private double edgeDistance(final BehaviorModelEdge edge1, final BehaviorModelEdge edge2) {
        final double distance = 0;

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
        return GraphEditDistance.EDGE_INSERT_COST;
    }

    private double nodeDeletionCost(final BehaviorModelNode node) {
        double distance = GraphEditDistance.NODE_DELETE_COST;

        for (final BehaviorModelEdge edge : node.getOutgoingEdges().values()) {
            distance += this.edgeDeletionCost(edge);
        }

        return distance;
    }

    private double edgeDeletionCost(final BehaviorModelEdge edge) {
        return GraphEditDistance.EDGE_DELETE_COST;
    }

}