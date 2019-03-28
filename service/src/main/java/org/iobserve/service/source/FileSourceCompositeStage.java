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
package org.iobserve.service.source;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import kieker.analysis.common.ConfigurationException;
import kieker.analysisteetime.plugin.reader.filesystem.className.ClassNameRegistryRepository;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.CompositeStage;
import teetime.framework.OutputPort;
import teetime.stage.InitialElementProducer;

import org.iobserve.stages.source.Dir2RecordsFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Composite stage designed to read one or more Kieker log directories. The stage provides one
 * output port.
 *
 * TODO: Please note the filter uses deprecated stages. There is a better setup available in Kieker
 * 1.15
 *
 * @author Reiner Jung
 *
 */
public class FileSourceCompositeStage extends CompositeStage implements ISourceCompositeStage {

    public static final String SOURCE_DIRECTORIES = FileSourceCompositeStage.class.getCanonicalName()
            + ".sourceDirectories";

    private static final Logger LOGGER = LoggerFactory.getLogger(FileSourceCompositeStage.class);

    private static final String DELIMETER = File.pathSeparator;

    private final Dir2RecordsFilter reader;

    /**
     * Create an file source composite stage.
     *
     * @param configuration
     *            configuration object.
     * @throws ConfigurationException
     *             when no valid directory was specified
     */
    public FileSourceCompositeStage(final Configuration configuration) throws ConfigurationException {
        final String[] directoryNames = configuration.getStringArrayProperty(
                FileSourceCompositeStage.SOURCE_DIRECTORIES, FileSourceCompositeStage.DELIMETER);

        final Collection<File> directories = this.collectDirectories(directoryNames);

        if (directories.isEmpty()) {
            FileSourceCompositeStage.LOGGER.error("No valid directory found.");
            throw new ConfigurationException("No valid directory found.");
        }

        final InitialElementProducer<File> files = new InitialElementProducer<>(directories);
        this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        /** connecting filters */
        this.connectPorts(files.getOutputPort(), this.reader.getInputPort());
    }

    /**
     * Collect input directories and check if they do exist.
     *
     * @param pathStrings
     *            array of string containing potential paths for directories
     * @return a collection of directory handles which point to existing directories
     */
    private Collection<File> collectDirectories(final String[] pathStrings) {
        final Collection<File> directories = new ArrayList<>();

        for (final String directoryName : pathStrings) {
            final File directory = new File(directoryName);
            if (directory.isDirectory() && directory.canRead()) {
                directories.add(directory);
            }
        }
        return directories;
    }

    @Override
    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.reader.getOutputPort();
    }
}
