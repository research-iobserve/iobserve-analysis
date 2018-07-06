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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

/**
 * @author Reiner Jung
 *
 */
public class QueryModelFacility extends AbstractModelFacility {

    private final List<EFactory> eFactories;

    /**
     * Create a query model facility.
     *
     * @param graphDatabaseService
     *            data base service
     * @param objectNodeMap
     *            object node map
     * @param eFactories
     *            used factories
     */
    public QueryModelFacility(final GraphDatabaseService graphDatabaseService, final Map<EObject, Node> objectNodeMap,
            final List<EFactory> eFactories) {
        super(graphDatabaseService, objectNodeMap);
        this.eFactories = eFactories;
    }

    /**
     * Helper method for reading: Starting from a given node this method recursively reads all
     * contained successor nodes and instantiates the correspondent EObjects. Calls to this method
     * have to be performed from inside a {@link Transaction}.
     *
     * @param node
     *            The node to start with
     * @param containmentsAndDatatypes
     *            Set of all containments and data types of the root node
     * @param nodesToCreatedObjects
     *            Initially empty map of nodes to already created objects to make sure that objects
     *            are instantiated just once
     * @param <T>
     *            type definition
     * @return The root
     */
    @SuppressWarnings("unchecked")
    public <T extends EObject> T readNodes(final Node node, final Set<Node> containmentsAndDatatypes,
            final Map<Node, EObject> nodesToCreatedObjects) {
        final T object;

        if (!nodesToCreatedObjects.containsKey(node)) {
            // Get node's data type label and instantiate a new empty object of this data type
            final Label label = ModelProviderUtil.getFirstLabel(node.getLabels());
            object = (T) ModelProviderUtil.instantiateEObject(this.eFactories, label.name());

            this.loadAttributes(object, node.getAllProperties());

            // Already register unfinished components because there might be circles
            nodesToCreatedObjects.putIfAbsent(node, object);

            this.loadRelatedNodes(object, node, containmentsAndDatatypes, nodesToCreatedObjects);
        } else {
            object = (T) nodesToCreatedObjects.get(node);
        }

        return object;
    }

    /**
     * Load attribute values from the node.
     *
     * @param object
     *            component where the attributes are attached to
     * @param properties
     *            the attributes to scan for values
     */
    private <T extends EObject> void loadAttributes(final T object, final Map<String, Object> properties) {
        for (final Entry<String, Object> property : properties.entrySet()) {
            final EAttribute attr = (EAttribute) object.eClass().getEStructuralFeature(property.getKey());

            // attr == null for the emfUri property stored in the graph
            if (attr != null) {
                if (attr.isMany()) {
                    this.createManyValuesAttribute(object, attr, property);
                } else {
                    this.createSingleValueAttribute(object, attr, property);
                }

            }
        }
    }

    private void createManyValuesAttribute(final EObject component, final EAttribute attr,
            final Entry<String, Object> property) {
        @SuppressWarnings("unchecked")
        final List<Object> attribute = (List<Object>) component.eGet(attr);

        final String valueString = property.getValue().toString();

        final String[] values = valueString.substring(1, valueString.length() - 1).split(", ");
        for (final String value : values) {
            final Object convertedValue = this.convertValue(attr.getEAttributeType(), value);

            attribute.add(convertedValue);
        }
    }

    private void createSingleValueAttribute(final EObject component, final EAttribute attr,
            final Entry<String, Object> property) {
        component.eSet(attr, this.convertValue(attr.getEAttributeType(), property.getValue().toString()));
    }

    private Object convertValue(final EDataType type, final String input) {
        Object value = ModelProviderUtil.instantiateAttribute(type, input);

        if (value == null) {
            for (final EFactory factory : this.eFactories) {
                value = factory.createFromString(type, input);

                if (value != null) {
                    return value;
                }
            }

            throw new InternalError("Type " + type.getInstanceClassName() + " is not supported.");
        } else {
            return value;
        }
    }

