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
package org.iobserve.evaluate.jss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.MeasureEventOccurance;

/**
 * @author Reiner Jung
 *
 */
public class EventCollector extends AbstractConsumerStage<MeasureEventOccurance> {

    private final OutputPort<List<MeasureEventOccurance>> outputPort = this.createOutputPort();
    private final Map<Long, List<MeasureEventOccurance>> data = new HashMap<>();

    @Override
    protected void execute(final MeasureEventOccurance observation) throws Exception {
        List<MeasureEventOccurance> eventList = this.data.get(observation.getId());
        if (eventList == null) {
            eventList = new ArrayList<>();
            this.data.put(observation.getId(), eventList);
        }
        eventList.add(observation);
    }

    @Override
    protected void onTerminating() {
        for (final Entry<Long, List<MeasureEventOccurance>> entry : this.data.entrySet()) {
            this.outputPort.send(entry.getValue());
        }
        super.onTerminating();
    }

    public OutputPort<List<MeasureEventOccurance>> getOutputPort() {
        return this.outputPort;
    }

}
