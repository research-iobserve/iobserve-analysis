/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.collector;

import teetime.framework.Configuration;

import org.iobserve.stages.sink.DataDumpStage;
import org.iobserve.stages.source.MultipleConnectionTcpReaderStage;

/**
 * Analysis configuration for the data collector.
 *
 * @author Reiner Jung
 *
 */
public class SimpleBridgeConfiguration extends Configuration {

    private final DataDumpStage consumer;

    /**
     * Configure analysis.
     *
     * @param dataLocation
     *            data location
     * @param inputPort
     *            input port
     */
    public SimpleBridgeConfiguration(final String dataLocation, final int inputPort) {
        final MultipleConnectionTcpReaderStage reader = new MultipleConnectionTcpReaderStage(inputPort, 1024);

        this.consumer = new DataDumpStage(dataLocation);

        this.connectPorts(reader.getOutputPort(), this.consumer.getInputPort());
    }

    public DataDumpStage getCounter() {
        return this.consumer;
    }
}
