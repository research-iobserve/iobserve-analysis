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
package org.iobserve.analysis.userbehavior.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.iobserve.analysis.correspondence.Correspondent;
import org.iobserve.analysis.correspondence.ICorrespondence;
import org.iobserve.analysis.data.EntryCallEvent;
import org.iobserve.analysis.filter.models.EntryCallSequenceModel;
import org.iobserve.analysis.filter.models.UserSession;
import org.iobserve.analysis.model.UsageModelBuilder;
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

import java.util.Optional;

/**
 *
 * This class is used to create reference usage models that can be used to evaluate the approach's
 * modeling accuracy by matching the reference usage model against an approach's generated usage
 * model. The matching is done in the
 * {@link org.iobserve.analysis.userbehavior.test.UserBehaviorEvaluation}. Each reference model
 * represents a certain user behavior that is evaluated. According to the user behavior user
 * sessions are created whose call sequences correspond to the user behavior of the reference model
 * Subsequently, the approach can be executed with the created user sessions and the obtained usage
 * model can be matched against the reference usage model.
 *
 * @author David Peter, Robert Heinrich
 */
public class ReferenceUsageModelBuilder {
    private final UsageModelBuilder usageModelBuilder;
    private final ICorrespondence correspondenceModel;
    // Directory to save the reference usage model
    private final String usageModelFolder = "/Users/David/GitRepositories/iObserve/org.iobserve.analysis/output/usageModels/";
    // EntryCallEvents used to create the PCM EntryLevelSystemCalls
    private final String classSignature1 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.BookSale";
    private final String operationSignature1 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.BookSale.Get ()";
    private final String classSignature2 = "tradingsystem_inventory_application_store.ejb.ITradingSystem_Inventory_Application_Store";
    private final String operationSignature2 = "tradingsystem_inventory_application_store.ejb.ITradingSystem_Inventory_Application_Store.storeIf_flushDatabase9()";
    private final String classSignature3 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStoreById";
    private final String operationSignature3 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStoreById.Get()";
    private final String classSignature4 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStockItem";
    private final String operationSignature4 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryStockItem.Get()";
    private final String classSignature5 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryLowStockItemsWithRespectToIncomingProducts";
    private final String operationSignature5 = "de.kit.ipd.cocome.cloud.serviceadapter.Services.queryLowStockItemsWithRespectToIncomingProducts.Get()";

    public ReferenceUsageModelBuilder(final UsageModelBuilder usageModelBuilder,
            final ICorrespondence correspondenceModel) {
        this.usageModelBuilder = usageModelBuilder;
        this.correspondenceModel = correspondenceModel;
    }

    /**
     * Creates a reference model that contains a loop element. Accordingly, user sessions whose call
     * sequences contain iterated calls are created.(RQ-1.3)
     *
     * @return the reference usage model and a corresponding EntryCallSequenceModel
     * @throws IOException
     */
    public ReferenceElements getSimpleLoopReferenceModel() throws IOException {

        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model
        final int numberOfConcurrentUsers = this.getRandomInteger(200, 1);
        final int loopCount = this.getRandomInteger(5, 2);
        final int numberOfIteratedCalls = this.getRandomInteger(5, 1);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                this.getUserSessions(numberOfConcurrentUsers));
        final ReferenceElements referenceElements = new ReferenceElements();

