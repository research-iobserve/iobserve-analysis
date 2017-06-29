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
import org.neo4j.graphdb.GraphDatabaseService;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class AllocationVisualizationStage extends AbstractVisualizationStage<IAllocationRecord> {

    private static final String USER_AGENT = "iObserve/0.0.2";

    private final URL outputURL;

    private final GraphDatabaseService resourceEnvironmentModelGraph;

    // public AllocationVisualizationStage(final URL outputURL, final GraphDatabaseService
    // resourceEnvironmentModelGraph) {
    // this.outputURL = outputURL;
    // this.resourceEnvironmentModelGraph = resourceEnvironmentModelGraph;
    // }

    public AllocationVisualizationStage(final URL outputURL, final GraphDatabaseService resourceEnvironmentModelGraph) {
        this.outputURL = outputURL;
        this.resourceEnvironmentModelGraph = resourceEnvironmentModelGraph;
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
        System.out.printf("hostname:%s\n", hostname);

        // final String path = url.getPath(); -> für die Serviceinstanz

        // Id vom ResourceContainer: funktioniert noch nicht, weil man den Graph noch nicht updaten
        // kann
        // Soll noch Funktion geben, die den Namen vom ResourceContainer nimmt und dann die Id
        // zurückgibt.
        String nodeId = "test-id";
        final ModelProvider<ResourceContainer> resourceEnvironmentModel = new ModelProvider<>(
                this.resourceEnvironmentModelGraph);

        final List<String> resourceContainerIds = resourceEnvironmentModel.readComponent(ResourceContainer.class);
        System.out.printf("resourceContainerIds:%s\n", resourceContainerIds);
        for (final String id : resourceContainerIds) {
            final ResourceContainer resourceContainer = resourceEnvironmentModel.readComponent(ResourceContainer.class,
                    id);
            System.out.printf("resourceContainer.getEntityName():%s\n", resourceContainer.getEntityName());
            if (resourceContainer.getEntityName() == hostname) {
                nodeId = resourceContainer.getId();
            } else {
                System.out.println("Error: No nodeId for the ResourceContainer with this name.");
            }

        }

        // final List<String> resourceContainerIds = resourceEnvironmentModel

        // brauchen dafür systemId und nodeGroupId
        final JsonObject node = Json.createObjectBuilder().add("type", "node").add("id", nodeId)
                .add("systemId", "CoCoME").add("nodeGroupId", "node-group-id-analysis").add("hostname", hostname)
                .add("ip", "127.0.0.1").build();

        final JsonObject nodeData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", node).build();
        final JsonArray dataArray = Json.createArrayBuilder().add(nodeData).build();

        return dataArray;
    }

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
