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
package org.iobserve.model.test.provider.neo4j;

import org.iobserve.model.provider.neo4j.ModelGraph;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.junit.Assert;
import org.junit.Test;
import org.palladiosimulator.pcm.core.entity.NamedElement;

/**
 * @author Reiner Jung
 *
 * @param <T>
 *            a named element subtype typed model
 */
public abstract class AbstractNamedElementModelProviderTest<T extends NamedElement>
        extends AbstractModelProviderTest<T> {

    public static final String CREATE_THEN_CLONE_THEN_READ = "createThenCloneThenRead";
    public static final String CREATE_THEN_CLEAR_GRAPH = "createThenClearGraph";
    public static final String CREATE_THEN_READ_ROOT = "createThenReadRoot";

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#collectAllObjectIdsByType(Class)} and asserts that it is equal to the
     * one written to the graph.
     */
    @Test
    abstract void createThenReadByType();

    /**
     * Writes a model to the graph, reads the container of a certain model component from the graph
     * using {@link ModelProvider#readOnlyContainingComponentById(Class, String)} and asserts that
     * it is equal to the container from the original model.
     */
    @Test
    abstract void createThenReadContaining();

    /**
     * Writes a model to the graph, reads the components referencing to a certain component using
     * {@link ModelProvider#collectReferencingObjectsByTypeAndId(Class, String)} and asserts that it
     * is equal to the referencing components from the original model.
     */
    @Test
    abstract void createThenReadReferencing();

    /**
     * Writes a model to the graph, modifies the original model, updates it in the graph using
     * {@link ModelProvider#updateObject(Class, org.eclipse.emf.ecore.EObject)}, reads the updated
     * model from the graph and asserts that it is equal to the modified original model.
     */
    @Test
    abstract void createThenUpdateThenReadUpdated();

    /**
     * Create, store and delete objects.
     */
    @Test
    abstract void createThenDeleteObject();

    /**
     * Create, store and delete objects and data types.
     */
    @Test
    abstract void createThenDeleteObjectAndDatatypes();

    /**
     * Writes a model to the graph, clones the graph using
     * {@link ModelProvider#cloneNewGraphVersion(Class)}, reads the model from the cloned graph and
     * asserts that it is equal to the one written to the first graph.
     */
    @Test
    public final void createThenCloneThenRead() {
        final ModelGraph storeGraph = this
                .prepareGraph(AbstractNamedElementModelProviderTest.CREATE_THEN_CLONE_THEN_READ);

        final ModelProvider<T> storeModelProvider = new ModelProvider<>(storeGraph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        storeModelProvider.storeModelPartition(this.testModel);

        final ModelGraph cloneGraph = storeModelProvider.cloneNewGraphVersion(this.factory);

        final ModelProvider<T> cloneModelProvider = new ModelProvider<>(cloneGraph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        final T clonedModel = cloneModelProvider.getModelRootNode(this.clazz);
        cloneGraph.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, clonedModel));

        storeGraph.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, clears the graph using {@link ModelProvider#clearGraph()} and
     * asserts that the graph is empty afterwards.
     */
    @Test
    public final void createThenClearGraph() {
        final ModelGraph graph = this.prepareGraph(AbstractNamedElementModelProviderTest.CREATE_THEN_CLEAR_GRAPH);

        final ModelProvider<T> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(this.isGraphEmpty(modelProvider));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#getModelRootNode(Class)} and asserts that it is equal to the one written
     * to the graph.
     */
    @Test
    public final void createThenReadRoot() {
        final ModelGraph graph = this.prepareGraph(AbstractNamedElementModelProviderTest.CREATE_THEN_READ_ROOT);

        final ModelProvider<T> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final T readModel = modelProvider.getModelRootNode(this.clazz);

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));

        graph.getGraphDatabaseService().shutdown();
    }

}
