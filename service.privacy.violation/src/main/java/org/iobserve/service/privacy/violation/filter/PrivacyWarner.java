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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.model.privacy.EDataPrivacyLevel;
import org.iobserve.model.privacy.EncapsulatedDataSource;
import org.iobserve.model.privacy.GeoLocation;
import org.iobserve.model.privacy.IPrivacyAnnotation;
import org.iobserve.model.privacy.ParameterPrivacy;
import org.iobserve.model.privacy.PrivacyModel;
import org.iobserve.model.privacy.ReturnTypePrivacy;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Graph;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Vertice;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Vertice.STEREOTYPES;
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

    private final IModelProvider<Allocation> allocationModelGraphProvider;
    private final IModelProvider<System> systemModelGraphProvider;
    private final IModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;
    private final IModelProvider<Repository> repositoryModelGraphProvider;
    private final IModelProvider<PrivacyModel> privacyModelGraphProvider;

    private final InputPort<PCMDeployedEvent> deployedInputPort = this.createInputPort(PCMDeployedEvent.class);
    private final InputPort<PCMUndeployedEvent> undeployedInputPort = this.createInputPort(PCMUndeployedEvent.class);

    private final OutputPort<Warnings> probesOutputPort = this.createOutputPort(Warnings.class);
    private final OutputPort<Warnings> warningsOutputPort = this.createOutputPort(Warnings.class);

    /** HashMaps for faster queries **/
    final Map<String, GeoLocation> geolocations = new LinkedHashMap<>();
    final Map<String, EncapsulatedDataSource> stereotypes = new LinkedHashMap<>();
    final Map<String, ParameterPrivacy> parameterprivacy = new LinkedHashMap<>();
    final Map<String, ReturnTypePrivacy> returntypeprivacy = new LinkedHashMap<>();
    final Map<String, OperationInterface> interfaces = new LinkedHashMap<>();

    final Map<String, Vertice> vertices = new LinkedHashMap<>();

    private Allocation allocationRootElement;
    private System systemRootElement;
    private Repository repositoryRootElement;
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
        Warnings warnings = new Warnings();
        final PCMDeployedEvent deployedEvent = this.deployedInputPort.receive();
        final PCMUndeployedEvent undeployedEvent = this.undeployedInputPort.receive();
        final Graph g = this.createAnalysisGraph();
        if (deployedEvent != null) {
            // TODO generate warnings after the last deployment
            this.print("Received Deployment");
            this.print("CountryCode: " + deployedEvent.getCountryCode());
            this.print("Service: " + deployedEvent.getService());
        }
        if (undeployedEvent != null) {
            // TODO generate warnings after the last undeployment
            this.print("Received undeployment");
        }
        warnings = this.checkGraph(g);
        this.probesOutputPort.send(warnings);

        this.warningsOutputPort.send(warnings);
    }

    private Warnings checkGraph(final Graph g) throws FileNotFoundException, InstantiationException,
            IllegalAccessException, ClassNotFoundException, IOException {
        final Warnings w = new Warnings();
        final PrivacyChecker p = new PrivacyChecker();
        final Vector<Edge> edges = p.check(g);

        for (final Edge edge : edges) {
            w.addMessage(edge.getPrint() + " Interface: " + edge.getInterfaceName());
        }
        return w;
    }

    private Graph createAnalysisGraph() {
        final Graph g = new Graph("PrivacyWarner");

        this.loadRoots();
        /** AssemblyContext View **/
        final IModelProvider<AssemblyContext> assemblyContextModelProvider = new ModelProvider<>(
                this.systemModelGraphProvider.getGraph(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        /** RepositoryComponent View **/
        final IModelProvider<BasicComponent> repositoryComponentModelProvider = new ModelProvider<>(
                this.repositoryModelGraphProvider.getGraph(), ModelProvider.PCM_ENTITY_NAME, ModelProvider.PCM_ID);
        /** ResourceContainer View **/
        final IModelProvider<ResourceContainer> resourceContainerModelProvider = new ModelProvider<>(
                this.resourceEnvironmentModelGraphProvider.getGraph(), ModelProvider.PCM_ENTITY_NAME,
                ModelProvider.PCM_ID);
        // Fill the hashmaps
        this.clearAndFillQueryMaps();

        this.addDeployedComponents(g, assemblyContextModelProvider, repositoryComponentModelProvider,
                resourceContainerModelProvider);

        this.addConnectors(g);

        return g;
    }

    /** Loads the rootelement for eacoh model **/
    private void loadRoots() {
        this.allocationRootElement = this.allocationModelGraphProvider.readRootNode(Allocation.class);
        this.systemRootElement = this.systemModelGraphProvider.readRootNode(System.class);
        this.repositoryRootElement = this.repositoryModelGraphProvider.readRootNode(Repository.class);
        this.privacyRootElement = this.privacyModelGraphProvider.readRootNode(PrivacyModel.class);
    }

    /** Adding deployment info **/
    private void addDeployedComponents(final Graph g,
            final IModelProvider<AssemblyContext> assemblyContextModelProvider,
            final IModelProvider<BasicComponent> repositoryComponentModelProvider,
            final IModelProvider<ResourceContainer> resourceContainerModelProvider) {
        for (final AllocationContext ac : this.allocationRootElement.getAllocationContexts_Allocation()) {
            final AssemblyContext asc = ac.getAssemblyContext_AllocationContext();
            final AssemblyContext queryAssemblyContext = assemblyContextModelProvider
                    .readObjectById(AssemblyContext.class, asc.getId());
            final RepositoryComponent rc = queryAssemblyContext.getEncapsulatedComponent__AssemblyContext();
            final BasicComponent bc = repositoryComponentModelProvider.readObjectById(BasicComponent.class, rc.getId());
            /** Creating component vertices **/
            final Vertice v = new Vertice(bc.getEntityName(),
                    this.stereotypes.get(bc.getId()) != null
                            ? this.stereotypes.get(bc.getId()).isDataSource() ? STEREOTYPES.Datasource
                                    : STEREOTYPES.ComputingNode
                            : STEREOTYPES.ComputingNode);
            g.addVertice(v);
            this.vertices.put(bc.getId(), v);

            final ResourceContainer queryResource = resourceContainerModelProvider
                    .readObjectById(ResourceContainer.class, ac.getResourceContainer_AllocationContext().getId());
            final GeoLocation geo = this.geolocations.get(queryResource.getId());
            final Vertice vGeo = new Vertice(geo.getIsocode().getName(), STEREOTYPES.Geolocation);
            if (!this.vertices.containsKey(geo.getIsocode().getName())) {// New Geolocation
                g.addVertice(vGeo);
                g.addEdge(vGeo, v);
                this.vertices.put(geo.getIsocode().getName(), vGeo);
            } else {// Existing Geolocation
                g.addEdge(this.vertices.get(geo.getIsocode().getName()), v);

            }
        }
    }

    /** Adding connections between components to the graph **/
    private void addConnectors(final Graph g) {
        for (final Connector c : this.systemRootElement.getConnectors__ComposedStructure()) {
            if (c instanceof AssemblyConnector) {
                final AssemblyConnector ac = (AssemblyConnector) c;
                // Providing Component
                final AssemblyContext provider = ac.getProvidingAssemblyContext_AssemblyConnector();
                final RepositoryComponent rcProvider = provider.getEncapsulatedComponent__AssemblyContext();
                // Requiring Component
                final AssemblyContext requiring = ac.getRequiringAssemblyContext_AssemblyConnector();
                final RepositoryComponent rcRequiring = requiring.getEncapsulatedComponent__AssemblyContext();
                if ((rcProvider != null) && (rcRequiring != null)) {
                    final OperationProvidedRole opr = ac.getProvidedRole_AssemblyConnector();
                    final String interfaceName = this.shortName(opr.getEntityName());
                    // Check Interface Name in Repository and add Edge
                    final OperationInterface oi = this.interfaces.get(interfaceName);
                    for (final OperationSignature os : oi.getSignatures__OperationInterface()) {
                        final IPrivacyAnnotation ipa_os = this.returntypeprivacy.get(os.getId());
                        EDataPrivacyLevel inEdge = null;
                        EDataPrivacyLevel outEdge = null;
                        // Ckeck Parameter
                        for (final Parameter p : os.getParameters__OperationSignature()) {
                            final ParameterModifier mod = p.getModifier__Parameter();
                            if ((mod == ParameterModifier.IN) || (mod == ParameterModifier.INOUT)) {
                                if (outEdge != null) {
                                    if (this.isMoreCritical(outEdge,
                                            this.parameterprivacy.get(p.getParameterName()).getLevel())) {
                                        outEdge = this.parameterprivacy.get(p.getParameterName()).getLevel();
                                    }
                                } else {
                                    outEdge = this.parameterprivacy.get(p.getParameterName()).getLevel();
                                }
                            }
                            if ((mod == ParameterModifier.OUT) || (mod == ParameterModifier.INOUT)) {
                                if (inEdge != null) {
                                    if (this.isMoreCritical(inEdge,
                                            this.parameterprivacy.get(p.getParameterName()).getLevel())) {
                                        inEdge = this.parameterprivacy.get(p.getParameterName()).getLevel();
                                    }
                                } else {
                                    inEdge = this.parameterprivacy.get(p.getParameterName()).getLevel();
                                }
                            }
                        }
                        // Check Returntype
                        if (ipa_os != null) {
                            if (inEdge != null) {
                                if (this.isMoreCritical(inEdge, ipa_os.getLevel())) {
                                    inEdge = ipa_os.getLevel();
                                }
                            } else {
                                inEdge = ipa_os.getLevel();
                            }
                        }
                        // Add Edges
                        if (inEdge != null) {
                            final Edge e = new Edge(this.vertices.get(rcProvider.getId()),
                                    this.vertices.get(rcRequiring.getId()));
                            e.setDPC(Policy.getDataClassification(inEdge));
                            e.setInterfaceName(os.getEntityName());
                            g.addEdge(e);
                        }
                        if (outEdge != null) {
                            final Edge e = new Edge(this.vertices.get(rcRequiring.getId()),
                                    this.vertices.get(rcProvider.getId()));
                            e.setDPC(Policy.getDataClassification(outEdge));
                            e.setInterfaceName(os.getEntityName());
                            g.addEdge(e);
                        }

                    }

                }
            }

        }
    }

    /** Fills the hasmaps used for queries **/
    private void clearAndFillQueryMaps() {
        this.vertices.clear();
        this.geolocations.clear();
        this.stereotypes.clear();
        this.parameterprivacy.clear();
        this.returntypeprivacy.clear();
        this.interfaces.clear();
        for (final GeoLocation geo : this.privacyRootElement.getResourceContainerLocations()) {
            this.geolocations.put(geo.getResourceContainer().getId(), geo);
        }
        for (final EncapsulatedDataSource stereotype : this.privacyRootElement.getEncapsulatedDataSources()) {
            this.stereotypes.put(stereotype.getComponent().getId(), stereotype);
        }
        for (final IPrivacyAnnotation ipa : this.privacyRootElement.getPrivacyLevels()) {
            if (ipa instanceof ParameterPrivacy) {
                final ParameterPrivacy pp = (ParameterPrivacy) ipa;
                this.parameterprivacy.put(pp.getParameter().getParameterName(), pp);
            }
            if (ipa instanceof ReturnTypePrivacy) {
                final ReturnTypePrivacy rtp = (ReturnTypePrivacy) ipa;
                this.returntypeprivacy.put(rtp.getOperationSignature().getId(), rtp);
            }
        }

        for (final Interface inf : this.repositoryRootElement.getInterfaces__Repository()) {
            if (inf instanceof OperationInterface) {
                this.interfaces.put(inf.getEntityName(), (OperationInterface) inf);
            }
        }
    }

    private boolean isMoreCritical(final EDataPrivacyLevel basis, final EDataPrivacyLevel compared) {
        if (compared.ordinal() > basis.ordinal()) {
            return true;
        }

        return false;
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
