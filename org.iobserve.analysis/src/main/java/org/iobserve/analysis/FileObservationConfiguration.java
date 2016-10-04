/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
package org.iobserve.analysis;

import java.io.File;
import java.util.Collection;

import org.iobserve.analysis.model.ModelProviderPlatform;

import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;
import teetime.stage.io.filesystem.Dir2RecordsFilter;

/**
 *
 * @author Reiner Jung
 *
 */
public class FileObservationConfiguration extends AbstractObservationConfiguration {

    private final InitialElementProducer<File> files;
    private final Dir2RecordsFilter reader;

    /**
     * Analysis configuration constructor.
     *
     * @param directories
     *            a collection of directories containing kieker logs
     * @param platform
     *            the model provider platform
     */
    public FileObservationConfiguration(final Collection<File> directories, final ModelProviderPlatform platform,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload) {
        super(platform, varianceOfUserGroups, thinkTime, closedWorkload);

        this.files = new InitialElementProducer<>(directories);
        this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        /** connecting filters */
        this.connectPorts(this.files.getOutputPort(), this.reader.getInputPort());
        this.connectPorts(this.reader.getOutputPort(), this.recordSwitch.getInputPort());
    }

}
