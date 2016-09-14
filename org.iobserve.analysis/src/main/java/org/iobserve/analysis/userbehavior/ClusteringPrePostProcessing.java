package org.iobserve.analysis.userbehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.userbehavior.data.ClusteringResults;
import org.iobserve.analysis.userbehavior.data.UserSessionAsTransitionMatrix;
import org.iobserve.analysis.userbehavior.data.WorkloadIntensity;


/**
 * This class holds a set of preprocessing and postprocessing methods for the clustering of user sessions.   
 *  
 * @author David Peter, Robert Heinrich
 */

public class ClusteringPrePostProcessing {
	
	
	/**
	 * It iterates over the userSessions and returns a list of distinct operation signatures occurring within the 
	 * userSessions´ entryCall sequences.
	 * 
	 * @param userSessions contains all userSessions of the input EntryCallSequenceModel that will be clustered. 
	 * @return a list of distinct operation signatures occurring within userSessions
	 */
	public List<String> getListOfDistinctOperationSignatures(List<UserSession> userSessions) {
		List<String> listOfDistinctOperationSignatures = new ArrayList<String>();
		for(final UserSession userSession:userSessions) {
			final Iterator<EntryCallEvent> iteratorEvents = userSession.iterator();
			while(iteratorEvents.hasNext()) {
				final EntryCallEvent event = iteratorEvents.next();
				if(!listOfDistinctOperationSignatures.contains(event.getOperationSignature())) {
					listOfDistinctOperationSignatures.add(event.getOperationSignature());
				}
			}
		}
		return listOfDistinctOperationSignatures;
	}

	/**
	 * Transforms the passed user sessions to transition matrices that can be used for the similarity calculation
	 * of the user group clustering. The rows and the columns of the transition matrix contain of the passed distinct
	 * operation signatures. It parses through the entry call sequences of each user session and counts the transition
	 * between each operation signature. The result is a list of user sessions whose call sequence is represented as
	 * a matrix of transitions between the operation signatures. 
	 * 
	 * @param userSessions are transformed to transition matrices
	 * @param listOfDistinctOperationSignatures are the distinct operation signatures that are used to build the transition matrix
	 * @return the passed user sessions as transition matrices
	 */
	public List<UserSessionAsTransitionMatrix> getAbsoluteCallModel(List<UserSession> userSessions, List<String> listOfDistinctOperationSignatures) {
		
		List<UserSessionAsTransitionMatrix> absoluteCallModel = new ArrayList<UserSessionAsTransitionMatrix>(); 
		
		for(final UserSession userSession:userSessions) {
			
			UserSessionAsTransitionMatrix absoluteCountOfCalls = new UserSessionAsTransitionMatrix(userSession.getSessionId(), listOfDistinctOperationSignatures.size());
			List<EntryCallEvent> callSequence = userSession.getEvents();
		
			for(int i=0;i<callSequence.size();i++) {
				String currentCall = callSequence.get(i).getOperationSignature();				
				int indexOfCurrentCall = listOfDistinctOperationSignatures.indexOf(currentCall);
				absoluteCountOfCalls.getAbsoluteCountOfCalls() [indexOfCurrentCall] = absoluteCountOfCalls.getAbsoluteCountOfCalls() [indexOfCurrentCall] + 1;					
			}
			absoluteCallModel.add(absoluteCountOfCalls);
		}
		
		return absoluteCallModel;
	}
	
