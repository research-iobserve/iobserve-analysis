package org.iobserve.analysis.userbehavior.test;

import org.palladiosimulator.pcm.usagemodel.UsageModel;

import org.iobserve.analysis.filter.models.EntryCallSequenceModel;

public class TestElements {
	
	private EntryCallSequenceModel entryCallSequenceModel;
	private UsageModel usageModel;
	private int sequenceLength;
	private int numberOfBranchTransitions;
	private int numberOfBranchTransitions2;
	public int getNumberOfBranchTransitions2() {
		return numberOfBranchTransitions2;
	}

	public void setNumberOfBranchTransitions2(int numberOfBranchTransitions2) {
		this.numberOfBranchTransitions2 = numberOfBranchTransitions2;
	}

	private int loopCount;
	private int loopCount2;
	
	public TestElements() {

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

	public int getSequenceLength() {
		return sequenceLength;
	}

	public void setSequenceLength(int sequenceLength) {
		this.sequenceLength = sequenceLength;
	}

	public int getNumberOfBranchTransitions() {
		return numberOfBranchTransitions;
	}

	public void setNumberOfBranchTransitions(int numberOfBranchTransitions) {
		this.numberOfBranchTransitions = numberOfBranchTransitions;
	}

	public int getLoopCount() {
		return loopCount;
	}

	public void setLoopCount(int loopCount) {
		this.loopCount = loopCount;
	}

	public int getLoopCount2() {
		return loopCount2;
	}

	public void setLoopCount2(int loopCount2) {
		this.loopCount2 = loopCount2;
	}

	
	
	
	
}
