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
package org.iobserve.stages.source;

import java.util.Date;

import teetime.framework.AbstractProducerStage;

/**
 * This approach will not provide exact intervals, as thread switches and other effects will add to
 * the interval.
 *
 * @author Reiner Jung
 *
 */
public class TimeTriggerFilter extends AbstractProducerStage<Long> {
    private final long interval;

    /**
     * Create a time trigger filter with an trigger interval in milliseconds.
     *
     * @param interval
     *            the trigger interval
     */
    public TimeTriggerFilter(final long interval) {
        this.interval = interval;
        this.declareActive();
    }

    // TODO: Generalize for experiments (continuous execution vs. sending a single signal and
    // terminating)
    @Override
    protected void execute() throws Exception {
        Thread.sleep(this.interval);
        this.outputPort.send(new Date().getTime());
        this.workCompleted();
    }

}
