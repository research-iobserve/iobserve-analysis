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

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.analysis.model.provider.neo4j.ModelProvider;
import org.iobserve.service.privacy.violation.data.Warnings;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * Privacy warner.
 *
 * @author Reiner Jung -- initial contribution
 *
 */
public class PrivacyWarner extends AbstractStage {

    private final ModelProvider<Allocation> allocationModelGraphProvider;
    private final ModelProvider<System> systemModelGraphProvider;
    private final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider;

    private final InputPort<PCMDeployedEvent> deployedInputPort = this.createInputPort(PCMDeployedEvent.class);
    private final InputPort<PCMUndeployedEvent> undeployedInputPort = this.createInputPort(PCMUndeployedEvent.class);

    private final OutputPort<Warnings> probesOutputPort = this.createOutputPort(Warnings.class);
    private final OutputPort<Warnings> warningsOutputPort = this.createOutputPort(Warnings.class);

    /**
     * Create and initialize privacy warner.
     *
     * @param allocationModelGraphProvider
     *            allocation model provider
     * @param systemModelGraphProvider
     *            system model provider
     * @param resourceEnvironmentModelGraphProvider
     *            resource environment model provider
     */
    public PrivacyWarner(final ModelProvider<Allocation> allocationModelGraphProvider,
            final ModelProvider<System> systemModelGraphProvider,
            final ModelProvider<ResourceEnvironment> resourceEnvironmentModelGraphProvider) {
        this.allocationModelGraphProvider = allocationModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.resourceEnvironmentModelGraphProvider = resourceEnvironmentModelGraphProvider;
    }

    @Override
    protected void execute() throws Exception {
        final Warnings warnings = new Warnings();

        final PCMDeployedEvent deployedEvent = this.deployedInputPort.receive();
        final PCMUndeployedEvent undeployedEvent = this.undeployedInputPort.receive();

        if (deployedEvent != null) {
            // TODO generate warnings after the last deployment
        } else if (undeployedEvent != null) {
            // TODO generate warnings after the last undeployment
        }

        this.probesOutputPort.send(warnings);

        this.warningsOutputPort.send(warnings);
    }

    public OutputPort<Warnings> getProbesOutputPort() {
        return this.probesOutputPort;
    }

    public OutputPort<Warnings> getWarningsOutputPort() {
        return this.warningsOutputPort;
    }

    public InputPort<PCMDeployedEvent> getDeployedInputPort() {
        return this.deployedInputPort;
    }

    public InputPort<PCMUndeployedEvent> getUndeployedInputPort() {
        return this.undeployedInputPort;
    }

}
