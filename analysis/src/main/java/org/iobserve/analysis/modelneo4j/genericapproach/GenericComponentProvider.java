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
package org.iobserve.analysis.modelneo4j.genericapproach;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.iobserve.analysis.modelneo4j.PcmRelationshipType;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.CorePackage;
import org.palladiosimulator.pcm.core.composition.CompositionFactory;
import org.palladiosimulator.pcm.core.composition.CompositionPackage;
import org.palladiosimulator.pcm.core.entity.Entity;
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
import org.palladiosimulator.pcm.repository.ParameterModifier;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
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
 *
 * @author Lars Bluemke
 *
 * @param <T>
 */
public class GenericComponentProvider<T extends EObject> {

    public static final String ID = "id";
    public static final String ENTITY_NAME = "entityName";
    public static final String TYPE = "type";
    public static final String REF_NAME = "name";

    private final GraphDatabaseService graph;
    private Node node;

    public GenericComponentProvider(final GraphDatabaseService graph) {
        this.graph = graph;
    }

    public Node createComponent(final T component) {
        /** Create a label representing the type of the component */
        final Label label = Label.label(this.getTypeName(component.eClass()));

        /**
         * For Entities and DataTypes: Look if there already is a node for the component in the
         * graph. Note that complex data types are entities. For everything else: Always create a
         * new node.
         */
        if (component instanceof Entity) {
            try (Transaction tx = this.getGraph().beginTx()) {
                this.node = this.getGraph().findNode(label, GenericComponentProvider.ID, ((Entity) component).getId());
                tx.success();
            }
        } else if (component instanceof PrimitiveDataType) {
            try (Transaction tx = this.getGraph().beginTx()) {
                this.node = this.getGraph().findNode(label, GenericComponentProvider.TYPE,
                        ((PrimitiveDataType) component).getType().name());
                tx.success();
            }
        }

        /** If there is no node yet, create one */
        if (this.node == null) {

            try (Transaction tx = this.getGraph().beginTx()) {
                this.node = this.getGraph().createNode(label);

                /** Iterate over all attributes */
                for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                    final Object value = component.eGet(attr);
                    if (value != null) {
                        this.node.setProperty(attr.getName(), value.toString());
                    }
                }

                tx.success();
            }

            /** Iterate over all references */
            for (final EReference ref : component.eClass().getEAllReferences()) {

                /**
                 * Save references of the component as references to other nodes in the graph
                 */
                final Object refReprensation = component.eGet(ref);

                RelationshipType relType;
                if (ref.isContainment()) {
                    relType = PcmRelationshipType.CONTAINS;
                } else {
                    relType = PcmRelationshipType.REFERENCES;
                }

                /**
                 * If references reference multiple other components they are represented as a list
                 * otherwise they are not represented as a list
                 *
                 * TODO: Make sure the order of the list is kept!
                 */
                if (refReprensation instanceof Iterable<?>) {

                    /** Store each single reference */
                    for (final Object o : (Iterable<?>) component.eGet(ref)) {

                        /** Let a new provider create a node for the referenced component */
                        final Node refNode = new GenericComponentProvider<>(this.graph).createComponent((EObject) o);

                        /** When the new node is created, create a reference */
                        try (Transaction tx = this.getGraph().beginTx()) {

                            final Relationship rel = this.node.createRelationshipTo(refNode, relType);
                            rel.setProperty(GenericComponentProvider.REF_NAME, ref.getName());
                            tx.success();
                        }
                    }
                } else {
                    if (refReprensation != null) {

                        /** Let a new provider create a node for the referenced component */
                        final Node refNode = new GenericComponentProvider<>(this.graph)
                                .createComponent((EObject) refReprensation);

                        /** When the new node is created, create a reference */
                        try (Transaction tx = this.getGraph().beginTx()) {
                            final Relationship rel = this.node.createRelationshipTo(refNode, relType);
                            rel.setProperty(GenericComponentProvider.REF_NAME, ref.getName());
                            tx.success();
                        }
                    }
                }
            }
        }

        return this.node;
    }

    public EObject readComponent(final String typeName, final String id) {
        final Label label = Label.label(typeName);

        try (Transaction tx = this.getGraph().beginTx()) {
            this.node = this.getGraph().findNode(label, GenericComponentProvider.ID, id);
            tx.success();
        }

        return this.readComponent(this.node);
    }

    public EObject readComponent(final Node node) {
        /**
         * Get the node's data type from its label and instantiate a new empty object of this data
         * type
         */
        Label label;
        try (Transaction tx = this.getGraph().beginTx()) {
            label = this.getFirstLabel(node.getLabels());
            tx.success();
        }
        System.out.println(label.name());

        final EObject component = this.instantiateEObject(label.name());

        /** Iterate over all attributes */
        try (Transaction tx = this.getGraph().beginTx()) {

            System.out.println("reading " + component);
            for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                System.out.print("\tattribute " + attr.getName() + " = ");
                try {
                    final Object value = this.instantiateAttribute(attr.getEAttributeType().getInstanceClass(),
                            node.getProperty(attr.getName()).toString());
                    System.out.println(value);
                    if (value != null) {
                        component.eSet(attr, value);
                    }
                } catch (final NotFoundException e) {
                    component.eSet(attr, null);
                }
            }
            tx.success();
        }

        /** Iterate over all references */
        for (final EReference ref : component.eClass().getEAllReferences()) {
            final String refName = ref.getName();
            System.out.print("\treference " + refName + " = ");
            Object refReprensation = component.eGet(ref);

            /** Iterate over all outgoing containment relationships of the node */
            try (Transaction tx = this.getGraph().beginTx()) {
                for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS)) {
                    /** If a relationship in the graph matches the references name... */
                    if (refName.equals(rel.getProperty(GenericComponentProvider.REF_NAME))) {
                        System.out.println(rel.getProperty(GenericComponentProvider.REF_NAME));
                        final Node endNode = rel.getEndNode();

                        /** ...recursively create an instance of the referenced object */
                        if (refReprensation instanceof EList<?>) {
                            final EObject endComponent = new GenericComponentProvider<>(this.graph)
                                    .readComponent(endNode);
                            ((EList<EObject>) refReprensation).add(endComponent);
                        } else {
                            refReprensation = new GenericComponentProvider<>(this.graph).readComponent(endNode);
                        }
                    }
                }
                tx.success();
            }
            // System.out.println(refReprensation);
        }
        return component;
    }

    public List<String> readComponent(final String typeName) {
        try (Transaction tx = this.getGraph().beginTx()) {
            final ResourceIterator<Node> nodes = this.graph.findNodes(Label.label(typeName));
            final LinkedList<String> ids = new LinkedList<>();

            while (nodes.hasNext()) {
                final Node n = nodes.next();
                ids.add(n.getProperty(GenericComponentProvider.ID).toString());
            }

            tx.success();
            return ids;
        }
    }

    public void updateComponent(final T component) {
    }

    public void deleteComponent(final T component) {
    }

    private Label getFirstLabel(final Iterable<Label> labels) {
        for (final Label l : labels) {
            return l;
        }

        return null;
    }

    private String getTypeName(final EClass c) {
        final String name = c.getInstanceTypeName();
        final int i = name.lastIndexOf(".");
        return name.substring(i + 1);
    }

    private Object instantiateAttribute(final Class<?> clazz, final String value) {
        if (clazz == String.class) {
            return value;
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

    private EObject instantiateEObject(final String name) {

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

    public GraphDatabaseService getGraph() {
        return this.graph;
    }
}
