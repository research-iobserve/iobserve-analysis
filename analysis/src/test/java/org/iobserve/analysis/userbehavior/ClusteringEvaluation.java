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
package org.iobserve.analysis.userbehavior;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.data.UserSession;
import org.iobserve.stages.general.data.EntryCallEvent;

/**
 * Contains the evaluation of the clustering. For three user groups user sessions with a user group
 * specific user behavior are created and each user session is labeled with its user group
 * belonging. Subsequently, the user sessions are clustered and it is evaluated how accurate our
 * approach assigns user sessions of a user group to the same cluster
 *
 * @author David Peter, Robert Heinrich
 */
public class ClusteringEvaluation {

    // The number of user sessions of each user group defines the user group mix
    private static final int NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_CUSTOMER = 2000;
    private static final int NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_STOCK_MANAGER = 2000;
    private static final int NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_STORE_MANAGER = 4000;
    private static final String CUSTOMER_TAG = "Customer";
    private static final String STOCK_MANAGER_TAG = "StockManager";
    private static final String STORE_MANAGER_TAG = "StoreManager";
    private static final int VARIANCE_VALUE = 10;
    private static final int NUMBER_OF_EVALUATION_ITERATIONS = 1;

    private EntryCallSequenceModel entryCallSequenceModel;
    private List<ClusterAssignmentsCounter> listOfClusterAssignmentsCounter = new ArrayList<>();

    /**
     * Default constructor.
     */
    public ClusteringEvaluation() {
    }

    /**
     * Executes the evaluation of the clustering.
     *
     * @throws IOException
     *             on error
     */
    public void evaluateTheClustering() throws IOException {

        final List<Double> sseValues = new ArrayList<>();
        final List<Double> mcValues = new ArrayList<>();

        for (int j = 0; j < ClusteringEvaluation.NUMBER_OF_EVALUATION_ITERATIONS; j++) {
            this.createCallSequenceModelWithVaryingUserGroups();
            final double sse = this.performClustering();
            sseValues.add(sse);
            final double mc = this.calculateMC();
            mcValues.add(mc);
        }

        this.writeResults(sseValues, mcValues);
    }

