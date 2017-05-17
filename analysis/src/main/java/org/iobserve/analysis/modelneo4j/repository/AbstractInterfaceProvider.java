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
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
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

    private Label interfaceLabel;

    public Label getInterfaceLabel() {
        return this.interfaceLabel;
    }

    @Override
    public Node createComponent(final Interface component) {

        try (Transaction tx = this.getGraph().beginTx()) {
            Node inode = this.getGraph().findNode(this.interfaceLabel, AbstractPcmComponentProvider.ID,
                    component.getId());
            if (inode == null) {
                inode = this.getGraph().createNode(this.interfaceLabel);
                inode.setProperty(AbstractPcmComponentProvider.ID, component.getId());
                inode.setProperty(AbstractPcmComponentProvider.ENTITY_NAME, component.getEntityName());

                final Node rnode = this.getGraph().findNode(Label.label("Repository"), AbstractPcmComponentProvider.ID,
                        component.getRepository__Interface().getId());
                rnode.createRelationshipTo(inode, PcmRelationshipType.CONTAINS);

                final AbstractInterfaceProvider iiProvider = new InfrastructureInterfaceProvider(this.getGraph());
                final AbstractInterfaceProvider oiProvider = new OperationInterfaceProvider(this.getGraph());

                for (final Interface i : component.getParentInterfaces__Interface()) {
                    if (i instanceof InfrastructureInterface) {
                        final Node inode2 = iiProvider.createComponent(i);
                        inode.createRelationshipTo(inode2, PcmRelationshipType.PARENT_INTERFACE);
                    } else if (i instanceof OperationInterface) {
                        final Node inode2 = oiProvider.createComponent(i);
                        inode.createRelationshipTo(inode2, PcmRelationshipType.PARENT_INTERFACE);
                    }
                }

                for (final Protocol p : component.getProtocols__Interface()) {
                    new ProtocolProvider(this.getGraph()).createComponent(p);
                }
            }

            tx.success();

            return inode;
        }

    }

    @Override
    public void updateComponent(final Interface component) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteComponent(final Interface component) {
        // TODO Auto-generated method stub

    }

    public void setInterfaceLabel(final Label interfaceLabel) {
        this.interfaceLabel = interfaceLabel;
    }

    public AbstractInterfaceProvider(final GraphDatabaseService graph) {
        super(graph);
    }
}
