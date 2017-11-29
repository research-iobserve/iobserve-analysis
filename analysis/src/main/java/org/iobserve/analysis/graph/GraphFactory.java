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
package org.iobserve.analysis.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.iobserve.analysis.InitializeModelProviders;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.compositionprivacy.AssemblyConnectorPrivacy;
import org.palladiosimulator.pcm.compositionprivacy.DataPrivacyLvl;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironmentprivacy.ResourceContainerPrivacy;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * TODO add description.
 *
 * @author unknown
 *
 */
public class GraphFactory {
    private static final Log LOG = LogFactory.getLog(GraphFactory.class);

    private InitializeModelProviders modelProvider;

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
    }

    public ModelGraph buildGraph(final InitializeModelProviders modelProvider) throws Exception {
        this.init(modelProvider);

        this.extractAssemblyContexts(this.modelProvider.getSystemModelProvider());
        this.extractAssemblyConnectors(this.modelProvider.getSystemModelProvider());
        this.extractResourceContainers(this.modelProvider.getResourceEnvironmentModelProvider());
        this.adaptPrivacyLvl();
        this.extractAllocations(this.modelProvider.getAllocationModelProvider());

        return this.createModelGraph();
    }

    /*
     * Prepare all data structures.
     */
    private void init(final InitializeModelProviders modelProvider) {
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
    private void extractAssemblyContexts(final SystemModelProvider sysModelProv) {
        final org.palladiosimulator.pcm.system.System sysModel = sysModelProv.getModel();
        final EList<AssemblyContext> assemblyContexts = sysModel.getAssemblyContexts__ComposedStructure();
        final Set<String> acs = new HashSet<>();

        for (final AssemblyContext assemblyContext : assemblyContexts) {
            this.assemblyContexts.put(assemblyContext.getId(), assemblyContext);
            acs.add(assemblyContext.getId());
        }

        GraphFactory.LOG.info("Individual Assembly Contexts found in System Model: " + acs.size());
    }

    private void extractAssemblyConnectors(final SystemModelProvider sysModelProv) {
        final org.palladiosimulator.pcm.system.System sysModel = sysModelProv.getModel();
        final EList<Connector> connectors = sysModel.getConnectors__ComposedStructure();

        for (final Connector connector : connectors) {
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

        GraphFactory.LOG.info("Individual Assembly Contexts found in Assembly Connectors: " + acs.size());
    }

    private void updatePrivacyLvl(final AssemblyConnectorPrivacy acp, final DataPrivacyLvl assemblyConnectorPrivacyLvl,
            final String assemblyContextID) {
        // Check whether the AssemblyContext was found while extracting
        final AssemblyContext assemblyContext = this.assemblyContexts.get(assemblyContextID);
        if (assemblyContext == null) {
            GraphFactory.LOG.error("The provided AssemblyContext (ID: " + assemblyContextID
                    + ") form the AssemblyConnectorPrivacy (ID:" + acp.getId() + ") "
                    + "was not found during the AssemblyContextExtraction");

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

    private void extractResourceContainers(final ResourceEnvironmentModelProvider resEnvModelProv) {
        final ResourceEnvironment resEnvModel = resEnvModelProv.getModel();
        final EList<ResourceContainer> resourceContainers = resEnvModel.getResourceContainer_ResourceEnvironment();
        for (final ResourceContainer resourceContainer : resourceContainers) {
            if (resourceContainer instanceof ResourceContainerPrivacy) {
                this.resourceContainers.put(resourceContainer.getId(), (ResourceContainerPrivacy) resourceContainer);
            } else {
                GraphFactory.LOG.error("A ResourceContainer (ID: " + resourceContainer.getId()
                        + ") was found which has no privacy extention\n");
            }
        }
    }

    private void extractAllocations(final AllocationModelProvider allocationModelProv) {
        final Allocation allocation = allocationModelProv.getModel();
        final EList<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();

        for (final AllocationContext allocationContext : allocationContexts) {
            final ResourceContainer resContainer = allocationContext.getResourceContainer_AllocationContext();
            final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();

            this.assemblyID2allocID.put(assemblyContext.getId(), allocationContext.getId());

            boolean correctIDs = true;
            final String resContainerID = resContainer.getId();
            if (!this.resourceContainers.containsKey(resContainerID)) {
                GraphFactory.LOG.error("A unknown ResourceContainer (ID: " + resContainer.getId()
                        + ") was found during allocation context analysis.\n");
                correctIDs = false;
            }

            final String assemblyContextID = assemblyContext.getId();
            if (!this.assemblyContexts.containsKey(assemblyContextID)) {
                GraphFactory.LOG.error("An unknown AssemblyContext (ID: " + assemblyContext.getId()
                        + ") was found during allocation context analysis.\n");
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
    private ModelGraph createModelGraph() {
        final HashMap<String, DeploymentNode> servers = new HashMap<>();
        final HashMap<String, ComponentNode> components = new HashMap<>();

        // Build Servers Nodes
        for (final ResourceContainerPrivacy resContainer : this.resourceContainers.values()) {
            final DeploymentNode server = new DeploymentNode(resContainer.getId(), resContainer.getEntityName(),
                    resContainer.getGeolocation());
            servers.put(resContainer.getId(), server);
        }

        // Build Component Nodes
        for (final AssemblyContext ac : this.assemblyContexts.values()) {

            final DeploymentNode hostServer = servers.get(this.ac2rcMap.get(ac.getId()));
            final DataPrivacyLvl acPrivacyLvl = this.assemblyContextPrivacyLvl.get(ac.getId());

            final ComponentNode component = new ComponentNode(ac.getId(), ac.getEntityName(), acPrivacyLvl, hostServer,
                    ac.getEncapsulatedComponent__AssemblyContext().getId(), this.assemblyID2allocID.get(ac.getId()));
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
                    acp.getPrivacyLevel());

            provNode.addCommunicationEdge(edge);
            reqNode.addCommunicationEdge(edge);
        }

        return new ModelGraph(servers.values(), components.values(), this.modelProvider);
    }
}
