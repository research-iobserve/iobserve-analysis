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

import org.iobserve.model.provider.neo4j.IModelProvider;
import org.iobserve.service.privacy.violation.data.IProbeManagement;
import org.iobserve.service.privacy.violation.data.Warnings;
import org.iobserve.service.privacy.violation.data.ProbeLocation;
import org.iobserve.stages.data.Warnings;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * Model level controller for probes. The filter receives a list of warnings and computes a list of
 * probe activation and deactivations. Therefore, it requires internal knowledge of previous probe
 * activations.
 *
 * @author Reiner Jung -- initial
 *
 */
public class ModelProbeController extends AbstractConsumerStage<Warnings> {

    private final OutputPort<IProbeManagement> outputPort = this.createOutputPort(IProbeManagement.class);
    private final IModelProvider<Allocation> allocationModelGraphProvider;
    private final IModelProvider<System> systemModelGraphProvider;
    private final IModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    /**
     * Create an initialize the model probe controller.
     *
     * @param allocationModelGraphProvider
     *            allocation model provider
     * @param systemModelGraphProvider
     *            system model provider
     * @param resourceEnvironmentModelGraphProvider
     *            resource environment model provider
     */
    public ModelProbeController(final IModelProvider<Allocation> allocationModelGraphProvider,
            final IModelProvider<System> systemModelGraphProvider,
            final IModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    @Override
    protected void execute(final Warnings element) throws Exception {
        // TODO Auto-generated method stub

    }

    public OutputPort<IProbeManagement> getOutputPort() {
        return this.outputPort;
    }

}
