package org.iobserve.analysis;

import org.iobserve.analysis.filter.IEntryCallTraceMatcher;

import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

public class JPetStoreCallTraceMatcher implements IEntryCallTraceMatcher {
	
	private BeforeOperationEvent beforeOperationEvent;

	private AfterOperationEvent afterOperationEvent;
	
	@Override
	public boolean stateMatch(final EventBasedTrace trace, final BeforeOperationEvent beforeEvent) {
		if (beforeEvent.getOrderIndex() == 0) {
			this.beforeOperationEvent = beforeEvent;
			for (AbstractTraceEvent event : trace.getTraceEvents()) {
				if (event instanceof AfterOperationEvent) {
					AfterOperationEvent afterEvent = (AfterOperationEvent)event;
					if (afterEvent.getClassSignature().equals(beforeEvent.getClassSignature()) &&
							afterEvent.getOperationSignature().equals(beforeEvent.getOperationSignature())) {
						this.afterOperationEvent = afterEvent;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public BeforeOperationEvent getBeforeOperationEvent() {
		return this.beforeOperationEvent;
	}

	@Override
	public AfterOperationEvent getAfterOperationEvent() {
		return afterOperationEvent;
	}

}
