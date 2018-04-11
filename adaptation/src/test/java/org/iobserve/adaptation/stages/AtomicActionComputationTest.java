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

import teetime.framework.Execution;
import teetime.stage.CollectorSink;
import teetime.stage.InitialElementProducer;

import org.hamcrest.Matchers;
import org.iobserve.adaptation.data.ActionFactory;
import org.iobserve.adaptation.data.AssemblyContextActionFactory;
import org.iobserve.adaptation.data.ResourceContainerActionFactory;
import org.iobserve.adaptation.data.graph.ComponentNode;
import org.iobserve.adaptation.data.graph.DeploymentNode;
import org.iobserve.adaptation.data.graph.GraphFactory;
import org.iobserve.adaptation.data.graph.ModelGraph;
import org.iobserve.adaptation.data.graph.ModelGraphRevision;
import org.iobserve.adaptation.executionplan.AllocateNodeAction;
import org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction;
import org.iobserve.adaptation.executionplan.ConnectComponentAction;
import org.iobserve.adaptation.executionplan.ConnectNodeAction;
import org.iobserve.adaptation.executionplan.DeallocateNodeAction;
import org.iobserve.adaptation.executionplan.DeployComponentAction;
import org.iobserve.adaptation.executionplan.DisconnectComponentAction;
import org.iobserve.adaptation.executionplan.DisconnectNodeAction;
import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.executionplan.FinishComponentAction;
import org.iobserve.adaptation.executionplan.MigrateComponentStateAction;
import org.iobserve.adaptation.executionplan.UndeployComponentAction;
import org.iobserve.adaptation.testmodel.AdaptationTestModel;
import org.iobserve.model.PCMModelHandlerMockup;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.DereplicateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.SystemAdaptation;
import org.iobserve.planning.systemadaptation.SystemadaptationFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the computation of atomic adaptation actions.
 *
 * @author Lars Bluemke
 *
 */
public class AtomicActionComputationTest {

    private AdaptationTestModel runtimeModel;
    private AdaptationTestModel redeploymentModel;
    private ModelGraph runtimeModelGraph;
    private ModelGraph redeploymentModelGraph;

    @Before
    public void initializePcmModels() {
        this.runtimeModel = new AdaptationTestModel();
        this.redeploymentModel = (AdaptationTestModel) this.runtimeModel.getCopyWithSameIds();

        ActionFactory.setRuntimeModels(new PCMModelHandlerMockup(this.runtimeModel.getAllocation(),
                this.runtimeModel.getResEnvironment(), this.runtimeModel.getSystem(), null, null,
                this.runtimeModel.getRepository(), null, null, null, null));
        ActionFactory.setRedeploymentModels(new PCMModelHandlerMockup(this.redeploymentModel.getAllocation(),
                this.redeploymentModel.getResEnvironment(), this.redeploymentModel.getSystem(), null, null,
                this.redeploymentModel.getRepository(), null, null, null, null));
    }

    @Test
    public void testReplicateAction2AtomicActions() throws Exception {
        final SystemAdaptation systemAdaptationModel = SystemadaptationFactory.eINSTANCE.createSystemAdaptation();
        final ExecutionPlan executionPlan;
        final ComponentNode runtimeNode;
        final ComponentNode redeploymentNode;
        final ReplicateAction replicateAction;

        // Replication specific setup
        this.redeploymentModel.replicateCompB1ToRc2();

        this.initializeModelGraphs();

        runtimeNode = this.findComponentNodeByID(this.runtimeModel.getAcxtB1().getId(),
                this.runtimeModelGraph.getComponents());
        redeploymentNode = this.findComponentNodeByID(this.redeploymentModel.getAcxtB2().getId(),
                this.redeploymentModelGraph.getComponents());

        replicateAction = AssemblyContextActionFactory.generateReplicateAction(runtimeNode, redeploymentNode);

        systemAdaptationModel.getActions().add(replicateAction);

        // Execute stage
        executionPlan = this.executeStage(systemAdaptationModel);

        // Basic verification
        Assert.assertThat(executionPlan.getActions().size(), Matchers.is(3));
        Assert.assertTrue(executionPlan.getActions().get(0) instanceof DeployComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(1) instanceof MigrateComponentStateAction);
        Assert.assertTrue(executionPlan.getActions().get(2) instanceof ConnectComponentAction);
    }

