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
package org.iobserve.analysis.source;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.OutputPort;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;

import org.iobserve.analysis.AbstractConfigurableCompositeStage;
import org.iobserve.analysis.ConfigurationException;
import org.iobserve.analysis.configurations.AnalysisConfiguration;
import org.iobserve.stages.source.Dir2RecordsFilter;

/**
 * @author Reiner Jung
 *
 */
public class FileSourceCompositeStage extends AbstractConfigurableCompositeStage {

    public static final String SOURCE_DIRECTORIES = FileSourceCompositeStage.class.getCanonicalName()
            + ".source.directories";

    private static final Log LOG = LogFactory.getLog(AnalysisConfiguration.class);

    private static final String DELIMETER = ";";

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
        super(configuration);

        final Collection<File> directories = new ArrayList<>();

        final String[] directoryNames = configuration.getStringArrayProperty(
                FileSourceCompositeStage.SOURCE_DIRECTORIES, FileSourceCompositeStage.DELIMETER);
        for (final String directoryName : directoryNames) {
            final File directory = new File(directoryName);
            if (directory.isDirectory() && directory.canRead()) {
                directories.add(directory);
            }
        }

        if (directories.size() == 0) {
            FileSourceCompositeStage.LOG.error("No valid directory found.");
            throw new ConfigurationException("No valid directory found.");
        }

        final InitialElementProducer<File> files = new InitialElementProducer<>(directories);
        this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        /** connecting filters */
        this.connectPorts(files.getOutputPort(), this.reader.getInputPort());
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.reader.getOutputPort();
    }
}
