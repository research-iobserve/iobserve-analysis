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
package org.iobserve.generic.analysis.cli;

import java.io.File;
import java.util.Collection;

import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.TEntryCall;
import org.iobserve.analysis.filter.reader.Dir2RecordsFilter;
import org.iobserve.analysis.filter.writer.DataDumpStage;

import teetime.framework.Configuration;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;

/**
 * @author Reiner Jung
 *
 */
public class ObservationConfiguration extends Configuration {

    /**
     * record switch filter. Is required to be global so we can cheat and get measurements from the
     * filter.
     */
    protected final RecordSwitch recordSwitch;

    private final InitialElementProducer<File> files;
    private final Dir2RecordsFilter reader;

    /**
     * Create a configuration with a ASCII file reader.
     *
     * @param directories
     * @param dataLocation
     * @param correspondenceModel
     */
    public ObservationConfiguration(final Collection<File> directories, final File dataLocation) {

        /** configure filter. */
        this.files = new InitialElementProducer<>(directories);
        this.reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());
        this.recordSwitch = new RecordSwitch();

        final TEntryCall tEntryCall = new TEntryCall();

        final DataDumpStage dumpStage = new DataDumpStage(dataLocation.getAbsolutePath());

        /** connections. */
        this.connectPorts(this.files.getOutputPort(), this.reader.getInputPort());
        this.connectPorts(this.reader.getOutputPort(), this.recordSwitch.getInputPort());
        this.connectPorts(this.recordSwitch.getFlowOutputPort(), tEntryCall.getInputPort());
        this.connectPorts(tEntryCall.getOutputPort(), dumpStage.getInputPort());
    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }

}
