package org.iobserve.analysis.userbehavior.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.userbehavior.UserGroupExtraction;
import org.iobserve.analysis.userbehavior.data.ClusteringMetrics;


public class ClusteringEvaluation {
	
	// The number of user sessions of each user group defines the user group mix
	int numberOfUserSessionsOfUserGroupCustomer = 2000;
	int numberOfUserSessionsOfUserGroupStockManager = 2000;
	int numberOfUserSessionsOfUserGroupStoreManager = 4000;
	EntryCallSequenceModel entryCallSequenceModel;
	String customerTag = "Customer";
	String StockManagerTag = "StockManager";
	String StoreManagerTag = "StoreManager";
	int varianceValue = 10;
	int numberOfEvaluationIterations = 1;
	List<ClusterAssignmentsCounter> listOfClusterAssignmentsCounter = new ArrayList<ClusterAssignmentsCounter>();
	
	/**
	 * Executes the evaluation of the clustering
	 * @throws IOException
	 */
	public void evaluateTheClustering() throws IOException {
		
		List<Double>sseValues = new ArrayList<Double>();
		List<Double>mcValues = new ArrayList<Double>();
		
		for(int j=0;j<numberOfEvaluationIterations;j++) {
			createCallSequenceModelWithVaryingUserGroups();
			double sse = performClustering();
			sseValues.add(sse);
			double mc = calculateMC();
			mcValues.add(mc);
		}
		
		writeResults(sseValues,mcValues);
	}
	
	/**
	 * Calculates the missclassifiction rate of user sessions over all clusters
	 * 
	 * @return the calculated missclassification rate
	 */
	private double calculateMC() {
		double mc = 0;
		

		List<Integer> assignmentsOfUserGroupCustomer = new ArrayList<Integer>();
		List<Integer> assignmentsOfUserGroupStockManager = new ArrayList<Integer>();
		List<Integer> assignmentsOfUserGroupStoreManager = new ArrayList<Integer>();
		for(ClusterAssignmentsCounter assignmentCounts : listOfClusterAssignmentsCounter) {
			if(assignmentCounts.getNumberOfUserGroupCustomer()>0)
				assignmentsOfUserGroupCustomer.add(assignmentCounts.getNumberOfUserGroupCustomer());
			if(assignmentCounts.getNumberOfUserGroupStockManager()>0)
				assignmentsOfUserGroupStockManager.add(assignmentCounts.getNumberOfUserGroupStockManager());
			if(assignmentCounts.getNumberOfUserGroupStoreManager()>0)
				assignmentsOfUserGroupStoreManager.add(assignmentCounts.getNumberOfUserGroupStoreManager());
		}
		
		if(assignmentsOfUserGroupCustomer.size()>1) {
		    int highest = assignmentsOfUserGroupCustomer.get(0);
		    int indexOfHighest = 0;
		    for (int s=1;s<assignmentsOfUserGroupCustomer.size();s++){
		        int curValue = assignmentsOfUserGroupCustomer.get(s);
		        if (curValue > highest) {
		            highest = curValue;
		            indexOfHighest = s;
		        }
		    }
		    assignmentsOfUserGroupCustomer.remove(indexOfHighest);
		    for (int s=0;s<assignmentsOfUserGroupCustomer.size();s++){
		        mc+=assignmentsOfUserGroupCustomer.get(s);
		    }
		}
		if(assignmentsOfUserGroupStockManager.size()>1) {
		    int highest = assignmentsOfUserGroupStockManager.get(0);
		    int indexOfHighest = 0;
		    for (int s=1;s<assignmentsOfUserGroupStockManager.size();s++){
		        int curValue = assignmentsOfUserGroupStockManager.get(s);
		        if (curValue > highest) {
		            highest = curValue;
		            indexOfHighest = s;
		        }
		    }
		    assignmentsOfUserGroupStockManager.remove(indexOfHighest);
		    for (int s=0;s<assignmentsOfUserGroupStockManager.size();s++){
		        mc+=assignmentsOfUserGroupStockManager.get(s);
		    }
		}
		if(assignmentsOfUserGroupStoreManager.size()>1) {
		    int highest = assignmentsOfUserGroupStoreManager.get(0);
		    int indexOfHighest = 0;
		    for (int s=1;s<assignmentsOfUserGroupStoreManager.size();s++){
		        int curValue = assignmentsOfUserGroupStoreManager.get(s);
		        if (curValue > highest) {
		            highest = curValue;
		            indexOfHighest = s;
		        }
		    }
		    assignmentsOfUserGroupStoreManager.remove(indexOfHighest);
		    for (int s=0;s<assignmentsOfUserGroupStoreManager.size();s++){
		        mc+=assignmentsOfUserGroupStoreManager.get(s);
		    }
		}
		
		
		mc = mc/(numberOfUserSessionsOfUserGroupCustomer+numberOfUserSessionsOfUserGroupStockManager+numberOfUserSessionsOfUserGroupStoreManager);
		
		return mc*100;
	}

