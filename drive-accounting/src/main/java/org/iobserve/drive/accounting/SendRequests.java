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

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Reiner Jung
 *
 */
public class SendRequests extends AbstractConsumerStage<Object> {

    private final OutputPort<Response> outputPort = this.createOutputPort(Response.class);

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
            this.outputPort.send(this.sendLogin((Login) element));
        } else if (element instanceof Account) {
            this.outputPort.send(this.sendAccount((Account) element));
        }
    }

    private Response sendLogin(final Login element) throws IOException {
        return HttpRequestUtils.get(HttpRequestUtils.createUrl(this.url, "request-user", "username",
                element.getUsername(), "password", element.getPassword()));
    }

    private Response sendAccount(final Account element) throws JsonProcessingException, IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return HttpRequestUtils.post(HttpRequestUtils.createUrl(this.url, "update-account"),
                mapper.writeValueAsString(element));
    }

    public OutputPort<Response> getOutputPort() {
        return this.outputPort;
    }
}
