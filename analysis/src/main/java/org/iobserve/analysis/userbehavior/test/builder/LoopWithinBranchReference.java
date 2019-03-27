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
import java.util.List;
import java.util.Optional;

import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.model.RepositoryModelProvider;
import org.iobserve.analysis.model.UsageModelBuilder;
import org.iobserve.analysis.model.correspondence.ArchitecturalModelElement;
import org.iobserve.analysis.model.correspondence.CorrespondenceModelProvider;
import org.iobserve.analysis.userbehavior.test.ReferenceElements;
import org.iobserve.analysis.userbehavior.test.ReferenceUsageModelBuilder;
import org.iobserve.analysis.userbehavior.test.TestHelper;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

/**
 * LoopWithinBranchReferenceModel.
 *
 * @author David Peter -- initial contribution
 * @author Reiner Jung -- refactoring
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
     * @param repositoryModelProvider
     *            repository model provider
     * @param correspondenceModel
     *            correspondence model
     *
     * @return a reference model and corresponding user sessions
     * @throws IOException
     *             on error
     */
    public static ReferenceElements getModel(final String referenceUsageModelFileName,
            final RepositoryModelProvider repositoryModelProvider, final CorrespondenceModelProvider correspondenceModelProvider)
            throws IOException {

        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model. The minimum number of user sessions is set
        // dependently from the random number of branch transitions, because it must be ensured that
        // each branch transition is represented within the user sessions.
        final int numberOfBranchTransitions = TestHelper.getRandomInteger(3, 2);
        final int numberOfConcurrentUsers = TestHelper.getRandomInteger(200, 10 * numberOfBranchTransitions); //30
        final int lengthOfBranchSequence = TestHelper.getRandomInteger(2, 1);
        final int countOfLoop = TestHelper.getRandomInteger(3, 2);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                TestHelper.getUserSessions(numberOfConcurrentUsers));
        final ReferenceElements referenceElements = new ReferenceElements();

        // In the following the reference usage model is created
        final UsageModel usageModel = UsageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = UsageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();

        // lastAction = start;

        final Branch branch = LoopWithinBranchReference.createBranch(repositoryModelProvider, scenarioBehaviour,
        		correspondenceModelProvider, numberOfBranchTransitions, lengthOfBranchSequence, countOfLoop);

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
            for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
                entryTime = 1;
                // Each user session represents one of the branch transitions
                final int branchDecisioner = TestHelper.getRandomInteger(numberOfBranchTransitions - 1, 0);
                if (branchDecisioner == 0) {
                    entryTime = LoopWithinBranchReference.createLoop(branchTransitionCounter, entryTime, 0, 1,
                            entryCallSequenceModel, lengthOfBranchSequence, countOfLoop, i);
                } else if (branchDecisioner == 1) {
                    entryTime = LoopWithinBranchReference.createLoop(branchTransitionCounter, entryTime, 1, 2,
                            entryCallSequenceModel, lengthOfBranchSequence, countOfLoop, i);
                } else if (branchDecisioner == 2) {
                    entryTime = LoopWithinBranchReference.createLoop(branchTransitionCounter, entryTime, 2, 0,
                            entryCallSequenceModel, lengthOfBranchSequence, countOfLoop, i);
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

    /**
     * Create a loop code.
     *
     * @param branchTransitionCounter
     * @param startTime
     * @param callIndex
     * @param loopIndex
     * @param entryCallSequenceModel
     * @param lengthOfBranchSequence
     * @param countOfLoop
     * @param i
     * @return
     */
    private static int createLoop(final List<Integer> branchTransitionCounter, final int startTime, final int callIndex,
            final int loopIndex, final EntryCallSequenceModel entryCallSequenceModel, final int lengthOfBranchSequence,
            final int countOfLoop, final int i) {
        int entryTime = startTime;
        final int countOfBranchTransition = branchTransitionCounter.get(callIndex) + 1;
        branchTransitionCounter.set(callIndex, countOfBranchTransition);
        EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, entryTime + 1,
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[callIndex],
                ReferenceUsageModelBuilder.CLASS_SIGNATURE[callIndex], String.valueOf(i), "hostname");
        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
        entryTime += 2;
        if (lengthOfBranchSequence == 2) {
            final EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, entryTime + 1,
                    ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4], ReferenceUsageModelBuilder.CLASS_SIGNATURE[4],
                    String.valueOf(i), "hostname");
            entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
            entryTime += 2;
        }
        // Within the branch transition the loop is represented by iterated calls
        for (int j = 0; j < countOfLoop; j++) {
            entryCallEvent = new EntryCallEvent(entryTime, entryTime + 1,
                    ReferenceUsageModelBuilder.OPERATION_SIGNATURE[loopIndex],
                    ReferenceUsageModelBuilder.CLASS_SIGNATURE[loopIndex], String.valueOf(i), "hostname");
            entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
            entryTime += 2;
        }

        return entryTime;
    }

    /**
     * Creates a branch and branch transitions according to the random countOfBranchTransitions.
     *
     * @param scenarioBehaviour
     * @param correspondenceModel
     * @param numberOfBranchTransitions
     * @param lengthOfBranchSequence
     * @param countOfLoop
     * @return
     */
    private static Branch createBranch(final RepositoryModelProvider repositoryModelProvider,
            final ScenarioBehaviour scenarioBehaviour, final CorrespondenceModelProvider correspondenceModelProvider,
            final int numberOfBranchTransitions, final int lengthOfBranchSequence, final int countOfLoop) {
        final Start start = UsageModelBuilder.createAddStartAction("", scenarioBehaviour);
        final Branch branch = UsageModelBuilder.createBranch("", scenarioBehaviour);
        final Stop stop = UsageModelBuilder.createAddStopAction("", scenarioBehaviour);

        UsageModelBuilder.connect(start, branch);
        UsageModelBuilder.connect(branch, stop);

        AbstractUserAction lastAction = start;

        // For each branch transition its calls are added to the branch transition
        for (int i = 0; i < numberOfBranchTransitions; i++) {
            final BranchTransition branchTransition = UsageModelBuilder.createBranchTransition(branch);
            final ScenarioBehaviour branchTransitionBehaviour = branchTransition
                    .getBranchedBehaviour_BranchTransition();
            final Start startBranchTransition = UsageModelBuilder.createStart("");
            UsageModelBuilder.addUserAction(branchTransitionBehaviour, startBranchTransition);
            final Stop stopBranchTransition = UsageModelBuilder.createStop("");
            UsageModelBuilder.addUserAction(branchTransitionBehaviour, stopBranchTransition);
            lastAction = startBranchTransition;

            if ((i >= 0) && (i < 3)) {
                final Optional<ArchitecturalModelElement> architecturalModelElement = correspondenceModelProvider.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[i],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[i]);
                if (architecturalModelElement.isPresent()) {
                    final EntryLevelSystemCall entryLevelSystemCall = UsageModelBuilder
                            .createEntryLevelSystemCall(repositoryModelProvider, architecturalModelElement.get());
                    UsageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                    UsageModelBuilder.connect(lastAction, entryLevelSystemCall);
                    lastAction = entryLevelSystemCall;
                }
            } else {
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }

            if (lengthOfBranchSequence == 2) {
                final Optional<ArchitecturalModelElement> architecturalModelElement = correspondenceModelProvider.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[4],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4]);
                if (architecturalModelElement.isPresent()) {
                    final EntryLevelSystemCall entryLevelSystemCall = UsageModelBuilder
                            .createEntryLevelSystemCall(repositoryModelProvider, architecturalModelElement.get());
                    UsageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                    UsageModelBuilder.connect(lastAction, entryLevelSystemCall);
                    lastAction = entryLevelSystemCall;
                }
            }

            // Within the branch transition a loop element is created
            final Loop loop = UsageModelBuilder.createLoop("", branchTransitionBehaviour);
            UsageModelBuilder.connect(lastAction, loop);
            final PCMRandomVariable pcmLoop2Iteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
            pcmLoop2Iteration.setSpecification(String.valueOf(countOfLoop));
            loop.setLoopIteration_Loop(pcmLoop2Iteration);
            final Start loopStart = UsageModelBuilder.createStart("");
            UsageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
            final Stop loopStop = UsageModelBuilder.createStop("");
            UsageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
            lastAction = loopStart;

            // The calls that are iterated are added to the loop
            final Optional<ArchitecturalModelElement> architecturalModelElement;
            switch (i) {
            case 0:
            	architecturalModelElement = correspondenceModelProvider.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[1],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[1]);
                break;
            case 1:
            	architecturalModelElement = correspondenceModelProvider.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[2],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2]);
                break;
            case 2:
            	architecturalModelElement = correspondenceModelProvider.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[0],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0]);
                break;
            default:
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (architecturalModelElement.isPresent()) {
                final EntryLevelSystemCall entryLevelSystemCall = UsageModelBuilder
                        .createEntryLevelSystemCall(repositoryModelProvider, architecturalModelElement.get());
                UsageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                UsageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            UsageModelBuilder.connect(lastAction, loopStop);
            UsageModelBuilder.connect(loop, stopBranchTransition);
        }

        return branch;
    }
}
