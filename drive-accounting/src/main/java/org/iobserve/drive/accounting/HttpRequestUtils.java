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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Reiner Jung
 *
 * @since 0.0.3
 *
 */
public final class HttpRequestUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestUtils.class);

    private HttpRequestUtils() {
        // nothing to do factory
    }

    public static URL createUrl(final URL baseUrl, final String operation, final String... parameters)
            throws MalformedURLException {
        String urlString = baseUrl.toString() + "/" + operation;

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

        return completeUrl;
    }

    public static Response get(final URL url) throws IOException {
        HttpRequestUtils.LOGGER.debug("Get {}", url.toString());

        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        // add request header
        connection.setRequestProperty("User-Agent", "DriveAccounting");

        return HttpRequestUtils.processResponse(connection);
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
    public static Response post(final URL url, final String value) throws IOException {
        HttpRequestUtils.LOGGER.debug("Post {}", url.toString());
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // add request header
        connection.setRequestMethod("POST");
        connection.setRequestProperty("content-type", "application/json"); // ; charset=utf-8");
        connection.setRequestProperty("User-Agent", "DriveAccounting");
        connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
        // Send post request
        connection.setDoOutput(true);

        final OutputStream out = connection.getOutputStream();
        out.write(value.getBytes(Charset.defaultCharset()));
        out.close();

        return HttpRequestUtils.processResponse(connection);
    }

    private static Response processResponse(final HttpURLConnection connection) throws IOException {
        final int responseCode = connection.getResponseCode();
        HttpRequestUtils.LOGGER.debug("Response code {}", responseCode);

        if (connection.getErrorStream() != null) {
            final char[] errorMessage = HttpRequestUtils.readData(connection.getErrorStream(),
                    connection.getContentLength());

            HttpRequestUtils.LOGGER.error("HTTP response error message {}", String.valueOf(errorMessage));
            return new Response(responseCode, errorMessage);
        }

        final char[] message;
        if (responseCode != 204) {
            if ((responseCode >= 200) && (responseCode <= 299)) {
                message = HttpRequestUtils.readData(connection.getInputStream(), connection.getContentLength());

                HttpRequestUtils.LOGGER.debug("HTTP response message {}", String.valueOf(message));
            } else {
                message = new char[0];
                HttpRequestUtils.LOGGER.debug("HTTP response error without a message");
            }
        } else {
            message = new char[0];
            HttpRequestUtils.LOGGER.debug("HTTP response without a message");
        }
        return new Response(responseCode, message);
    }

    private static char[] readData(final InputStream inputStream, final int contentLength) throws IOException {
        final CharBuffer buffer = CharBuffer.allocate(contentLength);

        final BufferedReader input = new BufferedReader(new InputStreamReader(inputStream));
        while ((input.read(buffer) != -1) && (buffer.position() < contentLength)) {
        }
        buffer.flip();
        input.close();
        return buffer.array();
    }

}
