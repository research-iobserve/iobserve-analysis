/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.drive.accounting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import teetime.framework.AbstractConsumerStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reiner Jung
 *
 */
public class SendRequests extends AbstractConsumerStage<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendRequests.class);

    private final URL url;

    public SendRequests(final URL url) {
        this.url = url;
    }

    /*
     * (non-Javadoc)
     *
     * @see teetime.framework.AbstractConsumerStage#execute(java.lang.Object)
     */
    @Override
    protected void execute(final Object element) throws Exception {
        if (element instanceof Login) {
            this.sendLogin((Login) element);
        } else if (element instanceof Account) {
            this.sendAccount((Account) element);
        }
    }

    private void sendAccount(final Account element) throws JsonProcessingException, IOException {
        final ObjectMapper mapper = new ObjectMapper();
        SendRequests.post(this.url, mapper.writeValueAsString(element));
    }

    private void sendLogin(final Login element) throws IOException {
        SendRequests.get(this.url, "username", element.getUsername(), "password", element.getPassword());
    }

    private static void get(final URL url, final String... parameters) throws IOException {
        final String urlString = url.toExternalForm();

        if (parameters.length > 0) {
            urlString += '?';
            String end = "=";
            for (final String parameter : parameters) {
                urlString += parameter + end;
                if ("=".equals(end)) {
                    end = "&";
                } else {
                    end = "=";
                }
            }
        }

        final URL completeUrl = new URL(urlString);

        final HttpURLConnection connection = (HttpURLConnection) completeUrl.openConnection();

        connection.setRequestMethod("GET");

        // add request header
        connection.setRequestProperty("User-Agent", "DriveAccounting");

        final int responseCode = connection.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
    }

    /**
     * Send change log updates to the visualization.
     *
     * @param url
     *            url
     * @param value
     *            data
     * @throws IOException
     *             if the input stream could not be read
     */
    private static void post(final URL url, final String value) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json; charset=utf-8");
        connection.setRequestProperty("User-Agent", "DriveAccounting");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        // Send post request
        connection.setDoOutput(true);

        final OutputStream out = connection.getOutputStream();
        out.write(value.getBytes(Charset.defaultCharset()));
        out.close();

        final int responseCode = connection.getResponseCode();
        SendRequests.LOGGER.debug("Response Code : {}", responseCode);

        if (responseCode != 204) {
            final BufferedReader err = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            String errLine;
            final StringBuffer error = new StringBuffer();

            while ((errLine = err.readLine()) != null) {
                error.append(errLine);
            }
            err.close();

            SendRequests.LOGGER.error("error:", error);
        }

        final BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        final StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // print result
        SendRequests.LOGGER.debug(response.toString());
    }

}
