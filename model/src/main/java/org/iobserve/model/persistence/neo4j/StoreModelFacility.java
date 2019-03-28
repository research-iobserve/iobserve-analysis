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

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.iobserve.model.persistence.DBException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

/**
 * @author Reiner Jung
 *
 * @param <R>
 *            root class type
 */
public final class StoreModelFacility<R extends EObject> extends GenericModelFacility<R> {

    /**
     * Create store model facility for the given resource.
     *
     * @param resource
     *            the resource
     * @param graphDatabaseService
     *            the database
     */
    public StoreModelFacility(final Neo4JModelResource<R> resource, final GraphDatabaseService graphDatabaseService) {
        super(resource, graphDatabaseService);
    }

    /**
     * Create the nodes of a partition recursively without the references.
     *
     * @param storeableObject
     *            Object to save
     * @throws DBException
     *             on db errors
     */
    public void storeAllNodesAndReferences(final EObject storeableObject) throws DBException {
        this.createAllReferences(this.storeNodesRecursively(storeableObject, new ArrayList<EObject>()));
    }

    /**
     * Create all references for a model.
     *
     * @param objectList
     *            list of objects
     * @throws DBException
     *             on db errors
     */
    private void createAllReferences(final List<EObject> objectList) throws DBException {
        for (final EObject sourceObject : objectList) {
            if (!sourceObject.eIsProxy()) {
                for (final EReference reference : sourceObject.eClass().getEAllReferences()) {
                    if (reference.isContainment()) {
                        this.createContainment(sourceObject, reference);
                    } else {
                        this.createAssociation(sourceObject, reference);
                    }
                }
            }
        }
    }

    /**
     * Create a containment reference.
     *
     * @param source
     *            source object
     * @param reference
     *            reference to be processed
     * @throws DBException
     *             on db errors
     */
    @SuppressWarnings("unchecked")
    private void createContainment(final EObject source, final EReference reference) throws DBException {
        final Object referencedObject = source.eGet(reference);
        if (reference.isMany()) {
            int i = 0;
            for (final EObject target : (EList<EObject>) referencedObject) {
                ModelGraphFactory.createRelationship(this.graphDatabaseService, source, target, reference, i++);
            }
        } else if (referencedObject != null) {
            ModelGraphFactory.createRelationship(this.graphDatabaseService, source, (EObject) referencedObject,
                    reference, 0);
        }
    }

    /**
     * Create an association reference.
     *
     * @param source
     *            source object
     * @param reference
     *            reference of the source object to be processed
     * @throws DBException
     *             on db errors
     */
    @SuppressWarnings("unchecked")
    private void createAssociation(final EObject source, final EReference reference) throws DBException {
        final Object referencedObject = source.eGet(reference);
        if (reference.isMany()) {
            int i = 0;
            for (final EObject target : (EList<EObject>) referencedObject) {
                this.createAssociationReference(source, target, reference, i++);
            }
        } else if (referencedObject != null) {
            this.createAssociationReference(source, (EObject) referencedObject, reference, 0);
        }
    }

    private void createAssociationReference(final EObject sourceObject, final EObject targetObject,
            final EReference reference, final long i) throws DBException {
        Node targetNode = ModelGraphFactory.findNode(this.graphDatabaseService, targetObject);
        if (targetNode == null) { /** foreign model object, create proxy. */
            targetNode = ModelGraphFactory.createProxyNode(this.graphDatabaseService, targetObject,
                    ModelGraphFactory.getIdentification(targetObject));
            ModelGraphFactory.createRelationship(this.graphDatabaseService,
                    ModelGraphFactory.findNode(this.graphDatabaseService, sourceObject), targetNode, reference, i);
        } else {
            ModelGraphFactory.createRelationship(this.graphDatabaseService, sourceObject, targetObject, reference, i);
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
    private List<EObject> storeNodesRecursively(final EObject storeableObject, final List<EObject> objectList) {
        ModelGraphFactory.createNode(this.graphDatabaseService, storeableObject,
                ModelGraphFactory.getIdentification(storeableObject));

        objectList.add(storeableObject);

        for (final EReference reference : storeableObject.eClass().getEAllContainments()) {
            final Object referencedObject = storeableObject.eGet(reference);
            if (reference.isMany()) {
                for (final Object object : (EList<?>) referencedObject) {
                    this.storeNodesRecursively((EObject) object, objectList);
                }
            } else if (referencedObject != null) {
                this.storeNodesRecursively((EObject) referencedObject, objectList);
            }
        }

        return objectList;
    }

}
