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
package org.iobserve.analysis.deployment;

import java.util.LinkedList;
import java.util.Queue;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Wait until the allocation (triggered by a deployment) is finished. Then add the resource
 * container to the PCMDeployedEvent and forward the event in order to complete the deployment.
 *
 * The functionality of this stage can be implemented in a better way by extending the TeeTime stage
 * AbstractBiCombinerStage<I,J>. The AbstractBiCombinerStage<I,J> will be part of TeeTime in near
 * future. Please use the TeeTime implementation, if it is available in your version of TeeTime.
 *
 *
 * @author Josefine Wegert
 * @author Reiner Jung
 */

public class AllocationFinishedStage extends AbstractStage {

    private final InputPort<PCMDeployedEvent> deployedInputPort = super.createInputPort();
    private final InputPort<ResourceContainer> allocationFinishedInputPort = super.createInputPort();
    private final Queue<PCMDeployedEvent> deployments = new LinkedList<>();
    private final Queue<ResourceContainer> resourceContainers = new LinkedList<>();

    private final OutputPort<PCMDeployedEvent> deployedOutputPort = this.createOutputPort();

    /**
     * Empty default constructor.
     */
    public AllocationFinishedStage() {
        // nothing to do here
    }

    /**
     *
     * @return deploymentInputPort
     */
    public InputPort<PCMDeployedEvent> getDeployedInputPort() {
        return this.deployedInputPort;
    }

    /**
     *
     * @return allocationFinishedInputPort
     */

    public InputPort<ResourceContainer> getAllocationFinishedInputPort() {
        return this.allocationFinishedInputPort;
    }

    /**
     *
     * @return deploymentOutputPort
     */

    public OutputPort<PCMDeployedEvent> getDeployedOutputPort() {
        return this.deployedOutputPort;
    }

    /**
     * Forwards the deployment event after the allocation is finished.
     *
     * @throws Exception
     *             exception
     */
    @Override
    protected void execute() throws Exception {
        final ResourceContainer resourceContainer = this.allocationFinishedInputPort.receive();
        final PCMDeployedEvent deployedEvent = this.deployedInputPort.receive();

        if (resourceContainer != null) {
            this.logger.debug("Receive resource container {}", resourceContainer);
            this.resourceContainers.add(resourceContainer);
        }

        if (deployedEvent != null) {
            this.logger.debug("Receive deployed event {}", deployedEvent);
            this.deployments.add(deployedEvent);
        }
        // TODO better ensure matching parts
        if (this.resourceContainers.size() > 0 && this.deployments.size() > 0) {
            final PCMDeployedEvent deployed = this.deployments.poll();
            deployed.setResourceContainer(this.resourceContainers.poll());

            this.logger.debug("Send deployed {}", deployed);
            this.deployedOutputPort.send(deployed);
        }

    }

}