    /**
     * Load related nodes representing referenced components.
     *
     * @param component
     *            component the other components are related to
     * @param node
     *            the corresponding node to the component
     * @param containmentsAndDatatypes
     *            datatypes
     * @param nodesToCreatedObjects
     *            additional nodes
     */
    @SuppressWarnings("unchecked")
    private void loadRelatedNodes(final EObject component, final Node node, final Set<Node> containmentsAndDatatypes,
            final Map<Node, EObject> nodesToCreatedObjects) {
        for (final Relationship rel : ModelProviderUtil.sortRelsByPosition(node.getRelationships(Direction.OUTGOING))) {
            final Node endNode = rel.getEndNode();
            final String relName = (String) rel.getProperty(ModelProviderUtil.REF_NAME);
            final EReference ref = (EReference) component.eClass().getEStructuralFeature(relName);
            Object refReprensation = component.eGet(ref);

            // For partial reading: Only load containments and data types of the root
            if (containmentsAndDatatypes.contains(endNode)) {

                if (refReprensation instanceof EList<?>) {
                    final EObject endComponent = this.readNodes(endNode, containmentsAndDatatypes,
                            nodesToCreatedObjects);
                    ((EList<EObject>) refReprensation).add(endComponent);

                } else {
                    refReprensation = this.readNodes(endNode, containmentsAndDatatypes, nodesToCreatedObjects);
                    component.eSet(ref, refReprensation);
                }

            } else {
                // Create proxy EObject here...
                final Label endLabel = ModelProviderUtil.getFirstLabel(endNode.getLabels());
                final EObject endComponent = ModelProviderUtil.instantiateEObject(this.eFactories, endLabel.name());

                if (endComponent != null) {
                    final URI endUri = URI.createURI(endNode.getProperty(ModelProviderUtil.EMF_URI).toString());
                    ((BasicEObjectImpl) endComponent).eSetProxyURI(endUri);
                    // TODO here to set the unique identifier

                    // Load attribute values from the node
                    final Iterator<Map.Entry<String, Object>> i2 = endNode.getAllProperties().entrySet().iterator();
                    while (i2.hasNext()) {
                        final Entry<String, Object> property = i2.next();
                        final EAttribute attr = (EAttribute) endComponent.eClass()
                                .getEStructuralFeature(property.getKey());

                        // attr == null for the emfUri property stored in the graph
                        if (attr != null) {
                            final Object value = ModelProviderUtil.instantiateAttribute(attr.getEAttributeType(),
                                    property.getValue().toString());

                            if (value != null) {
                                endComponent.eSet(attr, value);
                            }
                        }
                    }

                    if (refReprensation instanceof EList<?>) {
                        ((EList<EObject>) refReprensation).add(endComponent);
                    } else {
                        if (ref.isChangeable()) { // TODO what to do with unchangeable elements
                            component.eSet(ref, endComponent);
                        }
                    }
                }
            }
        }

    }

    /**
     * Helper method for writing: Recursively returns all containments and data types of a given
     * component.
     *
     * @param object
     *            The component to start with
     * @param containmentsAndDatatypes
     *            Initially empty set of all containments and data types
     * @return The passed containmentsAndDatatypes set now filled with containments and data types
     */
    Set<EObject> getAllContainmentsByObject(final EObject object, final Set<EObject> containmentsAndDatatypes) {
        if (!containmentsAndDatatypes.contains(object)) {
            containmentsAndDatatypes.add(object);

            for (final EReference reference : object.eClass().getEAllReferences()) {
                final Object referencedEntity = object.eGet(reference);
                if (referencedEntity instanceof EList<?>) {
                    this.checkMultipleReferences(reference, referencedEntity, containmentsAndDatatypes);
                } else {
                    this.checkSingleReferences(reference, referencedEntity, containmentsAndDatatypes);
                }
            }
        }

        return containmentsAndDatatypes;
    }

    private void checkMultipleReferences(final EReference reference, final Object referencedEntity,
            final Set<EObject> containmentsAndDatatypes) {
        for (final Object referencedElement : (EList<?>) referencedEntity) {
            if (reference.isContainment() || ModelProviderUtil.isDatatype(reference, referencedElement)) {
                this.getAllContainmentsByObject((EObject) referencedElement, containmentsAndDatatypes);
            }
        }
    }

    private void checkSingleReferences(final EReference reference, final Object referencedEntity,
            final Set<EObject> containmentsAndDatatypes) {
        if (referencedEntity != null
                && (reference.isContainment() || ModelProviderUtil.isDatatype(reference, referencedEntity))) {
            this.getAllContainmentsByObject((EObject) referencedEntity, containmentsAndDatatypes);
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

            for (final Relationship rel : node.getRelationships(Direction.OUTGOING, EMFRelationshipType.CONTAINS,
                    EMFRelationshipType.IS_TYPE)) {
                this.getAllContainmentNodes(rel.getEndNode(), nodes);
            }
        }

        return nodes;
    }

    /**
     * Read the object of the given and all contained objects.
     *
     * @param node
     *            the object identifying node
     *
     * @param <T>
     *            type of the root node
     * @return the object for the node
     */
    @SuppressWarnings("unchecked")
    public <T> T readContainedNodes(final Node node) {
        final Set<Node> containmentsAndDatatypes = this.getAllContainmentNodes(node, new HashSet<Node>());
        return (T) this.readNodes(node, containmentsAndDatatypes, new HashMap<Node, EObject>());
    }

}
