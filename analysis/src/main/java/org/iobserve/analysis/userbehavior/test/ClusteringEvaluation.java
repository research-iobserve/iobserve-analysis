/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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

/**
 * Contains the evaluation of the clustering. For three user groups user sessions with a user group
 * specific user behavior are created and each user session is labeled with its user group
 * belonging. Subsequently, the user sessions are clustered and it is evaluated how accurate our
 * approach assigns user sessions of a user group to the same cluster
 *
 * @author David Peter, Robert Heinrich
 */
public class ClusteringEvaluation {
    private static final String CUSTOMER_TAG = "Customer";
    private static final String STOCK_MANAGER_TAG = "StockManager";
    private static final String STORE_MANAGER_TAG = "StoreManager";
    
    private EntryCallSequenceModel entryCallSequenceModel;
    
    // Total number of sessions should equal 8000
    private int numberOfSessionsOfGroupCustomer; // 2000 or 4000
    private int numberOfSessionsOfGroupStockManager; // 2000 or 4000
    private int numberOfSessionsOfGroupStoreManager; // 2000 or 4000
    private int varianceValue; // 0 or 10
    private int evaluationIterations;
    private List<Integer> groupSplit;
    private String outputPath;

    /**
     * Default constructor.
     */
    public ClusteringEvaluation(int customerCount, int stockManagerCount, int storeManagerCount, int varianceValue, int evaluationIterations) {
        // The number of user sessions of each user group defines the user group mix
    	this.numberOfSessionsOfGroupCustomer = customerCount;
    	this.numberOfSessionsOfGroupStockManager = stockManagerCount;
    	this.numberOfSessionsOfGroupStoreManager = storeManagerCount;
    	this.varianceValue = varianceValue;
    	this.evaluationIterations = evaluationIterations;
    	this.groupSplit = new ArrayList<>();
    	int sessionCount = customerCount + stockManagerCount + storeManagerCount;
    	this.groupSplit.add((customerCount * 100) / sessionCount );
    	this.groupSplit.add((stockManagerCount * 100) / sessionCount);
    	this.groupSplit.add((storeManagerCount * 100) / sessionCount);
    	
    	this.outputPath = "D:\\Dokumente\\Uni\\HiWi\\UsageBehaviourTransformationTest\\ClusteringEvaluation\\varianceOfUserGroups = " + varianceValue + "\\";
    }

    /**
     * Executes the evaluation of the clustering.
     *
     * @throws IOException
     *             on error
     */
    public void evaluateTheClustering() {
    	String partitioningString = "" + this.groupSplit.get(0) + this.groupSplit.get(1) + this.groupSplit.get(2);
    	String varianceString = " Variance " + this.varianceValue + " ";
    	String iterationString = this.evaluationIterations + " Iterations";
    	System.out.println("Start clustering evaluation " + partitioningString + varianceString + iterationString);
    	final List<List<ClusterAssignmentsCounter>> clusteringResults = new ArrayList<>();
        final List<Double> sseValues = new ArrayList<>();
        final List<Double> mcValues = new ArrayList<>();

        for (int j = 0; j < this.evaluationIterations; j++) {
            this.createCallSequenceModelWithVaryingUserGroups();
            List<ClusterAssignmentsCounter> listOfClusterAssignmentsCounter = new ArrayList<>();
            final double sse = this.performClustering(listOfClusterAssignmentsCounter);
            sseValues.add(sse);
            clusteringResults.add(listOfClusterAssignmentsCounter);
            //refering to clusteringResults value that was set in performClustering();
            final double mc = this.calculateMC(clusteringResults.get(j));
            mcValues.add(mc);
        }

        this.writeResults(clusteringResults, sseValues, mcValues);
    }

