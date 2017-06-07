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
import org.palladiosimulator.pcm.repository.DataType;

/**
 *
 * @author Lars Bluemke
 *
 */
public class DataTypeProvider extends ModelProvider {

    /** Map of a datatype's subnodes + subtypes to detect circles */
    private final HashMap<Node, EObject> subtypes;

    public DataTypeProvider(final GraphDatabaseService graph, final HashMap<String, DataType> dataTypes) {
        super(graph, dataTypes);
        this.subtypes = new HashMap<>();
    }

    @Override
    public EObject readComponent(final Node node) {

        // Get the node's data type name...
        Label label;
        try (Transaction tx = this.getGraph().beginTx()) {
            label = ModelProviderUtil.getFirstLabel(node.getLabels());
            tx.success();
        }

        // Look if there has already been created an object for this node
        final EObject unfinishedComponent = this.subtypes.get(node);

        if (unfinishedComponent != null) {
            return unfinishedComponent;
        } else {
            // ...and instantiate a new empty object of this data type
            final EObject component = ModelProviderUtil.instantiateEObject(label.name());

            try (Transaction tx = this.getGraph().beginTx()) {

                // Iterate over all attributes and get their values from the node
                for (final EAttribute attr : component.eClass().getEAllAttributes()) {

                    try {
                        final Object value = ModelProviderUtil.instantiateAttribute(
                                attr.getEAttributeType().getInstanceClass(),
                                node.getProperty(attr.getName()).toString());

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

            // Add the unfinished component
            this.subtypes.put(node, component);

            // Iterate over all references...
            for (final EReference ref : component.eClass().getEAllReferences()) {
                final String refName = ref.getName();
                Object refReprensation = component.eGet(ref);

                try (final Transaction tx = this.getGraph().beginTx()) {

                    // ...and iterate over all outgoing containment or type relationships of the
                    // node */
                    for (final Relationship rel : node.getRelationships(Direction.OUTGOING,
                            PcmRelationshipType.CONTAINS, PcmRelationshipType.IS_TYPE)) {

                        // If a relationship in the graph matches the references name...
                        if (refName.equals(rel.getProperty(ModelProvider.REF_NAME))) {
                            final Node endNode = rel.getEndNode();

                            // ...recursively create an instance of the referenced object
                            if (rel.isType(PcmRelationshipType.CONTAINS)) {

                                if (refReprensation instanceof EList<?>) {
                                    final EObject endComponent = this.readComponent(endNode);
                                    ((EList<EObject>) refReprensation).add(endComponent);
                                } else {
                                    refReprensation = this.readComponent(endNode);
                                    component.eSet(ref, refReprensation);
                                }
                            } else if (rel.isType(PcmRelationshipType.IS_TYPE)) {
                                String typeName;

                                if (ModelProviderUtil.getFirstLabel(endNode.getLabels()).name()
                                        .equals("PrimitiveDataType")) {
                                    typeName = (String) endNode.getProperty(ModelProvider.TYPE);
                                } else {
                                    typeName = (String) endNode.getProperty(ModelProvider.ENTITY_NAME);
                                }

                                // Look if this data type has already been created
                                refReprensation = this.getDataTypes().get(typeName);

                                if (refReprensation == null) {
                                    refReprensation = this.readComponent(endNode);
                                    this.getDataTypes().put(typeName, (DataType) refReprensation);
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
