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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.iobserve.analysis.filter.reader.Dir2RecordsFilter;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
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
    private final DataDumpStage[] consumer;
    private final Splitter splitter;
    private final Filter filter;
    private static final Log LOG = LogFactory.getLog(SimpleSplitterConfiguration.class);

    /**
     * Configure analysis.
     *
     * @param dataLocation
     *            location of a Kieker data log
     * @param outputLocation
     *            output location
     * @param hostnames
     *            names of the different nodes
     * @throws IOException
     *             on error initializing dump stage
     */
    public SimpleSplitterConfiguration(final File dataLocation, final File outputLocation, final String[] hostnames)
            throws IOException {

        SimpleSplitterConfiguration.LOG.debug("Read from " + dataLocation);

        final Collection<File> directories = new ArrayList<>();

        directories.add(dataLocation);

        this.files = new InitialElementProducer<>(directories);
        this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        this.filter = new Filter();

        this.splitter = new Splitter(hostnames);

        this.consumer = new DataDumpStage[hostnames.length];

        for (int i = 0; i < hostnames.length; i++) {
            this.consumer[i] = new DataDumpStage(outputLocation.getCanonicalPath(), hostnames[i]);
            this.connectPorts(this.splitter.getAllOutputPorts().get(i), this.consumer[i].getInputPort());
        }
        this.connectPorts(this.files.getOutputPort(), this.reader.getInputPort());
        this.connectPorts(this.reader.getOutputPort(), this.filter.getInputPort());
        this.connectPorts(this.filter.getOutputPort(), this.splitter.getInputPort());
    }

}