    /**
     * Calculates the missclassifiction rate of user sessions over all clusters. Therefore, for each
     * user group the wrongly assigned user sessions are determined. MC = Number of missclassififed
     * user sessions / Number of all user sessions
     *
     * @return the calculated missclassification rate
     */
    private double calculateMC(List<ClusterAssignmentsCounter> listOfClusterAssignmentsCounter) {
        double mc = 0;

        final List<Integer> assignmentsOfUserGroupCustomer = new ArrayList<>();
        final List<Integer> assignmentsOfUserGroupStockManager = new ArrayList<>();
        final List<Integer> assignmentsOfUserGroupStoreManager = new ArrayList<>();
        // Counts the assignments of user sessions to the clusters
        // Thus, for each user group it is known to which clusters its user sessions are assigned
        for (final ClusterAssignmentsCounter assignmentCounts : listOfClusterAssignmentsCounter) {
            if (assignmentCounts.getNumberOfUserGroupCustomer() > 0) {
                assignmentsOfUserGroupCustomer.add(assignmentCounts.getNumberOfUserGroupCustomer());
            }
            if (assignmentCounts.getNumberOfUserGroupStockManager() > 0) {
                assignmentsOfUserGroupStockManager.add(assignmentCounts.getNumberOfUserGroupStockManager());
            }
            if (assignmentCounts.getNumberOfUserGroupStoreManager() > 0) {
                assignmentsOfUserGroupStoreManager.add(assignmentCounts.getNumberOfUserGroupStoreManager());
            }
        }

        // Counts for each user group the missclassified user sessions
        // Therefore, the cluster with the highest number of user sessions of a user group is
        // assumed as the cluster with the correct assigned user sessions. The remaining number of
        // user sessions is assumed as wrongly assigned user sessions
        if (assignmentsOfUserGroupCustomer.size() > 1) {
            int highest = assignmentsOfUserGroupCustomer.get(0);
            int indexOfHighest = 0;
            for (int s = 1; s < assignmentsOfUserGroupCustomer.size(); s++) {
                final int curValue = assignmentsOfUserGroupCustomer.get(s);
                if (curValue > highest) {
                    highest = curValue;
                    indexOfHighest = s;
                }
            }
            assignmentsOfUserGroupCustomer.remove(indexOfHighest);
            for (int s = 0; s < assignmentsOfUserGroupCustomer.size(); s++) {
                mc += assignmentsOfUserGroupCustomer.get(s);
            }
        }
        if (assignmentsOfUserGroupStockManager.size() > 1) {
            int highest = assignmentsOfUserGroupStockManager.get(0);
            int indexOfHighest = 0;
            for (int s = 1; s < assignmentsOfUserGroupStockManager.size(); s++) {
                final int curValue = assignmentsOfUserGroupStockManager.get(s);
                if (curValue > highest) {
                    highest = curValue;
                    indexOfHighest = s;
                }
            }
            assignmentsOfUserGroupStockManager.remove(indexOfHighest);
            for (int s = 0; s < assignmentsOfUserGroupStockManager.size(); s++) {
                mc += assignmentsOfUserGroupStockManager.get(s);
            }
        }
        if (assignmentsOfUserGroupStoreManager.size() > 1) {
            int highest = assignmentsOfUserGroupStoreManager.get(0);
            int indexOfHighest = 0;
            for (int s = 1; s < assignmentsOfUserGroupStoreManager.size(); s++) {
                final int curValue = assignmentsOfUserGroupStoreManager.get(s);
                if (curValue > highest) {
                    highest = curValue;
                    indexOfHighest = s;
                }
            }
            assignmentsOfUserGroupStoreManager.remove(indexOfHighest);
            for (int s = 0; s < assignmentsOfUserGroupStoreManager.size(); s++) {
                mc += assignmentsOfUserGroupStoreManager.get(s);
            }
        }

        // Calculates the mean missclassification rate over all user groups
        mc = mc / (this.numberOfSessionsOfGroupCustomer
                + this.numberOfSessionsOfGroupStockManager
                + this.numberOfSessionsOfGroupStoreManager);

        return mc * 100;
    }

