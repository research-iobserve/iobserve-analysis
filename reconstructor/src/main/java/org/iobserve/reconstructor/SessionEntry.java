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

import org.iobserve.common.record.SessionStartEvent;

/**
 * @author Reiner Jung
 *
 */
public class SessionEntry {

    private String hostname;

    private long timestamp;

    private String sessionId;

    public SessionEntry(final SessionStartEvent event) {
        this.hostname = event.getHostname();
        this.timestamp = event.getLoggingTimestamp();
        this.sessionId = event.getSessionId();
    }

    public final String getHostname() {
        return this.hostname;
    }

    public final void setHostname(final String hostname) {
        this.hostname = hostname;
    }

    public final long getTimestamp() {
        return this.timestamp;
    }

    public final void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    public final String getSessionId() {
        return this.sessionId;
    }

    public final void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

}
