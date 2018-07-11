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
package org.iobserve.service.privacy.violation.filter;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.service.privacy.violation.data.IProbeManagement;
import org.iobserve.stages.data.Warnings;

/**
 * Model level controller for probes. The filter receives a list of warnings and computes a list of
 * probe activation and deactivation. Therefore, it requires internal knowledge of previous probe
 * activations.
 *
 * @author Reiner Jung -- initial
 *
 */
public class ModelProbeController extends AbstractConsumerStage<Warnings> {

    private final OutputPort<IProbeManagement> outputPort = this.createOutputPort(IProbeManagement.class);
    private final ModelResource allocationModelResource;
    private final ModelResource systemModelResource;
    private final ModelResource resourceEnvironmentModelResource;

    /**
     * Create an initialize the model probe controller.
     *
     * @param allocationModelResource
     *            allocation model provider
     * @param systemModelResource
     *            system model provider
     * @param resourceEnvironmentModelResource
     *            resource environment model provider
     */
    public ModelProbeController(final ModelResource allocationModelResource, final ModelResource systemModelResource,
            final ModelResource resourceEnvironmentModelResource) {
        this.allocationModelResource = allocationModelResource;
        this.systemModelResource = systemModelResource;
        this.resourceEnvironmentModelResource = resourceEnvironmentModelResource;
    }

    @Override
    protected void execute(final Warnings element) throws Exception {
        // TODO Auto-generated method stub

    }

    public OutputPort<IProbeManagement> getOutputPort() {
        return this.outputPort;
    }

}
