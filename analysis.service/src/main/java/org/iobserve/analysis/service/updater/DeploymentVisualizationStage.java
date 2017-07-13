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
import javax.json.JsonWriter;

import org.iobserve.analysis.service.services.NodeService;
import org.iobserve.analysis.service.services.ServiceService;
import org.iobserve.analysis.service.services.ServiceinstanceService;
import org.iobserve.common.record.IDeploymentRecord;

import teetime.framework.AbstractConsumerStage;

// TODO the data type of the input element must be clarified

/**
 * This stage is triggered by an analysis deployment update.
 *
 * @author Reiner Jung
 *
 */
public class DeploymentVisualizationStage extends AbstractConsumerStage<IDeploymentRecord> {

    private final NodeService nodeService = new NodeService();
    private final ServiceService serviceService = new ServiceService();
    private final ServiceinstanceService serviceinstanceService = new ServiceinstanceService();

    private static final String USER_AGENT = "iObserve/0.0.2";

    private final URL outputURL;

    /**
     * Output visualization configuration.
     *
     * @param outputURL
     *            the output URL
     */
    public DeploymentVisualizationStage(final URL outputURL) {
        this.outputURL = outputURL;
    }

    @Override
    protected void execute(final IDeploymentRecord deployment) {

        // this.sendPostRequest(this.deployment(deployment));

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
        connection.setRequestProperty("User-Agent", DeploymentVisualizationStage.USER_AGENT);
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
