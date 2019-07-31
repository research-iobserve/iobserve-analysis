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

import teetime.framework.AbstractConsumerStage;
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
     * @param settings
     *            all settings
     * @throws IOException
     *             on io errors
     */
    public DrivePipelineConfiguration(final AccountDriverSettings settings) throws IOException {
        final GenerateAccountingRequests accountingRequests = new GenerateAccountingRequests(settings.getRepetition());
        final SendRequests jsonRequest = new SendRequests(settings.getUrl(), settings.getDelay());

        final AbstractConsumerStage<Response> handleResponses;
        if (settings.getReportModulo() == null) {
            handleResponses = new LogResponses();
        } else {
            handleResponses = new CountResponses(settings.getReportModulo());
        }

        this.connectPorts(accountingRequests.getOutputPort(), jsonRequest.getInputPort());
        this.connectPorts(jsonRequest.getOutputPort(), handleResponses.getInputPort());
    }
}
