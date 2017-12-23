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
package org.iobserve.analysis.userbehavior.test.builder;

import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.userbehavior.test.ReferenceElements;
import org.iobserve.analysis.userbehavior.test.ReferenceUsageModelBuilder;
import org.iobserve.analysis.userbehavior.test.TestHelper;
import org.iobserve.stages.general.data.EntryCallEvent;

/**
 *
 * @author Reiner Jung -- refactoring
 * @author David Peter -- initial contribution
 *
 */
public final class CallSequenceScalabilityReference {

    /**
     * Helper functionality. Do not instantiate.
     */
    private CallSequenceScalabilityReference() {

    }

    /**
     * It creates a fixed number of user sessions. The number of calls per user session is
     * determined by the passed numberOfIterations parameter. It defines how often a fixed call
     * sequence is added to the call sequence of each user session. It is used to evaluate the
     * approach's response times with an increasing number of calls (RQ-3.2). It returns user
     * session that are used to execute the approach and to measure the response time. Thereby, this
     * method is called repeatedly to constantly increase the number of calls per UserSession.
     *
     * @param numberOfIterations
     *            defines how many calls per user session are created
     * @return user sessions
     */
    public static ReferenceElements getModel(final int numberOfIterations) {

        final ReferenceElements testElements = new ReferenceElements();
        final int numberOfUserSessions = 50;
        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                TestHelper.getUserSessions(numberOfUserSessions));

        boolean branchDecisionerUserGroup1 = true;
        boolean branchDecisionerUserGroup2 = true;

        for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {

            final double userGroupDecisioner = (double) i / (double) numberOfUserSessions;
            int entryTime = 1;
            int exitTime = 2;

            // It is distinguished between two user groups. The user groups differ from each other
            // by their operation signatures
            if (userGroupDecisioner < 0.3) {
                branchDecisionerUserGroup1 = !branchDecisionerUserGroup1;
                // The passed number of iterations parameter defines how often the user behavior of
                // each user session is iterated and thus the number of calls per user session
                for (int j = 0; j < numberOfIterations; j++) {
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
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[2], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                }
            } else {
                branchDecisionerUserGroup2 = !branchDecisionerUserGroup2;
                // The passed number of iterations parameter defines how often the user behavior of
                // each user session is iterated and thus the number of calls per user session
                for (int j = 0; j < numberOfIterations; j++) {
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
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[0], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                }
            }

        }

        // Returns the created user sessions. Now our approach can be executed and the response
        // times can be measured
        testElements.setEntryCallSequenceModel(entryCallSequenceModel);
        return testElements;
    }
}
