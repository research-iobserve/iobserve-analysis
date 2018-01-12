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

import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.model.correspondence.Correspondent;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.model.factory.UsageModelFactory;
import org.iobserve.analysis.model.provider.file.RepositoryModelProvider;
import org.iobserve.analysis.userbehavior.ReferenceElements;
import org.iobserve.analysis.userbehavior.ReferenceUsageModelBuilder;
import org.iobserve.analysis.userbehavior.TestHelper;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

/**
 * BranchWithinBranchReference.
 *
 * @author David Peter -- initial contribution
 * @author Reiner Jung -- refactoring *
 */
public final class BranchWithinBranchReference {

    /**
     * Factory.
     */
    private BranchWithinBranchReference() {

    }

    /**
     * It creates a reference usage model that contains nested branches. Accordingly, user sessions
     * whose call sequences differ from each other at the positions of the branches are
     * created.(RQ-1.5)
     *
     * @param referenceUsageModelFileName
     *            file name of the reference model to store its result
     * @param usageModelBuilder
     *            usage model builder
     * @param repositoryModelProvider
     *            repository model provider
     * @param correspondenceModel
     *            correspondence model
     *
     * @return a reference usage model and corresponding user sessions
     * @throws IOException
     *             on error
     */
    public static ReferenceElements getModel(final String referenceUsageModelFileName,
            final UsageModelFactory usageModelBuilder, final RepositoryModelProvider repositoryModelProvider,
            final ICorrespondence correspondenceModel) throws IOException {

        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model. The minimum number of user sessions is set
        // dependently from the random number of branch transitions, because it must be ensured that
        // each branch transition is represented within the user sessions.
        final int numberOfTransitionsOfExteriorBranch = TestHelper.getRandomInteger(3, 2);
        final int numberOfTransitionsOfInteriorBranches = TestHelper.getRandomInteger(3, 2);
        final int numberOfConcurrentUsers = TestHelper.getRandomInteger(200, 10 * numberOfTransitionsOfExteriorBranch);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                TestHelper.getUserSessions(numberOfConcurrentUsers));

        final ReferenceElements referenceElements = new ReferenceElements();

        final List<Integer> branchTransitionCounter = new ArrayList<>();
        final List<List<Integer>> listOfbranchTransitionCounterInterior = new ArrayList<>();

        BranchWithinBranchReference.createUserSessions(branchTransitionCounter, listOfbranchTransitionCounterInterior,
                numberOfTransitionsOfExteriorBranch, numberOfTransitionsOfInteriorBranches, entryCallSequenceModel);

        final UsageModel usageModel = BranchWithinBranchReference.createTheReferenceModel(usageModelBuilder,
                repositoryModelProvider, correspondenceModel, numberOfTransitionsOfExteriorBranch,
                numberOfTransitionsOfInteriorBranches, numberOfConcurrentUsers, branchTransitionCounter,
                listOfbranchTransitionCounterInterior);

