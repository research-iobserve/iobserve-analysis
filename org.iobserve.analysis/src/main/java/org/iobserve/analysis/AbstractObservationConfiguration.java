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

import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.TAllocation;
import org.iobserve.analysis.filter.TDeployment;
import org.iobserve.analysis.filter.TEntryCall;
import org.iobserve.analysis.filter.TEntryCallSequence;
import org.iobserve.analysis.filter.TEntryEventSequence;
import org.iobserve.analysis.filter.TNetworkLink;
import org.iobserve.analysis.filter.TUndeployment;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.ModelProviderPlatform;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;

import teetime.framework.Configuration;

/**
 * @author Reiner Jung
 *
 */
public abstract class AbstractObservationConfiguration extends Configuration {
	
	/** record switch filter. Is required to be global so we can cheat and get measurements from the filter. */
	protected RecordSwitch recordSwitch;


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
	public AbstractObservationConfiguration(ModelProviderPlatform platform) {
		final ICorrespondence correspondenceModel = platform.getCorrespondenceModel();
		final UsageModelProvider usageModelProvider = platform.getUsageModelProvider();
		final ResourceEnvironmentModelProvider resourceEvnironmentModelProvider =
				platform.getResourceEnvironmentModelProvider();
		final AllocationModelProvider allocationModelProvider =	platform.getAllocationModelProvider();
		final SystemModelProvider systemModelProvider = platform.getSystemModelProvider();
		
		/** configure filter. */
		recordSwitch = new RecordSwitch();
		
		final TAllocation tAllocation = new TAllocation(correspondenceModel, resourceEvnironmentModelProvider);
		final TDeployment tDeployment = new TDeployment(correspondenceModel, allocationModelProvider,
				systemModelProvider, resourceEvnironmentModelProvider);
		final TUndeployment tUndeployment = new TUndeployment();
		final TEntryCall tEntryCall = new TEntryCall();
		final TEntryCallSequence tEntryCallSequence = new TEntryCallSequence();
		final TEntryEventSequence tEntryEventSequence = new TEntryEventSequence(correspondenceModel, usageModelProvider);
		final TNetworkLink tNetworkLink = new TNetworkLink();

		/** dispatch different monitoring data */
		connectPorts(this.recordSwitch.getDeploymentOutputPort(), tAllocation.getInputPort());
		connectPorts(this.recordSwitch.getUndeploymentOutputPort(), tUndeployment.getInputPort());
		connectPorts(this.recordSwitch.getFlowOutputPort(), tEntryCall.getInputPort());
		connectPorts(this.recordSwitch.getTraceMetaPort(), tNetworkLink.getInputPort());

		// 
		connectPorts(tAllocation.getDeploymentOutputPort(), tDeployment.getInputPort());
		connectPorts(tEntryCall.getOutputPort(), tEntryCallSequence.getInputPort());
		connectPorts(tEntryCallSequence.getOutputPort(), tEntryEventSequence.getInputPort());
	}
	
	public RecordSwitch getRecordSwitch() {
		return this.recordSwitch;
	}
	
}
