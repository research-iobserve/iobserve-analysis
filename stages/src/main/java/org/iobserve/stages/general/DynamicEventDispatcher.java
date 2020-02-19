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
package org.iobserve.stages.general;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kieker.common.exception.ConfigurationException;
import kieker.common.record.flow.IEventRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.ObservationPoint;
import org.iobserve.stages.data.ExperimentLoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The DynamicEventDispatcher allows to select specific events from the event stream and send them
 * to a specific event stream.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class DynamicEventDispatcher extends AbstractConsumerStage<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicEventDispatcher.class);

    private static final int LOOP_COUNT = 1000;

    private final boolean countEvents;
    private long eventCount;

    /** internal map to collect unknown record types. */
    private final Map<String, Integer> unknownRecords = new ConcurrentHashMap<>();

    private final boolean reportUnknown;

    private final boolean outputOther;

    private final OutputPort<Object> outputOtherPort = this.createOutputPort();

    private IEventMatcher<? extends Object> rootEventMatcher;

    private IEventMatcher<? extends Object> defaultMatcher;

    /**
     * Create a new dynamic dispatcher.
     *
     * @param rootEventMatcher
     *            first matcher in a sequence of matchers used for this dispatcher
     * @param countEvents
     *            flag to activate event counting
     * @param reportUnknown
     *            report on unknown event types
     * @param outputOther
     *            provide an output port for events not send to other ports
     */
    public DynamicEventDispatcher(final IEventMatcher<? extends Object> rootEventMatcher, final boolean countEvents,
            final boolean reportUnknown, final boolean outputOther) {
        if (rootEventMatcher == null) {
            this.defaultMatcher = new AllEventNoRelayMatcher();
            this.rootEventMatcher = this.defaultMatcher;
        } else {
            this.rootEventMatcher = rootEventMatcher;
            this.assignPorts(rootEventMatcher);
        }
        this.countEvents = countEvents;
        this.reportUnknown = reportUnknown;
        this.outputOther = outputOther;
    }

    private void assignPorts(final IEventMatcher<? extends Object> eventMatcher) {
        eventMatcher.setOutputPort(this.createOutputPort());
        if (eventMatcher.getNextMatcher() != null) {
            this.assignPorts(eventMatcher.getNextMatcher());
        }
    }

    @Override
    protected void execute(final Object event) throws Exception {
        if (this.countEvents) {
            this.eventCount++;
        }

        @SuppressWarnings("unchecked")
        final OutputPort<Object> selectedOutputPort = (OutputPort<Object>) this.selectOutputPort(this.rootEventMatcher,
                event);
        if (selectedOutputPort != null) {
            // collecting event observation time (if possible); only for performance tests.
            if (event instanceof IEventRecord) {
                final IEventRecord specialEvent = (IEventRecord) event;
                ExperimentLoggingUtils.measureDeploymentEvent(specialEvent, ObservationPoint.EVENT_CREATION_TIME);
                ExperimentLoggingUtils.measureDeploymentEvent(specialEvent, ObservationPoint.DISPATCHER_ENTRY);
            }
            selectedOutputPort.send(event);
        } else {
            if (this.reportUnknown) {
                final String className = event.getClass().getCanonicalName();
                Integer hits = this.unknownRecords.get(className);
                if (hits == null) {
                    DynamicEventDispatcher.LOGGER.warn("Configuration error: New unknown event type {}.", className);
                    this.unknownRecords.put(className, Integer.valueOf(1));
                } else {
                    hits++;
                    this.unknownRecords.put(className, hits);
                    if (hits % DynamicEventDispatcher.LOOP_COUNT == 0) {
                        DynamicEventDispatcher.LOGGER.warn("Event occurances {} of unknown eventtype {}.", hits,
                                className);
                    }
                }
            }
        }
    }

    private OutputPort<? extends Object> selectOutputPort(final IEventMatcher<? extends Object> eventMatcher,
            final Object event) {
        if (eventMatcher.matchEvent(event)) {
            return eventMatcher.getOutputPort();
        } else {
            final IEventMatcher<? extends Object> nextMatcher = eventMatcher.getNextMatcher();
            if (nextMatcher != null) {
                return this.selectOutputPort(nextMatcher, event);
            } else if (this.outputOther) {
                return this.outputOtherPort;
            } else {
                return null;
            }
        }
    }

    public long getEventCount() {
        return this.eventCount;
    }

    @Override
    public void onTerminating() {
        DynamicEventDispatcher.LOGGER.info("Records processed in total {}.", this.eventCount);
        super.onTerminating();
    }

    /**
     * Register an {@link IEventMatcher} and add it to the sequence of event matchers.
     *
     * @param leaveEventMatcher
     *            new leave event matcher
     * @throws ConfigurationException
     *             on error
     */
    public void registerOutput(final IEventMatcher<? extends Object> leaveEventMatcher) throws ConfigurationException {
        leaveEventMatcher.setOutputPort(this.createOutputPort());
        if (this.rootEventMatcher == this.defaultMatcher) {
            this.rootEventMatcher = leaveEventMatcher;
        } else {
            IEventMatcher<? extends Object> eventMatcher = this.rootEventMatcher;
            while (eventMatcher.getNextMatcher() != null) {
                eventMatcher = eventMatcher.getNextMatcher();
            }
            eventMatcher.setNextMatcher(leaveEventMatcher);
        }
    }

}
