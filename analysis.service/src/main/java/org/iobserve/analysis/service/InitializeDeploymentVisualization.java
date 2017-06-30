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
import javax.json.JsonWriter;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.neo4j.graphdb.GraphDatabaseService;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.system.System;

/**
 *
 * @author jweg
 *
 */
public class InitializeDeploymentVisualization {

    /** reference to allocation model graph. */
    private final GraphDatabaseService allocationModelGraph;
    /** reference to system model graph. */
    private final GraphDatabaseService systemModelGraph;
    /** reference to resource environment model graph. */
    private final GraphDatabaseService resourceEnvironmentModelGraph;

    private static final String USER_AGENT = "iObserve/0.0.2";

    private final URL outputURL;

    /**
     * constructor
     *
     * @param allocationModelProvider
     * @param systemModelProvider
     * @param resourceEnvironmentModelProvider
     */
    public InitializeDeploymentVisualization(final URL outputURL, final GraphDatabaseService allocationModelGraph,
            final GraphDatabaseService systemModelGraph, final GraphDatabaseService resourceEnvironmentModelGraph) {
        // get all model references
        this.outputURL = outputURL;
        this.allocationModelGraph = allocationModelGraph;
        this.systemModelGraph = systemModelGraph;
        this.resourceEnvironmentModelGraph = resourceEnvironmentModelGraph;
    }

    /**
     *
     * @return
     */
    private JsonArray createData() {
        // system data
        final ModelProvider<System> systemModelProvider = new ModelProvider<>(this.systemModelGraph);
        final System system = systemModelProvider.readRootComponent(System.class);
        final String systemId = system.getId();
        final String systemName = system.getEntityName();

        final JsonObject systemData = Json.createObjectBuilder().add("type", "system").add("id", systemId)
                .add("name", systemName).build();
        // system extra schicken, weil ja kein changelog?!
        JsonArray dataArray = Json.createArrayBuilder().add(systemData).build();
        // nodegroup data
        // node data
        final ModelProvider<ResourceContainer> resourceEnvironmentModelProvider = new ModelProvider<>(
                this.resourceEnvironmentModelGraph);
        final List<String> resourceContainerIds = resourceEnvironmentModelProvider
                .readComponentByType(ResourceContainer.class);
        for (int i = 0; i < resourceContainerIds.size(); i++) {
            final String resourceContainerId = resourceContainerIds.get(i);
            final ResourceContainer resourceContainer = resourceEnvironmentModelProvider
                    .readComponentById(ResourceContainer.class, resourceContainerId);
            final String hostname = resourceContainer.getEntityName();

            final JsonObject node = Json.createObjectBuilder().add("type", "node").add("id", resourceContainerId)
                    .add("systemId", systemId).add("nodeGroupId", "node-group-id-analysis").add("hostname", hostname)
                    .add("ip", "127.0.0.1").build();

            final JsonObject nodeData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                    .add("data", node).build();

            dataArray = Json.createArrayBuilder().add(nodeData).build();

        }

        // service data-> id und name von assemblyContext über systemModel
        final ModelProvider<AssemblyContext> assemblyContextModelProvider = new ModelProvider<>(this.systemModelGraph);
        final List<String> assemblyContextIds = assemblyContextModelProvider.readComponentByType(AssemblyContext.class);
        for (int i = 0; i < assemblyContextIds.size(); i++) {
            final String assemblyContextId = assemblyContextIds.get(i);
            final AssemblyContext assemblyContext = assemblyContextModelProvider
                    .readComponentById(AssemblyContext.class, assemblyContextId);
            final String assemblyContextName = assemblyContext.getEntityName();

            final JsonObject service = Json.createObjectBuilder().add("type", "service").add("id", assemblyContextId)
                    .add("systemId", systemId).add("name", assemblyContextName).build();
        }
        // service instance data
        final JsonObject serviceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", "serviceInstance-id-analysis").add("systemId", systemId)
                .add("name", "analysis-serviceInstance").add("serviceId", "assemblyContextId")
                .add("nodeId", "node-id-analysis-1").build();
        return dataArray;

    }

    /**
     * Send change log updates to the visualization.
     *
     * @param modelData
     * @throws IOException
     */
    private void sendPostRequest(final JsonArray modelData) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) this.outputURL.openConnection();

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", InitializeDeploymentVisualization.USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        connection.setDoOutput(true);

        final JsonWriter jsonWriter = Json.createWriter(connection.getOutputStream());

        jsonWriter.writeArray(modelData);
        jsonWriter.close();

        final int responseCode = connection.getResponseCode();
        // System.out.println("\nSending 'POST' request to URL : " + this.outputURL);
        // System.out.println("Post parameters : " + modelData);
        // System.out.println("Response Code : " + responseCode);

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        // System.out.println(response.toString());

    }

}