	private void writeResults(List<Double>sseValues, List<Double>mcValues) throws IOException {
		
		FileWriter writer = new FileWriter("/Users/David/Desktop/ClusteringEvaluationResults");
		writer.append("NumberOfUserSessionsOfUserGroupCustomer,NumberOfUserSessionsOfUserGroupStockManager,NumberOfUserSessionsOfUserGroupStoreManager,SSE,MC");
		writer.append('\n');
		
		for(int j=0;j<numberOfEvaluationIterations;j++) { 
			writer.append(String.valueOf(numberOfUserSessionsOfUserGroupCustomer));
			writer.append(',');
			writer.append(String.valueOf(numberOfUserSessionsOfUserGroupStockManager));
			writer.append(',');
			writer.append(String.valueOf(numberOfUserSessionsOfUserGroupStoreManager));
			writer.append(',');
    		writer.append(String.valueOf(sseValues.get(j)));
    		writer.append(',');
    		writer.append(String.valueOf(mcValues.get(j)));
    		writer.append('\n');
    	}
		
		writer.flush();
	    writer.close();
	}

	/**
	 * Creates for each user group user sessions with random behavior according to the BehaviorModels
	 * and subsumes the user sessions within an EntryCallSequenceModel
	 * @throws IOException
	 */
	private void createCallSequenceModelWithVaryingUserGroups() throws IOException {

		List<UserSession> userSessionsOfGroupCustomer = getUserSessions(numberOfUserSessionsOfUserGroupCustomer, customerTag);
		List<UserSession> userSessionsOfGroupStockManager = getUserSessions(numberOfUserSessionsOfUserGroupStockManager, StockManagerTag);
		List<UserSession> userSessionsOfGroupStoreManager = getUserSessions(numberOfUserSessionsOfUserGroupStoreManager, StoreManagerTag);
		createCallSequencesForUserGroupCustomer(userSessionsOfGroupCustomer);
		createCallSequencesForUserGroupStockManager(userSessionsOfGroupStockManager);
		createCallSequencesForUserGroupStoreManager(userSessionsOfGroupStoreManager);
		
		List<UserSession> userSessions = new ArrayList<UserSession>();
		userSessions.addAll(userSessionsOfGroupStockManager);
		userSessions.addAll(userSessionsOfGroupStoreManager);
		userSessions.addAll(userSessionsOfGroupCustomer);
		long seed = 5;
		Collections.shuffle(userSessions, new Random(seed));
		
		entryCallSequenceModel = new EntryCallSequenceModel(userSessions);
	}
	