    private void writeResults(List<List<ClusterAssignmentsCounter>> clusteringResults, 
    		List<Double> sseValues, List<Double> mcValues) {
    	
    	String partitioningString = "" + this.groupSplit.get(0) + this.groupSplit.get(1) + this.groupSplit.get(2) + ".csv";
        
		try {
			FileWriter metricResultWriter;
			metricResultWriter = new FileWriter(this.outputPath + "ClusteringMetrics" + partitioningString);
	        metricResultWriter.append("SSE;MC");
	        metricResultWriter.append('\n');
	        
	        double sseValuesAverage = 0;
	        double mcValuesAverage = 0;

	        for (int j = 0; j < this.evaluationIterations; j++) {
	        	metricResultWriter.append(String.valueOf(sseValues.get(j)));
	        	sseValuesAverage += sseValues.get(j);
	        	metricResultWriter.append(';');
	        	metricResultWriter.append(String.valueOf(mcValues.get(j)));
	        	mcValuesAverage += mcValues.get(j);
	        	metricResultWriter.append('\n');
	        }
	        
	        sseValuesAverage = sseValuesAverage / this.evaluationIterations;
	        mcValuesAverage = mcValuesAverage / this.evaluationIterations;
	        
	        metricResultWriter.append("average");
	        metricResultWriter.append('\n');
	        metricResultWriter.append(Double.toString(sseValuesAverage));
	        metricResultWriter.append(';');
	        metricResultWriter.append(Double.toString(mcValuesAverage));
	        metricResultWriter.append('\n');

	        metricResultWriter.flush();
	        metricResultWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error writing clustering metrics results!");
		}

        
        
		try {
			FileWriter clusteringResultWriter;
			clusteringResultWriter = new FileWriter(this.outputPath + "ClusteringResults" + partitioningString);
			 //Not beautiful, but only way to force output
	    	for (List<ClusterAssignmentsCounter> clustering : clusteringResults) {
	    		clusteringResultWriter.append("UG;UGM");
	    		for(int i = 1; i <= clustering.size(); i++){
	            	clusteringResultWriter.append(";C" + i);
	            }
	            clusteringResultWriter.append('\n');
	    		for(int k = 0; k < 3; k++) {
	    			if(k == 0) {
	            		clusteringResultWriter.append("CR;" + this.groupSplit.get(k) + "%;");
	            	} else if (k == 1) {
	            		clusteringResultWriter.append("SKM;" + this.groupSplit.get(k) + "%;");
	            	} else if (k == 2) {
	            		clusteringResultWriter.append("SEM;" + this.groupSplit.get(k) + "%;");
	            	}
	    			for(ClusterAssignmentsCounter assignmentCounts : clustering) {
	    				if(k == 0) {
	                		clusteringResultWriter.append(Integer.toString(assignmentCounts.getNumberOfUserGroupCustomer()));
	                	} else if (k == 1) {
	                		clusteringResultWriter.append(Integer.toString(assignmentCounts.getNumberOfUserGroupStockManager()));
	                	} else if (k == 2) {
	                		clusteringResultWriter.append(Integer.toString(assignmentCounts.getNumberOfUserGroupStoreManager()));
	                	}
	    				clusteringResultWriter.append(';');
	    			}
	    			clusteringResultWriter.append('\n');
	    		}
	    		clusteringResultWriter.append('\n');
	    	}
	        
	        clusteringResultWriter.flush();
	        clusteringResultWriter.close();
		} catch (IOException e) {
			System.out.println("Error printing clustering evaluation results!");
		}
  
       
    }

    /**
     * Creates for each user group user sessions with random behavior according to the
     * BehaviorModels and subsumes the user sessions within an EntryCallSequenceModel.
     *
     * @throws IOException
     */
    private void createCallSequenceModelWithVaryingUserGroups() {

        final List<UserSession> userSessionsOfGroupCustomer = this.getUserSessions(
        		this.numberOfSessionsOfGroupCustomer, CUSTOMER_TAG);
        final List<UserSession> userSessionsOfGroupStockManager = this.getUserSessions(
        		this.numberOfSessionsOfGroupStockManager,
                STOCK_MANAGER_TAG);
        final List<UserSession> userSessionsOfGroupStoreManager = this.getUserSessions(
        		this.numberOfSessionsOfGroupStoreManager,
                STORE_MANAGER_TAG);
        this.createCallSequencesForUserGroupCustomer(userSessionsOfGroupCustomer);
        this.createCallSequencesForUserGroupStockManager(userSessionsOfGroupStockManager);
        this.createCallSequencesForUserGroupStoreManager(userSessionsOfGroupStoreManager);

        final List<UserSession> userSessions = new ArrayList<>();
        userSessions.addAll(userSessionsOfGroupStockManager);
        userSessions.addAll(userSessionsOfGroupStoreManager);
        userSessions.addAll(userSessionsOfGroupCustomer);
        final long seed = 5;
        Collections.shuffle(userSessions, new Random(seed));

        this.entryCallSequenceModel = new EntryCallSequenceModel(userSessions);
    }

