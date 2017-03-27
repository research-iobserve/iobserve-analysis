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
package org.iobserve.splitter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import teetime.framework.Configuration;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;
import teetime.stage.io.filesystem.Dir2RecordsFilter;

/**
 * Analysis configuration for the data collector.
 *
 * @author Reiner Jung
 *
 */
public class SimpleSplitterConfiguration extends Configuration {

    private final InitialElementProducer<File> files;
    private final Dir2RecordsFilter reader;
    private final DataDumpStage adapterConsumer;
    private final DataDumpStage enterpriseConsumer;
    private final DataDumpStage storeConsumer;
    private final DataDumpStage webFrontendConsumer;
    private final Splitter splitter;

    /**
     * Configure analysis.
     *
     * @param dataLocation
     *            data location
     * @param inputPort
     *            input port
     */
    public SimpleSplitterConfiguration(final String dataLocation, final int inputPort, final String adapter,
            final String enterprise, final String store, final String webfrontend) {

        final Collection<File> directories = new ArrayList<>();

        this.files = new InitialElementProducer<>(directories);
        this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        this.splitter = new Splitter(adapter, enterprise, store, webfrontend);

        this.adapterConsumer = new DataDumpStage(dataLocation, adapter);
        this.enterpriseConsumer = new DataDumpStage(dataLocation, enterprise);
        this.storeConsumer = new DataDumpStage(dataLocation, store);
        this.webFrontendConsumer = new DataDumpStage(dataLocation, webfrontend);

        this.connectPorts(this.files.getOutputPort(), this.reader.getInputPort());
        this.connectPorts(this.reader.getOutputPort(), this.splitter.getInputPort());
        this.connectPorts(this.splitter.getAdapterOutputPort(), this.adapterConsumer.getInputPort());
        this.connectPorts(this.splitter.getEnterpriseOutputPort(), this.enterpriseConsumer.getInputPort());
        this.connectPorts(this.splitter.getStoreOutputPort(), this.storeConsumer.getInputPort());
        this.connectPorts(this.splitter.getWebFrontendOutputPort(), this.webFrontendConsumer.getInputPort());

    }

}
