package org.iobserve.analysis.service.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * This stage is triggered by an analysis allocation update.
 *
 * @author jweg
 *
 */
public class AllocationVisualizationStage extends AbstractVisualizationStage<IAllocationRecord> {

    private static final String USER_AGENT = "iObserve/0.0.2";

    private final URL outputURL;

    private final ModelProvider<ResourceContainer> resourceEnvironmentModelGraph;
    private final String systemId;

    /**
     *
     * @param outputURL
     *            the output URL
     * @param resourceEnvironmentModelGraph
     *            the resource environment model graph
     */
    public AllocationVisualizationStage(final URL outputURL,
            final ModelProvider<ResourceContainer> resourceEnvironmentModelGraph, final String systemId) {
        this.outputURL = outputURL;
        this.resourceEnvironmentModelGraph = resourceEnvironmentModelGraph;
        this.systemId = systemId;
    }

    @Override
    protected void execute(final IAllocationRecord allocation) throws Exception {

        if (allocation instanceof ContainerAllocationEvent) {

            this.sendPostRequest(this.createData(allocation));
        }

    }

    private JsonArray createData(final IAllocationRecord allocation) throws MalformedURLException {
        final URL url = new URL(allocation.toArray()[0].toString());
        final String hostname = url.getHost();

        // final String path = url.getPath(); -> für die Serviceinstanz

        String resourceContainerId = "test-id";

        final List<ResourceContainer> resourceContainers = this.resourceEnvironmentModelGraph
                .readComponentByName(ResourceContainer.class, hostname);
        System.out.printf("resourceContainerIds:%s\n", resourceContainers);

        resourceContainerId = resourceContainers.get(0).getId();

        // nodeGroupId(knotengruppe erst noch
        // bilden)
        final JsonObject node = Json.createObjectBuilder().add("type", "node").add("id", resourceContainerId)
                .add("systemId", this.systemId).add("nodeGroupId", "node-group-id-analysis").add("hostname", hostname)
                .add("ip", "127.0.0.1").build();

        final JsonObject nodeData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", node).build();
        final JsonArray dataArray = Json.createArrayBuilder().add(nodeData).build();

        return dataArray;
    }

    /**
     * Send change log updates to the visualization.
     *
     * @param allocationData
     * @throws IOException
     */
    private void sendPostRequest(final JsonArray allocationData) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) this.outputURL.openConnection();

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", AllocationVisualizationStage.USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        connection.setDoOutput(true);

        final JsonWriter jsonWriter = Json.createWriter(connection.getOutputStream());

        jsonWriter.writeArray(allocationData);
        jsonWriter.close();

        final int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + this.outputURL);
        System.out.println("Post parameters : " + allocationData);
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
