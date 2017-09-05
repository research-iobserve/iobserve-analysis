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
import java.io.IOException;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.modelneo4j.Graph;
import org.iobserve.analysis.modelneo4j.GraphLoader;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.io.fs.FileUtils;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;

/**
 * Test cases for the model provider using an usage model.
 *
 * @author Lars Bluemke
 *
 */
public class UsageModelProviderTest implements IModelProviderTest {
    private static final File GRAPH_DIR = new File("/Users/LarsBlumke/Desktop/testdb");
    private static final Graph GRAPH = new GraphLoader(UsageModelProviderTest.GRAPH_DIR).getUsageModelGraph();

    private final Neo4jEqualityHelper equalityHelper = new Neo4jEqualityHelper();

    @Override
    @Before
    public void clearGraph() {
        new ModelProvider<>(UsageModelProviderTest.GRAPH).clearGraph();
    }

    @Override
    @Test
    public void createThenCloneThenRead() {
        final ModelProvider<UsageModel> modelProvider1 = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final ModelProvider<UsageModel> modelProvider2;
        final UsageModel writtenModel = new TestModelBuilder().getUsageModel();
        final UsageModel readModel;
        final Graph graph2;

        modelProvider1.createComponent(writtenModel);

        graph2 = modelProvider1.cloneNewGraphVersion(UsageModel.class);
        modelProvider2 = new ModelProvider<>(graph2);

        readModel = modelProvider2.readOnlyRootComponent(UsageModel.class);
        graph2.getGraphDatabaseService().shutdown();

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenClearGraph() {
        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final UsageModel writtenModel = new TestModelBuilder().getUsageModel();

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        modelProvider.clearGraph();

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenReadById() {
        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final ModelProvider<UsageScenario> modelProvider2 = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final UsageModel writtenModel = new TestModelBuilder().getUsageModel();
        final UsageScenario writtenScenario = writtenModel.getUsageScenario_UsageModel().get(0);
        final UsageScenario readScenario;

        // Create complete model but only read a UsageScenario, because UsageModel itself has no id
        modelProvider.createComponent(writtenModel);
        readScenario = modelProvider2.readOnlyComponentById(UsageScenario.class, writtenScenario.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenScenario, readScenario));
    }

    @Override
    @Test
    public void createThenReadByName() {
        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final ModelProvider<UsageScenario> modelProvider2 = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final UsageModel writtenModel = new TestModelBuilder().getUsageModel();
        final UsageScenario writtenScenario = writtenModel.getUsageScenario_UsageModel().get(0);
        final List<UsageScenario> readScenarios;

        // Create complete model but only read a UsageScenario, because UsageModel itself has no id
        modelProvider.createComponent(writtenModel);
        readScenarios = modelProvider2.readOnlyComponentByName(UsageScenario.class, writtenScenario.getEntityName());

        Assert.assertTrue(readScenarios.size() == 1);

        for (final UsageScenario readScenario : readScenarios) {
            Assert.assertTrue(this.equalityHelper.equals(writtenScenario, readScenario));
        }
    }

    @Override
    @Test
    public void createThenReadByType() {
        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final ModelProvider<UsageScenario> modelProvider2 = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final UsageModel writtenModel = new TestModelBuilder().getUsageModel();
        final UsageScenario writtenScenario = writtenModel.getUsageScenario_UsageModel().get(0);
        final List<String> readIds;

        // Create complete model but only read a UsageScenario, because UsageModel itself has no id
        modelProvider.createComponent(writtenModel);
        readIds = modelProvider2.readComponentByType(UsageScenario.class);

        // There is only one usage scenario in the test model
        Assert.assertTrue(readIds.size() == 1);

        Assert.assertTrue(writtenScenario.getId().equals(readIds.get(0)));
    }

    @Override
    @Test
    public void createThenReadRoot() {
        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final UsageModel writtenModel = new TestModelBuilder().getUsageModel();
        final UsageModel readModel;

        modelProvider.createComponent(writtenModel);
        readModel = modelProvider.readOnlyRootComponent(UsageModel.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadContaining() {
        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final UsageModel writtenModel = new TestModelBuilder().getUsageModel();
        final UsageScenario writtenScenario = writtenModel.getUsageScenario_UsageModel().get(0);
        final UsageModel readModel;

        modelProvider.createComponent(writtenModel);
        readModel = (UsageModel) modelProvider.readOnlyContainingComponentById(UsageScenario.class,
                writtenScenario.getId());

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenReadReferencing() {
        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final UsageModel writtenModel = testModelBuilder.getUsageModel();
        final UsageScenario writtenScenario = writtenModel.getUsageScenario_UsageModel().get(0);
        List<EObject> readReferencingComponents;

        modelProvider.createComponent(writtenModel);

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
        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final TestModelBuilder testModelBuilder = new TestModelBuilder();
        final UsageModel writtenModel = testModelBuilder.getUsageModel();
        final UsageModel readModel;
        final UsageScenario writtenUsageScenarioGroup0 = testModelBuilder.getUsageScenarioGroup0();
        final ScenarioBehaviour writtenBuyBookScenarioBehaviour = testModelBuilder.getBuyBookScenarioBehaviour();
        final Start writtenStartScenario = testModelBuilder.getStartScenario();
        final EntryLevelSystemCall writtenGetQueryCall = testModelBuilder.getGetQueryCall();
        final EntryLevelSystemCall writtenGetPriceCall = testModelBuilder.getGetPriceCall();
        final EntryLevelSystemCall writtenWithdrawCall = testModelBuilder.getWithdrawCall();

        modelProvider.createComponent(writtenModel);

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
        writtenStartScenario.setSuccessor(shoppingLoop);
        writtenGetQueryCall.setPredecessor(shoppingLoop);
        writtenGetQueryCall.setScenarioBehaviour_AbstractUserAction(chooseBookBehaviour);
        writtenGetPriceCall.setSuccessor(shoppingLoop);
        writtenGetPriceCall.setScenarioBehaviour_AbstractUserAction(chooseBookBehaviour);
        writtenWithdrawCall.setPredecessor(shoppingLoop);

        shoppingLoop.setEntityName("Shopping loop");
        shoppingLoop.setScenarioBehaviour_AbstractUserAction(writtenBuyBookScenarioBehaviour);
        shoppingLoop.setBodyBehaviour_Loop(chooseBookBehaviour);
        shoppingLoop.setPredecessor(writtenStartScenario);
        shoppingLoop.setSuccessor(writtenWithdrawCall);
        shoppingLoop.setLoopIteration_Loop(loopIteration);

        loopIteration.setLoop_LoopIteration(shoppingLoop);
        loopIteration.setSpecification("2");

        modelProvider.updateComponent(UsageModel.class, writtenModel);

        readModel = modelProvider.readRootComponent(UsageModel.class);

        Assert.assertTrue(this.equalityHelper.equals(writtenModel, readModel));
    }

    @Override
    @Test
    public void createThenDeleteComponent() {
        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final UsageModel writtenModel = new TestModelBuilder().getUsageModel();
        final UsageScenario writtenScenario = writtenModel.getUsageScenario_UsageModel().get(0);

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        new ModelProvider<UsageScenario>(UsageModelProviderTest.GRAPH).deleteComponent(UsageScenario.class,
                writtenScenario.getId());

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    @Override
    @Test
    public void createThenDeleteComponentAndDatatypes() {
        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(UsageModelProviderTest.GRAPH);
        final UsageModel writtenModel = new TestModelBuilder().getUsageModel();
        final UsageScenario writtenScenario = writtenModel.getUsageScenario_UsageModel().get(0);

        modelProvider.createComponent(writtenModel);

        Assert.assertFalse(IModelProviderTest.isGraphEmpty(modelProvider));

        new ModelProvider<UsageScenario>(UsageModelProviderTest.GRAPH).deleteComponentAndDatatypes(UsageScenario.class,
                writtenScenario.getId());

        Assert.assertTrue(IModelProviderTest.isGraphEmpty(modelProvider));
    }

    // @AfterClass
    public static void cleanUp() throws IOException {
        UsageModelProviderTest.GRAPH.getGraphDatabaseService().shutdown();
        FileUtils.deleteRecursively(UsageModelProviderTest.GRAPH_DIR);
    }
}
