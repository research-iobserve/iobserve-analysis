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



import java.util.LinkedList;
import java.util.List;

import teetime.framework.test.StageTester;

import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.service.privacy.violation.data.Warnings;
import org.iobserve.service.privacy.violation.filter.ModelProbeController;
import org.junit.Assert;
import org.junit.Test;





/**
 * @author Marc Adolf
 *
 */
public class ModelProbeControllerTest {
	private ModelProbeController modelProbeController;
	
	
	@Test
	public void testComputedMethodsToActivateAndToDeactivate() {
		
		//TODO delayed until its clear how to create mock objects!
		
//		final OperationSignature operationSignature1 =  OperationSignatureImpl
//		
//		final Warnings warnings1 = new Warnings();
//		final AllocationContext allocationContext1 = AllocationFactoryImpl.init().createAllocationContext();
//		final Edge edge1 = new Edge(new Vertex("test vertex 1", EStereoType.DATASOURCE), new Vertex("test vertex 2", EStereoType.DATASOURCE));
//		edge1.setOperationSignature();
//		warnings1.addWarningEdge(edge1);
//		
//		final List<Warnings> input1 = new LinkedList<Warnings>();
//		input1.add(warnings1);
//		
////		ProbeManagementData expectedOutcome1 = new ProbeManagementData(methodsToActivate, methodsToDeactivate)
//		
//		modelProbeController = new ModelProbeController();
//		assertThat(modelProbeController.getOutputPort(), StageTester.test(this.modelProbeController).and().send(input1).to(modelProbeController.getInputPort()).produces(1));
	}
	@Test
	public void testNullWarningList() {
		final Warnings warnings = new Warnings();
		warnings.setWarningEdges(null);
		final List<Warnings> input1 = new LinkedList<Warnings>();
		input1.add(warnings);
		
		final List<ProbeManagementData> output = new LinkedList<ProbeManagementData>();

		
		modelProbeController = new ModelProbeController();
		StageTester.test(this.modelProbeController).and().send(input1).to(modelProbeController.getInputPort()).receive(output).from(modelProbeController.getOutputPort()).start();
		
		Assert.assertTrue(output.isEmpty());
		
		
	}
	
	@Test
	public void testEmptyWarningList() {
		final Warnings warnings = new Warnings();
		warnings.setWarningEdges(null);
		final List<Warnings> input1 = new LinkedList<Warnings>();
		input1.add(warnings);
		
		final List<ProbeManagementData> output = new LinkedList<ProbeManagementData>();

		
		modelProbeController = new ModelProbeController();
		StageTester.test(this.modelProbeController).and().send(input1).to(modelProbeController.getInputPort()).receive(output).from(modelProbeController.getOutputPort()).start();
		
		Assert.assertTrue(output.isEmpty());
		
		
	}

}
