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
package org.iobserve.analysis.configurations;

import teetime.framework.Configuration;

import org.iobserve.analysis.clustering.EAggregationType;
import org.iobserve.analysis.clustering.EOutputMode;
import org.iobserve.analysis.clustering.IVectorQuantizationClustering;
import org.iobserve.analysis.clustering.XMeansClustering;
import org.iobserve.analysis.clustering.filter.composite.BehaviorModelComparison;
import org.iobserve.analysis.clustering.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.clustering.filter.models.configuration.EntryCallFilterRules;
import org.iobserve.analysis.clustering.filter.models.configuration.GetLastXSignatureStrategy;
import org.iobserve.analysis.clustering.filter.models.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.clustering.filter.models.configuration.examples.CoCoMEEntryCallRulesFactory;
import org.iobserve.analysis.clustering.filter.models.configuration.examples.JPetStoreEntryCallRulesFactory;
import org.iobserve.analysis.clustering.filter.models.configuration.examples.JPetstoreStrategy;
import org.iobserve.analysis.deployment.AllocationStage;
import org.iobserve.analysis.deployment.DeploymentCompositeStage;
import org.iobserve.analysis.deployment.NetworkLink;
import org.iobserve.analysis.deployment.UndeploymentCompositeStage;
import org.iobserve.analysis.session.CollectUserSessionsFilter;
import org.iobserve.analysis.session.SessionAcceptanceFilter;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.analysis.systems.jpetstore.JPetStoreCallTraceMatcher;
import org.iobserve.analysis.systems.jpetstore.JPetStoreTraceAcceptanceMatcher;
import org.iobserve.analysis.systems.jpetstore.JPetStoreTraceSignatureCleanupRewriter;
import org.iobserve.analysis.toggle.FeatureToggle;
import org.iobserve.analysis.traces.EntryCallSequence;
import org.iobserve.analysis.traces.TraceOperationCleanupFilter;
import org.iobserve.analysis.traces.TraceReconstructionCompositeStage;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.stages.general.EntryCallStage;
import org.iobserve.stages.general.IEntryCallTraceMatcher;
import org.iobserve.stages.general.RecordSwitch;
import org.iobserve.stages.source.TimeTriggerFilter;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.usagemodel.UsageModel;

import weka.core.ManhattanDistance;

/**
 * @author Reiner Jung
 *
 * @deprecated since 0.0.2 use {@link AnalysisConfiguration} instead
 *
 */
@Deprecated
public abstract class AbstractObservationConfiguration extends Configuration {

    /**
     * record switch filter. Is required to be global so we can cheat and get measurements from the
     * filter.
     */
    protected final RecordSwitch recordSwitch;

