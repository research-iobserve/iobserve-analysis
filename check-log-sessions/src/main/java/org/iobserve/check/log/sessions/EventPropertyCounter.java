/***************************************************************************
 * Copyright (C) 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.check.log.sessions;

import java.util.HashMap;
import java.util.Map;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Stage to
 *
 * @param <I>
 *            type of receiving events
 * @param <T>
 * @param <R>
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 *
 */
public class EventPropertyCounter<I, T, R> extends AbstractConsumerStage<I> {

    private final IPropertySelector<I, R> selector;
    private final Map<T, R> valueMap;
    private final OutputPort<Map<T, R>> outputPort = this.createOutputPort();

    public EventPropertyCounter(final IPropertySelector<I, R> selector) {
        this.selector = selector;
        this.valueMap = new HashMap<>();
    }

    @Override
    protected void execute(final I element) throws Exception {
        final T entry = this.selector.match(element);
        if (this.valueMap.containsKey(entry)) {
            final R data = this.valueMap.get(entry);
            this.selector.compute(data, element);
            this.valueMap.put(entry, data);
        } else {
            final R data = this.selector.createNewSessionModel();
            this.selector.compute(data, element);
            this.valueMap.put(entry, data);
        }
    }

    @Override
    protected void onTerminating() {
        this.outputPort.send(this.valueMap);
        super.onTerminating();
    }

    public OutputPort<Map<T, R>> getOutputPort() {
        return this.outputPort;
    }

}
