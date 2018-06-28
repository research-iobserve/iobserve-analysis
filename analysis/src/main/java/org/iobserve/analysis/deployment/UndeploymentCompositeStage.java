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
import org.iobserve.model.provider.neo4j.ModelGraph;
import org.iobserve.model.provider.neo4j.ModelProvider;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Undeployment stage.
 *
 * @author Reiner Jung
 *
 */
public class UndeploymentCompositeStage extends CompositeStage {

    private final UndeployPCMMapperStage undeployPCMMapper;
    private final UndeploymentModelUpdater undeployment;

    /**
     * Create a composite stage handling undeployment.
     *
     * @param allocationContextModelGraphProvider
     *            allocation context model provider
     * @param assemblyContextModelProvider
     *            assembly context model provider
     * @param resourceContainerModelProvider
     *            resource container model provider
     * @param correspondenceModelGraph
     *            correspondence model graph
     */
    public UndeploymentCompositeStage(final IModelProvider<AllocationContext> allocationContextModelGraphProvider,
            final IModelProvider<AssemblyContext> assemblyContextModelProvider,
            final IModelProvider<ResourceContainer> resourceContainerModelProvider,
            final ModelGraph correspondenceModelGraph) {

        final IModelProvider<AssemblyEntry> correspondenceModelProvider = new ModelProvider<>(correspondenceModelGraph,
                ModelProvider.IMPLEMENTATION_ID, null);
        this.undeployPCMMapper = new UndeployPCMMapperStage(correspondenceModelProvider, assemblyContextModelProvider,
                resourceContainerModelProvider);

        this.undeployment = new UndeploymentModelUpdater(allocationContextModelGraphProvider);

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
