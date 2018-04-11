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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This approach will not provide exact intervals, as thread switches and other effects will add to
 * the interval length. The send message contain the number of milliseconds since January 1, 1970,
 * 00:00:00 GMT represented by this date.
 *
 * @author Reiner Jung
 *
 */
public class TimeTriggerFilter extends AbstractProducerStage<Long> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTriggerFilter.class);

    private final long interval;

    private final boolean singleEventMode;

    /**
     * Create a time trigger filter with an trigger interval in milliseconds.
     *
     * @param interval
     *            the trigger interval
     * @param singleEventMode
     *            create only one time stamp event after interval milliseconds
     */
    public TimeTriggerFilter(final long interval, final boolean singleEventMode) {
        this.interval = interval;
        this.singleEventMode = singleEventMode;
    }

    @Override
    protected void execute() throws Exception {
        TimeTriggerFilter.LOGGER.debug("Interval length in {} ms", this.interval);
        if (this.singleEventMode) {
            TimeTriggerFilter.LOGGER.info("Single event timer.");
            Thread.sleep(this.interval);
            this.outputPort.send(new Date().getTime());
        } else {
            while (!this.shouldBeTerminated()) {
                Thread.sleep(this.interval);
                final long timestamp = new Date().getTime();
                this.outputPort.send(timestamp);
                TimeTriggerFilter.LOGGER.debug("Time trigger sends {} ms", timestamp);
            }
        }
        this.workCompleted();
    }

}
