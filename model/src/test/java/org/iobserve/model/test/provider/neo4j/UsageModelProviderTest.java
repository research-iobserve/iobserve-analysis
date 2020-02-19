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
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.neo4j.ModelGraphFactory;
import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.iobserve.model.persistence.neo4j.NodeLookupException;
import org.iobserve.model.test.data.UsageModelDataFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;
import org.palladiosimulator.pcm.usagemodel.UsagemodelPackage;

/**
 * Test cases for the model provider using an usage model.
 *
 * @author Lars Bluemke
 *
 * @since 0.0.2
 */
@Ignore
public class UsageModelProviderTest extends AbstractModelProviderTest<UsageModel> { // NOCS
                                                                                    // no
    // constructor
    // in test

    @Override
    @Before
    public void setUp() {
        this.prefix = this.getClass().getCanonicalName();
        this.testModel = this.usageModel;
        this.ePackage = UsagemodelPackage.eINSTANCE;
        this.clazz = UsageModel.class;
        this.eClass = UsagemodelPackage.Literals.USAGE_MODEL;
    }

    /**
     * Test whether we can create and send search for it by type.
     *
     * @throws DBException
     */
    @Test
    public void createThenReadByType() throws DBException {

    }

    /**
     * Test whether we can create and then read an object including containing elements.
     *
     * @throws DBException
     */
    @Test
    public void createThenReadContaining() throws DBException {

    }

    /**
     * Test whether creating and then reading referencing objects works.
     *
     * @throws DBException
     */
    @Test
    public void createThenReadReferencing() throws DBException {
        final Neo4JModelResource<UsageModel> resource = ModelProviderTestUtils
                .prepareResource("createThenReadReferencing", this.prefix, this.ePackage);

        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);

        resource.storeModelPartition(this.testModel);

        // Using usage scenario because usage model does not have an id
        final List<EObject> readReferencingComponents = resource.collectReferencingObjectsByTypeAndProperty(
                UsageScenario.class, UsagemodelPackage.Literals.USAGE_SCENARIO,
                ModelGraphFactory.getIdentification(writtenScenario));

        final EObject buyABookBehavior = UsageModelDataFactory.findBehavior(this.usageModel,
                UsageModelDataFactory.BUY_A_BOOK_BEHAVIOR);
        final EObject closedWorkload = UsageModelDataFactory
                .findUsageScenario(this.usageModel, UsageModelDataFactory.USAGE_SCENARIO_GROUP_0)
                .getWorkload_UsageScenario();

        // Only the scenario behavior and the closed workload reference the usage scenario
        for (final EObject readReferencingComponent : readReferencingComponents) {
            Assert.assertTrue(this.equalityHelper.comparePartition(buyABookBehavior, readReferencingComponent,
                    buyABookBehavior.eClass())
                    || this.equalityHelper.comparePartition(closedWorkload, readReferencingComponent,
                            closedWorkload.eClass()));
        }

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Test whether we can create, update and then read the object for the DB with committed
     * correctly.
     *
     * @throws NodeLookupException
     * @throws DBException
     */
    @Test
    public void createThenUpdateThenReadUpdated() throws NodeLookupException, DBException {
        final Neo4JModelResource<UsageModel> resource = ModelProviderTestUtils
                .prepareResource("createThenUpdateThenReadUpdated", this.prefix, this.ePackage);

        final UsageScenario writtenUsageScenarioGroup0 = UsageModelDataFactory.findUsageScenario(this.usageModel,
                UsageModelDataFactory.USAGE_SCENARIO_GROUP_0);
        final ScenarioBehaviour writtenBuyBookScenarioBehaviour = UsageModelDataFactory.findBehavior(this.usageModel,
                UsageModelDataFactory.BUY_A_BOOK_BEHAVIOR);
        final EntryLevelSystemCall writtenGetQueryCall = UsageModelDataFactory.findSystemCallbyName(this.usageModel,
                UsageModelDataFactory.USAGE_SCENARIO_GROUP_0, UsageModelDataFactory.BUY_A_BOOK_BEHAVIOR,
                UsageModelDataFactory.QUERY_CALL);
        final EntryLevelSystemCall writtenGetPriceCall = UsageModelDataFactory.findSystemCallbyName(this.usageModel,
                UsageModelDataFactory.USAGE_SCENARIO_GROUP_0, UsageModelDataFactory.BUY_A_BOOK_BEHAVIOR,
                UsageModelDataFactory.PRICE_CALL);
        resource.storeModelPartition(this.testModel);

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

        resource.updatePartition(this.testModel);

        final UsageModel readModel = resource.getModelRootNode(UsageModel.class, this.eClass);

        Assert.assertTrue(this.equalityHelper.comparePartition(this.testModel, readModel, readModel.eClass()));

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Test create with subsequent delete sequence.
     *
     * @throws DBException
     */
    @Test
    public void createThenDeleteObject() throws DBException {
        final Neo4JModelResource<UsageModel> resource = ModelProviderTestUtils.prepareResource("createThenDeleteObject",
                this.prefix, this.ePackage);

        final UsageScenario writtenScenario = this.testModel.getUsageScenario_UsageModel().get(0);

        resource.storeModelPartition(this.testModel);

        Assert.assertFalse(ModelProviderTestUtils.isResourceEmpty(resource));

        resource.deleteObject(writtenScenario);

        // Manually delete the root node (as it has no id), the double literal node (as it is not
        // contained anywhere) and the proxy nodes (as they are no containments in this graph)
        // try (final Transaction tx = graph.getGraphDatabaseService().beginTx()) {
        // graph.getGraphDatabaseService().execute("MATCH (m:`" +
        // UsageModel.class.getCanonicalName() + "`), (n:`"
        // + DoubleLiteral.class.getCanonicalName() + "`), (o:`" +
        // OperationSignature.class.getCanonicalName()
        // + "`), (p:`" + OperationProvidedRole.class.getCanonicalName() + "`) DELETE n, m, o, p");
        // tx.success();
        // }

        // Assert.assertTrue(this.isGraphEmpty(modelProvider));

        final List<?> scenarios = resource.collectAllObjectsByType(UsageScenario.class,
                UsagemodelPackage.Literals.USAGE_SCENARIO);

        Assert.assertEquals("Usage scenario should all have been deleted.", scenarios.size(), 0);

        resource.getGraphDatabaseService().shutdown();
    }

    /**
     * Test whether we can create and then delete the object including its data types correctly.
     *
     * @throws DBException
     */
    @Test
    public void createThenDeleteObjectAndDatatypes() throws DBException {

    }

}
