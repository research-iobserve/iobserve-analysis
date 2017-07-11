package org.iobserve.analysis.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonWriter;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 *
 * @author jweg
 *
 */
public final class InitializeDeploymentVisualization {

    /** reference to allocation model graph. */
    private final ModelProvider<Allocation> allocationModelGraphProvider;
    /** reference to system model graph. */
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;
    /** reference to resource environment model graph. */
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

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
        this.systemId = systemModel.getId();

        final List<String> allocationIds = this.allocationModelGraphProvider.readComponentByType(Allocation.class);
        // the allocation model contains exactly one allocation
        final String allocationId = allocationIds.get(0);
        final Allocation allocation = this.allocationModelGraphProvider.readComponentById(Allocation.class,
                allocationId);
        final List<AllocationContext> allocationContexts = allocation.getAllocationContexts_Allocation();

        // this.sendPostRequest(this.createSystem(systemModel));
        System.out.println("createSystem()");
        this.changelogURL = new URL(this.systemURL + this.systemId + "/changelogs");

        for (int i = 0; i < allocationContexts.size(); i++) {
            final AllocationContext allocationContext = allocationContexts.get(i);
            this.sendPostRequest(this.createChangelog(allocationContext));
        }

    }

    /**
     *
     * @return
     */
    private JsonArray createSystem(final org.palladiosimulator.pcm.system.System systemModel) {

        final String systemName = systemModel.getEntityName();

        final JsonObject system = Json.createObjectBuilder().add("type", "system").add("id", this.systemId)
                .add("name", systemName).build();
        final JsonArray systemData = Json.createArrayBuilder().add(system).build();

        return systemData;
    }

    /**
     *
     *
     * @return
     */
    private JsonArray createChangelog(final AllocationContext allocationContext) {
        final JsonArrayBuilder nodeArrayBuilder = Json.createArrayBuilder();

        // resourceContainer
        final ResourceContainer resourceContainer = allocationContext.getResourceContainer_AllocationContext();

        final String resourceContainerId = resourceContainer.getId();
        final String hostname = resourceContainer.getEntityName();
        // as there is no grouping element available, each node gets its own nodegroup with a
        // random id
        final String nodeGroupId = "nodeGroup-" + Math.random();
        final JsonObject nodeGroup = Json.createObjectBuilder().add("type", "nodeGroup").add("id", nodeGroupId)
                .add("systemId", this.systemId).add("name", "nodeGroupName").build();
        final JsonObject nodeGroupData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", nodeGroup).build();
        final JsonObject node = Json.createObjectBuilder().add("type", "node").add("id", resourceContainerId)
                .add("systemId", this.systemId).add("nodeGroupId", nodeGroupId).add("hostname", hostname).build();
        final JsonObject nodeData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", node).build();
        // assemblyContext
        final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();

        final String assemblyContextId = assemblyContext.getId();
        final String assemblyContextName = assemblyContext.getEntityName();

        final JsonObject service = Json.createObjectBuilder().add("type", "service").add("id", assemblyContextId)
                .add("systemId", this.systemId).add("name", assemblyContextName).build();
        final JsonObject serviceData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", service).build();
        // TODO serviceInstance-id
        final JsonObject serviceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", "serviceInstance-id-analysis" + Math.random()).add("systemId", this.systemId)
                .add("name", "analysis-serviceInstance").add("serviceId", assemblyContextId)
                .add("nodeId", resourceContainerId).build();
        final JsonObject serviceInstanceData = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", serviceInstance).build();

        // nodeArrayBuilder.add(nodeGroupData).add(nodeData).add(serviceData);//
        // .add(serviceInstanceData);
        // communication data
        nodeArrayBuilder.add(serviceInstanceData);
        // communication instance data

        final JsonArray dataArray = nodeArrayBuilder.build();
        return dataArray;
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
