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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * This class handles the requests from the visualization stages for testing reasons.
 *
 * @author jweg
 *
 */
public final class TestHandler implements HttpHandler {

    private static String requestBody;

    @Override
    public void handle(final HttpExchange t) throws IOException {

        final InputStream is = t.getRequestBody();

        TestHandler.requestBody = IOUtils.toString(is, "UTF-8");

        final String response = "Changelog received";
        t.sendResponseHeaders(200, response.length());
        final OutputStream os = t.getResponseBody();
        os.write(response.getBytes());
        os.close();

    }

    public static String getRequestBody() {
        return TestHandler.requestBody;
    }

}
