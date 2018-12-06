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
    public UpdateModelFacility(final ModelResource<R> resource, final GraphDatabaseService graphDatabaseService) {
        super(resource, graphDatabaseService);
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
     * @throws DBException
     *             on db errors
     */
    public <T extends EObject> void updatePartition(final T object) throws NodeLookupException, DBException {
        /**
         * Identify new objects (they have no internal id) and create them in the DB. Collect all
         * objects in a list.
         */
        final List<EObject> objects = this.updateAndAddToPartition(object, null, new ArrayList<EObject>());

        /** fix references. */
        this.updateAllReferences(objects);

        /**
         * Identify nodes in the DB which are not represented in the collection.
         */
        ModelGraphFactory.deleteNodePartitionRecursively(ModelGraphFactory.findNode(this.graphDatabaseService, object),
                objects);
    }

    private List<EObject> updateAndAddToPartition(final EObject object, final EObject parent,
            final List<EObject> collectedObjects) throws DBException {
        collectedObjects.add(object);
        final Node node = ModelGraphFactory.findNode(this.graphDatabaseService, object);
        if (node == null) {
            /** object is new => store object */
            ModelGraphFactory.createNode(this.graphDatabaseService, object,
                    ModelGraphFactory.getIdentification(object));
            collectedObjects.add(object);
        } else {
            this.updateAttributes(object, node);
        }
        for (final EReference containment : object.eClass().getEAllContainments()) {
            this.processContainment(object, containment, collectedObjects);
        }

        return collectedObjects;
    }

    @SuppressWarnings("unchecked")
    private List<EObject> processContainment(final EObject object, final EReference containment,
            final List<EObject> collectedObjects) throws DBException {
        final Object item = object.eGet(containment);
        if (containment.isMany()) {
            for (final EObject value : (EList<EObject>) item) {
                this.updateAndAddToPartition(value, object, collectedObjects);
            }
        } else {
            this.updateAndAddToPartition((EObject) item, object, collectedObjects);
        }
        return collectedObjects;
    }

    private void updateAllReferences(final List<EObject> objects) throws NodeLookupException, DBException {
        for (final EObject object : objects) {
            if (!ModelGraphFactory.isProxyNode(this.graphDatabaseService, object)) {
                for (final EReference reference : object.eClass().getEAllReferences()) {
                    final Node node = ModelGraphFactory.findNode(this.graphDatabaseService, object);
                    this.updateReference(object, node, reference, objects);
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
     * @param objects
     *            all existing updated objects
     * @throws NodeLookupException
     * @throws DBException
     *             on various db errors
     */
    private void updateReference(final EObject sourceObject, final Node sourceNode, final EReference reference,
            final List<EObject> objects) throws NodeLookupException, DBException {
        final Object referencedObject = sourceObject.eGet(reference);
        if (reference.isMany()) {
            int i = 0;
            for (final Object element : (EList<?>) referencedObject) {
                this.updateOrCreateReferenceObject(sourceNode, reference, sourceObject, (EObject) element, i++);
            }
        } else {
            if (referencedObject != null) {
                this.updateOrCreateReferenceObject(sourceNode, reference, sourceObject, (EObject) referencedObject, 0);
            }
        }
    }

    private void updateOrCreateReferenceObject(final Node sourceNode, final EReference reference,
            final EObject sourceObject, final EObject targetObject, final int index) throws DBException {
        Node targetNode = ModelGraphFactory.findNode(this.graphDatabaseService, targetObject);
        if (targetNode == null) {
            targetNode = ModelGraphFactory.createProxyNode(this.graphDatabaseService, targetObject,
                    ModelGraphFactory.getIdentification(targetObject));
        }

        this.updateOrCreateRelationship(sourceNode, targetNode, reference, index);
    }

    private void updateOrCreateRelationship(final Node sourceNode, final Node targetNode, final EReference reference,
            final int position) throws DBException {
        if (reference.isContainment()) {
            final Iterable<Relationship> relationships = sourceNode.getRelationships(Direction.OUTGOING,
                    EMFRelationshipType.CONTAINS);
            final Relationship relationship = ModelGraphFactory.findRelationshipByEndNode(relationships, targetNode);
            if (relationship == null) {
                ModelGraphFactory.createRelationship(this.graphDatabaseService, sourceNode, targetNode, reference,
                        position);
            } else {
                ModelGraphFactory.updateRelationship(relationship, position);
            }
        } else {
            final Iterable<Relationship> relationships = sourceNode.getRelationships(Direction.OUTGOING,
                    EMFRelationshipType.REFERENCES);
            final Relationship relationship = ModelGraphFactory.findRelationshipByEndNode(relationships, targetNode);
            if (relationship == null) {
                ModelGraphFactory.createRelationship(this.graphDatabaseService, sourceNode, targetNode, reference,
                        position);
            } else {
                ModelGraphFactory.updateRelationship(relationship, position);
            }
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
