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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.AnalysisMain;
import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.model.ModelProviderPlatform;
import org.iobserve.analysis.model.UsageModelBuilder;
import org.iobserve.analysis.model.UsageModelProvider;
import org.iobserve.analysis.userbehavior.UserBehaviorModeling;
import org.iobserve.analysis.userbehavior.test.TestElements;
import org.iobserve.analysis.userbehavior.test.UsageModelMatcher;
import org.iobserve.analysis.userbehavior.test.UsageModelTestBuilder;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import teetime.framework.AbstractConsumerStage;

import com.google.common.base.Optional;

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
		AnalysisMain.getInstance().getTimeMemLogger()
			.before(this, this.getId() + TEntryEventSequence.executionCounter);
		
		/**
		 * Added by David Peter
		 */
		UsageModelTestBuilder usageModelTestBuilder = new UsageModelTestBuilder();
		UsageModelMatcher usageModelMatcher = new UsageModelMatcher();
		if(executionCounter==0) {
			List<List<Object>> listOfDataValues = new ArrayList<List<Object>>();
			List<List<Object>> listOfOverallTimeValues = new ArrayList<List<Object>>();
			List<List<Object>> listOftimeValuesOfUserGroupExtraction = new ArrayList<List<Object>>();
			List<List<Object>> listOftimeValuesOfBranchExtraction = new ArrayList<List<Object>>();
			List<List<Object>> listOftimeValuesOfLoopExtraction = new ArrayList<List<Object>>();
			List<List<Object>> listOftimeValuesOfPcmModeling = new ArrayList<List<Object>>();
			List<Object> numberOfCalls = new ArrayList<Object>();
			for(int j=0;j<1;j++) {
				numberOfCalls.clear();
			List<Object> timeValuesOfUserGroupExtraction = new ArrayList<Object>();
			List<Object> timeValuesOfBranchExtraction = new ArrayList<Object>();
			List<Object> timeValuesOfLoopExtraction = new ArrayList<Object>();
			List<Object> timeValuesOfPcmModeling = new ArrayList<Object>();
			List<Object> overallResponseTimeValues = new ArrayList<Object>();
			int numberOfIterations = 30;
			int stepSize = 1;
			try {
				for(int i=2;i<=numberOfIterations;i+=stepSize) {
					
//					int numberOfUserGroups = this.usageModelProvider.getModel().getUsageScenario_UsageModel().size();
					int numberOfUserGroups = 1;
					int varianceOfUserGroups = 0;
//					int varianceOfUserGroups = AnalysisMain.getInstance().getInputParameter().getVarianceOfUserGroups();
					int thinkTime = AnalysisMain.getInstance().getInputParameter().getThinkTime();
					boolean isClosedWorkload = AnalysisMain.getInstance().getInputParameter().isClosedWorkload();
					
//					TestElements testElementsOfSimpleBranchModel = usageModelTestBuilder.getSimpleBranchTestModel(thinkTime, isClosedWorkload);
//					TestElements testElementsOfSimpleLoopModel = usageModelTestBuilder.getSimpleLoopTestModel(thinkTime, isClosedWorkload);
//					TestElements testElementsOfSimpleSequenceModel = usageModelTestBuilder.getSimpleSequenceTestModel(thinkTime,isClosedWorkload);
//					TestElements testElementsOfDecisionLoopModel = usageModelTestBuilder.getLoopDecisionTestModel(thinkTime, isClosedWorkload);
					TestElements testElementsOfLoopInLoopModel = usageModelTestBuilder.getLoopWithinLoopTestModel(thinkTime, isClosedWorkload);
//					TestElements testElementsOfLoopInBranchModel = usageModelTestBuilder.getLoopWithinBranchTestModel(thinkTime, isClosedWorkload);
//					TestElements testElementsOfBranchInBranchModel = usageModelTestBuilder.getBranchWithinBranchTestModel(thinkTime, isClosedWorkload);
//					TestElements testElementsOfBranchInLoopModel = usageModelTestBuilder.getBranchWithinLoopTestModel(thinkTime, isClosedWorkload);
//					TestElements testElementsOfBranchToBranchToSequenceModel = usageModelTestBuilder.getBranchToBranchToSequenceTestModel(thinkTime, isClosedWorkload);
//					TestElements testElementsOfOpenWorkloadModel = usageModelTestBuilder.getOpenWorkloadTestModel();
//					TestElements testElementsOfIncreasingCallSequenceModel = usageModelTestBuilder.getIncreasingCallSequenceScalabilityTestModel(i);
//					TestElements testElementsOfIncreasingUserSessionsModel = usageModelTestBuilder.getIncreasingUserSessionsScalabilityTestModel(i);

					UserBehaviorModeling behaviorModeling = new UserBehaviorModeling(testElementsOfLoopInLoopModel.getEntryCallSequenceModel(), numberOfUserGroups, varianceOfUserGroups, isClosedWorkload, thinkTime);
					try {
						behaviorModeling.modelUserBehavior();
					} catch (IOException e) {
						e.printStackTrace();
					}

					numberOfCalls.add(testElementsOfLoopInLoopModel.getEntryCallSequenceModel().getUserSessions().get(0).getEvents().size());
					timeValuesOfUserGroupExtraction.add(behaviorModeling.getResponseTimeOfUserGroupExtraction());
					timeValuesOfBranchExtraction.add(behaviorModeling.getResponseTimeOfBranchExtraction());
					timeValuesOfLoopExtraction.add(behaviorModeling.getResponseTimeOfLoopExtraction());
					timeValuesOfPcmModeling.add(behaviorModeling.getResponseTimeOfPcmModelling());
					
//					numberOfCalls.add(i);
					overallResponseTimeValues.add(behaviorModeling.getOverallResponseTime());

					System.out.println("J:"+j+" I:"+i+"/"+numberOfIterations);
					boolean isMatch = usageModelMatcher.matchUsageModels(behaviorModeling.getPcmUsageModel(), testElementsOfLoopInLoopModel.getUsageModel());

//					dataValues.add(isMatch);
//					dataValues2.add(testElementsOfBranchInLoopModel.getLoopCount());
//					dataValues3.add(testElementsOfBranchInLoopModel.getNumberOfBranchTransitions2());
					usageModelTestBuilder.saveModel(behaviorModeling.getPcmUsageModel(), "/Users/David/GitRepositories/iObserve/org.iobserve.analysis/output/usageModels/output.usagemodel");
					if(!isMatch) {	
						System.out.println("MISSMATCH");
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			listOfDataValues.add(overallResponseTimeValues);
			listOfOverallTimeValues.add(overallResponseTimeValues);
			listOftimeValuesOfUserGroupExtraction.add(timeValuesOfUserGroupExtraction);
			listOftimeValuesOfBranchExtraction.add(timeValuesOfBranchExtraction);
			listOftimeValuesOfLoopExtraction.add(timeValuesOfLoopExtraction);
			listOftimeValuesOfPcmModeling.add(timeValuesOfPcmModeling);
//			listOfDataValues.clear();
//			listOfDataValues.add(numberOfCalls);
//			listOfDataValues.add(overallResponseTimeValues);
//			usageModelTestBuilder.writeToCsv("overallResponseTimeIncreasingUserSessions.csv",listOfDataValues);
//			listOfDataValues.clear();
//			listOfDataValues.add(numberOfCalls);
//			listOfDataValues.add(timeValuesOfUserGroupExtraction);
//			usageModelTestBuilder.writeToCsv("responseTimeOfUserGroupExtraction.csv",listOfDataValues);
//			listOfDataValues.clear();
//			listOfDataValues.add(numberOfCalls);
//			listOfDataValues.add(timeValuesOfBranchExtraction);
//			usageModelTestBuilder.writeToCsv("responseTimeOfBranchExtraction.csv",listOfDataValues);
//			listOfDataValues.clear();
//			listOfDataValues.add(numberOfCalls);
//			listOfDataValues.add(timeValuesOfLoopExtraction);
//			usageModelTestBuilder.writeToCsv("responseTimeOfLoopExtraction.csv",listOfDataValues);
//			listOfDataValues.clear();
//			listOfDataValues.add(numberOfCalls);
//			listOfDataValues.add(timeValuesOfPcmModeling);
//			usageModelTestBuilder.writeToCsv("responseTimeOfPcmModeling.csv",listOfDataValues);
			}
			List<List<Object>> listOfAverageDataValues = new ArrayList<List<Object>>();
			List<Object> overallTimeAverageValues = new ArrayList<Object>();
			List<Object> userGroupAverageValues = new ArrayList<Object>();
			List<Object> branchAverageValues = new ArrayList<Object>();
			List<Object> loopAverageValues = new ArrayList<Object>();
			List<Object> modelingAverageValues = new ArrayList<Object>();
			for(int j=0;j<listOfOverallTimeValues.get(0).size();j++){
				double timeValue = 0;
				for(int i=0;i<listOfOverallTimeValues.size();i++) {
					long t = (long)listOfOverallTimeValues.get(i).get(j);
					timeValue += (double)t;
				}
				timeValue = timeValue/listOfOverallTimeValues.size();
				overallTimeAverageValues.add(timeValue);
				
				timeValue = 0;
				for(int i=0;i<listOftimeValuesOfUserGroupExtraction.size();i++) {
					long t = (long)listOftimeValuesOfUserGroupExtraction.get(i).get(j);
					timeValue += (double)t;
				}
				timeValue = timeValue/listOftimeValuesOfUserGroupExtraction.size();
				userGroupAverageValues.add(timeValue);
				
				timeValue = 0;
				for(int i=0;i<listOftimeValuesOfBranchExtraction.size();i++) {
					long t = (long)listOftimeValuesOfBranchExtraction.get(i).get(j);
					timeValue += (double)t;
				}
				timeValue = timeValue/listOftimeValuesOfBranchExtraction.size();
				branchAverageValues.add(timeValue);
				
				timeValue = 0;
				for(int i=0;i<listOftimeValuesOfLoopExtraction.size();i++) {
					long t = (long)listOftimeValuesOfLoopExtraction.get(i).get(j);
					timeValue += (double)t;
				}
				timeValue = timeValue/listOftimeValuesOfLoopExtraction.size();
				loopAverageValues.add(timeValue);
				
				timeValue = 0;
				for(int i=0;i<listOftimeValuesOfPcmModeling.size();i++) {
					long t = (long)listOftimeValuesOfPcmModeling.get(i).get(j);
					timeValue += (double)t;
				}
				timeValue = timeValue/listOftimeValuesOfPcmModeling.size();
				modelingAverageValues.add(timeValue);
			}
			listOfAverageDataValues.add(numberOfCalls);
			listOfAverageDataValues.add(overallTimeAverageValues);
			usageModelTestBuilder.writeToCsv("AverageOverallTimeValuesIncreasingUserSessions.csv",listOfAverageDataValues);
			listOfAverageDataValues.clear();
			listOfAverageDataValues.add(numberOfCalls);
			listOfAverageDataValues.add(userGroupAverageValues);
			usageModelTestBuilder.writeToCsv("UserGroupTimeValuesIncreasingUserSessions.csv",listOfAverageDataValues);
			listOfAverageDataValues.clear();
			listOfAverageDataValues.add(numberOfCalls);
			listOfAverageDataValues.add(branchAverageValues);
			usageModelTestBuilder.writeToCsv("BranchTimeValuesIncreasingUserSessions.csv",listOfAverageDataValues);
			listOfAverageDataValues.clear();
			listOfAverageDataValues.add(numberOfCalls);
			listOfAverageDataValues.add(loopAverageValues);
			usageModelTestBuilder.writeToCsv("LoopValuesIncreasingUserSessions.csv",listOfAverageDataValues);
			listOfAverageDataValues.clear();
			listOfAverageDataValues.add(numberOfCalls);
			listOfAverageDataValues.add(modelingAverageValues);
			usageModelTestBuilder.writeToCsv("ModelingIncreasingUserSessions.csv",listOfAverageDataValues);
			
		}
		/**
		 * End 
		 */
		
		// do main task
//		 this.doUpdateUsageModel(model.getUserSessions());
		
		// logging execution time and memory
		AnalysisMain.getInstance().getTimeMemLogger()
			.after(this, this.getId() + TEntryEventSequence.executionCounter);
		
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
		
		// iterate over user sessions
		for(final UserSession userSession:sessions) {
			
			// create simple usage model builder
			final UsageModelBuilder builder = new UsageModelBuilder(this.usageModelProvider);
			
			// like re-load
			builder.loadModel().resetModel();
			
			final UsageScenario usageScenario = builder.createUsageScenario("MyTestScenario");
			builder.createOpenWorkload(averageInterarrivalTime, usageScenario);
			
			final Start start = builder.createStart();
			builder.addUserAction(usageScenario, start);
			
			AbstractUserAction lastAction = start;
			
			// iterate over all events to create the usage behavior
			final Iterator<EntryCallEvent> iteratorEvents = userSession.iterator();
			while(iteratorEvents.hasNext()) {
				final EntryCallEvent event = iteratorEvents.next();
				final String classSig = event.getClassSignature();
				final String opSig = event.getOperationSignature();
				
				final Optional<Correspondent> optionCorrespondent = this.correspondenceModel
						.getCorrespondent(classSig, opSig);
				if (optionCorrespondent.isPresent()) {
					final Correspondent correspondent = optionCorrespondent.get();
					final EntryLevelSystemCall eSysCall = builder.createEntryLevelSystemCall(correspondent);
					builder.connect(lastAction, eSysCall);
					builder.addUserAction(usageScenario, eSysCall);
					lastAction = eSysCall;
				}
			}
			
			final Stop stop = builder.createStop();
			builder.connect(lastAction, stop);
			builder.addUserAction(usageScenario, stop);
			
			builder.build();
			this.counterSavedUsageModel++; //TODO just for now
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
