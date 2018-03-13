/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.service.suites;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

/**
 * .
 *
 * @author Josefine Wegert
 *
 */
public class VisualizationHttpTestServer extends NanoHTTPD {

    private static final Logger LOGGER = LoggerFactory.getLogger(VisualizationHttpTestServer.class);

    private static final int VISUALIZATION_PORT = 9090;
    private static final String URL = "/v1/systems/test_systemId/changelogs";
    private static String response;

    public VisualizationHttpTestServer() throws IOException {
        super(VisualizationHttpTestServer.VISUALIZATION_PORT);
        VisualizationHttpTestServer.LOGGER.info("Initialization http server");
        this.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        VisualizationHttpTestServer.LOGGER.info("HTTP after start.");
        VisualizationHttpTestServer.response = "";
    }

    @Override
    public Response serve(final IHTTPSession session) {
        VisualizationHttpTestServer.LOGGER.info("http - response [" + session.getUri() + "]");
        if (session.getUri().equals(VisualizationHttpTestServer.URL)) {
            if (session.getMethod() == Method.POST) {
                try {
                    final Map<String, String> data = new HashMap<>();
                    session.parseBody(data);
                    VisualizationHttpTestServer.response = data.get("postData");

                    final String mimeType = "text/plain";
                    return NanoHTTPD.newFixedLengthResponse(Status.OK, mimeType, VisualizationHttpTestServer.response);
                } catch (IOException | ResponseException e) {
                    VisualizationHttpTestServer.LOGGER.error(e.getLocalizedMessage());
                    VisualizationHttpTestServer.LOGGER.error(e.getStackTrace().toString());
                    return NanoHTTPD.newFixedLengthResponse("FAILED");
                }
            } else {
                return NanoHTTPD.newFixedLengthResponse("get");
            }
        } else {
            return NanoHTTPD.newFixedLengthResponse("FAILED");
        }
    }

    public static String getRequestBody() {
        return VisualizationHttpTestServer.response;
    }
}
