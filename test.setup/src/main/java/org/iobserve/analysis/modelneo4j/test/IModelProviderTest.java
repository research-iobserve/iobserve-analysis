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
package org.iobserve.analysis.modelneo4j.test;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;

/**
 * An interface which provides methods to test the methods of the {@link ModelProvider}.
 *
 * @author Lars Bluemke
 *
 */
public interface IModelProviderTest {

    /**
     * Clears the graph db for the next test.
     */
    @Before
    public void clearGraph();

    /**
     * Writes a model to the graph, clones the graph using
     * {@link ModelProvider#cloneNewGraphVersion(Class)}, reads the model from the cloned graph and
     * asserts that it is equal to the one written to the first graph.
     */
    @Test
    public void createThenCloneThenRead();

    /**
     * Writes a model to the graph, clears the graph using {@link ModelProvider#clearGraph()} and
     * asserts that the graph is empty afterwards.
     */
    @Test
    public void createThenClearGraph();

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#readOnlyComponentById(Class, String)} and asserts that it is equal to
     * the one written to the graph.
     */
    @Test
    public void createThenReadById();

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#readOnlyComponentByName(Class, String)} and asserts that it is equal to
     * the one written to the graph.
     */
    @Test
    public void createThenReadByName();

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#readComponentByType(Class)} and asserts that it is equal to the one
     * written to the graph.
     */
    @Test
    public void createThenReadByType();

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#readOnlyRootComponent(Class)} and asserts that it is equal to the one
     * written to the graph.
     */
    @Test
    public void createThenReadRoot();

    /**
     * Writes a model to the graph, reads the container of a certain model component from the graph
     * using {@link ModelProvider#readOnlyContainingComponentById(Class, String)} and asserts that
     * it is equal to the container from the original model.
     */
    @Test
    public void createThenReadContaining();

    /**
     * Writes a model to the graph, reads the components referencing to a certain component using
     * {@link ModelProvider#readOnlyReferencingComponentsById(Class, String)} and asserts that it is
     * equal to the referencing components from the original model.
     */
    @Test
    public void createThenReadReferencing();

    /**
     * Writes a model to the graph, modifies the original model, updates it in the graph using
     * {@link ModelProvider#updateComponent(Class, org.eclipse.emf.ecore.EObject)}, reads the
     * updated model from the graph and asserts that it is equal to the modified original model.
     */
    @Test
    public void createThenUpdateThenReadUpdated();

    /**
     * Writes a model to the graph, deletes it using
     * {@link ModelProvider#deleteComponent(Class, String)} and asserts that the graph is empty
     * afterwards.
     */
    @Test
    public void createThenDeleteComponent();

    /**
     * Writes a model to the graph, deletes it using
     * {@link ModelProvider#deleteComponentAndDatatypes(Class, String)} and asserts that the graph
     * is empty afterwards.
     */
    @Test
    public void createThenDeleteComponentAndDatatypes();

    /**
     * Checks whether the graph of a given {@link ModelProvider} is empty.
     *
     * @param modelProvider
     *            A model provider, containing a graph
     * @return True if the graph is empty, false otherwise
     */
    public static boolean isGraphEmpty(final ModelProvider<?> modelProvider) {
        boolean isEmpty;

        try (Transaction tx = modelProvider.getGraph().getGraphDatabaseService().beginTx()) {
            isEmpty = !modelProvider.getGraph().getGraphDatabaseService().getAllNodes().iterator().hasNext();
            tx.success();
        }

        return isEmpty;
    }

}