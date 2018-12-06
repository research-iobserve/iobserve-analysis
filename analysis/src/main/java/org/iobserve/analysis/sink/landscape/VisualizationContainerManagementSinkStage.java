/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.sink.landscape;

import java.io.IOException;
import java.net.URL;

import kieker.common.configuration.Configuration;

import teetime.framework.InputPort;

import org.iobserve.analysis.ConfigurationKeys;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.analysis.feature.IContainerManagementSinkStage;
import org.iobserve.analysis.sink.InitializeDeploymentVisualization;
import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * Send container management information to the visualization service.
 *
 * @author Reiner Jung
 *
 */
public class VisualizationContainerManagementSinkStage implements IContainerManagementSinkStage {

    public VisualizationContainerManagementSinkStage(final Configuration configuration,
            final ModelResource<ResourceEnvironment> resourceEnvironmentModelProvider,
            final ModelResource<System> systemModelProvider, final ModelResource<Allocation> allocationModelProvider)
            throws IOException, DBException {
        final URL url = new URL(configuration.getStringProperty(ConfigurationKeys.IOBSERVE_VISUALIZATION_URL));
        final String systemId = configuration.getStringProperty(ConfigurationKeys.SYSTEM_ID);
        final InitializeDeploymentVisualization deploymentVisualization = new InitializeDeploymentVisualization(url,
                systemId, allocationModelProvider, systemModelProvider, resourceEnvironmentModelProvider);

        deploymentVisualization.initialize();
    }

    @Override
    public InputPort<ResourceContainer> getAllocationInputPort() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputPort<ResourceContainer> getDeallocationInputPort() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputPort<PCMDeployedEvent> getDeployedInputPort() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputPort<PCMUndeployedEvent> getUndeployedInputPort() {
        // TODO Auto-generated method stub
        return null;
    }

}
