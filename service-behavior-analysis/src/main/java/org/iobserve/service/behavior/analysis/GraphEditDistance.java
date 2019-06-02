package org.iobserve.service.behavior.analysis;

import java.util.Map;

import org.iobserve.service.behavior.analysis.model.BehaviorModelEdge;
import org.iobserve.service.behavior.analysis.model.BehaviorModelGED;
import org.iobserve.service.behavior.analysis.model.BehaviorModelNode;

import mtree.DistanceFunction;

public class GraphEditDistance implements DistanceFunction<BehaviorModelGED> {

    final double NODE_INSERT_COST = 5;
    final double NODE_DELETE_COST = this.NODE_INSERT_COST;

    final double EDGE_INSERT_COST = 1;
    final double EDGE_DELETE_COST = this.EDGE_INSERT_COST;

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
        double distance = this.NODE_INSERT_COST;

        for (final BehaviorModelEdge edge : node.getEdges().values()) {
            distance += this.edgeInsertionCost(edge);
        }

        return distance;
    }

    private double edgeInsertionCost(final BehaviorModelEdge edge) {
        return this.EDGE_INSERT_COST;
    }

    private double nodeDeletionCost(final BehaviorModelNode node) {
        double distance = this.NODE_DELETE_COST;

        for (final BehaviorModelEdge edge : node.getEdges().values()) {
            distance += this.edgeDeletionCost(edge);
        }

        return distance;
    }

    private double edgeDeletionCost(final BehaviorModelEdge edge) {
        return this.EDGE_DELETE_COST;
    }

}