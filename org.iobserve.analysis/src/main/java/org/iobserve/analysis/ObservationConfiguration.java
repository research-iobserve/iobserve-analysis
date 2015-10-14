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
import java.util.LinkedList;
import java.util.List;

import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.filter.DeploymentEventTransformation;
import org.iobserve.analysis.filter.EntryEventConstructionFilter;
import org.iobserve.analysis.filter.EntryEventSequenceAnalyzer;
import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.UndeploymentEventTransformation;

import kieker.common.record.IMonitoringRecord;

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
 * @author Reiner Jung
 *
 */
public class ObservationConfiguration extends AnalysisConfiguration {

	private final List<IMonitoringRecord> elementCollection = new LinkedList<IMonitoringRecord>();
	private final PipeFactoryRegistry pipeFactoryRegistry = PipeFactoryRegistry.INSTANCE;
	private final File directory;
	private RecordSwitch recordSwitch;

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
		this.addThreadableStage(this.buildAnalysis());
	}

	private Stage buildAnalysis() {
		final ICorrespondence correspondence = null;

		// create filter
		final InitialElementProducer<File> files = new InitialElementProducer<File>(this.directory);
		final Dir2RecordsFilter reader = new Dir2RecordsFilter(new ClassNameRegistryRepository());

		this.recordSwitch = new RecordSwitch();

		final DeploymentEventTransformation deployment = new DeploymentEventTransformation(
				correspondence);
		final UndeploymentEventTransformation undeployment = new UndeploymentEventTransformation(
				correspondence);
		final EntryEventConstructionFilter entryEventConstructionFilter = new EntryEventConstructionFilter();
		final EntryEventSequenceAnalyzer entryEventSequenceAnalyzer = new EntryEventSequenceAnalyzer(5 * 60 * 1000 * 1000);

		// connecting filters
		final IPipeFactory factory = this.pipeFactoryRegistry.getPipeFactory(
				ThreadCommunication.INTRA, PipeOrdering.ARBITRARY, false);

		factory.create(files.getOutputPort(), reader.getInputPort());
		factory.create(reader.getOutputPort(), this.recordSwitch.getInputPort());
		factory.create(this.recordSwitch.getDeploymentOutputPort(), deployment.getInputPort());
		factory.create(this.recordSwitch.getUndeploymentOutputPort(), undeployment.getInputPort());
		factory.create(this.recordSwitch.getFlowOutputPort(), entryEventConstructionFilter.getInputPort());
		factory.create(entryEventConstructionFilter.getOutputPort(),
				entryEventSequenceAnalyzer.getInputPort());

		return files;

	}

	public List<IMonitoringRecord> getElementCollection() {
		return this.elementCollection;
	}

	public RecordSwitch getRecordSwitch() {
		return this.recordSwitch;
	}
}
