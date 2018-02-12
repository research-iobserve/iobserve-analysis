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
package org.iobserve.analysis.test.cli;

import java.io.File;
import java.util.Collection;

import teetime.framework.Configuration;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;

import org.iobserve.stages.sink.DataDumpStage;
import org.iobserve.stages.sink.ESerializationType;
import org.iobserve.stages.source.Dir2RecordsFilter;

/**
 * @author Reiner Jung
 *
 */
public class ObservationConfiguration extends Configuration {

    private static final String TEST_HOSTNAME = "test-host";

    /**
     * Create a configuration with a ASCII file reader.
     *
     * @param directories
     *            input logs
     * @param dataLocation
     *            output directory
     */
    public ObservationConfiguration(final Collection<File> directories, final File dataLocation) {

        /** configure filter. */
        final InitialElementProducer<File> files = new InitialElementProducer<>(directories);
        final Dir2RecordsFilter reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());
        final RecordTypeFilter filter = new RecordTypeFilter();

        final DataDumpStage dumpStage = new DataDumpStage(dataLocation.getAbsolutePath(),
                ObservationConfiguration.TEST_HOSTNAME, ESerializationType.ASCII);

        /** connections. */
        this.connectPorts(files.getOutputPort(), reader.getInputPort());
        this.connectPorts(reader.getOutputPort(), filter.getInputPort());
        this.connectPorts(filter.getOutputPort(), dumpStage.getInputPort());
    }

}
