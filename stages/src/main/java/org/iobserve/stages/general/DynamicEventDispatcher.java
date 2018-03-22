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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Dynamic replacement for a normal record switch.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class DynamicEventDispatcher extends AbstractConsumerStage<IMonitoringRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicEventDispatcher.class);

    private static final int LOOP_COUNT = 1000;

    /** Internal list for class types. */
    private final List<Class<? extends IMonitoringRecord>> classTypeList = new ArrayList<>();

    /** Internal list output ports. */
    private final List<OutputPort<? extends IMonitoringRecord>> outputPortList = new ArrayList<>();

    private final boolean countEvents;
    private long eventCount;

    /** internal map to collect unknown record types. */
    private final Map<String, Integer> unknownRecords = new ConcurrentHashMap<>();

    private final boolean reportUnknown;

    private final boolean outputOther;

    private final OutputPort<IMonitoringRecord> outputOtherPort = this.createOutputPort();

    /**
     * Create a new dynamic dispatcher.
     *
     * @param countEvents
     *            flag to activate event counting
     * @param reportUnknown
     *            report on unknown event types
     * @param outputOther
     *            provide an output port for events not send to other ports
     */
    public DynamicEventDispatcher(final boolean countEvents, final boolean reportUnknown, final boolean outputOther) {
        this.countEvents = countEvents;
        this.reportUnknown = reportUnknown;
        this.outputOther = outputOther;
    }

    @Override
    protected void execute(final IMonitoringRecord event) throws Exception {
        if (this.countEvents) {
            this.eventCount++;
        }

        final OutputPort<IMonitoringRecord> selectedOutputPort = this.selectOutputPort(event.getClass());
        if (selectedOutputPort != null) {
            selectedOutputPort.send(event);
        } else {
            if (this.outputOther) {
                this.outputOtherPort.send(event);
            }
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

    @SuppressWarnings("unchecked")
    private OutputPort<IMonitoringRecord> selectOutputPort(final Class<? extends IMonitoringRecord> clazz) {
        for (int i = 0; i < this.classTypeList.size(); i++) {
            final Class<? extends IMonitoringRecord> type = this.classTypeList.get(i);
            if (type.isAssignableFrom(clazz)) {
                return (OutputPort<IMonitoringRecord>) this.outputPortList.get(i);
            }
        }

        return null;
    }

    /**
     * Register a new type to automatically create a new output port.
     *
     * @param type
     *            record event type
     */
    public void registerOutput(final Class<? extends IMonitoringRecord> type) {
        this.classTypeList.add(type);
        this.outputPortList.add(this.createOutputPort(type));
    }

    /**
     * Return the proper output port for a specific class or interface.
     *
     * @param type
     *            the class or interface used to select the port
     *
     * @param <T>
     *            class or interface type for the corresponding port
     *
     * @return returns the output port or null on error
     */
    @SuppressWarnings("unchecked")
    public <T extends IMonitoringRecord> OutputPort<T> getOutputPort(final Class<T> type) {
        for (int i = 0; i < this.classTypeList.size(); i++) {
            final Class<? extends IMonitoringRecord> classType = this.classTypeList.get(i);
            if (classType.equals(type)) {
                return (OutputPort<T>) this.outputPortList.get(i);
            }
        }

        return null;
    }

    public long getEventCount() {
        return this.eventCount;
    }

    @Override
    public void onTerminating() {
        DynamicEventDispatcher.LOGGER.info("Records processed in total {}.", this.eventCount);
        super.onTerminating();
    }

}
