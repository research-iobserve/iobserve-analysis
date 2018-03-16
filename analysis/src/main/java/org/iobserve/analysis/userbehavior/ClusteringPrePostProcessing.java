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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.analysis.userbehavior.data.ClusteringResults;
import org.iobserve.analysis.userbehavior.data.UserSessionAsCountsOfCalls;
import org.iobserve.analysis.userbehavior.data.WorkloadIntensity;
import org.iobserve.stages.general.data.EntryCallEvent;

/**
 * This class holds a set of preprocessing and postprocessing methods for the clustering of user
 * sessions.
 *
 * @author David Peter
 * @author Robert Heinrich
 */
public class ClusteringPrePostProcessing {

    /**
     * Sorts {@link UserSession} by the exit time.
     */
    private final Comparator<UserSession> sortUserSessionByExitTime = new Comparator<UserSession>() {

        @Override
        public int compare(final UserSession o1, final UserSession o2) {
            final long exitO1 = o1.getExitTime();
            final long exitO2 = o2.getExitTime();
            if (exitO1 > exitO2) {
                return 1;
            } else if (exitO1 < exitO2) {
                return -1;
            }
            return 0;
        }
    };

    /**
     * Sorts {@link UserSession} by the exit time.
     */
    private final Comparator<UserSession> sortUserSessionByEntryTime = new Comparator<UserSession>() {

        @Override
        public int compare(final UserSession o1, final UserSession o2) {
            final long entryO1 = ClusteringPrePostProcessing.this.getEntryTime(o1.getEvents());
            final long entryO2 = ClusteringPrePostProcessing.this.getEntryTime(o2.getEvents());
            if (entryO1 > entryO2) {
                return 1;
            } else if (entryO1 < entryO2) {
                return -1;
            }
            return 0;
        }
    };

