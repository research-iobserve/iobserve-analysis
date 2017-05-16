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
package org.iobserve.analysis.modelneo4j.misc;

import org.eclipse.emf.common.util.EList;
import org.neo4j.graphdb.GraphDatabaseService;
import org.palladiosimulator.pcm.repository.Interface;

public class Neo4jInterfaceProvider {

    private final GraphDatabaseService graph;
    private final EList<Interface> interfaces;

    // private final Neo4jInterfaceProvider interfaceProvider;
    // private final Neo4jProtocolProvider protocolProvider;
    // private final Neo4jRequiredCharacterizationProvider;

    public Neo4jInterfaceProvider(final GraphDatabaseService graph, final EList<Interface> interfaces) {
        this.graph = graph;
        this.interfaces = interfaces;
    }

    public void saveInterfaces() {
        // for each interface in interfaces list

        // ---> create a node of type interface... (check if node has already been created as a
        // result of a relation)

        // add attributes to the node

        // ---> save all relations to interfaces (check if end node already exists otherwise create
        // it!)

        // ---> create a protocol provider for the protocols (check if there is already a node for
        // the current protocol)

        // ---> create a required characterizations provider for the required characterizations
        // (check if there is already a node for the current required characterization)

    }

    public EList<Interface> loadInterfaces() {
        // load interface nodes from database

        // create interfaces into interfaces list
        // RepositoryFactoryImpl.init().createRepository().getInterfaces__Repository().get(0).getProtocols__Interface().

        return this.interfaces;
    }
}
