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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.data.UserSession;
import org.iobserve.analysis.model.builder.UsageModelBuilder;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.model.provider.RepositoryModelProvider;
import org.iobserve.analysis.userbehavior.test.ReferenceElements;
import org.iobserve.analysis.userbehavior.test.ReferenceUsageModelBuilder;
import org.iobserve.analysis.userbehavior.test.TestHelper;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

/**
 *
 * @author Reiner Jung -- refactoring
 * @author David Peters -- initial contribution
 *
 */
public final class BranchWithinLoopReference {

    /**
     * Helper class.
     */
    private BranchWithinLoopReference() {

    }

    /**
     * It creates a reference usage model that contains branches within loops. Accordingly, user
     * sessions whose call sequences differ from each other are iterated in a row. Thereby, at each
     * iteration of a branched call sequences the probabilities have to be equal because otherwise
     * it would not be an iteration (RQ-1.8)
     *
     * @param referenceUsageModelFileName
     *            reference usage model file name
     * @param repositoryModelProvider
     *            repository model builder
     * @param correspondenceModel
     *            correspondence model
     *
     * @return reference usage model and corresponding user sessions
     * @throws IOException
     *             on error
     */
    public static ReferenceElements getModel(final String referenceUsageModelFileName,
            final RepositoryModelProvider repositoryModelProvider, final ICorrespondence correspondenceModel)
            throws IOException {

        // The number of model element parameters are created randomly. The number of user sessions
        // must be created accordingly to the number of branch transitions, because it must be
        // ensured that at each iteration of an branch the branch transition probabilities are
        // equal. This can be achieved by the same number of user sessions representing the branch
        // transition at each iteration
        final int numberOfLoops = TestHelper.getRandomInteger(3, 3);
        final int numberOfConcurrentUsers = (int) Math.pow(2, numberOfLoops) * 5;

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                TestHelper.getUserSessions(numberOfConcurrentUsers));
        final ReferenceElements testElements = new ReferenceElements();

        // In the following the reference usage model is created
        final UsageModel usageModel = UsageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = UsageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();

        final Start start = UsageModelBuilder.createAddStartAction("", scenarioBehaviour);
        final Stop stop = UsageModelBuilder.createAddStopAction("", scenarioBehaviour);

        final AbstractUserAction lastAction = start;

        // The loop element is created that contains the iterated branch
        final Loop loop = UsageModelBuilder.createLoop("", scenarioBehaviour);
        final ScenarioBehaviour loopScenarioBehaviour = loop.getBodyBehaviour_Loop();
        UsageModelBuilder.connect(lastAction, loop);

        final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmLoopIteration.setSpecification(String.valueOf(numberOfLoops));
        loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
        UsageModelBuilder.connect(loop, stop);
        final Start loopStart = UsageModelBuilder.createAddStartAction("", loopScenarioBehaviour);
        final Stop loopStop = UsageModelBuilder.createAddStopAction("", loopScenarioBehaviour);

        // The branch that is contained within the loop element is created
        final org.palladiosimulator.pcm.usagemodel.Branch branch = UsageModelBuilder.createBranch("",
                loopScenarioBehaviour);
        UsageModelBuilder.connect(loopStart, branch);

        // The branch transition 1 is created
        final BranchTransition branchTransition1 = BranchWithinLoopReference.createBranchTransition(2,
                repositoryModelProvider, branch, correspondenceModel);

        // The branch transition 2 is created
        final BranchTransition branchTransition2 = BranchWithinLoopReference.createBranchTransition(3,
                repositoryModelProvider, branch, correspondenceModel);

