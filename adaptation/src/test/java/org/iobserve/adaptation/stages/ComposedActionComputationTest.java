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
package org.iobserve.adaptation.stages;

import java.util.Set;

import org.hamcrest.Matchers;
import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.adaptation.data.AssemblyContextActionFactory;
import org.iobserve.adaptation.data.ResourceContainerActionFactory;
import org.iobserve.adaptation.data.graph.ComponentNode;
import org.iobserve.adaptation.data.graph.DeploymentNode;
import org.iobserve.adaptation.data.graph.GraphFactory;
import org.iobserve.adaptation.data.graph.ModelGraph;
import org.iobserve.adaptation.data.graph.ModelGraphRevision;
import org.iobserve.adaptation.testmodel.AdaptationTestModel;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.DereplicateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import teetime.framework.Execution;
import teetime.stage.CollectorSink;
import teetime.stage.InitialElementProducer;

/**
 * Test cases for the rule based computation of composed adaptation actions.
 *
 * @author Lars Bluemke
 *
 */
public class ComposedActionComputationTest {

    private AdaptationTestModel runtimeModel;
    private AdaptationTestModel redeploymentModel;

    /**
     * Empty default constructor
     */
    public ComposedActionComputationTest() {

    }

    @Before
    public void initializePcmModels() throws CloneNotSupportedException {
        this.runtimeModel = new AdaptationTestModel();
        this.redeploymentModel = (AdaptationTestModel) this.runtimeModel.getCopyWithSameIds();
    }

    @Test
    public void testReplicationRule() throws Exception {
        final AdaptationData adaptationData;
        final ComponentNode runtimeNode;
        final ComponentNode redeploymentNode;
        final SystemAdaptation actualOutput;
        final ReplicateAction actualAction;
        final ReplicateAction expectedAction;

        // Perform replication
        this.redeploymentModel.replicateCompB11ToRc2();
        adaptationData = this.createAdaptationData(this.runtimeModel, this.redeploymentModel);

        // Create expected output
        runtimeNode = this.findComponentNodeByID(this.runtimeModel.getAcxt_b1().getId(),
                adaptationData.getRuntimeGraph().getComponents());
        redeploymentNode = this.findComponentNodeByID(this.redeploymentModel.getAcxt_b2().getId(),
                adaptationData.getReDeploymentGraph().getComponents());
        expectedAction = AssemblyContextActionFactory.generateReplicateAction(runtimeNode, redeploymentNode);

        // Execute stage
        actualOutput = this.executeStage(adaptationData);

        Assert.assertThat(actualOutput.getActions().size(), Matchers.is(1));
        Assert.assertTrue(actualOutput.getActions().get(0) instanceof ReplicateAction);

        actualAction = (ReplicateAction) actualOutput.getActions().get(0);

        Assert.assertThat(actualAction.getNewAllocationContext(),
                Matchers.is(expectedAction.getNewAllocationContext()));
        Assert.assertThat(actualAction.getResourceContainer(), Matchers.is(expectedAction.getResourceContainer()));
        Assert.assertThat(actualAction.getSourceAssemblyContext(),
                Matchers.is(expectedAction.getSourceAssemblyContext()));
    }

    @Test
    public void testDereplicationRule() throws Exception {
        final AdaptationData adaptationData;
        final ComponentNode runtimeNode;
        final SystemAdaptation actualOutput;
        final DereplicateAction actualAction;
        final DereplicateAction expectedAction;

        // Perform dereplication (simulated by replicating a component in the runtime model)
        this.runtimeModel.replicateCompB11ToRc2();
        adaptationData = this.createAdaptationData(this.runtimeModel, this.redeploymentModel);

        // Create expected output
        runtimeNode = this.findComponentNodeByID(this.runtimeModel.getAcxt_b2().getId(),
                adaptationData.getRuntimeGraph().getComponents());
        expectedAction = AssemblyContextActionFactory.generateDereplicateAction(runtimeNode);

        // Execute stage
        actualOutput = this.executeStage(adaptationData);

        Assert.assertThat(actualOutput.getActions().size(), Matchers.is(1));
        Assert.assertTrue(actualOutput.getActions().get(0) instanceof DereplicateAction);

        actualAction = (DereplicateAction) actualOutput.getActions().get(0);

        Assert.assertThat(actualAction.getOldAllocationContext(),
                Matchers.is(expectedAction.getOldAllocationContext()));
        Assert.assertThat(actualAction.getResourceContainer(), Matchers.is(expectedAction.getResourceContainer()));
        Assert.assertThat(actualAction.getSourceAssemblyContext(),
                Matchers.is(expectedAction.getSourceAssemblyContext()));
    }