    /**
     * Executes the approach's extraction of user groups process and counts the assignments of user
     * sessions of each user group within each cluster to be able to calculate the misclassification
     * rate. Returns the sum of squared error of the clustering
     *
     * @return the sum of squared error of the executed clustering
     * @throws IOException
     */
    private double performClustering(List<ClusterAssignmentsCounter> listOfClusterAssignmentsCounter) {

        final UserGroupExtraction userGroupExtraction = new UserGroupExtraction(this.entryCallSequenceModel, 3,
        		varianceValue, true);
        userGroupExtraction.extractUserGroups();
        final List<EntryCallSequenceModel> entryCallSequenceModelsOfUserGroups = userGroupExtraction
                .getEntryCallSequenceModelsOfUserGroups();

        for (int i = 0; i < entryCallSequenceModelsOfUserGroups.size(); i++) {
            final ClusterAssignmentsCounter clusterAssignments = new ClusterAssignmentsCounter();
            listOfClusterAssignmentsCounter.add(clusterAssignments);
        }

        int index = 0;
        for (final EntryCallSequenceModel entryCallSequence : entryCallSequenceModelsOfUserGroups) {
            for (final UserSession userSession : entryCallSequence.getUserSessions()) {
                if (userSession.getSessionId().equals(CUSTOMER_TAG)) {
                    listOfClusterAssignmentsCounter.get(index).increaseNumberOfUserGroupCustomer();
                } else if (userSession.getSessionId().equals(STORE_MANAGER_TAG)) {
                    listOfClusterAssignmentsCounter.get(index).increaseNumberOfUserGroupStoreManager();
                } else if (userSession.getSessionId().equals(STOCK_MANAGER_TAG)) {
                    listOfClusterAssignmentsCounter.get(index).increaseNumberOfUserGroupStockManager();
                }
            }
            index++;
        }

        return userGroupExtraction.getClusteringResults().getClusteringMetrics().getSumOfSquaredErrors();
    }

    /**
     * Creates for the passed user group the passed number of user sessions.
     *
     * @param numberOfUserSessionsToCreate
     * @param userGroupId
     * @return user sessions tagged with their user group belonging
     */
    private List<UserSession> getUserSessions(final int numberOfUserSessionsToCreate, final String userGroupId) {
        final List<UserSession> userSessions = new ArrayList<>();
        for (int i = 0; i < numberOfUserSessionsToCreate; i++) {
            final UserSession userSession = new UserSession("host", userGroupId);
            userSessions.add(userSession);
        }
        return userSessions;
    }

