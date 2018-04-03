/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.clustering.filter.similaritymatching;

import org.iobserve.analysis.clustering.behaviormodels.BehaviorModel;
import org.iobserve.analysis.clustering.behaviormodels.EntryCallEdge;
import org.iobserve.analysis.clustering.behaviormodels.EntryCallNode;

/**
 * Structure distance function for behavior models
 *
 * @author Jannis Kuckei
 *
 */
public class GeneralStructureMetric implements IStructureMetricStrategy {
    @Override
    public double getDistance(final BehaviorModel a, final BehaviorModel b) {
        /** Calculate node distance */

        // Get amount of shared nodes
        double sharedNodes = 0;
        for (final EntryCallNode nodeA : a.getNodes()) {
            for (final EntryCallNode nodeB : b.getNodes()) {
                if (nodeA.equals(nodeB)) {
                    sharedNodes++;
                    break; // There cannot be multiple nodes with the same signature in a model
                }
            }
        }
        final double nodesInA = a.getNodes().length;
        final double nodesInB = b.getNodes().length;
        final double nodeDistance = ((nodesInA + nodesInB) - sharedNodes) / (nodesInA + nodesInB);

        /** Calculate edge distance */
        // Get amount of shared egdges, count an edge with edge number n as n individual
        // edges
        double edgesInA = 0;
        double edgesInB = 0;
        double sharedEdges = 0;
        for (final EntryCallEdge edgeA : a.getEdges()) {
            edgesInA += edgeA.getCalls();
            for (final EntryCallEdge edgeB : b.getEdges()) {
                edgesInB += edgeB.getCalls();
                if (edgeA.equals(edgeB)) {
                    sharedEdges += Math.min(edgeA.getCalls(), edgeB.getCalls());
                    break; // There cannot be multiple edge instances with the same source and target nodes
                           // in a model
                }
            }
        }

        final double edgeDistance = ((edgesInA + edgesInB) - sharedEdges) / (edgesInA + edgesInB);

        // Return average of edge/node distance
        return (edgeDistance + nodeDistance) / 2;
    }
}
