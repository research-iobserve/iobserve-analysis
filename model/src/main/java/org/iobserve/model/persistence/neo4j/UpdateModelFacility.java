/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class UpdateModelFacility extends AbstractModelFacility {

    public UpdateModelFacility(final ModelResource resource, final GraphDatabaseService graphDatabaseService,
            final Map<EObject, Node> objectNodeMap) {
        super(resource, graphDatabaseService, objectNodeMap);
    }

    /**
     * Update the given object and all contained objects. In case of new contained objects have been
     * added, they are added. in case an object is missing, it will be removed from the database.
     *
     * @param object
     *            the object to be updated
     */
    public <T extends EObject> void updatePartition(final T object) {
        final Map<EObject, Node> updatedObjectNodesMap = this.updateNodes(object, new ConcurrentHashMap<>());
        this.updateAllReferences(updatedObjectNodesMap);

        /** remove objects from the objectNodeMap which do no longer exist in the model. */
        final List<EObject> objectList = new ArrayList<>(this.objectNodeMap.keySet());
        for (int i = 0; i < objectList.size(); i++) {
            final EObject oldObject = objectList.get(i);
            if (!updatedObjectNodesMap.containsKey(oldObject)) {
                this.deleteNode(this.objectNodeMap.get(oldObject));
                i--;
            }
        }

        /** add new objects to the map. */
        for (final Entry<EObject, Node> updatedEntry : updatedObjectNodesMap.entrySet()) {
            if (!this.objectNodeMap.containsKey(updatedEntry.getKey())) {
                this.objectNodeMap.put(updatedEntry.getKey(), updatedEntry.getValue());
            }
        }
    }

    /**
     * Delete a node an all its relationships.
     *
     * @param node
     *            the node to be deleted
     */
    private void deleteNode(final Node node) {
        for (final Relationship relationship : node.getRelationships()) {
            relationship.delete();
        }
        node.delete();
    }

    private <T extends EObject> Map<EObject, Node> updateNodes(final T storeableObject,
            final Map<EObject, Node> updatedNodes) {
        final Node node = this.updateNode(storeableObject);
        updatedNodes.put(storeableObject, node);

        for (final EReference reference : storeableObject.eClass().getEAllContainments()) {
            final Object referencedObject = storeableObject.eGet(reference);
            if (referencedObject instanceof EList<?>) {
                for (final Object object : (EList<?>) referencedObject) {
                    this.updateNodes((EObject) object, updatedNodes);
                }
            } else if (referencedObject != null) {
                this.updateNodes((EObject) referencedObject, updatedNodes);
            }
        }

        return updatedNodes;
    }

    private void updateAllReferences(final Map<EObject, Node> updateNodes) {
        for (final Entry<EObject, Node> entry : updateNodes.entrySet()) {
            final EObject sourceObject = entry.getKey();
            for (final EReference reference : sourceObject.eClass().getEAllReferences()) {
                if (reference.isContainment()) {
                    this.updateContainment(sourceObject, entry.getValue(), reference, updateNodes);
                } else {
                    this.updateAssociation(sourceObject, entry.getValue(), reference, updateNodes);
                }
            }
        }
    }

    private void updateAssociation(final EObject sourceObject, final Node sourceNode, final EReference reference,
            final Map<EObject, Node> nodes) {
        final Object referencedObject = sourceObject.eGet(reference);
        if (referencedObject instanceof EList<?>) {
            int i = 0;
            for (final Object element : (EList<?>) referencedObject) {
                Node targetNode = nodes.get(element);
                if (targetNode == null) {
                    targetNode = ModelGraphFactory.createProxyNode(this.graphDatabaseService, (EObject) element,
                            this.resource.getNextId());
                    nodes.put((EObject) element, targetNode);
                }
                ModelGraphFactory.createRelationship(sourceNode, targetNode, (EObject) element, reference, i++);
            }
        } else if (referencedObject != null) {
            Node targetNode = nodes.get(referencedObject);
            if (targetNode == null) {
                targetNode = ModelGraphFactory.createProxyNode(this.graphDatabaseService, (EObject) referencedObject,
                        this.resource.getNextId());
                nodes.put((EObject) referencedObject, targetNode);
            }
            ModelGraphFactory.createRelationship(sourceNode, targetNode, (EObject) referencedObject, reference, 0);
        }
    }

    /**
     * Update a containment reference.
     *
     * @param sourceObject
     *            source object
     * @param sourceNode
     *            source node
     * @param reference
     *            reference (type)
     * @param updatedNodes
     *            map of all updated nodes
     */
    private void updateContainment(final EObject sourceObject, final Node sourceNode, final EReference reference,
            final Map<EObject, Node> updatedNodes) {
        final Object referencedObject = sourceObject.eGet(reference);
        if (referencedObject instanceof EList<?>) {
            int i = 0;
            for (final Object element : (EList<?>) referencedObject) {
                final Node targetNode = updatedNodes.get(element);
                if (targetNode == null) {
                    throw new InternalError("Contained object has no node: " + element.toString());
                }
                final Iterable<Relationship> relationships = sourceNode.getRelationships(Direction.OUTGOING,
                        EMFRelationshipType.CONTAINS);
                final Relationship relationship = ModelGraphFactory.findRelationshipByEndNode(relationships,
                        targetNode);
                if (relationship == null) {
                    ModelGraphFactory.createRelationship(sourceNode, targetNode, (EObject) element, reference, i++);
                } else {
                    ModelGraphFactory.updateRelationship(relationship, i++);
                }
            }
        } else if (referencedObject != null) {
            final Node targetNode = updatedNodes.get(referencedObject);
            if (targetNode == null) {
                throw new InternalError("Contained object has no node: " + referencedObject.toString());
            }
            final Iterable<Relationship> relationships = sourceNode.getRelationships(Direction.OUTGOING,
                    EMFRelationshipType.CONTAINS);
            final Relationship relationship = ModelGraphFactory.findRelationshipByEndNode(relationships, targetNode);
            if (relationship == null) {
                ModelGraphFactory.createRelationship(sourceNode, targetNode, (EObject) referencedObject, reference, 0);
            } else {
                ModelGraphFactory.updateRelationship(relationship, 0);
            }
        }
    }

    /**
     * Create a single node without resolved references.
     *
     * @param storeableObject
     *            object to be mapped to a node
     *
     * @return returns the created node.
     */
    private <T extends EObject> Node updateNode(final T storeableObject) {
        final Node existingNode = this.objectNodeMap.get(storeableObject);
        if (existingNode != null) {
            this.updateAttributes(storeableObject, existingNode);
            return existingNode;
        } else {
            return ModelGraphFactory.createNode(this.graphDatabaseService, storeableObject, this.resource.getNextId());
        }
    }

    private void updateAttributes(final EObject object, final Node node) {
        for (final EAttribute attr : object.eClass().getEAllAttributes()) {
            final String key = attr.getName();
            final Object value = object.eGet(attr);
            if (value != null) {
                node.setProperty(key, value.toString());
            } else {
                node.removeProperty(key);
            }
        }
    }

}
