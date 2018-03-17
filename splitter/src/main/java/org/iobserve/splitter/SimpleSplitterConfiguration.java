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

import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.writer.filesystem.FileWriter;

import teetime.framework.Configuration;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;

import org.iobserve.stages.sink.DataDumpStage;
import org.iobserve.stages.source.Dir2RecordsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Analysis configuration for the data collector.
 *
 * @author Reiner Jung
 *
 */
public class SimpleSplitterConfiguration extends Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleSplitterConfiguration.class);

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

        SimpleSplitterConfiguration.LOGGER.debug("Read from {}", dataLocation);

        final Collection<File> directories = new ArrayList<>();

        directories.add(dataLocation);

        final InitialElementProducer<File> files = new InitialElementProducer<>(directories);
        final Dir2RecordsFilter reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        final StripIObserveSpecificEventsFilter removeIObserveEventsFilter = new StripIObserveSpecificEventsFilter();

        final Splitter splitter = new Splitter(hostnames);

        final DataDumpStage[] consumer = new DataDumpStage[hostnames.length];

        for (int i = 0; i < hostnames.length; i++) {
            consumer[i] = new DataDumpStage(this.createConfiguration(outputLocation.getCanonicalPath(), hostnames[i]));
            this.connectPorts(splitter.getAllOutputPorts().get(i), consumer[i].getInputPort());
        }

        this.connectPorts(files.getOutputPort(), reader.getInputPort());
        this.connectPorts(reader.getOutputPort(), removeIObserveEventsFilter.getInputPort());
        this.connectPorts(removeIObserveEventsFilter.getOutputPort(), splitter.getInputPort());
    }

    private kieker.common.configuration.Configuration createConfiguration(final String canonicalPath,
            final String hostname) {
        final kieker.common.configuration.Configuration configuration = new kieker.common.configuration.Configuration();

        configuration.setProperty(ConfigurationKeys.WRITER_CLASSNAME, FileWriter.class.getName());
        configuration.setProperty(FileWriter.CONFIG_PATH, canonicalPath);
        configuration.setProperty(ConfigurationKeys.HOST_NAME, hostname);

        return configuration;
    }

}
