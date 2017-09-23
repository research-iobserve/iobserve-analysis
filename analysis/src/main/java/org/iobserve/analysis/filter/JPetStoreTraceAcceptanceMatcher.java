package org.iobserve.analysis.filter;

import org.iobserve.analysis.data.EntryCallEvent;

public class JPetStoreTraceAcceptanceMatcher implements ITraceAcceptanceMatcher {

	@Override
	public boolean match(EntryCallEvent call) {
		return (matchClassSignature(call.getClassSignature()) &&
				matchOperationSignature(call.getOperationSignature()));
	}

	private boolean matchOperationSignature(String operationSignature) {
		return true;
	}

	private boolean matchClassSignature(String classSignature) {
		return true;
	}

}
