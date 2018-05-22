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
package org.iobserve.service.privacy.violation.filter;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
<<<<<<< HEAD
import org.iobserve.model.provider.neo4j.IModelProvider;
=======
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.privacy.violation.transformation.Edge;
import org.iobserve.service.privacy.violation.transformation.Graph;
import org.iobserve.service.privacy.violation.transformation.Vertice;
>>>>>>> Grapherstellung und Model Loading
import org.iobserve.stages.data.Warnings;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.CompositeDataType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationRequiredRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Privacy warner.
 *
 * @author Reiner Jung -- initial contribution
 *
 */
public class PrivacyWarner extends AbstractStage {

	private final ModelProvider<Allocation, Allocation> allocationModelGraphProvider;
	private final ModelProvider<System, System> systemModelGraphProvider;
	private final ModelProvider<ResourceEnvironment, ResourceEnvironment> resourceEnvironmentModelGraphProvider;
	private final ModelProvider<Repository, Repository> repositoryModelGraphProvider;

	private final InputPort<PCMDeployedEvent> deployedInputPort = this.createInputPort(PCMDeployedEvent.class);
	private final InputPort<PCMUndeployedEvent> undeployedInputPort = this.createInputPort(PCMUndeployedEvent.class);

	private final OutputPort<Warnings> probesOutputPort = this.createOutputPort(Warnings.class);
	private final OutputPort<Warnings> warningsOutputPort = this.createOutputPort(Warnings.class);

	private Allocation allocationRootElement;
	private System systemRootElement;
	private Repository repositoryRootElement;

	/**
	 * Create and initialize privacy warner.
	 *
	 * @param allocationModelGraphProvider
	 *            allocation model provider
	 * @param systemModelGraphProvider
	 *            system model provider
	 * @param resourceEnvironmentModelGraphProvider
	 *            resource environment model provider
	 */
	public PrivacyWarner(final ModelProvider<Allocation, Allocation> allocationModelGraphProvider,
			final ModelProvider<System, System> systemModelGraphProvider,
			final ModelProvider<ResourceEnvironment, ResourceEnvironment> resourceEnvironmentModelGraphProvider,
			final ModelProvider<Repository, Repository> repositoryModelGraphProvider) {
		this.allocationModelGraphProvider = allocationModelGraphProvider;
		this.systemModelGraphProvider = systemModelGraphProvider;
		this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
		this.repositoryModelGraphProvider = repositoryModelGraphProvider;
	}

	private void print(Object o) {
		java.lang.System.out.println(o);
	}

	private void print() {
		print("");
	}

	@Override
	protected void execute() throws Exception {
		java.lang.System.out.print("Execution started");
		final Warnings warnings = new Warnings();
		final PCMDeployedEvent deployedEvent = this.deployedInputPort.receive();
		final PCMUndeployedEvent undeployedEvent = this.undeployedInputPort.receive();
		this.createAnalysisGraph();
		if (deployedEvent != null) {
			// TODO generate warnings after the last deployment
			java.lang.System.out.print("Received Deployment");
			java.lang.System.out.print("CountryCode: " + deployedEvent.getCountryCode());
			java.lang.System.out.print("Service: " + deployedEvent.getService());
		}
		if (undeployedEvent != null) {
			// TODO generate warnings after the last undeployment
			java.lang.System.out.print("Received undeployment");
		}

		this.probesOutputPort.send(warnings);

		this.warningsOutputPort.send(warnings);
	}

