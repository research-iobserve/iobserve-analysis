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
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.iobserve.analysis.modelneo4j.PcmRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.core.entity.Entity;

/**
 *
 * @author Lars Bluemke
 *
 * @param <T>
 */
public class GenericComponentProvider<T extends Entity> {

    public static final String ID = "id";
    public static final String ENTITY_NAME = "entityName";

    private final GraphDatabaseService graph;
    private Node node;

    public GenericComponentProvider(final GraphDatabaseService graph) {
        this.graph = graph;
    }

    public Node createComponent(final T component) {
        System.out.println("\n\n------------------------------------------------------------------------");
        System.out.println("component " + component);
        final Label label = Label.label(this.parseTypeName(component.eClass().getInstanceTypeName()));
        try (Transaction tx = this.getGraph().beginTx()) {
            this.node = this.getGraph().findNode(label, GenericComponentProvider.ID,
                    component.eGet(component.eClass().getEIDAttribute()));
            tx.success();
        }

        if (this.node == null) {
            try (Transaction tx = this.getGraph().beginTx()) {
                this.node = this.getGraph().createNode(label);
                tx.success();
            }

            int as = 0;
            int rs = 0;
            for (final EStructuralFeature f : component.eClass().getEAllStructuralFeatures()) {
                if (f instanceof EAttribute) {
                    as++;
                    System.out.println("attibute " + ((EAttribute) f).getEAttributeType() + " key: " + f.getName()
                            + " value: " + component.eGet(f));

                    final Object a = component.eGet(f);
                    if (a != null) {
                        try (Transaction tx = this.getGraph().beginTx()) {
                            this.node.setProperty(f.getName(), a.toString());
                            tx.success();
                        }
                    }

                } else if (f instanceof EReference) {

                    rs++;
                    System.out.println("\n\n------------------------------------------------------------------------");
                    System.out.println("component " + component);
                    System.out.println(
                            "reference " + ((EReference) f).getEReferenceType() + " value: " + component.eGet(f));

                    final Object r = component.eGet(f);
                    if (r instanceof Iterable<?>) {
                        System.out.println("iterable");
                        for (final Object i : (Iterable<?>) component.eGet(f)) {
                            if (i instanceof Entity) {

                                final Node refNode = new GenericComponentProvider<>(this.graph)
                                        .createComponent((Entity) i);
                                try (Transaction tx = this.getGraph().beginTx()) {
                                    this.node.createRelationshipTo(refNode, PcmRelationshipType.REFERENCES);
                                    tx.success();
                                }
                            } else {
                                System.out.println(i + " should not happen!!!!!!!!!");
                            }
                        }

                    } else {
                        System.out.println("not iterable");
                        if (r instanceof Entity) {

                            final Node refNode = new GenericComponentProvider<>(this.graph).createComponent((Entity) r);
                            try (Transaction tx = this.getGraph().beginTx()) {
                                this.node.createRelationshipTo(refNode, PcmRelationshipType.REFERENCES);
                                tx.success();
                            }
                        } else {
                            System.out.println(r + " should not happen!!!!!!!!!");
                        }
                    }
                }
            }
            System.out.println(label + " has " + as + " attributes and " + rs + " references");

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
