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

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import teetime.framework.AbstractConsumerStage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sync all incoming records with a Kieker writer to a text file log.
 *
 * @author Reiner Jung
 *
 */
public class DataSinkStage extends AbstractConsumerStage<IMonitoringRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSinkStage.class);

    private final IMonitoringController ctrl;

    private long count;

    /**
     * Configure and setup the Kieker writer.
     *
     * @param configuration
     *            kieker configuration containing the dump stage writer setup
     */
    public DataSinkStage(final Configuration configuration) {
        DataSinkStage.LOGGER.debug("Configuration complete.");

        this.ctrl = MonitoringController.createInstance(configuration);
    }

    @Override
    protected void execute(final IMonitoringRecord record) {
        this.count++;
        this.ctrl.newMonitoringRecord(record);
        if (this.count % 100000 == 0) {
            DataSinkStage.LOGGER.info("Saved {} records.", this.count);
        }
    }

    public long getCount() {
        return this.count;
    }

}
