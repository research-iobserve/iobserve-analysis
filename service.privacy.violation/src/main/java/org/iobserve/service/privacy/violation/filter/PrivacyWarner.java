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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.model.persistence.neo4j.IModelProvider;
import org.iobserve.model.persistence.neo4j.ModelProvider;
import org.iobserve.model.privacy.EDataPrivacyLevel;
import org.iobserve.model.privacy.EncapsulatedDataSource;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.IPrivacyAnnotation;
import org.iobserve.model.privacy.ParameterPrivacy;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.ReturnTypePrivacy;
import org.iobserve.model.test.data.DebugHelper;
import org.iobserve.service.privacy.violation.PrivacyConfigurationsKeys;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.PrivacyGraph;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Vertex;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Vertex.EStereoType;
import org.iobserve.service.privacy.violation.transformation.privacycheck.Policy;
import org.iobserve.service.privacy.violation.transformation.privacycheck.PrivacyChecker;
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
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

import kieker.common.configuration.Configuration;
import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Privacy warner.
 *
 * @author Reiner Jung -- initial contribution
 * @author Clemens Brackmann -- analysis graph
 *
 */
public class PrivacyWarner extends AbstractStage {

    private final IModelProvider<Allocation> allocationModelProvider;
    private final IModelProvider<System> systemModelProvider;
    private final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider;
    private final IModelProvider<Repository> repositoryModelProvider;
    private final IModelProvider<PrivacyModel> privacyModelProvider;

    private final InputPort<PCMDeployedEvent> deployedInputPort = this.createInputPort(PCMDeployedEvent.class);
    private final InputPort<PCMUndeployedEvent> undeployedInputPort = this.createInputPort(PCMUndeployedEvent.class);

    private final OutputPort<Warnings> probesOutputPort = this.createOutputPort(Warnings.class);
    private final OutputPort<Warnings> warningsOutputPort = this.createOutputPort(Warnings.class);

    /** HashMaps for faster queries. **/
    private final Map<String, GeoLocation> geolocations = new LinkedHashMap<>();
    private final Map<String, EncapsulatedDataSource> stereotypes = new LinkedHashMap<>();
    private final Map<String, ParameterPrivacy> parameterprivacy = new LinkedHashMap<>();
    private final Map<String, ReturnTypePrivacy> returntypeprivacy = new LinkedHashMap<>();
    private final Map<String, OperationInterface> interfaces = new LinkedHashMap<>();

    private final Map<String, Vertex> vertices = new LinkedHashMap<>();

    private Allocation allocationRootElement;
    private System systemRootElement;
    private Repository repositoryRootElement;
    private PrivacyModel privacyRootElement;
    private final String[] policyList;
    private final String policyPackage;

    /**
     * Create and initialize privacy warner.
     *
     * @param configuration
     *            configuration object
     * @param allocationModelProvider
     *            allocation model provider
     * @param systemModelProvider
     *            system model provider
     * @param resourceEnvironmentModelProvider
     *            resource environment model provider
     * @param repositoryModelProvider
     *            repository model provider
     * @param privacyModelProvider
     *            privacy model provider
     * @param assemblyView2
     */
    public PrivacyWarner(final Configuration configuration, final IModelProvider<Allocation> allocationModelProvider,
            final IModelProvider<System> systemModelProvider,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final IModelProvider<Repository> repositoryModelProvider,
            final IModelProvider<PrivacyModel> privacyModelProvider) {

        /** get policy parameters. */
        this.policyPackage = configuration.getStringProperty(PrivacyConfigurationsKeys.POLICY_PACKAGE_PREFIX);
        this.policyList = configuration.getStringArrayProperty(PrivacyConfigurationsKeys.POLICY_LIST);

        this.allocationModelProvider = allocationModelProvider;
        this.systemModelProvider = systemModelProvider;
        this.resourceEnvironmentModelProvider = resourceEnvironmentModelProvider;
        this.repositoryModelProvider = repositoryModelProvider;
        this.privacyModelProvider = privacyModelProvider;
    }