	/**
	 * It creates for each cluster(user group) its own entryCallSequenceModel. For that, each entryCallSequenceModel receives
	 * exclusively the user group´s assigned user sessions obtained via the clustering. Additionally each entryCallSequenceModel
	 * receives the user group´s occurrence likelihood within the considered user sessions.
	 * 
	 * @param clusteringResults hold the assignments of the clustering and the number of clusters
	 * @param callSequenceModel is the input entryCallSequenceModel that holds all user sessions
	 * @return for each cluster one entryCallSequenceModel. Each contains exclusively the cluster´s assigned user sessions
	 */
	public List<EntryCallSequenceModel> getForEachUserGroupAnEntryCallSequenceModel(ClusteringResults clusteringResults, EntryCallSequenceModel callSequenceModel) {	
		int numberOfClusters = clusteringResults.getNumberOfClusters();
		int[] assignments = clusteringResults.getAssignments();
		List<EntryCallSequenceModel> entryCallSequenceModels = new ArrayList<EntryCallSequenceModel>(numberOfClusters);
		double countOfAbsoluteUserSessions = callSequenceModel.getUserSessions().size();
		for(int k=0;k<numberOfClusters;k++) {
			List<UserSession> sessions = new ArrayList<UserSession>();
			int instanceNumber = 0;
			double countOfAssigendUserSessions = 0;
			for(int clusterNum : assignments) {
				if(clusterNum==k) {
					sessions.add(callSequenceModel.getUserSessions().get(instanceNumber));
					countOfAssigendUserSessions++;
				}
				instanceNumber++;
			}			
			if(sessions.size()==0)
				continue;
			double relativeFrequencyOfUserGroup = countOfAssigendUserSessions/countOfAbsoluteUserSessions;
			entryCallSequenceModels.add(new EntryCallSequenceModel(sessions, relativeFrequencyOfUserGroup));
		}
		return entryCallSequenceModels;	
	}
	
	/**
	 * It calculates and sets for each entryCallSequenceModel its specific workload intensity. For that it calculates
	 * an open as well as a closed workload and add them to its entryCallSequenceModel.
	 * 
	 * @param entryCallSequenceModels are the entryCallSequenceModels of the detected user groups
	 */
	public void setTheWorkloadIntensityForTheEntryCallSequenceModels(List<EntryCallSequenceModel> entryCallSequenceModels, boolean isClosedWorkload) {
		for(final EntryCallSequenceModel entryCallSequenceModel:entryCallSequenceModels) {
			WorkloadIntensity workloadIntensity = new WorkloadIntensity();
			if(isClosedWorkload) {
				calculateTheNumberOfConcurrentUsers(entryCallSequenceModel.getUserSessions(), workloadIntensity);
			} else {
				calculateInterarrivalTime(entryCallSequenceModel.getUserSessions(), workloadIntensity);
			}
			entryCallSequenceModel.setWorkloadIntensity(workloadIntensity);
		}	
	}
	
