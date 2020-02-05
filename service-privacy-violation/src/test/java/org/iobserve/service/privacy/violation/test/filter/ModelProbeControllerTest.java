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
package org.iobserve.service.privacy.violation.test.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import teetime.framework.test.StageTester;

import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.service.privacy.violation.data.WarningModel;
import org.iobserve.service.privacy.violation.filter.ModelProbeController;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Vertex;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Vertex.EStereoType;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 * @author Marc Adolf
 *
 */
@Ignore
public class ModelProbeControllerTest {
    private ModelProbeController modelProbeController;

    @Test
    public void testComputedMethodsToActivateAndToDeactivate() {

        this.modelProbeController = new ModelProbeController();

        final OperationSignature operationSignature1 = Mockito.mock(OperationSignature.class);
        final OperationSignature operationSignature2 = Mockito.mock(OperationSignature.class);
        final AllocationContext allocationContext1 = Mockito.mock(AllocationContext.class);
        final AllocationContext allocationContext2 = Mockito.mock(AllocationContext.class);

        final WarningModel warnings1 = new WarningModel();
        final Edge edge1 = new Edge(new Vertex("test vertex 1", EStereoType.DATASOURCE),
                new Vertex("test vertex 2", EStereoType.DATASOURCE));
        edge1.setOperationSignature(operationSignature1);
        edge1.getSource().setAllocationContext(allocationContext1);

        final Edge edge2 = new Edge(new Vertex("test vertex 3", EStereoType.DATASOURCE),
                new Vertex("test vertex 4", EStereoType.DATASOURCE));
        edge2.setOperationSignature(operationSignature2);
        edge2.getSource().setAllocationContext(allocationContext2);

        warnings1.addWarningEdge(edge1);
        warnings1.addWarningEdge(edge2);

        final Map<AllocationContext, Set<OperationSignature>> methodsToActivate1 = new HashMap<>();

        final Set<OperationSignature> methodsSet1 = new HashSet<>();
        methodsSet1.add(operationSignature1);
        methodsToActivate1.put(allocationContext1, methodsSet1);

        final Set<OperationSignature> methodsSet2 = new HashSet<>();
        methodsSet2.add(operationSignature2);
        methodsToActivate1.put(allocationContext2, methodsSet2);

        final ProbeManagementData expectedOutcome1 = new ProbeManagementData(methodsToActivate1,
                new HashMap<AllocationContext, Set<OperationSignature>>());

        // deactivate 1, keep 1, add 1 to existing and add double set / allocation to output port
        final OperationSignature operationSignature3 = Mockito.mock(OperationSignature.class);
        final OperationSignature operationSignature4 = Mockito.mock(OperationSignature.class);
        final OperationSignature operationSignature5 = Mockito.mock(OperationSignature.class);

        final AllocationContext allocationContext3 = Mockito.mock(AllocationContext.class);

        final Edge edge3 = new Edge(new Vertex("test vertex 5", EStereoType.DATASOURCE),
                new Vertex("test vertex 6", EStereoType.DATASOURCE));
        edge3.setOperationSignature(operationSignature3);
        edge3.getSource().setAllocationContext(allocationContext2);

        final Edge edge4 = new Edge(new Vertex("test vertex 7", EStereoType.DATASOURCE),
                new Vertex("test vertex 8", EStereoType.DATASOURCE));
        edge4.setOperationSignature(operationSignature4);
        edge4.getSource().setAllocationContext(allocationContext3);

        final Edge edge5 = new Edge(new Vertex("test vertex 9", EStereoType.DATASOURCE),
                new Vertex("test vertex 10", EStereoType.DATASOURCE));
        edge5.setOperationSignature(operationSignature5);
        edge5.getSource().setAllocationContext(allocationContext3);

        final WarningModel warnings2 = new WarningModel();
        warnings2.addWarningEdge(edge2);
        warnings2.addWarningEdge(edge3);
        warnings2.addWarningEdge(edge4);
        warnings2.addWarningEdge(edge5);

        final Map<AllocationContext, Set<OperationSignature>> methodsToActivate2 = new HashMap<>();

        final Set<OperationSignature> methodsSet3 = new HashSet<>();
        methodsSet3.add(operationSignature3);
        methodsToActivate2.put(allocationContext2, methodsSet3);

        final Set<OperationSignature> methodsSet4 = new HashSet<>();
        methodsSet4.add(operationSignature4);
        methodsSet4.add(operationSignature5);
        methodsToActivate2.put(allocationContext3, methodsSet4);

        final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate2 = new HashMap<>();
        methodsToDeactivate2.put(allocationContext1, methodsSet1);

        final ProbeManagementData expectedOutcome2 = new ProbeManagementData(methodsToActivate2, methodsToDeactivate2);

        final List<WarningModel> input = new LinkedList<>();
        input.add(warnings1);
        input.add(warnings2);

        final List<ProbeManagementData> output = new LinkedList<>();

        StageTester.test(this.modelProbeController).and().send(input).to(this.modelProbeController.getInputPort())
                .receive(output).from(this.modelProbeController.getOutputPort()).start();
        Assert.assertTrue(output.get(0).getMethodsToActivate().equals(expectedOutcome1.getMethodsToActivate()));
        Assert.assertTrue(output.get(0).getMethodsToDeactivate().equals(expectedOutcome1.getMethodsToDeactivate()));

        Assert.assertTrue(output.get(1).getMethodsToActivate().equals(expectedOutcome2.getMethodsToActivate()));
        Assert.assertTrue(output.get(1).getMethodsToDeactivate().equals(expectedOutcome2.getMethodsToDeactivate()));

    }

    @Test
    public void testNullWarningList() {
        final WarningModel warnings = new WarningModel();
        warnings.setWarningEdges(null);
        final List<WarningModel> input1 = new LinkedList<>();
        input1.add(warnings);

        final List<ProbeManagementData> output = new LinkedList<>();

        this.modelProbeController = new ModelProbeController();
        StageTester.test(this.modelProbeController).and().send(input1).to(this.modelProbeController.getInputPort())
                .receive(output).from(this.modelProbeController.getOutputPort()).start();

        Assert.assertTrue(output.isEmpty());

    }

    @Test
    public void testEmptyWarningList() {
        final WarningModel warnings = new WarningModel();
        warnings.setWarningEdges(null);
        final List<WarningModel> input1 = new LinkedList<>();
        input1.add(warnings);

        final List<ProbeManagementData> output = new LinkedList<>();

        this.modelProbeController = new ModelProbeController();
        StageTester.test(this.modelProbeController).and().send(input1).to(this.modelProbeController.getInputPort())
                .receive(output).from(this.modelProbeController.getOutputPort()).start();

        Assert.assertTrue(output.isEmpty());

    }

}
