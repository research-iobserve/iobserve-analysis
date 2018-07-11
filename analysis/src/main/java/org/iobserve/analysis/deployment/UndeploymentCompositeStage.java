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
import org.iobserve.model.persistence.neo4j.ModelResource;

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
     * @param allocationModelResource
     *            allocation resource
     * @param systemModelResource
     *            system model resource
     * @param resourceEnvironmentResouce
     *            resource environment resource
     * @param correspondenceModelResource
     *            correspondence model resource
     */
    public UndeploymentCompositeStage(final ModelResource resourceEnvironmentResouce,
            final ModelResource systemModelResource, final ModelResource allocationModelResource,
            final ModelResource correspondenceModelResource) {

        this.undeployPCMMapper = new UndeployPCMMapperStage(correspondenceModelResource, systemModelResource,
                resourceEnvironmentResouce);

        this.undeployment = new UndeploymentModelUpdater(allocationModelResource);

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
