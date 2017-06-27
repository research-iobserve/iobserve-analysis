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
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonWriter;

import org.iobserve.common.record.IDeploymentRecord;

import teetime.framework.AbstractConsumerStage;

// TODO the data type of the input element must be clarified

/**
 * This stage is triggered by an analysis deployment update.
 *
 * @author Reiner Jung
 *
 */
public class VisualizationDeploymentStage extends AbstractConsumerStage<IDeploymentRecord> {

    private static final String USER_AGENT = "iObserve/0.0.2";

    private final URL outputURL;

    /**
     * Output visualization configuration.
     *
     * @param outputURL
     *            the output URL
     */
    public VisualizationDeploymentStage(final URL outputURL) {
        this.outputURL = outputURL;
    }

    @Override
    protected void execute(final IDeploymentRecord allocate) {
        try {
            if (allocate != null) {
                this.sendPostRequest(this.deployment(allocate));

            }
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private JsonArray deployment(final IDeploymentRecord allocate) {

        final JsonObject nodeGroup = Json.createObjectBuilder().add("type", "nodeGroup")
                .add("id", "node-group-id-analysis").add("systemId", "CoCoME").add("name", "XUbuntu VM").build();

        final JsonObject node1 = Json.createObjectBuilder().add("type", "node").add("id", "node-id-analysis-1")
                .add("systemId", "CoCoME").add("nodeGroupId", "node-group-id-analysis").add("hostname", "localhost")
                .add("ip", "127.0.0.1").build();

        final JsonObject service = Json.createObjectBuilder().add("type", "service").add("id", "service-id-analysis")
                .add("systemId", "CoCoME").add("name", "analysis-service").build();
        final JsonObject serviceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", "serviceInstance-id-analysis").add("systemId", "CoCoME")
                .add("name", "analysis-serviceInstance").add("serviceId", "service-id-analysis")
                .add("nodeId", "node-id-analysis-1").build();

        final JsonObject communication = Json.createObjectBuilder().add("type", "communication")
                .add("id", "communication-id-analysis").add("systemId", "CoCoME").add("technology", "TCP/IP")
                .add("sourceId", "service-id-analysis").add("targetId", "test-CoCoME-service-1").build();
        final JsonObject communicationInst = Json.createObjectBuilder().add("type", "communicationInstance")
                .add("id", "communicationInst-id-analysis" + Math.random()).add("systemId", "CoCoME")
                .add("communicationId", "communication-id-analysis").add("workload", "17")
                .add("sourceId", "serviceInstance-id-analysis").add("targetId", "test-CoCoME-serviceInstance-1")
                .build();

        final JsonObject nodeGroupData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", nodeGroup).build();
        final JsonObject nodeData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", node1).build();
        final JsonObject serviceData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", service).build();
        final JsonObject serviceInstData = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", serviceInstance).build();

        final JsonObject communicationData = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", communication).build();
        final JsonObject communicationInstData = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", communicationInst).build();

        final JsonArray dataArray = Json.createArrayBuilder().add(communicationInstData).build();

        return dataArray;
    }

    /**
     * Send change log updates to the visualization.
     *
     * @param systemId
     * @param message
     * @throws IOException
     */
    private void sendPostRequest(final JsonArray message) throws IOException {

        final HttpURLConnection connection = (HttpURLConnection) this.outputURL.openConnection();

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", VisualizationDeploymentStage.USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        connection.setDoOutput(true);

        final JsonWriter jsonWriter = Json.createWriter(connection.getOutputStream());

        jsonWriter.writeArray(message);
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
}
