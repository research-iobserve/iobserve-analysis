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
package org.iobserve.analysis.modelneo4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.CorePackage;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.core.composition.CompositionPackage;
import org.palladiosimulator.pcm.core.entity.EntityFactory;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcm.parameter.ParameterFactory;
import org.palladiosimulator.pcm.parameter.ParameterPackage;
import org.palladiosimulator.pcm.parameter.VariableCharacterisationType;
import org.palladiosimulator.pcm.protocol.ProtocolFactory;
import org.palladiosimulator.pcm.protocol.ProtocolPackage;
import org.palladiosimulator.pcm.qosannotations.QosannotationsFactory;
import org.palladiosimulator.pcm.qosannotations.QosannotationsPackage;
import org.palladiosimulator.pcm.qosannotations.qos_performance.QosPerformanceFactory;
import org.palladiosimulator.pcm.qosannotations.qos_performance.QosPerformancePackage;
import org.palladiosimulator.pcm.qosannotations.qos_reliability.QosReliabilityFactory;
import org.palladiosimulator.pcm.qosannotations.qos_reliability.QosReliabilityPackage;
import org.palladiosimulator.pcm.reliability.ReliabilityFactory;
import org.palladiosimulator.pcm.reliability.ReliabilityPackage;
import org.palladiosimulator.pcm.repository.ComponentType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.ParameterModifier;
import org.palladiosimulator.pcm.repository.PrimitiveTypeEnum;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentFactory;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;
import org.palladiosimulator.pcm.seff.SeffFactory;
import org.palladiosimulator.pcm.seff.SeffPackage;
import org.palladiosimulator.pcm.seff.seff_performance.SeffPerformanceFactory;
import org.palladiosimulator.pcm.seff.seff_performance.SeffPerformancePackage;
import org.palladiosimulator.pcm.seff.seff_reliability.SeffReliabilityFactory;
import org.palladiosimulator.pcm.seff.seff_reliability.SeffReliabilityPackage;
import org.palladiosimulator.pcm.subsystem.SubsystemFactory;
import org.palladiosimulator.pcm.subsystem.SubsystemPackage;
import org.palladiosimulator.pcm.system.SystemFactory;
import org.palladiosimulator.pcm.system.SystemPackage;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

/**
 * Provides different utilities for the {@link ModelProvider}.
 *
 * @author Lars Bluemke
 *
 */
public class ModelProviderUtil {

