/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.persistence.neo4j;

import org.eclipse.emf.ecore.EObject;
import org.neo4j.graphdb.GraphDatabaseService;

/**
 * @author Reiner Jung
 *
 * @param <R>
 *            root class type
 */
public class GenericModelFacility<R extends EObject> {

    protected final GraphDatabaseService graphDatabaseService;

    protected final Neo4JModelResource<R> resource;

    /**
     * Create an abstract model facility.
     *
     * @param resource
     *            the corresponding model resource
     * @param graphDatabaseService
     *            database for the model
     */
    public GenericModelFacility(final Neo4JModelResource<R> resource, final GraphDatabaseService graphDatabaseService) {
        this.resource = resource;
        this.graphDatabaseService = graphDatabaseService;
    }

}
