package org.iobserve.analysis.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonWriter;

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

    private static final String USER_AGENT = "iObserve/0.0.2";

    private final URL changelogUrl;
    private final URL systemUrl;
    private String systemId;

    /**
     * constructor
     *
     * @param systemUrl
     * @param changelogUrl
     * @param allocationModelGraphProvider
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

    protected void initialize() throws Exception {
        final org.palladiosimulator.pcm.system.System systemModel = this.systemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);

        final List<AssemblyContext> assemblyContexts = systemModel.getAssemblyContexts__ComposedStructure();
        final List<Connector> connectors = systemModel.getConnectors__ComposedStructure();

        final List<String> allocationIds = this.allocationModelGraphProvider.readComponentByType(Allocation.class);
        // an allocation model contains exactly one allocation
        final String allocationId = allocationIds.get(0);
        final Allocation allocation = this.allocationModelGraphProvider.readOnlyComponentById(Allocation.class,
                allocationId);
        final List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();
        final ResourceEnvironment resourceEnvironmentModel = this.resourceEnvironmentModelGraphProvider
                .readOnlyRootComponent(ResourceEnvironment.class);
        final List<LinkingResource> linkingResources = resourceEnvironmentModel
                .getLinkingResources__ResourceEnvironment();
        // nodes
        final List<ResourceContainer> resourceContainers = resourceEnvironmentModel
                .getResourceContainer_ResourceEnvironment();

        this.sendPostRequest(this.systemService.createSystem(systemModel));

        for (int i = 0; i < resourceContainers.size(); i++) {
            final ResourceContainer resourceContainer = resourceContainers.get(i);
            this.sendPostRequest(this.nodegroupService.createNodegroup(this.systemService.getSystemId()));
            this.sendPostRequest(this.nodeService.createNode(resourceContainer, this.systemService.getSystemId(),
                    this.nodegroupService.getNodegroupId()));
        }
        // architecture view in deployment visualization shows services and
        // communication
        for (int i = 0; i < assemblyContexts.size(); i++) {
            final AssemblyContext assemblyContext = assemblyContexts.get(i);
            this.sendPostRequest(this.serviceService.createService(assemblyContext, this.systemService.getSystemId()));
        }

        // deployment view in deployment visualization shows nodegroups, nodes, serviceinstances and
        // communicationinstances
        for (int i = 0; i < allocationContexts.size(); i++) {
            final AllocationContext allocationContext = allocationContexts.get(i);

            final String resourceContainerId = allocationContext.getResourceContainer_AllocationContext().getId();
            final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();
            final String assemblyContextId = allocationContext.getAssemblyContext_AllocationContext().getId();

            this.sendPostRequest(this.serviceinstanceService.createServiceInstance(assemblyContext,
                    this.systemService.getSystemId(), resourceContainerId, assemblyContextId));
        }

        String resourceSourceId = null;
        String resourceTargetId = null;
        String technology = null;
        for (int i = 0; i < connectors.size(); i++) {
            final Connector connector = connectors.get(i);
            if (connector instanceof AssemblyConnector) {

                // aim: find out which technology is used for communication
                // howTo: (AllocationModel) get the resourceContainer(s) on which the
                // source/targetAssemblyContext are deployed, (resourceEnvModel) get the
                // linkingResource which includes the resourceContainer in
                // connectedResourceContainers, entityName=technology
                final String assemContSourceId = ((AssemblyConnector) connector)
                        .getProvidingAssemblyContext_AssemblyConnector().getId();
                final String assemContTargetId = ((AssemblyConnector) connector)
                        .getRequiringAssemblyContext_AssemblyConnector().getId();

                for (int j = 0; j < allocationContexts.size(); j++) {
                    final AllocationContext allocationContext = allocationContexts.get(j);
                    final String actualAssemblyContextId = allocationContext.getAssemblyContext_AllocationContext()
                            .getId();
                    if (actualAssemblyContextId.equals(assemContSourceId)) {
                        resourceSourceId = allocationContext.getResourceContainer_AllocationContext().getId();
                    }
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
                this.sendPostRequest(this.communicationService.createCommunication((AssemblyConnector) connector,
                        this.systemService.getSystemId(), technology));

                this.sendPostRequest(
                        this.communicationinstanceService.createCommunicationinstance((AssemblyConnector) connector,
                                this.systemService.getSystemId(), this.communicationService.getCommunicationId()));

            } else {
                System.out.printf("no AssemblyConnector: %s\n", connector.getEntityName());
            }

        }

    }

    /**
     * Send change log updates to the visualization.
     *
     * @param modelData
     * @throws IOException
     */
    private void sendPostRequest(final JsonArray modelData) throws IOException {
        final HttpURLConnection connection;
        final JsonObject obj = modelData.getJsonObject(0);
        final JsonString type = (JsonString) obj.get("type");
        if (type.getString() == "system") {
            connection = (HttpURLConnection) this.systemUrl.openConnection();
        } else {
            connection = (HttpURLConnection) this.changelogUrl.openConnection();
        }

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", InitializeDeploymentVisualization.USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        // Send post request
        connection.setDoOutput(true);
        final JsonWriter jsonWriter = Json.createWriter(connection.getOutputStream());
        if (type.getString() == "system") {
            jsonWriter.write(obj);

            System.out.println("\nSending 'POST' request to URL : " + this.systemUrl);
            System.out.println("Post parameters : " + obj);

        } else {
            jsonWriter.writeArray(modelData); // work in progress

            System.out.println("\nSending 'POST' request to URL : " + this.changelogUrl);
            System.out.println("Post parameters : " + modelData);
        }

        jsonWriter.close();
        final int responseCode = connection.getResponseCode();
        System.out.println("Response Code : " + responseCode);

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        System.out.println(response.toString());

    }

}
