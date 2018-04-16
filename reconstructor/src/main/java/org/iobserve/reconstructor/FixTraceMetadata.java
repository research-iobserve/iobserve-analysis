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
package org.iobserve.reconstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.flow.trace.TraceMetadata;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Delay ITraceRecords until the corresponding TraceMetadata record shows up.
 *
 * @author Reiner Jung
 *
 */
public class FixTraceMetadata extends AbstractConsumerStage<IMonitoringRecord> {

    private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort(IMonitoringRecord.class);

    private final Map<Long, Long> traces = new HashMap<>();
    private final Map<Long, List<IMonitoringRecord>> stash = new HashMap<>();

    @Override
    protected void execute(final IMonitoringRecord event) throws Exception {
        if (event instanceof TraceMetadata) {
            this.traces.put(((TraceMetadata) event).getTraceId(), event.getLoggingTimestamp());
            this.outputPort.send(event);

            final List<IMonitoringRecord> keptRecords = this.stash.get(((TraceMetadata) event).getTraceId());
            if (keptRecords != null) {
                for (final IMonitoringRecord record : keptRecords) {
                    this.outputPort.send(record);
                }

            }
        } else if (event instanceof ITraceRecord) {
            final Long time = this.traces.get(((ITraceRecord) event).getTraceId());
            if (time != null) {
                this.traces.put(((ITraceRecord) event).getTraceId(), event.getLoggingTimestamp());
                this.outputPort.send(event);
            } else {
                List<IMonitoringRecord> keptRecords = this.stash.get(((ITraceRecord) event).getTraceId());
                if (keptRecords == null) {
                    keptRecords = new ArrayList<>();
                }
                keptRecords.add(event);
                this.stash.put(((ITraceRecord) event).getTraceId(), keptRecords);
            }
        } else {
            this.outputPort.send(event);
        }
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

}
