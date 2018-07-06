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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

/**
 * @author Reiner Jung
 *
 */
public final class StoreModelFacility extends AbstractModelFacility {

    private long objectId = 0;

    public StoreModelFacility(final GraphDatabaseService graphDatabaseService, final Map<EObject, Node> objectNodeMap) {
        super(graphDatabaseService, objectNodeMap);
    }

    /**
     * Create all references for a model.
     *
     * @param nodes
     *            hashmap of all objects and their corresponding nodes
     */
    public void createAllReferences(final Map<EObject, Node> nodes) {
        for (final Entry<EObject, Node> entry : nodes.entrySet()) {
            final EObject sourceObject = entry.getKey();
            for (final EReference reference : sourceObject.eClass().getEAllReferences()) {
                if (reference.isContainment()) {
                    this.createContainment(sourceObject, entry.getValue(), reference, nodes);
                } else {
                    this.createAssociation(sourceObject, entry.getValue(), reference, nodes);
                }
            }
        }
    }

    /**
     * Add all object attributes to the node as properties.
     *
     * @param node
     *            the node
     * @param storeableObject
     *            the object having attributes
     */
    private void storeAttributes(final Node node, final EObject storeableObject) {
        for (final EAttribute attr : storeableObject.eClass().getEAllAttributes()) {
            final Object value = storeableObject.eGet(attr);
            if (value != null) {
                node.setProperty(attr.getName(), value.toString());
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
    private Node createNode(final EObject storeableObject) {
        final Label typeName = Label.label(storeableObject.eClass().getInstanceTypeName());

        final Node node = this.graphDatabaseService.createNode(typeName);
        node.setProperty(ModelResource.INTERNAL_ID, this.getNextId());

        /** set object uri. */
        this.setNodeObjectUri(node, storeableObject);

        /** attributes. */
        this.storeAttributes(node, storeableObject);

        this.objectNodeMap.put(storeableObject, node);

        return node;
    }

    private Node createProxyNode(final EObject storeableObject) {
        final Label typeName = Label.label(storeableObject.eClass().getInstanceTypeName());

        final Node node = this.graphDatabaseService.createNode(typeName);
        node.setProperty(ModelResource.INTERNAL_ID, this.getNextId());

        this.setNodeObjectUri(node, storeableObject);

        node.setProperty(ModelResource.PROXY_OBJECT, true);

        this.objectNodeMap.put(storeableObject, node);

        return node;
    }

    /**
     * Create an association reference.
     *
     * @param sourceObject
     *            source object
     * @param sourceNode
     *            source node
     * @param reference
     *            reference of the source object to be processed
     * @param nodes
     *            hashmap of all objects and their corresponding nodes
     */
    private void createAssociation(final EObject sourceObject, final Node sourceNode, final EReference reference,
            final Map<EObject, Node> nodes) {
        final Object referencedObject = sourceObject.eGet(reference);
        if (referencedObject instanceof EList<?>) {
            int i = 0;
            for (final Object element : (EList<?>) referencedObject) {
                Node targetNode = nodes.get(element);
                if (targetNode == null) {
                    targetNode = this.createProxyNode((EObject) element);
                    nodes.put((EObject) element, targetNode);
                }
                this.createReference(sourceNode, targetNode, (EObject) element, reference, i++);
            }
        } else if (referencedObject != null) {
            Node targetNode = nodes.get(referencedObject);
            if (targetNode == null) {
                targetNode = this.createProxyNode((EObject) referencedObject);
                nodes.put((EObject) referencedObject, targetNode);
            }
            this.createReference(sourceNode, targetNode, (EObject) referencedObject, reference, 0);
        }
    }

    /**
     * Create a single reference.
     *
     * @param sourceNode
     *            source node
     * @param targetNode
     *            target node
     * @param targetObject
     *            target object
     * @param reference
     *            corresponding reference
     * @param i
     *            position info for lists
     */
    private void createReference(final Node sourceNode, final Node targetNode, final EObject targetObject,
            final EReference reference, final int i) {
        final Relationship relationship = sourceNode.createRelationshipTo(targetNode,
                ModelProviderUtil.getRelationshipType(reference, targetObject));
        relationship.setProperty(ModelProviderUtil.REF_NAME, reference.getName());
        relationship.setProperty(ModelProviderUtil.REF_POS, i);
    }

    /**
     * Create a containment reference.
     *
     * @param sourceObject
     *            source object
     * @param sourceNode
     *            source node
     * @param reference
     *            reference to be processed
     * @param nodes
     *            hashmap of existing nodes
     */
    private void createContainment(final EObject sourceObject, final Node sourceNode, final EReference reference,
            final Map<EObject, Node> nodes) {
        final Object referencedObject = sourceObject.eGet(reference);
        if (referencedObject instanceof EList<?>) {
            int i = 0;
            for (final Object element : (EList<?>) referencedObject) {
                final Node targetNode = nodes.get(element);
                if (targetNode == null) {
                    throw new InternalError("Contained object has no node: " + element.toString());
                }
                this.createReference(sourceNode, targetNode, (EObject) element, reference, i++);
            }
        } else if (referencedObject != null) {
            final Node targetNode = nodes.get(referencedObject);
            if (targetNode == null) {
                throw new InternalError("Contained object has no node: " + referencedObject.toString());
            }
            this.createReference(sourceNode, targetNode, (EObject) referencedObject, reference, 0);
        }
    }

    /**
     * Create the nodes of a partition recursively without the references.
     *
     * @param storeableObject
     *            Object to save
     */
    public void createNodes(final EObject storeableObject) {
        this.createAllReferences(this.createNodes(storeableObject, new ConcurrentHashMap<>()));
    }

    private Map<EObject, Node> createNodes(final EObject storeableObject, final Map<EObject, Node> nodes) {
        final Node node = this.createNode(storeableObject);
        nodes.put(storeableObject, node);

        for (final EReference reference : storeableObject.eClass().getEAllContainments()) {
            final Object referencedObject = storeableObject.eGet(reference);
            if (referencedObject instanceof EList<?>) {
                for (final Object object : (EList<?>) referencedObject) {
                    this.createNodes((EObject) object, nodes);
                }
            } else if (referencedObject != null) {
                this.createNodes((EObject) referencedObject, nodes);
            }
        }

        return nodes;
    }

    private long getNextId() {
        return this.objectId++;
    }

}
