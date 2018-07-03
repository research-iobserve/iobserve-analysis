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
package org.iobserve.analysis.behavior.karlsruhe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.iobserve.analysis.behavior.karlsruhe.data.Branch;
import org.iobserve.analysis.behavior.karlsruhe.data.BranchElement;
import org.iobserve.analysis.behavior.karlsruhe.data.BranchModel;
import org.iobserve.analysis.behavior.karlsruhe.data.BranchTransitionElement;
import org.iobserve.analysis.behavior.karlsruhe.data.CallElement;
import org.iobserve.analysis.behavior.karlsruhe.data.ISequenceElement;
import org.iobserve.analysis.behavior.karlsruhe.data.LoopBranchElement;
import org.iobserve.analysis.behavior.karlsruhe.data.LoopElement;
import org.iobserve.model.correspondence.Correspondent;
import org.iobserve.model.correspondence.ICorrespondence;
import org.iobserve.model.factory.UsageModelFactory;
import org.iobserve.model.provider.deprecated.RepositoryLookupModelProvider;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates a PCM usage model from the passed LoopBranchModels. For each user group its
 * own LoopBranchModel was created before. This class creates for each user group its own usage
 * scenario within an usage model according to its LoopBranchModel. Also, for each usage scenario
 * its specific workload is created.
 *
 *
 * @author David Peter, Robert Heinrich
 */
