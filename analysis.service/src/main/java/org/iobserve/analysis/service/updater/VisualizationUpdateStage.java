/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.service.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import org.iobserve.analysis.data.AddAllocationContextEvent;
import org.iobserve.analysis.data.RemoveAllocationContextEvent;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;

// TODO the data type of the input element must be clarified

/**
 * This stage is triggered by an analysis update.
 *
 * @author Reiner Jung
 *
 */
public class VisualizationUpdateStage extends AbstractStage {

    private static final String USER_AGENT = "iObserve/0.0.2";

    private final URL outputURL;

    private final InputPort<AddAllocationContextEvent> deploymentInputPort = super.createInputPort();

    private final InputPort<RemoveAllocationContextEvent> undeploymentInputPort = super.createInputPort();

    /**
     * Output visualization configuration.
     *
     * @param outputURL
     *            the output URL
     */
    public VisualizationUpdateStage(final URL outputURL) {
        this.outputURL = outputURL;
    }

    @Override
    protected void execute() {
        try {
            final AddAllocationContextEvent allocate = this.deploymentInputPort.receive();
            final RemoveAllocationContextEvent deallocate = this.undeploymentInputPort.receive();
            if (allocate != null) {
                this.sendPostRequest(this.deployment(allocate));
            }
            if (deallocate != null) {
                this.sendPostRequest(this.undeployment(deallocate));
            }
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private JsonObject deployment(final AddAllocationContextEvent allocate) {
        final JsonObject data = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE").build();

        return data;
    }

    private JsonObject undeployment(final RemoveAllocationContextEvent unallocate) {
        final JsonObject data = Json.createObjectBuilder().add("type", "changelog").add("operation", "DELETE").build();

        return data;
    }

    /**
     * Send change log updates to the visualization.
     *
     * @param systemId
     * @param message
     * @throws IOException
     */
    private void sendPostRequest(final JsonObject message) throws IOException {

        final HttpURLConnection connection = (HttpURLConnection) this.outputURL.openConnection();

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", VisualizationUpdateStage.USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        connection.setDoOutput(true);

        final JsonWriter jsonWriter = Json.createWriter(connection.getOutputStream());

        jsonWriter.writeObject(message);
        jsonWriter.close();

        final int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + this.outputURL);
        System.out.println("Post parameters : " + message);
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

    public InputPort<AddAllocationContextEvent> getDeploymentInputPort() {
        return this.deploymentInputPort;
    }

    public InputPort<RemoveAllocationContextEvent> getUndeploymentInputPort() {
        return this.undeploymentInputPort;
    }
}
