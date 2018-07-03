/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.provider.neo4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.palladiosimulator.pcm.parameter.VariableCharacterisationType;
import org.palladiosimulator.pcm.repository.ComponentType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.ParameterModifier;
import org.palladiosimulator.pcm.repository.PrimitiveTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides different utilities for the {@link ModelProvider}.
 *
 * @author Lars Bluemke
 *
 */
public final class ModelProviderUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModelProviderUtil.class);

    private ModelProviderUtil() {
        // private utility class
    }

    /**
     * Based on a certain object-URI and a list of references to nodes which possibly represent that
     * component, this method returns the node which actually represents the component or null if
     * there is none in the list. Relationships to matching nodes are removed from the list, so this
     * method can also be used to reduce a list of references to those references which link to
     * nodes whose component does not exist anymore.
     *
     * @param uri
     *            The object-URI
     * @param relationships
     *            The relationships to possibly matching nodes
     * @return The node representing the component or null if there is none
     */
    public static Node findMatchingNode(final String uri, final List<Relationship> relationships) {
        if (uri != null) {
            for (final Relationship relationship : relationships) {
                final Node node = relationship.getEndNode();
                try {
                    final String nodeUri = node.getProperty(ModelProvider.EMF_URI).toString();

                    if (uri.equals(nodeUri)) {
                        relationships.remove(relationship);
                        return node;
                    }
                } catch (final NotFoundException e) {
                    ModelProviderUtil.LOGGER.error(
                            "Tried to delete a relationship which has already been removed. id {} and exception {}",
                            relationship.getId(), e);
                }
            }
        }

        return null;
    }

    /**
     * Returns a URI based on the components containing the passed component.
     *
     * @param component
     *            The component to compute a URI to
     * @return The URI
     */
    public static String getUriString(final EObject component) {
        EObject comp = component;
        EObject container;
        String label;
        String uri = "";
        EAttribute idAttr;

        do {
            container = comp.eContainer();
            label = comp.eClass().getInstanceTypeName();

            idAttr = comp.eClass().getEIDAttribute();

            if (uri.isEmpty()) {
                if (idAttr != null) {
                    uri = label + "#" + comp.eGet(idAttr);
                } else {
                    uri = label;
                }
            } else {
                if (idAttr != null) {
                    uri = label + "#" + comp.eGet(idAttr) + "." + uri;
                } else {
                    uri = label + "." + uri;
                }
            }

            comp = container;

        } while (container != null);

        return uri;
    }

    /**
     * Returns the first of several labels.
     *
     * @param labels
     *            Several labels
     * @return The first label
     */
    public static Label getFirstLabel(final Iterable<Label> labels) {
        if (labels.iterator().hasNext()) {
            return labels.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Returns a {@link #PcmRelationshipType} for a given reference and the referenced object.
     *
     * @param ref
     *            The reference
     * @param refObj
     *            The referenced object
     * @return The proper relationship type
     */
    public static RelationshipType getRelationshipType(final EReference ref, final Object refObj) {

        if (ref.isContainment()) {
            return EMFRelationshipType.CONTAINS;
        } else if (ModelProviderUtil.isDatatype(ref, refObj)) {
            return EMFRelationshipType.IS_TYPE;
        } else {
            return EMFRelationshipType.REFERENCES;
        }
    }

    /**
     * Checks whether a referenced object is the referencer's data type.
     *
     * @param reference
     *            The reference
     * @param referenceObject
     *            The referenced object
     * @return True, if the referenced object is the referencer's data type, false otherwise
     */
    public static boolean isDatatype(final EReference reference, final Object referenceObject) {
        return referenceObject instanceof DataType && !(reference.getName().equals("parentType_CompositeDataType")
                || reference.getName().equals("compositeDataType_InnerDeclaration"));
    }

    /**
     * Instantiates attributes of pcm model components.
     *
     * @param type
     *            The attribute's data type
     * @param value
     *            The attribute's string value from the property of the neo4j graph
     * @return The attribute's value in the proper data type
     */
    public static Object instantiateAttribute(final EDataType type, final String value) {

        final Class<?> clazz = type.getInstanceClass();

        if (clazz == String.class) {
            return value;
        } else if (clazz == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (clazz == char.class) {
            return value.charAt(0);
        } else if (clazz == byte.class) {
            return Byte.parseByte(value);
        } else if (clazz == short.class) { // NOPMD short supported by Kieker
            return Short.parseShort(value);
        } else if (clazz == int.class) {
            return Integer.parseInt(value);
        } else if (clazz == long.class) {
            return Long.parseLong(value);
        } else if (clazz == float.class) {
            return Float.parseFloat(value);
        } else if (clazz == double.class) {
            return Double.parseDouble(value);
        } else if (clazz == ParameterModifier.class) {
            if (value.equals(ParameterModifier.NONE.toString())) {
                return ParameterModifier.NONE;
            } else if (value.equals(ParameterModifier.IN.toString())) {
                return ParameterModifier.IN;
            } else if (value.equals(ParameterModifier.OUT.toString())) {
                return ParameterModifier.OUT;
            } else if (value.equals(ParameterModifier.INOUT.toString())) {
                return ParameterModifier.INOUT;
            }
        } else if (clazz == ComponentType.class) {
            if (value.equals(ComponentType.BUSINESS_COMPONENT.toString())) {
                return ComponentType.BUSINESS_COMPONENT;
            } else if (value.equals(ComponentType.INFRASTRUCTURE_COMPONENT.toString())) {
                return ComponentType.INFRASTRUCTURE_COMPONENT;
            }
        } else if (clazz == PrimitiveTypeEnum.class) {
            if (value.equals(PrimitiveTypeEnum.INT.toString())) {
                return PrimitiveTypeEnum.INT;
            } else if (value.equals(PrimitiveTypeEnum.STRING.toString())) {
                return PrimitiveTypeEnum.STRING;
            } else if (value.equals(PrimitiveTypeEnum.BOOL.toString())) {
                return PrimitiveTypeEnum.BOOL;
            } else if (value.equals(PrimitiveTypeEnum.DOUBLE.toString())) {
                return PrimitiveTypeEnum.DOUBLE;
            } else if (value.equals(PrimitiveTypeEnum.CHAR.toString())) {
                return PrimitiveTypeEnum.CHAR;
            } else if (value.equals(PrimitiveTypeEnum.BYTE.toString())) {
                return PrimitiveTypeEnum.BYTE;
            } else if (value.equals(PrimitiveTypeEnum.LONG.toString())) {
                return PrimitiveTypeEnum.LONG;
            }
        } else if (clazz == VariableCharacterisationType.class) {
            if (value.equals(VariableCharacterisationType.STRUCTURE.toString())) {
                return VariableCharacterisationType.STRUCTURE;
            } else if (value.equals(VariableCharacterisationType.NUMBER_OF_ELEMENTS.toString())) {
                return VariableCharacterisationType.NUMBER_OF_ELEMENTS;
            } else if (value.equals(VariableCharacterisationType.VALUE.toString())) {
                return VariableCharacterisationType.VALUE;
            } else if (value.equals(VariableCharacterisationType.BYTESIZE.toString())) {
                return VariableCharacterisationType.BYTESIZE;
            } else if (value.equals(VariableCharacterisationType.TYPE.toString())) {
                return VariableCharacterisationType.TYPE;
            }
        }

        return null;
    }

    /**
     * Instantiates a pcm model component from a given type name.
     *
     * @param factories
     *            factories for this particular metamodel
     * @param name
     *            The component's data type name
     * @return New object of the given data type
     */
    public static EObject instantiateEObject(final List<EFactory> factories, final String name) {

        final int separationPoint = name.lastIndexOf('.');
        final String className = name.substring(separationPoint + 1);

        for (final EFactory factory : factories) {
            final EPackage ePackage = factory.getEPackage();
            final EClass eClass = (EClass) ePackage.getEClassifier(className);
            if (eClass != null) {
                return factory.create(eClass);
            }
        }
        return null;

    }

    /**
     * Sorts an Iterable of relationships by their position properties.
     *
     * @param relationships
     *            The relationships to be sorted
     * @return The sorted relationships
     */
    public static Iterable<Relationship> sortRelsByPosition(final Iterable<Relationship> relationships) {
        if (relationships == null) {
            return Collections.emptyList();
        }

        final List<Relationship> sortedRels = new ArrayList<>();
        relationships.forEach(sortedRels::add);

        Collections.sort(sortedRels, new RelationshipComparator());

        return sortedRels;
    }
}
