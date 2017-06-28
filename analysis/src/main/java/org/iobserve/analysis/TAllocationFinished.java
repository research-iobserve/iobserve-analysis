package org.iobserve.analysis;

import org.iobserve.common.record.IAllocationRecord;
import org.iobserve.common.record.IDeploymentRecord;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 *
 * @author jweg
 *
 */

public class TAllocationFinished extends AbstractStage {

    private final InputPort<IDeploymentRecord> deploymentInputPort = super.createInputPort();
    private final InputPort<IAllocationRecord> allocationFinishedInputPort = super.createInputPort();

    private final OutputPort<IDeploymentRecord> deploymentOutputPort = this.createOutputPort();

    /**
     * empty default constructor
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