    @Test
    public void testDereplicateAction2AtomicActions() throws Exception {
        final SystemAdaptation systemAdaptationModel = SystemadaptationFactory.eINSTANCE.createSystemAdaptation();
        final ExecutionPlan executionPlan;
        final ComponentNode runtimeNode;
        final DereplicateAction dereplicateAction;

        // Dereplication specific setup (simulated by replicating a component in the runtime model)
        this.runtimeModel.replicateCompB1ToRc2();

        this.initializeModelGraphs();

        runtimeNode = this.findComponentNodeByID(this.runtimeModel.getAcxtB1().getId(),
                this.runtimeModelGraph.getComponents());

        dereplicateAction = AssemblyContextActionFactory.generateDereplicateAction(runtimeNode);

        systemAdaptationModel.getActions().add(dereplicateAction);

        // Execute stage
        executionPlan = this.executeStage(systemAdaptationModel);

        // Basic verification
        Assert.assertThat(executionPlan.getActions().size(), Matchers.is(4));
        Assert.assertTrue(executionPlan.getActions().get(0) instanceof BlockRequestsToComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(1) instanceof FinishComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(2) instanceof DisconnectComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(3) instanceof UndeployComponentAction);
    }

    @Test
    public void testMigrateAction2AtomicActions() throws Exception {
        final SystemAdaptation systemAdaptationModel = SystemadaptationFactory.eINSTANCE.createSystemAdaptation();
        final ExecutionPlan executionPlan;
        final ComponentNode runtimeNode;
        final ComponentNode redeploymentNode;
        final MigrateAction migrateAction;

        // Migration specific setup
        this.redeploymentModel.migrateCompB1ToRc2();

        this.initializeModelGraphs();

        runtimeNode = this.findComponentNodeByID(this.runtimeModel.getAcxtB1().getId(),
                this.runtimeModelGraph.getComponents());
        redeploymentNode = this.findComponentNodeByID(this.redeploymentModel.getAcxtB1().getId(),
                this.redeploymentModelGraph.getComponents());

        migrateAction = AssemblyContextActionFactory.generateMigrateAction(runtimeNode, redeploymentNode);

        systemAdaptationModel.getActions().add(migrateAction);

        // Execute stage
        executionPlan = this.executeStage(systemAdaptationModel);

        // Basic verification
        Assert.assertThat(executionPlan.getActions().size(), Matchers.is(7));
        Assert.assertTrue(executionPlan.getActions().get(0) instanceof DeployComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(1) instanceof MigrateComponentStateAction);
        Assert.assertTrue(executionPlan.getActions().get(2) instanceof ConnectComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(3) instanceof BlockRequestsToComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(4) instanceof FinishComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(5) instanceof DisconnectComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(6) instanceof UndeployComponentAction);
    }

    @Test
    public void testChangeComponent2AtomicActions() throws Exception {
        final SystemAdaptation systemAdaptationModel = SystemadaptationFactory.eINSTANCE.createSystemAdaptation();
        final ExecutionPlan executionPlan;
        final ComponentNode runtimeNode;
        final ComponentNode redeploymentNode;
        final ChangeRepositoryComponentAction changeComponentAction;

        // Change component specific setup
        this.redeploymentModel.changeRepositoryCompBxToCompBy();

        this.initializeModelGraphs();

        runtimeNode = this.findComponentNodeByID(this.runtimeModel.getAcxtB1().getId(),
                this.runtimeModelGraph.getComponents());
        redeploymentNode = this.findComponentNodeByID(this.redeploymentModel.getAcxtB1().getId(),
                this.redeploymentModelGraph.getComponents());

        changeComponentAction = AssemblyContextActionFactory.generateChangeRepositoryComponentAction(runtimeNode,
                redeploymentNode);

        systemAdaptationModel.getActions().add(changeComponentAction);

        // Execute stage
        executionPlan = this.executeStage(systemAdaptationModel);

        // Basic verification
        Assert.assertThat(executionPlan.getActions().size(), Matchers.is(7));
        Assert.assertTrue(executionPlan.getActions().get(0) instanceof DeployComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(1) instanceof MigrateComponentStateAction);
        Assert.assertTrue(executionPlan.getActions().get(2) instanceof ConnectComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(3) instanceof BlockRequestsToComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(4) instanceof FinishComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(5) instanceof DisconnectComponentAction);
        Assert.assertTrue(executionPlan.getActions().get(6) instanceof UndeployComponentAction);
    }

