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
package org.iobserve.model.persistence.neo4j;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;

/**
 * @author Reiner Jung
 *
 */
public class DeleteModelFacility extends AbstractModelFacility {

    private static final String ACCESSIBLE = "accessible";
    private static final String DELETE = "delete";
    private static final String VISITED = "visited";

    public DeleteModelFacility(final GraphDatabaseService graphDatabaseService,
            final Map<EObject, Node> objectNodeMap) {
        super(graphDatabaseService, objectNodeMap);
    }

    /**
     * Helper method for deleting: Combines {@link #markAccessibleNodes(Node)},
     * {@link #markDeletableNodes(Node, boolean)} and {@link #deleteMarkedNodes(Node)} in one
     * method.
     *
     * @param node
     *            The node to start with
     * @param forceDelete
     *            Force method to delete the specified node even if it is referenced with an is_type
     *            or contains edge
     */
    public void deleteComponentAndDatatypeNodes(final Node node, final boolean forceDelete) {
        this.markAccessibleNodes(node);
        this.markDeletableNodes(node, true, forceDelete);
        this.deleteMarkedNodes(node);
        this.deleteNonReferencedNodes();
    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively marks all
     * nodes accessible via {@link EMFRelationshipType#CONTAINS} or
     * {@link EMFRelationshipType#IS_TYPE} edges. Calls to this method have to be performed from
     * inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     */
    private void markAccessibleNodes(final Node node) {
        node.setProperty(DeleteModelFacility.DELETE, true);

        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                EMFRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(DeleteModelFacility.ACCESSIBLE)) {
                rel.setProperty(DeleteModelFacility.ACCESSIBLE, true);
                this.markAccessibleNodes(rel.getEndNode());
            }
        }
    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively marks all
     * accessible nodes marked with {@link #markAccessibleNodes(Node)} which can be deleted.
     * Starting from one node all contained nodes can be deleted as well. Nodes with incoming
     * {@link EMFRelationshipType#IS_TYPE} edges may only be deleted if they are not referenced from
     * outside the accessible nodes and have no predecessor which is referenced from outside the
     * accessible nodes via an {@link EMFRelationshipType#IS_TYPE} edge. Calls to this method have
     * to be performed from inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     * @param reallyDeletePred
     *            Flag if predecessor may be deleted
     */
    private void markDeletableNodes(final Node node, final boolean reallyDeletePred, final boolean forceDelete) {
        boolean reallyDelete = reallyDeletePred;

        // Check if there are incoming IS_TYPE relations from outside
        for (final Relationship rel : node.getRelationships(Direction.INCOMING, EMFRelationshipType.CONTAINS,
                EMFRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(DeleteModelFacility.ACCESSIBLE) && !forceDelete) {
                reallyDelete = false;
            }
        }

        // Remove delete property if node must not be deleted
        if (node.hasProperty(DeleteModelFacility.DELETE) && !reallyDelete) {
            node.removeProperty(DeleteModelFacility.DELETE);
        }

        // Recursively check successors and mark already visited edges to prevent call circles
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                EMFRelationshipType.IS_TYPE)) {
            if (!rel.hasProperty(DeleteModelFacility.VISITED)) {
                rel.setProperty(DeleteModelFacility.VISITED, true);
                this.markDeletableNodes(rel.getEndNode(), reallyDelete, false);
            }
        }

        // Remove edge marks when returned from successor node's calls
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                EMFRelationshipType.IS_TYPE)) {
            rel.removeProperty(DeleteModelFacility.VISITED);
        }
    }

    /**
     * Helper method for deleting: Starting from a given node this method recursively traverses down
     * through all accessible nodes marked with {@link #markAccessibleNodes(Node)} and then deletes
     * all nodes marked with a delete flag by {@link #markDeletableNodes(Node)} from bottom to the
     * top. Calls to this method have to be performed from inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     */
    private void deleteMarkedNodes(final Node node) {
        // Recursively go to the lowest node and mark already visited edges to prevent call circles
        for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                EMFRelationshipType.IS_TYPE)) {

            try {
                if (!rel.hasProperty(DeleteModelFacility.VISITED)) {
                    rel.setProperty(DeleteModelFacility.VISITED, true);
                    this.deleteMarkedNodes(rel.getEndNode());
                }
            } catch (final NotFoundException e) {
                // relation has already been deleted on another path
                e.printStackTrace();
            }
        }

        try {
            if (node.hasProperty(DeleteModelFacility.DELETE)) {
                // Delete node and its relationships
                for (final Relationship rel : node.getRelationships()) {
                    rel.delete();
                }
                node.delete();

            } else {
                // Only remove visited mark
                for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                        EMFRelationshipType.IS_TYPE)) {
                    rel.removeProperty(DeleteModelFacility.VISITED);
                }
            }
        } catch (final NotFoundException e) {
            // node has already been deleted on another path
            e.printStackTrace();
        }
    }

    /**
     * This method acts as a kind of garbage collector and deletes nodes from the graph which are
     * not connected to any other node.
     */
    private void deleteNonReferencedNodes() {
        final ResourceIterator<Node> nodesIter = this.graphDatabaseService.getAllNodes().iterator();
        while (nodesIter.hasNext()) {
            final Node node = nodesIter.next();

            if (!node.getRelationships().iterator().hasNext()) {
                node.delete();
            }
        }
    }
}
