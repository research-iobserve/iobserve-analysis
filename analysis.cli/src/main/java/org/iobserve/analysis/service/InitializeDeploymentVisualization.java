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
package org.iobserve.analysis.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.service.services.CommunicationInstanceService;
import org.iobserve.analysis.service.services.CommunicationService;
import org.iobserve.analysis.service.services.NodeService;
import org.iobserve.analysis.service.services.NodegroupService;
import org.iobserve.analysis.service.services.ServiceInstanceService;
import org.iobserve.analysis.service.services.ServiceService;
import org.iobserve.analysis.service.services.SystemService;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.impl.LinkingResourceImpl;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import util.Changelog;
import util.SendHttpRequest;

/**
 * Initializes the deployment visualization by mapping the initial palladio components to
 * visualization elements.
 *
 * @author Josefine Wegert
 *
 */
public final class InitializeDeploymentVisualization {

    /** model provider for palladio models. */
    private final ModelProvider<Allocation> allocationModelGraphProvider;
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    /** services for visualization elements. */
    private final SystemService systemService = new SystemService();
    private final NodegroupService nodegroupService = new NodegroupService();
    private final NodeService nodeService = new NodeService();
    private final ServiceService serviceService = new ServiceService();
    private final ServiceInstanceService serviceinstanceService = new ServiceInstanceService();
    private final CommunicationService communicationService = new CommunicationService();
    private final CommunicationInstanceService communicationinstanceService = new CommunicationInstanceService();

    private final URL changelogUrl;
    private final URL systemUrl;

    private static final Log LOG = LogFactory.getLog(AnalysisMain.class);

    /**
     * constructor.
     *
     * @param visualizationBaseUrl
     *            Url used for the visualization
     * @param systemId
     *            system id
     * @param allocationModelGraphProvider
     *            provider for allocation model
     * @param systemModelGraphProvider
     *            provider for system model
     * @param resourceEnvironmentModelGraphProvider
     *            provider for resource environment model
     * @param usageModelGraphProvider
     *            provider for usage model
     * @throws MalformedURLException
     */

    public InitializeDeploymentVisualization(final URL visualizationBaseUrl, final String systemId,
            final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final ModelProvider<UsageModel> usageModelGraphProvider) throws MalformedURLException {
        this.systemUrl = new URL(visualizationBaseUrl + "/v1/systems/");
        this.changelogUrl = new URL(this.systemUrl + systemId + "/changelogs");
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    /**
     * Populates the database of the deployment visualization initially and respects the changelog
     * constraints of iobserve-ui-deployment. It takes information from the system model, the
     * allocation model and the resource environment model and creates corresponding visualization
     * components, e.g. nodes and services.
     *
     * @throws IOException
     *             when post request fails
     */
    public void initialize() throws IOException {
        // set up the system model and take parts from it
        final org.palladiosimulator.pcm.system.System systemModel = this.systemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);
        final List<AssemblyContext> assemblyContexts = systemModel.getAssemblyContexts__ComposedStructure();

        // set up the allocation model and take parts from it
        final List<String> allocationIds = this.allocationModelGraphProvider.readComponentByType(Allocation.class);
        // an allocation model contains exactly one allocation, therefore .get(0)
        final String allocationId = allocationIds.get(0);
        final Allocation allocation = this.allocationModelGraphProvider.readOnlyComponentById(Allocation.class,
                allocationId);
        final List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();

        // set up the resource environment model and take parts from it
        final ResourceEnvironment resourceEnvironmentModel = this.resourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class);
        final List<LinkingResource> linkingResources = resourceEnvironmentModel
                .getLinkingResources__ResourceEnvironment();
        final List<ResourceContainer> resourceContainers = resourceEnvironmentModel
                .getResourceContainer_ResourceEnvironment();

        // sending created components to visualization (in predefined order stated in changelog
        // constraints)
        /** system */
        SendHttpRequest.post(this.systemService.createSystem(systemModel), this.systemUrl, this.changelogUrl);

        /** node group and node */
        for (int i = 0; i < resourceContainers.size(); i++) {
            final ResourceContainer resourceContainer = resourceContainers.get(i);
            SendHttpRequest.post(
                    Changelog.create(this.nodegroupService.createNodegroup(this.systemService.getSystemId())),
                    this.systemUrl, this.changelogUrl);
            SendHttpRequest.post(Changelog.create(this.nodeService.createNode(resourceContainer,
                    this.systemService.getSystemId(), this.nodegroupService.getNodegroupId())), this.systemUrl,
                    this.changelogUrl);
        }

        /** service and service instance */
        for (int i = 0; i < assemblyContexts.size(); i++) {
            final AssemblyContext assemblyContext = assemblyContexts.get(i);
            SendHttpRequest.post(
                    Changelog.create(
                            this.serviceService.createService(assemblyContext, this.systemService.getSystemId())),
                    this.systemUrl, this.changelogUrl);
        }

        for (int i = 0; i < allocationContexts.size(); i++) {
            final AllocationContext allocationContext = allocationContexts.get(i);

            final String resourceContainerId = allocationContext.getResourceContainer_AllocationContext().getId();
            final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();
            final String assemblyContextId = allocationContext.getAssemblyContext_AllocationContext().getId();

            SendHttpRequest.post(
                    Changelog.create(this.serviceinstanceService.createServiceInstance(assemblyContext,
                            this.systemService.getSystemId(), resourceContainerId, assemblyContextId)),
                    this.systemUrl, this.changelogUrl);
        }

