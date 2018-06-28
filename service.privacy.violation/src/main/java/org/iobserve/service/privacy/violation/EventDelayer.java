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
package org.iobserve.service.privacy.violation;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Delay read events for better testing.
 *
 * @author Reiner Jung
 *
 * @param <I>
 *            type
 *
 * @since 0.0.3
 */
public class EventDelayer<I> extends AbstractConsumerStage<I> {

    private final OutputPort<I> outputPort = this.createOutputPort();

    private final long delay;

    /**
     * Create an event delayer.
     *
     * @param delay
     *            time in milliseconds
     */
    public EventDelayer(final long delay) {
        this.delay = delay;
    }

    @Override
    protected void execute(final I element) throws Exception {
        this.logger.debug("delay event {}", element);
        Thread.sleep(this.delay);
        this.outputPort.send(element);
    }

    public OutputPort<I> getOutputPort() {
        return this.outputPort;
    }

}
