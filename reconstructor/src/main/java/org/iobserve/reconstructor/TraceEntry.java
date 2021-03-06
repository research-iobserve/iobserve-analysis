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

import kieker.common.record.flow.trace.TraceMetadata;

/**
 * @author Reiner Jung
 *
 */
public class TraceEntry {

    private String sessionId;
    private long timestamp;
    private final long traceId;

    public TraceEntry(final TraceMetadata data) {
        this.sessionId = data.getSessionId();
        this.timestamp = data.getLoggingTimestamp();
        this.traceId = data.getTraceId();
    }

    public final String getSessionId() {
        return this.sessionId;
    }

    public final void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    public final long getTimestamp() {
        return this.timestamp;
    }

    public final void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTraceId() {
        return this.traceId;
    }

}
