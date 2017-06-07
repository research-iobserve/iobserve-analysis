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
import org.palladiosimulator.pcm.repository.CompositeDataType;
import org.palladiosimulator.pcm.repository.DataType;

/**
 *
 * @author Lars Bluemke
 *
 */
public class DataTypeProvider extends ModelProvider {

    private final HashMap<Node, EObject> subtypes;

    public DataTypeProvider(final GraphDatabaseService graph, final HashMap<String, DataType> dataTypes) {
        super(graph, dataTypes);
        this.subtypes = new HashMap<>();
    }

    @Override
    public EObject readComponent(final Node node) {
        boolean debug = false;

        /**
         * Get the node's data type from its label and instantiate a new empty object of this data
         * type
         */
        Label label;
        try (Transaction tx = this.getGraph().beginTx()) {
            label = ModelProviderUtil.getFirstLabel(node.getLabels());
            tx.success();
        }

        final EObject unfinishedComponent = this.subtypes.get(node);
        if (unfinishedComponent != null) {
            if (node.getId() == 646) {
                System.out.println(node + " " + node.getProperty(ModelProvider.ENTITY_NAME));
            }
            return unfinishedComponent;
        } else {
            final EObject component = ModelProviderUtil.instantiateEObject(label.name());

            /** Iterate over all attributes */
            try (Transaction tx = this.getGraph().beginTx()) {

                for (final EAttribute attr : component.eClass().getEAllAttributes()) {
                    // System.out.print("\t" + component + " attribute " + attr.getName() + " = ");
                    try {
                        final Object value = ModelProviderUtil.instantiateAttribute(
                                attr.getEAttributeType().getInstanceClass(),
                                node.getProperty(attr.getName()).toString());
                        // System.out.println(value);
                        if (value != null) {
                            component.eSet(attr, value);
                            if (node.getId() == 646) {
                                System.out.println(attr + " " + value);
                            }

                            if (value.equals("List_products"/* "list_productAmountTO" */)) {
                                debug = true;
                            }

                        }
                    } catch (final NotFoundException e) {
                        component.eSet(attr, null);
                    }
                }
                tx.success();

            }

            this.subtypes.put(node, component);

            /** Iterate over all references */
            for (final EReference ref : component.eClass().getEAllReferences()) {
                final String refName = ref.getName();
                Object refReprensation = component.eGet(ref);

                try (final Transaction tx = this.getGraph().beginTx()) {
                    /** Iterate over all outgoing containment relationships of the node */
                    for (final Relationship rel : node.getRelationships(Direction.OUTGOING)) {
                        /** If a relationship in the graph matches the references name... */
                        if (refName.equals(rel.getProperty(ModelProvider.REF_NAME))) {
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
                                if (debug) {
                                    System.out.println(node + " " + component + " " + refName + " " + refReprensation
                                            + " " + endNode);
                                }
                                String typeName;
                                if (ModelProviderUtil.getFirstLabel(endNode.getLabels()).name()
                                        .equals("PrimitiveDataType")) {
                                    typeName = (String) endNode.getProperty(ModelProvider.TYPE);
                                } else {
                                    typeName = (String) endNode.getProperty(ModelProvider.ENTITY_NAME);
                                }
                                refReprensation = this.getDataTypes().get(typeName);
                                if (debug) {
                                    System.out.println("refReprentation at 1 " + refReprensation + " : " + typeName);
                                }

                                if (refReprensation == null) {
                                    refReprensation = this.readComponent(endNode);
                                    this.getDataTypes().put(typeName, (DataType) refReprensation);
                                }
                                if (debug) {
                                    System.out.println("refReprentation at 2 " + refReprensation + " : " + typeName);
                                }

                                component.eSet(ref, refReprensation);

                                if (debug) {
                                    System.out.println("component.eget(ref) "
                                            + ((CompositeDataType) component.eGet(ref)).getEntityName());
                                }
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
