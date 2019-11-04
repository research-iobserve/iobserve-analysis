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
import org.iobserve.analysis.filter.TEntryCallSequence;
import org.iobserve.analysis.filter.TEntryEventSequence;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.userbehavior.UserBehaviorTransformation;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.IUndeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.iobserve.common.record.ServletUndeployedEvent;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;
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
    private final List<LoggingEntry> entryEventSequenceTimes;
    private final List<LoggingEntry> userBehaviorTransformationTimes;
    
    private final List<LoggingEntry> recordSwitchEntryCallPipelineTimes;
    private final List<LoggingEntry> entryCallEntryCallSequencePipelineTimes;
    private final List<LoggingEntry> entryCallSequenceEntryEventPipelineTimes;

    public static ExecutionTimeLogger getInstance() {
        if (ExecutionTimeLogger.instance == null) {
            ExecutionTimeLogger.instance = new ExecutionTimeLogger();
        }

        return ExecutionTimeLogger.instance;
    }
    
    public static void resetTimes() {
        ExecutionTimeLogger.instance = null;
    }

    private ExecutionTimeLogger() {
        this.tmpTimes = new HashMap<>();
        this.deploymentTimes = new ArrayList<>();
        this.undeploymentTimes = new ArrayList<>();
        this.allocationTimes = new ArrayList<>();
        this.entryCallTimes = new ArrayList<>();
        this.entryCallSequenceTimes = new ArrayList<>();
        this.entryEventSequenceTimes = new ArrayList<>();
        this.userBehaviorTransformationTimes = new ArrayList<>();
        this.recordSwitchEntryCallPipelineTimes = new ArrayList<>();
        this.entryCallEntryCallSequencePipelineTimes = new ArrayList<>();
        this.entryCallSequenceEntryEventPipelineTimes = new ArrayList<>();
    }

    public void startLogging(final IMonitoringRecord record) {
        this.tmpTimes.put(record.hashCode(), System.nanoTime());
    }
    
    public void startLogging(final EntryCallSequenceModel session) {
        this.tmpTimes.put(session.hashCode(), System.currentTimeMillis());
    }
    
    public void startLogging(final UserBehaviorTransformation transformation) {
        this.tmpTimes.put(transformation.hashCode(), System.currentTimeMillis());
    }
    
    public void stopLogging(IFlowRecord record) {
    	final Long endTime = System.nanoTime();
        final Long startTime = this.tmpTimes.remove(record.hashCode());
        if (startTime != null) {
        	final LoggingEntry entry = new LoggingEntry();
            entry.setLoggingInfo(endTime - startTime, -1, -1, -1, -1);
            this.recordSwitchEntryCallPipelineTimes.add(entry);
        }
    }
    
    public void stopLogging(EntryCallEvent event) {
    	final Long endTime = System.nanoTime();
        final Long startTime = this.tmpTimes.remove(event.hashCode());
        if (startTime != null) {
        	final LoggingEntry entry = new LoggingEntry();
            entry.setLoggingInfo(endTime - startTime, -1, -1, -1, -1);
            this.entryCallEntryCallSequencePipelineTimes.add(entry);
        }
    }
    
    public void stopLogging(final EntryCallSequenceModel session) {
        final Long endTime = System.currentTimeMillis();
        final Long startTime = this.tmpTimes.remove(session.hashCode());
        if (startTime != null) {
            final LoggingEntry entry = new LoggingEntry();
            entry.setLoggingInfo(endTime - startTime, -1,-1,-1,-1);
            this.entryCallSequenceEntryEventPipelineTimes.add(entry);
        }
    }
    
    public void stopLogging(final UserBehaviorTransformation transformation) {
        final Long endTime = System.currentTimeMillis();
        final Long startTime = this.tmpTimes.remove(transformation.hashCode());
        if (startTime != null) {
            final LoggingEntry entry = new LoggingEntry();
            
            transformation.getResponseTimeOfUserGroupExtraction();
            transformation.getResponseTimeOfBranchExtraction();
            transformation.getResponseTimeOfLoopExtraction();
            transformation.getResponseTimeOfPcmModelling();
            
            entry.setLoggingInfo(transformation.getResponseTimeOfUserGroupExtraction(), 
                    transformation.getResponseTimeOfBranchExtraction(), 
                    transformation.getResponseTimeOfLoopExtraction(), 
                    transformation.getResponseTimeOfPcmModelling(), endTime - startTime);
            this.userBehaviorTransformationTimes.add(entry);
        }
    }

    public void stopLogging(final EntryCallSequenceModel session, TEntryEventSequence sequence) {
        final Long endTime = System.currentTimeMillis();
        final Long startTime = this.tmpTimes.remove(session.hashCode());
        if (startTime != null) {
            final LoggingEntry entry = new LoggingEntry();
            entry.setLoggingInfo(endTime - startTime, sequence.getModelLoadTime(), sequence.getUserBehaviorTransformationTime(), sequence.getUpdateModelTime(), sequence.getModelSaveTime());
            this.entryEventSequenceTimes.add(entry);
            // resetting the times
            sequence.setModelLoadTime(-1);
            sequence.setUserBehaviorTransformationTime(0);
            sequence.setUpdateModelTime(0);
            sequence.setModelSaveTime(0);
        }
    }
    
    public void stopLogging(final EntryCallEvent record, TEntryCallSequence filter) {
        final Long endTime = System.nanoTime();
        final Long startTime = this.tmpTimes.remove(record.hashCode());
        if (startTime != null) {
            final LoggingEntry entry = new LoggingEntry();
            entry.setLoggingInfo(record.getOperationSignature(), Long.toString(filter.getSessionCreationTime()),
                    filter.getApprovedSessionsListCreationTime(), filter.getEntryCallSequenceModelCreationTime(), endTime - startTime);

            this.entryCallSequenceTimes.add(entry);
            
            filter.setSessionCreationTime(0);
            filter.setApprovedSessionsListCreationTime(0);
        }
    }

    public void stopLogging(final IDeploymentRecord record) {
        final Long endTime = System.nanoTime();
        final Long startTime = this.tmpTimes.remove(record.hashCode());
        if (startTime != null) {
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
        final Long endTime = System.nanoTime();
        final Long startTime = this.tmpTimes.remove(record.hashCode());
        if (startTime != null) {
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
        final Long endTime = System.currentTimeMillis();
        final Long startTime = this.tmpTimes.remove(record.hashCode());
        if (startTime != null) {
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
        final Long endTime = System.nanoTime();
        final Long startTime = this.tmpTimes.remove(record.hashCode());
        if (startTime != null) {
            final LoggingEntry entry = new LoggingEntry();
            entry.setLoggingInfo("", record.getOperationSignature(), endTime - startTime, startTime, endTime);

            this.entryCallTimes.add(entry);
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
        this.export(Arrays.asList("OperationSignature", "sessionCreation", "approvedListCreation", "entryCallSequenceModelCreation", "elapsed"),
                this.entryCallSequenceTimes, "TEntryCallSequence");
        this.export(Arrays.asList("overall elapsed", "model load", "behavior transf", "model update","model save"),
                this.entryEventSequenceTimes, "TEntryEventSequence");
        this.export(Arrays.asList("UserGroupExtraction", "BranchDetection", "LoopDetection", "PcmModelBuild", "Elapsed"),
                this.userBehaviorTransformationTimes, "UserBehaviorTransformation");
        
        this.export(Arrays.asList("Send to Execute Time", "", "", "", ""),
                this.recordSwitchEntryCallPipelineTimes, "RecordSwitchEntryCallPipeline");
        this.export(Arrays.asList("Send to Execute Time", "", "", "", ""),
                this.entryCallEntryCallSequencePipelineTimes, "EntryCallEntryCallSequencePipeline");
        this.export(Arrays.asList("Send to Execute Time", "", "", "", ""),
                this.entryCallSequenceEntryEventPipelineTimes, "EntryCallSequenceEntryEventPipeline");
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
        
        public void setLoggingInfo(final long info1, final long info2, final long info3, final long info4, final long info5) {
            this.sessionId = Long.toString(info1);
            this.entityId = Long.toString(info2);
            this.elapsedTime = info3;
            this.startTime = info4;
            this.endTime = info5;
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
