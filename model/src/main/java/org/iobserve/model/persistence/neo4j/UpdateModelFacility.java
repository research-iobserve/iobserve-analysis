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

/**
 *
 * @author Reiner Jung
 *
 * @param <R>
 *            root class type
 */
public class UpdateModelFacility<R extends EObject> extends GenericModelFacility<R> {

    /**
     * Create an update model facility.
     *
     * @param resource
     *            the corresponding resource
     * @param graphDatabaseService
     *            the database service
     * @param objectNodeMap
     *            the internal object map
     */
    public UpdateModelFacility(final ModelResource<R> resource, final GraphDatabaseService graphDatabaseService,
            final Map<EObject, Node> objectNodeMap) {
        super(resource, graphDatabaseService, objectNodeMap);
    }

    /**
     * Update the given object and all contained objects. In case of new contained objects have been
     * added, they are added. in case an object is missing, it will be removed from the database.
     *
     * @param object
     *            the object to be updated
     * @param <T>
     *            the objects' type
     * @throws NodeLookupException
     *             on node lookup errors
     */
    public <T extends EObject> void updatePartition(final T object) throws NodeLookupException {
        final Map<EObject, Node> updatedObjectNodesMap = new ConcurrentHashMap<>();
        this.updateNodes(object, updatedObjectNodesMap);
        this.updateAllReferences(updatedObjectNodesMap);

        /** remove objects from the objectNodeMap which do no longer exist in the model. */
        final List<EObject> objectList = new ArrayList<>(this.objectNodeMap.keySet());
        for (int i = 0; i < objectList.size(); i++) {
            final EObject oldObject = objectList.get(i);
            if (!updatedObjectNodesMap.containsKey(oldObject)) {
                this.deleteNode(this.objectNodeMap.get(oldObject));
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

    private <T extends EObject> void updateNodes(final T storeableObject,
            final Map<EObject, Node> updatedObjectNodeMap) {
        final Node node = this.updateNode(storeableObject);
        updatedObjectNodeMap.put(storeableObject, node);

        /** check contained objects. */
        for (final EReference reference : storeableObject.eClass().getEAllContainments()) {
            final Object referencedObject = storeableObject.eGet(reference);
            if (reference.isMany()) {
                for (final Object object : (EList<?>) referencedObject) {
                    this.updateNodes((EObject) object, updatedObjectNodeMap);
                }
            } else {
                if (referencedObject != null) {
                    this.updateNodes((EObject) referencedObject, updatedObjectNodeMap);
                }
            }
        }

        this.checkAllReferencedObject(storeableObject, updatedObjectNodeMap);
    }

    /**
     * check all referenced objects.
     */
    private <T extends EObject> void checkAllReferencedObject(final T storeableObject,
            final Map<EObject, Node> updatedObjectNodeMap) {
        for (final EReference reference : storeableObject.eClass().getEAllReferences()) {
            if (!reference.isContainment()) {
                final Object referencedObject = storeableObject.eGet(reference);
                if (reference.isMany()) {
                    for (final Object object : (EList<?>) referencedObject) {
                        if (updatedObjectNodeMap.get(object) == null) {
                            this.updateAssociation(updatedObjectNodeMap, (EObject) object);
                        }
                    }
                } else {
                    if (referencedObject != null) {
                        this.updateAssociation(updatedObjectNodeMap, (EObject) referencedObject);
                    }
                }
            }
        }
    }

    private void updateAssociation(final Map<EObject, Node> updatedObjectNodeMap, final EObject referencedObject) {
        if (updatedObjectNodeMap.get(referencedObject) == null) {
            final Node node = this.objectNodeMap.get(referencedObject);
            if (node != null) {
                updatedObjectNodeMap.put(referencedObject, node);
            }
        }
    }

    private void updateAllReferences(final Map<EObject, Node> updatedObjectNodeMap) throws NodeLookupException {
        for (final Entry<EObject, Node> entry : updatedObjectNodeMap.entrySet()) {
            final EObject sourceObject = entry.getKey();
            if (!ModelGraphFactory.isProxyNode(entry.getValue())) {
                for (final EReference reference : sourceObject.eClass().getEAllReferences()) {
                    this.updateReference(sourceObject, entry.getValue(), reference, updatedObjectNodeMap);
                }
            }
        }
    }

    /**
     * Update associations.
     *
     * @param sourceObject
     *            source object which references another object
     * @param sourceNode
     *            the node of the source object
     * @param reference
     *            the reference
     * @param updatedObjectNodeMap
     *            all existing updated nodes
     * @throws NodeLookupException
     */
    private void updateReference(final EObject sourceObject, final Node sourceNode, final EReference reference,
            final Map<EObject, Node> updatedObjectNodeMap) throws NodeLookupException {
        final Object referencedObject = sourceObject.eGet(reference);
        if (reference.isMany()) {
            int i = 0;
            for (final Object element : (EList<?>) referencedObject) {
                this.updateOrCreateReferenceObject(updatedObjectNodeMap, sourceNode, reference, (EObject) element, i++);
            }
        } else {
            if (referencedObject != null) {
                this.updateOrCreateReferenceObject(updatedObjectNodeMap, sourceNode, reference,
                        (EObject) referencedObject, 0);
            }
        }
    }

    private void updateOrCreateReferenceObject(final Map<EObject, Node> updatedObjectNodeMap, final Node sourceNode,
            final EReference reference, final EObject targetObject, final int index) {
        Node targetNode = updatedObjectNodeMap.get(targetObject);
        if (targetNode == null) {
            targetNode = this.objectNodeMap.get(targetObject);
            if (targetNode == null) {
                targetNode = ModelGraphFactory.createProxyNode(this.graphDatabaseService, targetObject,
                        this.resource.getNextId());
            }
        }
        this.updateOrCreateRelationship(sourceNode, targetNode, reference, targetObject, index);
    }

    private void updateOrCreateRelationship(final Node sourceNode, final Node targetNode, final EReference reference,
            final EObject targetObject, final int position) {
        if (reference.isContainment()) {
            final Iterable<Relationship> relationships = sourceNode.getRelationships(Direction.OUTGOING,
                    EMFRelationshipType.CONTAINS);
            final Relationship relationship = ModelGraphFactory.findRelationshipByEndNode(relationships, targetNode);
            if (relationship == null) {
                ModelGraphFactory.createRelationship(sourceNode, targetNode, targetObject, reference, position);
            } else {
                ModelGraphFactory.updateRelationship(relationship, position);
            }
        } else {
            final Iterable<Relationship> relationships = sourceNode.getRelationships(Direction.OUTGOING,
                    EMFRelationshipType.REFERENCES);
            final Relationship relationship = ModelGraphFactory.findRelationshipByEndNode(relationships, targetNode);
            if (relationship == null) {
                ModelGraphFactory.createRelationship(sourceNode, targetNode, targetObject, reference, position);
            } else {
                ModelGraphFactory.updateRelationship(relationship, position);
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
            if (!ModelGraphFactory.isProxyNode(existingNode)) {
                this.updateAttributes(storeableObject, existingNode);
            }
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
