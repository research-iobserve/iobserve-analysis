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
package org.iobserve.replayer;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.tcp.SingleSocketTcpWriter;
import teetime.framework.AbstractConsumerStage;

/**
 * Send data via TCP.
 *
 * @author Reiner Jung
 *
 */
public class DataSendStage extends AbstractConsumerStage<IMonitoringRecord> {

    private static final String WRITER_NAME = SingleSocketTcpWriter.class.getCanonicalName();

    private final IMonitoringController ctrl;

    private long count = 0;

    /**
     * Configure and setup the Kieker writer.
     *
     * @param hostname host where the data is send to
     * @param port port where the data is send to
     */
    public DataSendStage(final String hostname, final int port) {
        final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
        configuration.setProperty(ConfigurationFactory.CONTROLLER_NAME, "iObserve-Experiments");
        configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DataSendStage.WRITER_NAME);


        configuration.setProperty(SingleSocketTcpWriter.CONFIG_FLUSH, "true");
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_BUFFERSIZE, "25000");
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_HOSTNAME, hostname);
        configuration.setProperty(SingleSocketTcpWriter.CONFIG_PORT, port);

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

    public boolean isOutputConnected() {
        return !this.ctrl.isMonitoringTerminated();
    }
}