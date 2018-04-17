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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.iobserve.analysis.userbehavior.ReferenceElements;
import org.iobserve.analysis.userbehavior.ReferenceUsageModelBuilder;
import org.iobserve.analysis.userbehavior.TestHelper;
import org.iobserve.model.correspondence.Correspondent;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.factory.UsageModelFactory;
import org.iobserve.model.provider.RepositoryLookupModelProvider;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

/**
 * SimpleBranchReference.
 *
 * @author David Peter -- initial contribution
 * @author Reiner Jung -- refactoring
 *
 */
public final class SimpleBranchReference {

    /**
     * Factory.
     */
    private SimpleBranchReference() {
    }

    /**
     * Creates a reference model that contains a branch element. Accordingly, user sessions whose
     * call sequences differ from each other at the position of the branch are created.(RQ-1.2)
     *
     * @param referenceModelFileName
     *            file name of the reference model to store its result
     * @param repositoryLookupModelProvider
     *            repository model provider
     * @param correspondenceModel
     *            correspondence model
     *
     * @return a reference usage model and corresponding user sessions
     * @throws IOException
     *             on error
     */
    public static ReferenceElements getModel(final String referenceModelFileName,
            final RepositoryLookupModelProvider repositoryLookupModelProvider,
            final ICorrespondence correspondenceModel) throws IOException {

        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model. The minimum number of user sessions is set
        // dependently from the random number of branch transitions, because it must be ensured that
        // each branch transition is represented within the user sessions.
        final int numberOfBranchTransitions = TestHelper.getRandomInteger(5, 2);
        final int numberOfConcurrentUsers = TestHelper.getRandomInteger(200, 10 * numberOfBranchTransitions);

        final UserSessionCollectionModel entryCallSequenceModel = new UserSessionCollectionModel(
                TestHelper.getUserSessions(numberOfConcurrentUsers));

        // In the following the reference usage model is created
        Optional<Correspondent> correspondent;

        final UsageModel usageModel = UsageModelFactory.createUsageModel();
        final UsageScenario usageScenario = UsageModelFactory.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();

        final Start start = UsageModelFactory.createAddStartAction("", scenarioBehaviour);
        final Stop stop = UsageModelFactory.createAddStopAction("", scenarioBehaviour);

        AbstractUserAction lastAction = start;

        // Creates the branch element and the branch transitions according to the random number of
        // branch transitions
        final org.palladiosimulator.pcm.usagemodel.Branch branch = UsageModelFactory.createBranch("",
                scenarioBehaviour);
        UsageModelFactory.connect(start, branch);
        // For each branch transition an alternative EntryLevelSystemCall is created to represent
        // the alternative call sequences
        for (int i = 0; i < numberOfBranchTransitions; i++) {
            final BranchTransition branchTransition = UsageModelFactory.createBranchTransition(branch);
            final ScenarioBehaviour branchTransitionBehaviour = branchTransition
                    .getBranchedBehaviour_BranchTransition();
            final Start startBranchTransition = UsageModelFactory.createAddStartAction("", branchTransitionBehaviour);
            final Stop stopBranchTransition = UsageModelFactory.createAddStopAction("", branchTransitionBehaviour);

            lastAction = startBranchTransition;

            if (i >= 0 && i < 5) {
                correspondent = correspondenceModel.getCorrespondent(ReferenceUsageModelBuilder.CLASS_SIGNATURE[i],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[i]);
            } else {
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (correspondent.isPresent()) {
                final EntryLevelSystemCall entryLevelSystemCall = UsageModelFactory
                        .createEntryLevelSystemCall(repositoryLookupModelProvider, correspondent.get());
                UsageModelFactory.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                UsageModelFactory.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            if (i == 0) {
                correspondent = correspondenceModel.getCorrespondent(ReferenceUsageModelBuilder.CLASS_SIGNATURE[1],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[1]);
            } else {
                correspondent = correspondenceModel.getCorrespondent(ReferenceUsageModelBuilder.CLASS_SIGNATURE[0],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0]);
            }
            if (correspondent.isPresent()) {
                final EntryLevelSystemCall entryLevelSystemCall = UsageModelFactory
                        .createEntryLevelSystemCall(repositoryLookupModelProvider, correspondent.get());
                UsageModelFactory.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                UsageModelFactory.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }

            UsageModelFactory.connect(lastAction, stopBranchTransition);
        }
        correspondent = correspondenceModel.getCorrespondent(ReferenceUsageModelBuilder.CLASS_SIGNATURE[2],
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2]);
        if (correspondent.isPresent()) {
            final EntryLevelSystemCall entryLevelSystemCall = UsageModelFactory
                    .createEntryLevelSystemCall(repositoryLookupModelProvider, correspondent.get());
            UsageModelFactory.addUserAction(scenarioBehaviour, entryLevelSystemCall);
            UsageModelFactory.connect(branch, entryLevelSystemCall);
            lastAction = entryLevelSystemCall;
        }
        UsageModelFactory.connect(lastAction, stop);

        final List<Integer> branchTransitionCounter = SimpleBranchReference
                .computeBranchTransitions(entryCallSequenceModel, branch, numberOfBranchTransitions);

        // Set the likelihoods of the branch transitions of the reference usage model according to
        // the randomly created user sessions
        for (int i = 0; i < branch.getBranchTransitions_Branch().size(); i++) {
            branch.getBranchTransitions_Branch().get(i)
                    .setBranchProbability((double) branchTransitionCounter.get(i) / numberOfConcurrentUsers);
        }

        return SimpleBranchReference.saveReferenceElements(usageModel, referenceModelFileName, entryCallSequenceModel);
    }

