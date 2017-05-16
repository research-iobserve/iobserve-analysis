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
import org.palladiosimulator.pcm.protocol.Protocol;

/**
 *
 * @author Lars Bluemke
 *
 */
public class ProtocolProvider extends AbstractPcmComponentProvider<Protocol> {

    public ProtocolProvider(final GraphDatabaseService graph, final Protocol component) {
        super(graph, component);
    }

    @Override
    public Node createComponent() {
        try (Transaction tx = this.getGraph().beginTx()) {
            final Node pnode = this.getGraph().createNode(Label.label("Protocol"));
            pnode.setProperty("protocolTypeID", this.getComponent().getProtocolTypeID());
            tx.success();

            return pnode;
        }
    }

    @Override
    public Protocol readComponent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void updateComponent(final Protocol component) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteComponent() {
        // TODO Auto-generated method stub

    }

}
