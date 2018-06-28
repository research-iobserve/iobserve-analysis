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

import de.uka.ipd.sdq.stoex.DoubleLiteral;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.model.provider.neo4j.ModelGraph;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Transaction;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
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
        this.prefix = this.getClass().getCanonicalName();
        this.testModel = this.testModelBuilder.getUsageModel();
        this.factory = UsagemodelFactory.eINSTANCE;
        this.clazz = UsageModel.class;
    }

    /**
     * Test whether we can create and send search for it by type.
     */
    @Test
    public void createThenReadByType() {
        final ModelGraph graph = this.prepareGraph("createThenReadByType");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final ModelProvider<UsageScenario> modelProvider2 = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);

        // Create complete model but only read a UsageScenario, because UsageModel itself has no id
        modelProvider.storeModelPartition(this.testModel);

        final List<String> collectedIds = modelProvider2.collectAllObjectIdsByType(UsageScenario.class);

        // There is only one usage scenario in the test model
        Assert.assertTrue(collectedIds.size() == 1);

        Assert.assertTrue(writtenScenario.getId().equals(collectedIds.get(0)));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Test whether we can create and then read an object including containing elements.
     */
    @Test
    public void createThenReadContaining() {
        final ModelGraph graph = this.prepareGraph("createThenReadContaining");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);

        modelProvider.storeModelPartition(this.testModel);

        final UsageModel readModel = (UsageModel) modelProvider.readOnlyContainingComponentById(UsageScenario.class,
                writtenScenario.getId());

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Test whether creating and then reading referencing objects works.
     */
    @Test
    public void createThenReadReferencing() {
        final ModelGraph graph = this.prepareGraph("createThenReadReferencing");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);

        modelProvider.storeModelPartition(this.testModel);

        // Using usage scenario because usage model does not have an id
        final List<EObject> readReferencingComponents = modelProvider
                .collectReferencingObjectsByTypeAndId(UsageScenario.class, writtenScenario.getId());

        // Only the scenario behavior and the closed workload reference the usage scenario
        for (final EObject readReferencingComponent : readReferencingComponents) {
            Assert.assertTrue(this.equalityHelper.equals(this.testModelBuilder.getBuyBookScenarioBehaviour(),
                    readReferencingComponent)
                    || this.equalityHelper.equals(this.testModelBuilder.getClosedWorkload(), readReferencingComponent));
        }

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Test whether we can create, update and then read the object for the DB with committed
     * correctly.
     */
    @Test
    public void createThenUpdateThenReadUpdated() {
        final ModelGraph graph = this.prepareGraph("createThenUpdateThenReadUpdated");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        final UsageScenario writtenUsageScenarioGroup0 = this.testModelBuilder.getUsageScenarioGroup0();
        final ScenarioBehaviour writtenBuyBookScenarioBehaviour = this.testModelBuilder.getBuyBookScenarioBehaviour();
        final EntryLevelSystemCall writtenGetQueryCall = this.testModelBuilder.getGetQueryCall();
        final EntryLevelSystemCall writtenGetPriceCall = this.testModelBuilder.getGetPriceCall();

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

        final UsageModel readModel = modelProvider.getModelRootNode(UsageModel.class);

        Assert.assertTrue(this.equalityHelper.equals(this.testModel, readModel));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Test create with subsequent delete sequence.
     */
    @Test
    public void createThenDeleteObject() {
        final ModelGraph graph = this.prepareGraph("createThenDeleteObject");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        new ModelProvider<UsageScenario>(graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID)
                .deleteObjectByTypeAndId(UsageScenario.class, writtenScenario.getId());

        // Manually delete the root node (as it has no id), the double literal node (as it is not
        // contained anywhere) and the proxy nodes (as they are no containments in this graph)
        try (final Transaction tx = graph.getGraphDatabaseService().beginTx()) {
            graph.getGraphDatabaseService().execute("MATCH (m:`" + UsageModel.class.getCanonicalName() + "`), (n:`"
                    + DoubleLiteral.class.getCanonicalName() + "`), (o:`" + OperationSignature.class.getCanonicalName()
                    + "`), (p:`" + OperationProvidedRole.class.getCanonicalName() + "`) DELETE n, m, o, p");
            tx.success();
        }

        Assert.assertTrue(this.isGraphEmpty(modelProvider));

        graph.getGraphDatabaseService().shutdown();
    }

    /**
     * Test whether we can create and then delete the object including its data types correctly.
     */
    @Test
    public void createThenDeleteObjectAndDatatypes() {
        final ModelGraph graph = this.prepareGraph("createThenDeleteObjectAndDatatypes");

        final ModelProvider<UsageModel> modelProvider = new ModelProvider<>(graph, ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);

        modelProvider.storeModelPartition(this.testModel);

        Assert.assertFalse(this.isGraphEmpty(modelProvider));

        new ModelProvider<UsageScenario>(graph, ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID)
                .deleteObjectByIdAndDatatypes(UsageScenario.class, writtenScenario.getId(), true);

        Assert.assertTrue(this.isGraphEmpty(modelProvider));

        graph.getGraphDatabaseService().shutdown();
    }

}