    /**
     * According to the reference usage model user sessions are created that exactly represent the
     * user behavior of the reference usage model. The entry and exit times enable that the calls
     * within the user sessions are ordered according to the reference usage model The branch
     * transition counter ensures that each branch transition is visited by at least one user
     * session.
     *
     * @param entryCallSequenceModel
     * @param branch
     * @param numberOfBranchTransitions
     *
     * @return list of branch transition counter
     */
    private static List<Integer> computeBranchTransitions(final UserSessionCollectionModel entryCallSequenceModel,
            final Branch branch, final int numberOfBranchTransitions) {

        final List<Integer> branchTransitionCounter = new ArrayList<>();
        boolean areAllBranchesVisited = true;

        do {
            for (int i = 0; i < branch.getBranchTransitions_Branch().size(); i++) {
                branchTransitionCounter.add(i, 0);
            }
            for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
                // Each user sessions represents randomly one of the branch transitions
                final int branchDecisioner = TestHelper.getRandomInteger(numberOfBranchTransitions - 1, 0);
                if (branchDecisioner == 0) {
                    SimpleBranchReference.sequenceConstructor(branchTransitionCounter, 0, entryCallSequenceModel, i, 0,
                            1);
                } else if (branchDecisioner == 1) {
                    SimpleBranchReference.sequenceConstructor(branchTransitionCounter, 1, entryCallSequenceModel, i, 1,
                            0);
                } else if (branchDecisioner == 2) {
                    SimpleBranchReference.sequenceConstructor(branchTransitionCounter, 2, entryCallSequenceModel, i, 2,
                            0);
                } else if (branchDecisioner == 3) {
                    SimpleBranchReference.sequenceConstructor(branchTransitionCounter, 3, entryCallSequenceModel, i, 3,
                            0);
                } else if (branchDecisioner == 4) {
                    SimpleBranchReference.sequenceConstructor(branchTransitionCounter, 4, entryCallSequenceModel, i, 4,
                            0);
                } else {
                    throw new IllegalArgumentException("Illegal value of model element parameter");
                }
                final EntryCallEvent entryCallEvent3 = new EntryCallEvent(3, 4,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[2], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent3, true);
            }
            // Checks whether all branch transitions are represented within the user sessions
            for (int i = 0; i < branchTransitionCounter.size(); i++) {
                if (branchTransitionCounter.get(i) == 0) {
                    areAllBranchesVisited = false;
                    break;
                }
            }
        } while (!areAllBranchesVisited);

        return branchTransitionCounter;
    }

    /**
     * Saves the reference usage model and sets the usage model and the EntryCallSequenceModel as
     * the reference elements. Our approach is now executed with the EntryCallSequenceModel and the
     * resulting usage model can be matched against the reference usage model
     *
     * @param usageModel
     *            usage model
     * @param referenceModelFileName
     *            reference model file name
     * @param entryCallSequenceModel
     *            entry call sequence model
     *
     * @return reference elements
     * @throws IOException
     *             when the file is written
     */
    private static ReferenceElements saveReferenceElements(final UsageModel usageModel,
            final String referenceModelFileName, final UserSessionCollectionModel entryCallSequenceModel)
            throws IOException {
        final ReferenceElements referenceElements = new ReferenceElements();

        TestHelper.saveModel(usageModel, referenceModelFileName);
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }

    /**
     * Sequence constructor.
     *
     * @param branchTransitionCounter
     * @param index
     * @param entryCallSequenceModel
     * @param loopCount
     */
    private static void sequenceConstructor(final List<Integer> branchTransitionCounter, final int index,
            final UserSessionCollectionModel entryCallSequenceModel, final int loopCount, final int start, final int end) {
        final int countOfBranchTransition = branchTransitionCounter.get(index) + 1;
        branchTransitionCounter.set(index, countOfBranchTransition);
        final EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2,
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[start],
                ReferenceUsageModelBuilder.CLASS_SIGNATURE[start], String.valueOf(loopCount), "hostname");
        entryCallSequenceModel.getUserSessions().get(loopCount).add(entryCallEvent, true);
        final EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4,
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[end], ReferenceUsageModelBuilder.CLASS_SIGNATURE[end],
                String.valueOf(loopCount), "hostname");
        entryCallSequenceModel.getUserSessions().get(loopCount).add(entryCallEvent2, true);
    }
}
