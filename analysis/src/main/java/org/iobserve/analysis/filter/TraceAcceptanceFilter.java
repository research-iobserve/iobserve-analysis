/**
 * 
 */
package org.iobserve.analysis.filter;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.UserSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Tests whether a trace contains operations which identify it as a valid trace.
 * 
 * @author Reiner Jung
 *
 */
public class TraceAcceptanceFilter extends AbstractConsumerStage<UserSession> {

	final OutputPort<UserSession> outputPort = this.createOutputPort();
	private ITraceAcceptanceMatcher matcher;
	
	public TraceAcceptanceFilter(ITraceAcceptanceMatcher matcher) {
		this.matcher = matcher;
	}
	
	@Override
	protected void execute(UserSession session) throws Exception {
		for (EntryCallEvent call : session.getEvents()) {
			if (!this.matcher.match(call))
				return;
		}
		
		this.outputPort.send(session);
	}

	public OutputPort<UserSession> getOutputPort() {
		return this.outputPort;
	}

}
