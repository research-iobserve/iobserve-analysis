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

import java.util.HashSet;
import java.util.Set;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.TraceMetadata;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.common.record.SessionStartEvent;

/**
 * The filter detects when a new session is created and synthesizes and SessionStart event before
 * the event creating the session.
 *
 * @author Reiner Jung
 *
 * @since 0.0.3
 */
public class StartSessionDetector extends AbstractConsumerStage<IMonitoringRecord> {

    private final OutputPort<IMonitoringRecord> outputPort = this.createOutputPort(IMonitoringRecord.class);
    private final Set<String> sessionRegister = new HashSet<>();

    public StartSessionDetector() {

    }

    @Override
    protected void execute(final IMonitoringRecord event) throws Exception {
        // System.err.println("++ " + event.getClass().getCanonicalName() + " " + event.toString());
        if (event instanceof TraceMetadata) {
            final TraceMetadata traceMetadata = (TraceMetadata) event;
            if (!this.sessionRegister.stream().anyMatch(key -> key.equals(traceMetadata.getSessionId()))) {
                this.sessionRegister.add(traceMetadata.getSessionId());
                this.outputPort.send(new SessionStartEvent(traceMetadata.getLoggingTimestamp(),
                        traceMetadata.getHostname(), traceMetadata.getSessionId()));
            }
        }

        this.outputPort.send(event);
    }

    public OutputPort<IMonitoringRecord> getOutputPort() {
        return this.outputPort;
    }

}
