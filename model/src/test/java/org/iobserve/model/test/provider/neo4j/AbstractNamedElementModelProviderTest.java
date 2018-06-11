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

import java.util.List;

import org.iobserve.model.provider.neo4j.Graph;
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

    /**
     * Writes a model to the graph, clones the graph using
     * {@link ModelProvider#cloneNewGraphVersion(Class)}, reads the model from the cloned graph and
     * asserts that it is equal to the one written to the first graph.
     */
    @Test
    public final void createThenCloneThenRead() {
        final Graph storeGraph = this.prepareGraph("createThenCloneThenRead");

        final ModelProvider<T> storeModelProvider = new ModelProvider<>(storeGraph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        storeModelProvider.storeModelPartition(this.testModel);

        final Graph cloneGraph = storeModelProvider.cloneNewGraphVersion(this.factory);

        final ModelProvider<T> cloneModelProvider = new ModelProvider<>(cloneGraph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        final T clonedModel = cloneModelProvider.readRootNode(this.clazz);
        cloneGraph.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, clonedModel));
    }

    /**
     * Writes a model to the graph, clears the graph using {@link ModelProvider#clearGraph()} and
     * asserts that the graph is empty afterwards.
     */
    @Test
    public final void createThenClearGraph() {
        final Graph graph = this.prepareGraph("createThenClearGraph");

        final ModelProvider<T> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(this.isGraphEmpty(modelProvider));
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#readObjectsByName(Class, String)} and asserts that it is equal to the
     * one written to the graph.
     */
    @Test
    public final void createThenReadByName() {
        final Graph graph = this.prepareGraph("createThenClearGraph");

        final ModelProvider<T> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final List<T> readModels = modelProvider.readObjectsByName(this.clazz, this.testModel.getEntityName());

        for (final T readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
        }
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#readRootNode(Class)} and asserts that it is equal to the one written to
     * the graph.
     */
    @Test
    public final void createThenReadRoot() {
        final Graph graph = this.prepareGraph("createThenClearGraph");

        final ModelProvider<T> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final T readModel = modelProvider.readRootNode(this.clazz);

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

}