    protected AllocationStage allocation;
    protected DeploymentCompositeStage deploymentStage;
    protected UndeploymentCompositeStage undeploymentStage;

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
     *            the system model provide
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
     * @param snapshotBuilder
     *            snapshotbuilder
     * @param featureToggle
     *            feature toggle
     */
    public AbstractObservationConfiguration(final ICorrespondence correspondenceModel,
            final IModelProvider<UsageModel> usageModelProvider,
            final IModelProvider<Repository> repositoryModelProvider,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelProvider,
            final IModelProvider<Allocation> allocationModelProvider,
            final IModelProvider<org.palladiosimulator.pcm.system.System> systemModelProvider,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
            final String visualizationServiceURL, final EAggregationType aggregationType, final EOutputMode outputMode,
            final SnapshotBuilder snapshotBuilder, final FeatureToggle featureToggle) {

        final kieker.common.configuration.Configuration configuration = new kieker.common.configuration.Configuration();

        /** configure filter. */
        this.recordSwitch = new RecordSwitch();

        /** allocation. */
        if (featureToggle.isAllocationToggle()) {
            this.allocation = new AllocationStage(resourceEnvironmentModelProvider);
        }

        /** deployment. */
        if (featureToggle.isDeploymentToggle()) {
            this.deploymentStage = new DeploymentCompositeStage(resourceEnvironmentModelProvider,
                    allocationModelProvider, systemModelProvider, correspondenceModel);
        }

        /** undeployment. */
        if (featureToggle.isUndeploymentToggle()) {
            this.undeploymentStage = new UndeploymentCompositeStage(resourceEnvironmentModelProvider,
                    allocationModelProvider, systemModelProvider, correspondenceModel);
        }

        /** link deployments. */
        final NetworkLink networkLink = new NetworkLink(allocationModelProvider, systemModelProvider,
                resourceEnvironmentModelProvider);

        /** Trace reconstruction. */
        final TraceReconstructionCompositeStage traceReconstructionStage = new TraceReconstructionCompositeStage(
                configuration);

        /** entry call identifies. */
        final IEntryCallTraceMatcher matcher = new JPetStoreCallTraceMatcher();

        final EntryCallStage entryCallFilter = new EntryCallStage(matcher);

        // final TEntryCallSequenceWithPCM tEntryCallSequenceWithPCM;
        // final TEntryEventSequence tEntryEventSequence;

        /** new extended clustering. */
        final EntryCallSequence entryCallSequence = new EntryCallSequence();

        // one hour interval or at least 10 user sessions
        final CollectUserSessionsFilter collectUserSessions = new CollectUserSessionsFilter(1000 * 60 * 60, 10);
        final TimeTriggerFilter timeTriggerFilter = new TimeTriggerFilter(1000);

        final SessionAcceptanceFilter traceAcceptanceFilter = new SessionAcceptanceFilter(
                new JPetStoreTraceAcceptanceMatcher());

        final TraceOperationCleanupFilter traceOperationCleanupFilter = new TraceOperationCleanupFilter(
                new JPetStoreTraceSignatureCleanupRewriter());

        final EntryCallFilterRules modelGenerationFilter;
        final ISignatureCreationStrategy signatureStrategy;
        final int expectedUserGroups;
        if (thinkTime == 1) {
            modelGenerationFilter = JPetStoreEntryCallRulesFactory.createFilter();
            expectedUserGroups = 9;
            signatureStrategy = new GetLastXSignatureStrategy(2);
        } else {
            modelGenerationFilter = CoCoMEEntryCallRulesFactory.createFilter();
            signatureStrategy = new GetLastXSignatureStrategy(1);
            expectedUserGroups = 4;
        }

        // usageModelProvider.getModel().getUsageScenario_UsageModel().size();
        final IVectorQuantizationClustering behaviorModelClustering = new XMeansClustering(expectedUserGroups,
                varianceOfUserGroups, new ManhattanDistance());

        final BehaviorModelConfiguration behaviorModelConfiguration = new BehaviorModelConfiguration();

        // FEATURE BEHAVIOR
        behaviorModelConfiguration.setBehaviorModelNamePrefix("cdor-");
        behaviorModelConfiguration.setVisualizationUrl(visualizationServiceURL);
        behaviorModelConfiguration.setModelGenerationFilter(modelGenerationFilter);
        behaviorModelConfiguration.setRepresentativeStrategy(new JPetstoreStrategy());
        behaviorModelConfiguration.setSignatureCreationStrategy(signatureStrategy);
        behaviorModelConfiguration.setClustering(behaviorModelClustering);
        behaviorModelConfiguration.setAggregationType(aggregationType);
        behaviorModelConfiguration.setOutputMode(outputMode);
        // final TBehaviorModel tBehaviorModel = new TBehaviorModel(behaviorModelConfiguration);

        final BehaviorModelComparison tBehaviorModelComparison = new BehaviorModelComparison(behaviorModelConfiguration,
                correspondenceModel, usageModelProvider, repositoryModelProvider, varianceOfUserGroups, thinkTime,
                closedWorkload);

        /** plain clustering. It might be included in the setup above. */
        // tEntryCallSequenceWithPCM = new TEntryCallSequenceWithPCM(correspondenceModel);
        // tEntryEventSequence = new TEntryEventSequence(correspondenceModel, usageModelProvider,
        // repositoryModelProvider, varianceOfUserGroups, thinkTime, closedWorkload);
        // final TNetworkLink tNetworkLink = new TNetworkLink(allocationModelProvider,
        // systemModelProvider, resourceEnvironmentModelProvider);
        /** -- end plain clustering. */

        /** dispatch different monitoring data. */
        if (featureToggle.isDeploymentToggle()) {
            this.connectPorts(this.recordSwitch.getDeployedOutputPort(), this.deploymentStage.getDeployedInputPort());
        }
        if (featureToggle.isUndeploymentToggle()) {
            this.connectPorts(this.recordSwitch.getUndeployedOutputPort(),
                    this.undeploymentStage.getUndeployedInputPort());
        }
        if (featureToggle.isAllocationToggle()) {
            this.connectPorts(this.recordSwitch.getAllocationOutputPort(), this.allocation.getInputPort());
        }

        this.connectPorts(this.recordSwitch.getFlowOutputPort(), traceReconstructionStage.getInputPort());
        this.connectPorts(this.recordSwitch.getSessionEventOutputPort(), entryCallSequence.getSessionEventInputPort());

        this.connectPorts(traceReconstructionStage.getTraceValidOutputPort(), entryCallFilter.getInputPort());
        this.connectPorts(this.recordSwitch.getTraceMetadataOutputPort(), networkLink.getInputPort());

        this.connectPorts(entryCallFilter.getOutputPort(), entryCallSequence.getEntryCallInputPort());

        this.connectPorts(timeTriggerFilter.getOutputPort(), collectUserSessions.getTimeTriggerInputPort());
        this.connectPorts(entryCallSequence.getUserSessionOutputPort(), traceAcceptanceFilter.getInputPort());
        this.connectPorts(traceAcceptanceFilter.getOutputPort(), traceOperationCleanupFilter.getInputPort());
        this.connectPorts(traceOperationCleanupFilter.getOutputPort(), collectUserSessions.getUserSessionInputPort());

        // FEATURE BEHAVIOR
        this.connectPorts(collectUserSessions.getOutputPort(), tBehaviorModelComparison.getInputPort());

        // TODO for lbl: Implement a way to pass data to the following stages
        // Path Snapshot => Planning
        // this.connectPorts(snapshotBuilder.getOutputPort(),
        // candidateGenerator.getInputPort());
        // Path Snapshot => Evaluation
        // this.connectPorts(snapshotBuilder.getEvaluationOutputPort(),
        // systemEvaluator.getInputPort());

    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }
}
