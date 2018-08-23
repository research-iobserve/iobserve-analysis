/***************************************************************************
 * Copyright (C) 2014 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.model.factory;

import org.iobserve.model.provider.deprecated.RepositoryLookupModelProvider;
import org.palladiosimulator.pcm.core.CoreFactory;
import org.palladiosimulator.pcm.core.PCMRandomVariable;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationProvidedRole;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.usagemodel.AbstractUserAction;
import org.palladiosimulator.pcm.usagemodel.Branch;
import org.palladiosimulator.pcm.usagemodel.BranchTransition;
import org.palladiosimulator.pcm.usagemodel.ClosedWorkload;
import org.palladiosimulator.pcm.usagemodel.EntryLevelSystemCall;
import org.palladiosimulator.pcm.usagemodel.Loop;
import org.palladiosimulator.pcm.usagemodel.OpenWorkload;
import org.palladiosimulator.pcm.usagemodel.ScenarioBehaviour;
import org.palladiosimulator.pcm.usagemodel.Start;
import org.palladiosimulator.pcm.usagemodel.Stop;
import org.palladiosimulator.pcm.usagemodel.UsageModel;
import org.palladiosimulator.pcm.usagemodel.UsageScenario;
import org.palladiosimulator.pcm.usagemodel.UsagemodelFactory;

/**
 * UsageModelBuilder is able to build a {@link UsageModel}.
 *
 * @author Robert Heinrich
 * @author Nicolas Boltz
 * @author Alessandro Giusa
 * @author Reiner Jung
 *
 */
public final class UsageModelFactory {

    /**
     * Create a usage model builder.
     *
     * @param modelProvider
     *            model provider
     */
    private UsageModelFactory() {
    }

    /**
     * Create a new usage model.
     *
     * @return new usage model
     */
    public static UsageModel createUsageModel() {
        return UsagemodelFactory.eINSTANCE.createUsageModel();
    }

    /**
     * Convenience routine to create and add stop actions.
     *
     * @param name
     *            the name of the action
     * @param behavior
     *            the scenario behavior
     * @return the resulting stop action
     */
    public static Stop createAddStopAction(final String name, final ScenarioBehaviour behavior) {
        final Stop stop = UsageModelFactory.createStop(name);
        UsageModelFactory.addUserAction(behavior, stop);

        return stop;
    }

    /**
     * Convenience routine to create and add start actions.
     *
     * @param name
     *            the name of the action
     * @param behavior
     *            the scenario behavior
     * @return the resulting stop action
     */
    public static Start createAddStartAction(final String name, final ScenarioBehaviour behavior) {
        final Start start = UsageModelFactory.createStart(name);
        UsageModelFactory.addUserAction(behavior, start);

        return start;
    }

    /**
     * Create new usage scenario and add it to the passed usage model.
     *
     * @param name
     *            of the usage scenario
     * @param usageModel
     *            the usage scenario is added to
     * @return created usage scenario
     */
    public static UsageScenario createUsageScenario(final String name, final UsageModel usageModel) {
        // create the usage scenario
        final UsageScenario usageScenario = UsagemodelFactory.eINSTANCE.createUsageScenario();
        usageScenario.setEntityName(name);
        usageScenario.setUsageModel_UsageScenario(usageModel);
        usageModel.getUsageScenario_UsageModel().add(usageScenario);

        // create a scenario behavior
        final ScenarioBehaviour scenarioBehaviour = UsageModelFactory.createScenarioBehaviour();
        usageScenario.setScenarioBehaviour_UsageScenario(scenarioBehaviour);

        return usageScenario;
    }

    /**
     * Create a {@link ScenarioBehaviour}. The behavior is just created not added any model part.
     *
     * @return the behavior
     */
    public static ScenarioBehaviour createScenarioBehaviour() {
        return UsagemodelFactory.eINSTANCE.createScenarioBehaviour();
    }

    /**
     * Create an {@link OpenWorkload} and add it to the given {@link UsageScenario}.
     *
     * @param avgInterarrivalTime
     *            the interarrival time
     * @param parent
     *            usage scenario the workload should be added to
     * @return brand new instance of {@link OpenWorkload}
     */
    public static OpenWorkload createOpenWorkload(final long avgInterarrivalTime, final UsageScenario parent) {
        final OpenWorkload openWorkload = UsagemodelFactory.eINSTANCE.createOpenWorkload();
        parent.setWorkload_UsageScenario(openWorkload);

        // create variables
        final PCMRandomVariable pcmInterarrivalTime = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmInterarrivalTime.setSpecification(String.valueOf(avgInterarrivalTime));
        pcmInterarrivalTime.setOpenWorkload_PCMRandomVariable(openWorkload);
        openWorkload.setInterArrivalTime_OpenWorkload(pcmInterarrivalTime);

        return openWorkload;
    }

