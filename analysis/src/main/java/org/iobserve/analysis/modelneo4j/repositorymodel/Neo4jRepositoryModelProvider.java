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
package org.iobserve.analysis.modelneo4j.repositorymodel;

import org.eclipse.emf.common.util.EList;
import org.neo4j.graphdb.GraphDatabaseService;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.impl.RepositoryFactoryImpl;

/**
 *
 * @author Lars Bluemke
 *
 */
public class Neo4jRepositoryModelProvider {
    private final GraphDatabaseService graph;
    private Repository repository;

    // private final RepositoryComponentProvider repositoryComponentProvider;
    // private final InterfaceProvider interfaceProvider;
    // private final FailureTypeProvider failureTypeProvider;
    // private final DataTypeProvider dataTypeProvider;

    public Neo4jRepositoryModelProvider(final GraphDatabaseService graph) {
        this.graph = graph;
    }

    public void saveRepository() {
        // create a node of type repository with attribute repositoryDescription

        // create a repository component provider for the repository components
        // create an interface provider for the interfaces
        // create a failure type provider for the failure types
        // create a data type provider for the data types
    }

    public Repository loadRepository() {
        this.repository = RepositoryFactoryImpl.init().createRepository();

        // get back the required information from the providers?

        // for (Interface i : interfaceProvider.loadInterfaces) {
        // ___this.repository.getInterfaces__Repository().add(i);
        // }

        // do the same for the other providers...

        return null;
    }

    public EList<Interface> getInterfaces() {
        // return this.interfaceProvider.getInterfaces()
        return null;
    }
}