        /** communication and communication instance */

        final List<Connector> connectors = systemModel.getConnectors__ComposedStructure();

        for (int i = 0; i < connectors.size(); i++) {
            final Connector connector = connectors.get(i);
            // we are only interested in AssemblyConnectors
            if (connector instanceof AssemblyConnector) {

                final String technology = this.getTechnology((AssemblyConnector) connector, linkingResources);

                SendHttpRequest.post(
                        Changelog.create(this.communicationService.createCommunication((AssemblyConnector) connector,
                                this.systemService.getSystemId(), technology)),
                        this.systemUrl, this.changelogUrl);

                SendHttpRequest.post(Changelog.create(
                        this.communicationinstanceService.createCommunicationInstance((AssemblyConnector) connector,
                                this.systemService.getSystemId(), this.communicationService.getCommunicationId())),
                        this.systemUrl, this.changelogUrl);

            } else {
                InitializeDeploymentVisualization.LOG.debug("no AssemblyConnector: connector.getEntityName()");
            }

        }

        /** usergroup */
        // not working yet

        // map elements in entryLevelSystemCalls to assemblyContexts

        // final List<AssemblyContext> userInvokedServices = new ArrayList<>();
        // List<EntryLevelSystemCall> entryLevelSystemCalls = new ArrayList<>();
        //
        // entryLevelSystemCalls = this.collectEntryLevelSystemCalls(usageScenarios);
        //
        // for (int m = 0; m < entryLevelSystemCalls.size(); m++) {
        // final EntryLevelSystemCall userStep = entryLevelSystemCalls.get(m);
        //
        // final String providedRoleId = userStep.getProvidedRole_EntryLevelSystemCall().getId();
        //
        // final List<EObject> usergroupConnectors = this.systemModelGraphProvider
        // .readOnlyReferencingComponentsById(OperationProvidedRole.class, providedRoleId);
        // final ProvidedDelegationConnectorImpl usergroupConnector =
        // (ProvidedDelegationConnectorImpl) usergroupConnectors
        // .get(0);
        //
        // final AssemblyContext assemblyContext =
        // usergroupConnector.getAssemblyContext_ProvidedDelegationConnector();
        //
        // userInvokedServices.add(assemblyContext);
        // }

        // if (userInvokedServices.size() > 0) {
        // SendHttpRequest.post(Changelog.create(
        // this.usergroupService.createUsergroup(this.systemService.getSystemId(),
        // userInvokedServices)),
        // this.systemUrl, this.changelogUrl);
        //
        // }

    }

    /**
     * Help function for getting the technology used for communication. Therefore get the
     * {@link ResourceContainer}s on which the two communicating {@link AssemblyContext}s are
     * deployed. This information is stored in the {@link Allocation} model. Then find the
     * {@link LinkingResource} in the {@link ResourceEnvironment} model, that connect these
     * {@link ResourceContainer}s.
     *
     * @param connector
     *            assembly connector
     * @param linkingResources
     *            list of linking resources
     * @return technology used for communication, entity name of linking resource
     */

    private String getTechnology(final AssemblyConnector connector, final List<LinkingResource> linkingResources) {
        /**
         * ID of resource container on which source (regarding communication) assembly context is
         * deployed
         */
        String resourceSourceId = null;
        /**
         * ID of resource container on which target (regarding communication) assembly context is
         * deployed
         */
        String resourceTargetId = null;
        /** technology of communication */
        String technology = null;

        final String assemContSourceId = connector.getProvidingAssemblyContext_AssemblyConnector().getId();
        final String assemContTargetId = connector.getRequiringAssemblyContext_AssemblyConnector().getId();

        final List<EObject> allocationContextsWithSource = this.allocationModelGraphProvider
                .readOnlyReferencingComponentsById(AssemblyContext.class, assemContSourceId);
        if (allocationContextsWithSource.get(0) instanceof AllocationContext) {
            final AllocationContext allocationContext = (AllocationContext) allocationContextsWithSource.get(0);
            resourceSourceId = allocationContext.getResourceContainer_AllocationContext().getId();
        }

        final List<EObject> allocationContextsWithTarget = this.allocationModelGraphProvider
                .readOnlyReferencingComponentsById(AssemblyContext.class, assemContTargetId);
        if (allocationContextsWithTarget.get(0) instanceof AllocationContext) {
            final AllocationContext allocationContext = (AllocationContext) allocationContextsWithTarget.get(0);
            resourceTargetId = allocationContext.getResourceContainer_AllocationContext().getId();
        }

        if ((resourceSourceId != null) && (resourceTargetId != null)) {
            for (int l = 0; l < linkingResources.size(); l++) {
                final LinkingResource linkingResource = linkingResources.get(l);
                if (linkingResource instanceof LinkingResourceImpl) {
                    final List<ResourceContainer> connectedResourceConts = linkingResource
                            .getConnectedResourceContainers_LinkingResource();
                    final List<String> connectedResourceContsIds = new ArrayList<>();
                    for (int k = 0; k < connectedResourceConts.size(); k++) {
                        connectedResourceContsIds.add(connectedResourceConts.get(k).getId());
                    }
                    if (connectedResourceContsIds.contains(resourceSourceId)) {
                        if (connectedResourceContsIds.contains(resourceTargetId)) {
                            technology = linkingResource.getEntityName();
                        }
                    }
                }
            }
        }
        return technology;
    }

}