public class PcmUsageModelBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PcmUsageModelBuilder.class);

    private final List<BranchModel> loopBranchModels;
    private final boolean isClosedWorkloadRequested;
    private final double thinkTime;
    private final List<Map<Integer, ScenarioBehaviour>> branchScenarioBehavioursOfUserGroups;
    private final ICorrespondence correspondenceModel;
    private final RepositoryLookupModelProvider repositoryLookupModel;

    /**
     *
     * @param loopBranchModels
     *            used to build for each user group its own usage scenario within a PCM usage model
     * @param isClosedWorkloadRequested
     *            states whether a closed or an open workload is requested
     * @param thinkTime
     *            states the think time of a closed workload
     * @param repositoryLookupModelProvider
     *            repository lookup model provider
     * @param correspondenceModel
     *            used to map calls to call elements of the usage model
     */
    public PcmUsageModelBuilder(final List<BranchModel> loopBranchModels, final boolean isClosedWorkloadRequested,
            final double thinkTime, final RepositoryLookupModelProvider repositoryLookupModelProvider,
            final ICorrespondence correspondenceModel) {
        this.loopBranchModels = loopBranchModels;
        this.isClosedWorkloadRequested = isClosedWorkloadRequested;
        this.thinkTime = thinkTime;
        this.branchScenarioBehavioursOfUserGroups = new ArrayList<>();
        this.correspondenceModel = correspondenceModel;
        this.repositoryLookupModel = repositoryLookupModelProvider;
    }

    /**
     * Creates a PCM usage model from the passed LoopBranchModels.
     *
     * @return the created PCM usage model
     */
    public UsageModel createUsageModel() {

        final UsageModel usageModel = UsageModelFactory.createUsageModel();

        // Creates for each detected user group its own usage scenario
        for (int i = 0; i < this.loopBranchModels.size(); i++) {
            final BranchModel callBranchModel = this.loopBranchModels.get(i);
            final UsageScenario usageScenario = UsageModelFactory
                    .createUsageScenario("Usage Scneario of user group " + i, usageModel);
            final Map<Integer, ScenarioBehaviour> branchScenarioBehaviours = new HashMap<>();
            this.branchScenarioBehavioursOfUserGroups.add(branchScenarioBehaviours);

            if (this.isClosedWorkloadRequested) {
                UsageModelFactory.createClosedWorkload(
                        callBranchModel.getWorkloadIntensity().getAvgNumberOfConcurrentUsers(), this.thinkTime,
                        usageScenario);
            } else {
                UsageModelFactory.createOpenWorkload(
                        callBranchModel.getWorkloadIntensity().getInterarrivalTimeOfUserSessions(), usageScenario);
            }
            // creates for each Branch its own scenario behavior
            this.createForEachBranchAScenarioBehavior(callBranchModel.getRootBranch(), i);

            // The rootBranch contains every succeeding branches and is set as the scenario behavior
            // of the user group´s usage scenario
            usageScenario.setScenarioBehaviour_UsageScenario(this.branchScenarioBehavioursOfUserGroups.get(i)
                    .get(callBranchModel.getRootBranch().getBranchId()));
            usageModel.getUsageScenario_UsageModel().add(usageScenario);
        }

        return usageModel;
    }

    /**
     * Creates a scenario behavior according to the passed branch. Loops over all child branches
     *
     * @param branch
     *            whose behavior is created
     * @param indexOfUserGroup
     *            states to which user group the passed branch belongs
     */
    private void createForEachBranchAScenarioBehavior(final Branch branch, final int indexOfUserGroup) {
        for (int i = 0; i < branch.getChildBranches().size(); i++) {
            this.createForEachBranchAScenarioBehavior(branch.getChildBranches().get(i), indexOfUserGroup);
        }
        this.modelBranchScenarioBehavior(branch, indexOfUserGroup);
    }

    /**
     * Creates for the passed branch a scenario behavior.
     *
     * @param branch
     *            whose behavior is created
     * @param indexOfUserGroup
     *            states to which user group the passed branch belongs
     */
    private void modelBranchScenarioBehavior(final Branch branch, final int indexOfUserGroup) {
        final ScenarioBehaviour scenarioBehaviour = this.transformSequenceToScenarioBehavior(indexOfUserGroup,
                branch.getBranchSequence(), branch);
        this.branchScenarioBehavioursOfUserGroups.get(indexOfUserGroup).put(branch.getBranchId(), scenarioBehaviour);
    }

    /**
     * Creates a scenario behavior corresponding to the passed sequence. The sequence can be a
     * branch sequence or a loop sequence. It creates a sequence of entry level system calls
     * including loops within the sequence. It sets the successors and predecessors.
     *
     * @param indexOfScenario
     *            states the index of the created scenario within the usage scenario
     * @param sequence
     *            that is transformed to a scenario behavior
     * @param branch
     *            contains the child branches of the branch whose behavior is created
     * @return the created scenario behavior
     */
    private ScenarioBehaviour transformSequenceToScenarioBehavior(final int indexOfScenario, // NOCS
            final List<ISequenceElement> sequence, final Branch branch) {

        final ScenarioBehaviour scenarioBehaviour = UsageModelFactory.createScenarioBehaviour();

        final Start start = UsageModelFactory.createAddStartAction("", scenarioBehaviour);
        final Stop stop = UsageModelFactory.createAddStopAction("", scenarioBehaviour);

        EntryLevelSystemCall lastESysCall = UsageModelFactory.createEmptyEntryLevelSystemCall();
        boolean isLastElementACall = false;
        Loop lastLoop = UsageModelFactory.createEmptyLoop();
        boolean isLastElementALoop = false;
        org.palladiosimulator.pcm.usagemodel.Branch lastLoopBranch = UsagemodelFactory.eINSTANCE.createBranch();
        boolean isLastElementALoopBranch = false;
        org.palladiosimulator.pcm.usagemodel.Branch lastBranch = UsagemodelFactory.eINSTANCE.createBranch();
        boolean isLastElementABranch = false;

        /**
         * Loops over all elements of the sequence and creates a corresponding scenario behavior by
         * connecting the elements via successor and predecessor connections.
         */
        for (final ISequenceElement branchElement : sequence) {

            // Element is a entryLevelSystemCall
            if (branchElement.getClass().equals(CallElement.class)) {
                EntryLevelSystemCall eSysCall = null;

                // Workaround TODO
                final String operationSignature = branchElement.getOperationSignature().replaceAll("\\(.*\\)", "()");
                final String[] operationSplit = operationSignature.split(" ");

                final Optional<Correspondent> optionCorrespondent = this.correspondenceModel
                        .getCorrespondent(branchElement.getClassSignature(), operationSplit[operationSplit.length - 1]);
                if (optionCorrespondent.isPresent()) {
                    final Correspondent correspondent = optionCorrespondent.get();
                    if (PcmUsageModelBuilder.LOGGER.isDebugEnabled()) {
                        PcmUsageModelBuilder.LOGGER.debug("Usage: Found Correspondent: {}",
                                correspondent.getPcmEntityName() + " " + correspondent.getPcmOperationName());
                    }
                    eSysCall = UsageModelFactory.createEntryLevelSystemCall(this.repositoryLookupModel, correspondent);
                }
                if (eSysCall != null) {
                    if (isLastElementACall) {
                        UsageModelFactory.connect(lastESysCall, eSysCall);
                    } else if (isLastElementALoop) {
                        UsageModelFactory.connect(lastLoop, eSysCall);
                    } else if (isLastElementALoopBranch) {
                        UsageModelFactory.connect(lastLoopBranch, eSysCall);
                    } else if (isLastElementABranch) {
                        UsageModelFactory.connect(lastBranch, eSysCall);
                    } else {
                        UsageModelFactory.connect(start, eSysCall);
                    }
                    UsageModelFactory.addUserAction(scenarioBehaviour, eSysCall);
                    lastESysCall = eSysCall;
                    isLastElementACall = true;
                    isLastElementALoop = false;
                    isLastElementALoopBranch = false;
                    isLastElementABranch = false;
                }
            } else if (branchElement.getClass().equals(LoopElement.class)) { // Element is a loop
                final Loop loop = this.createLoop(scenarioBehaviour, (LoopElement) branchElement);
                if (isLastElementACall) {
                    UsageModelFactory.connect(lastESysCall, loop);
                } else if (isLastElementALoop) {
                    UsageModelFactory.connect(lastLoop, loop);
                } else if (isLastElementALoopBranch) {
                    UsageModelFactory.connect(lastLoopBranch, loop);
                } else if (isLastElementABranch) {
                    UsageModelFactory.connect(lastBranch, loop);
                } else {
                    UsageModelFactory.connect(start, loop);
                }
                lastLoop = loop;
                isLastElementALoop = true;
                isLastElementACall = false;
                isLastElementALoopBranch = false;
                isLastElementABranch = false;
            } else if (branchElement.getClass().equals(LoopBranchElement.class)) { // Element is a
                                                                                   // looped Branch
                final org.palladiosimulator.pcm.usagemodel.Branch loopBranch = this.createLoopBranch(scenarioBehaviour,
                        (LoopBranchElement) branchElement);
                if (isLastElementACall) {
                    UsageModelFactory.connect(lastESysCall, loopBranch);
                } else if (isLastElementALoop) {
                    UsageModelFactory.connect(lastLoop, loopBranch);
                } else if (isLastElementALoopBranch) {
                    UsageModelFactory.connect(lastLoopBranch, loopBranch);
                } else if (isLastElementABranch) {
                    UsageModelFactory.connect(lastBranch, loopBranch);
                } else {
                    UsageModelFactory.connect(start, loopBranch);
                }
                lastLoopBranch = loopBranch;
                isLastElementALoopBranch = true;
                isLastElementACall = false;
                isLastElementALoop = false;
                isLastElementABranch = false;
            } else if (branchElement.getClass().equals(BranchElement.class)) { // Element is a
                                                                               // Branch
                final org.palladiosimulator.pcm.usagemodel.Branch branchInter = this.createBranch(scenarioBehaviour,
                        (BranchElement) branchElement);
                if (isLastElementACall) {
                    UsageModelFactory.connect(lastESysCall, branchInter);
                } else if (isLastElementALoop) {
                    UsageModelFactory.connect(lastLoop, branchInter);
                } else if (isLastElementALoopBranch) {
                    UsageModelFactory.connect(lastLoopBranch, branchInter);
                } else if (isLastElementABranch) {
                    UsageModelFactory.connect(lastBranch, branchInter);
                } else {
                    UsageModelFactory.connect(start, branchInter);
                }
                lastBranch = branchInter;
                isLastElementABranch = true;
                isLastElementALoopBranch = false;
                isLastElementACall = false;
                isLastElementALoop = false;
            } else {
                break;
            }
        }

        // checks if the branch got child branches
        if (branch == null) {
            if (isLastElementACall) {
                UsageModelFactory.connect(lastESysCall, stop);
            } else if (isLastElementALoop) {
                UsageModelFactory.connect(lastLoop, stop);
            } else if (isLastElementALoopBranch) {
                UsageModelFactory.connect(lastLoopBranch, stop);
            } else if (isLastElementABranch) {
                UsageModelFactory.connect(lastBranch, stop);
            } else {
                UsageModelFactory.connect(start, stop);
            }
        } else {
            final org.palladiosimulator.pcm.usagemodel.Branch branchUM = this.createChildBranch(scenarioBehaviour,
                    indexOfScenario, branch);
            if (branchUM != null) {
                if (isLastElementACall) {
                    UsageModelFactory.connect(lastESysCall, branchUM);
                    UsageModelFactory.connect(branchUM, stop);
                } else if (isLastElementALoop) {
                    UsageModelFactory.connect(lastLoop, branchUM);
                    UsageModelFactory.connect(branchUM, stop);
                } else if (isLastElementALoopBranch) {
                    UsageModelFactory.connect(lastLoopBranch, branchUM);
                    UsageModelFactory.connect(branchUM, stop);
                } else if (isLastElementABranch) {
                    UsageModelFactory.connect(lastBranch, branchUM);
                    UsageModelFactory.connect(branchUM, stop);
                } else {
                    UsageModelFactory.connect(start, branchUM);
                    UsageModelFactory.connect(branchUM, stop);
                }
            } else {
                if (isLastElementACall) {
                    UsageModelFactory.connect(lastESysCall, stop);
                } else if (isLastElementALoop) {
                    UsageModelFactory.connect(lastLoop, stop);
                } else if (isLastElementALoopBranch) {
                    UsageModelFactory.connect(lastLoopBranch, stop);
                } else if (isLastElementABranch) {
                    UsageModelFactory.connect(lastBranch, stop);
                } else {
                    UsageModelFactory.connect(start, stop);
                }
            }
        }

        return scenarioBehaviour;
    }

    /**
     * Creates for a branch a corresponding PCM branch including a corresponding usage scenario.
     *
     * @param scenarioBehaviour
     *            to that the PCM branch is added
     * @param branchElement
     *            that is transformed to a PCM branch element
     * @return a PCM branch element
     */
    private org.palladiosimulator.pcm.usagemodel.Branch createBranch(final ScenarioBehaviour scenarioBehaviour,
            final BranchElement branchElement) {
        final org.palladiosimulator.pcm.usagemodel.Branch branchUM = UsageModelFactory.createBranch("",
                scenarioBehaviour);
        for (final BranchTransitionElement transition : branchElement.getBranchTransitions()) {
            final BranchTransition branchTransition = UsageModelFactory.createBranchTransition(branchUM);
            final ScenarioBehaviour branchScenarioBehaviour = this.transformSequenceToScenarioBehavior(0,
                    transition.getBranchSequence(), null);
            branchTransition.setBranchedBehaviour_BranchTransition(branchScenarioBehaviour);
            branchTransition.setBranch_BranchTransition(branchUM);
            branchTransition.setBranchProbability(transition.getTransitionLikelihood());
        }

        return branchUM;
    }

    /**
     * Creates for a loop element a corresponding PCM loop including a corresponding usage scenario.
     *
     * @param scenarioBehaviour
     *            to that the PCM loop is added
     * @param loopElement
     *            that is transformed to a PCM loop
     * @return a PCM loop
     */
    private Loop createLoop(final ScenarioBehaviour scenarioBehaviour, final LoopElement loopElement) {

        final Loop loop = UsageModelFactory.createLoop("", scenarioBehaviour);
        final ScenarioBehaviour loopScenarioBehaviour = this.transformSequenceToScenarioBehavior(0,
                loopElement.getLoopSequence(), null);
        loop.setBodyBehaviour_Loop(loopScenarioBehaviour); // Set behavior of the loop
        final PCMRandomVariable pcmLoopIteration = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmLoopIteration.setSpecification(String.valueOf(loopElement.getLoopCount()));
        loop.setLoopIteration_Loop(pcmLoopIteration); // Set number of loops

        return loop;
    }

    /**
     * Creates a PCM branch corresponding to the child branches of the passed branch.
     *
     * @param scenarioBehaviour
     *            to that the PCM branch is added
     * @param indexOfScenario
     *            states the index of the scenario within the usage scenario
     * @param branch
     *            whose child branches are transformed to a PCM branch
     * @return a PCM branch
     */
    private org.palladiosimulator.pcm.usagemodel.Branch createChildBranch(final ScenarioBehaviour scenarioBehaviour,
            final int indexOfScenario, final Branch branch) {

        if (branch.getChildBranches().size() > 0) {
            final org.palladiosimulator.pcm.usagemodel.Branch branchUM = UsageModelFactory.createBranch("",
                    scenarioBehaviour);
            for (final Branch childBranch : branch.getChildBranches()) {
                final BranchTransition branchTransition = UsageModelFactory.createBranchTransition(branchUM);
                branchTransition.setBranchedBehaviour_BranchTransition(
                        this.branchScenarioBehavioursOfUserGroups.get(indexOfScenario).get(childBranch.getBranchId()));
                branchTransition.setBranch_BranchTransition(branchUM);
                branchTransition.setBranchProbability(childBranch.getBranchLikelihood());
            }
            return branchUM;
        }

        return null;
    }

    /**
     * Creates a PCM branch for a LoopBranchElement.
     *
     * @param scenarioBehaviour
     *            to that the PCM branch is added
     * @param loopBranch
     *            that is transformed to a PCM branch
     * @return a PCM branch
     */
    private org.palladiosimulator.pcm.usagemodel.Branch createLoopBranch(final ScenarioBehaviour scenarioBehaviour,
            final LoopBranchElement loopBranch) {

        final org.palladiosimulator.pcm.usagemodel.Branch branchUM = UsageModelFactory.createBranch("",
                scenarioBehaviour);
        for (final Branch branch : loopBranch.getLoopBranches()) {
            final BranchTransition branchTransition = UsageModelFactory.createBranchTransition(branchUM);
            final ScenarioBehaviour branchScenarioBehaviour = this.transformSequenceToScenarioBehavior(0,
                    branch.getBranchSequence(), null);
            branchTransition.setBranchedBehaviour_BranchTransition(branchScenarioBehaviour);
            branchTransition.setBranch_BranchTransition(branchUM);
            branchTransition.setBranchProbability(branch.getBranchLikelihood());
        }

        return branchUM;
    }

}
