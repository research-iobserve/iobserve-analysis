/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.stages.sink;

import java.util.HashMap;
import java.util.Map;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;

/**
 * Act like a null device.
 *
 * @author Reiner Jung
 *
 */
public class NullStage extends AbstractConsumerStage<IMonitoringRecord> {

    private long count;
    private final boolean silent;

    private final Map<Class<? extends IMonitoringRecord>, Integer> types = new HashMap<>();

    /**
     * Null stage.
     *
     * @param silent
     *            silent operations.
     */
    public NullStage(final boolean silent) {
        this.silent = silent;
    }

    @Override
    protected void execute(final IMonitoringRecord record) {
        if (!this.silent) {
            Integer counter = this.types.get(record.getClass());
            if (counter == null) {
                counter = 1;
                this.types.put(record.getClass(), counter);
            } else {
                counter++;
            }

            this.count++;
            if (this.count % 100000 == 0) {
                this.logger.info("{} records received.", this.count);
            }
        }
    }

    public long getCount() {
        return this.count;
    }

    public Map<Class<? extends IMonitoringRecord>, Integer> getTypes() {
        return this.types;
    }

}
