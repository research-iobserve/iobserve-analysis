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

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.GeoLocation;
import org.iobserve.common.record.IAllocationEvent;
import org.iobserve.common.record.IDeallocationEvent;
import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.ITraceHelper;
import org.iobserve.common.record.IUndeployedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter removes iObserve records from a record stream to support ExplorViz reader.
 *
 * @author Reiner Jung
 *
 */
public class StripIObserveSpecificEventsFilter extends AbstractConsumerStage<IMonitoringRecord> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsumerStage.class);
    private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort();

    /**
     * Instantiate filter.
     */
    public StripIObserveSpecificEventsFilter() {
        // empty default constructor
    }

    @Override
    protected void execute(final IMonitoringRecord element) throws Exception {
        if (element instanceof IDeployedEvent || element instanceof IUndeployedEvent || element instanceof ITraceHelper
                || element instanceof IAllocationEvent || element instanceof IDeallocationEvent
                || element instanceof GeoLocation) {
            StripIObserveSpecificEventsFilter.LOGGER.debug("Got iobserve record {}.", element);
        } else {
            this.outputPort.send(element);
        }
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

}
