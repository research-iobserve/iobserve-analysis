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

import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.model.UsageModelBuilder;
import org.iobserve.analysis.userbehavior.test.ReferenceElements;
import org.iobserve.analysis.userbehavior.test.ReferenceUsageModelBuilder;
import org.iobserve.analysis.userbehavior.test.TestHelper;

/**
 * LoopWithinBranchReferenceModel.
 *
 * @author Reiner Jung
 *
 */
public final class LoopWithinBranchReference {

    /**
     * Factory.
     */
    private LoopWithinBranchReference() {

    }

    /**
     * It creates a reference usage model that contains loops within branches. Accordingly, user
     * sessions whose call sequences differ from each other at the positions of the branches and
     * that contain iterated call sequences are created.(RQ-1.6)
     *
     * @param referenceUsageModelFileName
     *            file name of the reference model to store its result
     * @param usageModelBuilder
     *            usage model builder
     * @param correspondenceModel
     *            correspondence model
     *
     * @return a reference model and corresponding user sessions
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
        final int numberOfBranchTransitions = TestHelper.getRandomInteger(3, 2);
        final int numberOfConcurrentUsers = TestHelper.getRandomInteger(30, 10 * numberOfBranchTransitions);
        final int lengthOfBranchSequence = TestHelper.getRandomInteger(2, 1);
        final int countOfLoop = TestHelper.getRandomInteger(3, 2);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                TestHelper.getUserSessions(numberOfConcurrentUsers));
        final ReferenceElements referenceElements = new ReferenceElements();

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

        // Creates a branch and branch transitions according to the random countOfBranchTransitions
        final org.palladiosimulator.pcm.usagemodel.Branch branch = usageModelBuilder.createBranch("",
                scenarioBehaviour);
        usageModelBuilder.connect(lastAction, branch);
        usageModelBuilder.connect(branch, stop);
        // For each branch transition its calls are added to the branch transition
        for (int i = 0; i < numberOfBranchTransitions; i++) {
            final BranchTransition branchTransition = usageModelBuilder.createBranchTransition(branch);
            final ScenarioBehaviour branchTransitionBehaviour = branchTransition
                    .getBranchedBehaviour_BranchTransition();
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
            if (lengthOfBranchSequence == 2) {
                optionCorrespondent = correspondenceModel.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[4],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4]);
                if (optionCorrespondent.isPresent()) {
                    final Correspondent correspondent = optionCorrespondent.get();
                    final EntryLevelSystemCall entryLevelSystemCall = usageModelBuilder
                            .createEntryLevelSystemCall(correspondent);
                    usageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                    usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                    lastAction = entryLevelSystemCall;
                }
            }

            // Within the branch transition a loop element is created
            final Loop loop = usageModelBuilder.createLoop("", branchTransitionBehaviour);
            usageModelBuilder.connect(lastAction, loop);
            final PCMRandomVariable pcmLoop2Iteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
            pcmLoop2Iteration.setSpecification(String.valueOf(countOfLoop));
            loop.setLoopIteration_Loop(pcmLoop2Iteration);
            final Start loopStart = usageModelBuilder.createStart("");
            usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
            final Stop loopStop = usageModelBuilder.createStop("");
            usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
            lastAction = loopStart;
            // The calls that are iterated are added to the loop
            switch (i) {
            case 0:
                optionCorrespondent = correspondenceModel.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[1],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[1]);
                break;
            case 1:
                optionCorrespondent = correspondenceModel.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[2],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2]);
                break;
            case 2:
                optionCorrespondent = correspondenceModel.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[0],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0]);
                break;
            default:
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            usageModelBuilder.connect(lastAction, loopStop);
            usageModelBuilder.connect(loop, stopBranchTransition);
        }

        // According to the reference usage model user sessions are created that exactly represent
        // the user behavior of the reference usage model. The entry and exit times enable that the
        // calls within the user sessions are ordered according to the reference usage model. The
        // branch transition counter ensures that each branch transition is represnted within the
        // user sessions
        final List<Integer> branchTransitionCounter = new ArrayList<>();
        boolean areAllBranchesVisited = true;
        do {
            for (int i = 0; i < branch.getBranchTransitions_Branch().size(); i++) {
                branchTransitionCounter.add(i, 0);
            }
            int entryTime = 1;
            int exitTime = 2;
            for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
                entryTime = 1;
                exitTime = 2;
                // Each user session represents one of the branch transitions
                final int branchDecisioner = TestHelper.getRandomInteger(numberOfBranchTransitions - 1, 0);
                if (branchDecisioner == 0) {
                    final int countOfBranchTransition = branchTransitionCounter.get(0) + 1;
                    branchTransitionCounter.set(0, countOfBranchTransition);
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[0], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    if (lengthOfBranchSequence == 2) {
                        final EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, exitTime,
                                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4],
                                ReferenceUsageModelBuilder.CLASS_SIGNATURE[4], String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                    // Within the branch transition the loop is represented by iterated calls
                    for (int j = 0; j < countOfLoop; j++) {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[1],
                                ReferenceUsageModelBuilder.CLASS_SIGNATURE[1], String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                } else if (branchDecisioner == 1) {
                    final int countOfBranchTransition = branchTransitionCounter.get(1) + 1;
                    branchTransitionCounter.set(1, countOfBranchTransition);
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[1],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[1], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    if (lengthOfBranchSequence == 2) {
                        final EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, exitTime,
                                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4],
                                ReferenceUsageModelBuilder.CLASS_SIGNATURE[4], String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                    // Within the branch transition the loop is represented by iterated calls
                    for (int j = 0; j < countOfLoop; j++) {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2],
                                ReferenceUsageModelBuilder.CLASS_SIGNATURE[2], String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                } else if (branchDecisioner == 2) {
                    final int countOfBranchTransition = branchTransitionCounter.get(2) + 1;
                    branchTransitionCounter.set(2, countOfBranchTransition);
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[2], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    if (lengthOfBranchSequence == 2) {
                        final EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, exitTime,
                                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4],
                                ReferenceUsageModelBuilder.CLASS_SIGNATURE[4], String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                    // Within the branch transition the loop is represented by iterated calls
                    for (int j = 0; j < countOfLoop; j++) {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0],
                                ReferenceUsageModelBuilder.CLASS_SIGNATURE[0], String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                }

            }
            // It is checked whether all branch transitions are represented within the user sessions
            for (int i = 0; i < branchTransitionCounter.size(); i++) {
                if (branchTransitionCounter.get(i) == 0) {
                    areAllBranchesVisited = false;
                    break;
                }
            }
        } while (!areAllBranchesVisited);

        // Sets the likelihoods of the branch transitions according to the created user sessions
        for (int i = 0; i < branch.getBranchTransitions_Branch().size(); i++) {
            branch.getBranchTransitions_Branch().get(i)
                    .setBranchProbability((double) branchTransitionCounter.get(i) / (double) numberOfConcurrentUsers);
        }

        // Saves the reference usage model and sets the usage model and the EntryCallSequenceModel
        // as the reference elements. Our approach is now executed with the EntryCallSequenceModel
        // and the resulting usage model can be matched against the reference usage model
        TestHelper.saveModel(usageModel, referenceUsageModelFileName);
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }
}
