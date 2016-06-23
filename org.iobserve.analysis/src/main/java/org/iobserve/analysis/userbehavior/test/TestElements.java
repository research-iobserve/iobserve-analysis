package org.iobserve.analysis.userbehavior.test;

import org.palladiosimulator.pcm.usagemodel.UsageModel;

import org.iobserve.analysis.filter.models.EntryCallSequenceModel;

public class TestElements {
	
	private final EntryCallSequenceModel entryCallSequenceModel;
	private final UsageModel usageModel;
	
	public TestElements(EntryCallSequenceModel entryCallSequenceModel, UsageModel usageModel) {
		this.entryCallSequenceModel = entryCallSequenceModel;
		this.usageModel = usageModel;
	}

	public EntryCallSequenceModel getEntryCallSequenceModel() {
		return entryCallSequenceModel;
	}

	public UsageModel getUsageModel() {
		return usageModel;
	}
	
}
