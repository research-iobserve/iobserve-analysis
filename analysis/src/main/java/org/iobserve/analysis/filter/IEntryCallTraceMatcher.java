package org.iobserve.analysis.filter;

import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import teetime.stage.trace.traceReconstruction.EventBasedTrace;

public interface IEntryCallTraceMatcher {

	boolean stateMatch(final EventBasedTrace trace, final BeforeOperationEvent beforeEvent);

	BeforeOperationEvent getBeforeOperationEvent();

	AfterOperationEvent getAfterOperationEvent();

}
