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
package org.iobserve.analysis;

import org.iobserve.common.record.IAllocationRecord;
import org.iobserve.common.record.IDeploymentRecord;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * TODO: There should be a short description.
 *
 * @author jweg
 *
 */
public class TAllocationFinished extends AbstractStage {

    private final InputPort<IDeploymentRecord> deploymentInputPort = super.createInputPort();
    private final InputPort<IAllocationRecord> allocationFinishedInputPort = super.createInputPort();

    private final OutputPort<IDeploymentRecord> deploymentOutputPort = this.createOutputPort();

    /**
     * empty default constructor.
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

    @Override
    protected void execute() throws Exception {
        final IDeploymentRecord deployEvent = this.deploymentInputPort.receive();
        final IAllocationRecord allocationFinished = this.allocationFinishedInputPort.receive();
        if ((deployEvent != null) && (allocationFinished != null)) {
            this.deploymentOutputPort.send(deployEvent);
        }
    }

}
