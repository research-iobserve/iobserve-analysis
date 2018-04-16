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
package org.iobserve.stages.source;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IEventRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * @author Reiner Jung
 *
 */
public class SynthesizedTimeTriggerStage extends AbstractConsumerStage<IMonitoringRecord>
        implements IPeriodicalTriggerStage {

    private final OutputPort<Long> outputPort = this.createOutputPort(Long.class);

    private final long interval;

    private long last;

    /**
     * Create a synthesized time trigger stage.
     *
     * @param interval
     *            trigger interval
     */
    public SynthesizedTimeTriggerStage(final long interval) {
        this.interval = interval;
        this.declareActive();
    }

    @Override
    public OutputPort<Long> getOutputPort() {
        return this.outputPort;
    }

    @Override
    protected void execute(final IMonitoringRecord event) throws Exception {
        if (event instanceof IEventRecord) {
            final long now = ((IEventRecord) event).getTimestamp();
            if (this.last - now > this.interval) {
                this.last = now;
                this.outputPort.send(this.last);
            }
        } else {
            final long now = event.getLoggingTimestamp();
            if (this.last - now > this.interval) {
                this.last = now;
                this.outputPort.send(this.last);
            }
        }
    }

}
