package org.iobserve.analysis.clustering.filter;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.clustering.filter.models.BehaviorModelTable;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;


public class TBehaviorModelAggregator extends AbstractStage {

	private final InputPort<BehaviorModelTable> inputPort = this.createInputPort();
	private final InputPort<Long> timestampTriggerInputPort = this.createInputPort();
	private final OutputPort<List<BehaviorModelTable>> outputPort = this.createOutputPort();

	private final List<BehaviorModelTable> collectedTables = new ArrayList<BehaviorModelTable>();

	@Override
	protected void execute() {
		BehaviorModelTable element = inputPort.receive();
		if (null != element) {
			collectedTables.add(element);
		}

		Long timestampTrigger = this.timestampTriggerInputPort.receive();
		if (null == timestampTrigger) {
			return;
		}

		outputPort.send(this.collectedTables);
	}

	public InputPort<BehaviorModelTable> getInputPort() {
		return this.inputPort;
	}

	public InputPort<Long> getTimestampTriggerInputPort() {
		return this.timestampTriggerInputPort;
	}

	public OutputPort<List<BehaviorModelTable>> getOutputPort() {
		return this.outputPort;
	}
	
}
