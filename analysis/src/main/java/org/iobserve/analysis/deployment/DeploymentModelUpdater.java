/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.Optional;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.model.builder.AllocationModelBuilder;
import org.iobserve.analysis.model.builder.SystemModelBuilder;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * This class contains the transformation for updating the PCM allocation model with respect to
 * deployment. The PCM allocation model represents a deployment model. That is it uses previously
 * allocated execution environments and deploys services on them.
 *
 * Note: The input PCMDeployedEvent must have the resourceContainer attribute set.
 *
 * @author Robert Heinrich
 * @author Alessandro Giusa
 * @author Josefine Wegert
 * @author Reiner Jung
 */
public final class DeploymentModelUpdater extends AbstractConsumerStage<PCMDeployedEvent> {

    private static final Logger LOGGER = LogManager.getLogger(DeployPCMMapper.class);

    /** reference to allocation model provider. */
    private final ModelProvider<Allocation> allocationModelGraphProvider;
    /** reference to system model provider. */
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;

    private final OutputPort<PCMDeployedEvent> deployedNotifyOutputPort = this.createOutputPort();

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param allocationModelGraphProvider
     *            allocation model provider
     * @param systemModelGraphProvider
     *            system model provider
     * @param resourceEnvironmentModelGraphProvider
     *            resource environment model provider
     */
    public DeploymentModelUpdater(final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider) {
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
    }

    /**
     * Execute an deployment event.
     *
     * @param event
     *            one deployment event to be processed
     */
    @Override
    protected void execute(final PCMDeployedEvent event) {
        // get the model entity name
        final String entityName = event.getCorrespondent().getPcmEntityName();
        // build the assembly context name
        final String assemblyContextName = entityName + "_" + event.getService();

        // get assembly context by name or create it if necessary.
        final AssemblyContext assemblyContext;

        final org.palladiosimulator.pcm.system.System systemModel = this.systemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);

        final Optional<AssemblyContext> optAssemblyContext = SystemModelBuilder.getAssemblyContextByName(systemModel,
                assemblyContextName);

        if (optAssemblyContext.isPresent()) {
            DeploymentModelUpdater.LOGGER
                    .warn("Assembly Context already exists in system model: " + assemblyContextName);
        } else {
            assemblyContext = this.createAndAddAssemblyContext(this.systemModelGraphProvider, assemblyContextName);

            /** update the PCM allocation model (deployment model). */
            final Allocation allocationModel = this.allocationModelGraphProvider
                    .readOnlyRootComponent(Allocation.class);
            if (!AllocationModelBuilder.isAllocationPresent(allocationModel, event.getResourceContainer(),
                    assemblyContext)) {
                AllocationModelBuilder.addAllocationContext(allocationModel, event.getResourceContainer(),
                        assemblyContext);
            }
            this.allocationModelGraphProvider.updateComponent(Allocation.class, allocationModel);

            // signal deployment update
            this.deployedNotifyOutputPort.send(event);
        }
    }

    /**
     * @return deploymentFinishedOutputPort
     */
    public OutputPort<PCMDeployedEvent> getDeployedNotifyOutputPort() {
        return this.deployedNotifyOutputPort;
    }

    /**
     * Create {@link AssemblyContext} with the given name and insert it in the system model.
     *
     * @param systemModelGraphProvider
     *            system model provider
     * @param name
     *            name of the assembly context
     * @return created assembly context
     */
    private AssemblyContext createAndAddAssemblyContext(
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider, final String name) {
        final org.palladiosimulator.pcm.system.System systemModel = systemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);
        final AssemblyContext assemblyContext = SystemModelBuilder.createAssemblyContextsIfAbsent(systemModel, name);
        systemModelGraphProvider.updateComponent(org.palladiosimulator.pcm.system.System.class, systemModel);

        return assemblyContext;
    }

}
