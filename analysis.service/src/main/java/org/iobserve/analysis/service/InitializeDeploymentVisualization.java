package org.iobserve.analysis.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonWriter;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.service.services.CommunicationService;
import org.iobserve.analysis.service.services.CommunicationinstanceService;
import org.iobserve.analysis.service.services.NodeService;
import org.iobserve.analysis.service.services.NodegroupService;
import org.iobserve.analysis.service.services.ServiceService;
import org.iobserve.analysis.service.services.ServiceinstanceService;
import org.iobserve.analysis.service.services.SystemService;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

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
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    /** services for visualization elements */
    private final SystemService systemService = new SystemService();
    private final NodegroupService nodegroupService = new NodegroupService();
    private final NodeService nodeService = new NodeService();
    private final ServiceService serviceService = new ServiceService();
    private final ServiceinstanceService serviceinstanceService = new ServiceinstanceService();
    private final CommunicationService communicationService = new CommunicationService();
    private final CommunicationinstanceService communicationinstanceService = new CommunicationinstanceService();

    private static final String USER_AGENT = "iObserve/0.0.2";

    private URL changelogURL;
    private final URL systemURL;
    private String systemId;

    /**
     * constructor
     *
     * @param systemURL
     * @param changelogURL
     * @param allocationModelGraphProvider
     * @param systemModelGraphProvider
     * @param resourceEnvironmentModelGraphProvider
     */

    public InitializeDeploymentVisualization(final URL systemURL,
            final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        // get all model references
        this.systemURL = systemURL;
        // this.changelogURL = changelogURL;
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    protected void initialize() throws Exception {
        final org.palladiosimulator.pcm.system.System systemModel = this.systemModelGraphProvider
                .readRootComponent(org.palladiosimulator.pcm.system.System.class);
        final List<String> allocationIds = this.allocationModelGraphProvider.readComponentByType(Allocation.class);
        // an allocation model contains exactly one allocation
        final String allocationId = allocationIds.get(0);
        final Allocation allocation = this.allocationModelGraphProvider.readComponentById(Allocation.class,
                allocationId);
        final List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();
        final ResourceEnvironment resourceEnvironmentModel = this.resourceEnvironmentModelGraphProvider
                .readRootComponent(ResourceEnvironment.class);
        final List<LinkingResource> linkingResources = resourceEnvironmentModel
                .getLinkingResources__ResourceEnvironment();

        this.sendPostRequest(this.systemService.createSystem(systemModel));
        // create URL for changelogs
        this.changelogURL = new URL(this.systemURL + this.systemService.getSystemId() + "/changelogs");

        for (int i = 0; i < allocationContexts.size(); i++) {
            final AllocationContext allocationContext = allocationContexts.get(i);
            this.sendPostRequest(this.nodegroupService.createNodegroup(this.systemService.getSystemId()));
            this.sendPostRequest(this.nodeService.createNode(allocationContext, this.systemService.getSystemId(),
                    this.nodegroupService.getNodegroupId()));
            this.sendPostRequest(
                    this.serviceService.createService(allocationContext, this.systemService.getSystemId()));
            this.sendPostRequest(this.serviceinstanceService.createServiceInstance(this.systemService.getSystemId(),
                    this.nodeService.getNodeId(), this.serviceService.getServiceId()));

        }

        for (int i = 0; i < linkingResources.size(); i++) {
            final List<ResourceContainer> connectedResourceContainers = linkingResources.get(i)
                    .getConnectedResourceContainers_LinkingResource();

            this.sendPostRequest(this.communicationService.createCommunication(resourceEnvironmentModel,
                    this.systemService.getSystemId()));

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
            connection = (HttpURLConnection) this.systemURL.openConnection();
        } else {
            connection = (HttpURLConnection) this.changelogURL.openConnection();
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

            System.out.println("\nSending 'POST' request to URL : " + this.systemURL);
            System.out.println("Post parameters : " + obj);

        } else {
            jsonWriter.writeArray(modelData); // work in progress

            System.out.println("\nSending 'POST' request to URL : " + this.changelogURL);
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