    @Override
    protected void execute() throws Exception {
        final PCMDeployedEvent deployedEvent = this.deployedInputPort.receive();
        final PCMUndeployedEvent undeployedEvent = this.undeployedInputPort.receive();

        if (deployedEvent != null) {
            this.logger.debug("Received Deployment");
            this.logger.debug("CountryCode: " + deployedEvent.getCountryCode());
            this.logger.debug("Service: " + deployedEvent.getService());
            this.performPrivacyEvaluation();
        }

        if (undeployedEvent != null) {
            this.logger.debug("Received undeployment");
            this.performPrivacyEvaluation();
        }
    }

    private void performPrivacyEvaluation() throws FileNotFoundException, InstantiationException,
            IllegalAccessException, ClassNotFoundException, IOException {
        final PrivacyGraph graph = this.createAnalysisGraph();

        final Warnings warnings = this.checkGraph(graph);

        this.probesOutputPort.send(warnings);
        this.warningsOutputPort.send(warnings);
    }

    private Warnings checkGraph(final PrivacyGraph graph) throws FileNotFoundException, InstantiationException,
            IllegalAccessException, ClassNotFoundException, IOException {
        final Warnings warnings = new Warnings();
        final PrivacyChecker privacyChecker = new PrivacyChecker(this.policyList, this.policyPackage);
        final List<Edge> edges = privacyChecker.check(graph);

        for (final Edge edge : edges) {
            warnings.addMessage(edge.getPrint() + " Interface: " + edge.getInterfaceName());
            warnings.addWarningEdge(edge);
        }
        warnings.setDate(new Date());

        return warnings;
    }

