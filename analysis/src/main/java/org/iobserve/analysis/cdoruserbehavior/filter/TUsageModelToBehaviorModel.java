/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

package org.iobserve.analysis.cdoruserbehavior.filter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.iobserve.analysis.cdoruserbehavior.filter.models.BehaviorModel;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallEdge;
import org.iobserve.analysis.cdoruserbehavior.filter.models.EntryCallNode;
import org.iobserve.analysis.cdoruserbehavior.util.SingleOrNoneCollector;
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

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Transforms an entryCallSequenceModel to an UBM UI compatible JSON and sends it to the UBM UI.
 *
 * @author Christoph Dornieden
 *
 */
public class TUsageModelToBehaviorModel extends AbstractConsumerStage<UsageModel> {

    private final OutputPort<BehaviorModel> outputPort;

    /**
     * constructor
     */
    public TUsageModelToBehaviorModel() {
        this.outputPort = this.createOutputPort();
    }

    /**
     * getter
     *
     * @return outputPort
     */
    public OutputPort<BehaviorModel> getOutputPort() {
        return this.outputPort;
    }

    @Override
    protected void execute(final UsageModel element) {

        element.getUsageScenario_UsageModel().stream().map(UsageScenario::getScenarioBehaviour_UsageScenario)
                .map(this::createBehaviorModel).filter(Optional::isPresent).map(Optional::get)
                .forEach(this.outputPort::send);

    }

    /**
     * send user behavior to ui
     *
     * @param behavior
     *            user behavior
     *
     * @return behaviorModel
     */
    private Optional<BehaviorModel> createBehaviorModel(final ScenarioBehaviour behavior) {

        if (!behavior.getActions_ScenarioBehaviour().isEmpty()) {
            final BehaviorModel behaviorModel = new BehaviorModel();
            this.traverseScenarioBehavior(behavior, behaviorModel, Optional.empty());
            behaviorModel.setName("mpe-behavior");
            return Optional.of(behaviorModel);
        } else {

            return Optional.empty();
        }
    }

    private Map<EntryCallNode, Double> traverseScenarioBehavior(final ScenarioBehaviour behavior,
            final BehaviorModel behaviorModel, final Optional<Map<EntryCallNode, Double>> optPreviousNodes) {

        // get start node
        final Optional<Start> startAction = behavior.getActions_ScenarioBehaviour().stream()
                .filter(Start.class::isInstance).map(Start.class::cast).collect(new SingleOrNoneCollector<>());

        if (startAction.isPresent()) {
            return this.traverseAction(behaviorModel, optPreviousNodes, startAction.get().getSuccessor());
        }
        return new HashMap<>();
    }

    private Map<EntryCallNode, Double> traverseAction(final BehaviorModel behaviorModel,
            final Optional<Map<EntryCallNode, Double>> optPreviousNodes, final AbstractUserAction action) {

        if (action instanceof Branch) {
            final Branch branch = (Branch) action;
            return this.traverseBranch(behaviorModel, optPreviousNodes, branch);

        } else if (action instanceof Loop) {
            final Loop loop = (Loop) action;
            return this.traverseLoop(behaviorModel, optPreviousNodes, loop);

        } else if (action instanceof EntryLevelSystemCall) {
            final Map<EntryCallNode, Double> endNodes = new HashMap<>();

            final EntryLevelSystemCall entryLevelSystemCall = (EntryLevelSystemCall) action;
            final EntryCallNode entryCallNode = this.createEntryCallNode(entryLevelSystemCall);
            behaviorModel.addNode(entryCallNode);

            if (optPreviousNodes.isPresent()) {
                optPreviousNodes.get().keySet().stream().map(previousNode -> new EntryCallEdge(previousNode,
                        entryCallNode, optPreviousNodes.get().get(previousNode))).forEach(behaviorModel::addEdge);
            }
            endNodes.put(entryCallNode, 1.0);
            return this.traverseAction(behaviorModel, Optional.of(endNodes), action.getSuccessor());

        } else if (action instanceof Stop) {
            return optPreviousNodes.isPresent() ? optPreviousNodes.get() : new HashMap<>();
        } else { // skip action
            return this.traverseAction(behaviorModel, optPreviousNodes, action.getSuccessor());
        }

    }

