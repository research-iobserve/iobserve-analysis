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
import org.neo4j.graphdb.Transaction;

/**
 *
 * @author Lars Bluemke
 *
 */
public class DataTypeProvider extends GenericComponentProvider<EObject> {

    private final HashMap<String, EObject> modelTypes;
    private final List<Node> subtypes;

    public DataTypeProvider(final GraphDatabaseService graph, final HashMap<String, EObject> modelTypes) {
        super(graph);
        this.modelTypes = modelTypes;
        this.subtypes = new LinkedList<>();
    }

    public DataTypeProvider(final GraphDatabaseService graph, final HashMap<String, EObject> modelTypes,
            final List<Node> subtypes) {
        super(graph);
        this.modelTypes = modelTypes;
        this.subtypes = subtypes;
    }

    @Override
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

        final EObject component = this.instantiateEObject(label.name());

        if (this.subtypes.contains(node)) {
            // //System.out.println("Circle closed");
            return component;
        } else {

            this.subtypes.add(node);

            /** Iterate over all attributes */
            try (Transaction tx = this.getGraph().beginTx()) {

                for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                    // System.out.print("\t" + component + " attribute " + attr.getName() + " = ");
                    try {
                        final Object value = this.instantiateAttribute(attr.getEAttributeType().getInstanceClass(),
                                node.getProperty(attr.getName()).toString());
                        // System.out.println(value);
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
                Object refReprensation = component.eGet(ref);

                try (final Transaction tx = this.getGraph().beginTx()) {
                    /** Iterate over all outgoing containment relationships of the node */
                    for (final Relationship rel : node.getRelationships(Direction.OUTGOING)) {
                        /** If a relationship in the graph matches the references name... */
                        if (refName.equals(rel.getProperty(GenericComponentProvider.REF_NAME))) {
                            final Node endNode = rel.getEndNode();

                            /** Only create a new object for containments */
                            if (rel.isType(PcmRelationshipType.CONTAINS)) {
                                /** ...recursively create an instance of the referenced object */
                                if (refReprensation instanceof EList<?>) {
                                    final EObject endComponent = this.readComponent(endNode);
                                    ((EList<EObject>) refReprensation).add(endComponent);
                                } else {
                                    refReprensation = this.readComponent(endNode);
                                    component.eSet(ref, refReprensation);
                                }
                            } else if (rel.isType(PcmRelationshipType.IS_TYPE)) {
                                String typeName;
                                if (this.getFirstLabel(endNode.getLabels()).name().equals("PrimitiveDataType")) {
                                    typeName = (String) endNode.getProperty(GenericComponentProvider.TYPE);
                                } else {
                                    typeName = (String) endNode.getProperty(GenericComponentProvider.ENTITY_NAME);
                                }
                                refReprensation = this.modelTypes.get(typeName);

                                if (refReprensation == null) {
                                    refReprensation = this.readComponent(endNode);
                                    this.modelTypes.put(typeName, (EObject) refReprensation);
                                }

                                component.eSet(ref, refReprensation);
                            }
                        }
                    }
                    tx.success();
                }
            }

            return component;
        }
    }
}
