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
package org.iobserve.splitter;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.filesystem.AsciiFileWriter;
import teetime.framework.AbstractConsumerStage;

/**
 * Sync all incoming records with a Kieker writer to a text file log.
 *
 * @author Reiner Jung
 *
 */
public class DataDumpStage extends AbstractConsumerStage<IMonitoringRecord> {

    private static final String WRITER_NAME = AsciiFileWriter.class.getCanonicalName();

    private final IMonitoringController ctrl;

    private long count = 0;

    /**
     * Configure and setup the Kieker writer.
     *
     * @param dataLocation
     *            data location
     */
    public DataDumpStage(final String dataLocation, final String hostname) {
        final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
        configuration.setProperty(ConfigurationFactory.CONTROLLER_NAME, "iObserve-Experiments");
        configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DataDumpStage.WRITER_NAME);

        configuration.setProperty("kieker.monitoring.hostname", hostname);

        configuration.setProperty(AsciiFileWriter.CONFIG_CHARSET_NAME, "UTF-8");
        configuration.setProperty(AsciiFileWriter.CONFIG_FLUSH, "true");
        configuration.setProperty(AsciiFileWriter.CONFIG_MAXENTRIESINFILE, "25000");
        configuration.setProperty(AsciiFileWriter.CONFIG_MAXLOGFILES, "-1");
        configuration.setProperty(AsciiFileWriter.CONFIG_MAXLOGSIZE, "-1");
        configuration.setProperty(AsciiFileWriter.CONFIG_PATH, dataLocation);
        configuration.setProperty(AsciiFileWriter.CONFIG_SHOULD_COMPRESS, "false");

        System.out.println("Configuration complete");

        this.ctrl = MonitoringController.createInstance(configuration);
    }

    @Override
    protected void execute(final IMonitoringRecord record) {
        this.count++;
        this.ctrl.newMonitoringRecord(record);
        if (this.count % 1000 == 0) {
            System.out.println("Saved " + this.count + " records");
        }
    }

    public long getCount() {
        return this.count;
    }

}
