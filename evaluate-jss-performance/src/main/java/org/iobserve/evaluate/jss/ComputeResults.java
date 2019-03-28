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
public class ComputeResults extends AbstractConsumerStage<List<MeasureEventOccurance>> {

    public static final String TOTAL_PROCESSING_TIME = "total processing";

    public static final String MODEL_UPDATE = "model update";

    public static final String PRIVACY_UPDATE = "privacy update";

    public static final String CONFIGURATION_TIME = "configuration time";

    private static final String TOTAL_REACTION_TIME = "reaction time";

    private static final String TRIGGER_TIME = "date";

    private final OutputPort<Map<String, Long>> outputPort = this.createOutputPort();

    private int sequenceCount;

    private Long timeNormalization;

    @Override
    protected void execute(final List<MeasureEventOccurance> events) throws Exception {
        this.logger.debug("Sequence {}", this.sequenceCount);
        this.sequenceCount++;

        final Map<String, Long> result = new HashMap<>();

        final Long eventTime = this.getFirstEventTime(events, ObservationPoint.EVENT_CREATION_TIME);
        final Long entryTime = this.getFirstEventTime(events, ObservationPoint.DISPATCHER_ENTRY);
        this.checkValue(entryTime, ObservationPoint.DISPATCHER_ENTRY);
        final Long code2ModelEntry = this.getFirstEventTime(events, ObservationPoint.CODE_TO_MODEL_ENTRY);
        this.checkValue(code2ModelEntry, ObservationPoint.CODE_TO_MODEL_ENTRY);
        final Long code2ModelExit = this.getFirstEventTime(events, ObservationPoint.CODE_TO_MODEL_EXIT);
        this.checkValue(code2ModelExit, ObservationPoint.CODE_TO_MODEL_EXIT);
        final Long modelUpdateEntry = this.getFirstEventTime(events, ObservationPoint.MODEL_UPDATE_ENTRY);
        this.checkValue(modelUpdateEntry, ObservationPoint.MODEL_UPDATE_ENTRY);
        final Long modelUpdateExit = this.getFirstEventTime(events, ObservationPoint.MODEL_UPDATE_EXIT);
        this.checkValue(modelUpdateExit, ObservationPoint.MODEL_UPDATE_EXIT);
        final Long privacyWarnerEntry = this.getFirstEventTime(events, ObservationPoint.PRIVACY_WARNER_ENTRY);
        this.checkValue(privacyWarnerEntry, ObservationPoint.PRIVACY_WARNER_ENTRY);
        final Long privacyWarnerExit = this.getFirstEventTime(events, ObservationPoint.PRIVACY_WARNER_EXIT);
        this.checkValue(privacyWarnerExit, ObservationPoint.PRIVACY_WARNER_EXIT);
        final Long computeProbeConfigEntry = this.getFirstEventTime(events,
                ObservationPoint.COMPUTE_PROBE_CONFIGURATION_ENTRY);
        this.checkValue(computeProbeConfigEntry, ObservationPoint.COMPUTE_PROBE_CONFIGURATION_ENTRY);
        final Long computeProbeConfigExit = this.getFirstEventTime(events,
                ObservationPoint.COMPUTE_PROBE_CONFIGURATION_EXIT);
        this.checkValue(computeProbeConfigExit, ObservationPoint.COMPUTE_PROBE_CONFIGURATION_EXIT);
        final Long whiteListEntry = this.getFirstEventTime(events, ObservationPoint.WHITE_LIST_FILTER_ENTRY);
        this.checkValue(whiteListEntry, ObservationPoint.WHITE_LIST_FILTER_ENTRY);
        final Long whiteListExit = this.getFirstEventTime(events, ObservationPoint.WHITE_LIST_FILTER_EXIT);
        this.checkValue(whiteListExit, ObservationPoint.WHITE_LIST_FILTER_EXIT);
        final Long probe2ModelEntry = this.getFirstEventTime(events, ObservationPoint.PROBE_MODEL_TO_CODE_ENTRY);
        this.checkValue(probe2ModelEntry, ObservationPoint.PROBE_MODEL_TO_CODE_ENTRY);
        final Long probe2ModelExit = this.getFirstEventTime(events, ObservationPoint.PROBE_MODEL_TO_CODE_EXIT);
        this.checkValue(probe2ModelExit, ObservationPoint.PROBE_MODEL_TO_CODE_EXIT);

        if (probe2ModelExit != null) {

            Long controlProbeEntry = this.getFirstEventTime(events, ObservationPoint.CONTROL_PROBES_ENTRY);
            final long lastControlEvent;
            if (controlProbeEntry != null) {
                lastControlEvent = this.getLastEvent(events, ObservationPoint.CONTROL_PROBES_EXIT);
            } else {
                lastControlEvent = probe2ModelExit;
                controlProbeEntry = probe2ModelExit;
                this.logger.info("No entry on probe control. Therefore, configuration time is zero");
            }

            if (this.timeNormalization == null) {
                this.timeNormalization = eventTime;
            }

            /** compute total. */
            result.put(ComputeResults.TRIGGER_TIME, eventTime - this.timeNormalization);
            result.put(ComputeResults.TOTAL_REACTION_TIME, lastControlEvent - eventTime);
            result.put(ComputeResults.TOTAL_PROCESSING_TIME, lastControlEvent - entryTime);
            result.put(ComputeResults.MODEL_UPDATE, modelUpdateExit - code2ModelEntry);
            if (privacyWarnerExit != null && privacyWarnerEntry != null) {
                result.put(ComputeResults.PRIVACY_UPDATE, privacyWarnerExit - privacyWarnerEntry);
            }
            result.put(ComputeResults.CONFIGURATION_TIME, lastControlEvent - controlProbeEntry);

            this.outputPort.send(result);
        } else {
            this.logger.warn("Missing probe configuration model to code mapping.");
        }
    }

    private void checkValue(final Long timestamp, final ObservationPoint point) {
        if (timestamp == null) {
            this.logger.error("Missing time stamp for {}", point.name());
        }
    }

    private long getLastEvent(final List<MeasureEventOccurance> events, final ObservationPoint point) {
        Long last = null;
        for (final MeasureEventOccurance event : events) {
            if (event.getPoint().equals(point)) {
                if (last == null) {
                    last = event.getTimestamp();
                } else if (last < event.getTimestamp()) {
                    last = event.getTimestamp();
                }
            }
        }
        if (last != null) {
            return last;
        } else {
            throw new InternalError("Event type not found (last event). Missing " + point.name());
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

    private Long getFirstEventTime(final List<MeasureEventOccurance> events, final ObservationPoint point) {
        final MeasureEventOccurance event = this.getFirstEvent(events, point);
        if (event != null) {
            return event.getTimestamp();
        } else {
            return null;
        }
    }

    public OutputPort<Map<String, Long>> getOutputPort() {
        return this.outputPort;
    }

}
