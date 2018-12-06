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

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

/**
 * @author Reiner Jung
 *
 * @param <R>
 *            root class type
 */
public class QueryModelFacility<R extends EObject> extends GenericModelFacility<R> {

    private final Set<EFactory> factories;

    /**
     * Create a query model facility.
     *
     * @param modelResource
     *            parent object reference to the resource
     *
     * @param graphDatabaseService
     *            data base service
     * @param factories
     *            factories of the metamodel or partition
     */
    public QueryModelFacility(final ModelResource<R> modelResource, final GraphDatabaseService graphDatabaseService,
            final Set<EFactory> factories) {
        super(modelResource, graphDatabaseService);
        this.factories = factories;
    }

    /**
     * Read the object of the given and all contained objects.
     *
     * @param node
     *            the object identifying node
     *
     * @param <T>
     *            type of the root node
     *
     * @return the object for the node
     *
     * @throws DBException
     *             when resolving references from relationships fail
     */
    @SuppressWarnings("unchecked")
    public <T extends EObject> T readContainedNodes(final Node node) throws DBException {
        return (T) this.readContainedNodesRecursively(node, new HashMap<Node, EObject>());
    }

    /**
     * Helper method for reading: Starting from a given node this method recursively reads all
     * contained successor nodes and instantiates the correspondent EObjects. Calls to this method
     * have to be performed from inside a {@link Transaction}.
     *
     * @param parentNode
     *            The node to start with
     * @param nodesToCreatedObjects
     *            Initially empty map of nodes to already created objects to make sure that objects
     *            are instantiated just once
     * @param <T>
     *            type definition
     * @return The root
     * @throws DBException
     *             when resolving references from relationships fail
     */
    @SuppressWarnings("unchecked")
    public <T extends EObject> T readContainedNodesRecursively(final Node parentNode,
            final Map<Node, EObject> nodesToCreatedObjects) throws DBException {
        if (!nodesToCreatedObjects.containsKey(parentNode)) {
            final T parentObject = (T) ModelObjectFactory.createObject(parentNode, this.factories);

            // Already register unfinished components because there might be circles
            nodesToCreatedObjects.putIfAbsent(parentNode, parentObject);

            this.scanContainmentReferences(parentNode, nodesToCreatedObjects);
            this.resolveReferences(nodesToCreatedObjects);

            return parentObject;
        } else {
            return (T) nodesToCreatedObjects.get(parentNode);
        }
    }

    /**
     * Load related nodes representing referenced objects.
     *
     * @param sourceNode
     *            the corresponding node to the component
     * @param nodesToCreatedObjects
     *            additional nodes
     * @throws DBException
     *             when a relationship cannot be resolved; indicates broken node model
     */
    private void scanContainmentReferences(final Node sourceNode, final Map<Node, EObject> nodesToCreatedObjects)
            throws DBException {
        for (final Relationship relationship : ModelProviderUtil
                .sortRelsByPosition(sourceNode.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS))) {
            this.scanSingleContainmentReference(relationship, nodesToCreatedObjects);
        }
    }

    private void scanSingleContainmentReference(final Relationship relationship,
            final Map<Node, EObject> nodesToCreatedObjects) throws DBException {
        final Node targetNode = relationship.getEndNode();

        if (!nodesToCreatedObjects.containsKey(targetNode)) {
            if (ModelGraphFactory.isProxyNode(targetNode)) {
                throw new DBException(String.format(
                        "Proxy object in the containment hierarchy is an internal error. Relationship %s and node %s",
                        relationship.getProperty(ModelProviderUtil.REF_NAME),
                        targetNode.getProperty(ModelGraphFactory.EMF_INTERNAL_ID)));
            } else {
                final EObject targetObject = ModelObjectFactory.createObject(targetNode, this.factories);
                this.scanContainmentReferences(targetNode, nodesToCreatedObjects);
                nodesToCreatedObjects.put(targetNode, targetObject);
            }

        }
    }

    /**
     * Resolve all references for all objects stored in the map.
     *
     * @param nodeObjectMap
     *            node object map
     * @throws DBException
     *             when there is a relationship which is not associated to an EReference
     */
    @SuppressWarnings("unchecked")
    public void resolveReferences(final Map<Node, EObject> nodeObjectMap) throws DBException {
        for (final Entry<Node, EObject> entry : nodeObjectMap.entrySet()) {
            final EObject sourceObject = entry.getValue();
            final Node sourceNode = entry.getKey();

            for (final Relationship relationship : ModelProviderUtil.sortRelsByPosition(sourceNode.getRelationships(
                    Direction.OUTGOING, EMFRelationshipType.CONTAINS, EMFRelationshipType.REFERENCES))) {
                final EReference reference = ModelGraphFactory.findReference(sourceObject, relationship);
                final Node targetNode = relationship.getEndNode();
                EObject targetObject = nodeObjectMap.get(targetNode);

                if (targetObject == null) {
                    /** object is either from another model or another partition in this model. */
                    if ((Boolean) targetNode.getProperty(ModelGraphFactory.PROXY_OBJECT)) {
                        targetObject = ModelObjectFactory.createProxyObject(targetNode, this.factories);
                    } else {
                        targetObject = ModelObjectFactory.createObject(targetNode, this.factories);
                    }
                }

                if (reference.isChangeable()) {
                    if (reference.isMany()) {
                        ((EList<EObject>) sourceObject.eGet(reference)).add(targetObject);
                    } else {
                        sourceObject.eSet(reference, targetObject);
                    }
                }
            }

        }
    }

    /**
     * Helper method for reading: Recursively returns all containments and data types of a given
     * node.
     *
     * @param node
     *            The node to start with
     * @param nodes
     *            Initially empty set of all containments and data types
     * @return The passed containmentsAndDatatypes set now filled with containments and data types
     */
    public Set<Node> getAllContainmentNodes(final Node node, final Set<Node> nodes) {
        if (!nodes.contains(node)) {
            nodes.add(node);

            for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS)) {
                this.getAllContainmentNodes(rel.getEndNode(), nodes);
            }
        }

        return nodes;
    }

}
