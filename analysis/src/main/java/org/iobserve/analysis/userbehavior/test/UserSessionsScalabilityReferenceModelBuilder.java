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

import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.stages.general.data.EntryCallEvent;

/**
 *
 * @author Reiner Jung -- refactoring
 * @author David Peters -- initial contribution
 *
 */
public final class UserSessionsScalabilityReferenceModelBuilder {

    /**
     * Helper class.
     */
    private UserSessionsScalabilityReferenceModelBuilder() {

    }

    /**
     * It creates the passed number of user sessions. Thereby, two user groups are distinguished.
     * Each user session of a user group contains the same call sequence. The call sequences between
     * the user groups differ from each other by their operation signatures. It is used to evaluate
     * the approach's response times with an increasing number of user sessions (RQ-3.1) It returns
     * user session that are used to execute the approach and to measure the response time Thereby,
     * this method is called repeatedly to constantly increase the number of UserSessions
     *
     *
     * @param numberOfUserSessions
     *            defines the number of user sessions to create
     * @return user sessions with a fixed user behavior
     */
    public static ReferenceElements getIncreasingUserSessionsScalabilityReferenceModel(final int numberOfUserSessions) {

        final ReferenceElements testElements = new ReferenceElements();
        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                TestHelper.getUserSessions(numberOfUserSessions));

        int entryTime = 1;
        int exitTime = 2;

        // Creates for each of the user sessions the same user behavior. This ensures that the
        // progressions of the response time can be ascribed to the increasing number of user
        // sessions
        for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {

            final double userGroupDecisioner = (double) i / (double) numberOfUserSessions;

            // It is distinguished between two user groups. The user groups differ from each other
            // by their operation signatures
            if (userGroupDecisioner < 0.3) {
                // One single call
                EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[0], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                // An iterated call to represent a loop
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[1],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[1], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[1],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[1], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                // Alternative calls to represent a branch
                final int branchDecisioner = TestHelper.getRandomInteger(2, 1);
                if (branchDecisioner == 1) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[0], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                } else {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[1],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[1], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                }
                // An equal call to merge the branch transitions
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[0], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
            } else {
                // One single call
                EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[3],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[3], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                // An iterated call to represent a loop
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[4], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[4], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                // Alternative calls to represent a branch
                final int branchDecisioner = TestHelper.getRandomInteger(2, 1);
                if (branchDecisioner == 1) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[3],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[3], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                } else {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[4], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                }
                // An equal call to merge the branch transitions
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[3],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[3], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
            }

        }

        // Returns the created user sessions. Now our approach can be executed and the response
        // times can be measured
        testElements.setEntryCallSequenceModel(entryCallSequenceModel);
        return testElements;
    }
}
