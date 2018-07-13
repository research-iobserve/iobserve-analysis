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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

/**
 * @author Reiner Jung
 *
 */
public final class StoreModelFacility extends GenericModelFacility {

    /**
     * Create store model facility for the given resource.
     *
     * @param resource
     *            the resource
     * @param graphDatabaseService
     *            the database
     * @param objectNodeMap
     *            the internal object node map
     */
    public StoreModelFacility(final ModelResource resource, final GraphDatabaseService graphDatabaseService,
            final Map<EObject, Node> objectNodeMap) {
        super(resource, graphDatabaseService, objectNodeMap);
    }

    /**
     * Create the nodes of a partition recursively without the references.
     *
     * @param storeableObject
     *            Object to save
     */
    public void storeAllNodesAndReferences(final EObject storeableObject) {
        this.storeNodesRecursively(storeableObject, this.objectNodeMap);
        this.createAllReferences(this.objectNodeMap);
    }

    /**
     * Create all references for a model.
     *
     * @param objectNodeMap
     *            hashmap of all objects and their corresponding nodes
     */
    private void createAllReferences(final Map<EObject, Node> objectNodeMap) {
        final List<EObject> objectList = new ArrayList<>(objectNodeMap.keySet());
        for (final EObject sourceObject : objectList) {
            if (!sourceObject.eIsProxy()) {
                final Node sourceNode = objectNodeMap.get(sourceObject);
                for (final EReference reference : sourceObject.eClass().getEAllReferences()) {
                    if (reference.isContainment()) {
                        this.createContainment(sourceObject, sourceNode, reference, objectNodeMap);
                    } else {
                        this.createAssociation(sourceObject, sourceNode, reference, objectNodeMap);
                    }
                }
            }
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
     * @param objectNodeMap
     *            hashmap of existing nodes
     */
    private void createContainment(final EObject sourceObject, final Node sourceNode, final EReference reference,
            final Map<EObject, Node> objectNodeMap) {
        final Object referencedObject = sourceObject.eGet(reference);
        if (reference.isMany()) {
            int i = 0;
            for (final Object element : (EList<?>) referencedObject) {
                final Node targetNode = objectNodeMap.get(element);
                if (targetNode == null) {
                    throw new InternalError("Contained object has no node: " + element.toString());
                }
                ModelGraphFactory.createRelationship(sourceNode, targetNode, (EObject) element, reference, i++);
            }
        } else if (referencedObject != null) {
            final Node targetNode = objectNodeMap.get(referencedObject);
            if (targetNode == null) {
                throw new InternalError("Contained object has no node: " + referencedObject.toString());
            }
            ModelGraphFactory.createRelationship(sourceNode, targetNode, (EObject) referencedObject, reference, 0);
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
     * @param objectNodeMap
     *            hashmap of all objects and their corresponding nodes
     */
    private void createAssociation(final EObject sourceObject, final Node sourceNode, final EReference reference,
            final Map<EObject, Node> objectNodeMap) {
        final Object referencedObject = sourceObject.eGet(reference);
        if (reference.isMany()) {
            int i = 0;
            for (final Object element : (EList<?>) referencedObject) {
                Node targetNode = objectNodeMap.get(element);
                if (targetNode == null) {
                    targetNode = ModelGraphFactory.createProxyNode(this.graphDatabaseService, (EObject) element,
                            this.resource.getNextId());
                    objectNodeMap.put((EObject) element, targetNode);
                }
                ModelGraphFactory.createRelationship(sourceNode, targetNode, (EObject) element, reference, i++);
            }
        } else if (referencedObject != null) {
            Node targetNode = objectNodeMap.get(referencedObject);
            if (targetNode == null) {
                targetNode = ModelGraphFactory.createProxyNode(this.graphDatabaseService, (EObject) referencedObject,
                        this.resource.getNextId());
                objectNodeMap.put((EObject) referencedObject, targetNode);
            }
            ModelGraphFactory.createRelationship(sourceNode, targetNode, (EObject) referencedObject, reference, 0);
        }
    }

    /**
     * Recurse over the containment tree, create nodes and add object node map entries.
     *
     * @param storeableObject
     *            object to store
     * @param objectNodeMap
     *            object to node map
     *
     * @return the object to node map
     */
    private Map<EObject, Node> storeNodesRecursively(final EObject storeableObject,
            final Map<EObject, Node> objectNodeMap) {
        final Node node = ModelGraphFactory.createNode(this.graphDatabaseService, storeableObject,
                this.resource.getNextId());
        objectNodeMap.put(storeableObject, node);

        for (final EReference reference : storeableObject.eClass().getEAllContainments()) {
            final Object referencedObject = storeableObject.eGet(reference);
            if (reference.isMany()) {
                for (final Object object : (EList<?>) referencedObject) {
                    this.storeNodesRecursively((EObject) object, objectNodeMap);
                }
            } else if (referencedObject != null) {
                this.storeNodesRecursively((EObject) referencedObject, objectNodeMap);
            }
        }

        return objectNodeMap;
    }

}
