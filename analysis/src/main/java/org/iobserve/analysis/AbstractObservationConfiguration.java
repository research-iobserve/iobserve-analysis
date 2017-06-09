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

import java.io.IOException;

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.AdaptationCalculation;
import org.iobserve.adaptation.AdaptationExecution;
import org.iobserve.adaptation.AdaptationPlanning;
import org.iobserve.adaptation.IAdaptationEventListener;
import org.iobserve.adaptation.SystemAdaptation;
import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.TAllocation;
import org.iobserve.analysis.filter.TDeployment;
import org.iobserve.analysis.filter.TEntryCall;
import org.iobserve.analysis.filter.TEntryCallSequence;
import org.iobserve.analysis.filter.TEntryEventSequence;
import org.iobserve.analysis.filter.TGeoLocation;
import org.iobserve.analysis.filter.TNetworkLink;
import org.iobserve.analysis.filter.TUndeployment;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.privacyanalysis.PrivacyAnalysis;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.evaluation.ModelComparer;
import org.iobserve.evaluation.SystemEvaluation;
import org.iobserve.planning.CandidateGeneration;
import org.iobserve.planning.CandidateProcessing;
import org.iobserve.planning.ModelOptimization;
import org.iobserve.planning.ModelProcessing;

import teetime.framework.Configuration;

/**
 * @author Reiner Jung
 *
 */
public abstract class AbstractObservationConfiguration extends Configuration {

	/**
	 * record switch filter. Is required to be global so we can cheat and get
	 * measurements from the filter.
	 */
	protected final RecordSwitch recordSwitch;

	protected final TDeployment deployment;

	protected final TUndeployment undeployment;

	/**
	 * Create a configuration with a ASCII file reader.
	 *
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
	 * @param eventListener
	 *            listener for operator interaction during adaptation phase
	 *
	 * @throws ClassNotFoundException
	 *             when a record type could not be loaded by class loader
	 * @throws IOException
	 *             for all file reading errors
	 */
	public AbstractObservationConfiguration(final ICorrespondence correspondenceModel, final UsageModelProvider usageModelProvider,
			final RepositoryModelProvider repositoryModelProvider, final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
			final AllocationModelProvider allocationModelProvider, final SystemModelProvider systemModelProvider,
			final SnapshotBuilder snapshotBuilder, final URI perOpteryxHeadless, final URI lqnsDir, final URI privacyAnalysisFile, final int varianceOfUserGroups,
			final int thinkTime, final boolean closedWorkload, final IAdaptationEventListener eventListener, final URI deployablesFolder) {
		/** configure filter. */
		this.recordSwitch = new RecordSwitch();

		final TAllocation tAllocation = new TAllocation(resourceEnvironmentModelProvider);
		this.deployment = new TDeployment(correspondenceModel, allocationModelProvider, systemModelProvider, resourceEnvironmentModelProvider);
		this.undeployment = new TUndeployment(correspondenceModel, allocationModelProvider, systemModelProvider, resourceEnvironmentModelProvider);
		final TEntryCall tEntryCall = new TEntryCall(correspondenceModel);
		final TEntryCallSequence tEntryCallSequence = new TEntryCallSequence();
		final TEntryEventSequence tEntryEventSequence = new TEntryEventSequence(correspondenceModel, usageModelProvider, repositoryModelProvider,
				varianceOfUserGroups, thinkTime, closedWorkload);
		
		final TNetworkLink tNetworkLink = new TNetworkLink(allocationModelProvider, systemModelProvider, resourceEnvironmentModelProvider);

		final TGeoLocation tGeoLocation = new TGeoLocation(resourceEnvironmentModelProvider);

		final PrivacyAnalysis privacyAnalysis = new org.iobserve.analysis.privacyanalysis.PrivacyAnalysis(
				new org.iobserve.analysis.privacyanalysis.GraphCreation(), new org.iobserve.analysis.privacyanalysis.GraphPrivacyAnalysis());

		final CandidateGeneration candidateGenerator = new CandidateGeneration(new ModelProcessing(perOpteryxHeadless, lqnsDir, privacyAnalysisFile),
				new ModelOptimization(), new CandidateProcessing());

		final SystemAdaptation systemAdaptor = new SystemAdaptation(new AdaptationCalculation(), new AdaptationPlanning(),
				new AdaptationExecution(eventListener, deployablesFolder));
		final SystemEvaluation systemEvaluator = new SystemEvaluation(new ModelComparer());

		/** dispatch different monitoring data. */
		// Path Allocation => Deployment => Snapshot
		this.connectPorts(this.recordSwitch.getDeploymentOutputPort(), tAllocation.getInputPort());
		this.connectPorts(tAllocation.getDeploymentOutputPort(), this.deployment.getInputPort());
		this.connectPorts(this.deployment.getOutputPortSnapshot(), snapshotBuilder.getInputPort());

		this.connectPorts(this.recordSwitch.getUndeploymentOutputPort(), this.undeployment.getInputPort());
		this.connectPorts(this.undeployment.getOutputPortSnapshot(), snapshotBuilder.getInputPort());

		this.connectPorts(this.recordSwitch.getFlowOutputPort(), tEntryCall.getInputPort());
		this.connectPorts(tEntryCall.getOutputPort(), tEntryCallSequence.getInputPort());
		this.connectPorts(tEntryCallSequence.getOutputPort(), tEntryEventSequence.getInputPort());
		this.connectPorts(tEntryEventSequence.getOutputPortSnapshot(), snapshotBuilder.getInputPort());

		this.connectPorts(this.recordSwitch.getTraceMetaPort(), tNetworkLink.getInputPort());
		this.connectPorts(tNetworkLink.getOutputPortSnapshot(), snapshotBuilder.getInputPort());

		// Path GeoLocation => Snapshot
		this.connectPorts(this.recordSwitch.getGeoLocationPort(), tGeoLocation.getInputPort());
		this.connectPorts(tGeoLocation.getOutputPortSnapshot(), snapshotBuilder.getInputPort());

		// Path Snapshot => Privacy Analysis
		this.connectPorts(snapshotBuilder.getOutputPort(), privacyAnalysis.getInputPort());
		this.connectPorts(snapshotBuilder.getEvaluationOutputPort(), systemEvaluator.getInputPort());

		// Path Privacy Analysis => Planning
		this.connectPorts(privacyAnalysis.getOutputPort(), candidateGenerator.getInputPort());

		// Path Planning => Adaptation
		this.connectPorts(candidateGenerator.getOutputPort(), systemAdaptor.getInputPort());
	}

	public RecordSwitch getRecordSwitch() {
		return this.recordSwitch;
	}

}