    @Test
    public void testAllocateAction2AtomicActions() throws Exception {
        final SystemAdaptation systemAdaptationModel = SystemadaptationFactory.eINSTANCE.createSystemAdaptation();
        final ExecutionPlan executionPlan;
        final DeploymentNode redeploymentNode;
        final AllocateAction allocateAction;

        // Allocation specific setup
        this.redeploymentModel.allocateResourceContainerR3();

        this.initializeModelGraphs();

        redeploymentNode = this.findDeploymentNodeByID(this.redeploymentModel.getRc3().getId(),
                this.redeploymentModelGraph.getServers());

        allocateAction = ResourceContainerActionFactory.createAllocateAction(redeploymentNode);

        systemAdaptationModel.getActions().add(allocateAction);

        // Execute stage
        executionPlan = this.executeStage(systemAdaptationModel);

        // Basic verification
        Assert.assertThat(executionPlan.getActions().size(), Matchers.is(2));
        Assert.assertTrue(executionPlan.getActions().get(0) instanceof AllocateNodeAction);
        Assert.assertTrue(executionPlan.getActions().get(1) instanceof ConnectNodeAction);
    }

    @Test
    public void testDeallocateAction2AtomicActions() throws Exception {
        final SystemAdaptation systemAdaptationModel = SystemadaptationFactory.eINSTANCE.createSystemAdaptation();
        final ExecutionPlan executionPlan;
        final DeploymentNode runtimeNode;
        final DeallocateAction deallocateAction;

        // Deallocation specific setup ((simulated by an allocation in the runtime model)
        this.runtimeModel.allocateResourceContainerR3();

        this.initializeModelGraphs();

        runtimeNode = this.findDeploymentNodeByID(this.runtimeModel.getRc3().getId(),
                this.runtimeModelGraph.getServers());

        deallocateAction = ResourceContainerActionFactory.createDeallocateAction(runtimeNode);

        systemAdaptationModel.getActions().add(deallocateAction);

        // Execute stage
        executionPlan = this.executeStage(systemAdaptationModel);

        // Basic verification
        Assert.assertThat(executionPlan.getActions().size(), Matchers.is(2));
        Assert.assertTrue(executionPlan.getActions().get(0) instanceof DisconnectNodeAction);
        Assert.assertTrue(executionPlan.getActions().get(1) instanceof DeallocateNodeAction);
    }

    private void initializeModelGraphs() throws Exception {
        final GraphFactory graphFactory = new GraphFactory();

        this.runtimeModelGraph = graphFactory.buildGraph(this.runtimeModel.getSystem(),
                this.runtimeModel.getResEnvironment(), this.runtimeModel.getAllocation(), ModelGraphRevision.RUNTIME);
        this.redeploymentModelGraph = graphFactory.buildGraph(this.redeploymentModel.getSystem(),
                this.redeploymentModel.getResEnvironment(), this.redeploymentModel.getAllocation(),
                ModelGraphRevision.REDEPLOYMENT);

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

    private ExecutionPlan executeStage(final SystemAdaptation systemAdaptationModel) {
        final InitialElementProducer<SystemAdaptation> producer = new InitialElementProducer<>(systemAdaptationModel);
        final AtomicActionComputation atomicActionComputation = new AtomicActionComputation();
        final CollectorSink<ExecutionPlan> collector = new CollectorSink<>();
        final AtomicActionComputationTestConfig configuration = new AtomicActionComputationTestConfig(producer,
                atomicActionComputation, collector);
        final Execution<AtomicActionComputationTestConfig> execution = new Execution<>(configuration);

        execution.executeBlocking();

        return collector.getElements().get(0);
    }
}
