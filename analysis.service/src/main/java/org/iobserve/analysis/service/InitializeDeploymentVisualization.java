package org.iobserve.analysis.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.service.services.CommunicationInstanceService;
import org.iobserve.analysis.service.services.CommunicationService;
import org.iobserve.analysis.service.services.NodeService;
import org.iobserve.analysis.service.services.NodegroupService;
import org.iobserve.analysis.service.services.ServiceService;
import org.iobserve.analysis.service.services.ServiceinstanceService;
import org.iobserve.analysis.service.services.SystemService;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.Connector;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.impl.LinkingResourceImpl;

import util.SendHttpRequest;

/**
 * Initializes the deployment visualization by mapping the initial palladio components to
 * visualization elements.
 *
 * @author jweg
 *
 */
public final class InitializeDeploymentVisualization {

    /** model provider for palladio models */
    private final ModelProvider<Allocation> allocationModelGraphProvider;
    private final ModelProvider<ResourceContainer> allocationResourceContainerModelGraphProvider;
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    /** services for visualization elements */
    private final SystemService systemService = new SystemService();
    private final NodegroupService nodegroupService = new NodegroupService();
    private final NodeService nodeService = new NodeService();
    private final ServiceService serviceService = new ServiceService();
    private final ServiceinstanceService serviceinstanceService = new ServiceinstanceService();
    private final CommunicationService communicationService = new CommunicationService();
    private final CommunicationInstanceService communicationinstanceService = new CommunicationInstanceService();

    private final URL changelogUrl;
    private final URL systemUrl;

    /**
     * constructor
     *
     * @param systemUrl
     *            Url to send requests for creating a system to
     * @param changelogUrl
     *            Url to send requests for changelogs
     * @param allocationModelGraphProvider
     * @param allocationResourceContainerModelGraphProvider
     * @param systemModelGraphProvider
     * @param resourceEnvironmentModelGraphProvider
     */

    public InitializeDeploymentVisualization(final URL systemUrl, final URL changelogUrl,
            final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<ResourceContainer> allocationResourceContainerModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        this.systemUrl = systemUrl;
        this.changelogUrl = changelogUrl;
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.allocationResourceContainerModelGraphProvider = allocationResourceContainerModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    /**
     * Populates the database of the deployment visualization initially and respects the changelog
     * constraints of iobserve-ui-deployment. It takes information from the system model, the
     * allocation model and the resource environment model and creates corresponding visualization
     * components, e.g. nodes and services.
     *
     * @throws Exception
     */
    protected void initialize() throws Exception {
        // set up the models
        final org.palladiosimulator.pcm.system.System systemModel = this.systemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);

        final List<AssemblyContext> assemblyContexts = systemModel.getAssemblyContexts__ComposedStructure();
        final List<Connector> connectors = systemModel.getConnectors__ComposedStructure();

        final List<String> allocationIds = this.allocationModelGraphProvider.readComponentByType(Allocation.class);
        // an allocation model contains exactly one allocation, therefore .get(0)
        final String allocationId = allocationIds.get(0);
        final Allocation allocation = this.allocationModelGraphProvider.readOnlyComponentById(Allocation.class,
                allocationId);
        final List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();
        final ResourceEnvironment resourceEnvironmentModel = this.resourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class);
        final List<LinkingResource> linkingResources = resourceEnvironmentModel
                .getLinkingResources__ResourceEnvironment();
        final List<ResourceContainer> resourceContainers = resourceEnvironmentModel
                .getResourceContainer_ResourceEnvironment();

        // sending created components to visualization (in predefined order stated in changelog
        // constraints)
        SendHttpRequest.post(this.systemService.createSystem(systemModel), this.systemUrl, this.changelogUrl);

