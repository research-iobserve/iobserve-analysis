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
package org.iobserve.adaptation.droolsstages;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.adaptation.data.AssemblyContextActionFactory;
import org.iobserve.adaptation.data.graph.ComponentNode;
import org.iobserve.adaptation.data.graph.GraphFactory;
import org.iobserve.adaptation.data.graph.ModelGraph;
import org.iobserve.adaptation.data.graph.ModelGraphRevision;
import org.iobserve.planning.systemadaptation.Action;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import teetime.framework.test.StageTester;

/**
 * Test cases for the rule based computation of composed adaptation actions.
 *
 * @author Lars Bluemke
 *
 */
public class ComposedAdaptationActionComputationTest {

    private ComposedAdaptationActionComputation composedActionComputation;

    private AdaptationTestModel runtimeModel;
    private AdaptationTestModel redeploymentModel;

    @Before
    public void initializePcmModels() throws CloneNotSupportedException {
        this.runtimeModel = new AdaptationTestModel();
        this.redeploymentModel = (AdaptationTestModel) this.runtimeModel.getCopyWithSameIds();

        this.composedActionComputation = new ComposedAdaptationActionComputation();
    }

    @Test
    public void testReplicationRule() throws Exception {
        final AdaptationData adaptationData;
        final ComponentNode runtimeNode;
        final ComponentNode redeploymentNode;
        final List<Action> expectedOutput = new ArrayList<>();

        // Perform replication
        this.redeploymentModel.replicateCompB11ToRc2();
        adaptationData = this.createAdaptationData(this.runtimeModel, this.redeploymentModel);

        // Execute stage
        StageTester.test(this.composedActionComputation).and().send(adaptationData)
                .to(this.composedActionComputation.getInputPort()).and().start();

        // Create expected output
        runtimeNode = this.findComponentNode(this.runtimeModel.getAcxt_b11().getId(),
                adaptationData.getRuntimeGraph().getComponents());
        redeploymentNode = this.findComponentNode(this.redeploymentModel.getAcxt_b12().getId(),
                adaptationData.getReDeploymentGraph().getComponents());
        expectedOutput.add(AssemblyContextActionFactory.generateReplicateAction(runtimeNode, redeploymentNode));

        Assert.assertThat(this.composedActionComputation.getOutputPort(), StageTester.produces(expectedOutput));
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

    private ComponentNode findComponentNode(final String assemblyContextID, final Set<ComponentNode> componentNodes) {

        for (final ComponentNode node : componentNodes) {
            if (node.getAssemblyContextID().equals(assemblyContextID)) {
                return node;
            }
        }

        return null;
    }
}
