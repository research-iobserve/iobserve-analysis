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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

/**
 * @author Reiner Jung
 *
 */
public final class StoreModelFacility extends AbstractModelFacility {

    public StoreModelFacility(final ModelResource resource, final GraphDatabaseService graphDatabaseService,
            final Map<EObject, Node> objectNodeMap) {
        super(resource, graphDatabaseService, objectNodeMap);
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
                ModelGraphFactory.createRelationship(sourceNode, targetNode, (EObject) element, reference, i++);
            }
        } else if (referencedObject != null) {
            final Node targetNode = nodes.get(referencedObject);
            if (targetNode == null) {
                throw new InternalError("Contained object has no node: " + referencedObject.toString());
            }
            ModelGraphFactory.createRelationship(sourceNode, targetNode, (EObject) referencedObject, reference, 0);
        }
    }

    /**
     * Create the nodes of a partition recursively without the references.
     *
     * @param storeableObject
     *            Object to save
     */
    public void createNodes(final EObject storeableObject) {
        this.createAllReferences(this.createNodes(storeableObject, this.objectNodeMap));
    }

    private Map<EObject, Node> createNodes(final EObject storeableObject, final Map<EObject, Node> nodes) {
        final Node node = ModelGraphFactory.createNode(this.graphDatabaseService, storeableObject,
                this.resource.getNextId());
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

}
