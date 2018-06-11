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

import java.util.LinkedHashMap;
import java.util.Map;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.IPrivacyAnnotation;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.privacy.violation.transformation.Edge;
import org.iobserve.service.privacy.violation.transformation.Graph;
import org.iobserve.service.privacy.violation.transformation.Vertice;
import org.iobserve.stages.data.Warnings;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.repository.BasicComponent;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.ParameterModifier;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * Privacy warner.
 *
 * @author Reiner Jung -- initial contribution
 * @author Clemens Brackmann -- analysis algorithm
 *
 */
public class PrivacyWarner extends AbstractStage {

    private final IModelProvider<Allocation> allocationModelGraphProvider;
    private final IModelProvider<System> systemModelGraphProvider;
    private final IModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;
    private final IModelProvider<Repository> repositoryModelGraphProvider;
    private final IModelProvider<PrivacyModel> privacyModelGraphProvider;

    private final InputPort<PCMDeployedEvent> deployedInputPort = this.createInputPort(PCMDeployedEvent.class);
    private final InputPort<PCMUndeployedEvent> undeployedInputPort = this.createInputPort(PCMUndeployedEvent.class);

    private final OutputPort<Warnings> probesOutputPort = this.createOutputPort(Warnings.class);
    private final OutputPort<Warnings> warningsOutputPort = this.createOutputPort(Warnings.class);

    private Allocation allocationRootElement;
    private System systemRootElement;
    private Repository repositoryRootElement;
    private ResourceEnvironment resEnvRootElement;
    private PrivacyModel privacyRootElement;

