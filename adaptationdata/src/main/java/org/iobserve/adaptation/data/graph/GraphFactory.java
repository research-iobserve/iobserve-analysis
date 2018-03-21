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
import org.iobserve.model.PCMModelHandler;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyConnectorPrivacy;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;
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
 * @author Lars Bluemke (added revision for drools rule matching, added buildGraph with model
 *         instances for testing)
 */
public class GraphFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphFactory.class);

    private PCMModelHandler modelProvider;

    private Map<String, AssemblyContext> assemblyContexts;
    private Map<String, DataPrivacyLvl> assemblyContextPrivacyLvl;
    private Map<String, ResourceContainerPrivacy> resourceContainers;
    private Map<String, String> ac2rcMap;
    private Map<String, AssemblyConnectorPrivacy> assemblyConnectors;
    private Map<String, String> assemblyID2allocID;

    /**
     * Empty Constructor.
     */
    public GraphFactory() {
        // state holding graph factory
    }

    /**
     * Build a graph based on the given model providers.
     *
     * @param modelHandler
     *            object containing all model providers
     * @return returns the model graph for this
     * @throws Exception
     *             on error
     */
    public ModelGraph buildGraph(final PCMModelHandler modelHandler, final ModelGraphRevision revision)
            throws Exception {
        this.init(modelHandler);

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
     * @return returns the model graph for this
     * @throws Exception
     *             on error
     */
    public ModelGraph buildGraph(final System systemModel, final ResourceEnvironment resourceEnvironmentModel,
            final Allocation allocationModel, final ModelGraphRevision revision) throws Exception {
        this.init(null);

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
    private void init(final PCMModelHandler modelProvider) { // NOCS
        this.modelProvider = modelProvider;

        this.assemblyContexts = new HashMap<>();
        this.assemblyContextPrivacyLvl = new HashMap<>();
        this.assemblyConnectors = new HashMap<>();
        this.resourceContainers = new HashMap<>();
        this.ac2rcMap = new HashMap<>();
        this.assemblyID2allocID = new HashMap<>();
    }

    /*
     * Extract Information Helpers
     */
    private void extractAssemblyContexts(final org.palladiosimulator.pcm.system.System sysModel) {
        final EList<AssemblyContext> newAssemblyContexts = sysModel.getAssemblyContexts__ComposedStructure();
        final Set<String> acs = new HashSet<>();

        for (final AssemblyContext assemblyContext : newAssemblyContexts) {
            this.assemblyContexts.put(assemblyContext.getId(), assemblyContext);
            acs.add(assemblyContext.getId());
        }

        if (GraphFactory.LOGGER.isInfoEnabled()) {
            GraphFactory.LOGGER.info("Individual Assembly Contexts found in System Model: " + acs.size());
        }
    }

    private void extractAssemblyConnectors(final org.palladiosimulator.pcm.system.System sysModel) {
        final EList<Connector> newConnectors = sysModel.getConnectors__ComposedStructure();

        for (final Connector connector : newConnectors) {
            if (connector instanceof AssemblyConnectorPrivacy) {
                final AssemblyConnectorPrivacy acp = (AssemblyConnectorPrivacy) connector;
                this.assemblyConnectors.put(connector.getId(), acp);
            }
        }
    }

    private void adaptPrivacyLvl() {
        final Collection<AssemblyConnectorPrivacy> acps = this.assemblyConnectors.values();

        final Set<String> acs = new HashSet<>();

        for (final AssemblyConnectorPrivacy acp : acps) {
            final DataPrivacyLvl assemblyConnectorPrivacyLvl = acp.getPrivacyLevel();

            final String providedACID = acp.getProvidingAssemblyContext_AssemblyConnector().getId();
            final String requiredACID = acp.getRequiringAssemblyContext_AssemblyConnector().getId();

            this.updatePrivacyLvl(acp, assemblyConnectorPrivacyLvl, providedACID);
            this.updatePrivacyLvl(acp, assemblyConnectorPrivacyLvl, requiredACID);

            acs.add(requiredACID);
            acs.add(providedACID);
        }

        if (GraphFactory.LOGGER.isInfoEnabled()) {
            GraphFactory.LOGGER.info("Individual Assembly Contexts found in Assembly Connectors: " + acs.size());
        }
    }

    private void updatePrivacyLvl(final AssemblyConnectorPrivacy acp, final DataPrivacyLvl assemblyConnectorPrivacyLvl,
            final String assemblyContextID) {
        // Check whether the AssemblyContext was found while extracting
        final AssemblyContext assemblyContext = this.assemblyContexts.get(assemblyContextID);
        if (assemblyContext == null) {
            if (GraphFactory.LOGGER.isErrorEnabled()) {
                GraphFactory.LOGGER.error("The provided AssemblyContext (ID: " + assemblyContextID
                        + ") form the AssemblyConnectorPrivacy (ID:" + acp.getId() + ") "
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
            if (resourceContainer instanceof ResourceContainerPrivacy) {
                this.resourceContainers.put(resourceContainer.getId(), (ResourceContainerPrivacy) resourceContainer);
            } else {
                if (GraphFactory.LOGGER.isErrorEnabled()) {
                    GraphFactory.LOGGER.error("A ResourceContainer (ID: " + resourceContainer.getId()
                            + ") was found which has no privacy extention\n");
                }
            }
        }
    }

    private void extractAllocations(final Allocation allocationModel) {
        final EList<AllocationContext> allocationContexts = allocationModel.getAllocationContexts_Allocation();

        for (final AllocationContext allocationContext : allocationContexts) {
            final ResourceContainer resContainer = allocationContext.getResourceContainer_AllocationContext();
            final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();

            this.assemblyID2allocID.put(assemblyContext.getId(), allocationContext.getId());

            boolean correctIDs = true;
            final String resContainerID = resContainer.getId();
            if (!this.resourceContainers.containsKey(resContainerID)) {
                if (GraphFactory.LOGGER.isErrorEnabled()) {
                    GraphFactory.LOGGER.error("A unknown ResourceContainer (ID: " + resContainer.getId()
                            + ") was found during allocation context analysis.\n");
                }
                correctIDs = false;
            }

            final String assemblyContextID = assemblyContext.getId();
            if (!this.assemblyContexts.containsKey(assemblyContextID)) {
                if (GraphFactory.LOGGER.isErrorEnabled()) {
                    GraphFactory.LOGGER.error("An unknown AssemblyContext (ID: " + assemblyContext.getId()
                            + ") was found during allocation context analysis.\n");
                }
                correctIDs = false;
            }

            if (correctIDs) {
                this.ac2rcMap.put(assemblyContext.getId(), resContainer.getId());
            }
        }
    }

    /*
     * Build Graph Helpers
     */
    private ModelGraph createModelGraph(final ModelGraphRevision revision) {
        final Map<String, DeploymentNode> servers = new HashMap<>();
        final Map<String, ComponentNode> components = new HashMap<>();

        // Build Servers Nodes
        for (final ResourceContainerPrivacy resContainer : this.resourceContainers.values()) {
            final DeploymentNode server = new DeploymentNode(resContainer.getId(), resContainer.getEntityName(),
                    resContainer.getGeolocation(), revision);
            servers.put(resContainer.getId(), server);
        }

        // Build Component Nodes
        for (final AssemblyContext ac : this.assemblyContexts.values()) {

            final DeploymentNode hostServer = servers.get(this.ac2rcMap.get(ac.getId()));
            final DataPrivacyLvl acPrivacyLvl = this.assemblyContextPrivacyLvl.get(ac.getId());

            final ComponentNode component = new ComponentNode(ac.getId(), ac.getEntityName(), acPrivacyLvl, hostServer,
                    ac.getEncapsulatedComponent__AssemblyContext().getId(), this.assemblyID2allocID.get(ac.getId()),
                    revision);
            hostServer.addComponent(component);

            components.put(ac.getId(), component);
        }

        // Set Edges
        for (final AssemblyConnectorPrivacy acp : this.assemblyConnectors.values()) {
            final String provACID = acp.getProvidingAssemblyContext_AssemblyConnector().getId();
            final String reqACID = acp.getRequiringAssemblyContext_AssemblyConnector().getId();

            final ComponentNode provNode = components.get(provACID);
            final ComponentNode reqNode = components.get(reqACID);

            final ComponentEdge edge = new ComponentEdge(acp.getId(), acp.getEntityName(), provNode, reqNode,
                    acp.getPrivacyLevel(), revision);

            provNode.addCommunicationEdge(edge);
            reqNode.addCommunicationEdge(edge);
        }

        return new ModelGraph(servers.values(), components.values(), this.modelProvider, revision);
    }
}