    /**
     * Simple comparator for comparing the entry times.
     */
    private final Comparator<EntryCallEvent> sortEntryCallEventsByEntryTime = new Comparator<EntryCallEvent>() {

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

    /**
     * Default constructor.
     */
    public ClusteringPrePostProcessing() {

    }

    /**
     * It iterates over the userSessions and returns a list of distinct operation signatures
     * occurring within the userSessions' EntryCallEvents.
     *
     * @param userSessions
     *            contains all userSessions of the input EntryCallSequenceModel that will be
     *            clustered.
     * @return a list of distinct operation signatures occurring within the userSessions
     */
    public List<String> getListOfDistinctOperationSignatures(final List<UserSession> userSessions) {
        final List<String> listOfDistinctOperationSignatures = new ArrayList<>();
        for (final UserSession userSession : userSessions) {
            final Iterator<EntryCallEvent> iteratorEvents = userSession.iterator();
            while (iteratorEvents.hasNext()) {
                final EntryCallEvent event = iteratorEvents.next();
                if (!listOfDistinctOperationSignatures.contains(event.getOperationSignature())) {
                    listOfDistinctOperationSignatures.add(event.getOperationSignature());
                }
            }
        }
        return listOfDistinctOperationSignatures;
    }

    /**
     * Transforms the passed user sessions to counts of called operation signatures that can be used
     * for the similarity calculation of the user group clustering. The objective is to transform
     * each user session to a list that contains the number of calls of each distinct operation
     * signature. It parses through the entry call sequences of each user session and counts the
     * calls of each distinct operation signature. The result is a list of user sessions whose call
     * sequence is represented as counts of calls.
     *
     * @param userSessions
     *            are transformed to counts of calls
     * @param listOfDistinctOperationSignatures
     *            are the distinct operation signatures whose calls are counted for each user
     *            session
     * @return the passed user sessions as counts of calls
     */
    public List<UserSessionAsCountsOfCalls> getCallCountModel(final List<UserSession> userSessions,
            final List<String> listOfDistinctOperationSignatures) {

        final List<UserSessionAsCountsOfCalls> callCountModel = new ArrayList<>();

        // // Counts for each user session how often each distinct operation signature was called
        // during the user session
        for (final UserSession userSession : userSessions) {
            final UserSessionAsCountsOfCalls absoluteCountOfCalls = new UserSessionAsCountsOfCalls(
                    userSession.getSessionId(), listOfDistinctOperationSignatures.size());
            final List<EntryCallEvent> callSequence = userSession.getEvents();
            for (int i = 0; i < callSequence.size(); i++) {
                final String currentCall = callSequence.get(i).getOperationSignature();
                final int indexOfCurrentCall = listOfDistinctOperationSignatures.indexOf(currentCall);
                absoluteCountOfCalls.getAbsoluteCountOfCalls()[indexOfCurrentCall] = absoluteCountOfCalls
                        .getAbsoluteCountOfCalls()[indexOfCurrentCall] + 1;
            }
            callCountModel.add(absoluteCountOfCalls);
        }
        return callCountModel;
    }

    /**
     * It creates for each cluster(user group) its own entryCallSequenceModel. For that, each
     * entryCallSequenceModel receives exclusively the user group's assigned user sessions obtained
     * via the clustering. Additionally each entryCallSequenceModel receives the user group's
     * occurrence likelihood within the considered user sessions.
     *
     * @param clusteringResults
     *            hold the assignments of the clustering and the number of clusters
     * @param callSequenceModel
     *            is the input entryCallSequenceModel that holds all user sessions
     * @return for each cluster one entryCallSequenceModel. Each contains exclusively the cluster's
     *         assigned user sessions
     */
    public List<EntryCallSequenceModel> getForEachUserGroupAnEntryCallSequenceModel(
            final ClusteringResults clusteringResults, final EntryCallSequenceModel callSequenceModel) {
        final int numberOfClusters = clusteringResults.getNumberOfClusters();
        final int[] assignments = clusteringResults.getAssignments();
        final List<EntryCallSequenceModel> entryCallSequenceModels = new ArrayList<>(numberOfClusters);
        final double countOfAbsoluteUserSessions = callSequenceModel.getUserSessions().size();
        for (int k = 0; k < numberOfClusters; k++) {
            final List<UserSession> sessions = new ArrayList<>();
            int instanceNumber = 0;
            double countOfAssigendUserSessions = 0;
            for (final int clusterNum : assignments) {
                if (clusterNum == k) {
                    sessions.add(callSequenceModel.getUserSessions().get(instanceNumber));
                    countOfAssigendUserSessions++;
                }
                instanceNumber++;
            }
            if (sessions.isEmpty()) {
                continue;
            }
            final double relativeFrequencyOfUserGroup = countOfAssigendUserSessions / countOfAbsoluteUserSessions;
            entryCallSequenceModels.add(new EntryCallSequenceModel(sessions, relativeFrequencyOfUserGroup));
        }
        return entryCallSequenceModels;
    }

    /**
     * It calculates and sets for each entryCallSequenceModel its specific workload intensity. For
     * that it calculates an open or a closed workload and adds it to its entryCallSequenceModel.
     *
     * @param entryCallSequenceModels
     *            are the entryCallSequenceModels of the detected user groups
     * @param isClosedWorkload
     *            states whether a closed or an open workload is requested by the user
     */
    public void setTheWorkloadIntensityForTheEntryCallSequenceModels(
            final List<EntryCallSequenceModel> entryCallSequenceModels, final boolean isClosedWorkload) {
        for (final EntryCallSequenceModel entryCallSequenceModel : entryCallSequenceModels) {
            final WorkloadIntensity workloadIntensity = new WorkloadIntensity();
            if (isClosedWorkload) {
                this.calculateTheNumberOfConcurrentUsers(entryCallSequenceModel.getUserSessions(), workloadIntensity);
            } else {
                this.calculateInterarrivalTime(entryCallSequenceModel.getUserSessions(), workloadIntensity);
            }
            entryCallSequenceModel.setWorkloadIntensity(workloadIntensity);
        }
    }

    /**
     * Calculates the population count of closed workload specification. Therefore, the mean number
     * of concurrent user sessions is approximated: Divides the timeframe of the passed user
     * sessions into equal timeframes and calculates for each timeframe the number of concurrent
     * user sessions. Subsequently, it calculates the average number of concurrent users over the
     * ten frames and saves the obtained number as the closed workload population
     *
     * @param sessions
     *            to calculate the average number of concurrent user sessions from
     * @param workloadIntensity
     *            to set the obtained average concurrent number of user sessions
     */
    private void calculateTheNumberOfConcurrentUsers(final List<UserSession> sessions,
            final WorkloadIntensity workloadIntensity) {

        if (sessions.isEmpty()) {
            workloadIntensity.setMaxNumberOfConcurrentUsers(0);
            workloadIntensity.setAvgNumberOfConcurrentUsers(0);
            return;
        }

        Collections.sort(sessions, this.sortUserSessionByEntryTime);

        // Calculates the average interval within the user sessions
        long interval = 0;
        for (int i = 0; i < sessions.size(); i++) {
            final long entryTimeUS = this.getEntryTime(sessions.get(i).getEvents());
            final long exitTimeUS = sessions.get(i).getExitTime();
            interval += exitTimeUS - entryTimeUS;
        }
        final long avgInterval = interval / sessions.size();

        // Divides the overall time of the user sessions into a number of timeframes according to
        // the averageInterval
        final long startTimeOfAllUserSessions = this.getEntryTime(sessions.get(0).getEvents());
        final long endTimeOfAllUserSessions = sessions.get(sessions.size() - 1).getExitTime();
        final long numberOfTimeFrames = Math
                .round((endTimeOfAllUserSessions - startTimeOfAllUserSessions) / avgInterval + 0.5);

        // Set the start and end times and its initial count of each timeframe
        final List<long[]> timeframes = new ArrayList<>();
        long startTimeOfTimeframe = startTimeOfAllUserSessions;
        long endTimeOfTimeframe = startTimeOfAllUserSessions + avgInterval;
        for (int i = 0; i < numberOfTimeFrames; i++) {
            final long[] timeframe = new long[3];
            timeframe[0] = startTimeOfTimeframe;
            timeframe[1] = endTimeOfTimeframe;
            timeframe[2] = 0;
            timeframes.add(timeframe);
            startTimeOfTimeframe = endTimeOfTimeframe + 1;
            endTimeOfTimeframe = endTimeOfTimeframe + avgInterval + 1;
        }

        // Obtains for each user session its starting timeframe and increases the count of the
        // corrsponding timeframe
        for (int i = 0; i < sessions.size(); i++) {
            final long entryTimeUS = this.getEntryTime(sessions.get(i).getEvents());
            for (int j = 0; j < timeframes.size(); j++) {
                if (entryTimeUS >= timeframes.get(j)[0] && entryTimeUS <= timeframes.get(j)[1]) {
                    timeframes.get(j)[2] = timeframes.get(j)[2] + 1;
                    break;
                }
            }
        }

        // Calculates the average concurrent user sessions over the timeframes
        int numberOfConcurrentUserSessions = 0;
        for (int j = 0; j < timeframes.size(); j++) {
            numberOfConcurrentUserSessions += timeframes.get(j)[2] * timeframes.get(j)[2];
        }

        final int averageNumberOfConcurrentUsers = numberOfConcurrentUserSessions / sessions.size();

        workloadIntensity.setAvgNumberOfConcurrentUsers(averageNumberOfConcurrentUsers);
    }

    /**
     * Calculate the mean inter arrival time of the given user sessions.
     *
     * @param sessions
     *            sessions
     * @param workloadIntensity
     *            to set the obtained mean inter arrival time
     */
    private void calculateInterarrivalTime(final List<UserSession> sessions,
            final WorkloadIntensity workloadIntensity) {
        long interArrivalTime = 0;
        if (!sessions.isEmpty()) {
            // sort user sessions
            Collections.sort(sessions, this.sortUserSessionByExitTime);
            long sum = 0;
            for (int i = 0; i < sessions.size() - 1; i++) {
                final long exitTimeU1 = sessions.get(i).getEntryTime();
                final long exitTimeU2 = sessions.get(i + 1).getEntryTime();
                sum += exitTimeU2 - exitTimeU1;
            }
            final long numberSessions = sessions.size() > 1 ? sessions.size() - 1 : 1;
            interArrivalTime = sum / numberSessions;
        }
        workloadIntensity.setInterarrivalTimeOfUserSessions(interArrivalTime);
    }

    /**
     * Get the entry time of this entire session.
     *
     * @param events
     *            list of events.
     *
     * @return 0 if no events available at all and > 0 else.
     */
    public long getEntryTime(final List<EntryCallEvent> events) {
        long entryTime = 0;
        if (events.size() > 0) {
            this.sortEventsBy(this.sortEntryCallEventsByEntryTime, events);
            // Here was the bug: First element has to be returned instead of last
            entryTime = events.get(0).getEntryTime();
        }
        return entryTime;
    }

    /**
     * Sort by events.
     *
     * @param cmp
     *            comparator
     * @param events
     *            events
     */
    public void sortEventsBy(final Comparator<EntryCallEvent> cmp, final List<EntryCallEvent> events) {
        Collections.sort(events, cmp);
    }

}
