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

import teetime.framework.Configuration;

import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.clustering.IVectorQuantizationClustering;
import org.iobserve.analysis.clustering.XMeansClustering;
import org.iobserve.analysis.clustering.filter.composite.TBehaviorModelComparison;
import org.iobserve.analysis.clustering.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.clustering.filter.models.configuration.EntryCallFilterRules;
import org.iobserve.analysis.clustering.filter.models.configuration.GetLastXSignatureStrategy;
import org.iobserve.analysis.clustering.filter.models.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.clustering.filter.models.configuration.examples.CoCoMEEntryCallRulesFactory;
import org.iobserve.analysis.clustering.filter.models.configuration.examples.JPetStoreEntryCallRulesFactory;
import org.iobserve.analysis.clustering.filter.models.configuration.examples.JPetstoreStrategy;
import org.iobserve.analysis.filter.CollectUserSessionsFilter;
import org.iobserve.analysis.filter.IEntryCallTraceMatcher;
import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.TAllocation;
import org.iobserve.analysis.filter.TAllocationFinished;
import org.iobserve.analysis.filter.TDeployment;
import org.iobserve.analysis.filter.TEntryCall;
import org.iobserve.analysis.filter.TEntryCallSequence;
import org.iobserve.analysis.filter.TUndeployment;
import org.iobserve.analysis.filter.TimeTriggerFilter;
import org.iobserve.analysis.filter.TraceAcceptanceFilter;
import org.iobserve.analysis.filter.TraceOperationCleanupFilter;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;

import org.iobserve.analysis.systems.jpetstore.JPetStoreCallTraceMatcher;
import org.iobserve.analysis.systems.jpetstore.JPetStoreTraceAcceptanceMatcher;
import org.iobserve.analysis.systems.jpetstore.JPetStoreTraceSignatureCleanupRewriter;

import teetime.framework.Configuration;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;
import teetime.stage.trace.traceReconstruction.EventBasedTraceFactory;
import teetime.stage.trace.traceReconstruction.TraceReconstructionFilter;
import teetime.util.ConcurrentHashMapWithDefault;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;


import weka.core.ManhattanDistance;

/**
 * @author Reiner Jung
 *
 */
public abstract class AbstractObservationConfiguration extends Configuration {

    /**
     * record switch filter. Is required to be global so we can cheat and get measurements from the
     * filter.
     */
    protected final RecordSwitch recordSwitch;

    protected TAllocation tAllocationSuccDeploy;

    protected TAllocationFinished tAllocationFinished;

    protected TDeployment deployment;

    protected final TDeployment deploymentAfterAllocation;
    protected TDeployment deploymentSuccAllocation;

    protected final TAllocation tAllocationAfterDeploy;

