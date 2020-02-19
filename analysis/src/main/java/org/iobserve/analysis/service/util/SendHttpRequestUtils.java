/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.service.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.json.JsonWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This helper class actually sends data to the deployment visualization.
 *
 * @author jweg
 *
 */
public final class SendHttpRequestUtils {

    private static final String USER_AGENT = "iObserve/0.0.3";
    private static final Logger LOGGER = LoggerFactory.getLogger(SendHttpRequestUtils.class);

    private SendHttpRequestUtils() {

    }

    /**
     * Send change log updates to the visualization.
     *
     * @param modelData
     *            actual data that will be send
     * @param systemUrl
     *            Url for sending a system element to the visualization
     * @param changelogUrl
     *            Url for sending changelogs to the visualization
     * @throws IOException
     *             if the input stram could not be read
     */
    public static void post(final JsonObject modelData, final URL systemUrl, final URL changelogUrl)
            throws IOException {
        final HttpURLConnection connection;
        // prepare data for changelog constraint of deployment visualization
        final JsonArray dataArray = Json.createArrayBuilder().add(modelData).build();

        final JsonString type = (JsonString) modelData.get("type");
        if ("system".equals(type.getString())) {
            connection = (HttpURLConnection) systemUrl.openConnection();
        } else {
            connection = (HttpURLConnection) changelogUrl.openConnection();
        }

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", SendHttpRequestUtils.USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        // Send post request
        connection.setDoOutput(true);
        final JsonWriter jsonWriter = Json.createWriter(connection.getOutputStream());
        if ("system".equals(type.getString())) {
            jsonWriter.write(modelData);

            SendHttpRequestUtils.LOGGER.debug("Sending 'POST' request to URL : {}", systemUrl);
            SendHttpRequestUtils.LOGGER.debug("Post parameters : {}", modelData);

        } else {
            jsonWriter.writeArray(dataArray); // work in progress

            SendHttpRequestUtils.LOGGER.debug("Sending 'POST' request to URL : {}", changelogUrl);
            SendHttpRequestUtils.LOGGER.debug("Post parameters : {}", dataArray);
        }

        jsonWriter.close();

        final int responseCode = connection.getResponseCode();
        SendHttpRequestUtils.LOGGER.debug("Response Code : {}", responseCode);

        if (responseCode != 204) {
            final BufferedReader err = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String errLine;
            final StringBuffer error = new StringBuffer();

            while ((errLine = err.readLine()) != null) {
                error.append(errLine);
            }
            err.close();

            SendHttpRequestUtils.LOGGER.error("error:", error);
        }

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        SendHttpRequestUtils.LOGGER.debug(response.toString());

    }

    /**
     * Send change log updates to the visualization.
     *
     * @param dataArray
     *            actual data that will be send
     * @param changelogUrl
     *            Url for sending changelogs to the visualization
     * @throws IOException
     *             if the input stream fails.
     */
    public static void post(final JsonArray dataArray, final URL changelogUrl) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) changelogUrl.openConnection();
        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", SendHttpRequestUtils.USER_AGENT);
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

        // Send post request
        connection.setDoOutput(true);

        final JsonWriter jsonWriter = Json.createWriter(connection.getOutputStream());

        jsonWriter.writeArray(dataArray);

        jsonWriter.close();

        final int responseCode = connection.getResponseCode();
        SendHttpRequestUtils.LOGGER.debug("Sending 'POST' request to URL : {}", changelogUrl);
        SendHttpRequestUtils.LOGGER.debug("Post parameters : {}", dataArray);
        SendHttpRequestUtils.LOGGER.debug("Response Code : {}", responseCode);

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        SendHttpRequestUtils.LOGGER.info(response.toString());

    }

}
