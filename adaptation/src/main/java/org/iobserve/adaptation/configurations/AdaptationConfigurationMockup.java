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
package org.iobserve.adaptation.configurations;

import java.io.File;

import teetime.framework.Configuration;

import org.iobserve.adaptation.stages.ExecutionPlanSerializationMockup;
import org.iobserve.stages.source.SingleConnectionTcpWriterStage;

/**
 * A configuration for mocking iObserve's adaptation service.
 *
 * @author Lars Bluemke
 *
 */
public class AdaptationConfigurationMockup extends Configuration {

    public AdaptationConfigurationMockup(final File executionPlanURI, final String executionHostname,
            final int executionInputPort) {
        final ExecutionPlanSerializationMockup executionPlanSerializer = new ExecutionPlanSerializationMockup(
                executionPlanURI);
        final SingleConnectionTcpWriterStage executionPlanWriter = new SingleConnectionTcpWriterStage(executionHostname,
                executionInputPort);

        // Send execution plan to execution service
        this.connectPorts(executionPlanSerializer.getOutputPort(), executionPlanWriter.getInputPort());
    }

}