    private PrivacyGraph createAnalysisGraph() {
        final PrivacyGraph graph = new PrivacyGraph("PrivacyWarner");

        this.loadRoots();

        /** AssemblyContext View **/
        final IModelProvider<AssemblyContext> assemblyContextModelProvider = new ModelProvider<>(
                this.systemModelProvider.getResource(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        /** RepositoryComponent View **/
        final IModelProvider<BasicComponent> repositoryComponentModelProvider = new ModelProvider<>(
                this.repositoryModelProvider.getResource(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        /** ResourceContainer View **/
        final IModelProvider<ResourceContainer> resourceContainerModelProvider = new ModelProvider<>(
                this.resourceEnvironmentModelProvider.getResource(), ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        // Fill the hashmaps
        this.clearAndFillQueryMaps();

        this.addDeployedComponents(graph, assemblyContextModelProvider, repositoryComponentModelProvider,
                resourceContainerModelProvider);

        this.addConnectors(graph);

        return graph;
    }

    /**
     * Loads the root element for each model.
     **/
    private void loadRoots() {
        this.allocationRootElement = this.allocationModelProvider.getModelRootNode(Allocation.class);
        DebugHelper.printModelPartition(this.allocationRootElement);
        this.systemRootElement = this.systemModelProvider.getModelRootNode(System.class);
        this.repositoryRootElement = this.repositoryModelProvider.getModelRootNode(Repository.class);
        this.privacyRootElement = this.privacyModelProvider.getModelRootNode(PrivacyModel.class);
        DebugHelper.printModelPartition(this.privacyRootElement);
    }

    /**
     * Adding deployment info.
     *
     * @param graph
     *            the graph
     * @param assemblyContextModelProvider
     *            assembly model view on assembly contexts
     * @param repositoryComponentModelProvider
     *            repository model view on components
     * @param resourceContainerModelProvider
     *            resource environment model view on resource container
     */
    private void addDeployedComponents(final PrivacyGraph graph,
            final IModelProvider<AssemblyContext> assemblyContextModelProvider,
            final IModelProvider<BasicComponent> repositoryComponentModelProvider,
            final IModelProvider<ResourceContainer> resourceContainerModelProvider) {
        for (final AllocationContext allocationContext : this.allocationRootElement
                .getAllocationContexts_Allocation()) {
            final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();
            final AssemblyContext queryAssemblyContext = assemblyContextModelProvider
                    .findObjectByTypeAndId(AssemblyContext.class, assemblyContext.getId());
            final RepositoryComponent repositoryComponent = queryAssemblyContext
                    .getEncapsulatedComponent__AssemblyContext();
            final BasicComponent basicComponent = repositoryComponentModelProvider
                    .findObjectByTypeAndId(BasicComponent.class, repositoryComponent.getId());

            /** Creating component vertices. **/
            final Vertex v = new Vertex(basicComponent.getEntityName(),
                    this.stereotypes.get(basicComponent.getId()) != null
                            ? this.stereotypes.get(basicComponent.getId()).isDataSource() ? EStereoType.DATASOURCE
                                    : EStereoType.COMPUTING_NODE
                            : EStereoType.COMPUTING_NODE);
            v.setAllocation(allocationContext.getAllocation_AllocationContext());
            graph.addVertex(v);
            this.vertices.put(basicComponent.getId(), v);

            final ResourceContainer queryResource = resourceContainerModelProvider.findObjectByTypeAndId(
                    ResourceContainer.class, allocationContext.getResourceContainer_AllocationContext().getId());
            final GeoLocation geo = this.geolocations.get(queryResource.getId());
            final Vertex vGeo = new Vertex(geo.getIsocode().getName(), EStereoType.GEOLOCATION);
            if (!this.vertices.containsKey(geo.getIsocode().getName())) { // New Geolocation
                graph.addVertex(vGeo);
                graph.addEdge(vGeo, v);
                this.vertices.put(geo.getIsocode().getName(), vGeo);
            } else { // Existing Geolocation
                graph.addEdge(this.vertices.get(geo.getIsocode().getName()), v);

            }
        }
    }

    /**
     * Adding connections between components to the graph.
     *
     * @param graph
     *            graph
     */
    private void addConnectors(final PrivacyGraph graph) {
        for (final Connector connector : this.systemRootElement.getConnectors__ComposedStructure()) {
            if (connector instanceof AssemblyConnector) {
                final AssemblyConnector assemblyConnector = (AssemblyConnector) connector;

                // Providing Component
                final AssemblyContext provididingAssemblyContext = assemblyConnector
                        .getProvidingAssemblyContext_AssemblyConnector();
                final RepositoryComponent providingComponent = provididingAssemblyContext
                        .getEncapsulatedComponent__AssemblyContext();

                // Requiring Component
                final AssemblyContext requiringAssemblyContext = assemblyConnector
                        .getRequiringAssemblyContext_AssemblyConnector();
                final RepositoryComponent requiringComponent = requiringAssemblyContext
                        .getEncapsulatedComponent__AssemblyContext();

                if ((providingComponent != null) && (requiringComponent != null)) {
                    final OperationProvidedRole providedRole = assemblyConnector.getProvidedRole_AssemblyConnector();
                    final String interfaceName = this.shortName(providedRole.getEntityName());

                    // Check Interface Name in Repository and add Edge
                    final OperationInterface operationInterface = this.interfaces.get(interfaceName);
                    this.computePrivacyLevelsAndAddEdge(graph, operationInterface, providingComponent,
                            requiringComponent);

                }
            }

        }
    }

    private void computePrivacyLevelsAndAddEdge(final PrivacyGraph graph, final OperationInterface operationInterface,
            final RepositoryComponent providingComponent, final RepositoryComponent requiringComponent) {
        for (final OperationSignature operationSignature : operationInterface.getSignatures__OperationInterface()) {
            final IPrivacyAnnotation operationSignaturePrivacyAnnotation = this.returntypeprivacy
                    .get(operationSignature.getId());
            EDataPrivacyLevel inEdgePrivacyLevel = null;
            EDataPrivacyLevel outEdgePrivacyLevel = null;

            /** Check parameter. */
            for (final Parameter parameter : operationSignature.getParameters__OperationSignature()) {
                final ParameterModifier mod = parameter.getModifier__Parameter();
                if ((mod == ParameterModifier.IN) || (mod == ParameterModifier.INOUT)) {
                    outEdgePrivacyLevel = this.updatePrivacyLevel(outEdgePrivacyLevel,
                            this.parameterprivacy.get(parameter.getParameterName()).getLevel());
                }
                if ((mod == ParameterModifier.OUT) || (mod == ParameterModifier.INOUT)) {
                    inEdgePrivacyLevel = this.updatePrivacyLevel(inEdgePrivacyLevel,
                            this.parameterprivacy.get(parameter.getParameterName()).getLevel());
                }
            }

            /** Check return type. */
            if (operationSignaturePrivacyAnnotation != null) {
                inEdgePrivacyLevel = this.updatePrivacyLevel(inEdgePrivacyLevel,
                        operationSignaturePrivacyAnnotation.getLevel());
            }

            // Add Edges
            if (inEdgePrivacyLevel != null) {
                final Edge edge = new Edge(this.vertices.get(providingComponent.getId()),
                        this.vertices.get(requiringComponent.getId()));
                edge.setDPC(Policy.getDataClassification(inEdgePrivacyLevel));
                edge.setInterfaceName(operationSignature.getEntityName());
                graph.addEdge(edge);
            }
            if (outEdgePrivacyLevel != null) {
                final Edge edge = new Edge(this.vertices.get(requiringComponent.getId()),
                        this.vertices.get(providingComponent.getId()));
                edge.setDPC(Policy.getDataClassification(outEdgePrivacyLevel));
                edge.setInterfaceName(operationSignature.getEntityName());
                graph.addEdge(edge);
            }

        }
    }

    /**
     * Increase the privacy, in case the new privacy level is higher than the current one.
     *
     * @param currentPrivacyLevel
     *            current privacy level of an edge
     * @param newPrivacyLevel
     *            new privacy level
     * @return the resulting privacy level
     */
    private EDataPrivacyLevel updatePrivacyLevel(final EDataPrivacyLevel currentPrivacyLevel,
            final EDataPrivacyLevel newPrivacyLevel) {
        if (currentPrivacyLevel != null) {
            if (this.isMoreCritical(currentPrivacyLevel, newPrivacyLevel)) {
                return newPrivacyLevel;
            } else {
                return currentPrivacyLevel;
            }
        } else {
            return newPrivacyLevel;
        }
    }

    /**
     * Fills the hash maps used for queries.
     **/
    private void clearAndFillQueryMaps() {
        this.vertices.clear();
        this.geolocations.clear();
        this.stereotypes.clear();
        this.parameterprivacy.clear();
        this.returntypeprivacy.clear();
        this.interfaces.clear();

        for (final GeoLocation location : this.privacyRootElement.getResourceContainerLocations()) {
            this.geolocations.put(location.getResourceContainer().getId(), location);
        }
        for (final EncapsulatedDataSource stereotype : this.privacyRootElement.getEncapsulatedDataSources()) {
            this.stereotypes.put(stereotype.getComponent().getId(), stereotype);
        }
        for (final IPrivacyAnnotation privacyAnnocation : this.privacyRootElement.getPrivacyLevels()) {
            if (privacyAnnocation instanceof ParameterPrivacy) {
                final ParameterPrivacy parameterPrivacy = (ParameterPrivacy) privacyAnnocation;
                this.parameterprivacy.put(parameterPrivacy.getParameter().getParameterName(), parameterPrivacy);
            }
            if (privacyAnnocation instanceof ReturnTypePrivacy) {
                final ReturnTypePrivacy returnTypePrivacy = (ReturnTypePrivacy) privacyAnnocation;
                this.returntypeprivacy.put(returnTypePrivacy.getOperationSignature().getId(), returnTypePrivacy);
            }
        }

        for (final Interface inf : this.repositoryRootElement.getInterfaces__Repository()) {
            if (inf instanceof OperationInterface) {
                this.interfaces.put(inf.getEntityName(), (OperationInterface) inf);
            }
        }
    }

    // TODO better method name
    private boolean isMoreCritical(final EDataPrivacyLevel basis, final EDataPrivacyLevel compared) {
        return compared.ordinal() > basis.ordinal();
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
