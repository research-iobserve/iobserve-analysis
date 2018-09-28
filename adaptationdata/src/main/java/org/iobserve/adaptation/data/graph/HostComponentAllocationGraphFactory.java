/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.adaptation.data.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.iobserve.model.ModelImporter;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyConnectorPrivacy;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;
import org.palladiosimulator.pcm.system.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory class to create a model graph which is a simple, more direct representation of the
 * host-component-allocation structure from the PCM model.
 *
 * @author Philipp Weimann
 * @author Lars Bluemke<br>
 *         - added revision for drools rule matching<br>
 *         - added buildGraph with model instances for testing<br>
 *         - enabled use of models other than pcm privacy models<br>
 *         - fixed the wrong mapping of an assembly context to a resource container (an allocation
 *         contexts must be mapped to a resource container)
 */
public class HostComponentAllocationGraphFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(HostComponentAllocationGraphFactory.class);

    private ModelImporter modelProvider;

    private Map<String, AllocationContext> allocationContexts;
    private Map<String, AssemblyContext> assemblyContexts;
    private Map<String, DataPrivacyLvl> assemblyContextPrivacyLvl;
    private Map<String, ResourceContainer> resourceContainers;
    private Map<String, AssemblyConnector> assemblyConnectors;
    private Map<String, Set<AllocationContext>> assCtxt2AllocCtxts;

    /**
     * Empty Constructor.
     */
    public HostComponentAllocationGraphFactory() {
        // state holding graph factory
    }

    /**
     * Build a graph based on the given model providers.
     *
     * @param modelHandler
     *            object containing all model providers
     * @param revision
     *            the graph's revision
     * @return returns the model graph for this
     * @throws Exception
     *             on error
     */
    public HostComponentAllocationGraph buildGraph(final ModelImporter modelHandler, final ModelGraphRevision revision)
            throws Exception {
        this.modelProvider = modelHandler;
        this.init();

        this.extractAssemblyContexts(this.modelProvider.getSystemModel());
        this.extractAssemblyConnectors(this.modelProvider.getSystemModel());
        this.extractResourceContainers(this.modelProvider.getResourceEnvironmentModel());
        this.adaptPrivacyLvl();
        this.extractAllocations(this.modelProvider.getAllocationModel());

        return this.createModelGraph(revision);
    }

    /**
     * Build a graph based on the given models. Note: As this method does not take a model provider
     * as an argument the created graph's getPcmModels() method will return null;
     *
     * @param systemModel
     *            a system model instance
     * @param resourceEnvironmentModel
     *            a resource environment model instance
     * @param allocationModel
     *            an allocation model instance
     * @param revision
     *            the graph's revision
     * @return returns the model graph for this
     * @throws Exception
     *             on error
     */
    public HostComponentAllocationGraph buildGraph(final System systemModel,
            final ResourceEnvironment resourceEnvironmentModel, final Allocation allocationModel,
            final ModelGraphRevision revision) throws Exception {
        this.init();

        this.extractAssemblyContexts(systemModel);
        this.extractAssemblyConnectors(systemModel);
        this.extractResourceContainers(resourceEnvironmentModel);
        this.adaptPrivacyLvl();
        this.extractAllocations(allocationModel);

        return this.createModelGraph(revision);
    }

    /*
     * Prepare all data structures.
     */
    private void init() {
        this.allocationContexts = new HashMap<>();
        this.assemblyContexts = new HashMap<>();
        this.assemblyContextPrivacyLvl = new HashMap<>();
        this.assemblyConnectors = new HashMap<>();
        this.resourceContainers = new HashMap<>();
        this.assCtxt2AllocCtxts = new HashMap<>();
    }

    /*
     * Extract Information Helpers
     */
    private void extractAssemblyContexts(final System sysModel) {
        final EList<AssemblyContext> newAssemblyContexts = sysModel.getAssemblyContexts__ComposedStructure();
        final Set<String> acs = new HashSet<>();

        for (final AssemblyContext assemblyContext : newAssemblyContexts) {
            this.assemblyContexts.put(assemblyContext.getId(), assemblyContext);
            acs.add(assemblyContext.getId());
        }

        if (HostComponentAllocationGraphFactory.LOGGER.isDebugEnabled()) {
            HostComponentAllocationGraphFactory.LOGGER
                    .debug("Individual Assembly Contexts found in System Model: " + acs.size());
        }
    }

    private void extractAssemblyConnectors(final System sysModel) {
        final EList<Connector> newConnectors = sysModel.getConnectors__ComposedStructure();

        for (final Connector connector : newConnectors) {
            if (connector instanceof AssemblyConnectorPrivacy) {
                final AssemblyConnectorPrivacy acp = (AssemblyConnectorPrivacy) connector;
                this.assemblyConnectors.put(connector.getId(), acp);
            } else if (connector instanceof AssemblyConnector) {
                final AssemblyConnector acp = (AssemblyConnector) connector;
                this.assemblyConnectors.put(connector.getId(), acp);
                if (HostComponentAllocationGraphFactory.LOGGER.isWarnEnabled()) {
                    HostComponentAllocationGraphFactory.LOGGER.warn(
                            "An AssemblyContext (ID: " + acp.getId() + ") was found which has no privacy extention\n");
                }
            }
        }
    }

    private void adaptPrivacyLvl() {
        final Collection<AssemblyConnector> acps = this.assemblyConnectors.values();

        final Set<String> acs = new HashSet<>();

        for (final AssemblyConnector acp : acps) {
            if (acp instanceof AssemblyConnectorPrivacy) {
                final DataPrivacyLvl assemblyConnectorPrivacyLvl = ((AssemblyConnectorPrivacy) acp).getPrivacyLevel();

                final String providedACID = acp.getProvidingAssemblyContext_AssemblyConnector().getId();
                final String requiredACID = acp.getRequiringAssemblyContext_AssemblyConnector().getId();

                this.updatePrivacyLvl((AssemblyConnectorPrivacy) acp, assemblyConnectorPrivacyLvl, providedACID);
                this.updatePrivacyLvl((AssemblyConnectorPrivacy) acp, assemblyConnectorPrivacyLvl, requiredACID);

                acs.add(requiredACID);
                acs.add(providedACID);
            }
        }

        if (HostComponentAllocationGraphFactory.LOGGER.isDebugEnabled()) {
            HostComponentAllocationGraphFactory.LOGGER
                    .debug("Individual Assembly Contexts found in Assembly Connectors: " + acs.size());
        }
    }

    private void updatePrivacyLvl(final AssemblyConnectorPrivacy acp, final DataPrivacyLvl assemblyConnectorPrivacyLvl,
            final String assemblyContextID) {
        // Check whether the AssemblyContext was found while extracting
        final AssemblyContext assemblyContext = this.assemblyContexts.get(assemblyContextID);
        if (assemblyContext == null) {
            if (HostComponentAllocationGraphFactory.LOGGER.isErrorEnabled()) {
                HostComponentAllocationGraphFactory.LOGGER.error("The provided AssemblyContext (ID: "
                        + assemblyContextID + ") form the AssemblyConnectorPrivacy (ID:" + acp.getId() + ") "
                        + "was not found during the AssemblyContextExtraction");
            }
            this.assemblyContexts.put(assemblyContextID, acp.getProvidingAssemblyContext_AssemblyConnector());
        }

        // Do the actual job and update the privacy lvl
        DataPrivacyLvl currentDataLevelPrivacy = this.assemblyContextPrivacyLvl.get(assemblyContextID);

        if (currentDataLevelPrivacy != null) {
            currentDataLevelPrivacy = DataPrivacyLvl
                    .get(Math.min(assemblyConnectorPrivacyLvl.getValue(), currentDataLevelPrivacy.getValue()));
        } else {
            currentDataLevelPrivacy = assemblyConnectorPrivacyLvl;
        }

        this.assemblyContextPrivacyLvl.put(assemblyContextID, currentDataLevelPrivacy);
    }

    private void extractResourceContainers(final ResourceEnvironment resEnvModel) {
        final EList<ResourceContainer> newResourceContainers = resEnvModel.getResourceContainer_ResourceEnvironment();
        for (final ResourceContainer resourceContainer : newResourceContainers) {
            if (!(resourceContainer instanceof ResourceContainerPrivacy)
                    && HostComponentAllocationGraphFactory.LOGGER.isWarnEnabled()) {
                HostComponentAllocationGraphFactory.LOGGER.warn("A ResourceContainer (ID: " + resourceContainer.getId()
                        + ") was found which has no privacy extention\n");
            }
            this.resourceContainers.put(resourceContainer.getId(), resourceContainer);
        }
    }

    private void extractAllocations(final Allocation allocationModel) {
        final EList<AllocationContext> allocContexts = allocationModel.getAllocationContexts_Allocation();

        for (final AllocationContext allocationContext : allocContexts) {
            final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();
            final Set<String> acs = new HashSet<>();

            this.allocationContexts.put(allocationContext.getId(), allocationContext);
            acs.add(allocationContext.getId());

            if (HostComponentAllocationGraphFactory.LOGGER.isDebugEnabled()) {
                HostComponentAllocationGraphFactory.LOGGER
                        .debug("Individual Allocation Contexts found in Allocation Model: " + acs.size());
            }

            final String assemblyContextID = assemblyContext.getId();
            if (!this.assemblyContexts.containsKey(assemblyContextID)) {
                if (HostComponentAllocationGraphFactory.LOGGER.isErrorEnabled()) {
                    HostComponentAllocationGraphFactory.LOGGER.error("An unknown AssemblyContext (ID: "
                            + assemblyContext.getId() + ") was found during allocation context analysis.\n");
                }
            } else {
                final Set<AllocationContext> allocCtxts;
                if (this.assCtxt2AllocCtxts.containsKey(assemblyContextID)) {
                    allocCtxts = this.assCtxt2AllocCtxts.get(assemblyContextID);
                } else {
                    allocCtxts = new HashSet<>();
                }
                allocCtxts.add(allocationContext);
                this.assCtxt2AllocCtxts.put(assemblyContextID, allocCtxts);
            }
        }
    }

    /*
     * Build Graph Helpers
     */
    private HostComponentAllocationGraph createModelGraph(final ModelGraphRevision revision) {
        // Build Servers Nodes
        final Map<String, DeploymentNode> servers = this.createServerNodes(revision);

        // Build Component Nodes
        final Map<String, ComponentNode> components = this.createComponentNodes(servers, revision);

        // Set Edges
        this.createEdges(components, revision);

        return new HostComponentAllocationGraph(servers.values(), components.values(), this.modelProvider, revision);
    }

    private void createEdges(final Map<String, ComponentNode> components, final ModelGraphRevision revision) {
        for (final AssemblyConnector acp : this.assemblyConnectors.values()) {
            final String provACID = acp.getProvidingAssemblyContext_AssemblyConnector().getId();
            final String reqACID = acp.getRequiringAssemblyContext_AssemblyConnector().getId();

            ComponentNode provNode;
            ComponentNode reqNode;
            for (final AllocationContext provAlloc : this.assCtxt2AllocCtxts.get(provACID)) {
                provNode = components.get(provAlloc.getId());
                for (final AllocationContext reqAlloc : this.assCtxt2AllocCtxts.get(reqACID)) {
                    reqNode = components.get(reqAlloc.getId());

                    final ComponentEdge edge;
                    if (acp instanceof AssemblyConnectorPrivacy) {
                        edge = new ComponentEdge(acp.getId(), acp.getEntityName(), provNode, reqNode,
                                ((AssemblyConnectorPrivacy) acp).getPrivacyLevel(), revision);
                    } else {
                        edge = new ComponentEdge(acp.getId(), acp.getEntityName(), provNode, reqNode, null, revision);
                    }

                    provNode.addCommunicationEdge(edge);
                    reqNode.addCommunicationEdge(edge);
                }
            }

        }
    }

    private Map<String, DeploymentNode> createServerNodes(final ModelGraphRevision revision) {
        final Map<String, DeploymentNode> servers = new HashMap<>();

        for (final ResourceContainer resContainer : this.resourceContainers.values()) {
            final DeploymentNode server;
            if (resContainer instanceof ResourceContainerPrivacy) {
                server = new DeploymentNode(resContainer.getId(), resContainer.getEntityName(),
                        ((ResourceContainerPrivacy) resContainer).getGeolocation(), revision);
            } else {
                server = new DeploymentNode(resContainer.getId(), resContainer.getEntityName(), 0, revision);
            }

            servers.put(resContainer.getId(), server);
        }

        return servers;
    }

    private Map<String, ComponentNode> createComponentNodes(final Map<String, DeploymentNode> servers,
            final ModelGraphRevision revision) {
        final Map<String, ComponentNode> components = new HashMap<>();

        for (final AllocationContext allocationContext : this.allocationContexts.values()) {

            final DeploymentNode hostServer = servers
                    .get(allocationContext.getResourceContainer_AllocationContext().getId());
            final String assemblyContextID = allocationContext.getAssemblyContext_AllocationContext().getId();
            final AssemblyContext assemblyContext = this.assemblyContexts.get(assemblyContextID);
            final DataPrivacyLvl acPrivacyLvl = this.assemblyContextPrivacyLvl.get(assemblyContextID);

            final ComponentNode component = new ComponentNode(assemblyContextID, assemblyContext.getEntityName(),
                    acPrivacyLvl, hostServer, assemblyContext.getEncapsulatedComponent__AssemblyContext().getId(),
                    allocationContext.getId(), revision);
            hostServer.addComponent(component);

            components.put(allocationContext.getId(), component);
        }
        return components;
    }
}
