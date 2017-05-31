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
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.RepositoryFactory;
import org.palladiosimulator.pcm.repository.RepositoryPackage;

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

            /** Iterate over all attributes */
            try (Transaction tx = this.getGraph().beginTx()) {
                this.node = this.getGraph().createNode(label);

                /** Save attributes as properties of the node */
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
        Label label;
        try (Transaction tx = this.getGraph().beginTx()) {
            label = this.getFirstLabel(node.getLabels());
            tx.success();
        }
        final EClass eClass = (EClass) RepositoryPackage.eINSTANCE.getEClassifier(label.name());
        final EObject component = RepositoryFactory.eINSTANCE.create(eClass);

        /** Iterate over all attributes */
        try (Transaction tx = this.getGraph().beginTx()) {

            System.out.println("reading " + component);
            for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                System.out.println("\tattribute " + attr.getName());
                try {
                    component.eSet(attr, node.getProperty(attr.getName()));
                } catch (final NotFoundException e) {
                    component.eSet(attr, null);
                }
            }
            tx.success();
        }

        /** Iterate over all references */
        for (final EReference ref : component.eClass().getEAllReferences()) {
            final String refName = ref.getName();
            System.out.println("\treference " + ref.getName());
            Object refReprensation = component.eGet(ref);

            try (Transaction tx = this.getGraph().beginTx()) {
                for (final Relationship rel : node.getRelationships(Direction.OUTGOING)) {
                    System.out
                            .println(rel + " " + rel.getProperty(GenericComponentProvider.REF_NAME) + " = " + refName);
                    if ((rel.getProperty(GenericComponentProvider.REF_NAME)).equals(refName)) {
                        System.out.println("found matching relationship");
                        Node endNode;
                        endNode = rel.getEndNode();

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

    private String parseTypeName(final String name) {
        final int i = name.lastIndexOf(".");
        return name.substring(i + 1);

    }

    public GraphDatabaseService getGraph() {
        return this.graph;
    }
}
