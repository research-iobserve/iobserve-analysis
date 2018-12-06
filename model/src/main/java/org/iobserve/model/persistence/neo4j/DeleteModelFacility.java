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

import org.eclipse.emf.ecore.EObject;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * @author Reiner Jung
 *
 * @param <R>
 *            root class type
 *
 */
public class DeleteModelFacility<R extends EObject> extends GenericModelFacility<R> {

    /**
     * Create a delete model facility.
     *
     * @param modelResource
     *            the resource
     * @param graphDatabaseService
     *            the database
     */
    public DeleteModelFacility(final ModelResource<R> modelResource, final GraphDatabaseService graphDatabaseService) {
        super(modelResource, graphDatabaseService);
    }

    /**
     * Delete an object specified by type and node id.
     *
     * @param object
     *            object
     * @throws DBException
     *             on db errors
     */
    public void deleteObjectRecursively(final EObject object) throws DBException {
        final Node node = ModelGraphFactory.findNode(this.graphDatabaseService, object);
        if (node != null) {
            this.deleteObjectNodesRecursively(node);
        }
    }

    /**
     * Delete nodes for objects recursiveley.
     *
     * @param rootNode
     *            root node
     */
    public void deleteObjectNodesRecursively(final Node rootNode) {
        for (final Relationship relationship : rootNode.getRelationships(Direction.OUTGOING,
                EMFRelationshipType.CONTAINS)) {
            final Node targetNode = relationship.getEndNode();
            this.deleteObjectNodesRecursively(targetNode);
        }
    }

}