	private void createAnalysisGraph() {
		Graph g = new Graph();
		HashMap<String, Vertice> vertices = new LinkedHashMap<String, Vertice>();
		allocationRootElement = allocationModelGraphProvider.readRootComponent(Allocation.class);
		systemRootElement = systemModelGraphProvider.readRootComponent(System.class);
		repositoryRootElement = repositoryModelGraphProvider.readRootComponent(Repository.class);
		print("******************************************************************************************************************");
		java.lang.System.out.println("Starting creation of Analysis Graph");
		// print("Allocation");
		// print();
		for (AllocationContext ac : allocationRootElement.getAllocationContexts_Allocation()) {
			// print("AllocationContext: " + ac.getEntityName());
			// print("AssemblyContext: " +
			// ac.getAssemblyContext_AllocationContext().getEntityName());
			RepositoryComponent rc = ac.getAssemblyContext_AllocationContext()
					.getEncapsulatedComponent__AssemblyContext();
			// print();
		}
		// print("System");
		// print();
		for (AssemblyContext ac : systemRootElement.getAssemblyContexts__ComposedStructure()) {
			// print("AssemblyContext: " + ac.getEntityName());
			RepositoryComponent rc = ac.getEncapsulatedComponent__AssemblyContext();
			if (rc != null) {
				// print(rc.getEntityName());
				Vertice v = new Vertice(rc.getEntityName());
				g.addVertice(v);
				vertices.put(rc.getEntityName(), v);
			} else
				print("Null");
			// print();
		}
		for (Interface inf : repositoryRootElement.getInterfaces__Repository()) {
			if (inf instanceof OperationInterface) {
				OperationInterface oi = (OperationInterface) inf;
				for (OperationSignature os : oi.getSignatures__OperationInterface()) {
					print(os.getReturnType__OperationSignature());
					for (Parameter p : os.getParameters__OperationSignature()) {
						
						DataType dt = p.getDataType__Parameter();
						if (dt instanceof CompositeDataType) {
							CompositeDataType cdt = (CompositeDataType) dt;
							print("Composite: "+cdt.getEntityName());
						}
						if (dt instanceof PrimitiveDataType) {
							PrimitiveDataType pdt = (PrimitiveDataType) dt;
							print("Enum: "+pdt.getType());
							print(pdt.getType().getLiteral());
							print(pdt.getType().getName());
						}
					}
				}
			}
			print(inf.getEntityName());

		}
		for (Connector c : systemRootElement.getConnectors__ComposedStructure()) {
			if (c instanceof AssemblyConnector) {
				AssemblyConnector ac = (AssemblyConnector) c;
				AssemblyContext provider = ac.getProvidingAssemblyContext_AssemblyConnector();
				RepositoryComponent rcProvider = provider.getEncapsulatedComponent__AssemblyContext();
				AssemblyContext requiring = ac.getRequiringAssemblyContext_AssemblyConnector();
				RepositoryComponent rcRequiring = requiring.getEncapsulatedComponent__AssemblyContext();
				if (rcProvider != null && rcRequiring != null) {
					OperationProvidedRole opr = ac.getProvidedRole_AssemblyConnector();
					OperationRequiredRole orr = ac.getRequiredRole_AssemblyConnector();
					print(opr.getEntityName());
					if (opr != null) {
						
						OperationInterface oi = orr.getRequiredInterface__OperationRequiredRole();
						print(orr.getRequiringEntity_RequiredRole());

						if (oi != null) {
							print("NOT NULLLLLL!!!!!");
							for (OperationSignature os : oi.getSignatures__OperationInterface()) {
								for (Parameter params : os.getParameters__OperationSignature()) {
									print(params.getParameterName());
								}
							}
						}
					}
					Edge e = new Edge(vertices.get(rcRequiring.getEntityName()),
							vertices.get(rcProvider.getEntityName()));
					g.addEdge(e);
				}
			}

		}
		print();
		print("End of Graph Analysis");

		g.printGraph();
		print("******************************************************************************************************************");
	}

	private String shortName(String s) {
		if (!s.contains("_"))
			return s;
		else {
			String tmp[] = s.split("_");
			return tmp[tmp.length - 1];
		}

	}

	public OutputPort<Warnings> getProbesOutputPort() {
		return this.probesOutputPort;
	}

	public OutputPort<Warnings> getWarningsOutputPort() {
		return this.warningsOutputPort;
	}

	public InputPort<PCMDeployedEvent> getDeployedInputPort() {
		return this.deployedInputPort;
	}

	public InputPort<PCMUndeployedEvent> getUndeployedInputPort() {
		return this.undeployedInputPort;
	}


}
