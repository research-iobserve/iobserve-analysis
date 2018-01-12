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

import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.analysis.model.factory.AllocationModelFactory;
import org.iobserve.analysis.model.factory.SystemModelFactory;
import org.iobserve.analysis.model.provider.neo4j.ModelProvider;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.system.System;

/**
 * This class contains the transformation for updating the PCM allocation model with respect to
 * undeployment. It processes undeployment events and uses the correspondence information in the RAC
 * to update the PCM allocation model.
 *
 * @author Robert Heinrich
 * @author Reiner Jung
 * @author Josefine Wegner
 *
 */
public final class UndeploymentModelUpdater extends AbstractConsumerStage<PCMUndeployedEvent> {

    /** reference to allocation model provider. */
    private final ModelProvider<Allocation> allocationModelGraphProvider;
    /** reference to system model provider. */
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;

    private final OutputPort<PCMUndeployedEvent> outputPort = this.createOutputPort();

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param allocationModelGraphProvider
     *            allocation model access
     * @param systemModelGraphProvider
     *            system model access
     * @param resourceEnvironmentModelGraphProvider
     *            resource environment model access
     */
    public UndeploymentModelUpdater(final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider) {
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
    }

    /**
     * This method is triggered for every undeployment event.
     *
     * @param event
     *            undeployment event
     */
    @Override
    protected void execute(final PCMUndeployedEvent event) {
        // get the model entity name
        final String entityName = event.getCorrespondent().getPcmEntityName();

        // build the assembly context name
        final String assemblyContextName = entityName + "_" + event.getService();

        final ResourceContainer resourceContainer = event.getResourceContainer();

        // get assembly context by name or create it if necessary.
        final System system = this.systemModelGraphProvider
                .readOnlyRootComponent(org.palladiosimulator.pcm.system.System.class);
        final Optional<AssemblyContext> optAssemblyContext = SystemModelFactory.getAssemblyContextByName(system,
                assemblyContextName);

        if (optAssemblyContext.isPresent()) {
            // update the allocation graph
            final Allocation allocationModel = this.allocationModelGraphProvider
                    .readOnlyRootComponent(Allocation.class);
            AllocationModelFactory.removeAllocationContext(allocationModel, resourceContainer,
                    optAssemblyContext.get());
            this.allocationModelGraphProvider.updateComponent(Allocation.class, allocationModel);

            // signal allocation update
            this.outputPort.send(event);
        } else {
            if (resourceContainer != null) {
                this.logger.warn("AssemblyContext " + assemblyContextName + " for " + resourceContainer.getEntityName()
                        + " not found! \n");
            } else {
                this.logger.warn(
                        "AssemblyContext " + assemblyContextName + " not found and no resource container specified.\n");
            }

        }
    }

    /**
     *
     * @return output port that signals a model update to the deployment visualization
     */
    public OutputPort<PCMUndeployedEvent> getOutputPort() {
        return this.outputPort;
    }

}