    @Test
    public void testMigrationRule() throws Exception {
        final AdaptationData adaptationData;
        final ComponentNode runtimeNode;
        final ComponentNode redeploymentNode;
        final SystemAdaptation actualOutput;
        final MigrateAction actualAction;
        final MigrateAction expectedAction;

        // Perform migration
        this.redeploymentModel.migrateCompB1ToRc2();
        adaptationData = this.createAdaptationData(this.runtimeModel, this.redeploymentModel);

        // Create expected output
        runtimeNode = this.findComponentNodeByID(this.runtimeModel.getAcxt_b1().getId(),
                adaptationData.getRuntimeGraph().getComponents());
        redeploymentNode = this.findComponentNodeByID(this.redeploymentModel.getAcxt_b1().getId(),
                adaptationData.getReDeploymentGraph().getComponents());
        expectedAction = AssemblyContextActionFactory.generateMigrateAction(runtimeNode, redeploymentNode);

        // Execute stage
        actualOutput = this.executeStage(adaptationData);

        Assert.assertThat(actualOutput.getActions().size(), Matchers.is(1));
        Assert.assertTrue(actualOutput.getActions().get(0) instanceof MigrateAction);

        actualAction = (MigrateAction) actualOutput.getActions().get(0);

        Assert.assertThat(actualAction.getNewAllocationContext(),
                Matchers.is(expectedAction.getNewAllocationContext()));
        Assert.assertThat(actualAction.getResourceContainer(), Matchers.is(expectedAction.getResourceContainer()));
        Assert.assertThat(actualAction.getSourceAllocationContext(),
                Matchers.is(expectedAction.getSourceAllocationContext()));
        Assert.assertThat(actualAction.getSourceAssemblyContext(),
                Matchers.is(expectedAction.getSourceAssemblyContext()));
    }

    @Test
    public void testChangeRepositoryRule() throws Exception {
        final AdaptationData adaptationData;
        final ComponentNode runtimeNode;
        final ComponentNode redeploymentNode;
        final SystemAdaptation actualOutput;
        final ChangeRepositoryComponentAction actualAction;
        final ChangeRepositoryComponentAction expectedAction;

        // Perform change of component
        this.redeploymentModel.changeRepositoryCompBxToCompBy();
        adaptationData = this.createAdaptationData(this.runtimeModel, this.redeploymentModel);

        // Create expected output
        runtimeNode = this.findComponentNodeByID(this.runtimeModel.getAcxt_b1().getId(),
                adaptationData.getRuntimeGraph().getComponents());
        redeploymentNode = this.findComponentNodeByID(this.redeploymentModel.getAcxt_b1().getId(),
                adaptationData.getReDeploymentGraph().getComponents());
        expectedAction = AssemblyContextActionFactory.generateChangeRepositoryComponentAction(runtimeNode,
                redeploymentNode);

        // Execute stage
        actualOutput = this.executeStage(adaptationData);

        Assert.assertThat(actualOutput.getActions().size(), Matchers.is(1));
        Assert.assertTrue(actualOutput.getActions().get(0) instanceof ChangeRepositoryComponentAction);

        actualAction = (ChangeRepositoryComponentAction) actualOutput.getActions().get(0);

        Assert.assertThat(actualAction.getNewRepositoryComponent(),
                Matchers.is(expectedAction.getNewRepositoryComponent()));
        Assert.assertThat(actualAction.getResourceContainer(), Matchers.is(expectedAction.getResourceContainer()));
        Assert.assertThat(actualAction.getSourceAssemblyContext(),
                Matchers.is(expectedAction.getSourceAssemblyContext()));
    }

