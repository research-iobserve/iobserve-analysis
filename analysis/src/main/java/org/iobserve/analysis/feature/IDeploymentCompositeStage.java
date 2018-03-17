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

import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * @author Reiner Jung
 *
 */
public interface IDeploymentCompositeStage {

    /**
     * Input port for deployment events.
     *
     * @return returns the input port
     */
    InputPort<IDeployedEvent> getDeployedInputPort();

    /**
     * output port to inform about deployments.
     *
     * @return returns the output port
     */
    OutputPort<PCMDeployedEvent> getDeployedOutputPort();

    /**
     * Output port to inform about an allocation performed by the stage.
     *
     * @return returns the output port
     */
    OutputPort<ResourceContainer> getAllocationOutputPort();

}
