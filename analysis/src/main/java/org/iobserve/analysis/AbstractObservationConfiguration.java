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

import org.iobserve.analysis.filter.RecordSwitch;
import org.iobserve.analysis.filter.TAllocation;
import org.iobserve.analysis.filter.TAllocationFinished;
import org.iobserve.analysis.filter.TDeployment;
import org.iobserve.analysis.filter.TEntryCall;
import org.iobserve.analysis.filter.TEntryCallSequence;
import org.iobserve.analysis.filter.TEntryEventSequence;
import org.iobserve.analysis.filter.TNetworkLink;
import org.iobserve.analysis.filter.TUndeployment;
import org.iobserve.analysis.model.AllocationModelProvider;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.ResourceEnvironmentModelProvider;
import org.iobserve.analysis.model.SystemModelProvider;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

import teetime.framework.Configuration;

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

    protected final TDeployment deployment;

    protected final TDeployment deploymentAfterAllocation;

    protected final TAllocation tAllocationAfterDeploy;

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
     */
    public AbstractObservationConfiguration(final ICorrespondence correspondenceModel,
            final UsageModelProvider usageModelProvider, final RepositoryModelProvider repositoryModelProvider,
            final ResourceEnvironmentModelProvider resourceEnvironmentModelProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final AllocationModelProvider allocationModelProvider,
            final ModelProvider<Allocation> allocationModelGraphProvider, final SystemModelProvider systemModelProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final int varianceOfUserGroups, final int thinkTime, final boolean closedWorkload) {

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

        final TEntryCall tEntryCall = new TEntryCall();
        final TEntryCallSequence tEntryCallSequence = new TEntryCallSequence(correspondenceModel);
        final TEntryEventSequence tEntryEventSequence = new TEntryEventSequence(correspondenceModel, usageModelProvider,
                repositoryModelProvider, varianceOfUserGroups, thinkTime, closedWorkload);
        final TNetworkLink tNetworkLink = new TNetworkLink(allocationModelProvider, systemModelProvider,
                resourceEnvironmentModelProvider);

        /** dispatch different monitoring data. */
        this.connectPorts(this.recordSwitch.getDeploymentOutputPort(), this.deployment.getInputPort());
        this.connectPorts(this.recordSwitch.getUndeploymentOutputPort(), this.undeployment.getInputPort());
        this.connectPorts(this.recordSwitch.getAllocationOutputPort(), tAllocation.getInputPort());
        this.connectPorts(this.recordSwitch.getFlowOutputPort(), tEntryCall.getInputPort());
        this.connectPorts(this.recordSwitch.getTraceMetaPort(), tNetworkLink.getInputPort());

        this.connectPorts(this.deployment.getDeploymentOutputPort(), tAllocationFinished.getDeploymentInputPort());
        this.connectPorts(this.deployment.getAllocationOutputPort(), this.tAllocationAfterDeploy.getInputPort());
        this.connectPorts(this.tAllocationAfterDeploy.getAllocationFinishedOutputPort(),
                tAllocationFinished.getAllocationFinishedInputPort());
        this.connectPorts(tAllocationFinished.getDeploymentOutputPort(), this.deploymentAfterAllocation.getInputPort());
        this.connectPorts(tEntryCall.getOutputPort(), tEntryCallSequence.getInputPort());
        this.connectPorts(tEntryCallSequence.getOutputPort(), tEntryEventSequence.getInputPort());

    }

    public RecordSwitch getRecordSwitch() {
        return this.recordSwitch;
    }

}