        // Saves the reference usage model and sets the usage model and the EntryCallSequenceModel
        // as the reference elements. Our approach is now executed with the EntryCallSequenceModel
        // and the resulting usage model can be matched against the reference usage model
        TestHelper.saveModel(usageModel, referenceUsageModelFileName);
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }

    /**
     * User sessions are created that exactly represent the user behavior of the reference usage
     * model. The entry and exit times enable that the calls within the user sessions are ordered
     * according to the reference usage model. The branch transition counter ensures that each
     * branch transition is visited by at least one user session
     *
     * @param branchTransitionCounter
     * @param listOfbranchTransitionCounterInterior
     * @param numberOfTransitionsOfExteriorBranch
     * @param numberOfTransitionsOfInteriorBranches
     * @param entryCallSequenceModel
     */
    private static void createUserSessions(final List<Integer> branchTransitionCounter,
            final List<List<Integer>> listOfbranchTransitionCounterInterior,
            final int numberOfTransitionsOfExteriorBranch, final int numberOfTransitionsOfInteriorBranches,
            final EntryCallSequenceModel entryCallSequenceModel) {
        boolean areAllBranchesVisited = true;

        do {
            for (int i = 0; i < numberOfTransitionsOfExteriorBranch; i++) {
                branchTransitionCounter.add(i, 0);
                final List<Integer> branchTransitionCounterInterior = new ArrayList<>();
                for (int j = 0; j < numberOfTransitionsOfInteriorBranches; j++) {
                    branchTransitionCounterInterior.add(j, 0);
                }
                listOfbranchTransitionCounterInterior.add(i, branchTransitionCounterInterior);
            }
            int entryTime = 1;

            for (int userSessionIndex = 0; userSessionIndex < entryCallSequenceModel.getUserSessions()
                    .size(); userSessionIndex++) {
                entryTime = 1;
                // Each user sessions represents randomly one of the branch transitions
                final int branchDecisioner = TestHelper.getRandomInteger(numberOfTransitionsOfExteriorBranch - 1, 0);
                if (branchDecisioner < 3) {
                    entryTime = BranchWithinBranchReference.createBranch(branchTransitionCounter, entryTime,
                            entryCallSequenceModel, numberOfTransitionsOfInteriorBranches,
                            listOfbranchTransitionCounterInterior, userSessionIndex, branchDecisioner);
                }
            }
            // Checks whether all branch transitions of the exterior branch are represented
            // within
            // the user sessions
            for (int i = 0; i < branchTransitionCounter.size(); i++) {
                if (branchTransitionCounter.get(i) == 0) {
                    areAllBranchesVisited = false;
                    break;
                }
            }
            // Checks whether all branch transitions of the interior branches are represented
            // within
            // the user sessions
            for (final List<Integer> branchTransitionCounterInterior : listOfbranchTransitionCounterInterior) {
                for (int j = 0; j < branchTransitionCounterInterior.size(); j++) {
                    if (branchTransitionCounterInterior.get(j) == 0) {
                        areAllBranchesVisited = false;
                        break;
                    }
                }
                if (!areAllBranchesVisited) {
                    break;
                }
            }
            if (!areAllBranchesVisited) {
                listOfbranchTransitionCounterInterior.clear();
                branchTransitionCounter.clear();
            }
        } while (!areAllBranchesVisited);
    }

    /**
     * Creates the reference model.
     *
     * @param usageModelBuilder
     * @param correspondenceModel
     * @param numberOfTransitionsOfExteriorBranch
     * @param numberOfTransitionsOfInteriorBranches
     * @param numberOfConcurrentUsers
     * @param branchTransitionCounter
     * @param listOfbranchTransitionCounterInterior
     * @return
     */
    private static UsageModel createTheReferenceModel(final UsageModelFactory usageModelBuilder,
            final RepositoryModelProvider repositoryModelProvider, final ICorrespondence correspondenceModel,
            final int numberOfTransitionsOfExteriorBranch, final int numberOfTransitionsOfInteriorBranches,
            final int numberOfConcurrentUsers, final List<Integer> branchTransitionCounter,
            final List<List<Integer>> listOfbranchTransitionCounterInterior) {
        // In the following the reference usage model is created
        AbstractUserAction lastAction;
        Optional<Correspondent> optionCorrespondent;
        final UsageModel usageModel = UsageModelFactory.createUsageModel();
        final UsageScenario usageScenario = UsageModelFactory.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = UsageModelFactory.createAddStartAction("", scenarioBehaviour);
        final Stop stop = UsageModelFactory.createAddStopAction("", scenarioBehaviour);
        lastAction = start;

        // The exterior branch is created
        final org.palladiosimulator.pcm.usagemodel.Branch branch = UsageModelFactory.createBranch("",
                scenarioBehaviour);
        UsageModelFactory.connect(lastAction, branch);
        UsageModelFactory.connect(branch, stop);

        // Creates branch transitions according to the random countOfBranchTransitions
        for (int i = 0; i < numberOfTransitionsOfExteriorBranch; i++) {
            final BranchTransition branchTransition = UsageModelFactory.createBranchTransition(branch);
            final ScenarioBehaviour branchTransitionBehaviour = branchTransition
                    .getBranchedBehaviour_BranchTransition();
            branchTransition
                    .setBranchProbability((double) branchTransitionCounter.get(i) / (double) numberOfConcurrentUsers);
            final Start startBranchTransition = UsageModelFactory.createStart("");
            UsageModelFactory.addUserAction(branchTransitionBehaviour, startBranchTransition);
            final Stop stopBranchTransition = UsageModelFactory.createStop("");
            UsageModelFactory.addUserAction(branchTransitionBehaviour, stopBranchTransition);
            lastAction = startBranchTransition;
            if (i >= 0 && i < 3) {
                optionCorrespondent = correspondenceModel.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[i],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[i]);
            } else {
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final EntryLevelSystemCall entryLevelSystemCall = UsageModelFactory
                        .createEntryLevelSystemCall(repositoryModelProvider, optionCorrespondent.get());
                UsageModelFactory.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                UsageModelFactory.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }

            // The interior branch is created
            final org.palladiosimulator.pcm.usagemodel.Branch branchInterior = UsageModelFactory.createBranch("",
                    branchTransitionBehaviour);
            UsageModelFactory.connect(lastAction, branchInterior);
            UsageModelFactory.connect(branchInterior, stopBranchTransition);

            for (int j = 0; j < numberOfTransitionsOfInteriorBranches; j++) {
                final BranchTransition branchTransitionInterior = UsageModelFactory
                        .createBranchTransition(branchInterior);
                final ScenarioBehaviour branchTransitionBehaviourInterior = branchTransitionInterior
                        .getBranchedBehaviour_BranchTransition();
                branchTransitionInterior
                        .setBranchProbability((double) listOfbranchTransitionCounterInterior.get(i).get(j)
                                / (double) branchTransitionCounter.get(i));

                final Start startBranchTransitionInterior = UsageModelFactory.createAddStartAction("",
                        branchTransitionBehaviourInterior);
                final Stop stopBranchTransitionInterior = UsageModelFactory.createAddStopAction("",
                        branchTransitionBehaviourInterior);
                lastAction = startBranchTransitionInterior;
                switch (j) {
                case 0:
                    optionCorrespondent = correspondenceModel.getCorrespondent(
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[0],
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0]);
                    break;
                case 1:
                    optionCorrespondent = correspondenceModel.getCorrespondent(
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[3],
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[3]);
                    break;
                case 2:
                    optionCorrespondent = correspondenceModel.getCorrespondent(
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[4],
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4]);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal value of model element parameter");
                }
                if (optionCorrespondent.isPresent()) {
                    final Correspondent correspondent = optionCorrespondent.get();
                    final EntryLevelSystemCall entryLevelSystemCall = UsageModelFactory
                            .createEntryLevelSystemCall(repositoryModelProvider, correspondent);
                    UsageModelFactory.addUserAction(branchTransitionBehaviourInterior, entryLevelSystemCall);
                    UsageModelFactory.connect(lastAction, entryLevelSystemCall);
                    lastAction = entryLevelSystemCall;
                }
                UsageModelFactory.connect(lastAction, stopBranchTransitionInterior);

            }
        }

        return usageModel;
    }

    /**
     * Create a branch.
     *
     * @param branchTransitionCounter
     * @param entryTime
     * @param entryCallSequenceModel
     * @param numberOfTransitionsOfInteriorBranches
     * @param listOfbranchTransitionCounterInterior
     * @param userSessionIndex
     * @param operationId
     * @return
     */
    private static int createBranch(final List<Integer> branchTransitionCounter, final int entryTime,
            final EntryCallSequenceModel entryCallSequenceModel, final int numberOfTransitionsOfInteriorBranches,
            final List<List<Integer>> listOfbranchTransitionCounterInterior, final int userSessionIndex,
            final int operationId) {
        final int countOfBranchTransition = branchTransitionCounter.get(operationId) + 1;
        branchTransitionCounter.set(operationId, countOfBranchTransition);
        final EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, entryTime + 1,
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[operationId],
                ReferenceUsageModelBuilder.CLASS_SIGNATURE[operationId], String.valueOf(userSessionIndex), "hostname");
        entryCallSequenceModel.getUserSessions().get(userSessionIndex).add(entryCallEvent, true);

        int branchDecisioner = TestHelper.getRandomInteger(numberOfTransitionsOfInteriorBranches - 1, 0);
        for (int k = 0; k < numberOfTransitionsOfInteriorBranches; k++) {
            if (listOfbranchTransitionCounterInterior.get(operationId).get(k) == 0) {
                branchDecisioner = k;
                break;
            }
        }
        // Within the branch transition again a random branch transition is chosen
        if (branchDecisioner == 0) {
            return BranchWithinBranchReference.createEntryCallEventForBranch(userSessionIndex, operationId, 0, 0,
                    entryCallSequenceModel, entryTime + 2, listOfbranchTransitionCounterInterior);
        } else if (branchDecisioner == 1) {
            return BranchWithinBranchReference.createEntryCallEventForBranch(userSessionIndex, operationId, 1, 3,
                    entryCallSequenceModel, entryTime + 2, listOfbranchTransitionCounterInterior);
        } else if (branchDecisioner == 2) {
            return BranchWithinBranchReference.createEntryCallEventForBranch(userSessionIndex, operationId, 2, 4,
                    entryCallSequenceModel, entryTime + 2, listOfbranchTransitionCounterInterior);
        } else {
            return entryTime + 2;
        }
    }

    /**
     * Helper function to create and add an entry call event to a call sequence model.
     *
     * @param index
     *            user session index
     * @param branchDecisioner
     *            branch decision
     * @param entryCallSequenceModel
     *            the sequence call model
     * @param entryTime
     *            call entry time
     * @param exitTime
     *            call exit time
     * @param listOfbranchTransitionCounterInterior
     *            list of iterators
     */
    private static int createEntryCallEventForBranch(final int index, final int iteratorId, final int branchDecisioner,
            final int signatureId, final EntryCallSequenceModel entryCallSequenceModel, final int entryTime,
            final List<List<Integer>> listOfbranchTransitionCounterInterior) {
        final int countOfBranchTransition = listOfbranchTransitionCounterInterior.get(iteratorId).get(branchDecisioner)
                + 1;
        listOfbranchTransitionCounterInterior.get(iteratorId).set(branchDecisioner, countOfBranchTransition);
        final EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, entryTime + 1,
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[signatureId],
                ReferenceUsageModelBuilder.CLASS_SIGNATURE[signatureId], String.valueOf(index), "hostname");
        entryCallSequenceModel.getUserSessions().get(index).add(entryCallEvent, true);

        return entryTime + 2;
    }
}
