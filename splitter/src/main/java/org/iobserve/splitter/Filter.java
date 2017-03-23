package org.iobserve.splitter;

import org.iobserve.common.record.GeoLocation;
import org.iobserve.common.record.IAllocationRecord;
import org.iobserve.common.record.IDeallocationRecord;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ITraceHelper;
import org.iobserve.common.record.IUndeploymentRecord;

import kieker.common.record.IMonitoringRecord;
import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * This filter removes iObserve records from a record stream to support ExplorViz reader.
 *
 * @author Reiner Jung
 *
 */
public class Filter extends AbstractConsumerStage<IMonitoringRecord> {

    private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort();

    @Override
    protected void execute(final IMonitoringRecord element) throws Exception {
        if (element instanceof IDeploymentRecord || element instanceof IUndeploymentRecord
                || element instanceof ITraceHelper || element instanceof IAllocationRecord
                || element instanceof IDeallocationRecord || element instanceof GeoLocation) {
            System.out.println("Got iobserve record " + element);
        } else {
            this.outputPort.send(element);
        }
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

}
