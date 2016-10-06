/***************************************************************************
 * Copyright 2016 iObserve Project (http://dfg-spp1593.de/index.php?id=44)
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
import java.util.List;
import java.util.Optional;

import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.model.UsageModelBuilder;
import org.iobserve.analysis.userbehavior.test.ReferenceElements;
import org.iobserve.analysis.userbehavior.test.ReferenceUsageModelBuilder;
import org.iobserve.analysis.userbehavior.test.TestHelper;

/**
 * BranchWithinBranchReference.
 *
 * @author Reiner Jung
 *
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
     * @param correspondenceModel
     *            correspondence model
     *
     * @return a reference usage model and corresponding user sessions
     * @throws IOException
     *             on error
     */
    public static ReferenceElements getModel(final String referenceUsageModelFileName,
            final UsageModelBuilder usageModelBuilder, final ICorrespondence correspondenceModel) throws IOException {

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

        // User sessions are created that exactly represent
        // the user behavior of the reference usage model. The entry and exit times enable that the
        // calls within the user sessions are ordered according to the reference usage model
        // The branch transition counter ensures that each branch transition is visited by at least
        // one user session
        final List<Integer> branchTransitionCounter = new ArrayList<>();
        final List<List<Integer>> listOfbranchTransitionCounterInterior = new ArrayList<>();
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
            int exitTime = 2;

            for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
                entryTime = 1;
                exitTime = 2;
                // Each user sessions represents randomly one of the branch transitions
                int branchDecisioner = TestHelper.getRandomInteger(numberOfTransitionsOfExteriorBranch - 1, 0);
                if (branchDecisioner == 0) {
                    final int countOfBranchTransition = branchTransitionCounter.get(0) + 1;
                    branchTransitionCounter.set(0, countOfBranchTransition);
                    final EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[0], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    branchDecisioner = TestHelper.getRandomInteger(numberOfTransitionsOfInteriorBranches - 1, 0);
                    for (int k = 0; k < numberOfTransitionsOfInteriorBranches; k++) {
                        if (listOfbranchTransitionCounterInterior.get(0).get(k) == 0) {
                            branchDecisioner = k;
                            break;
                        }
                    }
                    // Within the branch transition again a random branch transition is chosen
                    if (branchDecisioner == 0) {
                        BranchWithinBranchReference.createEntryCallEventForBranch(i, 0, 0, 0, entryCallSequenceModel,
                                entryTime, exitTime, listOfbranchTransitionCounterInterior);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    } else if (branchDecisioner == 1) {
                        BranchWithinBranchReference.createEntryCallEventForBranch(i, 0, 1, 3, entryCallSequenceModel,
                                entryTime, exitTime, listOfbranchTransitionCounterInterior);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    } else if (branchDecisioner == 2) {
                        BranchWithinBranchReference.createEntryCallEventForBranch(i, 0, 2, 4, entryCallSequenceModel,
                                entryTime, exitTime, listOfbranchTransitionCounterInterior);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                } else if (branchDecisioner == 1) {
                    final int countOfBranchTransition = branchTransitionCounter.get(1) + 1;
                    branchTransitionCounter.set(1, countOfBranchTransition);
                    final EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[1],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[1], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    branchDecisioner = TestHelper.getRandomInteger(numberOfTransitionsOfInteriorBranches - 1, 0);
                    for (int k = 0; k < numberOfTransitionsOfInteriorBranches; k++) {
                        if (listOfbranchTransitionCounterInterior.get(1).get(k) == 0) {
                            branchDecisioner = k;
                            break;
                        }
                    }
                    // Within the branch transition again a random branch transition is chosen
                    if (branchDecisioner == 0) {
                        BranchWithinBranchReference.createEntryCallEventForBranch(i, 1, 0, 0, entryCallSequenceModel,
                                entryTime, exitTime, listOfbranchTransitionCounterInterior);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    } else if (branchDecisioner == 1) {
                        BranchWithinBranchReference.createEntryCallEventForBranch(i, 1, 1, 3, entryCallSequenceModel,
                                entryTime, exitTime, listOfbranchTransitionCounterInterior);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    } else if (branchDecisioner == 2) {
                        BranchWithinBranchReference.createEntryCallEventForBranch(i, 1, 2, 4, entryCallSequenceModel,
                                entryTime, exitTime, listOfbranchTransitionCounterInterior);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                } else if (branchDecisioner == 2) {
                    final int countOfBranchTransition = branchTransitionCounter.get(2) + 1;
                    branchTransitionCounter.set(2, countOfBranchTransition);
                    final EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[2], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    branchDecisioner = TestHelper.getRandomInteger(numberOfTransitionsOfInteriorBranches - 1, 0);
                    for (int k = 0; k < numberOfTransitionsOfInteriorBranches; k++) {
                        if (listOfbranchTransitionCounterInterior.get(2).get(k) == 0) {
                            branchDecisioner = k;
                            break;
                        }
                    }
                    // Within the branch transition again a random branch transition is chosen
                    if (branchDecisioner == 0) {
                        BranchWithinBranchReference.createEntryCallEventForBranch(i, 2, 0, 0, entryCallSequenceModel,
                                entryTime, exitTime, listOfbranchTransitionCounterInterior);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;

                    } else if (branchDecisioner == 1) {
                        BranchWithinBranchReference.createEntryCallEventForBranch(i, 2, 1, 3, entryCallSequenceModel,
                                entryTime, exitTime, listOfbranchTransitionCounterInterior);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    } else if (branchDecisioner == 2) {
                        BranchWithinBranchReference.createEntryCallEventForBranch(i, 2, 2, 4, entryCallSequenceModel,
                                entryTime, exitTime, listOfbranchTransitionCounterInterior);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                }
            }
            // Checks whether all branch transitions of the exterior branch are represented within
            // the user sessions
            for (int i = 0; i < branchTransitionCounter.size(); i++) {
                if (branchTransitionCounter.get(i) == 0) {
                    areAllBranchesVisited = false;
                    break;
                }
            }
            // Checks whether all branch transitions of the interior branches are represented within
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

        // In the following the reference usage model is created
        AbstractUserAction lastAction;
        Optional<Correspondent> optionCorrespondent;
        final UsageModel usageModel = usageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = usageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = usageModelBuilder.createStart("");
        usageModelBuilder.addUserAction(scenarioBehaviour, start);
        final Stop stop = usageModelBuilder.createStop("");
        usageModelBuilder.addUserAction(scenarioBehaviour, stop);
        lastAction = start;

        // The exterior branch is created
        final org.palladiosimulator.pcm.usagemodel.Branch branch = usageModelBuilder.createBranch("",
                scenarioBehaviour);
        usageModelBuilder.connect(lastAction, branch);
        usageModelBuilder.connect(branch, stop);

        // Creates branch transitions according to the random countOfBranchTransitions
        for (int i = 0; i < numberOfTransitionsOfExteriorBranch; i++) {
            final BranchTransition branchTransition = usageModelBuilder.createBranchTransition(branch);
            final ScenarioBehaviour branchTransitionBehaviour = branchTransition
                    .getBranchedBehaviour_BranchTransition();
            branchTransition
                    .setBranchProbability((double) branchTransitionCounter.get(i) / (double) numberOfConcurrentUsers);
            final Start startBranchTransition = usageModelBuilder.createStart("");
            usageModelBuilder.addUserAction(branchTransitionBehaviour, startBranchTransition);
            final Stop stopBranchTransition = usageModelBuilder.createStop("");
            usageModelBuilder.addUserAction(branchTransitionBehaviour, stopBranchTransition);
            lastAction = startBranchTransition;
            if ((i >= 0) && (i < 3)) {
                optionCorrespondent = correspondenceModel.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[i],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[i]);
            } else {
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                usageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }

            // The interior branch is created
            final org.palladiosimulator.pcm.usagemodel.Branch branchInterior = usageModelBuilder.createBranch("",
                    branchTransitionBehaviour);
            usageModelBuilder.connect(lastAction, branchInterior);
            usageModelBuilder.connect(branchInterior, stopBranchTransition);

            for (int j = 0; j < numberOfTransitionsOfInteriorBranches; j++) {
                final BranchTransition branchTransitionInterior = usageModelBuilder
                        .createBranchTransition(branchInterior);
                final ScenarioBehaviour branchTransitionBehaviourInterior = branchTransitionInterior
                        .getBranchedBehaviour_BranchTransition();
                branchTransitionInterior
                        .setBranchProbability((double) listOfbranchTransitionCounterInterior.get(i).get(j)
                                / (double) branchTransitionCounter.get(i));
                final Start startBranchTransitionInterior = usageModelBuilder.createStart("");
                usageModelBuilder.addUserAction(branchTransitionBehaviourInterior, startBranchTransitionInterior);
                final Stop stopBranchTransitionInterior = usageModelBuilder.createStop("");
                usageModelBuilder.addUserAction(branchTransitionBehaviourInterior, stopBranchTransitionInterior);
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
                    final EntryLevelSystemCall entryLevelSystemCall = usageModelBuilder
                            .createEntryLevelSystemCall(correspondent);
                    usageModelBuilder.addUserAction(branchTransitionBehaviourInterior, entryLevelSystemCall);
                    usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                    lastAction = entryLevelSystemCall;
                }
                usageModelBuilder.connect(lastAction, stopBranchTransitionInterior);

            }

        }

        // Saves the reference usage model and sets the usage model and the EntryCallSequenceModel
        // as the reference elements. Our approach is now executed with the EntryCallSequenceModel
        // and the resulting usage model can be matched against the reference usage model
        TestHelper.saveModel(usageModel, referenceUsageModelFileName);
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
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
    private static void createEntryCallEventForBranch(final int index, final int iteratorId, final int branchDecisioner,
            final int signatureId, final EntryCallSequenceModel entryCallSequenceModel, final int entryTime,
            final int exitTime, final List<List<Integer>> listOfbranchTransitionCounterInterior) {
        final int countOfBranchTransition = listOfbranchTransitionCounterInterior.get(iteratorId).get(branchDecisioner)
                + 1;
        listOfbranchTransitionCounterInterior.get(iteratorId).set(branchDecisioner, countOfBranchTransition);
        final EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[signatureId],
                ReferenceUsageModelBuilder.CLASS_SIGNATURE[signatureId], String.valueOf(index), "hostname");
        entryCallSequenceModel.getUserSessions().get(index).add(entryCallEvent, true);
    }
}
