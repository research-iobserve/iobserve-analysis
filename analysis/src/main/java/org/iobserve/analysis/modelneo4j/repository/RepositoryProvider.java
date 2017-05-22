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
import org.palladiosimulator.pcm.repository.InfrastructureInterface;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryFactory;

/**
 *
 * @author Lars Bluemke
 *
 */
public class RepositoryProvider extends AbstractPcmComponentProvider<Repository> {

    public RepositoryProvider(final GraphDatabaseService graph) {
        super(graph);
    }

    @Override
    public Node createComponent(final Repository component) {
        try (Transaction tx = this.getGraph().beginTx()) {
            final Node rnode = this.getGraph().createNode(Label.label("Repository"));
            rnode.setProperty(AbstractPcmComponentProvider.ID, component.getId());
            rnode.setProperty(AbstractPcmComponentProvider.ENTITY_NAME, component.getEntityName());

            final String repositoryDecription = component.getRepositoryDescription();
            if (repositoryDecription != null) {
                rnode.setProperty("repositoryDescription", repositoryDecription);
            }

            final AbstractInterfaceProvider iiProvider = new InfrastructureInterfaceProvider(this.getGraph());
            final AbstractInterfaceProvider oiProvider = new OperationInterfaceProvider(this.getGraph());

            Node inode;
            for (final Interface i : component.getInterfaces__Repository()) {
                if (i instanceof InfrastructureInterface) {
                    inode = iiProvider.createComponent(i);
                    rnode.createRelationshipTo(inode, PcmRelationshipType.CONTAINS);
                } else if (i instanceof OperationInterface) {
                    inode = oiProvider.createComponent(i);
                    rnode.createRelationshipTo(inode, PcmRelationshipType.CONTAINS);
                }

            }
            tx.success();

            return rnode;
        }
    }

    @Override
    public Repository readComponent(final String entityName) {
        final Repository repo = RepositoryFactory.eINSTANCE.createRepository();

        try (Transaction tx = this.getGraph().beginTx()) {
            final Node rnode = this.getGraph().findNode(Label.label("Repository"),
                    AbstractPcmComponentProvider.ENTITY_NAME, entityName);
            final AbstractInterfaceProvider iiProvider = new InfrastructureInterfaceProvider(this.getGraph());
            final AbstractInterfaceProvider oiProvider = new OperationInterfaceProvider(this.getGraph());

            for (final Relationship r : rnode.getRelationships(Direction.OUTGOING, PcmRelationshipType.CONTAINS)) {
                final Node enode = r.getEndNode();

                if (enode.hasLabel(Label.label("InfrastructureInterface"))) {
                    final Interface i = iiProvider
                            .readComponent(enode.getProperty(AbstractPcmComponentProvider.ENTITY_NAME).toString());
                    repo.getInterfaces__Repository().add(i);
                } else if (enode.hasLabel(Label.label("OperationInterface"))) {
                    System.out.println("DEBUG " + enode.getProperty(AbstractPcmComponentProvider.ENTITY_NAME));
                    final Interface i = oiProvider
                            .readComponent(enode.getProperty(AbstractPcmComponentProvider.ENTITY_NAME).toString());
                    repo.getInterfaces__Repository().add(i);
                }

            }
            tx.success();
        }

        return repo;
    }

    @Override
    public void updateComponent(final Repository component) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteComponent(final Repository component) {
        // TODO Auto-generated method stub

    }

}
