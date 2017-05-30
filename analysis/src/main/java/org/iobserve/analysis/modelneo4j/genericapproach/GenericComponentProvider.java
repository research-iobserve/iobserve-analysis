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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.iobserve.analysis.modelneo4j.PcmRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.core.entity.Entity;

/**
 *
 * @author Lars Bluemke
 *
 * @param <T>
 */
public class GenericComponentProvider<T extends EObject> {

    public static final String ID = "id";
    public static final String ENTITY_NAME = "entityName";

    private final GraphDatabaseService graph;
    private Node node;

    public GenericComponentProvider(final GraphDatabaseService graph) {
        this.graph = graph;
    }

    public Node createComponent(final T component) {
        // System.out.println("\n\n------------------------------------------------------------------------");
        // System.out.println("component " + component);

        /** Create a label representing the type of the component */
        final Label label = Label.label(this.parseTypeName(component.eClass().getInstanceTypeName()));

        /** Look if there already is a node for the component in the graph */
        try (Transaction tx = this.getGraph().beginTx()) {
            this.node = this.getGraph().findNode(label, GenericComponentProvider.ID,
                    component.eGet(component.eClass().getEIDAttribute()));
            tx.success();
        }

        /** If there is no node yet, create one */
        if (this.node == null) {

            /** Iterate over all attributes */
            try (Transaction tx = this.getGraph().beginTx()) {
                this.node = this.getGraph().createNode(label);

                for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                    // System.out.println("attibute " + attr.getEAttributeType() + " key: " +
                    // attr.getName() + " value: "
                    // + component.eGet(attr));

                    /** Save attributes as properties of the node */
                    final Object value = component.eGet(attr);
                    if (value != null) {
                        this.node.setProperty(attr.getName(), value.toString());
                    }
                }

                tx.success();
            }

            /** Iterate over all references */
            for (final EReference ref : component.eClass().getEAllReferences()) {
                // System.out.println("\n\n------------------------------------------------------------------------");
                // System.out.println("component " + component);
                // System.out.println("reference " + ref.getEReferenceType() + " value: " +
                // component.eGet(ref));

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
                        if (o instanceof Entity) {

                            /** Let a new provider create a node for the referenced component */
                            final Node refNode = new GenericComponentProvider<>(this.graph).createComponent((Entity) o);

                            /** When the new node is created, create a reference */
                            try (Transaction tx = this.getGraph().beginTx()) {

                                this.node.createRelationshipTo(refNode, relType);
                                tx.success();
                            }
                        } else {
                            if (o != null) {
                                System.out.println(component + " has " + o
                                        + " (iterable) which is not an Entity and reltype is " + relType);
                            }
                        }
                    }
                } else {
                    if (refReprensation instanceof Entity) {

                        /** Let a new provider create a node for the referenced component */
                        final Node refNode = new GenericComponentProvider<>(this.graph)
                                .createComponent((Entity) refReprensation);

                        /** When the new node is created, create a reference */
                        try (Transaction tx = this.getGraph().beginTx()) {
                            this.node.createRelationshipTo(refNode, relType);
                            tx.success();
                        }
                    } else {
                        if (refReprensation != null) {
                            System.out.println(component + " has " + refReprensation
                                    + " (single) which is not an Entity and reltype is " + relType);
                        }
                    }
                }
            }
        }

        return this.node;
    }

    private String parseTypeName(final String name) {
        final int i = name.lastIndexOf(".");
        return name.substring(i + 1);

    }

    public T readComponent(final String id) {
        return null;
    }

    public void updateComponent(final T component) {
    }

    public void deleteComponent(final T component) {
    }

    public GraphDatabaseService getGraph() {
        return this.graph;
    }
}
