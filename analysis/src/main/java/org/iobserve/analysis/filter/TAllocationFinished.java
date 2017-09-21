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
package org.iobserve.analysis.filter;

import java.util.LinkedList;
import java.util.Queue;

import org.iobserve.common.record.IAllocationRecord;
import org.iobserve.common.record.IDeploymentRecord;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Wait until the allocation (triggered by a deployment) is finished. Then forward the deployment
 * event in order to complete the deployment.
 *
 * The functionality of this stage can be implemented in a better way by extending the TeeTime stage
 * AbstractBiCombinerStage<I,J>. The AbstractBiCombinerStage<I,J> will be part of TeeTime in near
 * future. Please use the TeeTime implementation, if it is available in your version of TeeTime.
 *
 *
 * @author jweg
 *
 */

public class TAllocationFinished extends AbstractStage {

    private final InputPort<IDeploymentRecord> deploymentInputPort = super.createInputPort();
    private final InputPort<IAllocationRecord> allocationFinishedInputPort = super.createInputPort();
    private final Queue<IDeploymentRecord> deployments = new LinkedList<>();
    private final Queue<IAllocationRecord> allocations = new LinkedList<>();

    private final OutputPort<IDeploymentRecord> deploymentOutputPort = this.createOutputPort();

    /**
     * Empty default constructor.
     */
    public TAllocationFinished() {
        // nothing to do here
    }

    /**
     *
     * @return deploymentInputPort
     */
    public InputPort<IDeploymentRecord> getDeploymentInputPort() {
        return this.deploymentInputPort;
    }

    /**
     *
     * @return allocationFinishedInputPort
     */

    public InputPort<IAllocationRecord> getAllocationFinishedInputPort() {
        return this.allocationFinishedInputPort;
    }

    /**
     *
     * @return deploymentOutputPort
     */

    public OutputPort<IDeploymentRecord> getDeploymentOutputPort() {
        return this.deploymentOutputPort;
    }

    /**
     * Forwards the deployment event after the allocation is finished.
     * 
     * @throws Exception
     *             exception
     */
    @Override
    protected void execute() throws Exception {
        final IAllocationRecord allocationFinished = this.allocationFinishedInputPort.receive();
        final IDeploymentRecord deploymentEvent = this.deploymentInputPort.receive();

        if (allocationFinished != null) {
            this.allocations.add(allocationFinished);
        }

        if (deploymentEvent != null) {
            this.deployments.add(deploymentEvent);
        }

        if ((this.allocations.size() > 0) && (this.deployments.size() > 0)) {
            this.deploymentOutputPort.send(this.deployments.poll());
        }

    }

}
