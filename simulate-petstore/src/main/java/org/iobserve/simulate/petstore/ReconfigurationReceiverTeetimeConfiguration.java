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
package org.iobserve.simulate.petstore;

import teetime.framework.Configuration;

import org.iobserve.stages.sink.NullStage;
import org.iobserve.stages.source.MultipleConnectionTcpReaderStage;
import org.iobserve.stages.source.NoneTraceMetadataRewriter;

/**
 * @author Reiner Jung
 *
 */
public class ReconfigurationReceiverTeetimeConfiguration extends Configuration {

    /**
     * Configuration for the TCP receiver.
     *
     * @param configuration
     *            simulator configuration.
     */
    public ReconfigurationReceiverTeetimeConfiguration(final SimulatePetStoreConfiguration configuration) {
        final MultipleConnectionTcpReaderStage reader = new MultipleConnectionTcpReaderStage(configuration.getPort(),
                4096, new NoneTraceMetadataRewriter());
        final NullStage sink = new NullStage(false);

        this.connectPorts(reader.getOutputPort(), sink.getInputPort());
    }
}
