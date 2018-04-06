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
package org.iobserve.adaptation.data;

import org.iobserve.adaptation.data.graph.ComponentNode;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DereplicateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.SystemadaptationFactory;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.system.System;

/**
 * This class provides a factory for system adaption Actions. It provides all basic functions.
 * Initialize the static fields {@value runtimeModels} and {@value redeploymentModels} before using
 * this class.
 *
 * @author Philipp Weimann
 * @author Lars Bluemke (Refactoring of system adaptation model: terminology: "(de-)allocate" ->
 *         "(de-)replicate", changes to sources and targets of actions, addition of new attributes)
 */
public final class AssemblyContextActionFactory {

    /**
     * Empty default constructor.
     */
    private AssemblyContextActionFactory() {
        // empty private factory
    }

    /**
     * Initializes the targetAllocationContext, targetProvidingAllocationContexts and
     * targetRequiringAllocationContexts of the abstract {@link AssemblyContextAction} superclass.
     *
     * @param action
     *            the action
     * @param targetNode
     *            the action's target node (may be a runtime or a redeployment node)
     * @param targetAllocation
     *            the target allocation model
     * @param targetSystem
     *            the target system model
     */
    private static void initializeAssemblyContextAction(final AssemblyContextAction action,
            final ComponentNode targetNode, final Allocation targetAllocation, final System targetSystem) {

        action.setTargetAllocationContext(
                ActionFactory.getAllocationContext(targetNode.getAllocationContextID(), targetAllocation));

        // target providing allocation contexts
        ActionFactory.getProvidingAssemblyContexts(targetNode.getAssemblyContextID(), targetSystem)
                .forEach(ac -> action.getTargetProvidingAllocationContexts().add(
                        ActionFactory.getAllocationContextContainingAssemblyContext(ac.getId(), targetAllocation)));

        // target requiring allocation contexts
        ActionFactory.getRequiringAssemblyContexts(targetNode.getAssemblyContextID(), targetSystem)
                .forEach(ac -> action.getTargetRequiringAllocationContexts().add(
                        ActionFactory.getAllocationContextContainingAssemblyContext(ac.getId(), targetAllocation)));
    }

    /**
     * Create an replication action.
     *
     * @param runtimeNode
     *            node to be replicated
     * @param reDeploymentNode
     *            target node of replication
     * @return returns the replication action
     */
    public static ReplicateAction generateReplicateAction(final ComponentNode runtimeNode,
            final ComponentNode reDeploymentNode) {
        final ReplicateAction action = SystemadaptationFactory.eINSTANCE.createReplicateAction();
        final Allocation runtimeAllocation = ActionFactory.getRuntimeModels().getAllocationModel();
        final Allocation redeploymentAllocation = ActionFactory.getRedeploymentModels().getAllocationModel();
        final System redeploymentSystem = ActionFactory.getRedeploymentModels().getSystemModel();

        AssemblyContextActionFactory.initializeAssemblyContextAction(action, reDeploymentNode, redeploymentAllocation,
                redeploymentSystem);

        action.setSourceAllocationContext(
                ActionFactory.getAllocationContext(runtimeNode.getAllocationContextID(), runtimeAllocation));

        return action;
    }

    /**
     * Create a dereplication action.
     *
     * @param runtimeNode
     *            node to be deallocated
     * @return returns the dereplication action
     */
    public static DereplicateAction generateDereplicateAction(final ComponentNode runtimeNode) {
        final DereplicateAction action = SystemadaptationFactory.eINSTANCE.createDereplicateAction();
        final Allocation runtimeAllocation = ActionFactory.getRuntimeModels().getAllocationModel();
        final System runtimeSystem = ActionFactory.getRuntimeModels().getSystemModel();

        AssemblyContextActionFactory.initializeAssemblyContextAction(action, runtimeNode, runtimeAllocation,
                runtimeSystem);

        return action;
    }

    /**
     * Create a migration action.
     *
     * @param runtimeNode
     *            source node
     * @param reDeploymentNode
     *            target node
     * @return returns the migration action
     */
    public static MigrateAction generateMigrateAction(final ComponentNode runtimeNode,
            final ComponentNode reDeploymentNode) {
        final MigrateAction action = SystemadaptationFactory.eINSTANCE.createMigrateAction();
        final Allocation runtimeAllocation = ActionFactory.getRuntimeModels().getAllocationModel();
        final Allocation redeploymentAllocation = ActionFactory.getRedeploymentModels().getAllocationModel();
        final System runtimeSystem = ActionFactory.getRuntimeModels().getSystemModel();
        final System redeploymentSystem = ActionFactory.getRedeploymentModels().getSystemModel();

        AssemblyContextActionFactory.initializeAssemblyContextAction(action, reDeploymentNode, redeploymentAllocation,
                redeploymentSystem);

        action.setSourceAllocationContext(
                ActionFactory.getAllocationContext(runtimeNode.getAllocationContextID(), runtimeAllocation));

        // source providing allocation contexts
        ActionFactory.getProvidingAssemblyContexts(runtimeNode.getAssemblyContextID(), runtimeSystem)
                .forEach(ac -> action.getTargetProvidingAllocationContexts().add(
                        ActionFactory.getAllocationContextContainingAssemblyContext(ac.getId(), runtimeAllocation)));

        // source requiring allocation contexts
        ActionFactory.getRequiringAssemblyContexts(runtimeNode.getAssemblyContextID(), runtimeSystem)
                .forEach(ac -> action.getTargetRequiringAllocationContexts().add(
                        ActionFactory.getAllocationContextContainingAssemblyContext(ac.getId(), runtimeAllocation)));

        return action;
    }

    /**
     * Create a change repository component action.
     *
     * @param runtimeNode
     *            node to be changed
     * @param reDeploymentNode
     *            target node
     * @return returns the composed action
     */
    public static ChangeRepositoryComponentAction generateChangeRepositoryComponentAction(
            final ComponentNode runtimeNode, final ComponentNode reDeploymentNode) {
        final ChangeRepositoryComponentAction action = SystemadaptationFactory.eINSTANCE
                .createChangeRepositoryComponentAction();
        final Allocation runtimeAllocation = ActionFactory.getRuntimeModels().getAllocationModel();
        final Allocation redeploymentAllocation = ActionFactory.getRedeploymentModels().getAllocationModel();
        final System redeploymentSystem = ActionFactory.getRedeploymentModels().getSystemModel();

        AssemblyContextActionFactory.initializeAssemblyContextAction(action, reDeploymentNode, redeploymentAllocation,
                redeploymentSystem);

        action.setSourceAllocationContext(
                ActionFactory.getAllocationContext(runtimeNode.getAllocationContextID(), runtimeAllocation));

        return action;
    }

}
