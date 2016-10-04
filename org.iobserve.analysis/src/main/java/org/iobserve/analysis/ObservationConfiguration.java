/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
import java.io.IOException;

import teetime.framework.Configuration;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;
import teetime.stage.io.filesystem.Dir2RecordsFilter;

import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.TAllocation;
import org.iobserve.analysis.filter.TDeployment;
import org.iobserve.analysis.filter.TEntryCall;
import org.iobserve.analysis.filter.TEntryCallSequence;
import org.iobserve.analysis.filter.TEntryEventSequence;
import org.iobserve.analysis.filter.TNetworkLink;
import org.iobserve.analysis.filter.TUndeployment;

// TODO moved and split

/**
 * @author Reiner Jung
 *
 */
public class ObservationConfiguration extends Configuration {

    /** directory containing Kieker monitoring data. */
    private final File directory;

    // TODO fix that hack
    /**
     * record switch filter. Is required to be global so we can cheat and get measurements from the
     * filter.
     */
    private final RecordSwitch recordSwitch;

    /**
     * Create a configuration with a ASCII file reader.
     *
     * @param directory
     *            directory containing kieker data
     *
     * @throws ClassNotFoundException
     *             when a record type could not be loaded by class loader
     * @throws IOException
     *             for all file reading errors
     */
    public ObservationConfiguration(final File directory) throws IOException, ClassNotFoundException {
        this.directory = directory;

        // create filter
        final InitialElementProducer<File> files = new InitialElementProducer<>(this.directory);
        final Dir2RecordsFilter reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

        this.recordSwitch = new RecordSwitch();

        final TAllocation tAllocation = new TAllocation();
        final TDeployment tDeployment = new TDeployment();
        final TUndeployment tUndeployment = new TUndeployment();
        final TEntryCall tEntryCall = new TEntryCall();
        final TEntryCallSequence tEntryCallSequence = new TEntryCallSequence();
        final TEntryEventSequence tEntryEventSequence = new TEntryEventSequence();
        final TNetworkLink tNetworkLink = new TNetworkLink();

        this.connectPorts(files.getOutputPort(), reader.getInputPort());
        this.connectPorts(reader.getOutputPort(), this.recordSwitch.getInputPort());

        // dispatch different monitoring data
        this.connectPorts(this.recordSwitch.getDeploymentOutputPort(), tAllocation.getInputPort());
        this.connectPorts(this.recordSwitch.getUndeploymentOutputPort(), tUndeployment.getInputPort());
        this.connectPorts(this.recordSwitch.getFlowOutputPort(), tEntryCall.getInputPort());
        this.connectPorts(this.recordSwitch.getTraceMetaPort(), tNetworkLink.getInputPort());

        //
        this.connectPorts(tAllocation.getDeploymentOutputPort(), tDeployment.getInputPort());
        this.connectPorts(tEntryCall.getOutputPort(), tEntryCallSequence.getInputPort());
        this.connectPorts(tEntryCallSequence.getOutputPort(), tEntryEventSequence.getInputPort());

    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }
}
