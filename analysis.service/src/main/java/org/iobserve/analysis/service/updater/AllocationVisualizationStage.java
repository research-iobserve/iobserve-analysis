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
import org.iobserve.analysis.service.services.NodeService;
import org.iobserve.analysis.service.services.NodegroupService;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import teetime.framework.AbstractConsumerStage;

/**
 * This stage is triggered by an analysis allocation update (resource container added) and updates
 * the deployment visualization by adding a node. Therefore it gets the information about the added
 * resource container from the resource environment model.
 *
 * @author jweg
 *
 */
public class AllocationVisualizationStage extends AbstractConsumerStage<IAllocationRecord> {

    private final NodegroupService nodegroupService = new NodegroupService();
    private final NodeService nodeService = new NodeService();

    private static final String USER_AGENT = "iObserve/0.0.2";

    private final URL outputURL;
    private final String systemId;
    private final ModelProvider<ResourceContainer> resourceContainerModelProvider;

    /**
     * @param outputURL
     *            the output URL
     * @param systemId
     *            the system id
     * @param resourceEnvironmentModelProvider
     *            the resource environment model graph
     */
    public AllocationVisualizationStage(final URL outputURL, final String systemId,
            final ModelProvider<ResourceContainer> resourceContainerModelProvider) {
        this.outputURL = outputURL;
        this.resourceContainerModelProvider = resourceContainerModelProvider;
        this.systemId = systemId;
    }

    @Override
    protected void execute(final IAllocationRecord allocation) throws Exception {

        if (allocation instanceof ContainerAllocationEvent) {

            this.sendPostRequest(this.createData(allocation));
        }

    }

    /**
     * This method is triggered for every container allocation event.
     *
     * @param allocation
     *            the container allocation event
     * @return JsonArray holding necessary information to create a nodegroup and a node in the
     *         deployment visualization
     * @throws MalformedURLException
     */
    private JsonArray createData(final IAllocationRecord allocation) throws MalformedURLException {
        final URL url = new URL(allocation.toArray()[0].toString());
        final String hostname = url.getHost();

        final List<ResourceContainer> resourceContainerHostname = this.resourceContainerModelProvider
                .readOnlyComponentByName(ResourceContainer.class, hostname);

        final ResourceContainer resourceContainer = resourceContainerHostname.get(0);

        // Each node has its own nodegroup now. This can/should change in future.
        final JsonObject nodegroupObject = this.nodegroupService.createNodegroup(this.systemId);
        final JsonObject nodeObject = this.nodeService.createNode(resourceContainer, this.systemId,
                this.nodegroupService.getNodegroupId());
        final JsonArray dataArray = Json.createArrayBuilder().add(nodegroupObject).add(nodeObject).build();

        return dataArray;
    }

    /**
     * Send change log updates to the visualization.
     *
     * @param allocationData
     * @throws IOException
     */
    // TODO put this in AbstractVisualizationStage
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
