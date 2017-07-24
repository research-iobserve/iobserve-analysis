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
        if ((element instanceof IDeploymentRecord) || (element instanceof IUndeploymentRecord)
                || (element instanceof ITraceHelper) || (element instanceof IAllocationRecord)
                || (element instanceof IDeallocationRecord) || (element instanceof GeoLocation)) {
            System.out.println("Got iobserve record " + element);
        } else {
            this.outputPort.send(element);
        }
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

}
