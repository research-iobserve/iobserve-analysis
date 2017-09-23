package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.List;

import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * The collect user sessions filter collects incoming user sessions and
 * sends the collection as an model to the next stage based on a time trigger.
 * Therefore, the stage has two input ports userSessionInputPort and
 * timeTriggerInputPort, and one output port. The collection to be send includes
 * all UserSessions (exitTime) older an interval (keepTime) or at least the last
 * (minCollectionSize) UserSessions. 
 * 
 * @author Reiner Jung
 *
 * @version 0.0.2
 */
public class CollectUserSessionsFilter extends AbstractStage {

	final InputPort<UserSession> userSessionInputPort = this.createInputPort();
	final InputPort<Long> timeTriggerInputPort = this.createInputPort();
	
	final OutputPort<EntryCallSequenceModel> outputPort = this.createOutputPort();
	
	final List<UserSession> userSessions = new ArrayList<>();
	
	final long keepTime;
	private int minCollectionSize;
	
	public CollectUserSessionsFilter(long keepTime, int minCollectionSize) {
		this.keepTime = keepTime;
		this.minCollectionSize = minCollectionSize;
	}
	
	@Override
	protected void execute() throws Exception {
		UserSession userSession = userSessionInputPort.receive();
		
		if (userSession != null) {
			userSessions.add(userSession);
		}
		
		Long triggerTime = timeTriggerInputPort.receive();
		if (triggerTime != null) {
			/** collect all sessions. */
			EntryCallSequenceModel model = new EntryCallSequenceModel(userSessions);
			/** remove expired sessions. */
			if (userSessions.size() > this.minCollectionSize) {
				for (int i=0; userSessions.size() > i; i++) {
					if (userSessions.get(i).getExitTime() + this.keepTime < triggerTime) {
						userSessions.remove(i);
					}
				}
			}
			outputPort.send(model);
		}
		
	}

	public InputPort<UserSession> getUserSessionInputPort() {
		return userSessionInputPort;
	}

	public InputPort<Long> getTimeTriggerInputPort() {
		return timeTriggerInputPort;
	}

	public OutputPort<EntryCallSequenceModel> getOutputPort() {
		return outputPort;
	}

	

}
