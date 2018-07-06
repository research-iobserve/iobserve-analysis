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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class UpdateModelFacility extends AbstractModelFacility {

    public UpdateModelFacility(final GraphDatabaseService graphDatabaseService,
            final Map<EObject, Node> objectNodeMap) {
        super(graphDatabaseService, objectNodeMap);
    }

    private <T extends EObject> void updateNode(final T object) {
        final Label clazzNameLabel = Label.label(object.getClass().getCanonicalName());

        final Node node = this.objectNodeMap.get(object);
        if (node == null) {
            // TODO new node
        } else {
            this.updateAttributes(object, node);
            this.updateReferences(object, node);
        }
    }

    private <T extends EObject> void updateReferences(final T object, final Node sourceNode) {
        for (final EReference reference : object.eClass().getEAllReferences()) {
            final Object referencedObject = object.eGet(reference);
            if (referencedObject instanceof EList<?>) {
                for (final Object element : (EList<?>) referencedObject) {
                    this.updateReference((EObject) element, sourceNode, this.objectNodeMap.get(element));
                }
            } else if (referencedObject != null) {

            }
        }
    }

    private void updateReference(final EObject object, final Node sourceNode, final Node targetNode) {
        for (final Relationship relationship : sourceNode.getRelationships(Direction.OUTGOING,
                EMFRelationshipType.CONTAINS, EMFRelationshipType.IS_TYPE)) {
            if (relationship.getEndNode().getId() == targetNode.getId()) {
                // relationship exists, nothing to do
                return;
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

    /**
     * Update a node based on current object values or if node is missing create a new node and
     * place a new reference.
     *
     * @param parentObjectNode
     *            node corresponding to the parent object
     * @param reference
     *            reference definition of the parent object which refers to the referencedObject
     * @param referencedObject
     *            the object to be updated or initially stored
     * @param i
     *            position in the list
     * @param outgoingRelationships
     *            set of outgoingRelationships which have been checked.
     * @param containmentsAndDatatypes
     *            set of objects which are part of the current containment hierarchy
     * @param updatedObjects
     *            set of objects which have been updated
     */
    private void updateOrCreate(final Node parentObjectNode, final EReference reference, final EObject referencedObject,
            final int i, final List<Relationship> outgoingRelationships, final Set<EObject> containmentsAndDatatypes) {
        final Node referencedObjectNode = this.findMatchingNode(ModelProviderUtil.getUriString(referencedObject),
                outgoingRelationships);

        if (referencedObjectNode == null) { // object has no corresponding node,
                                            // create
            // TODO create node if necessary
        } else {
            this.updateNodes(referencedObject, referencedObjectNode, containmentsAndDatatypes);
        }
    }

}