    /**
     * Based on a certain component's URI and a list of references to nodes which possibly represent
     * that component, this method returns the node which actually represents the component or null
     * if there is none in the list. Relationships to matching nodes are removed from the list, so
     * this method can also be used to reduce a list of references to those references which link to
     * nodes whose component does not exist anymore.
     *
     * @param uri
     *            The component's URI
     * @param rels
     *            The relationships to possibly matching nodes
     * @return The node representing the component or null if there is none
     */
    public static Node findMatchingNode(final String uri, final List<Relationship> rels) {

        if (uri != null) {
            for (final Relationship r : rels) {
                final Node node = r.getEndNode();
                try {
                    final String nodeUri = node.getProperty(ModelProvider.EMF_URI).toString();

                    if (uri.equals(nodeUri)) {
                        rels.remove(r);
                        return node;
                    }
                } catch (final NotFoundException e) {
                    // node as already been deleted
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
            label = ModelProviderUtil.getTypeName(comp.eClass());
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
        for (final Label l : labels) {
            return l;
        }

        return null;
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
            return PcmRelationshipType.CONTAINS;
        } else if (ModelProviderUtil.isDatatype(ref, refObj)) {
            return PcmRelationshipType.IS_TYPE;
        } else {
            return PcmRelationshipType.REFERENCES;
        }
    }

    /**
     * Returns only an EClasses simple name, not the fully qualified name.
     *
     * @param c
     *            The EClass
     * @return The EClasses simple name
     */
    public static String getTypeName(final EClass c) {
        final String name = c.getInstanceTypeName();
        final int i = name.lastIndexOf(".");
        return name.substring(i + 1);
    }

    /**
     * Checks whether a referenced object is the referencer's data type.
     *
     * @param ref
     *            The reference
     * @param refObj
     *            The referenced object
     * @return True, if the referenced object is the referencer's data type, false otherwise
     */
    public static boolean isDatatype(final EReference ref, final Object refObj) {
        return (refObj instanceof DataType) && !(ref.getName().equals("parentType_CompositeDataType")
                || (ref.getName().equals("compositeDataType_InnerDeclaration")));
    }

    /**
     * Instantiates attributes of pcm model components.
     *
     * @param clazz
     *            The attribute's data type
     * @param value
     *            The attribute's string value from the property of the neo4j graph
     * @return The attribute's value in the proper data type
     */
    public static Object instantiateAttribute(final Class<?> clazz, final String value) {

        if (clazz == String.class) {
            return value;
        } else if (clazz == boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (clazz == char.class) {
            return value.charAt(0);
        } else if (clazz == byte.class) {
            return Byte.parseByte(value);
        } else if (clazz == short.class) {
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
     * @param name
     *            The component's data type name
     * @return New object of the given data type
     */
    public static EObject instantiateEObject(final String name) {

        if (CorePackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) CorePackage.eINSTANCE.getEClassifier(name);
            return CoreFactory.eINSTANCE.create(eClass);
        } else if (EntityPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) EntityPackage.eINSTANCE.getEClassifier(name);
            return EntityFactory.eINSTANCE.create(eClass);
        } else if (CompositionPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) CompositionPackage.eINSTANCE.getEClassifier(name);
            return CompositionFactory.eINSTANCE.create(eClass);
        } else if (UsagemodelPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) UsagemodelPackage.eINSTANCE.getEClassifier(name);
            return UsagemodelFactory.eINSTANCE.create(eClass);
        } else if (RepositoryPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) RepositoryPackage.eINSTANCE.getEClassifier(name);
            return RepositoryFactory.eINSTANCE.create(eClass);
        } else if (ResourcetypePackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) ResourcetypePackage.eINSTANCE.getEClassifier(name);
            return ResourcetypeFactory.eINSTANCE.create(eClass);
        } else if (ProtocolPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) ProtocolPackage.eINSTANCE.getEClassifier(name);
            return ProtocolFactory.eINSTANCE.create(eClass);
        } else if (ParameterPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) ParameterPackage.eINSTANCE.getEClassifier(name);
            return ParameterFactory.eINSTANCE.create(eClass);
        } else if (ReliabilityPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) ReliabilityPackage.eINSTANCE.getEClassifier(name);
            return ReliabilityFactory.eINSTANCE.create(eClass);
        } else if (SeffPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) SeffPackage.eINSTANCE.getEClassifier(name);
            return SeffFactory.eINSTANCE.create(eClass);
        } else if (SeffPerformancePackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) SeffPerformancePackage.eINSTANCE.getEClassifier(name);
            return SeffPerformanceFactory.eINSTANCE.create(eClass);
        } else if (SeffReliabilityPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) SeffReliabilityPackage.eINSTANCE.getEClassifier(name);
            return SeffReliabilityFactory.eINSTANCE.create(eClass);
        } else if (QosannotationsPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) QosannotationsPackage.eINSTANCE.getEClassifier(name);
            return QosannotationsFactory.eINSTANCE.create(eClass);
        } else if (QosPerformancePackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) QosPerformancePackage.eINSTANCE.getEClassifier(name);
            return QosPerformanceFactory.eINSTANCE.create(eClass);
        } else if (QosReliabilityPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) QosReliabilityPackage.eINSTANCE.getEClassifier(name);
            return QosReliabilityFactory.eINSTANCE.create(eClass);
        } else if (SystemPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) SystemPackage.eINSTANCE.getEClassifier(name);
            return SystemFactory.eINSTANCE.create(eClass);
        } else if (ResourceenvironmentPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) ResourceenvironmentPackage.eINSTANCE.getEClassifier(name);
            return ResourceenvironmentFactory.eINSTANCE.create(eClass);
        } else if (AllocationPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) AllocationPackage.eINSTANCE.getEClassifier(name);
            return AllocationFactory.eINSTANCE.create(eClass);
        } else if (SubsystemPackage.eINSTANCE.getEClassifier(name) != null) {
            final EClass eClass = (EClass) SubsystemPackage.eINSTANCE.getEClassifier(name);
            return SubsystemFactory.eINSTANCE.create(eClass);
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
