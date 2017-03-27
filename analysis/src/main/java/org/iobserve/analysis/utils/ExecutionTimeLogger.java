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

public class ExecutionTimeLogger {
	
	private static ExecutionTimeLogger instance;
	
	private Map<Integer,Long> tmpTimes;
	
	private List<LoggingEntry> deploymentTimes;
	private List<LoggingEntry> undeploymentTimes;
	private List<LoggingEntry> allocationTimes;
	private List<LoggingEntry> entryCallTimes;
	private List<LoggingEntry> entryCallSequenceTimes;
	
	public static ExecutionTimeLogger getInstance() {
		if(instance == null) {
			instance = new ExecutionTimeLogger();
		}
		
		return instance;
	}
	
	private ExecutionTimeLogger() {
		tmpTimes = new HashMap<>();
		deploymentTimes = new ArrayList<>();
		undeploymentTimes = new ArrayList<>();
		allocationTimes = new ArrayList<>();
		entryCallTimes = new ArrayList<>();
		entryCallSequenceTimes = new ArrayList<>();
	}
	
	public void startLogging(final IMonitoringRecord record) {
		tmpTimes.put(record.hashCode(), System.nanoTime());
	}
	
	public void stopLogging(final IDeploymentRecord record) {
		Long startTime = tmpTimes.get(record.hashCode());
		if(startTime != null) {
			Long endTime = System.nanoTime();
			LoggingEntry entry = new LoggingEntry();
			if (record instanceof ServletDeployedEvent) {
				ServletDeployedEvent event = (ServletDeployedEvent) record;
				entry.setLoggingInfo(event.getSerivce(), event.getContext(),
						endTime - startTime, startTime, endTime);
	        } else if (record instanceof EJBDeployedEvent) {
	        	EJBDeployedEvent event = (EJBDeployedEvent) record;
	        	entry.setLoggingInfo(event.getSerivce(), event.getContext(),
						endTime - startTime, startTime, endTime);
	        }
			
			deploymentTimes.add(entry);
		}
	}
	
	public void stopLogging(final IUndeploymentRecord record) {
		Long startTime = tmpTimes.get(record.hashCode());
		if(startTime != null) {
			Long endTime = System.nanoTime();
			LoggingEntry entry = new LoggingEntry();
			if (record instanceof ServletUndeployedEvent) {
				ServletUndeployedEvent event = (ServletUndeployedEvent) record;
				entry.setLoggingInfo(event.getSerivce(), event.getContext(),
						endTime - startTime, startTime, endTime);
	        } else if (record instanceof EJBUndeployedEvent) {
	        	EJBUndeployedEvent event = (EJBUndeployedEvent) record;
	        	entry.setLoggingInfo(event.getSerivce(), event.getContext(),
						endTime - startTime, startTime, endTime);
	        }
			
			undeploymentTimes.add(entry);
		}
	}
	
	public void stopLogging(final IAllocationRecord record) {
		Long startTime = tmpTimes.get(record.hashCode());
		if(startTime != null) {
			Long endTime = System.nanoTime();
			LoggingEntry entry = new LoggingEntry();
			if (record instanceof ServletDeployedEvent) {
				ServletDeployedEvent event = (ServletDeployedEvent) record;
				entry.setLoggingInfo(event.getSerivce(), event.getContext(),
						endTime - startTime, startTime, endTime);
	        } else if (record instanceof EJBDeployedEvent) {
	        	EJBDeployedEvent event = (EJBDeployedEvent) record;
	        	entry.setLoggingInfo(event.getSerivce(), event.getContext(),
						endTime - startTime, startTime, endTime);
	        }
			
			allocationTimes.add(entry);
		}
	}
	
	public void stopLogging(final AfterOperationEvent record) {
		Long startTime = tmpTimes.get(record.hashCode());
		if(startTime != null) {
			Long endTime = System.nanoTime();
			
			LoggingEntry entry = new LoggingEntry();
			entry.setLoggingInfo("", record.getOperationSignature(),
					endTime - startTime, startTime, endTime);
			
			entryCallTimes.add(entry);
		}
	}
	
	public void stopLogging(final EntryCallEvent record) {
		Long startTime = tmpTimes.get(record.hashCode());
		if(startTime != null) {
			Long endTime = System.nanoTime();
			
			LoggingEntry entry = new LoggingEntry();
			entry.setLoggingInfo(record.getSessionId(), record.getOperationSignature(),
					endTime - startTime, startTime, endTime);
			
			entryCallSequenceTimes.add(entry);
		}
	}
	
	public void exportAsCsv() {
		export(Arrays.asList("Service", "Context", "elapsed", "start", "end"), allocationTimes, "TAllocation");
		export(Arrays.asList("Service", "Context", "elapsed", "start", "end"), deploymentTimes, "TDeployment");
		export(Arrays.asList("Service", "Context", "elapsed", "start", "end"), undeploymentTimes, "TUndeployment");
		export(Arrays.asList("", "OperationSignature", "elapsed", "start", "end"), entryCallTimes, "TEntryCall");
		export(Arrays.asList("SessionId", "OperationSignature", "elapsed", "start", "end"), entryCallSequenceTimes, "TEntryCallSequence");
	}
	
	private void export(List<String> headlines, List<LoggingEntry> list, String mapName) {
		CsvExporter exporter = new CsvExporter(mapName + "Logging.csv");
		exporter.setHeadline(mapName);
		exporter.addRow(headlines);
		
		for(LoggingEntry entry : list) {
			exporter.addRow(entry.asList());
		}

		exporter.export();
	}
	
	private class LoggingEntry {
		public void setLoggingInfo(String sessionId, String entityId, long elapsedTime, long startTime, long endTime) {
			this.sessionId = sessionId;
			this.entityId = entityId;
			this.elapsedTime = elapsedTime;
			this.startTime = startTime;
			this.endTime = endTime;
		}
		
		public List<Object> asList() {
			List<Object> entryContent = new ArrayList<>();
			entryContent.add(this.sessionId);
			entryContent.add(this.entityId);
			entryContent.add(this.elapsedTime);
			entryContent.add(this.startTime);
			entryContent.add(this.endTime);
			return entryContent;
		}
		
		String sessionId;
		String entityId;
		long elapsedTime = 0;
		long startTime = 0;
		long endTime = 0;
	}
}