    /**
     * Create an {@link OpenWorkload} and add it to the given {@link UsageScenario}.
     *
     * @param population
     *            population
     * @param thinkTime
     *            thinkTime
     * @param parent
     *            usage scenario the workload should be added to
     * @return created closed workload instance
     */
    public static ClosedWorkload createClosedWorkload(final int population, final double thinkTime,
            final UsageScenario parent) {
        final ClosedWorkload closedWorkload = UsagemodelFactory.eINSTANCE.createClosedWorkload();
        parent.setWorkload_UsageScenario(closedWorkload);

        // create variables
        final PCMRandomVariable pcmThinkTime = CoreFactory.eINSTANCE.createPCMRandomVariable();
        pcmThinkTime.setSpecification(String.valueOf(thinkTime));
        pcmThinkTime.setClosedWorkload_PCMRandomVariable(closedWorkload);
        closedWorkload.setPopulation(population);
        closedWorkload.setThinkTime_ClosedWorkload(pcmThinkTime);
        return closedWorkload;
    }

    /**
     * Create a start node.
     *
     * @param name
     *            of start node
     * @return start node
     */
    public static Start createStart(final String name) {
        final Start start = UsagemodelFactory.eINSTANCE.createStart();
        start.setEntityName(name);
        return start;
    }

    /**
     * Create a start node without name. (Recommended to use {@link #createStart(String)}).
     *
     * @return start node
     */
    public static Start createStart() {
        return UsageModelFactory.createStart("start-with-no-name");
    }

    /**
     * Create a stop node.
     *
     * @param name
     *            name of stop node
     * @return stop node.
     */
    public static Stop createStop(final String name) {
        final Stop stop = UsagemodelFactory.eINSTANCE.createStop();
        stop.setEntityName(name);
        return stop;
    }

    /**
     * Create a stop node without name. (Recommended to use {@link #createStop(String)}).
     *
     * @return stop node.
     */
    public static Stop createStop() {
        return UsageModelFactory.createStop("stop-with-no-name");
    }

    /**
     * Create an EntryLevelSystemCall with the given operation signature.
     *
     * @param repositoryLookupModel
     *            provides a lookup model for the repository
     * @param operationSignature
     *            operation id of the EntryLevelSystemCall
     * @return null, if the creation failed, the instance if not.
     */
    public static EntryLevelSystemCall createEntryLevelSystemCall(
            final RepositoryLookupModelProvider repositoryLookupModel, final OperationSignature operationSignature) {

        final EntryLevelSystemCall eSysCall = UsagemodelFactory.eINSTANCE.createEntryLevelSystemCall();
        eSysCall.setEntityName(operationSignature.getEntityName());
        eSysCall.setOperationSignature__EntryLevelSystemCall(operationSignature);

        final OperationInterface opInf = operationSignature.getInterface__OperationSignature();
        final OperationProvidedRole providedRole = repositoryLookupModel.getOperationProvidedRole(opInf);
        eSysCall.setProvidedRole_EntryLevelSystemCall(providedRole);

        return eSysCall;
    }

    /**
     * Create a new empty EntryLevelSystemCall.
     *
     * @return entry level system call.
     */
    public static EntryLevelSystemCall createEmptyEntryLevelSystemCall() {
        return UsagemodelFactory.eINSTANCE.createEntryLevelSystemCall();
    }

    // *****************************************************************
    // BRANCHING
    // *****************************************************************

    /**
     * Create branch with the given name and add it to the given scenario behavior.
     *
     * @param name
     *            name of the branch
     * @param parent
     *            parent to add branch to
     * @return created branch instance
     */
    public static Branch createBranch(final String name, final ScenarioBehaviour parent) {
        final Branch branch = UsagemodelFactory.eINSTANCE.createBranch();
        branch.setEntityName(name);
        branch.setScenarioBehaviour_AbstractUserAction(parent);
        parent.getActions_ScenarioBehaviour().add(branch);
        return branch;
    }

    /**
     * Create branch with the given name and add it to the given loop.
     *
     * @param name
     *            name of the branch
     * @param parent
     *            parent to add branch to
     * @return created branch instance
     */
    public static Branch createBranch(final String name, final Loop parent) {
        return UsageModelFactory.createBranch(name, parent.getBodyBehaviour_Loop());
    }

