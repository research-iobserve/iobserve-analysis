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
package org.iobserve.collector;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.AbstractAsyncWriter;
import kieker.monitoring.writer.filesystem.AbstractAsyncFSWriter;
import kieker.monitoring.writer.filesystem.AsyncFsWriter;

import teetime.framework.AbstractConsumerStage;

/**
 * Sync all incoming records with a Kieker writer to a text file log.
 *
 * @author Reiner Jung
 *
 */
public class WallStage extends AbstractConsumerStage<IMonitoringRecord> {

    private static final String WRITER_NAME = "kieker.monitoring.writer.filesystem.AsyncFsWriter";

    private final IMonitoringController ctrl;

    private long count = 0;

    /**
     * Configure and setup the Kieker writer.
     */
    public WallStage(final String dataLocation) {
        final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
        configuration.setProperty(ConfigurationFactory.CONTROLLER_NAME, "iObserve-Experiments");
        configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, WallStage.WRITER_NAME);

        configuration.setProperty(WallStage.WRITER_NAME + "." + AbstractAsyncFSWriter.CONFIG_PATH, dataLocation);
        configuration.setProperty(WallStage.WRITER_NAME + "." + AbstractAsyncFSWriter.CONFIG_MAXENTRIESINFILE, "25000");
        configuration.setProperty(WallStage.WRITER_NAME + "." + AbstractAsyncFSWriter.CONFIG_MAXLOGSIZE, "-1");
        configuration.setProperty(WallStage.WRITER_NAME + "." + AbstractAsyncFSWriter.CONFIG_MAXLOGFILES, "-1");
        configuration.setProperty(WallStage.WRITER_NAME + "." + AsyncFsWriter.CONFIG_FLUSH, "true");
        configuration.setProperty(WallStage.WRITER_NAME + "." + AsyncFsWriter.CONFIG_BUFFER, "8192");
        configuration.setProperty(WallStage.WRITER_NAME + "." + AbstractAsyncWriter.CONFIG_QUEUESIZE, "10000");
        configuration.setProperty(WallStage.WRITER_NAME + "." + AbstractAsyncWriter.CONFIG_PRIORITIZED_QUEUESIZE,
                "100");
        configuration.setProperty(WallStage.WRITER_NAME + "." + AbstractAsyncWriter.CONFIG_BEHAVIOR, "1");
        configuration.setProperty(WallStage.WRITER_NAME + "." + AbstractAsyncWriter.CONFIG_SHUTDOWNDELAY, "-1");

        System.out.println("Configuration complete");

        this.ctrl = MonitoringController.createInstance(configuration);
    }

    @Override
    protected void execute(final IMonitoringRecord record) {
        this.count++;
        this.ctrl.newMonitoringRecord(record);
        if ((this.count % 1000) == 0) {
            System.out.println("Saved " + this.count + " records");
        }
    }

    public long getCount() {
        return this.count;
    }

}
