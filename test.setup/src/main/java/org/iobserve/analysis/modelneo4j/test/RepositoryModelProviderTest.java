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

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.modelneo4j.Graph;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.palladiosimulator.pcm.core.entity.Entity;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 *
 * @author Lars Bluemke
 *
 */
public class RepositoryModelProviderTest implements IModelProviderTest {
    private static final File GRAPH_DIR = new File("/Users/LarsBlumke/Desktop/neo4jDb");
    private static final Graph GRAPH = new Graph(RepositoryModelProviderTest.GRAPH_DIR);

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();
    private Repository model = TestModelBuilder.createReposiory();

    @Override
    @Before
    public void clearGraph() {
        new ModelProvider<>(RepositoryModelProviderTest.GRAPH).clearGraph();
    }

    @Before
    public void createModel() {
        this.model = TestModelBuilder.createReposiory();
    }

    @Override
    @Test
    public void createThenReadById() {
        final Repository readModel;
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);
        readModel = modelProvider.readOnlyComponentById(Repository.class, this.model.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.model, readModel));
    }

    @Override
    @Test
    public void createThenReadByName() {
        final List<Repository> readModels;
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);
        readModels = modelProvider.readOnlyComponentByName(Repository.class, this.model.getEntityName());

        for (final Repository readModel : readModels) {
            Assert.assertTrue(this.equalityHelper.equals(this.model, readModel));
        }
    }

    @Override
    @Test
    public void createThenReadByType() {
        final List<String> readIds;
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);
        readIds = modelProvider.readComponentByType(Repository.class);

        for (final String readId : readIds) {
            Assert.assertTrue(this.model.getId().equals(readId));
        }

    }

    @Override
    @Test
    public void createThenReadRoot() {
        final Repository readModel;
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);
        readModel = modelProvider.readOnlyRootComponent(Repository.class);

        Assert.assertTrue(this.equalityHelper.equals(this.model, readModel));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final Repository readModel;
        final OperationInterface inter = (OperationInterface) this.model.getInterfaces__Repository().get(0);
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        modelProvider.createComponent(this.model);
        readModel = (Repository) modelProvider.readOnlyContainingComponentById(OperationInterface.class, inter.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.model, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final Map<String, EObject> expectedObjects = new HashMap<>();
        final List<EObject> readObjects;
        final ModelProvider<Repository> modelProvider = new ModelProvider<>(RepositoryModelProviderTest.GRAPH);

        // Only components, interfaces and data types reference back the repository in our test
        // model
        for (final RepositoryComponent o : this.model.getComponents__Repository()) {
            expectedObjects.put(o.getId(), o);
        }
        for (final Interface o : this.model.getInterfaces__Repository()) {
            expectedObjects.put(o.getId(), o);
        }
        for (final DataType o : this.model.getDataTypes__Repository()) {
            expectedObjects.put(o.toString(), o);
        }

        modelProvider.createComponent(this.model);
        readObjects = modelProvider.readReferencingComponentsById(Repository.class, this.model.getId());

        Assert.assertTrue(expectedObjects.size() == readObjects.size());

        for (int i = 0; i < readObjects.size(); i++) {

            final EObject readObject = readObjects.get(i);
            EObject expectedObject = null;
            if (readObject instanceof Entity) {
                expectedObject = expectedObjects.get(((Entity) readObject).getId());
            } else if (readObject instanceof PrimitiveDataType) {
                expectedObject = expectedObjects.get(readObject.toString());
            }

            if (!this.equalityHelper.equals(expectedObject, readObject)) {
                System.out.println(expectedObject + " " + readObject);
            }

            // System.out.println("Testing " + expectedObject);
            // if (expectedObject instanceof Entity) {
            // System.out.println(expectedObject + " " + ((Entity) expectedObject).getEntityName() +
            // "\t" + readObject
            // + " " + ((Entity) readObject).getEntityName() + "\t"
            // + this.equalityHelper.equals(expectedObject, readObject));
            // } else {
            // System.out.println(expectedObject + "\t" + readObject + "\t"
            // + this.equalityHelper.equals(expectedObject, readObject));
            // }

        }

    }

}
