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

import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

/**
 * Undeployment stage. TODO we need a separate silo to collect synthetic allocations which can then
 * be used by the undeployment stage to automatically deallocate host which have been automatically
 * allocated.
 *
 * @author Reiner Jung
 *
 */
public class UndeploymentCompositeStage extends CompositeStage {

    private final UndeployPCMMapper undeployPCMMapper;
    private final UndeploymentModelUpdater undeployment;

    /**
     * Create a composite stage handling undeployment.
     *
     * @param resourceEnvironmentModelGraphProvider
     *            resource environment provider
     * @param allocationModelGraphProvider
     *            allocation model provider
     * @param allocationContextModelGraphProvider
     *            allocation context model provider
     * @param correspondenceModelProvider
     *            correspondence model handler
     */
    public UndeploymentCompositeStage(final IModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider,
            final IModelProvider<Allocation> allocationModelGraphProvider,
            final IModelProvider<AllocationContext> allocationContextModelGraphProvider,
            final IModelProvider<AssemblyEntry> correspondenceModelProvider) {

        this.undeployPCMMapper = new UndeployPCMMapper(correspondenceModelProvider);

        this.undeployment = new UndeploymentModelUpdater(allocationModelGraphProvider,
                allocationContextModelGraphProvider);

        /** connect internal ports. */
        this.connectPorts(this.undeployPCMMapper.getOutputPort(), this.undeployment.getInputPort());
    }

    public InputPort<IUndeployedEvent> getUndeployedInputPort() {
        return this.undeployPCMMapper.getInputPort();
    }

    public OutputPort<PCMUndeployedEvent> getUndeployedOutputPort() {
        return this.undeployment.getOutputPort();
    }

}
