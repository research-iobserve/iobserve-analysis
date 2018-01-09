/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.deployment;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.common.record.IDeployedEvent;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * Composite stage for deployment. This stage automatically creates an allocation (in PCM creates a
 * resource container) for the deployment in case the deployment target does not exists.
 *
 * @author Reiner Jung
 *
 */
public class DeploymentCompositeStage extends CompositeStage {

    private final DeployPCMMapper deployPCMMapper;
    private final RelayEventStage<PCMDeployedEvent> relayDeployedEventStage;
    private final AllocationStage syntehticAllocation;

    public DeploymentCompositeStage(final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<System> systemModelGraphProvider, final ICorrespondence correspondence) {

        this.deployPCMMapper = new DeployPCMMapper(correspondence);
        final SynthesizeAllocationEvent synthesizeAllocationEvent = new SynthesizeAllocationEvent(
                resourceEnvironmentModelGraphProvider);

        final DeploymentModelUpdater deployment = new DeploymentModelUpdater(allocationModelGraphProvider,
                systemModelGraphProvider);

        this.syntehticAllocation = new AllocationStage(resourceEnvironmentModelGraphProvider);
        final AllocationFinishedStage allocationFinished = new AllocationFinishedStage();
        final DeploymentModelUpdater deploymentAfterAllocation = new DeploymentModelUpdater(
                allocationModelGraphProvider, systemModelGraphProvider);

        this.relayDeployedEventStage = new RelayEventStage<>(2);

        /** connect internal ports. */
        this.connectPorts(this.deployPCMMapper.getOutputPort(), synthesizeAllocationEvent.getInputPort());
        this.connectPorts(synthesizeAllocationEvent.getDeployedOutputPort(), deployment.getInputPort());
        this.connectPorts(synthesizeAllocationEvent.getAllocationOutputPort(), this.syntehticAllocation.getInputPort());
        this.connectPorts(synthesizeAllocationEvent.getRelayDeployedOutputPort(),
                allocationFinished.getDeployedInputPort());
        this.connectPorts(this.syntehticAllocation.getAllocationNotifyOutputPort(),
                allocationFinished.getAllocationFinishedInputPort());
        this.connectPorts(allocationFinished.getDeployedOutputPort(), deploymentAfterAllocation.getInputPort());
        this.connectPorts(deployment.getDeployedNotifyOutputPort(), this.relayDeployedEventStage.getInputPort(0));
        this.connectPorts(deploymentAfterAllocation.getDeployedNotifyOutputPort(),
                this.relayDeployedEventStage.getInputPort(1));
    }

    public InputPort<IDeployedEvent> getDeployedInputPort() {
        return this.deployPCMMapper.getInputPort();
    }

    public OutputPort<PCMDeployedEvent> getDeployedOutputPort() {
        return this.relayDeployedEventStage.getOutputPort();
    }

    public OutputPort<ResourceContainer> getAllocationOutputPort() {
        return this.syntehticAllocation.getAllocationNotifyOutputPort();
    }

}
