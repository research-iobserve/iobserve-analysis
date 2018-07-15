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

import java.util.List;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.model.DebugHelper;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.model.persistence.neo4j.NodeLookupException;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationFactory;
import org.palladiosimulator.pcm.allocation.AllocationPackage;

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

    /** reference to allocation model provider. */
    private final ModelResource allocationModelResource;

    private final OutputPort<PCMDeployedEvent> deployedNotifyOutputPort = this.createOutputPort();

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param allocationModelResource
     *            allocation model provider
     */
    public DeploymentModelUpdater(final ModelResource allocationModelResource) {
        this.allocationModelResource = allocationModelResource;
    }

    /**
     * Execute an deployment event.
     *
     * @param event
     *            one deployment event to be processed
     * @throws NodeLookupException
     *             node lookup failed
     */
    @Override
    protected void execute(final PCMDeployedEvent event) throws NodeLookupException {
        this.logger.debug("Send event from {}", this.getInputPort().getPipe().getSourcePort().getOwningStage().getId());
        this.logger.debug("Deployment model update: assemblyContext={} resourceContainer={} service={}",
                event.getAssemblyContext(), event.getResourceContainer(), event.getService());
        final String allocationContextName = event.getAssemblyContext().getEntityName() + " : "
                + event.getResourceContainer().getEntityName();

        final List<AllocationContext> allocationContext = this.allocationModelResource.findObjectsByTypeAndName(
                AllocationContext.class, AllocationPackage.Literals.ALLOCATION_CONTEXT, "entityName",
                allocationContextName);
        if (allocationContext.isEmpty()) {
            this.logger.debug("Create allocation context {}", event);
            final Allocation allocationModel = this.allocationModelResource.getModelRootNode(Allocation.class,
                    AllocationPackage.Literals.ALLOCATION);

            final AllocationContext newAllocationContext = AllocationFactory.eINSTANCE.createAllocationContext();
            newAllocationContext.setEntityName(allocationContextName);
            newAllocationContext.setAssemblyContext_AllocationContext(event.getAssemblyContext());
            newAllocationContext.setResourceContainer_AllocationContext(event.getResourceContainer());

            allocationModel.getAllocationContexts_Allocation().add(newAllocationContext);

            this.allocationModelResource.updatePartition(allocationModel);

            DebugHelper.printModelPartition(this.allocationModelResource.getModelRootNode(Allocation.class,
                    AllocationPackage.Literals.ALLOCATION));

            for (final AllocationContext context : this.allocationModelResource
                    .collectAllObjectsByType(AllocationContext.class, AllocationPackage.Literals.ALLOCATION_CONTEXT)) {
                DebugHelper.printModelPartition(context);
            }

        } else {
            this.logger.error("Deployment failed: Allocation Context {} already exists in allocation model.",
                    allocationContextName);
        }

        // signal deployment update
        this.deployedNotifyOutputPort.send(event);
    }

    /**
     * @return deploymentFinishedOutputPort
     */
    public OutputPort<PCMDeployedEvent> getDeployedNotifyOutputPort() {
        return this.deployedNotifyOutputPort;
    }

}
