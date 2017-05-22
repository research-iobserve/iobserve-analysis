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

import org.iobserve.analysis.modelneo4j.AbstractPcmComponentProvider;
import org.iobserve.analysis.modelneo4j.PcmRelationshipType;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.protocol.Protocol;
import org.palladiosimulator.pcm.repository.InfrastructureInterface;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;

/**
 *
 * @author Lars Bluemke
 *
 */
public abstract class AbstractInterfaceProvider extends AbstractPcmComponentProvider<Interface> {

    public AbstractInterfaceProvider(final GraphDatabaseService graph) {
        super(graph);
    }

    private Label interfaceLabel;

    @Override
    public Node createComponent(final Interface component) {

        try (Transaction tx = this.getGraph().beginTx()) {
            Node inode = this.getGraph().findNode(this.interfaceLabel, AbstractPcmComponentProvider.ID,
                    component.getId());
            if (inode == null) {
                inode = this.getGraph().createNode(this.interfaceLabel);
                inode.setProperty(AbstractPcmComponentProvider.ID, component.getId());
                inode.setProperty(AbstractPcmComponentProvider.ENTITY_NAME, component.getEntityName());

                final AbstractInterfaceProvider iiProvider = new InfrastructureInterfaceProvider(this.getGraph());
                final AbstractInterfaceProvider oiProvider = new OperationInterfaceProvider(this.getGraph());

                for (final Interface i : component.getParentInterfaces__Interface()) {
                    if (i instanceof InfrastructureInterface) {
                        final Node inode2 = iiProvider.createComponent(i);
                        inode.createRelationshipTo(inode2, PcmRelationshipType.REFERENCES);
                    } else if (i instanceof OperationInterface) {
                        final Node inode2 = oiProvider.createComponent(i);
                        inode.createRelationshipTo(inode2, PcmRelationshipType.REFERENCES);
                    }
                }

                for (final Protocol p : component.getProtocols__Interface()) {
                    // TODO create relationship!
                    new ProtocolProvider(this.getGraph()).createComponent(p);
                }
            }

            tx.success();

            return inode;
        }

    }

    @Override
    public Interface readComponent(final String id) {
        try (Transaction tx = this.getGraph().beginTx()) {
            final Interface inter = this.getInfrastructureOrOperationInterface();
            final AbstractInterfaceProvider iiProvider = new InfrastructureInterfaceProvider(this.getGraph());
            final AbstractInterfaceProvider oiProvider = new OperationInterfaceProvider(this.getGraph());
            final ProtocolProvider pProvider = new ProtocolProvider(this.getGraph());
            final Node iNode = this.getGraph().findNode(this.getInterfaceLabel(), AbstractPcmComponentProvider.ID, id);

            inter.setId(iNode.getProperty(AbstractPcmComponentProvider.ID).toString());
            inter.setEntityName(iNode.getProperty(AbstractPcmComponentProvider.ENTITY_NAME).toString());

            Node eNode;
            for (final Relationship r : iNode.getRelationships(Direction.OUTGOING, PcmRelationshipType.REFERENCES)) {
                eNode = r.getEndNode();

                if (eNode.hasLabel(Label.label("InfrastructureInterface"))) {
                    final Interface i = iiProvider
                            .readComponent(eNode.getProperty(AbstractPcmComponentProvider.ID).toString());
                    inter.getParentInterfaces__Interface().add(i);
                } else if (eNode.hasLabel(Label.label("OperationInterface"))) {
                    final Interface i = oiProvider
                            .readComponent(eNode.getProperty(AbstractPcmComponentProvider.ID).toString());
                    inter.getParentInterfaces__Interface().add(i);
                } else if (eNode.hasLabel(Label.label("Protocol"))) {
                    final Protocol p = pProvider
                            .readComponent(eNode.getProperty(AbstractPcmComponentProvider.ID).toString());
                    inter.getProtocols__Interface().add(p);
                }
            }
            tx.success();

            return inter;
        }
    }

    protected abstract Interface getInfrastructureOrOperationInterface();

    @Override
    public void updateComponent(final Interface component) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteComponent(final Interface component) {
        // TODO Auto-generated method stub

    }

    public Label getInterfaceLabel() {
        return this.interfaceLabel;
    }

    protected void setInterfaceLabel(final Label interfaceLabel) {
        this.interfaceLabel = interfaceLabel;
    }

}
