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

import teetime.framework.Configuration;

/**
 *
 * @author Reiner Jung
 *
 */
public class DrivePipelineConfiguration extends Configuration {

    /**
     * Create the teetime configuration.
     *
     * @param input
     *            input file
     * @throws IOException
     *             on io errors
     */
    public DrivePipelineConfiguration(final URL url) throws IOException {
        final GenerateAccountingRequests accountingRequests = new GenerateAccountingRequests();
        final SendRequests jsonRequest = new SendRequests(url);

        this.connectPorts(accountingRequests.getOutputPort(), jsonRequest.getInputPort());
    }
}
