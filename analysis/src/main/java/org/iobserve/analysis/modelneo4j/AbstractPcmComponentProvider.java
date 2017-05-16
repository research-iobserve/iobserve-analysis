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
package org.iobserve.analysis.modelneo4j;

import org.eclipse.emf.ecore.EObject;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

/**
 *
 * @author Lars Bluemke
 *
 * @param <T>
 */
public abstract class AbstractPcmComponentProvider<T extends EObject> {

    public static final String ID = "id";
    public static final String ENTITY_NAME = "entityName";

    private final GraphDatabaseService graph;
    private final T component;

    public AbstractPcmComponentProvider(final GraphDatabaseService graph, final T component) {
        this.graph = graph;
        this.component = component;
    }

    public abstract Node createComponent();

    public abstract T readComponent();

    public abstract void updateComponent(T component);

    public abstract void deleteComponent();

    public T getComponent() {
        return this.component;
    }

    public GraphDatabaseService getGraph() {
        return this.graph;
    }
}
