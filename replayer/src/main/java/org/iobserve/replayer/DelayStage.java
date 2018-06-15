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
package org.iobserve.replayer;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Reiner Jung
 *
 */
public class DelayStage extends AbstractConsumerStage<IMonitoringRecord> {

    private long last;
    private final long unitAdjustment;
    private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort(IMonitoringRecord.class);

    /**
     * Create a delay stage.
     *
     * @param unitAdjustment
     *            divisor for time values.
     */
    public DelayStage(final long unitAdjustment) {
        this.unitAdjustment = unitAdjustment;
    }

    @Override
    protected void execute(final IMonitoringRecord event) {
        if (this.last == 0) {
            this.last = event.getLoggingTimestamp();
        } else {
            final long now = event.getLoggingTimestamp();
            try {
                Thread.sleep((now - this.last) / this.unitAdjustment);
                this.last = now;
                this.outputPort.send(event);
            } catch (final InterruptedException e) {
                this.logger.info("DelayStage got interrupted.");
            }
        }
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

}