    /**
     * Calculates the missclassifiction rate of user sessions over all clusters. Therefore, for each
     * user group the wrongly assigned user sessions are determined. MC = Number of missclassififed
     * user sessions / Number of all user sessions
     *
     * @return the calculated missclassification rate
     */
    private double calculateMC() {
        double mc = 0;

        final List<Integer> assignmentsOfUserGroupCustomer = new ArrayList<>();
        final List<Integer> assignmentsOfUserGroupStockManager = new ArrayList<>();
        final List<Integer> assignmentsOfUserGroupStoreManager = new ArrayList<>();
        // Counts the assignments of user sessions to the clusters
        // Thus, for each user group it is known to which clusters its user sessions are assigned
        for (final ClusterAssignmentsCounter assignmentCounts : this.listOfClusterAssignmentsCounter) {
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
        mc = mc / (ClusteringEvaluation.NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_CUSTOMER
                + ClusteringEvaluation.NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_STOCK_MANAGER
                + ClusteringEvaluation.NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_STORE_MANAGER);

        return mc * 100;
    }

    private void writeResults(final List<Double> sseValues, final List<Double> mcValues) throws IOException {

        final FileWriter writer = new FileWriter("/Users/David/Desktop/ClusteringEvaluationResults");
        writer.append(
                "NumberOfUserSessionsOfUserGroupCustomer,NumberOfUserSessionsOfUserGroupStockManager,NumberOfUserSessionsOfUserGroupStoreManager,SSE,MC");
        writer.append('\n');

        for (int j = 0; j < ClusteringEvaluation.NUMBER_OF_EVALUATION_ITERATIONS; j++) {
            writer.append(String.valueOf(ClusteringEvaluation.NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_CUSTOMER));
            writer.append(',');
            writer.append(String.valueOf(ClusteringEvaluation.NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_STOCK_MANAGER));
            writer.append(',');
            writer.append(String.valueOf(ClusteringEvaluation.NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_STORE_MANAGER));
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
     * Creates for each user group user sessions with random behavior according to the
     * BehaviorModels and subsumes the user sessions within an EntryCallSequenceModel.
     *
     * @throws IOException
     */
    private void createCallSequenceModelWithVaryingUserGroups() throws IOException {

        final List<UserSession> userSessionsOfGroupCustomer = this.getUserSessions(
                ClusteringEvaluation.NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_CUSTOMER, ClusteringEvaluation.CUSTOMER_TAG);
        final List<UserSession> userSessionsOfGroupStockManager = this.getUserSessions(
                ClusteringEvaluation.NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_STOCK_MANAGER,
                ClusteringEvaluation.STOCK_MANAGER_TAG);
        final List<UserSession> userSessionsOfGroupStoreManager = this.getUserSessions(
                ClusteringEvaluation.NUMBER_OF_USER_SESSIONS_OF_USER_GROUP_STORE_MANAGER,
                ClusteringEvaluation.STORE_MANAGER_TAG);
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
    private double performClustering() throws IOException {

        final UserGroupExtraction userGroupExtraction = new UserGroupExtraction(this.entryCallSequenceModel, 3,
                ClusteringEvaluation.VARIANCE_VALUE, true);
        userGroupExtraction.extractUserGroups();
        final List<EntryCallSequenceModel> entryCallSequenceModelsOfUserGroups = userGroupExtraction
                .getEntryCallSequenceModelsOfUserGroups();
        this.listOfClusterAssignmentsCounter = new ArrayList<>();

        for (int i = 0; i < entryCallSequenceModelsOfUserGroups.size(); i++) {
            final ClusterAssignmentsCounter clusterAssignments = new ClusterAssignmentsCounter();
            this.listOfClusterAssignmentsCounter.add(clusterAssignments);
        }

        int index = 0;
        for (final EntryCallSequenceModel entryCallSequence : entryCallSequenceModelsOfUserGroups) {
            for (final UserSession userSession : entryCallSequence.getUserSessions()) {
                if (userSession.getSessionId().equals(ClusteringEvaluation.CUSTOMER_TAG)) {
                    this.listOfClusterAssignmentsCounter.get(index).increaseNumberOfUserGroupCustomer();
                } else if (userSession.getSessionId().equals(ClusteringEvaluation.STORE_MANAGER_TAG)) {
                    this.listOfClusterAssignmentsCounter.get(index).increaseNumberOfUserGroupStoreManager();
                } else if (userSession.getSessionId().equals(ClusteringEvaluation.STOCK_MANAGER_TAG)) {
                    this.listOfClusterAssignmentsCounter.get(index).increaseNumberOfUserGroupStockManager();
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
        return rand.nextInt(max - min + 1) + min;
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
        return min + (max - min) * r.nextDouble();
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

                if (entryCallEvent.getOperationSignature().equals("changePrice") && callDecisioner <= 0.3) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "changePrice", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("changePrice") && callDecisioner > 0.3
                        && callDecisioner <= 0.6) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "orderProduct", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("changePrice") && callDecisioner > 0.6) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "commit", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }

                if (entryCallEvent.getOperationSignature().equals("orderProduct") && callDecisioner <= 0.3) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "changePrice", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("orderProduct") && callDecisioner > 0.3
                        && callDecisioner <= 0.6) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "orderProduct", "class", "2", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("orderProduct") && callDecisioner > 0.6) {
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

                if (entryCallEvent.getOperationSignature().equals("checkDelivery") && callDecisioner <= 0.3) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "checkDelivery", "class", "3", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("checkDelivery") && callDecisioner > 0.3) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "updateStock", "class", "3", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }
                if (entryCallEvent.getOperationSignature().equals("updateStock") && callDecisioner <= 0.4) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, "checkDelivery", "class", "3", "hostname");
                    userSession.add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    continue;
                }

                if (entryCallEvent.getOperationSignature().equals("updateStock") && callDecisioner > 0.4) {
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
