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
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.Repository;

/**
 *
 * @author Lars Bluemke
 *
 */
public class RepositoryProvider extends AbstractPcmComponentProvider<Repository> {

    public RepositoryProvider(final GraphDatabaseService graph, final Repository component) {
        super(graph, component);
    }

    @Override
    public Node createComponent() {
        try (Transaction tx = this.getGraph().beginTx()) {
            final Node rnode = this.getGraph().createNode(Label.label("Repository"));
            rnode.setProperty(AbstractPcmComponentProvider.ID, this.getComponent().getId());
            rnode.setProperty(AbstractPcmComponentProvider.ENTITY_NAME, this.getComponent().getEntityName());

            final String repositoryDecription = this.getComponent().getRepositoryDescription();
            if (repositoryDecription != null) {
                rnode.setProperty("repositoryDescription", repositoryDecription);
            }

            for (final Interface i : this.getComponent().getInterfaces__Repository()) {
                new InterfaceProvider(this.getGraph(), i).createComponent();
            }
            tx.success();

            return rnode;
        }
    }

    @Override
    public Repository readComponent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateComponent(final Repository component) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteComponent() {
        // TODO Auto-generated method stub

    }

}
