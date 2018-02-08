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
import java.util.Optional;

import org.iobserve.analysis.data.EntryCallSequenceModel;
import org.iobserve.analysis.userbehavior.ReferenceElements;
import org.iobserve.analysis.userbehavior.ReferenceUsageModelBuilder;
import org.iobserve.analysis.userbehavior.TestHelper;
import org.iobserve.model.correspondence.Correspondent;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.factory.UsageModelFactory;
import org.iobserve.model.provider.RepositoryLookupModelProvider;
import org.iobserve.stages.general.data.EntryCallEvent;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;

/**
 * OverlappingIterationReferenceModel.
 *
 * @author David Peter -- initial contribution
 * @author Reiner Jung -- refactoring
 */
public final class OverlappingIterationReference {

    /**
     * Factory.
     */
    private OverlappingIterationReference() {

    }

    /**
     * Creates a reference model that contains a loop element. The user sessions contain iterated
     * call sequences that share overlapping calls. Thereby, one iterated sequence consists of more
     * calls than the other. Thus, it can be checked whether the approach transforms the iterated
     * call sequence that consists of more calls to a loop (RQ-1.4)
     *
     * @param referenceUsageModelFileName
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
    public static ReferenceElements getModel(final String referenceUsageModelFileName,
            final RepositoryLookupModelProvider repositoryLookupModelProvider, final ICorrespondence correspondenceModel)
            throws IOException {

        // Creates a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model.
        final int numberOfConcurrentUsers = TestHelper.getRandomInteger(200, 1);
        // One of the iterated sequences is iterated twice and one is iterated three times. The
        // number of iterations is set randomly.
        final int loopCount1 = TestHelper.getRandomInteger(3, 2);
        final int lengthOfSequence1 = 2 * loopCount1;
        final int loopCount2;
        if (loopCount1 == 3) {
            loopCount2 = 2;
        } else {
            loopCount2 = 3;
        }
        final int lengthOfSequence2 = 2 * loopCount2;

        final ReferenceElements referenceElements = new ReferenceElements();
        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                TestHelper.getUserSessions(numberOfConcurrentUsers));

        // In the following the reference usage model is created
        AbstractUserAction lastAction;
        final UsageModel usageModel = UsageModelFactory.createUsageModel();
        final UsageScenario usageScenario = UsageModelFactory.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = UsageModelFactory.createStart("");
        UsageModelFactory.addUserAction(scenarioBehaviour, start);
        final Stop stop = UsageModelFactory.createStop("");
        UsageModelFactory.addUserAction(scenarioBehaviour, stop);
        // According to the randomly set number of iterations the sequence that is iterated three
        // times is represented by a loop element. The other sequence is represented by a sequence
        final Loop loop = UsageModelFactory.createLoop("", scenarioBehaviour);

        if (lengthOfSequence1 >= lengthOfSequence2) {
            UsageModelFactory.connect(start, loop);
            final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
            pcmLoopIteration.setSpecification(String.valueOf(loopCount1));
            loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
            final Start loopStart = UsageModelFactory.createStart("");
            UsageModelFactory.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
            final Stop loopStop = UsageModelFactory.createStop("");
            UsageModelFactory.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);

            lastAction = OverlappingIterationReference.createEntryLevelSystemCall(repositoryLookupModelProvider,
                    correspondenceModel, 0, loopStart, scenarioBehaviour);
            lastAction = OverlappingIterationReference.createEntryLevelSystemCall(repositoryLookupModelProvider,
                    correspondenceModel, 2, lastAction, scenarioBehaviour);

            UsageModelFactory.connect(lastAction, loopStop);

            lastAction = OverlappingIterationReference.createEntryLevelSystemCall(repositoryLookupModelProvider,
                    correspondenceModel, 3, loop, scenarioBehaviour);
            lastAction = OverlappingIterationReference.createEntryLevelSystemCall(repositoryLookupModelProvider,
                    correspondenceModel, 2, lastAction, scenarioBehaviour);
            lastAction = OverlappingIterationReference.createEntryLevelSystemCall(repositoryLookupModelProvider,
                    correspondenceModel, 3, lastAction, scenarioBehaviour);

            UsageModelFactory.connect(lastAction, stop);
        } else {
            lastAction = OverlappingIterationReference.createEntryLevelSystemCall(repositoryLookupModelProvider,
                    correspondenceModel, 0, start, scenarioBehaviour);
            lastAction = OverlappingIterationReference.createEntryLevelSystemCall(repositoryLookupModelProvider,
                    correspondenceModel, 2, lastAction, scenarioBehaviour);
            lastAction = OverlappingIterationReference.createEntryLevelSystemCall(repositoryLookupModelProvider,
                    correspondenceModel, 0, lastAction, scenarioBehaviour);

            UsageModelFactory.connect(lastAction, loop);
            final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
            pcmLoopIteration.setSpecification(String.valueOf(loopCount2));
            loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
            final Start loopStart = UsageModelFactory.createStart("");
            UsageModelFactory.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
            final Stop loopStop = UsageModelFactory.createStop("");
            UsageModelFactory.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
            lastAction = loopStart;

            lastAction = OverlappingIterationReference.createEntryLevelSystemCall(repositoryLookupModelProvider,
                    correspondenceModel, 2, lastAction, scenarioBehaviour);
            lastAction = OverlappingIterationReference.createEntryLevelSystemCall(repositoryLookupModelProvider,
                    correspondenceModel, 3, lastAction, scenarioBehaviour);

            UsageModelFactory.connect(lastAction, loopStop);
            UsageModelFactory.connect(loop, stop);
        }

        OverlappingIterationReference.createMatchingEntryAndExitEvents(entryCallSequenceModel, lengthOfSequence1,
                lengthOfSequence2, loopCount1, loopCount2);

        // Saves the reference usage model and sets the usage model and the EntryCallSequenceModel
        // as the reference elements. Our approach is now executed with the EntryCallSequenceModel
        // and the resulting usage model can be matched against the reference usage model
        TestHelper.saveModel(usageModel, referenceUsageModelFileName);
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }

    /**
     * According to the reference usage model user sessions are created that exactly represent the
     * user behavior of the reference usage model. The entry and exit times enable that the calls
     * within the user sessions are ordered according to the reference usage model.
     *
     * @param entryCallSequenceModel
     *            entry call sequence model which is filled with events
     * @param lengthOfSequence1
     *            length of the first sequence
     * @param lengthOfSequence2
     *            length of the second sequence
     * @param loopCount1
     *            loop counter 1
     * @param loopCount2
     *            loop counter 2
     */
    private static void createMatchingEntryAndExitEvents(final EntryCallSequenceModel entryCallSequenceModel,
            final int lengthOfSequence1, final int lengthOfSequence2, final int loopCount1, final int loopCount2) {
        for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
            // According to the randomly set parameter one of the sequences is iterated twice and
            // one is iterated threee times
            if (lengthOfSequence1 >= lengthOfSequence2) {
                EntryCallEvent entryCallEvent;
                int entryTime = 1;
                int exitTime = 2;
                for (int k = 0; k < loopCount1; k++) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[0], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[2], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                }
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[3],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[3], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[2], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[3],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[3], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
            } else {
                EntryCallEvent entryCallEvent;
                int entryTime = 1;
                int exitTime = 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[0], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[2], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[0],
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[0], String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                for (int k = 0; k < loopCount2; k++) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[2], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[3],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[3], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                }
            }
        }
    }

    /**
     * Create and link a user action into a sequence.
     *
     * @param repositoryLookupModelProvider
     *            repository model provider
     * @param correspondenceModel
     *            correspondence model
     * @param index
     *            correspondent index for string arrays
     * @param lastAction
     *            previous action
     * @param correspondent
     *            correspondent element
     * @param scenarioBehaviour
     *            behavior
     *
     * @return return this call
     */
    private static AbstractUserAction createEntryLevelSystemCall(
            final RepositoryLookupModelProvider repositoryLookupModelProvider, final ICorrespondence correspondenceModel,
            final int index, final AbstractUserAction lastAction, final ScenarioBehaviour scenarioBehaviour) {
        final Optional<Correspondent> correspondent = correspondenceModel.getCorrespondent(
                ReferenceUsageModelBuilder.CLASS_SIGNATURE[index],
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[index]);
        if (correspondent.isPresent()) {
            final EntryLevelSystemCall entryLevelSystemCall = UsageModelFactory
                    .createEntryLevelSystemCall(repositoryLookupModelProvider, correspondent.get());
            UsageModelFactory.addUserAction(scenarioBehaviour, entryLevelSystemCall);
            UsageModelFactory.connect(lastAction, entryLevelSystemCall);

            return entryLevelSystemCall;
        } else {
            return lastAction;
        }
    }
}