    @Test
    public void testAllocateRule() throws Exception {
        final AdaptationData adaptationData;
        final DeploymentNode redeploymentNode;
        final SystemAdaptation actualOutput;
        final AllocateAction actualAction;
        final AllocateAction expectedAction;

        // Perform allocation
        this.redeploymentModel.allocateResourceContainerR3();
        adaptationData = this.createAdaptationData(this.runtimeModel, this.redeploymentModel);

        // Create expected output
        redeploymentNode = this.findDeploymentNodeByID(this.redeploymentModel.getRc_3().getId(),
                adaptationData.getReDeploymentGraph().getServers());
        expectedAction = ResourceContainerActionFactory.createAllocateAction(redeploymentNode);

        // Execute stage
        actualOutput = this.executeStage(adaptationData);

        Assert.assertThat(actualOutput.getActions().size(), Matchers.is(1));
        Assert.assertTrue(actualOutput.getActions().get(0) instanceof AllocateAction);

        actualAction = (AllocateAction) actualOutput.getActions().get(0);

        Assert.assertThat(actualAction.getResourceContainer(), Matchers.is(expectedAction.getResourceContainer()));
        Assert.assertThat(actualAction.getSourceResourceContainer(),
                Matchers.is(expectedAction.getSourceResourceContainer()));
    }

    @Test
    public void testDeallocateRule() throws Exception {
        final AdaptationData adaptationData;
        final DeploymentNode runtimeNode;
        final SystemAdaptation actualOutput;
        final DeallocateAction actualAction;
        final DeallocateAction expectedAction;

        // Perform deallocation (simulated by an allocation in the runtime model)
        this.runtimeModel.allocateResourceContainerR3();
        adaptationData = this.createAdaptationData(this.runtimeModel, this.redeploymentModel);

        // Create expected output
        runtimeNode = this.findDeploymentNodeByID(this.runtimeModel.getRc_3().getId(),
                adaptationData.getRuntimeGraph().getServers());
        expectedAction = ResourceContainerActionFactory.createDeallocateAction(runtimeNode);

        // Execute stage
        actualOutput = this.executeStage(adaptationData);

        Assert.assertThat(actualOutput.getActions().size(), Matchers.is(1));
        Assert.assertTrue(actualOutput.getActions().get(0) instanceof DeallocateAction);

        actualAction = (DeallocateAction) actualOutput.getActions().get(0);

        Assert.assertThat(actualAction.getResourceContainer(), Matchers.is(expectedAction.getResourceContainer()));
        Assert.assertThat(actualAction.getSourceResourceContainer(),
                Matchers.is(expectedAction.getSourceResourceContainer()));
    }

    private AdaptationData createAdaptationData(final AdaptationTestModel runtimeModel,
            final AdaptationTestModel redeploymentModel) throws Exception {
        final AdaptationData adaptationData = new AdaptationData();
        final GraphFactory graphFactory = new GraphFactory();

        final ModelGraph runtimeModelGraph = graphFactory.buildGraph(runtimeModel.getSystem(),
                runtimeModel.getResEnvironment(), runtimeModel.getAllocation(), ModelGraphRevision.RUNTIME);
        final ModelGraph redeploymentModelGraph = graphFactory.buildGraph(redeploymentModel.getSystem(),
                redeploymentModel.getResEnvironment(), redeploymentModel.getAllocation(),
                ModelGraphRevision.REDEPLOYMENT);

        adaptationData.setRuntimeGraph(runtimeModelGraph);
        adaptationData.setReDeploymentGraph(redeploymentModelGraph);

        return adaptationData;
    }

    private ComponentNode findComponentNodeByID(final String assemblyContextID,
            final Set<ComponentNode> componentNodes) {

        for (final ComponentNode node : componentNodes) {
            if (node.getAssemblyContextID().equals(assemblyContextID)) {
                return node;
            }
        }

        return null;
    }

    private DeploymentNode findDeploymentNodeByID(final String resourceContainerID,
            final Set<DeploymentNode> deploymentNodes) {

        for (final DeploymentNode node : deploymentNodes) {
            if (node.getResourceContainerID().equals(resourceContainerID)) {
                return node;
            }
        }

        return null;
    }

    private SystemAdaptation executeStage(final AdaptationData adaptationData) {
        final InitialElementProducer<AdaptationData> producer = new InitialElementProducer<>(adaptationData);
        final ComposedActionComputation composedActionComputation = new ComposedActionComputation(this.runtimeModel,
                this.redeploymentModel);
        final CollectorSink<SystemAdaptation> collector = new CollectorSink<>();
        final ComposedActionComputationTestConfig configuration = new ComposedActionComputationTestConfig(producer,
                composedActionComputation, collector);
        final Execution<ComposedActionComputationTestConfig> execution = new Execution<>(configuration);

        execution.executeBlocking();

        return collector.getElements().get(0);
    }
}
