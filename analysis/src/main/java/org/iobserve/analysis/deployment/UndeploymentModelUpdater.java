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

import org.iobserve.analysis.AnalysisExperimentLogging;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.common.record.ObservationPoint;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.IModelResource;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationPackage;

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

    /** reference to system model provider. */
    private final IModelResource<Allocation> allocationModelResource;

    private final OutputPort<PCMUndeployedEvent> outputPort = this.createOutputPort();

    /**
     * Most likely the constructor needs an additional field for the PCM access. But this has to be
     * discussed with Robert.
     *
     * @param allocationModelResource
     *            system model access
     */
    public UndeploymentModelUpdater(final IModelResource<Allocation> allocationModelResource) {
        this.allocationModelResource = allocationModelResource;
    }

    /**
     * This method is triggered for every undeployment event.
     *
     * @param event
     *            undeployment event
     * @throws DBException
     *             on db errors
     */
    @Override
    protected void execute(final PCMUndeployedEvent event) throws DBException {
        DeploymentLock.lock();
        AnalysisExperimentLogging.measure(event, ObservationPoint.MODEL_UPDATE_ENTRY);
        this.logger.debug("Undeployment assemblyContext={} resourceContainer={}", event.getAssemblyContext(),
                event.getResourceContainer());
        final String allocationContextName = NameFactory.createAllocationContextName(event.getAssemblyContext(),
                event.getResourceContainer());

        final List<AllocationContext> allocationContexts = this.allocationModelResource.findObjectsByTypeAndProperty(
                AllocationContext.class, AllocationPackage.Literals.ALLOCATION_CONTEXT, "entityName",
                allocationContextName);

        if (allocationContexts.size() == 1) {
            final AllocationContext allocationContext = allocationContexts.get(0);
            this.allocationModelResource.deleteObject(allocationContext);
            AnalysisExperimentLogging.measure(event, ObservationPoint.MODEL_UPDATE_EXIT);
            DeploymentLock.unlock();
            this.outputPort.send(event);
        } else if (allocationContexts.size() > 1) {
            AnalysisExperimentLogging.measure(event, ObservationPoint.MODEL_UPDATE_EXIT);
            this.logger.error("Undeployment failed: More than one allocation found for allocation {}",
                    allocationContextName);
            DeploymentLock.unlock();
        } else {
            AnalysisExperimentLogging.measure(event, ObservationPoint.MODEL_UPDATE_EXIT);
            this.logger.error("Undeployment failed: No allocation found for allocation {}", allocationContextName);
            DeploymentLock.unlock();
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
