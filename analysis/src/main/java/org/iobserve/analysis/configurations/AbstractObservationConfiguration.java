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

import org.eclipse.emf.common.util.URI;
import org.iobserve.adaptation.AdaptationCalculation;
import org.iobserve.adaptation.AdaptationExecution;
import org.iobserve.adaptation.AdaptationPlanning;
import org.iobserve.adaptation.IAdaptationEventListener;
import org.iobserve.adaptation.SystemAdaptation;
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
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.model.provider.neo4j.AllocationModelProvider;
import org.iobserve.analysis.model.provider.neo4j.ModelProvider;
import org.iobserve.analysis.model.provider.neo4j.RepositoryModelProvider;
import org.iobserve.analysis.model.provider.neo4j.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.provider.neo4j.SystemModelProvider;
import org.iobserve.analysis.model.provider.neo4j.UsageModelProvider;
import org.iobserve.analysis.session.CollectUserSessionsFilter;
import org.iobserve.analysis.session.SessionAcceptanceFilter;
import org.iobserve.analysis.snapshot.SnapshotBuilder;
import org.iobserve.analysis.systems.jpetstore.JPetStoreCallTraceMatcher;
import org.iobserve.analysis.systems.jpetstore.JPetStoreTraceAcceptanceMatcher;
import org.iobserve.analysis.systems.jpetstore.JPetStoreTraceSignatureCleanupRewriter;
import org.iobserve.analysis.traces.EntryCallSequence;
import org.iobserve.analysis.traces.TraceOperationCleanupFilter;
import org.iobserve.analysis.traces.TraceReconstructionCompositeStage;
import org.iobserve.evaluation.ModelComparer;
import org.iobserve.evaluation.SystemEvaluation;
import org.iobserve.planning.CandidateGeneration;
import org.iobserve.planning.CandidateProcessing;
import org.iobserve.planning.ModelOptimization;
import org.iobserve.planning.ModelProcessing;
import org.iobserve.stages.general.EntryCallStage;
import org.iobserve.stages.general.IEntryCallTraceMatcher;
import org.iobserve.stages.general.RecordSwitch;
import org.iobserve.stages.source.TimeTriggerFilter;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

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

    protected final AllocationStage allocation;
    protected final DeploymentCompositeStage deploymentStage;
    protected final UndeploymentCompositeStage undeploymentStage;

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
     * @param snapshotBuilder
     *            snapshotbuilder
     * @param perOpteryxHeadless
     *            perOpterxyheadless URI
     * @param lqnsDir
     *            layered queuing networks directory
     * @param eventListener
     *            eventlistener of some kind
     * @param deployablesFolder
     *            folder containing deployables
     */
    public AbstractObservationConfiguration(final ICorrespondence correspondenceModel,
            final UsageModelProvider usageModelProvider, final RepositoryModelProvider repositoryModelProvider,
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final AllocationModelProvider allocationModelProvider,
            final ModelProvider<Allocation> allocationModelGraphProvider, final SystemModelProvider systemModelProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload,
            final String visualizationServiceURL, final EAggregationType aggregationType, final EOutputMode outputMode,
            final SnapshotBuilder snapshotBuilder, final URI perOpteryxHeadless, final URI lqnsDir,
            final IAdaptationEventListener eventListener, final URI deployablesFolder) {

        final kieker.common.configuration.Configuration configuration = new kieker.common.configuration.Configuration();

        /** configure filter. */
        this.recordSwitch = new RecordSwitch();

        /** allocation. */
        this.allocation = new AllocationStage(resourceEnvironmentModelGraphProvider);

        /** deployment. */
        this.deploymentStage = new DeploymentCompositeStage(configuration, resourceEnvironmentModelGraphProvider,
                allocationModelGraphProvider, systemModelGraphProvider, correspondenceModel);

        /** undeployment. */
        this.undeploymentStage = new UndeploymentCompositeStage(resourceEnvironmentModelGraphProvider,
                allocationModelGraphProvider, systemModelGraphProvider, correspondenceModel);

        /** deallocation. */
        // TODO missing

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
        this.connectPorts(this.recordSwitch.getDeployedOutputPort(), this.deploymentStage.getDeployedInputPort());
        this.connectPorts(this.recordSwitch.getUndeployedOutputPort(), this.undeploymentStage.getUndeployedInputPort());
        this.connectPorts(this.recordSwitch.getAllocationOutputPort(), this.allocation.getInputPort());
        this.connectPorts(this.recordSwitch.getFlowOutputPort(), traceReconstructionStage.getInputPort());
        this.connectPorts(this.recordSwitch.getSessionEventOutputPort(), entryCallSequence.getSessionEventInputPort());

        this.connectPorts(traceReconstructionStage.getTraceValidOutputPort(), entryCallFilter.getInputPort());
        this.connectPorts(this.recordSwitch.getTraceMetadataOutputPort(), networkLink.getInputPort());

        this.connectPorts(entryCallFilter.getOutputPort(), entryCallSequence.getEntryCallInputPort());

        this.connectPorts(timeTriggerFilter.getOutputPort(), collectUserSessions.getTimeTriggerInputPort());
        this.connectPorts(entryCallSequence.getUserSessionOutputPort(), traceAcceptanceFilter.getInputPort());
        this.connectPorts(traceAcceptanceFilter.getOutputPort(), traceOperationCleanupFilter.getInputPort());
        this.connectPorts(traceOperationCleanupFilter.getOutputPort(), collectUserSessions.getUserSessionInputPort());
        this.connectPorts(collectUserSessions.getOutputPort(), tBehaviorModelComparison.getInputPort());

        if (snapshotBuilder != null && perOpteryxHeadless != null && lqnsDir != null && deployablesFolder != null) {
            // create filters for snapshot planning, evaluation and adaptation
            final CandidateGeneration candidateGenerator = new CandidateGeneration(
                    new ModelProcessing(perOpteryxHeadless, lqnsDir), new ModelOptimization(),
                    new CandidateProcessing());
            // There is an AdaptionEventListener class, but in the previous implementation null was
            // used instead.
            final SystemAdaptation systemAdaptor = new SystemAdaptation(new AdaptationCalculation(),
                    new AdaptationPlanning(), new AdaptationExecution(eventListener, deployablesFolder));
            final SystemEvaluation systemEvaluator = new SystemEvaluation(new ModelComparer());

            // Path Snapshot => Planning
            this.connectPorts(snapshotBuilder.getOutputPort(), candidateGenerator.getInputPort());
            // Path Snapshot => Evaluation
            this.connectPorts(snapshotBuilder.getEvaluationOutputPort(), systemEvaluator.getInputPort());
            // Path Planning => Adaptation
            this.connectPorts(candidateGenerator.getOutputPort(), systemAdaptor.getInputPort());
        }
    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }
}
