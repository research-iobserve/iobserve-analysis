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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.iobserve.analysis.modelneo4j.PcmRelationshipType;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;

/**
 *
 * @author Lars Bluemke
 *
 * @param <T>
 */
public class ModelProvider<T extends EObject> {

    public static final String ID = "id";
    public static final String ENTITY_NAME = "entityName";
    public static final String TYPE = "type";
    public static final String REF_NAME = "refName";

    private final GraphDatabaseService graph;
    private final HashMap<Node, DataType> dataTypes;

    public ModelProvider(final GraphDatabaseService graph) {
        this.graph = graph;
        this.dataTypes = new HashMap<>();
    }

    public ModelProvider(final GraphDatabaseService graph, final HashMap<Node, DataType> dataTypes) {
        this.graph = graph;
        this.dataTypes = dataTypes;
    }

    public Node createComponent(final EObject component) {
        // Create a label representing the type of the component
        final Label label = Label.label(ModelProviderUtil.getTypeName(component.eClass()));
        Node node = null;

        // For Entities and DataTypes: Look if there already is a node for the component in the
        // graph. Note that complex data types are entities. For everything else: Always create a
        // new node.
        if (component instanceof Entity) {
            try (Transaction tx = this.getGraph().beginTx()) {
                node = this.getGraph().findNode(label, ModelProvider.ID, ((Entity) component).getId());
                tx.success();
            }
        } else if (component instanceof PrimitiveDataType) {
            try (Transaction tx = this.getGraph().beginTx()) {
                node = this.getGraph().findNode(label, ModelProvider.TYPE,
                        ((PrimitiveDataType) component).getType().name());
                tx.success();
            }
        }

        // If there is no node yet, create one
        if (node == null) {

            try (Transaction tx = this.getGraph().beginTx()) {
                node = this.getGraph().createNode(label);

                // System.out.println("writing " + component + " " + label.name());

                // Iterate over all attributes and save them as node properties
                for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                    final Object value = component.eGet(attr);
                    if (value != null) {
                        node.setProperty(attr.getName(), value.toString());
                        // System.out.println("\t" + component + " attribute " + attr.getName() + "
                        // " + value.toString());
                    }
                }
                tx.success();
            }

            // Iterate over all references and save as them as relations in the graph
            for (final EReference ref : component.eClass().getEAllReferences()) {

                final Object refReprensation = component.eGet(ref);
                // System.out.println("\t" + component + " all refs " + ref + " " +
                // refReprensation);

                // 0..* refs are represented as a list and 1 refs are represented directly
                if (refReprensation instanceof EList<?>) {

                    for (final Object o : (EList<?>) component.eGet(ref)) {
                        // System.out.println("\t" + component + " reference " + o);

                        // Create a new node recursively
                        final Node refNode = this.createComponent((EObject) o);

                        // When the new node is created, create a reference
                        try (Transaction tx = this.getGraph().beginTx()) {
                            final Relationship rel = node.createRelationshipTo(refNode,
                                    ModelProviderUtil.getRelationshipType(ref, o));
                            rel.setProperty(ModelProvider.REF_NAME, ref.getName());
                            tx.success();
                        }
                    }
                } else {
                    if (refReprensation != null) {
                        // System.out.println("\t" + component + " reference " + refReprensation);

                        // Create a new node recursively
                        final Node refNode = this.createComponent((EObject) refReprensation);

                        // When the new node is created, create a reference
                        try (Transaction tx = this.getGraph().beginTx()) {
                            final Relationship rel = node.createRelationshipTo(refNode,
                                    ModelProviderUtil.getRelationshipType(ref, refReprensation));
                            rel.setProperty(ModelProvider.REF_NAME, ref.getName());
                            tx.success();
                        }
                    }
                }
            }
        }

        return node;
    }

    public EObject readComponent(final Class<T> clazz, final String id) {
        final Label label = Label.label(clazz.getSimpleName());
        Node node;

        try (Transaction tx = this.getGraph().beginTx()) {
            node = this.getGraph().findNode(label, ModelProvider.ID, id);
            tx.success();
        }

        return this.readComponent(node);
    }

    public EObject readComponent(final Node node) {

        // Get the node's data type name and instantiate a new empty object of this data type
        Label label;
        try (Transaction tx = this.getGraph().beginTx()) {
            label = ModelProviderUtil.getFirstLabel(node.getLabels());
            tx.success();
        }
        final EObject component = ModelProviderUtil.instantiateEObject(label.name());

        try (Transaction tx = this.getGraph().beginTx()) {

            // Iterate over all attributes and get their values from the node
            for (final EAttribute attr : component.eClass().getEAllAttributes()) {

                try {
                    final Object value = ModelProviderUtil.instantiateAttribute(
                            attr.getEAttributeType().getInstanceClass(), node.getProperty(attr.getName()).toString());

                    if (value != null) {
                        component.eSet(attr, value);
                    }
                    // System.out.println("\t" + component + " attribute " + attr.getName() + "
                    // = " + value);
                } catch (final NotFoundException e) {
                    component.eSet(attr, null);
                }
            }
            tx.success();
        }

        // Already register unfinished types because there might be circles with inner declarations
        if (component instanceof DataType) {
            this.getDataTypes().putIfAbsent(node, (DataType) component);
        }

        // Iterate over all references...
        for (final EReference ref : component.eClass().getEAllReferences()) {
            final String refName = ref.getName();
            Object refReprensation = component.eGet(ref);

            try (final Transaction tx = this.getGraph().beginTx()) {

                // ...and iterate over all outgoing containment or type relationships of the node
                for (final Relationship rel : node.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS,
                        PcmRelationshipType.IS_TYPE)) {

                    // If a relationship in the graph matches the references name...
                    if (refName.equals(rel.getProperty(ModelProvider.REF_NAME))) {
                        final Node endNode = rel.getEndNode();

                        // ...recursively create an instance of the referenced object
                        if (rel.isType(PcmRelationshipType.CONTAINS)) {
                            // System.out.println("\t" + component + " reference " + refName);

                            if (refReprensation instanceof EList<?>) {
                                final EObject endComponent = this.readComponent(endNode);
                                ((EList<EObject>) refReprensation).add(endComponent);
                            } else {
                                refReprensation = this.readComponent(endNode);
                                component.eSet(ref, refReprensation);

                            }
                        } else if (rel.isType(PcmRelationshipType.IS_TYPE)) {
                            // System.out.println("\t" + component + " reference " + refName);

                            // Look if this data type has already been created
                            EObject endComponent = this.getDataTypes().get(endNode);

                            if (endComponent == null) {
                                endComponent = this.readComponent(endNode);
                            }

                            if (refReprensation instanceof EList<?>) {
                                ((EList<EObject>) refReprensation).add(endComponent);
                            } else {
                                component.eSet(ref, endComponent);
                            }

                            // Replace possibly unfinished data type
                            this.getDataTypes().replace(node, (DataType) endComponent);

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
                ids.add(n.getProperty(ModelProvider.ID).toString());
            }

            tx.success();
            return ids;
        }
    }

    public void updateComponent(final EObject component) {
    }

    public void deleteComponent(final EObject component) {
    }

    public HashMap<Node, DataType> getDataTypes() {
        return this.dataTypes;
    }

    public GraphDatabaseService getGraph() {
        return this.graph;
    }
}
