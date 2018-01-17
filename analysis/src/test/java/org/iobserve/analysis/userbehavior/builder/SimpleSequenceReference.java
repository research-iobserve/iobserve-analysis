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
package org.iobserve.analysis.userbehavior.builder;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.session.data.UserSession;
import org.iobserve.analysis.userbehavior.ReferenceElements;
import org.iobserve.analysis.userbehavior.ReferenceUsageModelBuilder;
import org.iobserve.analysis.userbehavior.TestHelper;
import org.iobserve.model.correspondence.Correspondent;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.factory.UsageModelFactory;
import org.iobserve.model.provider.neo4j.RepositoryModelProvider;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

/**
 * SimpleSequenceReference.
 *
 * @author David Peter -- initial contribution
 * @author Reiner Jung -- refactoring
 *
 */
public final class SimpleSequenceReference {

    /**
     * Factory.
     */
    private SimpleSequenceReference() {
    }

    /**
     * Creates a reference model that contains a simple sequence of calls. Accordingly, user
     * sessions whose call sequences contain a simple call sequence are created. (RQ-1.1) It is also
     * used to evaluate the accuracy of workload specifications. Therefore, varying workload is
     * generated by random entry and exit times of the user sessions, a random number of user
     * sessions for a closed workload specification and a random mean inter arrival time for an open
     * workload specification (RQ-1.9)
     *
     * @param referenceUsageModelFileName
     *            file name of the reference model to store its result
     * @param repositoryModelProvider
     *            repository model provider
     * @param correspondenceModel
     *            correspondence model
     * @param thinkTime
     *            of a closed workload.
     * @param isClosedWorkload
     *            decides whether a closed or an open workload is created
     * @return the reference usage model, a corresponding EntryCallSequenceModel and a reference
     *         workload
     * @throws IOException
     *             on error
     */
    public static ReferenceElements getModel(final String referenceUsageModelFileName,
            final RepositoryModelProvider repositoryModelProvider, final ICorrespondence correspondenceModel,
            final int thinkTime, final boolean isClosedWorkload) throws IOException {

        // Creates a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model.
        final int numberOfUsersSessions = TestHelper.getRandomInteger(200, 1);
        final int numberOfCalls = TestHelper.getRandomInteger(5, 1);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                TestHelper.getUserSessions(numberOfUsersSessions));
        final ReferenceElements referenceElements = new ReferenceElements();

        // In the following the reference usage model is created
        final UsageModel usageModel = UsageModelFactory.createUsageModel();
        final UsageScenario usageScenario = UsageModelFactory.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = UsageModelFactory.createAddStartAction("", scenarioBehaviour);
        final Stop stop = UsageModelFactory.createAddStopAction("", scenarioBehaviour);

        AbstractUserAction lastAction = start;