    /**
     * Create branch with the given name and add it to the given branch transition.
     *
     * @param name
     *            name of the branch
     * @param parent
     *            parent to add branch to
     * @return created branch instance
     */
    public static Branch createBranch(final String name, final BranchTransition parent) {
        return UsageModelFactory.createBranch(name, parent.getBranchedBehaviour_BranchTransition());
    }

    /**
     * Create empty {@link BranchTransition} with the given parent. A {@link ScenarioBehaviour} is
     * added to the body of the branch transition, in order to make it possible adding further model
     * elements.
     *
     * @param parent
     *            branch to add transition to
     * @return created branch transition
     */
    public static BranchTransition createBranchTransition(final Branch parent) {
        // create branch transition
        final BranchTransition branchTransition = UsagemodelFactory.eINSTANCE.createBranchTransition();
        branchTransition.setBranch_BranchTransition(parent);
        parent.getBranchTransitions_Branch().add(branchTransition);

        // create body of branch transition
        final ScenarioBehaviour bodyScenarioBehaviour = UsageModelFactory.createScenarioBehaviour();
        branchTransition.setBranchedBehaviour_BranchTransition(bodyScenarioBehaviour);
        return branchTransition;
    }

    /**
     * Create loop in given parent {@link ScenarioBehaviour}. A {@link ScenarioBehaviour} is added
     * to the body of the loop, in order to make it possible adding further model elements.
     *
     * @param name
     *            name of loop
     * @param parent
     *            parent
     * @return created loop
     */
    public static Loop createLoop(final String name, final ScenarioBehaviour parent) {
        // create the loop
        final Loop loop = UsagemodelFactory.eINSTANCE.createLoop();
        loop.setEntityName(name);
        parent.getActions_ScenarioBehaviour().add(loop);

        // create the body scenario behavior
        final ScenarioBehaviour bodyScenarioBehaviour = UsageModelFactory.createScenarioBehaviour();
        loop.setBodyBehaviour_Loop(bodyScenarioBehaviour);
        return loop;
    }

    /**
     * Create a loop within the given loop.
     *
     * @param name
     *            name of the loop
     * @param parent
     *            parent loop
     * @return created loop
     */
    public static Loop createLoop(final String name, final Loop parent) {
        return UsageModelFactory.createLoop(name, parent.getBodyBehaviour_Loop());
    }

    /**
     * Create a loop within the given branch transition.
     *
     * @param name
     *            name of loop
     * @param parent
     *            parent branch transition
     * @return created loop
     */
    public static Loop createLoop(final String name, final BranchTransition parent) {
        return UsageModelFactory.createLoop(name, parent.getBranchedBehaviour_BranchTransition());
    }

    /**
     * Creates a new and empty Loop.
     *
     * @return loop
     */
    public static Loop createEmptyLoop() {
        return UsagemodelFactory.eINSTANCE.createLoop();
    }

    /**
     * Connect actions.
     *
     * @param predecessor
     *            predecessor of successor
     * @param successor
     *            successor of predecessor
     */
    public static void connect(final AbstractUserAction predecessor, final AbstractUserAction successor) {
        successor.setPredecessor(predecessor);
        predecessor.setSuccessor(successor);
    }

    /**
     * Add the given actions to the given {@link ScenarioBehaviour}.
     *
     * @param parent
     *            parent
     * @param actions
     *            actions to add
     */
    public static void addUserAction(final ScenarioBehaviour parent, final AbstractUserAction... actions) {
        for (final AbstractUserAction nextUserAction : actions) {
            parent.getActions_ScenarioBehaviour().add(nextUserAction);
        }
    }

    /**
     * Add the given actions to the given {@link UsageScenario}.
     *
     * @param parent
     *            parent
     * @param actions
     *            actions to add
     */
    public static void addUserAction(final UsageScenario parent, final AbstractUserAction... actions) {
        UsageModelFactory.addUserAction(parent.getScenarioBehaviour_UsageScenario(), actions);
    }

    /**
     * Add the given actions to the given {@link BranchTransition}.
     *
     * @param parent
     *            parent
     * @param actions
     *            actions to add
     */
    public static void addUserAction(final BranchTransition parent, final AbstractUserAction... actions) {
        UsageModelFactory.addUserAction(parent.getBranchedBehaviour_BranchTransition(), actions);
    }

    /**
     * Add the given actions to the given {@link Loop}.
     *
     * @param parent
     *            parent
     * @param actions
     *            actions to add
     */
    public static void addUserAction(final Loop parent, final AbstractUserAction... actions) {
        UsageModelFactory.addUserAction(parent.getBodyBehaviour_Loop(), actions);
    }

}