        // In the following the reference usage model is created
        AbstractUserAction lastAction;
        final UsageModel usageModel = this.usageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
        final Stop stop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);

        // A loop is created and the loop count is set
        final Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
        this.usageModelBuilder.connect(start, loop);
        final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmLoopIteration.setSpecification(String.valueOf(loopCount));
        loop.setLoopIteration_Loop(pcmLoopIteration);
        this.usageModelBuilder.connect(loop, stop);

        // The EntryLevelSystemCalls that are iterated are added to the loop element
        final Start loopStart = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
        final Stop loopStop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
        lastAction = loopStart;
        Optional<Correspondent> optionCorrespondent;
        // Because the number of iterated calls is set randomly, the switch case adds the number of
        // iterated calls to the loop
        for (int i = 0; i < numberOfIteratedCalls; i++) {
            switch (i) {
            case 0:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                        this.operationSignature1);
                break;
            case 1:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2,
                        this.operationSignature2);
                break;
            case 2:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3,
                        this.operationSignature3);
                break;
            case 3:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4,
                        this.operationSignature4);
                break;
            case 4:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5,
                        this.operationSignature5);
                break;
            default:
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
        }
        this.usageModelBuilder.connect(lastAction, loopStop);

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
                    switch (j) {
                    case 0:
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                                this.classSignature1, String.valueOf(i), "hostname");
                        break;
                    case 1:
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2,
                                this.classSignature2, String.valueOf(i), "hostname");
                        break;
                    case 2:
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3,
                                this.classSignature3, String.valueOf(i), "hostname");
                        break;
                    case 3:
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                                this.classSignature4, String.valueOf(i), "hostname");
                        break;
                    case 4:
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5,
                                this.classSignature5, String.valueOf(i), "hostname");
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
        this.saveModel(usageModel, this.usageModelFolder + "ReferenceModel.usagemodel");
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }

    /**
     * Creates a reference model that contains a branch element. Accordingly, user sessions whose
     * call sequences differ from each other at the position of the branch are created.(RQ-1.2)
     *
     * @return a reference usage model and corresponding user sessions
     * @throws IOException
     */
    public ReferenceElements getSimpleBranchReferenceModel() throws IOException {

        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model. The minimum number of user sessions is set
        // dependently from the random number of branch transitions, because it must be ensured that
        // each branch transition is represented within the user sessions.
        final int numberOfBranchTransitions = this.getRandomInteger(5, 2);
        final int numberOfConcurrentUsers = this.getRandomInteger(200, 10 * numberOfBranchTransitions);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                this.getUserSessions(numberOfConcurrentUsers));
        final ReferenceElements referenceElements = new ReferenceElements();

        // In the following the reference usage model is created
        AbstractUserAction lastAction;
        Optional<Correspondent> optionCorrespondent;
        final UsageModel usageModel = this.usageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
        lastAction = start;
        final Stop stop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);

        // Creates the branch element and the branch transitions according to the random number of
        // branch transitions
        final org.palladiosimulator.pcm.usagemodel.Branch branch = this.usageModelBuilder.createBranch("",
                scenarioBehaviour);
        this.usageModelBuilder.connect(start, branch);
        // For each branch transition an alternative EntryLevelSystemCall is created to represent
        // the alternative call sequences
        for (int i = 0; i < numberOfBranchTransitions; i++) {
            final BranchTransition branchTransition = this.usageModelBuilder.createBranchTransition(branch);
            final ScenarioBehaviour branchTransitionBehaviour = branchTransition
                    .getBranchedBehaviour_BranchTransition();
            final Start startBranchTransition = this.usageModelBuilder.createStart("");
            this.usageModelBuilder.addUserAction(branchTransitionBehaviour, startBranchTransition);
            final Stop stopBranchTransition = this.usageModelBuilder.createStop("");
            this.usageModelBuilder.addUserAction(branchTransitionBehaviour, stopBranchTransition);
            lastAction = startBranchTransition;

            switch (i) {
            case 0:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                        this.operationSignature1);
                break;
            case 1:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2,
                        this.operationSignature2);
                break;
            case 2:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3,
                        this.operationSignature3);
                break;
            case 3:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4,
                        this.operationSignature4);
                break;
            case 4:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5,
                        this.operationSignature5);
                break;
            default:
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            if (i == 0) {
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2,
                        this.operationSignature2);
            } else {
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                        this.operationSignature1);
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }

            this.usageModelBuilder.connect(lastAction, stopBranchTransition);
        }
        optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
        if (optionCorrespondent.isPresent()) {
            final Correspondent correspondent = optionCorrespondent.get();
            final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                    .createEntryLevelSystemCall(correspondent);
            this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
            this.usageModelBuilder.connect(branch, entryLevelSystemCall);
            lastAction = entryLevelSystemCall;
        }
        this.usageModelBuilder.connect(lastAction, stop);

        // According to the reference usage model user sessions are created that exactly represent
        // the user behavior of the reference usage model. The entry and exit times enable that the
        // calls within the user sessions are ordered according to the reference usage model
        // The branch transition counter ensures that each branch transition is visited by at least
        // one user session
        final List<Integer> branchTransitionCounter = new ArrayList<>();
        boolean areAllBranchesVisited = true;
        do {
            for (int i = 0; i < branch.getBranchTransitions_Branch().size(); i++) {
                branchTransitionCounter.add(i, 0);
            }
            for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
                // Each user sessions represents randomly one of the branch transitions
                final int branchDecisioner = this.getRandomInteger(numberOfBranchTransitions - 1, 0);
                if (branchDecisioner == 0) {
                    final int countOfBranchTransition = branchTransitionCounter.get(0) + 1;
                    branchTransitionCounter.set(0, countOfBranchTransition);
                    final EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    final EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature2,
                            this.classSignature2, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                } else if (branchDecisioner == 1) {
                    final int countOfBranchTransition = branchTransitionCounter.get(1) + 1;
                    branchTransitionCounter.set(1, countOfBranchTransition);
                    final EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature2,
                            this.classSignature2, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    final EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                } else if (branchDecisioner == 2) {
                    final int countOfBranchTransition = branchTransitionCounter.get(2) + 1;
                    branchTransitionCounter.set(2, countOfBranchTransition);
                    final EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature3,
                            this.classSignature3, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    final EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                } else if (branchDecisioner == 3) {
                    final int countOfBranchTransition = branchTransitionCounter.get(3) + 1;
                    branchTransitionCounter.set(3, countOfBranchTransition);
                    final EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature4,
                            this.classSignature4, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    final EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                } else if (branchDecisioner == 4) {
                    final int countOfBranchTransition = branchTransitionCounter.get(4) + 1;
                    branchTransitionCounter.set(4, countOfBranchTransition);
                    final EntryCallEvent entryCallEvent = new EntryCallEvent(1, 2, this.operationSignature5,
                            this.classSignature5, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    final EntryCallEvent entryCallEvent2 = new EntryCallEvent(3, 4, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                } else {
                    throw new IllegalArgumentException("Illegal value of model element parameter");
                }
                final EntryCallEvent entryCallEvent3 = new EntryCallEvent(3, 4, this.operationSignature3,
                        this.classSignature3, String.valueOf(i), "hostname");
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

        // Set the likelihoods of the branch transitions of the reference usage model according to
        // the randomly created user sessions
        for (int i = 0; i < branch.getBranchTransitions_Branch().size(); i++) {
            branch.getBranchTransitions_Branch().get(i)
                    .setBranchProbability((double) branchTransitionCounter.get(i) / (double) numberOfConcurrentUsers);
        }

        // Saves the reference usage model and sets the usage model and the EntryCallSequenceModel
        // as the reference elements. Our approach is now executed with the EntryCallSequenceModel
        // and the resulting usage model can be matched against the reference usage model
        this.saveModel(usageModel, this.usageModelFolder + "ReferenceModel.usagemodel");
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }

    /**
     * Creates a reference model that contains a simple sequence of calls. Accordingly, user
     * sessions whose call sequences contain a simple call sequence are created. (RQ-1.1) It is also
     * used to evaluate the accuracy of workload specifications. Therefore, varying workload is
     * generated by random entry and exit times of the user sessions, a random number of user
     * sessions for a closed workload specification and a random mean inter arrival time for an open
     * workload specification (RQ-1.9)
     *
     * @param thinkTime
     *            of a closed workload.
     * @param isClosedWorkload
     *            decides whether a closed or an open workload is created
     * @return the reference usage model, a corresponding EntryCallSequenceModel and a reference
     *         workload
     * @throws IOException
     */
    public ReferenceElements getSimpleSequenceReferenceModel(final int thinkTime, final boolean isClosedWorkload)
            throws IOException {

        // Creates a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model.
        final int numberOfUsersSessions = this.getRandomInteger(200, 1);
        final int numberOfCalls = this.getRandomInteger(5, 1);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                this.getUserSessions(numberOfUsersSessions));
        final ReferenceElements referenceElements = new ReferenceElements();

        // In the following the reference usage model is created
        AbstractUserAction lastAction;
        final UsageModel usageModel = this.usageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
        final Stop stop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
        lastAction = start;
        Optional<Correspondent> optionCorrespondent;
        // According to the randomly set length of the call sequence, EntryLevelSystemCalls are
        // created
        for (int i = 0; i < numberOfCalls; i++) {
            switch (i) {
            case 0:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                        this.operationSignature1);
                break;
            case 1:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2,
                        this.operationSignature2);
                break;
            case 2:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3,
                        this.operationSignature3);
                break;
            case 3:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4,
                        this.operationSignature4);
                break;
            case 4:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5,
                        this.operationSignature5);
                break;
            default:
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
        }
        this.usageModelBuilder.connect(lastAction, stop);

        // According to the reference usage model user sessions are created that exactly represent
        // the user behavior of the reference usage model. The entry and exit times are set randomly
        // to evaluate a closed workload. For the evaluation of an open workload the mean inter
        // arrival time is set randomly
        int entryTime = 0;
        int exitTime = 1;
        final int meanInterArrivalTime = this.getRandomInteger(30, 1);

        for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
            if (isClosedWorkload) {
                entryTime = this.getRandomInteger(30, 1);
                exitTime = entryTime + 1;
            } else {
                entryTime += meanInterArrivalTime;
                exitTime += meanInterArrivalTime;
            }
            for (int k = 0; k < numberOfCalls; k++) {
                EntryCallEvent entryCallEvent = null;
                switch (k) {
                case 0:
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    break;
                case 1:
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2,
                            this.classSignature2, String.valueOf(i), "hostname");
                    break;
                case 2:
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3,
                            this.classSignature3, String.valueOf(i), "hostname");
                    break;
                case 3:
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                            this.classSignature4, String.valueOf(i), "hostname");
                    break;
                case 4:
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5,
                            this.classSignature5, String.valueOf(i), "hostname");
                    break;
                default:
                    throw new IllegalArgumentException("Illegal value of model element parameter");
                }
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime = entryTime + 2;
                exitTime = exitTime + 2;
            }
        }

        // Saves the reference usage model and sets the usage model, the EntryCallSequenceModel
        // and the workload as the reference elements. Our approach is now executed with the
        // EntryCallSequenceModel and the resulting usage model can be matched against the reference
        // usage model. Alike, the by our approach calculated workload can be matched against the
        // reference workload. This is done by {@link
        // org.iobserve.analysis.userbehavior.test.WorkloadEvaluation}
        this.saveModel(usageModel, this.usageModelFolder + "ReferenceModel.usagemodel");
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);
        referenceElements.setMeanInterArrivalTime(meanInterArrivalTime + (numberOfCalls * 2));
        referenceElements.setMeanConcurrentUserSessions(
                this.calculateTheNumberOfConcurrentUsers(entryCallSequenceModel.getUserSessions()));

        return referenceElements;
    }

    /**
     * Creates a reference model that contains a loop element. The user sessions contain iterated
     * call sequences that share overlapping calls. Thereby, one iterated sequence consists of more
     * calls than the other. Thus, it can be checked whether the approach transforms the iterated
     * call sequence that consists of more calls to a loop (RQ-1.4)
     *
     * @return a reference usage model and corresponding user sessions
     * @throws IOException
     */
    public ReferenceElements getOverlappingIterationReferenceModel() throws IOException {

        // Creates a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model.
        final int numberOfConcurrentUsers = this.getRandomInteger(200, 1);
        // One of the iterated sequences is iterated twice and one is iterated three times. The
        // number of iterations is set randomly.
        final int loopCount1 = this.getRandomInteger(3, 2);
        final int lengthOfSequence1 = 2 * loopCount1;
        int loopCount2;
        if (loopCount1 == 3) {
            loopCount2 = 2;
        } else {
            loopCount2 = 3;
        }
        final int lengthOfSequence2 = 2 * loopCount2;

        final ReferenceElements referenceElements = new ReferenceElements();
        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                this.getUserSessions(numberOfConcurrentUsers));

        // In the following the reference usage model is created
        AbstractUserAction lastAction;
        final UsageModel usageModel = this.usageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
        final Stop stop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
        // According to the randomly set number of iterations the sequence that is iterated three
        // times is represented by a loop element. The other sequence is represented by a sequence
        final Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
        if (lengthOfSequence1 >= lengthOfSequence2) {
            this.usageModelBuilder.connect(start, loop);
            final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
            pcmLoopIteration.setSpecification(String.valueOf(loopCount1));
            loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
            final Start loopStart = this.usageModelBuilder.createStart("");
            this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
            final Stop loopStop = this.usageModelBuilder.createStop("");
            this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
            lastAction = loopStart;
            Optional<Correspondent> optionCorrespondent;
            optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                    this.operationSignature1);
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3,
                    this.operationSignature3);
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            this.usageModelBuilder.connect(lastAction, loopStop);

            lastAction = loop;
            optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4,
                    this.operationSignature4);
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3,
                    this.operationSignature3);
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4,
                    this.operationSignature4);
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }

            this.usageModelBuilder.connect(lastAction, stop);

        } else {

            lastAction = start;
            Optional<Correspondent> optionCorrespondent;
            optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                    this.operationSignature1);
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3,
                    this.operationSignature3);
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                    this.operationSignature1);
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(scenarioBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }

            this.usageModelBuilder.connect(lastAction, loop);
            final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
            pcmLoopIteration.setSpecification(String.valueOf(loopCount2));
            loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
            final Start loopStart = this.usageModelBuilder.createStart("");
            this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
            final Stop loopStop = this.usageModelBuilder.createStop("");
            this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
            lastAction = loopStart;

            optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3,
                    this.operationSignature3);
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4,
                    this.operationSignature4);
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }

            this.usageModelBuilder.connect(lastAction, loopStop);
            this.usageModelBuilder.connect(loop, stop);
        }

        // According to the reference usage model user sessions are created that exactly represent
        // the user behavior of the reference usage model. The entry and exit times enable that the
        // calls within the user sessions are ordered according to the reference usage model.
        for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {
            // According to the randomly set parameter one of the sequences is iterated twice and
            // one is iterated threee times
            if (lengthOfSequence1 >= lengthOfSequence2) {
                EntryCallEvent entryCallEvent;
                int entryTime = 1;
                int exitTime = 2;
                for (int k = 0; k < loopCount1; k++) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3,
                            this.classSignature3, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                }
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
            } else {
                EntryCallEvent entryCallEvent;
                int entryTime = 1;
                int exitTime = 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3, this.classSignature3,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                for (int k = 0; k < loopCount2; k++) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3,
                            this.classSignature3, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                            this.classSignature4, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                }
            }
        }

        // Saves the reference usage model and sets the usage model and the EntryCallSequenceModel
        // as the reference elements. Our approach is now executed with the EntryCallSequenceModel
        // and the resulting usage model can be matched against the reference usage model
        this.saveModel(usageModel, this.usageModelFolder + "ReferenceModel.usagemodel");
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }

    /**
     * It creates a reference usage model that contains nested branches. Accordingly, user sessions
     * whose call sequences differ from each other at the positions of the branches are
     * created.(RQ-1.5)
     *
     * @return a reference usage model and corresponding user sessions
     * @throws IOException
     */
    public ReferenceElements getBranchWithinBranchReferenceModel() throws IOException {

        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model. The minimum number of user sessions is set
        // dependently from the random number of branch transitions, because it must be ensured that
        // each branch transition is represented within the user sessions.
        final int numberOfTransitionsOfExteriorBranch = this.getRandomInteger(3, 2);
        final int numberOfTransitionsOfInteriorBranches = this.getRandomInteger(3, 2);
        final int numberOfConcurrentUsers = this.getRandomInteger(200, 10 * numberOfTransitionsOfExteriorBranch);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                this.getUserSessions(numberOfConcurrentUsers));
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
                int branchDecisioner = this.getRandomInteger(numberOfTransitionsOfExteriorBranch - 1, 0);
                if (branchDecisioner == 0) {
                    int countOfBranchTransition = branchTransitionCounter.get(0) + 1;
                    branchTransitionCounter.set(0, countOfBranchTransition);
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    branchDecisioner = this.getRandomInteger(numberOfTransitionsOfInteriorBranches - 1, 0);
                    for (int k = 0; k < numberOfTransitionsOfInteriorBranches; k++) {
                        if (listOfbranchTransitionCounterInterior.get(0).get(k) == 0) {
                            branchDecisioner = k;
                            break;
                        }
                    }
                    // Within the branch transition again a random branch transition is chosen
                    if (branchDecisioner == 0) {
                        countOfBranchTransition = listOfbranchTransitionCounterInterior.get(0).get(0) + 1;
                        listOfbranchTransitionCounterInterior.get(0).set(0, countOfBranchTransition);
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                                this.classSignature1, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    } else if (branchDecisioner == 1) {
                        countOfBranchTransition = listOfbranchTransitionCounterInterior.get(0).get(1) + 1;
                        listOfbranchTransitionCounterInterior.get(0).set(1, countOfBranchTransition);
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                                this.classSignature4, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    } else if (branchDecisioner == 2) {
                        countOfBranchTransition = listOfbranchTransitionCounterInterior.get(0).get(2) + 1;
                        listOfbranchTransitionCounterInterior.get(0).set(2, countOfBranchTransition);
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5,
                                this.classSignature5, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                } else if (branchDecisioner == 1) {
                    int countOfBranchTransition = branchTransitionCounter.get(1) + 1;
                    branchTransitionCounter.set(1, countOfBranchTransition);
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2,
                            this.classSignature2, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    branchDecisioner = this.getRandomInteger(numberOfTransitionsOfInteriorBranches - 1, 0);
                    for (int k = 0; k < numberOfTransitionsOfInteriorBranches; k++) {
                        if (listOfbranchTransitionCounterInterior.get(1).get(k) == 0) {
                            branchDecisioner = k;
                            break;
                        }
                    }
                    // Within the branch transition again a random branch transition is chosen
                    if (branchDecisioner == 0) {
                        countOfBranchTransition = listOfbranchTransitionCounterInterior.get(1).get(0) + 1;
                        listOfbranchTransitionCounterInterior.get(1).set(0, countOfBranchTransition);
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                                this.classSignature1, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;

                    } else if (branchDecisioner == 1) {
                        countOfBranchTransition = listOfbranchTransitionCounterInterior.get(1).get(1) + 1;
                        listOfbranchTransitionCounterInterior.get(1).set(1, countOfBranchTransition);
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                                this.classSignature4, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    } else if (branchDecisioner == 2) {
                        countOfBranchTransition = listOfbranchTransitionCounterInterior.get(1).get(2) + 1;
                        listOfbranchTransitionCounterInterior.get(1).set(2, countOfBranchTransition);
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5,
                                this.classSignature5, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                } else if (branchDecisioner == 2) {
                    int countOfBranchTransition = branchTransitionCounter.get(2) + 1;
                    branchTransitionCounter.set(2, countOfBranchTransition);
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3,
                            this.classSignature3, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    branchDecisioner = this.getRandomInteger(numberOfTransitionsOfInteriorBranches - 1, 0);
                    for (int k = 0; k < numberOfTransitionsOfInteriorBranches; k++) {
                        if (listOfbranchTransitionCounterInterior.get(2).get(k) == 0) {
                            branchDecisioner = k;
                            break;
                        }
                    }
                    // Within the branch transition again a random branch transition is chosen
                    if (branchDecisioner == 0) {
                        countOfBranchTransition = listOfbranchTransitionCounterInterior.get(2).get(0) + 1;
                        listOfbranchTransitionCounterInterior.get(2).set(0, countOfBranchTransition);
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                                this.classSignature1, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;

                    } else if (branchDecisioner == 1) {
                        countOfBranchTransition = listOfbranchTransitionCounterInterior.get(2).get(1) + 1;
                        listOfbranchTransitionCounterInterior.get(2).set(1, countOfBranchTransition);
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                                this.classSignature4, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    } else if (branchDecisioner == 2) {
                        countOfBranchTransition = listOfbranchTransitionCounterInterior.get(2).get(2) + 1;
                        listOfbranchTransitionCounterInterior.get(2).set(2, countOfBranchTransition);
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5,
                                this.classSignature5, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
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
        final UsageModel usageModel = this.usageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
        final Stop stop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
        lastAction = start;

        // The exterior branch is created
        final org.palladiosimulator.pcm.usagemodel.Branch branch = this.usageModelBuilder.createBranch("",
                scenarioBehaviour);
        this.usageModelBuilder.connect(lastAction, branch);
        this.usageModelBuilder.connect(branch, stop);

        // Creates branch transitions according to the random countOfBranchTransitions
        for (int i = 0; i < numberOfTransitionsOfExteriorBranch; i++) {
            final BranchTransition branchTransition = this.usageModelBuilder.createBranchTransition(branch);
            final ScenarioBehaviour branchTransitionBehaviour = branchTransition
                    .getBranchedBehaviour_BranchTransition();
            branchTransition
                    .setBranchProbability((double) branchTransitionCounter.get(i) / (double) numberOfConcurrentUsers);
            final Start startBranchTransition = this.usageModelBuilder.createStart("");
            this.usageModelBuilder.addUserAction(branchTransitionBehaviour, startBranchTransition);
            final Stop stopBranchTransition = this.usageModelBuilder.createStop("");
            this.usageModelBuilder.addUserAction(branchTransitionBehaviour, stopBranchTransition);
            lastAction = startBranchTransition;
            switch (i) {
            case 0:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                        this.operationSignature1);
                break;
            case 1:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2,
                        this.operationSignature2);
                break;
            case 2:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3,
                        this.operationSignature3);
                break;
            default:
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }

            // The interior branch is created
            final org.palladiosimulator.pcm.usagemodel.Branch branchInterior = this.usageModelBuilder.createBranch("",
                    branchTransitionBehaviour);
            this.usageModelBuilder.connect(lastAction, branchInterior);
            this.usageModelBuilder.connect(branchInterior, stopBranchTransition);

            for (int j = 0; j < numberOfTransitionsOfInteriorBranches; j++) {
                final BranchTransition branchTransitionInterior = this.usageModelBuilder
                        .createBranchTransition(branchInterior);
                final ScenarioBehaviour branchTransitionBehaviourInterior = branchTransitionInterior
                        .getBranchedBehaviour_BranchTransition();
                branchTransitionInterior
                        .setBranchProbability((double) listOfbranchTransitionCounterInterior.get(i).get(j)
                                / (double) branchTransitionCounter.get(i));
                final Start startBranchTransitionInterior = this.usageModelBuilder.createStart("");
                this.usageModelBuilder.addUserAction(branchTransitionBehaviourInterior, startBranchTransitionInterior);
                final Stop stopBranchTransitionInterior = this.usageModelBuilder.createStop("");
                this.usageModelBuilder.addUserAction(branchTransitionBehaviourInterior, stopBranchTransitionInterior);
                lastAction = startBranchTransitionInterior;
                switch (j) {
                case 0:
                    optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                            this.operationSignature1);
                    break;
                case 1:
                    optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4,
                            this.operationSignature4);
                    break;
                case 2:
                    optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5,
                            this.operationSignature5);
                    break;
                default:
                    throw new IllegalArgumentException("Illegal value of model element parameter");
                }
                if (optionCorrespondent.isPresent()) {
                    final Correspondent correspondent = optionCorrespondent.get();
                    final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                            .createEntryLevelSystemCall(correspondent);
                    this.usageModelBuilder.addUserAction(branchTransitionBehaviourInterior, entryLevelSystemCall);
                    this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                    lastAction = entryLevelSystemCall;
                }
                this.usageModelBuilder.connect(lastAction, stopBranchTransitionInterior);

            }

        }

        // Saves the reference usage model and sets the usage model and the EntryCallSequenceModel
        // as the reference elements. Our approach is now executed with the EntryCallSequenceModel
        // and the resulting usage model can be matched against the reference usage model
        this.saveModel(usageModel, this.usageModelFolder + "ReferenceModel.usagemodel");
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }

    /**
     * It creates a reference usage model that contains loops within branches. Accordingly, user
     * sessions whose call sequences differ from each other at the positions of the branches and
     * that contain iterated call sequences are created.(RQ-1.6)
     *
     * @return a reference model and corresponding user sessions
     * @throws IOException
     */
    public ReferenceElements getLoopWithinBranchReferenceModel() throws IOException {

        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model. The minimum number of user sessions is set
        // dependently from the random number of branch transitions, because it must be ensured that
        // each branch transition is represented within the user sessions.
        final int numberOfBranchTransitions = this.getRandomInteger(3, 2);
        final int numberOfConcurrentUsers = this.getRandomInteger(30, 10 * numberOfBranchTransitions);
        final int lengthOfBranchSequence = this.getRandomInteger(2, 1);
        final int countOfLoop = this.getRandomInteger(3, 2);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                this.getUserSessions(numberOfConcurrentUsers));
        final ReferenceElements referenceElements = new ReferenceElements();

        // In the following the reference usage model is created
        AbstractUserAction lastAction;
        Optional<Correspondent> optionCorrespondent;
        final UsageModel usageModel = this.usageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
        final Stop stop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
        lastAction = start;

        // Creates a branch and branch transitions according to the random countOfBranchTransitions
        final org.palladiosimulator.pcm.usagemodel.Branch branch = this.usageModelBuilder.createBranch("",
                scenarioBehaviour);
        this.usageModelBuilder.connect(lastAction, branch);
        this.usageModelBuilder.connect(branch, stop);
        // For each branch transition its calls are added to the branch transition
        for (int i = 0; i < numberOfBranchTransitions; i++) {
            final BranchTransition branchTransition = this.usageModelBuilder.createBranchTransition(branch);
            final ScenarioBehaviour branchTransitionBehaviour = branchTransition
                    .getBranchedBehaviour_BranchTransition();
            final Start startBranchTransition = this.usageModelBuilder.createStart("");
            this.usageModelBuilder.addUserAction(branchTransitionBehaviour, startBranchTransition);
            final Stop stopBranchTransition = this.usageModelBuilder.createStop("");
            this.usageModelBuilder.addUserAction(branchTransitionBehaviour, stopBranchTransition);
            lastAction = startBranchTransition;
            switch (i) {
            case 0:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                        this.operationSignature1);
                break;
            case 1:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2,
                        this.operationSignature2);
                break;
            case 2:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3,
                        this.operationSignature3);
                break;
            default:
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            if (lengthOfBranchSequence == 2) {
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5,
                        this.operationSignature5);
                if (optionCorrespondent.isPresent()) {
                    final Correspondent correspondent = optionCorrespondent.get();
                    final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                            .createEntryLevelSystemCall(correspondent);
                    this.usageModelBuilder.addUserAction(branchTransitionBehaviour, entryLevelSystemCall);
                    this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                    lastAction = entryLevelSystemCall;
                }
            }

            // Within the branch transition a loop element is created
            final Loop loop = this.usageModelBuilder.createLoop("", branchTransitionBehaviour);
            this.usageModelBuilder.connect(lastAction, loop);
            final PCMRandomVariable pcmLoop2Iteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
            pcmLoop2Iteration.setSpecification(String.valueOf(countOfLoop));
            loop.setLoopIteration_Loop(pcmLoop2Iteration);
            final Start loopStart = this.usageModelBuilder.createStart("");
            this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
            final Stop loopStop = this.usageModelBuilder.createStop("");
            this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
            lastAction = loopStart;
            // The calls that are iterated are added to the loop
            switch (i) {
            case 0:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature2,
                        this.operationSignature2);
                break;
            case 1:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3,
                        this.operationSignature3);
                break;
            case 2:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature1,
                        this.operationSignature1);
                break;
            default:
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
            this.usageModelBuilder.connect(lastAction, loopStop);
            this.usageModelBuilder.connect(loop, stopBranchTransition);
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
                final int branchDecisioner = this.getRandomInteger(numberOfBranchTransitions - 1, 0);
                if (branchDecisioner == 0) {
                    final int countOfBranchTransition = branchTransitionCounter.get(0) + 1;
                    branchTransitionCounter.set(0, countOfBranchTransition);
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    if (lengthOfBranchSequence == 2) {
                        final EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, exitTime,
                                this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                    // Within the branch transition the loop is represented by iterated calls
                    for (int j = 0; j < countOfLoop; j++) {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2,
                                this.classSignature2, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                } else if (branchDecisioner == 1) {
                    final int countOfBranchTransition = branchTransitionCounter.get(1) + 1;
                    branchTransitionCounter.set(1, countOfBranchTransition);
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2,
                            this.classSignature2, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    if (lengthOfBranchSequence == 2) {
                        final EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, exitTime,
                                this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                    // Within the branch transition the loop is represented by iterated calls
                    for (int j = 0; j < countOfLoop; j++) {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3,
                                this.classSignature3, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                } else if (branchDecisioner == 2) {
                    final int countOfBranchTransition = branchTransitionCounter.get(2) + 1;
                    branchTransitionCounter.set(2, countOfBranchTransition);
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3,
                            this.classSignature3, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                    if (lengthOfBranchSequence == 2) {
                        final EntryCallEvent entryCallEvent2 = new EntryCallEvent(entryTime, exitTime,
                                this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent2, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                    // Within the branch transition the loop is represented by iterated calls
                    for (int j = 0; j < countOfLoop; j++) {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                                this.classSignature1, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime = entryTime + 2;
                        exitTime = exitTime + 2;
                    }
                }

            }
            // It is checked wheter all branch transitions are represented within the user sessions
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
        this.saveModel(usageModel, this.usageModelFolder + "ReferenceModel.usagemodel");
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }

    /**
     * It creates a reference usage model that contains loops within loops. Accordingly, user
     * sessions whose call sequences contain iterated segments that again contain iterated segments
     * are created.(RQ-1.7)
     *
     * @return reference usage model and corresponding user sessions
     * @throws IOException
     */
    public ReferenceElements getLoopWithinLoopReferenceModel() throws IOException {

        // Create a random number of user sessions and random model element parameters. The user
        // sessions' behavior will be created according to the reference usage model and
        // subsequently the user sessions are used to create a usage model. The created usage model
        // is matched against the reference usage model
        final int numberOfConcurrentUsers = this.getRandomInteger(200, 1);
        final int countOfLoop1 = this.getRandomInteger(4, 2);
        final int countOfLoop2 = this.getRandomInteger(4, 2);
        final int lengthOfSubsequentLoopSequence = this.getRandomInteger(2, 1);

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                this.getUserSessions(numberOfConcurrentUsers));
        final ReferenceElements referenceElements = new ReferenceElements();

        // In the following the reference usage model is created
        Optional<Correspondent> optionCorrespondent;
        AbstractUserAction lastAction;
        final UsageModel usageModel = this.usageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
        final Stop stop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
        lastAction = start;

        // The exterior loop is created
        final Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
        this.usageModelBuilder.connect(lastAction, loop);
        this.usageModelBuilder.connect(loop, stop);
        final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmLoopIteration.setSpecification(String.valueOf(countOfLoop1));
        loop.setLoopIteration_Loop(pcmLoopIteration);
        final Start loopStart = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStart);
        final Stop loopStop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), loopStop);
        lastAction = loopStart;

        // The interior loop is created
        final Loop loop2 = this.usageModelBuilder.createLoop("", loop.getBodyBehaviour_Loop());
        this.usageModelBuilder.connect(lastAction, loop2);
        final PCMRandomVariable pcmLoop2Iteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmLoop2Iteration.setSpecification(String.valueOf(countOfLoop2));
        loop2.setLoopIteration_Loop(pcmLoop2Iteration);
        final Start loop2Start = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(loop2.getBodyBehaviour_Loop(), loop2Start);
        final Stop loop2Stop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(loop2.getBodyBehaviour_Loop(), loop2Stop);
        lastAction = loop2Start;
        optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
        if (optionCorrespondent.isPresent()) {
            final Correspondent correspondent = optionCorrespondent.get();
            final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                    .createEntryLevelSystemCall(correspondent);
            this.usageModelBuilder.addUserAction(loop2.getBodyBehaviour_Loop(), entryLevelSystemCall);
            this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
            lastAction = entryLevelSystemCall;
        }
        this.usageModelBuilder.connect(lastAction, loop2Stop);
        lastAction = loop2;

        // The sequence that exclusively belongs to the exterior loop is created
        for (int i = 0; i < lengthOfSubsequentLoopSequence; i++) {
            switch (i) {
            case 0:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4,
                        this.operationSignature4);
                break;
            case 1:
                optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5,
                        this.operationSignature5);
                break;
            default:
                throw new IllegalArgumentException("Illegal value of model element parameter");
            }
            if (optionCorrespondent.isPresent()) {
                final Correspondent correspondent = optionCorrespondent.get();
                final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                        .createEntryLevelSystemCall(correspondent);
                this.usageModelBuilder.addUserAction(loop.getBodyBehaviour_Loop(), entryLevelSystemCall);
                this.usageModelBuilder.connect(lastAction, entryLevelSystemCall);
                lastAction = entryLevelSystemCall;
            }
        }
        this.usageModelBuilder.connect(lastAction, loopStop);

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
                            this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime = entryTime + 2;
                    exitTime = exitTime + 2;
                }
                // Calls of the exterior loop
                for (int j = 0; j < lengthOfSubsequentLoopSequence; j++) {
                    EntryCallEvent entryCallEvent = null;
                    switch (j) {
                    case 0:
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                                this.classSignature4, String.valueOf(i), "hostname");
                        break;
                    case 1:
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5,
                                this.classSignature5, String.valueOf(i), "hostname");
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
        this.saveModel(usageModel, this.usageModelFolder + "ReferenceModel.usagemodel");
        referenceElements.setEntryCallSequenceModel(entryCallSequenceModel);
        referenceElements.setUsageModel(usageModel);

        return referenceElements;
    }

    /**
     * It creates a reference usage model that contains branches within loops. Accordingly, user
     * sessions whose call sequences differ from each other are iterated in a row. Thereby, at each
     * iteration of a branched call sequences the probabilities have to be equal because otherwise
     * it would not be an iteration (RQ-1.8)
     *
     * @return reference usage model and corresponding user sessions
     * @throws IOException
     */
    public ReferenceElements getBranchWithinLoopReferenceModel() throws IOException {

        // The number of model element parameters are created randomly. The number of user sessions
        // must be created accordingly to the number of branch transitions, because it must be
        // ensured that at each iteration of an branch the branch transition probabilities are
        // equal. This can be achieved by the same number of user sessions representing the branch
        // transition at each iteration
        final int numberOfLoops = this.getRandomInteger(3, 3);
        final int numberOfConcurrentUsers = (int) Math.pow(2, numberOfLoops) * 5;

        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                this.getUserSessions(numberOfConcurrentUsers));
        final ReferenceElements testElements = new ReferenceElements();

        // In the following the reference usage model is created
        AbstractUserAction lastAction;
        Optional<Correspondent> optionCorrespondent;
        final UsageModel usageModel = this.usageModelBuilder.createUsageModel();
        final UsageScenario usageScenario = this.usageModelBuilder.createUsageScenario("", usageModel);
        final ScenarioBehaviour scenarioBehaviour = usageScenario.getScenarioBehaviour_UsageScenario();
        final Start start = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, start);
        final Stop stop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(scenarioBehaviour, stop);
        lastAction = start;

        // The loop element is created that contains the iterated branch
        final Loop loop = this.usageModelBuilder.createLoop("", scenarioBehaviour);
        final ScenarioBehaviour loopScenarioBehaviour = loop.getBodyBehaviour_Loop();
        this.usageModelBuilder.connect(lastAction, loop);
        final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmLoopIteration.setSpecification(String.valueOf(numberOfLoops));
        loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops
        this.usageModelBuilder.connect(loop, stop);
        final Start loopStart = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(loopScenarioBehaviour, loopStart);
        final Stop loopStop = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(loopScenarioBehaviour, loopStop);

        // The branch that is contained within the loop element is created
        final org.palladiosimulator.pcm.usagemodel.Branch branch = this.usageModelBuilder.createBranch("",
                loopScenarioBehaviour);
        this.usageModelBuilder.connect(loopStart, branch);

        // The branch transition 1 is created
        final BranchTransition branchTransition1 = this.usageModelBuilder.createBranchTransition(branch);
        final ScenarioBehaviour branchTransition1Behaviour = branchTransition1.getBranchedBehaviour_BranchTransition();
        final Start startBranchTransition1 = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(branchTransition1Behaviour, startBranchTransition1);
        final Stop stopBranchTransition1 = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(branchTransition1Behaviour, stopBranchTransition1);
        optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature3, this.operationSignature3);
        if (optionCorrespondent.isPresent()) {
            final Correspondent correspondent = optionCorrespondent.get();
            final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                    .createEntryLevelSystemCall(correspondent);
            this.usageModelBuilder.addUserAction(branchTransition1Behaviour, entryLevelSystemCall);
            this.usageModelBuilder.connect(startBranchTransition1, entryLevelSystemCall);
            this.usageModelBuilder.connect(entryLevelSystemCall, stopBranchTransition1);
        }

        // The branch transition 2 is created
        final BranchTransition branchTransition2 = this.usageModelBuilder.createBranchTransition(branch);
        final ScenarioBehaviour branchTransition2Behaviour = branchTransition2.getBranchedBehaviour_BranchTransition();
        final Start startBranchTransition2 = this.usageModelBuilder.createStart("");
        this.usageModelBuilder.addUserAction(branchTransition2Behaviour, startBranchTransition2);
        final Stop stopBranchTransition2 = this.usageModelBuilder.createStop("");
        this.usageModelBuilder.addUserAction(branchTransition2Behaviour, stopBranchTransition2);
        optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature4, this.operationSignature4);
        if (optionCorrespondent.isPresent()) {
            final Correspondent correspondent = optionCorrespondent.get();
            final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                    .createEntryLevelSystemCall(correspondent);
            this.usageModelBuilder.addUserAction(branchTransition2Behaviour, entryLevelSystemCall);
            this.usageModelBuilder.connect(startBranchTransition2, entryLevelSystemCall);
            this.usageModelBuilder.connect(entryLevelSystemCall, stopBranchTransition2);
        }

        optionCorrespondent = this.correspondenceModel.getCorrespondent(this.classSignature5, this.operationSignature5);
        if (optionCorrespondent.isPresent()) {
            final Correspondent correspondent = optionCorrespondent.get();
            final EntryLevelSystemCall entryLevelSystemCall = this.usageModelBuilder
                    .createEntryLevelSystemCall(correspondent);
            this.usageModelBuilder.addUserAction(loopScenarioBehaviour, entryLevelSystemCall);
            this.usageModelBuilder.connect(branch, entryLevelSystemCall);
            this.usageModelBuilder.connect(entryLevelSystemCall, loopStop);
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
        final HashMap<Integer, List<List<UserSession>>> userSessionGroups = new HashMap<Integer, List<List<UserSession>>>();
        final List<List<UserSession>> startList = new ArrayList<List<UserSession>>();
        startList.add(entryCallSequenceModel.getUserSessions());
        userSessionGroups.put(0, startList);

        // The loop that contains the branch
        for (int j = 0; j < numberOfLoops; j++) {

            countOfCallEvent3 = 0;
            countOfCallEvent4 = 0;

            final List<List<UserSession>> newUserSessionGroups = new ArrayList<List<UserSession>>();

            // Ensures that the user sessions distribution stays equally
            for (int k = 0; k < userSessionGroups.get(j).size(); k++) {

                for (int i = 0; i < 2; i++) {
                    final List<UserSession> userSessions = new ArrayList<UserSession>();
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
                        final EntryCallEvent entryCallEvent3 = new EntryCallEvent(entryTime, exitTime,
                                this.operationSignature3, this.classSignature3, String.valueOf(i), "hostname");
                        userSessionGroups.get(j).get(k).get(i).add(entryCallEvent3, true);
                        newUserSessionGroups.get(indexGroupCallEvent3).add(userSessionGroups.get(j).get(k).get(i));
                        countOfCallEvent3++;
                        entryTime += 2;
                        exitTime += 2;
                    } else {
                        final EntryCallEvent entryCallEvent4 = new EntryCallEvent(entryTime, exitTime,
                                this.operationSignature4, this.classSignature4, String.valueOf(i), "hostname");
                        userSessionGroups.get(j).get(k).get(i).add(entryCallEvent4, true);
                        newUserSessionGroups.get(indexGroupCallEvent4).add(userSessionGroups.get(j).get(k).get(i));
                        countOfCallEvent4++;
                        entryTime += 2;
                        exitTime += 2;
                    }

                    final EntryCallEvent entryCallEvent5 = new EntryCallEvent(entryTime, exitTime,
                            this.operationSignature5, this.classSignature5, String.valueOf(i), "hostname");
                    userSessionGroups.get(j).get(k).get(i).add(entryCallEvent5, true);
                    entryTime += 2;
                    exitTime += 2;

                    entryTime -= 4;
                    exitTime -= 4;
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
        this.saveModel(usageModel, this.usageModelFolder + "ReferenceModel.usagemodel");
        testElements.setEntryCallSequenceModel(entryCallSequenceModel);
        testElements.setUsageModel(usageModel);

        return testElements;
    }

    /**
     * It creates the passed number of user sessions. Thereby, two user groups are distinguished.
     * Each user session of a user group contains the same call sequence. The call sequences between
     * the user groups differ from each other by their operation signatures. It is used to evaluate
     * the approach's response times with an increasing number of user sessions (RQ-3.1) It returns
     * user session that are used to execute the approach and to measure the response time Thereby,
     * this method is called repeatedly to constantly increase the number of UserSessions
     *
     *
     * @param numberOfUserSessions
     *            defines the number of user sessions to create
     * @return user sessions with a fixed user behavior
     */
    public ReferenceElements getIncreasingUserSessionsScalabilityReferenceModel(final int numberOfUserSessions) {

        final ReferenceElements testElements = new ReferenceElements();
        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                this.getUserSessions(numberOfUserSessions));

        int entryTime = 1;
        int exitTime = 2;

        // Creates for each of the user sessions the same user behavior. This ensures that the
        // progressions of the response time can be ascribed to the increasing number of user
        // sessions
        for (int i = 0; i < entryCallSequenceModel.getUserSessions().size(); i++) {

            final double userGroupDecisioner = (double) i / (double) numberOfUserSessions;

            // It is distinguished between two user groups. The user groups differ from each other
            // by their operation signatures
            if (userGroupDecisioner < 0.3) {
                // One single call
                EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                        this.classSignature1, String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                // An iterated call to represent a loop
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2, this.classSignature2,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                // Alternative calls to represent a branch
                final int branchDecisioner = this.getRandomInteger(2, 1);
                if (branchDecisioner == 1) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                } else {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2,
                            this.classSignature2, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                }
                // An equal call to merge the branch transitions
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1, this.classSignature1,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
            } else {
                // One single call
                EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                        this.classSignature4, String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                // An iterated call to represent a loop
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5, this.classSignature5,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
                // Alternative calls to represent a branch
                final int branchDecisioner = this.getRandomInteger(2, 1);
                if (branchDecisioner == 1) {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                            this.classSignature4, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                } else {
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5,
                            this.classSignature5, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                }
                // An equal call to merge the branch transitions
                entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4, this.classSignature4,
                        String.valueOf(i), "hostname");
                entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                entryTime += 2;
                exitTime += 2;
            }

        }

        // Returns the created user sessions. Now our approach can be executed and the response
        // times can be measured
        testElements.setEntryCallSequenceModel(entryCallSequenceModel);
        return testElements;
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
    public ReferenceElements getIncreasingCallSequenceScalabilityReferenceModel(final int numberOfIterations) {

        final ReferenceElements testElements = new ReferenceElements();
        final int numberOfUserSessions = 50;
        final EntryCallSequenceModel entryCallSequenceModel = new EntryCallSequenceModel(
                this.getUserSessions(numberOfUserSessions));

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
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    // An iterated call to represent a loop
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2,
                            this.classSignature2, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2,
                            this.classSignature2, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    // Alternative calls to represent a branch
                    final int branchDecisioner = this.getRandomInteger(2, 1);
                    if (branchDecisioner == 1) {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                                this.classSignature1, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime += 2;
                        exitTime += 2;
                    } else {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature2,
                                this.classSignature2, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime += 2;
                        exitTime += 2;
                    }
                    // An equal call to merge the branch transitions
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature3,
                            this.classSignature3, String.valueOf(i), "hostname");
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
                    EntryCallEvent entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                            this.classSignature4, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    // An iterated call to represent a loop
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5,
                            this.classSignature5, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5,
                            this.classSignature5, String.valueOf(i), "hostname");
                    entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                    entryTime += 2;
                    exitTime += 2;
                    // Alternative calls to represent a branch
                    final int branchDecisioner = this.getRandomInteger(2, 1);
                    if (branchDecisioner == 1) {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature4,
                                this.classSignature4, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime += 2;
                        exitTime += 2;
                    } else {
                        entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature5,
                                this.classSignature5, String.valueOf(i), "hostname");
                        entryCallSequenceModel.getUserSessions().get(i).add(entryCallEvent, true);
                        entryTime += 2;
                        exitTime += 2;
                    }
                    // An equal call to merge the branch transitions
                    entryCallEvent = new EntryCallEvent(entryTime, exitTime, this.operationSignature1,
                            this.classSignature1, String.valueOf(i), "hostname");
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

    /**
     * Creates new user sessions
     *
     * @param numberOfUserSessionsToCreate
     *            defines the number of user sessions
     * @return new user sessions
     */
    private List<UserSession> getUserSessions(final int numberOfUserSessionsToCreate) {
        final List<UserSession> userSessions = new ArrayList<UserSession>();
        for (int i = 0; i < numberOfUserSessionsToCreate; i++) {
            final UserSession userSession = new UserSession("host", String.valueOf(i));
            userSessions.add(userSession);
        }
        return userSessions;
    }

    /**
     * @param max
     * @param min
     * @return random integer in the passed range
     */
    private int getRandomInteger(final int max, final int min) {
        final Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    /**
     * Calculates the exact mean number of concurrent user sessions as a reference workload
     *
     * @param sessions
     *            used to calculate the mean number of user sessions from
     * @return mean number of concurrent user sessions
     */
    private int calculateTheNumberOfConcurrentUsers(final List<UserSession> sessions) {
        int averageNumberOfConcurrentUsers = 0;
        if (sessions.size() > 0) {
            int countOfConcurrentUsers = 0;
            Collections.sort(sessions, this.SortUserSessionByEntryTime);
            for (int i = 0; i < sessions.size(); i++) {
                final long entryTimeUS1 = this.getEntryTime(sessions.get(i).getEvents());
                final long exitTimeUS1 = sessions.get(i).getExitTime();
                int numberOfConcurrentUserSessionsDuringThisSession = 1;
                for (int j = 0; j < sessions.size(); j++) {
                    if (j == i) {
                        continue;
                    }
                    final long entryTimeUS2 = sessions.get(j).getEntryTime();
                    final long exitTimeUS2 = sessions.get(j).getExitTime();
                    if (exitTimeUS2 < entryTimeUS1) {
                        continue;
                    }
                    if (exitTimeUS1 >= entryTimeUS2) {
                        numberOfConcurrentUserSessionsDuringThisSession++;
                    }
                }

                countOfConcurrentUsers += numberOfConcurrentUserSessionsDuringThisSession;
            }
            averageNumberOfConcurrentUsers = countOfConcurrentUsers / sessions.size();
        }

        return averageNumberOfConcurrentUsers;
    }

    /**
     * Sort user sessions by entry time
     */
    private final Comparator<UserSession> SortUserSessionByEntryTime = new Comparator<UserSession>() {

        @Override
        public int compare(final UserSession o1, final UserSession o2) {
            final long entryO1 = ReferenceUsageModelBuilder.this.getEntryTime(o1.getEvents());
            final long entryO2 = ReferenceUsageModelBuilder.this.getEntryTime(o2.getEvents());
            if (entryO1 > entryO2) {
                return 1;
            } else if (entryO1 < entryO2) {
                return -1;
            }
            return 0;
        }
    };

    /**
     * Returns the entry time of a user sessions
     *
     * @param events
     *            of an user sessions
     * @return the entry time of an user session
     */
    public long getEntryTime(final List<EntryCallEvent> events) {
        long entryTime = 0;
        if (events.size() > 0) {
            this.sortEventsBy(SortEntryCallEventsByEntryTime, events);
            entryTime = events.get(0).getEntryTime();
        }
        return entryTime;
    }

    public void sortEventsBy(final Comparator<EntryCallEvent> cmp, final List<EntryCallEvent> events) {
        Collections.sort(events, cmp);
    }

    /**
     * Sorts EntryCallEvents by their entry times
     */
    public static final Comparator<EntryCallEvent> SortEntryCallEventsByEntryTime = new Comparator<EntryCallEvent>() {

        @Override
        public int compare(final EntryCallEvent o1, final EntryCallEvent o2) {
            if (o1.getEntryTime() > o2.getEntryTime()) {
                return 1;
            } else if (o1.getEntryTime() < o2.getEntryTime()) {
                return -1;
            }
            return 0;
        }
    };

    /**
     * Saves a usage model to the drive
     *
     * @param obj
     *            that is saved
     * @param saveDestination
     *            directory to save to
     * @throws IOException
     */
    public void saveModel(final EObject obj, final String saveDestination) throws IOException {

        final Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
        final Map<String, Object> map = reg.getExtensionToFactoryMap();
        map.put("*", new XMIResourceFactoryImpl());

        final ResourceSet resSet = new ResourceSetImpl();
        resSet.setResourceFactoryRegistry(reg);

        final Resource res = resSet.createResource(URI.createFileURI(saveDestination));
        res.getContents().add(obj);
        try {
            res.save(null);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writte the JC and SRCC as .csv to the drive
     *
     * @param accuracyResults
     *            that are written
     * @throws IOException
     */
    public void writeAccuracyResults(final List<AccuracyResults> accuracyResults) throws IOException {

        final FileWriter writer = new FileWriter("/Users/David/Desktop/AccuracyEvaluationResults");
        writer.append("jc,srcc");
        writer.append('\n');

        for (final AccuracyResults accuracyResult : accuracyResults) {
            writer.append(String.valueOf(accuracyResult.getJc()));
            writer.append(',');
            writer.append(String.valueOf(accuracyResult.getSrcc()));
            writer.append('\n');
        }

        writer.flush();
        writer.close();
    }

    /**
     * Write the RME results as .csv
     *
     * @param accuracyResults
     *            that are written to the drive
     * @throws IOException
     */
    public void writeRME(final List<Double> accuracyResults) throws IOException {

        final FileWriter writer = new FileWriter("/Users/David/Desktop/RMEResults");
        writer.append("rme");
        writer.append('\n');

        double avg = 0;

        for (final Double rme : accuracyResults) {
            writer.append(String.valueOf(rme));
            writer.append('\n');
            avg += rme;
        }

        avg = avg / accuracyResults.size();
        writer.append(String.valueOf(avg));
        writer.append('\n');

        writer.flush();
        writer.close();
    }

}
