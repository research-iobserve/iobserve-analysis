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
package org.iobserve.analysis;

import org.eclipse.emf.common.util.URI;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.reader.MultipleConnectionTcpReaderStage;
import org.iobserve.analysis.snapshot.SnapshotBuilder;

/**
 * Configuration prepared to handle multiple TCP input streams.
 *
 * @author Reiner Jung
 *
 */
public class MultiInputObservationConfiguration extends AbstractObservationConfiguration {

	private static final int CAPACITY = 1024 * 1024;

	/**
	 * Construct an analysis for multiple TCP inputs.
	 *
	 * @param inputPort
	 *            the input port where the analysis is listening
	 * @param correspondenceModel
	 *            the correspondence model
	 * @param usageModelProvider
	 *            the usage model provider
	 * @param repositoryModelProvider
	 *            the repository model provider
	 * @param resourceEnvironmentModelProvider
	 *            the resource environment provider
	 * @param allocationModelProvider
	 *            the allocation model provider
	 * @param systemModelProvider
	 *            the system model provider
	 * @param varianceOfUserGroups
	 *            variance of user groups, configuration for entry event filter
	 * @param thinkTime
	 *            think time, configuration for entry event filter
	 * @param closedWorkload
	 *            kind of workload, configuration for entry event filter
	 */
	public MultiInputObservationConfiguration(final int inputPort, final ICorrespondence correspondenceModel,
			final UsageModelProvider usageModelProvider, final RepositoryModelProvider repositoryModelProvider,
			final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider, final AllocationModelProvider allocationModelProvider,
			final SystemModelProvider systemModelProvider, final SnapshotBuilder snapshotBuilder, final URI perOpteryxHeadless, final URI lqnsDir,
			final URI privacyAnalysisFile, final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
			final URI deployablesFolder) {
		super(correspondenceModel, usageModelProvider, repositoryModelProvider, resourceEnvironmentModelProvider, allocationModelProvider,
				systemModelProvider, snapshotBuilder, perOpteryxHeadless, lqnsDir, privacyAnalysisFile, varianceOfUserGroups, thinkTime,
				closedWorkload, null, deployablesFolder);

		final MultipleConnectionTcpReaderStage reader = new MultipleConnectionTcpReaderStage(inputPort, MultiInputObservationConfiguration.CAPACITY);
		this.connectPorts(reader.getOutputPort(), this.recordSwitch.getInputPort());
	}

}