    protected TUndeployment undeployment;

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
     * @param resourceEnvironmentModelGraphProvider
     *            the resource environment graph provider
     * @param allocationModelProvider
     *            the allocation model provider
     * @param allocationModelGraphProvider
     *            the allocation model graph provider
     * @param systemModelProvider
     *            the system model provide
     * @param systemModelGraphProvider
     *            the system model graph provider
     * @param varianceOfUserGroups
     *            variance of user groups, configuration for entry event filter
     * @param thinkTime
     *            think time, configuration for entry event filter
     * @param closedWorkload
     *            kind of workload, configuration for entry event filter
     * @param visualizationServiceURL
     *            url to the visualization service
     * @param aggregationType
     *            aggregation type
     * @param outputMode
     *            output mode
     */
    public AbstractObservationConfiguration(final ICorrespondence correspondenceModel,
            final UsageModelProvider usageModelProvider, final RepositoryModelProvider repositoryModelProvider,
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final AllocationModelProvider allocationModelProvider,
            final ModelProvider<Allocation> allocationModelGraphProvider, final SystemModelProvider systemModelProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
            final String visualizationServiceURL, final EAggregationType aggregationType,
            final EOutputMode outputMode) {

        /** configure filter. */
        this.recordSwitch = new RecordSwitch();

        final TAllocation tAllocation = new TAllocation(resourceEnvironmentModelGraphProvider);
        final TAllocationFinished tAllocationFinished = new TAllocationFinished();
        this.deployment = new TDeployment(correspondenceModel, allocationModelGraphProvider, systemModelGraphProvider,
                resourceEnvironmentModelGraphProvider);
        this.deploymentAfterAllocation = new TDeployment(correspondenceModel, allocationModelGraphProvider,
                systemModelGraphProvider, resourceEnvironmentModelGraphProvider);
        this.tAllocationAfterDeploy = new TAllocation(resourceEnvironmentModelGraphProvider);
        this.undeployment = new TUndeployment(correspondenceModel, allocationModelGraphProvider,
                systemModelGraphProvider, resourceEnvironmentModelGraphProvider);

        /** Trace reconstruction. */
        ConcurrentHashMapWithDefault<Long, EventBasedTrace> traceBuffer = new ConcurrentHashMapWithDefault<Long, EventBasedTrace>(
                EventBasedTraceFactory.INSTANCE);

        final TraceReconstructionFilter traceReconstructionFilter = new TraceReconstructionFilter(traceBuffer);

        IEntryCallTraceMatcher matcher = new JPetStoreCallTraceMatcher();

        final TEntryCall tEntryCall = new TEntryCall(matcher);

        // final TEntryCallSequenceWithPCM tEntryCallSequenceWithPCM;
        // final TEntryEventSequence tEntryEventSequence;

        /** new extended clustering. */
        final TEntryCallSequence entryCallSequence = new TEntryCallSequence();

        // one hour interval or at least 10 user sessions
        final CollectUserSessionsFilter collectUserSessions = new CollectUserSessionsFilter(1000 * 60 * 60, 10);
        final TimeTriggerFilter timeTriggerFilter = new TimeTriggerFilter(1000);

        final TraceAcceptanceFilter traceAcceptanceFilter = new TraceAcceptanceFilter(
                new JPetStoreTraceAcceptanceMatcher());

        final TraceOperationCleanupFilter traceOperationCleanupFilter = new TraceOperationCleanupFilter(
                new JPetStoreTraceSignatureCleanupRewriter());

        final EntryCallFilterRules modelGenerationFilter;
        final ISignatureCreationStrategy signatureStrategy;
        final int expectedUserGroups;
        if (thinkTime == 1) {
            modelGenerationFilter = new JPetStoreEntryCallRulesFactory().createFilter();
            expectedUserGroups = 9;
            signatureStrategy = new GetLastXSignatureStrategy(2);
        } else {
            modelGenerationFilter = new CoCoMEEntryCallRulesFactory().createFilter();
            signatureStrategy = new GetLastXSignatureStrategy(1);
            expectedUserGroups = 4;
        }

        // usageModelProvider.getModel().getUsageScenario_UsageModel().size();
        final IVectorQuantizationClustering behaviorModelClustering = new XMeansClustering(expectedUserGroups,
                varianceOfUserGroups, new ManhattanDistance());

        final BehaviorModelConfiguration behaviorModelConfiguration = new BehaviorModelConfiguration();
        behaviorModelConfiguration.setBehaviorModelNamePrefix("cdor-");
        behaviorModelConfiguration.setVisualizationUrl(visualizationServiceURL);
        behaviorModelConfiguration.setModelGenerationFilter(modelGenerationFilter);
        behaviorModelConfiguration.setRepresentativeStrategy(new JPetstoreStrategy());
        behaviorModelConfiguration.setSignatureCreationStrategy(signatureStrategy);
        behaviorModelConfiguration.setClustering(behaviorModelClustering);
        behaviorModelConfiguration.setAggregationType(aggregationType);
        behaviorModelConfiguration.setOutputMode(outputMode);

        // final TBehaviorModel tBehaviorModel = new TBehaviorModel(behaviorModelConfiguration);

        final TBehaviorModelComparison tBehaviorModelComparison = new TBehaviorModelComparison(
                behaviorModelConfiguration, correspondenceModel, usageModelProvider, repositoryModelProvider,
                varianceOfUserGroups, thinkTime, closedWorkload);

        /** plain clustering. It might be included in the setup above. */
        // tEntryCallSequenceWithPCM = new TEntryCallSequenceWithPCM(correspondenceModel);
        // tEntryEventSequence = new TEntryEventSequence(correspondenceModel, usageModelProvider,
        // repositoryModelProvider, varianceOfUserGroups, thinkTime, closedWorkload);
        // final TNetworkLink tNetworkLink = new TNetworkLink(allocationModelProvider,
        // systemModelProvider, resourceEnvironmentModelProvider);

        /** -- end plain clustering. */

        /** dispatch different monitoring data. */
        this.connectPorts(this.recordSwitch.getDeploymentOutputPort(), this.deployment.getInputPort());
        this.connectPorts(this.recordSwitch.getUndeploymentOutputPort(), this.undeployment.getInputPort());
        this.connectPorts(this.recordSwitch.getAllocationOutputPort(), tAllocation.getInputPort());
        this.connectPorts(this.recordSwitch.getFlowOutputPort(), traceReconstructionFilter.getInputPort());
        this.connectPorts(traceReconstructionFilter.getTraceValidOutputPort(), tEntryCall.getInputPort());
        // this.connectPorts(this.recordSwitch.getTraceMetaPort(), tNetworkLink.getInputPort());

        this.connectPorts(this.deployment.getDeploymentOutputPort(), tAllocationFinished.getDeploymentInputPort());
        this.connectPorts(this.deployment.getAllocationOutputPort(), this.tAllocationAfterDeploy.getInputPort());
        this.connectPorts(this.tAllocationAfterDeploy.getAllocationFinishedOutputPort(),
                tAllocationFinished.getAllocationFinishedInputPort());
        this.connectPorts(tAllocationFinished.getDeploymentOutputPort(), this.deploymentAfterAllocation.getInputPort());

        this.connectPorts(tEntryCall.getOutputPort(), entryCallSequence.getEntryCallInputPort());
        this.connectPorts(this.recordSwitch.getSessionEventPort(), entryCallSequence.getSessionEventInputPort());

        this.connectPorts(timeTriggerFilter.getOutputPort(), collectUserSessions.getTimeTriggerInputPort());
        this.connectPorts(entryCallSequence.getUserSessionOutputPort(), traceAcceptanceFilter.getInputPort());
        this.connectPorts(traceAcceptanceFilter.getOutputPort(), traceOperationCleanupFilter.getInputPort());
        this.connectPorts(traceOperationCleanupFilter.getOutputPort(), collectUserSessions.getUserSessionInputPort());
        this.connectPorts(collectUserSessions.getOutputPort(), tBehaviorModelComparison.getInputPort());
    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }

}
