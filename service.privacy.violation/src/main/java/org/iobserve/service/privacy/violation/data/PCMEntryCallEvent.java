/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.service.privacy.violation.data;

import org.iobserve.analysis.protocom.PcmEntity;

/**
 * @author Reiner Jung
 *
 */
public class PCMEntryCallEvent {
    private final long entryTime;
    private final long exitTime;
    private final PcmEntity entity;

    private final String sessionId;
    private final String hostname;

    /**
     * Create a new PCM entry call.
     *
     * @param entryTime
     *            entry time
     * @param exitTime
     *            exit time
     * @param entity
     *            related pcm entity
     * @param sessionId
     *            session id
     * @param hostname
     *            hostname
     */
    public PCMEntryCallEvent(final long entryTime, final long exitTime, final PcmEntity entity, final String sessionId,
            final String hostname) {
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.entity = entity;
        this.sessionId = sessionId;
        this.hostname = hostname;
    }

    public final long getEntryTime() {
        return this.entryTime;
    }

    public final long getExitTime() {
        return this.exitTime;
    }

    public final PcmEntity getEntity() {
        return this.entity;
    }

    public final String getSessionId() {
        return this.sessionId;
    }

    public final String getHostname() {
        return this.hostname;
    }

}
