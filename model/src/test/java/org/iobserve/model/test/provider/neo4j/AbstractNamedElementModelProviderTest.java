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

import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.ModelProviderUtil;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.persistence.neo4j.NodeLookupException;
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
     *
     * @throws DBException
     */
    protected abstract void createThenReadByType() throws DBException;

    /**
     * Writes a model to the graph, reads the container of a certain model component from the graph
     * using {@link ModelProvider#readContainingObjectById(Class, String)} and asserts that it is
     * equal to the container from the original model.
     *
     * @throws DBException
     */
    protected abstract void createThenReadContaining() throws DBException;

    /**
     * Writes a model to the graph, reads the components referencing to a certain component using
     * {@link ModelProvider#collectReferencingObjectsByTypeAndId(Class, String)} and asserts that it
     * is equal to the referencing components from the original model.
     *
     * @throws DBException
     */
    @Test
    abstract void createThenReadReferencing() throws DBException;

    /**
     * Writes a model to the graph, modifies the original model, updates it in the graph using
     * {@link ModelProvider#updatePartition(Class, org.eclipse.emf.ecore.EObject)}, reads the
     * updated model from the graph and asserts that it is equal to the modified original model.
     *
     * @throws NodeLookupException
     * @throws DBException
     */
    @Test
    abstract void createThenUpdateThenReadUpdated() throws NodeLookupException, DBException;

    /**
     * Create, store and delete objects.
     *
     * @throws DBException
     */
    @Test
    abstract void createThenDeleteObject() throws DBException;

    /**
     * Create, store and delete objects and data types.
     * 
     * @throws DBException
     */
    @Test
    abstract void createThenDeleteObjectAndDatatypes() throws DBException;

    /**
     * Writes a model to the graph, clones the graph using
     * {@link ModelProvider#cloneNewGraphVersion(Class)}, reads the model from the cloned graph and
     * asserts that it is equal to the one written to the first graph.
     *
     * @throws DBException
     */
    @Test
    public final void createThenCloneThenRead() throws DBException {
        final ModelResource<T> storeResource = ModelProviderTestUtils.prepareResource(
                AbstractNamedElementModelProviderTest.CREATE_THEN_CLONE_THEN_READ, this.prefix, this.ePackage);

        storeResource.storeModelPartition(this.testModel);

        final ModelResource<T> newRevisionResource = ModelProviderUtil.createNewModelResourceVersion(this.ePackage,
                storeResource);

        final T clonedModel = newRevisionResource.getModelRootNode(this.clazz, this.eClass);
        newRevisionResource.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, clonedModel, clonedModel.eClass()));

        storeResource.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, clears the graph using {@link ModelProvider#clearResource()} and
     * asserts that the graph is empty afterwards.
     *
     * @throws DBException
     */
    @Test
    public final void createThenClearResource() throws DBException {
        final ModelResource<T> resource = ModelProviderTestUtils.prepareResource(
                AbstractNamedElementModelProviderTest.CREATE_THEN_CLEAR_GRAPH, this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        Assert.assertFalse(ModelProviderTestUtils.isResourceEmpty(resource));

        resource.clearResource();

        Assert.assertTrue(ModelProviderTestUtils.isResourceEmpty(resource));

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Writes a model to the graph, reads it from the graph using
     * {@link ModelProvider#getModelRootNode(Class)} and asserts that it is equal to the one written
     * to the graph.
     *
     * @throws DBException
     */
    @Test
    public final void createThenReadRoot() throws DBException {
        final ModelResource<T> resource = ModelProviderTestUtils.prepareResource(
                AbstractNamedElementModelProviderTest.CREATE_THEN_READ_ROOT, this.prefix, this.ePackage);

        resource.storeModelPartition(this.testModel);

        final T readModel = resource.getModelRootNode(this.clazz, this.eClass);

        Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));

        resource.getGraphDatabaseService().shutdown();
    }

}
