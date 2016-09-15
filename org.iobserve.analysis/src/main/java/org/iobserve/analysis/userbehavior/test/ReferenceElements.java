package org.iobserve.analysis.userbehavior.test;

import org.palladiosimulator.pcm.usagemodel.UsageModel;

import org.iobserve.analysis.filter.models.EntryCallSequenceModel;

/**
 * Contains the reference elements that are created by the ReferenceUsageModelBuilder: Reference model, user sessions, workload
 * Used for the evaluation of the modeling accuracy
 * 
 * @author David
 *
 */
public class ReferenceElements {
	
	private EntryCallSequenceModel entryCallSequenceModel;
	private UsageModel usageModel;
	private long meanInterArrivalTime;
	private int meanConcurrentUserSessions;
	
	public ReferenceElements() {

	}

	public EntryCallSequenceModel getEntryCallSequenceModel() {
		return entryCallSequenceModel;
	}

	public void setEntryCallSequenceModel(EntryCallSequenceModel entryCallSequenceModel) {
		this.entryCallSequenceModel = entryCallSequenceModel;
	}

	public UsageModel getUsageModel() {
		return usageModel;
	}

	public void setUsageModel(UsageModel usageModel) {
		this.usageModel = usageModel;
	}

	public long getMeanInterArrivalTime() {
		return meanInterArrivalTime;
	}

	public void setMeanInterArrivalTime(long meanInterArrivalTime) {
		this.meanInterArrivalTime = meanInterArrivalTime;
	}

	public int getMeanConcurrentUserSessions() {
		return meanConcurrentUserSessions;
	}

	public void setMeanConcurrentUserSessions(int meanConcurrentUserSessions) {
		this.meanConcurrentUserSessions = meanConcurrentUserSessions;
	}

	
	
	
	
}