        for (int i = 0; i < resourceContainers.size(); i++) {
            final ResourceContainer resourceContainer = resourceContainers.get(i);
            SendHttpRequest.post(this.nodegroupService.createNodegroup(this.systemService.getSystemId()),
                    this.systemUrl, this.changelogUrl);
            SendHttpRequest.post(this.nodeService.createNode(resourceContainer, this.systemService.getSystemId(),
                    this.nodegroupService.getNodegroupId()), this.systemUrl, this.changelogUrl);
        }
        // architecture view in deployment visualization shows services and
        // communication
        for (int i = 0; i < assemblyContexts.size(); i++) {
            final AssemblyContext assemblyContext = assemblyContexts.get(i);
            SendHttpRequest.post(this.serviceService.createService(assemblyContext, this.systemService.getSystemId()),
                    this.systemUrl, this.changelogUrl);
        }

        // deployment view in deployment visualization shows nodegroups, nodes, serviceinstances and
        // communicationinstances
        for (int i = 0; i < allocationContexts.size(); i++) {
            final AllocationContext allocationContext = allocationContexts.get(i);

            final String resourceContainerId = allocationContext.getResourceContainer_AllocationContext().getId();
            final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();
            final String assemblyContextId = allocationContext.getAssemblyContext_AllocationContext().getId();

            SendHttpRequest.post(this.serviceinstanceService.createServiceInstance(assemblyContext,
                    this.systemService.getSystemId(), resourceContainerId, assemblyContextId), this.systemUrl,
                    this.changelogUrl);
        }

        String resourceSourceId = null;
        String resourceTargetId = null;
        String technology = null;
        for (int i = 0; i < connectors.size(); i++) {
            final Connector connector = connectors.get(i);
            // we are only interested in AssemblyConnectors
            if (connector instanceof AssemblyConnector) {

                // aim: find out which technology is used for communication
                // howTo: (AllocationModel) get the resourceContainer(s) on which the
                // source/targetAssemblyContext are deployed, (resourceEnvModel) get the
                // linkingResource which includes the resourceContainers in
                // connectedResourceContainers, entityName=technology
                final String assemContSourceId = ((AssemblyConnector) connector)
                        .getProvidingAssemblyContext_AssemblyConnector().getId();
                final String assemContTargetId = ((AssemblyConnector) connector)
                        .getRequiringAssemblyContext_AssemblyConnector().getId();

                final List<EObject> allocationContextsWithSource = this.allocationModelGraphProvider
                        .readOnlyReferencingComponentsById(AssemblyContext.class, assemContSourceId);
                if (allocationContextsWithSource.get(0) instanceof AllocationContext) {
                    final AllocationContext allocationContext = (AllocationContext) allocationContextsWithSource.get(0);
                    resourceSourceId = allocationContext.getResourceContainer_AllocationContext().getId();
                }

                final List<EObject> allocationContextsWithTarget = this.allocationModelGraphProvider
                        .readOnlyReferencingComponentsById(AssemblyContext.class, assemContTargetId);
                if (allocationContextsWithSource.get(0) instanceof AllocationContext) {
                    final AllocationContext allocationContext = (AllocationContext) allocationContextsWithSource.get(0);
                    resourceTargetId = allocationContext.getResourceContainer_AllocationContext().getId();
                }

                for (int j = 0; j < allocationContexts.size(); j++) {
                    final AllocationContext allocationContext = allocationContexts.get(j);
                    final String actualAssemblyContextId = allocationContext.getAssemblyContext_AllocationContext()
                            .getId();

                    if (actualAssemblyContextId.equals(assemContTargetId)) {
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

                }
                SendHttpRequest.post(this.communicationService.createCommunication((AssemblyConnector) connector,
                        this.systemService.getSystemId(), technology), this.systemUrl, this.changelogUrl);

                SendHttpRequest.post(
                        this.communicationinstanceService.createCommunicationinstance((AssemblyConnector) connector,
                                this.systemService.getSystemId(), this.communicationService.getCommunicationId()),
                        this.systemUrl, this.changelogUrl);

            } else {
                System.out.printf("no AssemblyConnector: %s\n", connector.getEntityName());
            }

        }

    }

}
