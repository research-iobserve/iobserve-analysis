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
package org.iobserve.model.test.provider.neo4j;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.provider.neo4j.Graph;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;

/**
 * Test cases for the model provider using an usage model.
 *
 * @author Lars Bluemke
 *
 * @since 0.0.2
 */
public class UsageModelProviderTest extends AbstractModelProviderTest<UsageModel> { // NOCS
                                                                                    // no
    // constructor
    // in test

    @Override
    @Before
    public void setUp() {
        this.testModel = new TestModelBuilder().getUsageModel();
        this.factory = UsagemodelFactory.eINSTANCE;
        this.clazz = UsageModel.class;
    }

    @Override
    @Test
    public void createThenReadByType() {
        final Graph graph = this.prepareGraph("createThenReadByType");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final ModelProvider<UsageScenario> modelProvider2 = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);
        final List<String> readIds;

        // Create complete model but only read a UsageScenario, because UsageModel itself has no id
        modelProvider.storeModelPartition(this.testModel);
        readIds = modelProvider2.collectAllObjectIdsByType(UsageScenario.class);

        // There is only one usage scenario in the test model
        Assert.assertTrue(readIds.size() == 1);

        Assert.assertTrue(writtenScenario.getId().equals(readIds.get(0)));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final Graph graph = this.prepareGraph("createThenReadContaining");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);
        final UsageModel readModel;

        modelProvider.storeModelPartition(this.testModel);
        readModel = (UsageModel) modelProvider.readOnlyContainingComponentById(UsageScenario.class,
                writtenScenario.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final Graph graph = this.prepareGraph("createThenReadReferencing");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);
        final List<EObject> readReferencingComponents;

        modelProvider.storeModelPartition(this.testModel);

        // Using usage scenario because usage model does not have an id
        readReferencingComponents = modelProvider.readOnlyReferencingComponentsById(UsageScenario.class,
                writtenScenario.getId());

        // Only the scenario behavior and the closed workload reference the usage scenario
        for (final EObject readReferencingComponent : readReferencingComponents) {
            Assert.assertTrue(this.equalityHelper.equals(testModelBuilder.getBuyBookScenarioBehaviour(),
                    readReferencingComponent)
                    || this.equalityHelper.equals(testModelBuilder.getClosedWorkload(), readReferencingComponent));
        }
    }

    @Override
    @Test
    public void createThenUpdateThenReadUpdated() {
        final Graph graph = this.prepareGraph("createThenUpdateThenReadUpdated");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final UsageModel readModel;
        final UsageScenario writtenUsageScenarioGroup0 = testModelBuilder.getUsageScenarioGroup0();
        final ScenarioBehaviour writtenBuyBookScenarioBehaviour = testModelBuilder.getBuyBookScenarioBehaviour();
        final EntryLevelSystemCall writtenGetQueryCall = testModelBuilder.getGetQueryCall();
        final EntryLevelSystemCall writtenGetPriceCall = testModelBuilder.getGetPriceCall();

        modelProvider.storeModelPartition(this.testModel);

        // Update the model by adding a loop for choosing several books
        final Loop shoppingLoop = UsagemodelFactory.eINSTANCE.createLoop();
        final PCMRandomVariable loopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
        final ScenarioBehaviour chooseBookBehaviour = UsagemodelFactory.eINSTANCE.createScenarioBehaviour();
        chooseBookBehaviour.setEntityName("Choose a book");
        chooseBookBehaviour.getActions_ScenarioBehaviour().add(writtenGetQueryCall);
        chooseBookBehaviour.getActions_ScenarioBehaviour().add(writtenGetPriceCall);

        writtenUsageScenarioGroup0.setEntityName("Updated " + writtenUsageScenarioGroup0.getEntityName());
        writtenBuyBookScenarioBehaviour.getActions_ScenarioBehaviour().remove(writtenGetQueryCall);
        writtenBuyBookScenarioBehaviour.getActions_ScenarioBehaviour().remove(writtenGetPriceCall);
        writtenBuyBookScenarioBehaviour.getActions_ScenarioBehaviour().add(shoppingLoop);
        writtenGetQueryCall.setScenarioBehaviour_AbstractUserAction(chooseBookBehaviour);
        writtenGetPriceCall.setScenarioBehaviour_AbstractUserAction(chooseBookBehaviour);

        shoppingLoop.setEntityName("Shopping loop");
        shoppingLoop.setScenarioBehaviour_AbstractUserAction(writtenBuyBookScenarioBehaviour);
        shoppingLoop.setBodyBehaviour_Loop(chooseBookBehaviour);
        shoppingLoop.setLoopIteration_Loop(loopIteration);

        loopIteration.setLoop_LoopIteration(shoppingLoop);
        loopIteration.setSpecification("2");

        modelProvider.updateObject(UsageModel.class, this.testModel);

        readModel = modelProvider.readRootNode(UsageModel.class);

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));
    }

    @Override
    @Test
    public void createThenDeleteObject() {
        final Graph graph = this.prepareGraph("createThenDeleteObject");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        new ModelProvider<UsageScenario>(graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID)
                .deleteObjectById(UsageScenario.class, writtenScenario.getId());

        // Manually delete the root node (as it has no id), the double literal node (as it is not
        // contained anywhere) and the proxy nodes (as they are no containments in this graph)
        try (Transaction tx = graph.getGraphDatabaseService().beginTx()) {
            graph.getGraphDatabaseService().execute(
                    "MATCH (m:UsageModel), (n:DoubleLiteral), (o:OperationSignature), (p:OperationProvidedRole) DELETE n, m, o, p");
            tx.success();
        }

        Assert.assertTrue(this.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteObjectAndDatatypes() {
        final Graph graph = this.prepareGraph("createThenDeleteObjectAndDatatypes");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        new ModelProvider<UsageScenario>(graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID)
                .deleteObjectByIdAndDatatypes(UsageScenario.class, writtenScenario.getId(), true);

        Assert.assertTrue(this.isGraphEmpty(modelProvider));
    }

}