        Optional<Correspondent> correspondent;
        // According to the randomly set length of the call sequence, EntryLevelSystemCalls are
        // created
        for (int i = 0; i < numberOfCalls; i++) {
            if (i >= 0 && i < 5) {
                correspondent = correspondenceModel.getCorrespondent(ReferenceUsageModelBuilder.CLASS_SIGNATURE[i],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[i]);
            } else {
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (correspondent.isPresent()) {
                final EntryLevelSystemCall entryLevelSystemCall = UsageModelFactory
                        .createEntryLevelSystemCall(repositoryModelProvider, correspondent.get());
                UsageModelFactory.addUserAction(scenarioBehaviour, entryLevelSystemCall);
                UsageModelFactory.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
        }
        UsageModelFactory.connect(lastAction, stop);

        // According to the reference usage model user sessions are created that exactly represent
        // the user behavior of the reference usage model. The entry and exit times are set randomly
        // to evaluate a closed workload. For the evaluation of an open workload the mean inter
        // arrival time is set randomly
        int entryTime = 0;
        int exitTime = 1;
        final int meanInterArrivalTime = TestHelper.getRandomInteger(30, 1);

        for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
            if (isClosedWorkload) {
                entryTime = TestHelper.getRandomInteger(30, 1);
                exitTime = entryTime + 1;
            } else {
                entryTime += meanInterArrivalTime;
                exitTime += meanInterArrivalTime;
            }
            for (int k = 0; k < numberOfCalls; k++) {
                EntryCallEvent entryCallEvent = null;
                if (k >= 0 && k < 5) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[k],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[k], String.valueOf(i), "hostname");
                } else {
                    throw new IllegalArgumentException("Illegal value of model element parameter");
                }
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime = entryTime + 2;
                exitTime = exitTime + 2;
            }
        }

        // Saves the reference usage model and sets the usage model, the EntryCallSequenceModel
        // and the workload as the reference elements. Our approach is now executed with the
        // EntryCallSequenceModel and the resulting usage model can be matched against the reference
        // usage model. Alike, the by our approach calculated workload can be matched against the
        // reference workload. This is done by {@link
        // org.iobserve.analysis.userbehavior.test.WorkloadEvaluation}
        TestHelper.saveModel(usageModel, referenceUsageModelFileName);
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);
        referenceElements.setMeanInterArrivalTime(meanInterArrivalTime + numberOfCalls * 2);
        referenceElements.setMeanConcurrentUserSessions(
                SimpleSequenceReference.calculateTheNumberOfConcurrentUsers(entryCallSequenceModel.getUserSessions()));

        return referenceElements;
    }

    /**
     * Calculates the exact mean number of concurrent user sessions as a reference workload.
     *
     * @param sessions
     *            used to calculate the mean number of user sessions from
     * @return mean number of concurrent user sessions
     */
    private static int calculateTheNumberOfConcurrentUsers(final List<UserSession> sessions) {
        int averageNumberOfConcurrentUsers = 0;
        if (sessions.size() > 0) {
            int countOfConcurrentUsers = 0;
            Collections.sort(sessions, new Comparator<UserSession>() {

                @Override
                public int compare(final UserSession o1, final UserSession o2) {
                    final long entryO1 = SimpleSequenceReference.getEntryTime(o1.getEvents());
                    final long entryO2 = SimpleSequenceReference.getEntryTime(o2.getEvents());
                    if (entryO1 > entryO2) {
                        return 1;
                    } else if (entryO1 < entryO2) {
                        return -1;
                    }
                    return 0;
                }
            });

            for (int i = 0; i < sessions.size(); i++) {
                final long entryTimeUS1 = SimpleSequenceReference.getEntryTime(sessions.get(i).getEvents());
                final long exitTimeUS1 = sessions.get(i).getExitTime();
                int numberOfConcurrentUserSessionsDuringThisSession = 1;
                for (int j = 0; j < sessions.size(); j++) {
                    if (j == i) {
                        continue;
                    }
                    final long entryTimeUS2 = sessions.get(j).getEntryTime();
                    final long exitTimeUS2 = sessions.get(j).getExitTime();
                    if (exitTimeUS2 < entryTimeUS1) {
                        continue;
                    }
                    if (exitTimeUS1 >= entryTimeUS2) {
                        numberOfConcurrentUserSessionsDuringThisSession++;
                    }
                }

                countOfConcurrentUsers += numberOfConcurrentUserSessionsDuringThisSession;
            }
            averageNumberOfConcurrentUsers = countOfConcurrentUsers / sessions.size();
        }

        return averageNumberOfConcurrentUsers;
    }

    /**
     * Returns the entry time of a user sessions.
     *
     * @param events
     *            of an user sessions
     * @return the entry time of an user session
     */
    public static long getEntryTime(final List<EntryCallEvent> events) {
        long entryTime = 0;
        if (events.size() > 0) {
            Collections.sort(events, new Comparator<EntryCallEvent>() {

                @Override
                public int compare(final EntryCallEvent o1, final EntryCallEvent o2) {
                    if (o1.getEntryTime() > o2.getEntryTime()) {
                        return 1;
                    } else if (o1.getEntryTime() < o2.getEntryTime()) {
                        return -1;
                    }
                    return 0;
                }
            });
            entryTime = events.get(0).getEntryTime();
        }
        return entryTime;
    }

}
