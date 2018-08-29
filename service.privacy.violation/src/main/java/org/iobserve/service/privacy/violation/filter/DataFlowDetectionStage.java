/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
import org.iobserve.service.privacy.violation.data.PCMEntryCallEvent;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * @author Reiner Jung
 *
 */
public class DataFlowDetectionStage extends AbstractConsumerStage<PCMEntryCallEvent> {

    private final ModelResource<ResourceEnvironment> resourceEnvironmentResource;
    private final ModelResource<System> systemModelResource;
    private final ModelResource<Allocation> allocationModelResource;
    private final OutputPort<?> outputPort = this.createOutputPort(Object.class); // TODO define
                                                                                  // better
                                                                                  // data type

    /**
     * Create a data flow detection stage.
     *
     * @param allocationModelResource
     *            allocation model provider
     * @param systemModelResource
     *            system model provider
     * @param resourceEnvironmentResource
     *            resource environment model provider
     */
    public DataFlowDetectionStage(final ModelResource<ResourceEnvironment> resourceEnvironmentResource,
            final ModelResource<System> systemModelResource, final ModelResource<Allocation> allocationModelResource) {
        this.allocationModelResource = allocationModelResource;
        this.systemModelResource = systemModelResource;
        this.resourceEnvironmentResource = resourceEnvironmentResource;
    }

    @Override
    protected void execute(final PCMEntryCallEvent element) throws Exception {
        // TODO Auto-generated method stub

    }

    public OutputPort<?> getOutputPort() {
        return this.outputPort;
    }

}
