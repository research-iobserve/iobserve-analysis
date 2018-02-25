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
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;

public class VisualizationHttpTestServer extends NanoHTTPD {

    private static final int VISUALIZATION_PORT = 9090;
    private static final String URL = "/v1/systems/test_systemId/changelogs";
    private static String response;

    public VisualizationHttpTestServer() throws IOException {
        super(VisualizationHttpTestServer.VISUALIZATION_PORT);
        this.start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    @Override
    public Response serve(final IHTTPSession session) {
        if (session.getUri().equals(VisualizationHttpTestServer.URL)) {
            final InputStream is = session.getInputStream();

            try {
                VisualizationHttpTestServer.response = IOUtils.toString(is, "UTF-8");
                final String response = "Changelog received";

                final String mimeType = "text/plain";
                return NanoHTTPD.newFixedLengthResponse(Status.OK, mimeType, response);
            } catch (final IOException e) {
                return NanoHTTPD.newFixedLengthResponse("FAILED");
            }
        } else {
            return NanoHTTPD.newFixedLengthResponse("FAILED");
        }
    }

    public static String getRequestBody() {
        return VisualizationHttpTestServer.response;
    }
}
