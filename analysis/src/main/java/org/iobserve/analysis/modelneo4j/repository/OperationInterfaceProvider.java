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
package org.iobserve.analysis.modelneo4j.repository;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.RepositoryFactory;

/**
 *
 * @author Lars Bluemke
 *
 */
public class OperationInterfaceProvider extends AbstractInterfaceProvider {

    public OperationInterfaceProvider(final GraphDatabaseService graph) {
        super(graph);
        this.setInterfaceLabel(Label.label("OperationInterface"));
    }

    @Override
    protected Interface getInfrastructureOrOperationInterface() {
        return RepositoryFactory.eINSTANCE.createOperationInterface();
    }

    // @Override
    // public Interface readComponent(final String entityName) {
    // try (Transaction tx = this.getGraph().beginTx()) {
    // final OperationInterface inter = RepositoryFactory.eINSTANCE.createOperationInterface();
    // final AbstractInterfaceProvider iiProvider = new
    // InfrastructureInterfaceProvider(this.getGraph());
    // final AbstractInterfaceProvider oiProvider = new OperationInterfaceProvider(this.getGraph());
    // final ProtocolProvider pProvider = new ProtocolProvider(this.getGraph());
    // final Node iNode = this.getGraph().findNode(this.getInterfaceLabel(),
    // AbstractPcmComponentProvider.ENTITY_NAME, entityName);
    //
    // inter.setId(iNode.getProperty(AbstractPcmComponentProvider.ID).toString());
    // inter.setEntityName(iNode.getProperty(AbstractPcmComponentProvider.ENTITY_NAME).toString());
    //
    // Node eNode;
    // for (final Relationship r : iNode.getRelationships(Direction.OUTGOING,
    // PcmRelationshipType.PARENT_INTERFACE)) {
    // eNode = r.getEndNode();
    //
    // if (eNode.hasLabel(Label.label("InfrastructureInterface"))) {
    // final Interface i = iiProvider
    // .readComponent(eNode.getProperty(AbstractPcmComponentProvider.ENTITY_NAME).toString());
    // inter.getParentInterfaces__Interface().add(i);
    // } else if (eNode.hasLabel(Label.label("OperationInterface"))) {
    // final Interface i = oiProvider
    // .readComponent(eNode.getProperty(AbstractPcmComponentProvider.ENTITY_NAME).toString());
    // inter.getParentInterfaces__Interface().add(i);
    // }
    // }
    //
    // for (final Relationship r : iNode.getRelationships(Direction.OUTGOING,
    // PcmRelationshipType.PROTOCOL)) {
    // eNode = r.getEndNode();
    //
    // final Protocol p = pProvider
    // .readComponent(eNode.getProperty(AbstractPcmComponentProvider.ENTITY_NAME).toString());
    // inter.getProtocols__Interface().add(p);
    // }
    // tx.success();
    //
    // return inter;
    // }
    // }

}
