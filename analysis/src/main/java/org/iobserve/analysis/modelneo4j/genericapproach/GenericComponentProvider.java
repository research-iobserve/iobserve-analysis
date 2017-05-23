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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.iobserve.analysis.modelneo4j.PcmRelationshipType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

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

    public GenericComponentProvider(final GraphDatabaseService graph) {
        this.graph = graph;
    }

    public Node createComponent(final T component) {
        System.out.println("\n\n------------------------------------------------------------------------");
        System.out.println("component " + component);
        try (Transaction tx = this.getGraph().beginTx()) {
            final Label label = Label.label(component.eClass().getInstanceTypeName());
            Node thisNode = this.getGraph().findNode(label, GenericComponentProvider.ID,
                    component.eGet(component.eClass().getEIDAttribute()));

            if (thisNode == null) {
                thisNode = this.getGraph().createNode(label);

                int as = 0;
                int rs = 0;
                for (final EStructuralFeature f : component.eClass().getEAllStructuralFeatures()) {
                    if (f instanceof EAttribute) {
                        as++;
                        System.out.println("attibute " + ((EAttribute) f).getEAttributeType() + " key: " + f.getName()
                                + " value: " + component.eGet(f));

                        final Object a = component.eGet(f);
                        if (a != null) {
                            thisNode.setProperty(f.getName(), a.toString());
                        }
                    } else if (f instanceof EReference) {

                        rs++;
                        System.out.println(
                                "\n\n------------------------------------------------------------------------");
                        System.out.println("component " + component);
                        System.out.println(
                                "reference " + ((EReference) f).getEReferenceType() + " value: " + component.eGet(f));

                        final Object r = component.eGet(f);
                        if (r instanceof Iterable<?>) {
                            System.out.println("iterable");
                            for (final Object i : (Iterable<?>) component.eGet(f)) {
                                if (i instanceof EObject) {

                                    final Node refNode = new GenericComponentProvider<>(this.graph)
                                            .createComponent((EObject) i);
                                    thisNode.createRelationshipTo(refNode, PcmRelationshipType.REFERENCES);
                                } else {
                                    System.out.println(i + " should not happen!!!!!!!!!");
                                }
                            }

                        } else {
                            System.out.println("not iterable");
                            if (r instanceof EObject) {

                                final Node refNode = new GenericComponentProvider<>(this.graph)
                                        .createComponent((EObject) r);
                                thisNode.createRelationshipTo(refNode, PcmRelationshipType.REFERENCES);
                            } else {
                                System.out.println(r + " should not happen!!!!!!!!!");
                            }
                        }
                    }
                }
                System.out.println(label + " has " + as + " attributes and " + rs + " references");

                tx.success();
            }

            return thisNode;
        }
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