    /**
     * Returns a random integer according to the passed max and min values.
     *
     * @param max
     * @param min
     * @return
     */
    private int getRandomInteger(final int max, final int min) {
        final Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Returns a random double according to the passed max and min values.
     *
     * @param max
     * @param min
     * @return
     */
    private double getRandomDouble(final double max, final double min) {
        final Random r = new Random();
        return min + ((max - min) * r.nextDouble());
    }

    /**
     * Behavior Model of user group Customer. It creates for each user session a random user
     * behavior according to the BehaviorModel it describes
     *
     * @param userSessions
     */
    private void createCallSequencesForUserGroupCustomer(final List<UserSession> userSessions) {
        EntryCallEvent entryCallEvent;
        for (final UserSession userSession : userSessions) {
            int entryTime = this.getRandomInteger(30, 1);
            int exitTime = entryTime + 1;
            entryCallEvent = new EntryCallEvent(entryTime, exitTime, "login", "class", "1", "hostname");
            userSession.add(entryCallEvent, true);
            entryTime += 2;
            exitTime += 2;
            entryCallEvent = new EntryCallEvent(entryTime, exitTime, "buyProduct", "class", "1", "hostname");
            userSession.add(entryCallEvent, true);
            entryTime += 2;
            exitTime += 2;
            while (!entryCallEvent.getOperationSignature().equals("logout")) {
                if (this.getRandomDouble(1, 0) <= 0.7) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "buyProduct", "class", "1", "hostname");
                    userSession.add(entryCallEvent, true);
                } else {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "logout", "class", "1", "hostname");
                    userSession.add(entryCallEvent, true);
                }
                entryTime += 2;
                exitTime += 2;
            }
        }
    }

    /**
     * Behavior Model of user group Store Manager. It creates for each user session a random user
     * behavior according to the BehaviorModel it describes
     *
     * @param userSessions
     */
    private void createCallSequencesForUserGroupStoreManager(final List<UserSession> userSessions) {
        EntryCallEvent entryCallEvent;
        for (final UserSession userSession : userSessions) {
            int entryTime = this.getRandomInteger(30, 1);
            int exitTime = entryTime + 1;
            entryCallEvent = new EntryCallEvent(entryTime, exitTime, "login", "class", "2", "hostname");
            userSession.add(entryCallEvent, true);
            entryTime += 2;
            exitTime += 2;
            double callDecisioner = this.getRandomDouble(1, 0);
            if (callDecisioner <= 0.5) {
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, "changePrice", "class", "2", "hostname");
                userSession.add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
            } else {
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, "orderProduct", "class", "2", "hostname");
                userSession.add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
            }

            while (!entryCallEvent.getOperationSignature().equals("commit")) {

                callDecisioner = this.getRandomDouble(1, 0);

                if (entryCallEvent.getOperationSignature().equals("changePrice") && (callDecisioner <= 0.3)) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "changePrice", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("changePrice") && (callDecisioner > 0.3)
                        && (callDecisioner <= 0.6)) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "orderProduct", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("changePrice") && (callDecisioner > 0.6)) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "commit", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }

                if (entryCallEvent.getOperationSignature().equals("orderProduct") && (callDecisioner <= 0.3)) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "changePrice", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("orderProduct") && (callDecisioner > 0.3)
                        && (callDecisioner <= 0.6)) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "orderProduct", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("orderProduct") && (callDecisioner > 0.6)) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "commit", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
            }
            entryCallEvent = new EntryCallEvent(entryTime, exitTime, "logout", "class", "2", "hostname");
            userSession.add(entryCallEvent, true);
        }
    }

    /**
     * Behavior Model of user group Stock Manager. It creates for each user session a random user
     * behavior according to the BehaviorModel it describes
     *
     * @param userSessions
     */
    private void createCallSequencesForUserGroupStockManager(final List<UserSession> userSessions) {
        EntryCallEvent entryCallEvent;
        for (final UserSession userSession : userSessions) {
            int entryTime = this.getRandomInteger(30, 1);
            int exitTime = entryTime + 1;
            entryCallEvent = new EntryCallEvent(entryTime, exitTime, "login", "class", "3", "hostname");
            userSession.add(entryCallEvent, true);
            entryTime += 2;
            exitTime += 2;
            entryCallEvent = new EntryCallEvent(entryTime, exitTime, "checkDelivery", "class", "3", "hostname");
            userSession.add(entryCallEvent, true);
            entryTime += 2;
            exitTime += 2;
            while (!entryCallEvent.getOperationSignature().equals("logout")) {

                final double callDecisioner = this.getRandomDouble(1, 0);

                if (entryCallEvent.getOperationSignature().equals("checkDelivery") && (callDecisioner <= 0.3)) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "checkDelivery", "class", "3", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("checkDelivery") && (callDecisioner > 0.3)) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "updateStock", "class", "3", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("updateStock") && (callDecisioner <= 0.4)) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "checkDelivery", "class", "3", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }

                if (entryCallEvent.getOperationSignature().equals("updateStock") && (callDecisioner > 0.4)) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "confirm", "class", "3", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "logout", "class", "3", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
            }
        }

    }

}