	/**
	 * Divides the timeframe of the passed user sessions into ten equal timeframes and calculates for each timeframe the number
	 * of concurrent user sessions. Subsequently, it calculates the average number of concurrent users
	 * over the ten frames and saves the obtained number as the closed workload population 
	 * 
	 * 
	 * @param sessions to calculate the average concurrent sessions within
	 * @param workloadIntensity to set the obtained average concurrent session number
	 */
	private void calculateTheNumberOfConcurrentUsers(final List<UserSession> sessions, WorkloadIntensity workloadIntensity) {
		
		int maxNumberOfConcurrentUsers = 0;
		int averageNumberOfConcurrentUsers = 0;
	
		if(sessions.size()<1) {
			workloadIntensity.setMaxNumberOfConcurrentUsers(0);
			workloadIntensity.setAvgNumberOfConcurrentUsers(0);
			return;
		}
			
		Collections.sort(sessions, this.SortUserSessionByEntryTime);
		
		// Calculates the average interval within the user sessions
		long interval = 0;
		for (int i=0;i<sessions.size();i++) {
			final long entryTimeUS =  getEntryTime(sessions.get(i).getEvents());
			final long exitTimeUS =  sessions.get(i).getExitTime();
			interval += (exitTimeUS-entryTimeUS);
		}
		long averageInterval = interval/sessions.size();
		
		// Divides the overall time of the user sessions into a number of timeframes according to the averageInterval
		final long startTimeOfAllUserSessions = getEntryTime(sessions.get(0).getEvents());
		final long endTimeOfAllUserSessions = sessions.get(sessions.size()-1).getExitTime();
		long numberOfTimeFrames = Math.round(((endTimeOfAllUserSessions-startTimeOfAllUserSessions)/averageInterval)+0.5);
		
		// Set the start and end times and its initial count of each timeframe 
		List<long[]> timeframes = new ArrayList<long[]>();
		long startTimeOfTimeframe = startTimeOfAllUserSessions;
		long endTimeOfTimeframe = startTimeOfAllUserSessions+averageInterval;
		for(int i=0;i<numberOfTimeFrames;i++) {
			long[] timeframe = new long[3];
			timeframe[0] = startTimeOfTimeframe;
			timeframe[1] = endTimeOfTimeframe;
			timeframe[2] = 0;
			timeframes.add(timeframe);
			startTimeOfTimeframe = endTimeOfTimeframe;
			endTimeOfTimeframe = endTimeOfTimeframe+averageInterval;
		}
		
		// Obtains for each user session its starting timeframe and increases the count of the corrsponding timeframe
		for (int i=0;i<sessions.size();i++) {
			final long entryTimeUS =  getEntryTime(sessions.get(i).getEvents());
			for(int j=0;j<timeframes.size();j++) {
				if(entryTimeUS>=timeframes.get(j)[0]&&entryTimeUS<=timeframes.get(j)[1]){
					timeframes.get(j)[2] = timeframes.get(j)[2] + 1;
					break;
				}		
			}
		}
		
		// Calculates the average concurrent user sessions over the timeframes
		int numberOfConcurrentUserSessions = 0;
		for(int j=0;j<timeframes.size();j++) {
			numberOfConcurrentUserSessions += timeframes.get(j)[2]*timeframes.get(j)[2];
			if(timeframes.get(j)[2]>maxNumberOfConcurrentUsers) {
				maxNumberOfConcurrentUsers = (int)timeframes.get(j)[2];
			}
		}
		averageNumberOfConcurrentUsers = numberOfConcurrentUserSessions/sessions.size();
		
		workloadIntensity.setMaxNumberOfConcurrentUsers(maxNumberOfConcurrentUsers);
		workloadIntensity.setAvgNumberOfConcurrentUsers(averageNumberOfConcurrentUsers);		
	}
	
