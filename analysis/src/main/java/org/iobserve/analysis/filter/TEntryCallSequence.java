/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.filter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.model.correspondence.CorrespondenceModelProvider;
import org.iobserve.analysis.utils.ExecutionTimeLogger;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * TEntryCallSequence maps incoming {@link EntryCallEvents} according to their session-id, recreating user sessions. 
 * While mapping, all sessions that exceed a certain size are collected and forwarded to {@link TEntryEventSequence}.
 * Only events that have a direct mapping to a PCM entity in the correspondence model are processed.
 *
 * @author Robert Heinrich
 * @author Nicolas Boltz
 */
public final class TEntryCallSequence extends AbstractConsumerStage<EntryCallEvent> {

    /** reference to the correspondence model provider. */
    private final CorrespondenceModelProvider correspondenceModelProvider;
    /** Frequency of when to send to output port. */
	private final int generationFrequency;
	/** count of events processed by filter. */
	private int eventCount;
    /** threshold for user session elements until their are send to the next filter. */
    private static final int USER_SESSION_THRESHOLD = 0;
    /** map of sessions. */
    private final Map<String, UserSession> sessions = new HashMap<>();
    /** list of sessions with size bigger than TEntryCallSequence.USER_SESSION_THRESHOLD. */
    private final Map<String, UserSession> approvedSessions = new HashMap<>();
    /** list of approved sessions, which have a certain size */
    private final List<UserSession> approvedSessionsList = new ArrayList<>();
    /** output port. */
    private final OutputPort<EntryCallSequenceModel> outputPort = this.createOutputPort();
    
    /** different variables used for logging execution time of this filter. */
    private long sessionCreationTime = 0;
	private long approvedSessionsListCreationTime = 0;
	private long entryCallSequenceModelCreationTime = 0;

	/**
     * Creates new TEntryCallSequence filter.
     *
     * @param correspondenceModel
     *            reference to the correspondence model
     * @param generationFrequency
     *            frequency in which a updated usage model should be created
     */
    public TEntryCallSequence(final CorrespondenceModelProvider correspondenceModelProvider, final int generationFrequency) {
        this.correspondenceModelProvider = correspondenceModelProvider;
        this.generationFrequency = generationFrequency;
        this.eventCount = 0;
    }

    @Override
    protected void execute(final EntryCallEvent event) {
        /** check if operationEvent is from an known object */
        if (this.correspondenceModelProvider.containsCorrespondence(event.getClassSignature(), event.getOperationSignature())) {
			ExecutionTimeLogger.getInstance().startLogging(event);
        	eventCount++;
            // add the event to the corresponding user session
            // in case the user session is not yet available, create one
            final String userSessionId = UserSession.parseUserSessionId(event);
            UserSession userSession = this.sessions.get(userSessionId);
            this.sessionCreationTime = System.nanoTime();
            if (userSession == null) {
                userSession = new UserSession(event.getHostname(), event.getSessionId());
                this.sessions.put(userSessionId, userSession);
            }
            this.sessionCreationTime = System.nanoTime() - this.sessionCreationTime;
            // do not sort since TEntryEventSequence will sort any ways
            userSession.add(event, false);
            
            // collect all user sessions which have more elements as a defined threshold
            if(userSession.size() > TEntryCallSequence.USER_SESSION_THRESHOLD) {
            	if(!approvedSessions.containsKey(userSessionId)) {
            		approvedSessions.putIfAbsent(userSessionId, userSession);
                    approvedSessionsList.add(userSession);
            	}

                this.approvedSessionsListCreationTime = System.nanoTime();
                // only if a session has changed and has more elements than the threshold
                // the list of approved the usagemodel needs to be newly build
                final List<UserSession> listToSend = this.approvedSessionsList;
                // approvedSessions.values() -> O(n) + new ArrayList<>(Collection c) -> AbstractCollection.toArray() -> O(n) + Arrays.copyOf() -> ~ O(n)
                // c.toArray is basically done in every copy constructor for lists.
                // Copying of the sessions list may not even be necessary, as the created entryCallSequenceModel is only accessed reading not manipulating the list

                this.approvedSessionsListCreationTime = System.nanoTime() - this.approvedSessionsListCreationTime;

                entryCallSequenceModelCreationTime = System.nanoTime();
                EntryCallSequenceModel outputModel = null;
                if(!listToSend.isEmpty()) {
                	outputModel = new EntryCallSequenceModel(listToSend);
                }
                entryCallSequenceModelCreationTime = System.nanoTime() - entryCallSequenceModelCreationTime;
                
                ExecutionTimeLogger.getInstance().stopLogging(event, this);
                
                if (outputModel != null && (eventCount % generationFrequency) == 0) {
                    this.outputPort.send(outputModel);
                }
            }
        }
    }
    
    public OutputPort<EntryCallSequenceModel> getOutputPort() {
        return this.outputPort;
    }
    
	/** Getter and Setter methods that have been added to allow more precise time logging of the filters components.
	 *  Can be removed if the precise time logging is not needed.
	 */
    public long getSessionCreationTime() {
		return sessionCreationTime;
	}

	public void setSessionCreationTime(long sessionCreationTime) {
		this.sessionCreationTime = sessionCreationTime;
	}
    
    public long getApprovedSessionsListCreationTime() {
		return approvedSessionsListCreationTime;
	}

	public void setApprovedSessionsListCreationTime(long approvedSessionsListCreationTime) {
		this.approvedSessionsListCreationTime = approvedSessionsListCreationTime;
	}
	
    public long getEntryCallSequenceModelCreationTime() {
		return entryCallSequenceModelCreationTime;
	}

	public void setEntryCallSequenceModelCreationTime(long entryCallSequenceModelCreationTime) {
		this.entryCallSequenceModelCreationTime = entryCallSequenceModelCreationTime;
	}
}
