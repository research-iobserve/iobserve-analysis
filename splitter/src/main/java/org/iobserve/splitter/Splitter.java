/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.splitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.misc.KiekerMetadataRecord;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The record switch filter is used to scan the event stream and send events based on their type to
 * different output ports.
 *
 * @author Reiner Jung
 *
 */
public class Splitter extends AbstractConsumerStage<IMonitoringRecord> {

    private final List<OutputPort<IMonitoringRecord>> outputPorts = new ArrayList<>();

    private final Map<Long, TraceMetadata> traceRegisterMap = new HashMap<>();

    /** Statistics. */
    private int recordCount;

    private final String[] hostnames;
    private static final Logger LOGGER = LoggerFactory.getLogger(Splitter.class);

    /**
     * Splitter constructor.
     *
     * @param hostnames
     *            array of host names
     */
    public Splitter(final String[] hostnames) {
        this.hostnames = hostnames;
        final int numOfPorts = hostnames.length;
        for (int i = 0; i < numOfPorts; i++) {
            this.outputPorts.add(this.createOutputPort());
        }
    }

    @Override
    protected void execute(final IMonitoringRecord element) {
        this.recordCount++;
        if (element instanceof TraceMetadata) {
            final TraceMetadata traceMetadata = (TraceMetadata) element;
            this.traceRegisterMap.put(traceMetadata.getTraceId(), traceMetadata);

            boolean send = false;
            for (int i = 0; i < this.hostnames.length; i++) {
                if (this.hostnames[i].equals(traceMetadata.getHostname())) {
                    this.outputPorts.get(i).send(element);
                    send = true;
                }
            }
            if (!send) {
                for (int i = 0; i < this.hostnames.length; i++) {
                    this.outputPorts.get(i).send(element);
                }
            }
        } else if (element instanceof ITraceRecord) {
            final TraceMetadata metadata = this.traceRegisterMap.get(((ITraceRecord) element).getTraceId());

            boolean send = false;
            for (int i = 0; i < this.hostnames.length; i++) {
                if (this.hostnames[i].equals(metadata.getHostname())) {
                    this.outputPorts.get(i).send(element);
                    send = true;
                }
            }
            if (!send) {
                for (int i = 0; i < this.hostnames.length; i++) {
                    this.outputPorts.get(i).send(element);
                }
            }
        } else if (element instanceof KiekerMetadataRecord) {
            /** ignore. */
            Splitter.LOGGER.debug("Metadata record {}.", element);
        } else {
            for (int i = 0; i < this.hostnames.length; i++) {
                this.outputPorts.get(i).send(element);
            }
        }
    }

    /**
     * @return all output ports
     */
    public final List<OutputPort<IMonitoringRecord>> getAllOutputPorts() {
        return this.outputPorts;
    }

    public long getRecordCount() {
        return this.recordCount;
    }
}
