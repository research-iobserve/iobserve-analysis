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
import org.palladiosimulator.pcm.core.entity.Entity;

/**
 * An abstract test class which provides methods to test the methods of the {@link ModelProvider}.
 *
 * @author Lars Bluemke
 * @author Reiner Jung -- restructuring
 *
 * @param <T>
 *            root class
 */
public abstract class AbstractEnityModelProviderTest<T extends Entity>
        extends AbstractNamedElementModelProviderTest<T> {

    public static final String CREATE_THEN_DELETE_COMPONENT_AND_DATATYPES = "createThenDeleteComponentAndDatatypes";
    public static final String CREATE_THEN_READ_BY_ID = "crateThenReadById";

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#readObjectById(Class, String)} and asserts that it is equal to the one
     * written to the graph.
     */
    @Test
    public final void createThenReadById() {
        final ModelGraph graph = this.prepareGraph(AbstractEnityModelProviderTest.CREATE_THEN_READ_BY_ID);

        final ModelProvider<T> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        final T readModel = modelProvider.readObjectById(this.clazz, this.testModel.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, deletes it using
     * {@link ModelProvider#deleteComponentAndDatatypes(Class, String)} and asserts that the graph
     * is empty afterwards.
     */
    @Test
    public final void createThenDeleteComponentAndDatatypes() {
        final ModelGraph graph = this
                .prepareGraph(AbstractEnityModelProviderTest.CREATE_THEN_DELETE_COMPONENT_AND_DATATYPES);

        final ModelProvider<T> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        modelProvider.deleteObjectByIdAndDatatypes(this.clazz, this.testModel.getId(), true);

        Assert.assertTrue(this.isGraphEmpty(modelProvider));

        graph.getGraphDatabaseService().shutdown();
    }

}
