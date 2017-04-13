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

import org.iobserve.analysis.filter.reader.Dir2RecordsFilter;

import teetime.framework.Configuration;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;

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
    private final DataDumpStage registryConsumer;
    private final DataDumpStage webFrontendConsumer;
    private final Splitter splitter;
    private final Filter filter;

    /**
     * Configure analysis.
     *
     * @param dataLocation
     *            data location
     * @param inputPort
     *            input port
     */
    public SimpleSplitterConfiguration(final String dataLocation, final String outputLocation, final String adapter,
            final String enterprise, final String store, final String registry, final String webfrontend) {

        System.out.println("Read from " + dataLocation);

        final Collection<File> directories = new ArrayList<>();

        final File directory = new File(dataLocation);

        if (directory.isDirectory()) {
            for (final File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    directories.add(file);
                }
            }

        } else {
            System.out.println(dataLocation + " is not a directory containing Kieker directories.");
        }

        this.files = new InitialElementProducer<>(directories);
        this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        this.filter = new Filter();

        this.splitter = new Splitter(adapter, enterprise, store, registry, webfrontend);

        this.adapterConsumer = new DataDumpStage(outputLocation, adapter);
        this.enterpriseConsumer = new DataDumpStage(outputLocation, enterprise);
        this.storeConsumer = new DataDumpStage(outputLocation, store);
        this.registryConsumer = new DataDumpStage(outputLocation, registry);
        this.webFrontendConsumer = new DataDumpStage(outputLocation, webfrontend);

        this.connectPorts(this.files.getOutputPort(), this.reader.getInputPort());
        this.connectPorts(this.reader.getOutputPort(), this.filter.getInputPort());
        this.connectPorts(this.filter.getOutputPort(), this.splitter.getInputPort());
        this.connectPorts(this.splitter.getAdapterOutputPort(), this.adapterConsumer.getInputPort());
        this.connectPorts(this.splitter.getEnterpriseOutputPort(), this.enterpriseConsumer.getInputPort());
        this.connectPorts(this.splitter.getStoreOutputPort(), this.storeConsumer.getInputPort());
        this.connectPorts(this.splitter.getRegistryOutputPort(), this.registryConsumer.getInputPort());
        this.connectPorts(this.splitter.getWebFrontendOutputPort(), this.webFrontendConsumer.getInputPort());

    }

}
