/***************************************************************************
 * Copyright 2014 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.model.ModelProviderPlatform;
import org.iobserve.analysis.model.ModelSaveStrategy;
import org.iobserve.analysis.model.UsageModelProvider;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;

import teetime.framework.AbstractConsumerStage;

/**
 * Represents the TEntryEventSequence Transformation in the paper
 * <i>Run-time Architecture Models for Dynamic Adaptation and Evolution of Cloud Applications</i>
 * 
 * @author Robert Heinrich, Alessandro Guisa
 * @version 1.0
 *
 */
public class TEntryEventSequence extends AbstractConsumerStage<EntryCallSequenceModel> {
	
	private static int executionCounter = 0;
	
	private int counterSavedUsageModel = 0;

	private final ICorrespondence correspondenceModel;
	private final UsageModelProvider usageModelProvider;

	public TEntryEventSequence() {
		final ModelProviderPlatform modelProviderPlatform = AnalysisMain.getInstance().getModelProviderPlatform();
		this.correspondenceModel = modelProviderPlatform.getCorrespondenceModel();
		this.usageModelProvider = modelProviderPlatform.getUsageModelProvider();
	}

	@Override
	protected void execute(final EntryCallSequenceModel model) {
		// logging execution time and memory
		AnalysisMain.getInstance().getTimeMemLogger().before(this, this.getId() + TEntryEventSequence.executionCounter);
		
		// do main task
		this.doUpdateUsageModel(model.getUserSessions());
		
		// logging execution time and memory
		AnalysisMain.getInstance().getTimeMemLogger().after(this, this.getId() + TEntryEventSequence.executionCounter);
		
		// count execution
		TEntryEventSequence.executionCounter++;
	}
	
	/**
	 * Calculate the inter arrival time of the given user sessions
	 * @param sessions sessions
	 * @return >= 0.
	 */
	private long calculateInterarrivalTime(final List<UserSession> sessions) {
		long interArrivalTime = 0;
		if(sessions.size() > 0) {
			//sort user sessions
			Collections.sort(sessions, this.SortUserSessionByExitTime);
			
			long sum = 0;
			for (int i = 0; i < sessions.size() - 1; i++) {
				final long exitTimeU1 = sessions.get(i).getExitTime();
				final long exitTimeU2 = sessions.get(i + 1).getExitTime();
				sum += exitTimeU2 - exitTimeU1;
			}
			
			final long numberSessions = sessions.size() > 1?sessions.size()-1:1;
			interArrivalTime = sum / numberSessions;
		}
		
		return interArrivalTime;
	}
	
	/**
	 * Do update the PCM usage model by iterating over user sessions and constructing the
	 * different paths
	 * 
	 * @param sessions user session
	 */
	private void doUpdateUsageModel(final List<UserSession> sessions) {
		final long averageInterarrivalTime = this.calculateInterarrivalTime(sessions);
		
		AbstractUserAction lastAction = null;
		
		// iterate over user sessions
		for(final UserSession userSession:sessions) {
			
			// create start node
			lastAction = this.usageModelProvider.createStart();
			
			// create open-work-load for inter arrival time
			this.usageModelProvider.createOpenWorkload(averageInterarrivalTime);
			
			// iterate over all events to create the usage behavior
			final Iterator<EntryCallEvent> iteratorEvents = userSession.iterator();
			while(iteratorEvents.hasNext()) {
				final EntryCallEvent event = iteratorEvents.next();
				final String classSig = event.getClassSignature();
				final String opSig = event.getOperationSignature();
				final String operationSig = this.correspondenceModel
						.getCorrespondent(classSig, opSig);
				if (operationSig != null) {
					final AbstractUserAction action = this.usageModelProvider.addAction(operationSig);
					lastAction.setSuccessor(action);
					action.setPredecessor(lastAction);
					lastAction = action;
				}
			}
			
			// create stop node
			final AbstractUserAction stopAction = this.usageModelProvider.createStop();
			lastAction.setSuccessor(stopAction);
			stopAction.setPredecessor(lastAction);
			
			// save the model to the output
			this.usageModelProvider.save(ModelSaveStrategy.OVERRIDE);
			this.counterSavedUsageModel++; //TODO just for now
			
			// reset the model
			this.usageModelProvider.reloadModel();
			
		}
	}
	
	
	/**
	 * Sorts {@link UserSession} by the exit time
	 */
	private final Comparator<UserSession> SortUserSessionByExitTime = new Comparator<UserSession>() {
		
		@Override
		public int compare(final UserSession o1, final UserSession o2) {
			long exitO1 = o1.getExitTime();
			long exitO2 = o2.getExitTime();
			if(exitO1 > exitO2) {
				return 1;
			} else if(exitO1 < exitO2) {
				return -1;
			}
			return 0;
		}
	};
	
}
