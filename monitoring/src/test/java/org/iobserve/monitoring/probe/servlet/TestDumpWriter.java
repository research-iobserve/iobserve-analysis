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
package org.iobserve.monitoring.probe.servlet;

import java.util.ArrayList;
import java.util.List;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * @author Reiner Jung
 *
 */
public class TestDumpWriter extends AbstractMonitoringWriter {

    private static final List<IMonitoringRecord> STORAGE = new ArrayList<>();

    /**
     * Constructor.
     *
     * @param configuration
     *            the Kieker configuration which defines monitoring parameters.
     */
    public TestDumpWriter(final Configuration configuration) {
        super(configuration);
    }

    /*
     * (non-Javadoc)
     *
     * @see kieker.monitoring.writer.AbstractMonitoringWriter#onStarting()
     */
    @Override
    public void onStarting() {
        // Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * kieker.monitoring.writer.AbstractMonitoringWriter#writeMonitoringRecord(kieker.common.record.
     * IMonitoringRecord)
     */
    @Override
    public void writeMonitoringRecord(final IMonitoringRecord record) {
        synchronized (TestDumpWriter.STORAGE) {
            TestDumpWriter.STORAGE.add(record);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see kieker.monitoring.writer.AbstractMonitoringWriter#onTerminating()
     */
    @Override
    public void onTerminating() {
        // Auto-generated method stub

    }

    public static List<IMonitoringRecord> getRecords() {
        return TestDumpWriter.STORAGE;
    }

}
