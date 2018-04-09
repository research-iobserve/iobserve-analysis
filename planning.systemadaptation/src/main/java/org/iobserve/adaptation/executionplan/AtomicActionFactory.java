/***************************************************************************
 * Copyright (C) 2018 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.adaptation.executionplan;

import java.util.List;

import org.iobserve.adaptation.executionplan.AllocateNodeAction;
import org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction;
import org.iobserve.adaptation.executionplan.ConnectComponentAction;
import org.iobserve.adaptation.executionplan.ConnectNodeAction;
import org.iobserve.adaptation.executionplan.DeallocateNodeAction;
import org.iobserve.adaptation.executionplan.DeployComponentAction;
import org.iobserve.adaptation.executionplan.DisconnectComponentAction;
import org.iobserve.adaptation.executionplan.DisconnectNodeAction;
import org.iobserve.adaptation.executionplan.ExecutionplanFactory;
import org.iobserve.adaptation.executionplan.FinishComponentAction;
import org.iobserve.adaptation.executionplan.MigrateComponentStateAction;
import org.iobserve.adaptation.executionplan.UndeployComponentAction;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.resourceenvironment.LinkingResource;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * Factory class for the creation of atomic adaptation actions as used in the execution plan.
 *
 * @author Lars Bluemke
 *
 */
public final class AtomicActionFactory {

    private AtomicActionFactory() {
    }

    /**
     * Generates an action for a component's deployment.
     *
     * @param targetAllocationContext
     *            Allocation context containing the target component's assembly context and resource
     *            container
     * @return The action
     */
    public static DeployComponentAction generateDeployComponentAction(final AllocationContext targetAllocationContext) {
        final DeployComponentAction deployAction = ExecutionplanFactory.eINSTANCE.createDeployComponentAction();
        deployAction.setTargetAllocationContext(targetAllocationContext);

        return deployAction;
    }

    /**
     * Generates an action for a component's undeployment.
     *
     * @param targetAllocationContext
     *            Allocation context containing the target component's assembly context and resource
     *            container
     * @return The action
     */
    public static UndeployComponentAction generateUndeployComponentAction(
            final AllocationContext targetAllocationContext) {
        final UndeployComponentAction undeployAction = ExecutionplanFactory.eINSTANCE.createUndeployComponentAction();
        undeployAction.setTargetAllocationContext(targetAllocationContext);

        return undeployAction;
    }

    /**
     * Generates an action for a component's migration of state.
     *
     * @param sourceAllocationContext
     *            Allocation context containing the source component's assembly context and resource
     * @param targetAllocationContext
     *            Allocation context containing the target component's assembly context and resource
     * @return The action
     */
    public static MigrateComponentStateAction generateMigrateComponentStateAction(
            final AllocationContext sourceAllocationContext, final AllocationContext targetAllocationContext) {
        final MigrateComponentStateAction migrateStateAction = ExecutionplanFactory.eINSTANCE
                .createMigrateComponentStateAction();
        migrateStateAction.setSourceAllocationContext(sourceAllocationContext);
        migrateStateAction.setTargetAllocationContext(targetAllocationContext);

        return migrateStateAction;
    }

    /**
     * Generates an action for connecting a component.
     *
     * @param targetAllocationContext
     *            Allocation context containing the target component's assembly context and resource
     *            container
     * @param targetProvidingAllocationContexts
     *            List of allocation contexts providing something to the target component
     * @param targetRequiringAllocationContexts
     *            List of allocation contexts requiring something from the target component
     * @return The action
     */
    public static ConnectComponentAction generateConnectComponentAction(final AllocationContext targetAllocationContext,
            final List<AllocationContext> targetProvidingAllocationContexts,
            final List<AllocationContext> targetRequiringAllocationContexts) {
        final ConnectComponentAction connectAction = ExecutionplanFactory.eINSTANCE.createConnectComponentAction();
        connectAction.setTargetAllocationContext(targetAllocationContext);
        targetProvidingAllocationContexts.forEach(ac -> connectAction.getTargetProvidingAllocationContexts().add(ac));
        targetRequiringAllocationContexts.forEach(ac -> connectAction.getTargetRequiringAllocationContexts().add(ac));

        return connectAction;
    }

