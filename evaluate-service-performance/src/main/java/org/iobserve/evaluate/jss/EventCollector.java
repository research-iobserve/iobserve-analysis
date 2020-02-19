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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.MeasureEventOccurance;
import org.iobserve.common.record.ObservationPoint;

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
        final Comparator<List<MeasureEventOccurance>> comparator = new Comparator<List<MeasureEventOccurance>>() {

            @Override
            public int compare(final List<MeasureEventOccurance> o1, final List<MeasureEventOccurance> o2) {
                final Long eventTime1 = this.getFirstEventTime(o1, ObservationPoint.EVENT_CREATION_TIME);
                final Long eventTime2 = this.getFirstEventTime(o2, ObservationPoint.EVENT_CREATION_TIME);

                if (eventTime1 < eventTime2) {
                    return -1;
                } else if (eventTime1 > eventTime2) {
                    return 1;
                } else {
                    return 0;
                }
            }

            private Long getFirstEventTime(final List<MeasureEventOccurance> events, final ObservationPoint point) {
                final MeasureEventOccurance event = this.getFirstEvent(events, point);
                if (event != null) {
                    return event.getTimestamp();
                } else {
                    return null;
                }
            }

            private MeasureEventOccurance getFirstEvent(final List<MeasureEventOccurance> events,
                    final ObservationPoint point) {
                for (final MeasureEventOccurance event : events) {
                    if (event.getPoint().equals(point)) {
                        return event;
                    }
                }
                return null;
            }
        };
        final List<List<MeasureEventOccurance>> list = new ArrayList<>();
        list.addAll(this.data.values());
        Collections.sort(list, comparator);

        for (final List<MeasureEventOccurance> entry : list) {
            this.outputPort.send(entry);
        }
        super.onTerminating();
    }

    public OutputPort<List<MeasureEventOccurance>> getOutputPort() {
        return this.outputPort;
    }

}