	/**
	 * Executes the approach's extraction of user groups process and counts the assignments of user sessions
	 * of each user group within each cluster to be able to calculate the misclassification rate.
	 * Returns the sum of squared error of the clustering
	 * 
	 * @return the sum of squared error of the executed clustering
	 * @throws IOException
	 */
	private double performClustering() throws IOException {
				
		UserGroupExtraction userGroupExtraction = new UserGroupExtraction(entryCallSequenceModel, 3, varianceValue, true);
		userGroupExtraction.extractUserGroups();
		List<EntryCallSequenceModel> entryCallSequenceModelsOfUserGroups = userGroupExtraction.getEntryCallSequenceModelsOfUserGroups();
		listOfClusterAssignmentsCounter = new ArrayList<ClusterAssignmentsCounter>();
		
		for(int i=0;i<entryCallSequenceModelsOfUserGroups.size();i++) {
			ClusterAssignmentsCounter clusterAssignments = new ClusterAssignmentsCounter();
			listOfClusterAssignmentsCounter.add(clusterAssignments);
		}
		
		int index = 0;
		for(EntryCallSequenceModel entryCallSequenceModel : entryCallSequenceModelsOfUserGroups) {
			for(UserSession userSession : entryCallSequenceModel.getUserSessions()) {
				if(userSession.getSessionId().equals(customerTag))
					listOfClusterAssignmentsCounter.get(index).increaseNumberOfUserGroupCustomer();
				else if(userSession.getSessionId().equals(StoreManagerTag))
					listOfClusterAssignmentsCounter.get(index).increaseNumberOfUserGroupStoreManager();
				else if(userSession.getSessionId().equals(StockManagerTag))
					listOfClusterAssignmentsCounter.get(index).increaseNumberOfUserGroupStockManager();
			}
			index++;
		}
		
		return userGroupExtraction.getClusteringResults().getClusteringMetrics().getSumOfSquaredErrors();
	}
	
	/**
	 * Creates for the passed user group the passed number of user sessions
	 * 
	 * @param numberOfUserSessionsToCreate
	 * @param userGroupId
	 * @return user sessions tagged with their user group belonging
	 */
	private List<UserSession> getUserSessions (int numberOfUserSessionsToCreate, String userGroupId) {
		List<UserSession> userSessions = new ArrayList<UserSession>();
		for(int i=0;i<numberOfUserSessionsToCreate;i++) {
			UserSession userSession = new UserSession("host",userGroupId);
			userSessions.add(userSession);
		}
		return userSessions;
	}
	
	private int getRandomInteger(int max, int min) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
	
	private double getRandomDouble(double max, double min) {
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}
	
