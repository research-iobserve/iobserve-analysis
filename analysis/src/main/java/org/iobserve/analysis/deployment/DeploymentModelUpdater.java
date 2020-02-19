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
import org.iobserve.model.correspondence.AllocationEntry;
import org.iobserve.model.correspondence.CorrespondenceFactory;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.Part;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.IModelResource;
import org.iobserve.model.persistence.neo4j.InvocationException;
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
    private final IModelResource<Allocation> allocationModelResource;

    private final OutputPort<PCMDeployedEvent> deployedNotifyOutputPort = this.createOutputPort();

    private final IModelResource<CorrespondenceModel> correspondenceModelResource;

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param correspondenceModelResource
     *            correspondence model resource
     * @param allocationModelResource
     *            allocation model provider
     */
    public DeploymentModelUpdater(final IModelResource<CorrespondenceModel> correspondenceModelResource,
            final IModelResource<Allocation> allocationModelResource) {
        this.correspondenceModelResource = correspondenceModelResource;
        this.allocationModelResource = allocationModelResource;
    }

    /**
     * Execute an deployment event.
     *
     * @param event
     *            one deployment event to be processed
     * @throws NodeLookupException
     *             node lookup failed
     * @throws DBException
     *             on db error
     * @throws InvocationException
     *             on invocation errors
     */
    @Override
    protected void execute(final PCMDeployedEvent event) throws NodeLookupException, InvocationException, DBException {
        DeploymentLock.lock();
        // ExperimentLoggingUtils.measureDeploymentEvent(event, ObservationPoint.MODEL_UPDATE_ENTRY);
        this.logger.debug("Send event from {}", this.getInputPort().getPipe().getSourcePort().getOwningStage().getId());
        this.logger.debug("Deployment model update: assemblyContext={} resourceContainer={} service={}",
                event.getAssemblyContext(), event.getResourceContainer(), event.getService());
        final String allocationContextName = NameFactory.createAllocationContextName(event.getAssemblyContext(),
                event.getResourceContainer());

        final List<AllocationContext> allocationContext = this.allocationModelResource.findObjectsByTypeAndProperty(
                AllocationContext.class, AllocationPackage.Literals.ALLOCATION_CONTEXT, "entityName",
                allocationContextName);
        if (allocationContext.isEmpty()) {
            this.logger.debug("Create allocation context (deploy) {}", event);
            final Allocation allocationModel = this.allocationModelResource.getModelRootNode(Allocation.class,
                    AllocationPackage.Literals.ALLOCATION);

            final AllocationContext newAllocationContext = AllocationFactory.eINSTANCE.createAllocationContext();
            newAllocationContext.setEntityName(allocationContextName);
            newAllocationContext.setAssemblyContext_AllocationContext(event.getAssemblyContext());
            newAllocationContext.setResourceContainer_AllocationContext(event.getResourceContainer());

            allocationModel.getAllocationContexts_Allocation().add(newAllocationContext);

            this.allocationModelResource.updatePartition(allocationModel);

            final AllocationContext storedAllocationContext = this.allocationModelResource
                    .resolve(newAllocationContext);

            /** create correspondence model entry. */
            final CorrespondenceModel correspondenceModel = this.correspondenceModelResource
                    .getModelRootNode(CorrespondenceModel.class, CorrespondencePackage.Literals.CORRESPONDENCE_MODEL);
            final Part part = this.findOrCreate(correspondenceModel, allocationModel);

            final AllocationEntry entry = CorrespondenceFactory.eINSTANCE.createAllocationEntry();
            entry.setAllocation(storedAllocationContext);
            entry.setTechnology(event.getTechnology());
            entry.setImplementationId(event.getUrl());

            part.getEntries().add(entry);

            this.correspondenceModelResource.updatePartition(correspondenceModel);

            this.logger.debug("PUSHED CORRESPONDENCE {} {} {}", newAllocationContext.getEntityName(),
                    event.getTechnology().getLiteral(), event.getUrl());
        } else {
            this.logger.error("Deployment failed: Allocation Context {} already exists in allocation model.",
                    allocationContextName);
        }

        // signal deployment update
        // ExperimentLoggingUtils.measureDeploymentEvent(event, ObservationPoint.MODEL_UPDATE_EXIT);
        DeploymentLock.unlock();
        this.deployedNotifyOutputPort.send(event);
    }

    /**
     * Find the corresponding part for the allocationModel or create a new part if no part exists.
     * The method has a side effect, as the part is added to the correspondence model.
     *
     * @param correspondenceModel
     *            correspondence model
     * @param allocationModel
     *            allocation model
     * @return returns a part
     */
    private Part findOrCreate(final CorrespondenceModel correspondenceModel, final Allocation allocationModel) {
        for (final Part part : correspondenceModel.getParts()) {
            if (part.getModelType().equals(allocationModel)) {
                return part;
            }
        }

        final Part part = CorrespondenceFactory.eINSTANCE.createPart();
        part.setModelType(allocationModel);

        correspondenceModel.getParts().add(part);

        return part;
    }

    /**
     * @return deploymentFinishedOutputPort
     */
    public OutputPort<PCMDeployedEvent> getDeployedNotifyOutputPort() {
        return this.deployedNotifyOutputPort;
    }

}
