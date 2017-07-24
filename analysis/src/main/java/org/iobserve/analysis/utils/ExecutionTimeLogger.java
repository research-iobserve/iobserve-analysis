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
package org.iobserve.analysis.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.IUndeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.iobserve.common.record.ServletUndeployedEvent;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;

/**
 *
 * @author unknown
 *
 */
public final class ExecutionTimeLogger {

    private static ExecutionTimeLogger instance;

    private final Map<Integer, Long> tmpTimes;

    private final List<LoggingEntry> deploymentTimes;
    private final List<LoggingEntry> undeploymentTimes;
    private final List<LoggingEntry> allocationTimes;
    private final List<LoggingEntry> entryCallTimes;
    private final List<LoggingEntry> entryCallSequenceTimes;

    public static ExecutionTimeLogger getInstance() {
        if (ExecutionTimeLogger.instance == null) {
            ExecutionTimeLogger.instance = new ExecutionTimeLogger();
        }

        return ExecutionTimeLogger.instance;
    }

    private ExecutionTimeLogger() {
        this.tmpTimes = new HashMap<>();
        this.deploymentTimes = new ArrayList<>();
        this.undeploymentTimes = new ArrayList<>();
        this.allocationTimes = new ArrayList<>();
        this.entryCallTimes = new ArrayList<>();
        this.entryCallSequenceTimes = new ArrayList<>();
    }

    public void startLogging(final IMonitoringRecord record) {
        this.tmpTimes.put(record.hashCode(), System.nanoTime());
    }

    public void stopLogging(final IDeploymentRecord record) {
        final Long startTime = this.tmpTimes.get(record.hashCode());
        if (startTime != null) {
            final Long endTime = System.nanoTime();
            final LoggingEntry entry = new LoggingEntry();
            if (record instanceof ServletDeployedEvent) {
                final ServletDeployedEvent event = (ServletDeployedEvent) record;
                entry.setLoggingInfo(event.getSerivce(), event.getContext(), endTime - startTime, startTime, endTime);
            } else if (record instanceof EJBDeployedEvent) {
                final EJBDeployedEvent event = (EJBDeployedEvent) record;
                entry.setLoggingInfo(event.getSerivce(), event.getContext(), endTime - startTime, startTime, endTime);
            }

            this.deploymentTimes.add(entry);
        }
    }

    public void stopLogging(final IUndeploymentRecord record) {
        final Long startTime = this.tmpTimes.get(record.hashCode());
        if (startTime != null) {
            final Long endTime = System.nanoTime();
            final LoggingEntry entry = new LoggingEntry();
            if (record instanceof ServletUndeployedEvent) {
                final ServletUndeployedEvent event = (ServletUndeployedEvent) record;
                entry.setLoggingInfo(event.getSerivce(), event.getContext(), endTime - startTime, startTime, endTime);
            } else if (record instanceof EJBUndeployedEvent) {
                final EJBUndeployedEvent event = (EJBUndeployedEvent) record;
                entry.setLoggingInfo(event.getSerivce(), event.getContext(), endTime - startTime, startTime, endTime);
            }

            this.undeploymentTimes.add(entry);
        }
    }

    public void stopLogging(final IAllocationRecord record) {
        final Long startTime = this.tmpTimes.get(record.hashCode());
        if (startTime != null) {
            final Long endTime = System.nanoTime();
            final LoggingEntry entry = new LoggingEntry();
            if (record instanceof ServletDeployedEvent) {
                final ServletDeployedEvent event = (ServletDeployedEvent) record;
                entry.setLoggingInfo(event.getSerivce(), event.getContext(), endTime - startTime, startTime, endTime);
            } else if (record instanceof EJBDeployedEvent) {
                final EJBDeployedEvent event = (EJBDeployedEvent) record;
                entry.setLoggingInfo(event.getSerivce(), event.getContext(), endTime - startTime, startTime, endTime);
            }

            this.allocationTimes.add(entry);
        }
    }

    public void stopLogging(final AfterOperationEvent record) {
        final Long startTime = this.tmpTimes.get(record.hashCode());
        if (startTime != null) {
            final Long endTime = System.nanoTime();

            final LoggingEntry entry = new LoggingEntry();
            entry.setLoggingInfo("", record.getOperationSignature(), endTime - startTime, startTime, endTime);

            this.entryCallTimes.add(entry);
        }
    }

    public void stopLogging(final EntryCallEvent record) {
        final Long startTime = this.tmpTimes.get(record.hashCode());
        if (startTime != null) {
            final Long endTime = System.nanoTime();

            final LoggingEntry entry = new LoggingEntry();
            entry.setLoggingInfo(record.getSessionId(), record.getOperationSignature(), endTime - startTime, startTime,
                    endTime);

            this.entryCallSequenceTimes.add(entry);
        }
    }

    public void exportAsCsv() {
        this.export(Arrays.asList("Service", "Context", "elapsed", "start", "end"), this.allocationTimes,
                "TAllocation");
        this.export(Arrays.asList("Service", "Context", "elapsed", "start", "end"), this.deploymentTimes,
                "TDeployment");
        this.export(Arrays.asList("Service", "Context", "elapsed", "start", "end"), this.undeploymentTimes,
                "TUndeployment");
        this.export(Arrays.asList("", "OperationSignature", "elapsed", "start", "end"), this.entryCallTimes,
                "TEntryCall");
        this.export(Arrays.asList("SessionId", "OperationSignature", "elapsed", "start", "end"),
                this.entryCallSequenceTimes, "TEntryCallSequence");
    }

    private void export(final List<String> headlines, final List<LoggingEntry> list, final String mapName) {
        final CsvExporter exporter = new CsvExporter(mapName + "Logging.csv");
        exporter.setHeadline(mapName);
        exporter.addRow(headlines);

        for (final LoggingEntry entry : list) {
            exporter.addRow(entry.asList());
        }

        exporter.export();
    }

    private class LoggingEntry {

        private String sessionId;
        private String entityId;
        private long elapsedTime = 0;
        private long startTime = 0;
        private long endTime = 0;

        public void setLoggingInfo(final String sessionId, final String entityId, final long elapsedTime,
                final long startTime, final long endTime) {
            this.sessionId = sessionId;
            this.entityId = entityId;
            this.elapsedTime = elapsedTime;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public List<Object> asList() {
            final List<Object> entryContent = new ArrayList<>();
            entryContent.add(this.sessionId);
            entryContent.add(this.entityId);
            entryContent.add(this.elapsedTime);
            entryContent.add(this.startTime);
            entryContent.add(this.endTime);
            return entryContent;
        }
    }
}
