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
package org.iobserve.analysis.test.userbehavior.builder;

import java.io.IOException;
import java.util.Optional;

import org.iobserve.analysis.data.UserSessionCollectionModel;
import org.iobserve.analysis.test.userbehavior.ReferenceElements;
import org.iobserve.analysis.test.userbehavior.ReferenceUsageModelBuilder;
import org.iobserve.analysis.test.userbehavior.TestHelper;
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
 * Simple loop reference.
 *
 * @author David Peter -- initial contribution
 * @author Reiner Jung -- refactoring
 *
 */
public final class SimpleLoopReference {

    /**
     * Factory.
     */
    private SimpleLoopReference() {
    }

    /**
     * Creates a reference model that contains a loop element. Accordingly, user sessions whose call
     * sequences contain iterated calls are created.(RQ-1.3)
     *
     *
     * @param referenceUsageModelFileName
     *            reference usage model file name
     * @param repositoryLookupModelProvider
     *            repository model provider
     * @param correspondenceModel
     *            correspondence model
     *
     * @return the reference usage model and a corresponding EntryCallSequenceModel
     * @throws IOException
     *             on error
     */
    public static ReferenceElements getModel(final String referenceUsageModelFileName,
            final RepositoryLookupModelProvider repositoryLookupModelProvider, final ICorrespondence correspondenceModel)
            throws IOException {
        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model
        final int numberOfConcurrentUsers = TestHelper.getRandomInteger(200, 1);
        final int loopCount = TestHelper.getRandomInteger(5, 2);
        final int numberOfIteratedCalls = TestHelper.getRandomInteger(5, 1);

        final UserSessionCollectionModel entryCallSequenceModel = new UserSessionCollectionModel(
                TestHelper.getUserSessions(numberOfConcurrentUsers));
        final ReferenceElements referenceElements = new ReferenceElements();

        // In the following the reference usage model is created
        AbstractUserAction lastAction;
        final UsageModel usageModel = UsageModelFactory.createUsageModel();
        final UsageScenario usageScenario = UsageModelFactory.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = UsageModelFactory.createAddStartAction("", scenarioBehaviour);
        final Stop stop = UsageModelFactory.createAddStopAction("", scenarioBehaviour);

        // A loop is created and the loop count is set
        final Loop loop = UsageModelFactory.createLoop("", scenarioBehaviour);
        UsageModelFactory.connect(start, loop);
        final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmLoopIteration.setSpecification(String.valueOf(loopCount));
        loop.setLoopIteration_Loop(pcmLoopIteration);
        UsageModelFactory.connect(loop, stop);

        // The EntryLevelSystemCalls that are iterated are added to the loop element
        final Start loopStart = UsageModelFactory.createAddStartAction("", loop.getBodyBehaviour_Loop());
        final Stop loopStop = UsageModelFactory.createAddStopAction("", loop.getBodyBehaviour_Loop());
        lastAction = loopStart;

        Optional<Correspondent> optionCorrespondent;
        // Because the number of iterated calls is set randomly, the switch case adds the number of
        // iterated calls to the loop
        for (int i = 0; i < numberOfIteratedCalls; i++) {
            if (i >= 0 && i < 5) {
                optionCorrespondent = correspondenceModel.getCorrespondent(
                        ReferenceUsageModelBuilder.CLASS_SIGNATURE[i],
                        ReferenceUsageModelBuilder.OPERATION_SIGNATURE[i]);

            } else {
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = UsageModelFactory
                        .createEntryLevelSystemCall(repositoryLookupModelProvider, correspondent);
                UsageModelFactory.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                UsageModelFactory.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
        }
        UsageModelFactory.connect(lastAction, loopStop);

        // According to the reference usage model user sessions are created that exactly represent
        // the user behavior of the reference usage model. The entry and exit times enable that the
        // calls within the user sessions are ordered according to the reference usage model
        int entryTime = 1;
        int exitTime = 2;
        for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
            entryTime = 1;
            exitTime = 2;
            // The user sessions are created according to the random number of loops and the random
            // number of iterated calls
            for (int k = 0; k < loopCount; k++) {
                for (int j = 0; j < numberOfIteratedCalls; j++) {
                    EntryCallEvent entryCallEvent = null;
                    if (j >= 0 && j < 5) {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime,
                                ReferenceUsageModelBuilder.OPERATION_SIGNATURE[j],
                                ReferenceUsageModelBuilder.CLASS_SIGNATURE[j], String.valueOf(i), "hostname");

                    } else {
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
        TestHelper.saveModel(usageModel, referenceUsageModelFileName);
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }

}