	/**
	 * Calculates a closed workload. For that, it calculates the average number of concurrent users as well as the maximum
	 * number of concurrent users. That means the max/avg amount of concurrent user sessions determined by their entry and exit times. 
	 * Both values can be used for the specification of a closed workload in an PCM usage model.
	 * By calculating both it can later be chosen between the two values.
	 * 
	 * @param sessions are the user group´s userSessions
	 * @param workloadIntensity is the empty workload intensity that is filled with values for the specification of a closed workload
	 */
	/**
	 * deprecated
	 * Nested loop of user sessions -> Bad scalability
	 * Only used to compare with the approximated approach of calculating the number of concurrent users
	 */
//	private void calculateTheNumberOfConcurrentUsers(final List<UserSession> sessions, WorkloadIntensity workloadIntensity) {
//		int maxNumberOfConcurrentUsers = 0;
//		int averageNumberOfConcurrentUsers = 0;
//		if(sessions.size() > 0) {
//			int countOfConcurrentUsers = 0;
//			Collections.sort(sessions, this.SortUserSessionByEntryTime);
//			for (int i = 0; i < sessions.size(); i++) {
//				final long entryTimeUS1 =  sessions.get(i).getEntryTime();
//				final long exitTimeUS1 = sessions.get(i).getExitTime();
//				int numberOfConcurrentUserSessionsDuringThisSession = 1;
//				for (int j = 0; j < sessions.size(); j++) {
//					if(j==i)
//						continue;
//					final long entryTimeUS2 =  sessions.get(j).getEntryTime();
//					final long exitTimeUS2 = sessions.get(j).getExitTime();
//					if(exitTimeUS2<entryTimeUS1)
//						continue;
//					if(entryTimeUS2>exitTimeUS1)
//						break;
//					if((exitTimeUS1>=entryTimeUS2&&exitTimeUS1<=exitTimeUS2)||(exitTimeUS2>=entryTimeUS1&&exitTimeUS2<=exitTimeUS1))
//						numberOfConcurrentUserSessionsDuringThisSession++;
//				}
//				if(numberOfConcurrentUserSessionsDuringThisSession > maxNumberOfConcurrentUsers)
//					maxNumberOfConcurrentUsers = numberOfConcurrentUserSessionsDuringThisSession;
//				countOfConcurrentUsers += numberOfConcurrentUserSessionsDuringThisSession;
//			}
//			averageNumberOfConcurrentUsers = countOfConcurrentUsers/sessions.size();
//		}
//		workloadIntensity.setMaxNumberOfConcurrentUsers(maxNumberOfConcurrentUsers);
//		workloadIntensity.setAvgNumberOfConcurrentUsers(averageNumberOfConcurrentUsers);
//	}
	
	
	/**
	 * David Peter
	 * Following Methods:
	 * Used from class org.iobserve.analysis.filter.TEntryEventSequence;
	 * Calculates the value for an open workload.
	 * Changed getExitTime to getEntryTime because of using the inter arrival time
	 * TODO: Ask Alessandro why he used exitTime instead of entryTime
	 */
	/**
	 * Calculate the inter arrival time of the given user sessions
	 * @param sessions sessions
	 * @return >= 0.
	 */
	private void calculateInterarrivalTime(final List<UserSession> sessions, WorkloadIntensity workloadIntensity) {
		long interArrivalTime = 0;
		if(sessions.size() > 0) {
			//sort user sessions
			Collections.sort(sessions, this.SortUserSessionByExitTime);
			
			long sum = 0;
			for (int i = 0; i < sessions.size() - 1; i++) {
				// Changed to getEntryTime() instead of getExitTime()
				// see method description above
				final long exitTimeU1 = sessions.get(i).getEntryTime();
				final long exitTimeU2 = sessions.get(i + 1).getEntryTime();
				sum += exitTimeU2 - exitTimeU1;
			}
			
			final long numberSessions = sessions.size() > 1?sessions.size()-1:1;
			interArrivalTime = sum / numberSessions;
		}
		
		workloadIntensity.setInterarrivalTimeOfUserSessions(interArrivalTime);
	}
	
	
	/**
	 * David Peter
	 * Used from package org.iobserve.analysis.filter.TEntryEventSequence;
	 */
	/**
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
	
	/**
	 * David Peter
	 * Used from package org.iobserve.analysis.filter.TEntryEventSequence;
	 */
	/**
	/**
	 * Sorts {@link UserSession} by the exit time
	 */
	private final Comparator<UserSession> SortUserSessionByEntryTime = new Comparator<UserSession>() {
		
		@Override
		public int compare(final UserSession o1, final UserSession o2) {
			long entryO1 = getEntryTime(o1.getEvents());
			long entryO2 = getEntryTime(o2.getEvents());
			if(entryO1 > entryO2) {
				return 1;
			} else if(entryO1 < entryO2) {
				return -1;
			}
			return 0;
		}
	};
	
	
	/**
	 * From UserSession
	 * After the error is removed from UserSession this can be deleted
	 * TODO: Ask Alessandro: Bug see below
	 */
	
	/**
	 * Get the entry time of this entire session.
	 * @return 0 if no events available at all and > 0 else.
	 */
	public long getEntryTime(List<EntryCallEvent> events) {
		long entryTime = 0;
		if (events.size() > 0) {
			this.sortEventsBy(SortEntryCallEventsByEntryTime, events);
			// Here was the bug: First element has to be returned instead of last
			entryTime = events.get(0).getEntryTime();
		}
		return entryTime;
	}
	
	public void sortEventsBy(final Comparator<EntryCallEvent> cmp, List<EntryCallEvent> events) {
		Collections.sort(events,cmp);
	}
	
	/**
	 * Simple comparator for comparing the entry times
	 */
	public static final Comparator<EntryCallEvent> SortEntryCallEventsByEntryTime = 
			new Comparator<EntryCallEvent>() {
		
		@Override
		public int compare(final EntryCallEvent o1, final EntryCallEvent o2) {
			if (o1.getEntryTime() > o2.getEntryTime()) {
				return 1;
			} else if (o1.getEntryTime() < o2.getEntryTime()) {
				return -1;
			}
			return 0;
		}
	};

}