    private Map<EntryCallNode, Double> traverseBranch(final BehaviorModel behaviorModel,
            final Optional<Map<EntryCallNode, Double>> optPreviousNodes, final Branch branch) {

        // assign new probabilities to the nodes
        final Map<BranchTransition, Map<EntryCallNode, Double>> transitionMap = new HashMap<>();
        for (final BranchTransition transition : branch.getBranchTransitions_Branch()) {

            final Map<EntryCallNode, Double> branchMap = new HashMap<>();
            optPreviousNodes.ifPresent(previousNodes -> previousNodes.keySet().stream()
                    .forEach(node -> branchMap.put(node, transition.getBranchProbability())));

            transitionMap.put(transition, branchMap);
        }

        // traverse all branches and collect the end nodes
        final Map<EntryCallNode, Double> endNodes = branch.getBranchTransitions_Branch().stream()
                .map(transition -> this.traverseScenarioBehavior(transition.getBranchedBehaviour_BranchTransition(),
                        behaviorModel, Optional.of(transitionMap.get(transition)))) // all
                                                                                    // branches
                .collect(HashMap::new, Map::putAll, Map::putAll); // collect endNodes
        return endNodes;
    }

    private Map<EntryCallNode, Double> traverseLoop(final BehaviorModel behaviorModel,
            final Optional<Map<EntryCallNode, Double>> optPreviousNodes, final Loop loop) {

        final Map<EntryCallNode, Double> loopEnds = this.traverseScenarioBehavior(loop.getBodyBehaviour_Loop(),
                behaviorModel, optPreviousNodes);

        final Optional<EntryCallNode> loopStart = this.findLoopStart(loop);

        if (loopStart.isPresent()) {
            final Map<EntryCallNode, Double> endNodes = new HashMap<>();
            final Optional<EntryCallNode> entryCallNode = behaviorModel.findNode(loopStart.get());
            loopEnds.keySet().stream()
                    .map(loopEnd -> new EntryCallEdge(loopEnd, entryCallNode.get(), loopEnds.get(loopEnd)))
                    .forEach(behaviorModel::addEdge);
            endNodes.put(entryCallNode.get(), 1.0);
            return this.traverseAction(behaviorModel, Optional.of(endNodes), loop.getSuccessor());
        }

        return this.traverseAction(behaviorModel, optPreviousNodes, loop.getSuccessor());
    }

    /**
     * Find first {@link EntryLevelSystemCall} element of a {@link Loop} body
     *
     * @param loop
     *            loop
     * @return first found {@link EntryLevelSystemCall}, if found
     */
    private Optional<EntryCallNode> findLoopStart(final AbstractUserAction action) {
        if (action instanceof Stop) { // LoopStart not found
            return Optional.empty();

        } else if (action instanceof EntryLevelSystemCall) { // found loop start
            final EntryLevelSystemCall entryLevelSystemCall = (EntryLevelSystemCall) action;
            final EntryCallNode entryCallNode = this.createEntryCallNode(entryLevelSystemCall);
            return Optional.of(entryCallNode);

        } else if (action instanceof Loop) { // search nested scenario
            final Loop loop = (Loop) action;
            final List<AbstractUserAction> userActions = loop.getBodyBehaviour_Loop().getActions_ScenarioBehaviour();

            if (userActions.size() > 0) {
                return this.findLoopStart(userActions.get(0));
            } else {
                return Optional.empty();
            }

        } else if (action instanceof Branch) { // not possible in theory, a ENtryCallSystemCall
                                               // will always found before
            return Optional.empty();

        } else { // next action
            return this.findLoopStart(action.getSuccessor());
        }

    }

    /**
     * creates an {@link EntryCallNode} from an {@link EntryLevelSystemCall}
     *
     * @param entryLevelSystemCall
     *            {@link EntryLevelSystemCall}
     * @return entryCallNode
     */
    private EntryCallNode createEntryCallNode(final EntryLevelSystemCall entryLevelSystemCall) {
        final String signature = entryLevelSystemCall.getOperationSignature__EntryLevelSystemCall().getEntityName();

        final EntryCallNode entryCallNode = new EntryCallNode(signature);
        return entryCallNode;
    }

}
