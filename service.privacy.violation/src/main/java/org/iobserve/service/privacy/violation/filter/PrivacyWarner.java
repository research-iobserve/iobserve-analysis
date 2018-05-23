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

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.privacy.violation.transformation.Vertice;
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

/**
 * Privacy warner.
 *
 * @author Reiner Jung -- initial contribution
 *
 */
public class PrivacyWarner extends AbstractStage {

    private final IModelProvider<Allocation> allocationModelGraphProvider;
    private final IModelProvider<System> systemModelGraphProvider;
    private final IModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;
    private final IModelProvider<Repository> repositoryModelGraphProvider;

    private final ModelProvider<System, AssemblyContext> assemblyContextProvider;

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
    public PrivacyWarner(final IModelProvider<Allocation> allocationModelGraphProvider,
            final IModelProvider<System> systemModelGraphProvider,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final IModelProvider<Repository> repositoryModelGraphProvider) {
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
        this.repositoryModelGraphProvider = repositoryModelGraphProvider;

        this.assemblyContextProvider = new ModelProvider<>(systemModelGraphProvider.getGraph(),
                ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
    }

    private void print(final Object o) {
        java.lang.System.out.println(o);
    }

    private void print() {
        this.print("");
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
        final Graph g = new Graph();
        final HashMap<String, Vertice> vertices = new LinkedHashMap<String, Vertice>();
        this.allocationRootElement = this.allocationModelGraphProvider.readRootComponent(Allocation.class);
        this.systemRootElement = this.systemModelGraphProvider.readRootComponent(System.class);
        this.repositoryRootElement = this.repositoryModelGraphProvider.readRootComponent(Repository.class);
        this.print(
                "******************************************************************************************************************");
        java.lang.System.out.println("Starting creation of Analysis Graph");
        // print("Allocation");
        // print();
        for (final AllocationContext ac : this.allocationRootElement.getAllocationContexts_Allocation()) {
            // print("AllocationContext: " + ac.getEntityName());
            // print("AssemblyContext: " +
            // ac.getAssemblyContext_AllocationContext().getEntityName());
            final RepositoryComponent rc = ac.getAssemblyContext_AllocationContext()
                    .getEncapsulatedComponent__AssemblyContext();
            // print();
        }
        // print("System");
        // print();
        for (final AssemblyContext ac : this.systemRootElement.getAssemblyContexts__ComposedStructure()) {
            // print("AssemblyContext: " + ac.getEntityName());
            final RepositoryComponent rc = ac.getEncapsulatedComponent__AssemblyContext();
            if (rc != null) {
                // print(rc.getEntityName());
                final Vertice v = new Vertice(rc.getEntityName());
                g.addVertice(v);
                vertices.put(rc.getEntityName(), v);
            } else {
                this.print("Null");
                // print();
            }
        }
        for (final Interface inf : this.repositoryRootElement.getInterfaces__Repository()) {
            if (inf instanceof OperationInterface) {
                final OperationInterface oi = (OperationInterface) inf;
                for (final OperationSignature os : oi.getSignatures__OperationInterface()) {
                    this.print(os.getReturnType__OperationSignature());
                    for (final Parameter p : os.getParameters__OperationSignature()) {
                        this.print(p.getModifier__Parameter());
                        final DataType dt = p.getDataType__Parameter();
                        if (dt instanceof CompositeDataType) {
                            final CompositeDataType cdt = (CompositeDataType) dt;
                            this.print("Composite: " + cdt.getEntityName());
                        }
                        if (dt instanceof PrimitiveDataType) {
                            final PrimitiveDataType pdt = (PrimitiveDataType) dt;
                            this.print("Enum: " + pdt.getType());
                            this.print(pdt.getType().getLiteral());
                            this.print(pdt.getType().getName());
                        }
                    }
                }
            }
            this.print(inf.getEntityName());

        }
        for (final Connector c : this.systemRootElement.getConnectors__ComposedStructure()) {
            if (c instanceof AssemblyConnector) {
                final AssemblyConnector ac = (AssemblyConnector) c;
                final AssemblyContext provider = ac.getProvidingAssemblyContext_AssemblyConnector();
                final RepositoryComponent rcProvider = provider.getEncapsulatedComponent__AssemblyContext();
                final AssemblyContext requiring = ac.getRequiringAssemblyContext_AssemblyConnector();
                final RepositoryComponent rcRequiring = requiring.getEncapsulatedComponent__AssemblyContext();
                if (rcProvider != null && rcRequiring != null) {
                    final OperationProvidedRole opr = ac.getProvidedRole_AssemblyConnector();
                    final OperationRequiredRole orr = ac.getRequiredRole_AssemblyConnector();
                    this.print(opr.getEntityName());
                    if (opr != null) {

                        final OperationInterface oi = orr.getRequiredInterface__OperationRequiredRole();
                        this.print(orr.getRequiringEntity_RequiredRole());

                        if (oi != null) {
                            this.print("NOT NULLLLLL!!!!!");
                            for (final OperationSignature os : oi.getSignatures__OperationInterface()) {
                                for (final Parameter params : os.getParameters__OperationSignature()) {
                                    this.print(params.getParameterName());
                                }
                            }
                        }
                    }
                    final Edge e = new Edge(vertices.get(rcRequiring.getEntityName()),
                            vertices.get(rcProvider.getEntityName()));
                    g.addEdge(e);
                }
            }

        }
        this.print();
        this.print("End of Graph Analysis");

        g.printGraph();
        this.print(
                "******************************************************************************************************************");
    }

    private String shortName(final String s) {
        if (!s.contains("_")) {
            return s;
        } else {
            final String tmp[] = s.split("_");
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
