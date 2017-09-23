package org.iobserve.analysis.filter;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.UserSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public class TraceOperationCleanupFilter extends AbstractConsumerStage<UserSession> {

	private final ITraceSignatureCleanupRewriter rewriter;
	private final OutputPort<UserSession> outputPort = this.createOutputPort();
	
	public TraceOperationCleanupFilter(ITraceSignatureCleanupRewriter rewriter) {
		this.rewriter = rewriter;
	}

	@Override
	protected void execute(UserSession session) throws Exception {
		for (EntryCallEvent event : session.getEvents()) {
			event.setClassSignature(this.rewriter.rewriteClassSignature(event.getClassSignature()));
			event.setClassSignature(this.rewriter.rewriteOperationSignature(event.getOperationSignature()));
			
		}
	}

	public OutputPort<UserSession> getOutputPort() {
		return this.outputPort;
	}

}