	/**
	 * Behavior Model of user group Customer
	 * It creates for each user session a random user behavior according to the BehaviorModel it describes
	 * @param userSessions
	 */
	private void createCallSequencesForUserGroupCustomer(List<UserSession> userSessions) {
		EntryCallEvent entryCallEvent;
		for(UserSession userSession : userSessions) {
			int entryTime = getRandomInteger(30, 1);
			int exitTime = entryTime+1;
			entryCallEvent = new EntryCallEvent(entryTime, exitTime, "login", "class", "1", "hostname");
			userSession.add(entryCallEvent,true);
			entryTime+=2;
			exitTime+=2;
			entryCallEvent = new EntryCallEvent(entryTime, exitTime, "buyProduct", "class", "1", "hostname");
			userSession.add(entryCallEvent,true);
			entryTime+=2;
			exitTime+=2;
			while(!entryCallEvent.getOperationSignature().equals("logout")) {
				if(getRandomDouble(1,0)<=0.7) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "buyProduct", "class", "1", "hostname");
					userSession.add(entryCallEvent,true);
				} else {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "logout", "class", "1", "hostname");
					userSession.add(entryCallEvent,true);
				}
				entryTime+=2;
				exitTime+=2;
			}
		}
	}
	

	/**
	 * Behavior Model of user group Store Manager
	 * It creates for each user session a random user behavior according to the BehaviorModel it describes
	 * @param userSessions
	 */
	private void createCallSequencesForUserGroupStoreManager(List<UserSession> userSessions) {
		EntryCallEvent entryCallEvent;
		for(UserSession userSession : userSessions) {
			int entryTime = getRandomInteger(30, 1);
			int exitTime = entryTime+1;
			entryCallEvent = new EntryCallEvent(entryTime, exitTime, "login", "class", "2", "hostname");
			userSession.add(entryCallEvent,true);
			entryTime+=2;
			exitTime+=2;
			double callDecisioner = getRandomDouble(1,0);
			if(callDecisioner<=0.5) {
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, "changePrice", "class", "2", "hostname");
				userSession.add(entryCallEvent,true);
				entryTime+=2;
				exitTime+=2;
			} else {
				entryCallEvent = new EntryCallEvent(entryTime, exitTime, "orderProduct", "class", "2", "hostname");
				userSession.add(entryCallEvent,true);
				entryTime+=2;
				exitTime+=2;
			}
			
			while(!entryCallEvent.getOperationSignature().equals("commit")) {
				
				callDecisioner = getRandomDouble(1,0);
								
				if(entryCallEvent.getOperationSignature().equals("changePrice")&&callDecisioner<=0.3) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "changePrice", "class", "2", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					continue;
				}
				if(entryCallEvent.getOperationSignature().equals("changePrice")&&callDecisioner>0.3&&callDecisioner<=0.6) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "orderProduct", "class", "2", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					continue;
				}
				if(entryCallEvent.getOperationSignature().equals("changePrice")&&callDecisioner>0.6) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "commit", "class", "2", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					continue;
				}
				
				if(entryCallEvent.getOperationSignature().equals("orderProduct")&&callDecisioner<=0.3) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "changePrice", "class", "2", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					continue;
				}
				if(entryCallEvent.getOperationSignature().equals("orderProduct")&&callDecisioner>0.3&&callDecisioner<=0.6) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "orderProduct", "class", "2", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					continue;
				}
				if(entryCallEvent.getOperationSignature().equals("orderProduct")&&callDecisioner>0.6) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "commit", "class", "2", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					continue;
				}
			}
			entryCallEvent = new EntryCallEvent(entryTime, exitTime, "logout", "class", "2", "hostname");
			userSession.add(entryCallEvent,true);
		}
	}
	
	/**
	 * Behavior Model of user group Stock Manager
	 * It creates for each user session a random user behavior according to the BehaviorModel it describes
	 * @param userSessions
	 */
	private void createCallSequencesForUserGroupStockManager(List<UserSession> userSessions) {
		EntryCallEvent entryCallEvent;
		for(UserSession userSession : userSessions) {
			int entryTime = getRandomInteger(30, 1);
			int exitTime = entryTime+1;
			entryCallEvent = new EntryCallEvent(entryTime, exitTime, "login", "class", "3", "hostname");
			userSession.add(entryCallEvent,true);
			entryTime+=2;
			exitTime+=2;
			entryCallEvent = new EntryCallEvent(entryTime, exitTime, "checkDelivery", "class", "3", "hostname");
			userSession.add(entryCallEvent,true);
			entryTime+=2;
			exitTime+=2;
			while(!entryCallEvent.getOperationSignature().equals("logout")) {
				
				double callDecisioner = getRandomDouble(1,0);
				
				if(entryCallEvent.getOperationSignature().equals("checkDelivery")&&callDecisioner<=0.3) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "checkDelivery", "class", "3", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					continue;
				}
				if(entryCallEvent.getOperationSignature().equals("checkDelivery")&&callDecisioner>0.3) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "updateStock", "class", "3", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					continue;
				}
				if(entryCallEvent.getOperationSignature().equals("updateStock")&&callDecisioner<=0.4) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "checkDelivery", "class", "3", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					continue;
				}

				if(entryCallEvent.getOperationSignature().equals("updateStock")&&callDecisioner>0.4) {
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "confirm", "class", "3", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					entryCallEvent = new EntryCallEvent(entryTime, exitTime, "logout", "class", "3", "hostname");
					userSession.add(entryCallEvent,true);
					entryTime+=2;
					exitTime+=2;
					continue;
				}
			}
		}
		
	}

}
