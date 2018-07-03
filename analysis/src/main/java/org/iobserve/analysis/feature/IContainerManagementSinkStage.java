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
package org.iobserve.analysis.feature;

import kieker.common.configuration.Configuration;

import teetime.framework.InputPort;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.model.persistence.neo4j.IModelProvider;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Interface for container management events.
 *
 * @author Reiner Jung
 *
 */
public interface IContainerManagementSinkStage {

    Class<?>[] SIGNATURE = { Configuration.class, IModelProvider.class, IModelProvider.class, IModelProvider.class,
            IModelProvider.class };

    /**
     *
     * @return returns the allocation input port
     */
    InputPort<ResourceContainer> getAllocationInputPort();

    /**
     *
     * @return returns the deallocation input port
     */
    InputPort<ResourceContainer> getDeallocationInputPort();

    /**
     *
     * @return returns the deployed input port
     */
    InputPort<PCMDeployedEvent> getDeployedInputPort();

    /**
     *
     * @return returns the undeployed input port
     */
    InputPort<PCMUndeployedEvent> getUndeployedInputPort();

}