        final Optional<Correspondent> optionCorrespondent = correspondenceModel.getCorrespondent(
                ReferenceUsageModelBuilder.CLASS_SIGNATURE[4], ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4]);
        if (optionCorrespondent.isPresent()) {
            final Correspondent correspondent = optionCorrespondent.get();
            final EntryLevelSystemCall entryLevelSystemCall = UsageModelBuilder
                    .createEntryLevelSystemCall(repositoryModelProvider, correspondent);
            UsageModelBuilder.addUserAction(loopScenarioBehaviour, entryLevelSystemCall);
            UsageModelBuilder.connect(branch, entryLevelSystemCall);
            UsageModelBuilder.connect(entryLevelSystemCall, loopStop);
        }

        // User sessions according to the reference usage model are created. Thereby, it must be
        // ensured that each iteration of the branch the branch probabilities stay the same because
        // the branch is iterated. This can be achieved by an equal number of user sessions for each
        // branch transition at each iteration of the branch
        int countOfCallEvent3 = 0;
        int countOfCallEvent4 = 0;
        int entryTime = 1;
        int exitTime = 2;
        boolean branchDecision = false;

        // At each iteration the user sessions are distributed equally between the branch
        // transitions to ensure that the probabilities of the branch transitions stay equally
        final Map<Integer, List<List<UserSession>>> userSessionGroups = new HashMap<>();
        final List<List<UserSession>> startList = new ArrayList<>();
        startList.add(entryCallSequenceModel.getUserSessions());
        userSessionGroups.put(0, startList);

        // The loop that contains the branch
        for (int j = 0; j < numberOfLoops; j++) {

            countOfCallEvent3 = 0;
            countOfCallEvent4 = 0;

            final List<List<UserSession>> newUserSessionGroups = new ArrayList<>();

            // Ensures that the user sessions distribution stays equally
            for (int k = 0; k < userSessionGroups.get(j).size(); k++) {

                for (int i = 0; i < 2; i++) {
                    final List<UserSession> userSessions = new ArrayList<>();
                    newUserSessionGroups.add(userSessions);
                }

                final int indexGroupCallEvent3 = newUserSessionGroups.size() - 2;
                final int indexGroupCallEvent4 = newUserSessionGroups.size() - 1;

                for (int i = 0; i < userSessionGroups.get(j).get(k).size(); i++) {

                    if (newUserSessionGroups.get(indexGroupCallEvent3).size() > newUserSessionGroups
                            .get(indexGroupCallEvent4).size()) {
                        branchDecision = false;
                    } else {
                        branchDecision = true;
                    }

                    // The branch within the loop
                    if (branchDecision) {
                        BranchWithinLoopReference.createEntryCall(entryTime, exitTime, 2, k, j, i, userSessionGroups,
                                newUserSessionGroups, indexGroupCallEvent3);
                        countOfCallEvent3++;
                        entryTime += 2;
                        exitTime += 2;
                    } else {
                        BranchWithinLoopReference.createEntryCall(entryTime, exitTime, 3, k, j, i, userSessionGroups,
                                newUserSessionGroups, indexGroupCallEvent4);
                        countOfCallEvent4++;
                        entryTime += 2;
                        exitTime += 2;
                    }

                    final EntryCallEvent entryCallEvent5 = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[4], String.valueOf(i), "hostname");
                    userSessionGroups.get(j).get(k).get(i).add(entryCallEvent5, true);
                    entryTime -= 2;
                    exitTime -= 2;
                }

            }

            userSessionGroups.put(j + 1, newUserSessionGroups);
            entryTime += 2;
            exitTime += 2;
        }

        // Sets the likelihoods of branch transitions
        final double likelihoodOfCallEvent3 = (double) countOfCallEvent3 / (double) numberOfConcurrentUsers;
        final double likelihoodOfCallEvent4 = (double) countOfCallEvent4 / (double) numberOfConcurrentUsers;
        branchTransition1.setBranchProbability(likelihoodOfCallEvent3);
        branchTransition2.setBranchProbability(likelihoodOfCallEvent4);

        // Saves the reference usage model and sets the usage model and the EntryCallSequenceModel
        // as the reference elements. Our approach is now executed with the EntryCallSequenceModel
        // and the resulting usage model can be matched against the reference usage model
        TestHelper.saveModel(usageModel, referenceUsageModelFileName);
        testElements.setEntryCallSequenceModel(entryCallSequenceModel);
        testElements.setUsageModel(usageModel);

        return testElements;
    }

    /**
     * Create entry call.
     *
     * @param entryTime
     *            entry time
     * @param exitTime
     *            exit time
     * @param index
     *            signature index
     * @param k
     *            k
     * @param j
     *            j
     * @param i
     *            i
     * @param userSessionGroups
     *            user session groups
     * @param newUserSessionGroups
     *            new user session groups
     * @param indexGroupCallEvent
     *            index group call event
     */
    private static void createEntryCall(final int entryTime, final int exitTime, final int index, final int k,
            final int j, final int i, final Map<Integer, List<List<UserSession>>> userSessionGroups,
            final List<List<UserSession>> newUserSessionGroups, final int indexGroupCallEvent) {
        final EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[index],
                ReferenceUsageModelBuilder.CLASS_SIGNATURE[index], String.valueOf(i), "hostname");
        userSessionGroups.get(j).get(k).get(i).add(entryCallEvent, true);
        newUserSessionGroups.get(indexGroupCallEvent).add(userSessionGroups.get(j).get(k).get(i));
    }

    /**
     * Create a branch transition.
     *
     * @param callId
     *            id of the operation class and operation signature
     * @param repositoryModelProvider
     *            usage model builder
     * @param branch
     *            pcm branch element
     * @param correspondenceModel
     *            correspondence model data
     *
     * @return returns the created branch transition or null on error
     */
    private static BranchTransition createBranchTransition(final int callId,
            final RepositoryModelProvider repositoryModelProvider,
            final org.palladiosimulator.pcm.usagemodel.Branch branch, final ICorrespondence correspondenceModel) {
        final BranchTransition branchTransition = UsageModelBuilder.createBranchTransition(branch);
        final ScenarioBehaviour branchTransitionBehaviour = branchTransition.getBranchedBehaviour_BranchTransition();

        final Start start = UsageModelBuilder.createStart("");
        UsageModelBuilder.addUserAction(branchTransitionBehaviour, start);

        final Stop stop = UsageModelBuilder.createStop("");
        UsageModelBuilder.addUserAction(branchTransitionBehaviour, stop);

        final Optional<Correspondent> optionCorrespondent = correspondenceModel.getCorrespondent(
                ReferenceUsageModelBuilder.CLASS_SIGNATURE[callId],
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[callId]);
        if (optionCorrespondent.isPresent()) {
            final Correspondent correspondent = optionCorrespondent.get();
            final EntryLevelSystemCall entryLevelSystemCall = UsageModelBuilder
                    .createEntryLevelSystemCall(repositoryModelProvider, correspondent);
            UsageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
            UsageModelBuilder.connect(start, entryLevelSystemCall);
            UsageModelBuilder.connect(entryLevelSystemCall, stop);

            return branchTransition;
        } else {
            return null;
        }
    }
}
