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
package org.iobserve.analysis.test.cli;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.IDeployedEvent;
import org.iobserve.common.record.IUndeployedEvent;
import org.iobserve.common.record.ServletTraceHelper;

/**
 *
 * @author Reiner Jung
 *
 */
public class RecordTypeFilter extends AbstractConsumerStage<IMonitoringRecord> {

    private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort();

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

    @Override
    protected void execute(final IMonitoringRecord element) throws Exception {
        if (!(element instanceof IDeployedEvent) && !(element instanceof IUndeployedEvent)
                && !(element instanceof ServletTraceHelper)) {
            this.outputPort.send(element);
        }
    }

}
