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

import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.TAllocation;
import org.iobserve.analysis.filter.TDeployment;
import org.iobserve.analysis.filter.TEntryCall;
import org.iobserve.analysis.filter.TEntryCallSequence;
import org.iobserve.analysis.filter.TEntryEventSequence;
import org.iobserve.analysis.filter.TNetworkLink;
import org.iobserve.analysis.filter.TUndeployment;

import teetime.framework.AnalysisConfiguration;
import teetime.framework.Stage;
import teetime.framework.pipe.IPipeFactory;
import teetime.framework.pipe.PipeFactoryRegistry;
import teetime.framework.pipe.PipeFactoryRegistry.PipeOrdering;
import teetime.framework.pipe.PipeFactoryRegistry.ThreadCommunication;
import teetime.stage.InitialElementProducer;
import teetime.stage.className.ClassNameRegistryRepository;
import teetime.stage.io.filesystem.Dir2RecordsFilter;

/**
 * Configuration of teetime framework.
 * @author Reiner Jung
 * @author Alessandro Giusa
 */
public final class ObservationConfiguration extends AnalysisConfiguration {

	/** registry for the pipe factory used by Teetime. */
	private final PipeFactoryRegistry pipeFactoryRegistry = PipeFactoryRegistry.INSTANCE;

	/** directory containing Kieker monitoring data. */
	private final File directory;

	// TODO fix that hack
	/** record switch filter. Is required to be global so we can 
	 * cheat and get measurements from the filter. */
	private RecordSwitch recordSwitch;

	/**
	 * Create a configuration with a ASCII file reader.
	 *
	 * @param theDirectory
	 *            directory containing kieker data
	 *
	 * @throws ClassNotFoundException
	 *             when a record type could not be loaded by class loader
	 * @throws IOException
	 *             for all file reading errors
	 */
	public ObservationConfiguration(final File theDirectory) throws IOException, ClassNotFoundException {
		this.directory = theDirectory;
		this.addThreadableStage(this.buildAnalysis());
	}

	/**
	 * Build the pipeline.
	 * @return stage
	 */
	private Stage buildAnalysis() {
		// create filter
		final InitialElementProducer<File> files = new InitialElementProducer<File>(this.directory);
		final Dir2RecordsFilter reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

		this.recordSwitch = new RecordSwitch();

		final TAllocation tAllocation = new TAllocation();
		final TDeployment tDeployment = new TDeployment();
		final TUndeployment tUndeployment = new TUndeployment();
		final TEntryCall tEntryCall = new TEntryCall();
		final TEntryCallSequence tEntryCallSequence = new TEntryCallSequence();
		final TEntryEventSequence tEntryEventSequence = new TEntryEventSequence();
		final TNetworkLink tNetworkLink = new TNetworkLink();

		// connecting filters
		final IPipeFactory factory = this.pipeFactoryRegistry.getPipeFactory(
				ThreadCommunication.INTRA, PipeOrdering.ARBITRARY, false);
		factory.create(files.getOutputPort(), reader.getInputPort());
		factory.create(reader.getOutputPort(), this.recordSwitch.getInputPort());
		factory.create(this.recordSwitch.getDeploymentOutputPort(), tAllocation.getInputPort());
		factory.create(this.recordSwitch.getUndeploymentOutputPort(), tUndeployment.getInputPort());
		factory.create(this.recordSwitch.getFlowOutputPort(), tEntryCall.getInputPort());
		factory.create(this.recordSwitch.getTraceMetaPort(), tNetworkLink.getInputPort());
		factory.create(tAllocation.getDeploymentOutputPort(), tDeployment.getInputPort());
		factory.create(tEntryCall.getOutputPort(), tEntryCallSequence.getInputPort());
		factory.create(tEntryCallSequence.getOutputPort(), tEntryEventSequence.getInputPort());
		return files;
	}
	
	/**
	 * @return {@link RecordSwitch}
	 */
	public RecordSwitch getRecordSwitch() {
		return this.recordSwitch;
	}
}
