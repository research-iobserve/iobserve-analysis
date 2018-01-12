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
package org.iobserve.replayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import teetime.framework.Configuration;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;

import org.iobserve.stages.source.Dir2RecordsFilter;

/**
 * Configuration for the log replayer.
 *
 * @author Reiner Jung
 *
 */
public class ReplayerConfiguration extends Configuration {

    private final DataSendStage consumer;

    /**
     * Construct the replayer configuration.
     *
     * @param dataLocation
     *            directory containing Kieker data.
     * @param hostname
     *            name of the host where the data is send to
     * @param outputPort
     *            port on the host the data is send to
     */
    public ReplayerConfiguration(final File dataLocation, final String hostname, final int outputPort) {

        final List<File> directories = new ArrayList<File>();
        directories.add(dataLocation);

        final InitialElementProducer<File> files = new InitialElementProducer<>(directories);
        final Dir2RecordsFilter reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        this.consumer = new DataSendStage(hostname, outputPort);

        this.connectPorts(files.getOutputPort(), reader.getInputPort());
        this.connectPorts(reader.getOutputPort(), this.consumer.getInputPort());
    }

    public DataSendStage getCounter() {
        return this.consumer;
    }

    public boolean isOutputConnected() {
        return this.consumer.isOutputConnected();
    }

}
