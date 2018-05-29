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

import org.iobserve.analysis.data.UserSessionCollectionModel;
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
 * LoopWithinLoopReference.
 *
 * @author David Peter -- initial contribution
 * @author Reiner Jung -- refactoring
 *
 */
public final class LoopWithinLoopReference {

    /**
     * Factory.
     */
    private LoopWithinLoopReference() {
    }

    /**
     * It creates a reference usage model that contains loops within loops. Accordingly, user
     * sessions whose call sequences contain iterated segments that again contain iterated segments
     * are created.(RQ-1.7)
     *
     * @param referenceUsageModelFileName
     *            file name of the reference model to store its result
     * @param repositoryLookupModel
     *            repository model provider
     * @param correspondenceModel
     *            correspondence model
     *
     * @return reference usage model and corresponding user sessions
     * @throws IOException
     *             on error
     */
    public ReferenceElements getLoopWithinLoopReferenceModel(final String referenceUsageModelFileName,
            final RepositoryLookupModelProvider repositoryLookupModel, final ICorrespondence correspondenceModel)
            throws IOException {

        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model
        final int countOfLoop1 = TestHelper.getRandomInteger(4, 2);
        final int countOfLoop2 = TestHelper.getRandomInteger(4, 2);
        final int lengthOfSubsequentLoopSequence = TestHelper.getRandomInteger(2, 1);

        // In the following the reference usage model is created
        Optional<Correspondent> optionCorrespondent;
        AbstractUserAction lastAction;
        final UsageModel usageModel = UsageModelFactory.createUsageModel();
        final UsageScenario usageScenario = UsageModelFactory.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = UsageModelFactory.createStart("");
        UsageModelFactory.addUserAction(scenarioBehaviour, start);
        final Stop stop = UsageModelFactory.createStop("");
        UsageModelFactory.addUserAction(scenarioBehaviour, stop);
        lastAction = start;

        // The exterior loop is created
        final Loop loop = UsageModelFactory.createLoop("", scenarioBehaviour);
        UsageModelFactory.connect(lastAction, loop);
        UsageModelFactory.connect(loop, stop);
        final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmLoopIteration.setSpecification(String.valueOf(countOfLoop1));
        loop.setLoopIteration_Loop(pcmLoopIteration);
        final Start loopStart = UsageModelFactory.createStart("");
        UsageModelFactory.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
        final Stop loopStop = UsageModelFactory.createStop("");
        UsageModelFactory.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
        lastAction = loopStart;

        // The interior loop is created
        final Loop loop2 = UsageModelFactory.createLoop("", loop.getBodyBehaviour_Loop());
        UsageModelFactory.connect(lastAction, loop2);
        final PCMRandomVariable pcmLoop2Iteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmLoop2Iteration.setSpecification(String.valueOf(countOfLoop2));
        loop2.setLoopIteration_Loop(pcmLoop2Iteration);
        final Start loop2Start = UsageModelFactory.createStart("");
        UsageModelFactory.addUserAction(loop2.getBodyBehaviour_Loop(), loop2Start);
        final Stop loop2Stop = UsageModelFactory.createStop("");
        UsageModelFactory.addUserAction(loop2.getBodyBehaviour_Loop(), loop2Stop);
        lastAction = loop2Start;
        optionCorrespondent = correspondenceModel.getCorrespondent(ReferenceUsageModelBuilder.CLASS_SIGNATURE[2],
                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2]);
        if (optionCorrespondent.isPresent()) {
            final Correspondent correspondent = optionCorrespondent.get();
            final EntryLevelSystemCall entryLevelSystemCall = UsageModelFactory
                    .createEntryLevelSystemCall(repositoryLookupModel, correspondent);
            UsageModelFactory.addUserAction(loop2.getBodyBehaviour_Loop(), entryLevelSystemCall);
            UsageModelFactory.connect(lastAction, entryLevelSystemCall);
            lastAction = entryLevelSystemCall;
        }
        UsageModelFactory.connect(lastAction, loop2Stop);
        lastAction = loop2;

        // The sequence that exclusively belongs to the exterior loop is created
        for (int i = 0; i < lengthOfSubsequentLoopSequence; i++) {
            switch (i) {
            case 0:
                optionCorrespondent = correspondenceModel.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[3],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[3]);
                break;
            case 1:
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
                        .createEntryLevelSystemCall(repositoryLookupModel, correspondent);
                UsageModelFactory.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                UsageModelFactory.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
        }
        UsageModelFactory.connect(lastAction, loopStop);

        final int numberOfConcurrentUsers = TestHelper.getRandomInteger(200, 1);

        final UserSessionCollectionModel entryCallSequenceModel = new UserSessionCollectionModel(
                TestHelper.getUserSessions(numberOfConcurrentUsers));

        // According to the reference usage model user sessions are created that exactly represent
        // the user behavior of the reference usage model. The entry and exit times enable that the
        // calls within the user sessions are ordered according to the reference usage model.
        int entryTime = 1;
        int exitTime = 2;
        for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
            entryTime = 1;
            exitTime = 2;
            // Represents the exterior loop of the reference usage model
            for (int k = 0; k < countOfLoop1; k++) {
                // Represents the interior loop of the reference usage model
                for (int j = 0; j < countOfLoop2; j++) {
                    final EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                            ReferenceUsageModelBuilder.OPERATION_SIGNATURE[2],
                            ReferenceUsageModelBuilder.CLASS_SIGNATURE[2], String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                }
                // Calls of the exterior loop
                for (int j = 0; j < lengthOfSubsequentLoopSequence; j++) {
                    EntryCallEvent entryCallEvent = null;
                    switch (j) {
                    case 0:
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[3],
                                ReferenceUsageModelBuilder.CLASS_SIGNATURE[3], String.valueOf(i), "hostname");
                        break;
                    case 1:
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[4],
                                ReferenceUsageModelBuilder.CLASS_SIGNATURE[4], String.valueOf(i), "hostname");
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal value of model element parameter");
                    }
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                }
            }
        }

        // Saves the reference usage model and sets the usage model and the EntryCallSequenceModel
        // as the reference elements. Our approach is now executed with the EntryCallSequenceModel
        // and the resulting usage model can be matched against the reference usage model
        final ReferenceElements referenceElements = new ReferenceElements();

        TestHelper.saveModel(usageModel, referenceUsageModelFileName);
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }
}
