/***************************************************************************
 * Copyright 2019 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.resources.calculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueSerializer;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Receives events of a specific type and maps their value to a hashmap.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class EventMapper<I extends AbstractMonitoringRecord> extends AbstractConsumerStage<I> {

    private final OutputPort<Map<String, Object>> outputPort = this.createOutputPort();

    @Override
    protected void execute(final I element) throws Exception {
        final List<Object> list = new ArrayList<>();
        final IValueSerializer serializer = new ListValueSerializer(list);
        element.serialize(serializer);

        final Map<String, Object> map = new HashMap<>();

        int i = 0;
        for (final String name : element.getValueNames()) {
            map.put(name, list.get(i));
            i++;
        }
        this.outputPort.send(map);
    }

    public OutputPort<Map<String, Object>> getOutputPort() {
        return this.outputPort;
    }

}
