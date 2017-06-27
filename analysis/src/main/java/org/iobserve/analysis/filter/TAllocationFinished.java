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
