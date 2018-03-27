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

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.ITraceRecord;

/**
 * @author Reiner Jung
 *
 */
public class Pair {

    private final IMonitoringRecord first;

    private final ITraceRecord last;

    public Pair(final IMonitoringRecord first, final ITraceRecord last) {
        this.first = first;
        this.last = last;
    }

    public final IMonitoringRecord getFirst() {
        return this.first;
    }

    public final ITraceRecord getLast() {
        return this.last;
    }

}