    /**
     * Generates an action for disconnecting a component.
     *
     * @param targetAllocationContext
     *            Allocation context containing the target component's assembly context and resource
     *            container
     * @param targetProvidingAllocationContexts
     *            List of allocation contexts providing something to the target component
     * @param targetRequiringAllocationContexts
     *            List of allocation contexts requiring something from the target component
     * @return The action
     */
    public static DisconnectComponentAction generateDisconnectComponentAction(
            final AllocationContext targetAllocationContext,
            final List<AllocationContext> targetProvidingAllocationContexts,
            final List<AllocationContext> targetRequiringAllocationContexts) {
        final DisconnectComponentAction disconnectAction = ExecutionplanFactory.eINSTANCE
                .createDisconnectComponentAction();
        disconnectAction.setTargetAllocationContext(targetAllocationContext);
        targetProvidingAllocationContexts
                .forEach(ac -> disconnectAction.getTargetProvidingAllocationContexts().add(ac));
        targetRequiringAllocationContexts
                .forEach(ac -> disconnectAction.getTargetRequiringAllocationContexts().add(ac));

        return disconnectAction;
    }

    /**
     * Generates an action for blocking requests to a component-
     *
     * @param targetAllocationContext
     *            Allocation context containing the target component's assembly context and resource
     *            container
     * @param targetRequiringAllocationContexts
     *            List of allocation contexts requiring something from the target component
     * @return The action
     */
    public static BlockRequestsToComponentAction generateBlockRequestsToComponentAction(
            final AllocationContext targetAllocationContext,
            final List<AllocationContext> targetRequiringAllocationContexts) {
        final BlockRequestsToComponentAction blockRequestsAction = ExecutionplanFactory.eINSTANCE
                .createBlockRequestsToComponentAction();
        blockRequestsAction.setTargetAllocationContext(targetAllocationContext);
        targetRequiringAllocationContexts
                .forEach(ac -> blockRequestsAction.getTargetRequiringAllocationContexts().add(ac));

        return blockRequestsAction;
    }

    /**
     * Generates an action for waiting for a component's running tasks to finish.
     *
     * @param targetAllocationContext
     *            Allocation context containing the target component's assembly context and resource
     *            container
     * @return The action
     */
    public static FinishComponentAction generateFinishComponentAction(final AllocationContext targetAllocationContext) {
        final FinishComponentAction finishAction = ExecutionplanFactory.eINSTANCE.createFinishComponentAction();
        finishAction.setTargetAllocationContext(targetAllocationContext);

        return finishAction;
    }

    /**
     * Generates an action for a node's allocation.
     * 
     * @param targetResourceContainer
     *            Resource container to allocate
     * @return The action
     */
    public static AllocateNodeAction generateAllocateNodeAction(final ResourceContainer targetResourceContainer) {
        final AllocateNodeAction allocateAction = ExecutionplanFactory.eINSTANCE.createAllocateNodeAction();
        allocateAction.setTargetResourceContainer(targetResourceContainer);

        return allocateAction;
    }

    /**
     * Generates an action for a node's deallocation.
     * 
     * @param targetResourceContainer
     *            Resource container to deallocate
     * @return The action
     */
    public static DeallocateNodeAction generateDeallocateNodeAction(final ResourceContainer targetResourceContainer) {
        final DeallocateNodeAction deallocateAction = ExecutionplanFactory.eINSTANCE.createDeallocateNodeAction();
        deallocateAction.setTargetResourceContainer(targetResourceContainer);

        return deallocateAction;
    }

    /**
     * Generates an action for connecting a node to other nodes
     * 
     * @param targetResourceContainer
     *            Resource container to connect
     * @param targetConnectors
     *            Linking resources which have to be connected to the node
     * @return The action
     */
    public static ConnectNodeAction generateConnectNodeAction(final ResourceContainer targetResourceContainer,
            final List<LinkingResource> targetConnectors) {
        final ConnectNodeAction connectAction = ExecutionplanFactory.eINSTANCE.createConnectNodeAction();
        connectAction.setTargetResourceContainer(targetResourceContainer);
        targetConnectors.forEach(lr -> connectAction.getTargetConnectors().add(lr));

        return connectAction;
    }

    /**
     * Generates an action for disconnecting a node from other nodes
     * 
     * @param targetResourceContainer
     *            Resource container to disconnect
     * @param targetConnectors
     *            Linking resources which have to be disconnected from the node
     * @return The action
     */
    public static DisconnectNodeAction generateDisonnectNodeAction(final ResourceContainer targetResourceContainer,
            final List<LinkingResource> targetConnectors) {
        final DisconnectNodeAction disconnectAction = ExecutionplanFactory.eINSTANCE.createDisconnectNodeAction();
        disconnectAction.setTargetResourceContainer(targetResourceContainer);
        targetConnectors.forEach(lr -> disconnectAction.getTargetConnectors().add(lr));

        return disconnectAction;
    }
}