    /**
     * Create and initialize privacy warner.
     *
     * @param allocationModelGraphProvider
     *            allocation model provider
     * @param systemModelGraphProvider
     *            system model provider
     * @param resourceEnvironmentModelGraphProvider
     *            resource environment model provider
     * @param assemblyView2
     */
    public PrivacyWarner(final IModelProvider<Allocation> allocationModelGraphProvider,
            final IModelProvider<System> systemModelGraphProvider,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final IModelProvider<Repository> repositoryModelGraphProvider,
            final IModelProvider<PrivacyModel> privacyModelGraphProvider) {
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
        this.repositoryModelGraphProvider = repositoryModelGraphProvider;
        this.privacyModelGraphProvider = privacyModelGraphProvider;
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

        final Map<String, Vertice> vertices = new LinkedHashMap<>();
        this.allocationRootElement = this.allocationModelGraphProvider.readRootNode(Allocation.class);
        this.systemRootElement = this.systemModelGraphProvider.readRootNode(System.class);
        this.repositoryRootElement = this.repositoryModelGraphProvider.readRootNode(Repository.class);
        this.resEnvRootElement = this.resourceEnvironmentModelGraphProvider
                .readRootNode(ResourceEnvironment.class);
        this.privacyRootElement = this.privacyModelGraphProvider.readRootNode(PrivacyModel.class);
        /** AssemblyContext View **/
        final IModelProvider<AssemblyContext> assemblyContextModelProvider = new ModelProvider<>(
                this.systemModelGraphProvider.getGraph(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        /** RepositoryComponent View **/
        final IModelProvider<BasicComponent> repositoryComponentModelProvider = new ModelProvider<>(
                this.repositoryModelGraphProvider.getGraph(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        /** OperationInterface View **/
        final IModelProvider<OperationInterface> operationInterfaceModelProvider = new ModelProvider<>(
                this.repositoryModelGraphProvider.getGraph(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        /** ResourceContainer View **/
        final IModelProvider<ResourceContainer> resourceContainerModelProvider = new ModelProvider<>(
                this.resourceEnvironmentModelGraphProvider.getGraph(), ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);

        this.print(
                "******************************************************************************************************************");
        this.print("Starting creation of Analysis Graph");

        for (final AllocationContext ac : this.allocationRootElement.getAllocationContexts_Allocation()) {
            final AssemblyContext asc = ac.getAssemblyContext_AllocationContext();
            final AssemblyContext queryAssemblyContext = assemblyContextModelProvider
                    .readObjectById(AssemblyContext.class, asc.getId());
            final RepositoryComponent rc = queryAssemblyContext.getEncapsulatedComponent__AssemblyContext();

            final BasicComponent bc = repositoryComponentModelProvider.readObjectById(BasicComponent.class,
                    rc.getId());
            //
            for (final ProvidedRole or : bc.getProvidedRoles_InterfaceProvidingEntity()) {

                if (or instanceof OperationProvidedRole) {
                    final OperationProvidedRole opr = (OperationProvidedRole) or;
                    this.print(opr.getProvidedInterface__OperationProvidedRole().getEntityName());
                }
            }
            //
            /** Creating component vertices **/
            final Vertice v = new Vertice(bc.getEntityName());
            g.addVertice(v);
            vertices.put(bc.getId(), v);
            // TODO Effizienter machen falls möglich
            final ResourceContainer queryResource = resourceContainerModelProvider.readObjectById(
                    ResourceContainer.class, ac.getResourceContainer_AllocationContext().getId());

            if (this.privacyRootElement != null) {
                for (final GeoLocation geo : this.privacyRootElement.getResourceContainerLocations()) {
                    this.print("GEO ID: " + geo.getResourceContainer().getId() + " "
                            + geo.getResourceContainer().getEntityName() + " is proxy "
                            + geo.getResourceContainer().eIsProxy());

                    this.print("RESOURCE ID: " + queryResource.getId());
                    if (geo.getResourceContainer().getId() == queryResource.getId()) {
                        final Vertice vGeo = new Vertice(geo.getIsocode().getName());
                        g.addVertice(vGeo);
                        g.addEdge(vGeo, v);
                    }

                }
            } else {
                this.print("PRIVACY ROOT IS NULL");
            }
        }
        for (final GeoLocation geo : this.privacyRootElement.getResourceContainerLocations()) {
            this.print(geo.getIsocode().getName());
            final ResourceContainer tmp = geo.getResourceContainer();
            final String s = tmp.getId();
            final ResourceContainer rc = resourceContainerModelProvider.readObjectById(ResourceContainer.class,
                    s);
        }

        /** Adding connections between components to the graph **/
        for (final Connector c : this.systemRootElement.getConnectors__ComposedStructure()) {
            if (c instanceof AssemblyConnector) {
                final AssemblyConnector ac = (AssemblyConnector) c;
                // Providing Component
                final AssemblyContext provider = ac.getProvidingAssemblyContext_AssemblyConnector();
                final RepositoryComponent rcProvider = provider.getEncapsulatedComponent__AssemblyContext();
                // Requiring Component
                final AssemblyContext requiring = ac.getRequiringAssemblyContext_AssemblyConnector();
                final RepositoryComponent rcRequiring = requiring.getEncapsulatedComponent__AssemblyContext();

                if (rcProvider != null && rcRequiring != null) {
                    final OperationProvidedRole opr = ac.getProvidedRole_AssemblyConnector();
                    this.print(opr.getEntityName());
                    final String interfaceName = this.shortName(opr.getEntityName());
                    // Check Interface Name in Repository and add Edge
                    for (final Interface inf : this.repositoryRootElement.getInterfaces__Repository()) {

                        if (inf instanceof OperationInterface) {
                            if (!inf.getEntityName().equals(interfaceName)) {
                                continue;
                            }

                            final OperationInterface oi = (OperationInterface) inf;

                            for (final OperationSignature os : oi.getSignatures__OperationInterface()) {

                                for (final Parameter p : os.getParameters__OperationSignature()) {
                                    for (final IPrivacyAnnotation ipa : this.privacyRootElement.getPrivacyLevels()) {

                                    }
                                    if (p.getModifier__Parameter() == ParameterModifier.IN) {
                                        final Edge e = new Edge(vertices.get(rcRequiring.getId()),
                                                vertices.get(rcProvider.getId()));
                                        // e.setDPC(s);//TODO
                                        g.addEdge(e);
                                    }
                                    if (p.getModifier__Parameter() == ParameterModifier.OUT) {
                                        final Edge e = new Edge(vertices.get(rcProvider.getId()),
                                                vertices.get(rcRequiring.getId()));

                                        g.addEdge(e);
                                    }
                                    if (p.getModifier__Parameter() == ParameterModifier.INOUT) {

                                        final Edge e1 = new Edge(vertices.get(rcProvider.getId()),
                                                vertices.get(rcRequiring.getId()));
                                        final Edge e2 = new Edge(vertices.get(rcRequiring.getId()),
                                                vertices.get(rcProvider.getId()));
                                        g.addEdge(e1);
                                        g.addEdge(e2);
                                    }
                                }
                            }

                        }

                    }
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
            final String[] tmp = s.split("_");
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
