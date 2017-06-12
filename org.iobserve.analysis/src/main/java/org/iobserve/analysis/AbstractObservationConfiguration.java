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

import org.iobserve.analysis.cdoruserbehavior.filter.composite.TBehaviorModelComparison;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.BehaviorModelConfiguration;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.GetLastXSignatureStrategy;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.IClustering;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.ISignatureCreationStrategy;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.EntryCallFilterRules;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.XMeansClustering;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.examples.CoCoMEEntryCallRulesFactory;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.examples.JPetStoreEntryCallRulesFactory;
import org.iobserve.analysis.cdoruserbehavior.filter.models.configuration.examples.JPetstoreStrategy;
import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.TAllocation;
import org.iobserve.analysis.filter.TDeployment;
import org.iobserve.analysis.filter.TEntryCall;
import org.iobserve.analysis.filter.TEntryCallSequence;
import org.iobserve.analysis.filter.TUndeployment;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;

import teetime.framework.Configuration;
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
    protected RecordSwitch recordSwitch;

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
     *
     * @throws ClassNotFoundException
     *             when a record type could not be loaded by class loader
     * @throws IOException
     *             for all file reading errors
     */
    public AbstractObservationConfiguration(final ICorrespondence correspondenceModel,
            final UsageModelProvider usageModelProvider, final RepositoryModelProvider repositoryModelProvider,
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
            final AllocationModelProvider allocationModelProvider, final SystemModelProvider systemModelProvider,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload) {
        /** configure filter. */
        this.recordSwitch = new RecordSwitch();

        final TAllocation tAllocation = new TAllocation(resourceEnvironmentModelProvider);
        final TDeployment tDeployment = new TDeployment(correspondenceModel, allocationModelProvider,
                systemModelProvider, resourceEnvironmentModelProvider);
        final TUndeployment tUndeployment = new TUndeployment(correspondenceModel, allocationModelProvider,
                systemModelProvider, resourceEnvironmentModelProvider);
        final TEntryCall tEntryCall = new TEntryCall();
        final TEntryCallSequence tEntryCallSequence = new TEntryCallSequence();

        // final TEntryEventSequence tEntryEventSequence = new
        // TEntryEventSequence(correspondenceModel, usageModelProvider,
        // repositoryModelProvider, varianceOfUserGroups, thinkTime, closedWorkload);

        // final TNetworkLink tNetworkLink = new TNetworkLink(allocationModelProvider,
        // systemModelProvider,
        // resourceEnvironmentModelProvider);

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
        final IClustering behaviorModelClustering = new XMeansClustering(expectedUserGroups, varianceOfUserGroups,
                new ManhattanDistance());

        final BehaviorModelConfiguration behaviorModelConfiguration = new BehaviorModelConfiguration();
        behaviorModelConfiguration.setBehaviorModelNamePrefix("cdor-");
        behaviorModelConfiguration.setVisualizationUrl("http://localhost:8080/ubm-backend/v1");
        behaviorModelConfiguration.setModelGenerationFilter(modelGenerationFilter);
        behaviorModelConfiguration.setRepresentativeStrategy(new JPetstoreStrategy());
        behaviorModelConfiguration.setSignatureCreationStrategy(signatureStrategy);
        behaviorModelConfiguration.setClustering(behaviorModelClustering);

        // final TBehaviorModel tBehaviorModel = new TBehaviorModel(behaviorModelConfiguration);

        final TBehaviorModelComparison tBehaviorModelComparison = new TBehaviorModelComparison(
                behaviorModelConfiguration, correspondenceModel, usageModelProvider, repositoryModelProvider,
                varianceOfUserGroups, thinkTime, closedWorkload);

        /** dispatch different monitoring data. */
        this.connectPorts(this.recordSwitch.getDeploymentOutputPort(), tAllocation.getInputPort());
        this.connectPorts(this.recordSwitch.getUndeploymentOutputPort(), tUndeployment.getInputPort());
        this.connectPorts(this.recordSwitch.getFlowOutputPort(), tEntryCall.getInputPort());
        // this.connectPorts(this.recordSwitch.getTraceMetaPort(), tNetworkLink.getInputPort());

        this.connectPorts(tAllocation.getDeploymentOutputPort(), tDeployment.getInputPort());
        this.connectPorts(tEntryCall.getOutputPort(), tEntryCallSequence.getInputPort());
        // this.connectPorts(tEntryCallSequence.getOutputPort(),
        // tEntryEventSequence.getInputPort());
        this.connectPorts(tEntryCallSequence.getOutputPortToBehaviorModelPreperation(),
                tBehaviorModelComparison.getInputPort());

    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }

}
