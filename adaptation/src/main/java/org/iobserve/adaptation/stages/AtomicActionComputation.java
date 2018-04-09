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
package org.iobserve.adaptation.stages;

import teetime.stage.basic.AbstractTransformation;

import org.iobserve.adaptation.executionplan.AllocateNodeAction;
import org.iobserve.adaptation.executionplan.AtomicActionFactory;
import org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction;
import org.iobserve.adaptation.executionplan.ConnectComponentAction;
import org.iobserve.adaptation.executionplan.ConnectNodeAction;
import org.iobserve.adaptation.executionplan.DeallocateNodeAction;
import org.iobserve.adaptation.executionplan.DeployComponentAction;
import org.iobserve.adaptation.executionplan.DisconnectComponentAction;
import org.iobserve.adaptation.executionplan.DisconnectNodeAction;
import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.executionplan.ExecutionplanFactory;
import org.iobserve.adaptation.executionplan.FinishComponentAction;
import org.iobserve.adaptation.executionplan.MigrateComponentStateAction;
import org.iobserve.adaptation.executionplan.UndeployComponentAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.DereplicateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.SystemAdaptation;

/**
 * Receives a {@link SystemAdaptation} model containing a list of composed adaptation actions and
 * computes the required atomic adaptation actions - the execution plan - which is passed to the
 * output port.
 *
 * @author Lars Bluemke
 *
 */
public class AtomicActionComputation extends AbstractTransformation<SystemAdaptation, ExecutionPlan> {

    @Override
    protected void execute(final SystemAdaptation systemAdaptationModel) throws Exception {
        final ExecutionPlan executionPlan = ExecutionplanFactory.eINSTANCE.createExecutionPlan();

        for (final Action composedAction : systemAdaptationModel.getActions()) {
            if (composedAction instanceof ReplicateAction) {
                this.generateAtomicActions((ReplicateAction) composedAction, executionPlan);
            } else if (composedAction instanceof DereplicateAction) {
                this.generateAtomicActions((DereplicateAction) composedAction, executionPlan);
            } else if (composedAction instanceof MigrateAction) {
                this.generateAtomicActions((MigrateAction) composedAction, executionPlan);
            } else if (composedAction instanceof ChangeRepositoryComponentAction) {
                this.generateAtomicActions((ChangeRepositoryComponentAction) composedAction, executionPlan);
            } else if (composedAction instanceof AllocateAction) {
                this.generateAtomicActions((AllocateAction) composedAction, executionPlan);
            } else if (composedAction instanceof DeallocateAction) {
                this.generateAtomicActions((DeallocateAction) composedAction, executionPlan);
            }
        }

        this.outputPort.send(executionPlan);
    }

    private void generateAtomicActions(final ReplicateAction replicateAction, final ExecutionPlan executionPlan) {
        // Deploy new component instance
        final DeployComponentAction deployAction = AtomicActionFactory
                .generateDeployComponentAction(replicateAction.getTargetAllocationContext());

        // Migrate state
        final MigrateComponentStateAction migrateStateAction = AtomicActionFactory.generateMigrateComponentStateAction(
                replicateAction.getSourceAllocationContext(), replicateAction.getTargetAllocationContext());

        // Connect replication
        final ConnectComponentAction connectAction = AtomicActionFactory.generateConnectComponentAction(
                replicateAction.getTargetAllocationContext(), replicateAction.getTargetProvidingAllocationContexts(),
                replicateAction.getTargetRequiringAllocationContexts());

        executionPlan.getActions().add(deployAction);
        executionPlan.getActions().add(migrateStateAction);
        executionPlan.getActions().add(connectAction);
    }

    private void generateAtomicActions(final DereplicateAction dereplicateAction, final ExecutionPlan executionPlan) {
        // Block incoming requests
        final BlockRequestsToComponentAction blockRequestsAction = AtomicActionFactory
                .generateBlockRequestsToComponentAction(dereplicateAction.getTargetAllocationContext(),
                        dereplicateAction.getTargetRequiringAllocationContexts());

        // Finish running transactions
        final FinishComponentAction finishAction = AtomicActionFactory
                .generateFinishComponentAction(dereplicateAction.getTargetAllocationContext());

        // Disconnect component instance
        final DisconnectComponentAction disconnectAction = AtomicActionFactory.generateDisconnectComponentAction(
                dereplicateAction.getTargetAllocationContext(),
                dereplicateAction.getTargetProvidingAllocationContexts(),
                dereplicateAction.getTargetRequiringAllocationContexts());

        // Undeploy component instance
        final UndeployComponentAction undeployAction = AtomicActionFactory
                .generateUndeployComponentAction(dereplicateAction.getTargetAllocationContext());

        executionPlan.getActions().add(blockRequestsAction);
        executionPlan.getActions().add(finishAction);
        executionPlan.getActions().add(disconnectAction);
        executionPlan.getActions().add(undeployAction);
    }

    private void generateAtomicActions(final MigrateAction migrateAction, final ExecutionPlan executionPlan) {
        // Deploy new component instance
        final DeployComponentAction deployAction = AtomicActionFactory
                .generateDeployComponentAction(migrateAction.getTargetAllocationContext());

        // Migrate state
        final MigrateComponentStateAction migrateStateAction = AtomicActionFactory.generateMigrateComponentStateAction(
                migrateAction.getSourceAllocationContext(), migrateAction.getTargetAllocationContext());

        // Connect replication
        final ConnectComponentAction connectAction = AtomicActionFactory.generateConnectComponentAction(
                migrateAction.getTargetAllocationContext(), migrateAction.getTargetProvidingAllocationContexts(),
                migrateAction.getTargetRequiringAllocationContexts());

        // Block incoming requests
        final BlockRequestsToComponentAction blockRequestsAction = AtomicActionFactory
                .generateBlockRequestsToComponentAction(migrateAction.getTargetAllocationContext(),
                        migrateAction.getTargetRequiringAllocationContexts());

        // Finish running transactions
        final FinishComponentAction finishAction = AtomicActionFactory
                .generateFinishComponentAction(migrateAction.getTargetAllocationContext());

        // Disconnect component instance
        final DisconnectComponentAction disconnectAction = AtomicActionFactory.generateDisconnectComponentAction(
                migrateAction.getTargetAllocationContext(), migrateAction.getTargetProvidingAllocationContexts(),
                migrateAction.getTargetRequiringAllocationContexts());

        // Undeploy component instance
        final UndeployComponentAction undeployAction = AtomicActionFactory
                .generateUndeployComponentAction(migrateAction.getTargetAllocationContext());

        executionPlan.getActions().add(deployAction);
        executionPlan.getActions().add(migrateStateAction);
        executionPlan.getActions().add(connectAction);
        executionPlan.getActions().add(blockRequestsAction);
        executionPlan.getActions().add(finishAction);
        executionPlan.getActions().add(disconnectAction);
        executionPlan.getActions().add(undeployAction);
    }

    private void generateAtomicActions(final ChangeRepositoryComponentAction changeComponentAction,
            final ExecutionPlan executionPlan) {
        // Deploy new component instance
        final DeployComponentAction deployAction = AtomicActionFactory
                .generateDeployComponentAction(changeComponentAction.getTargetAllocationContext());

        // Migrate state
        final MigrateComponentStateAction migrateStateAction = AtomicActionFactory.generateMigrateComponentStateAction(
                changeComponentAction.getSourceAllocationContext(), changeComponentAction.getTargetAllocationContext());

        // Connect replication
        final ConnectComponentAction connectAction = AtomicActionFactory.generateConnectComponentAction(
                changeComponentAction.getTargetAllocationContext(),
                changeComponentAction.getTargetProvidingAllocationContexts(),
                changeComponentAction.getTargetRequiringAllocationContexts());

        // Block incoming requests
        final BlockRequestsToComponentAction blockRequestsAction = AtomicActionFactory
                .generateBlockRequestsToComponentAction(changeComponentAction.getTargetAllocationContext(),
                        changeComponentAction.getTargetRequiringAllocationContexts());

        // Finish running transactions
        final FinishComponentAction finishAction = AtomicActionFactory
                .generateFinishComponentAction(changeComponentAction.getTargetAllocationContext());

        // Disconnect component instance
        final DisconnectComponentAction disconnectAction = AtomicActionFactory.generateDisconnectComponentAction(
                changeComponentAction.getTargetAllocationContext(),
                changeComponentAction.getTargetProvidingAllocationContexts(),
                changeComponentAction.getTargetRequiringAllocationContexts());

        // Undeploy component instance
        final UndeployComponentAction undeployAction = AtomicActionFactory
                .generateUndeployComponentAction(changeComponentAction.getTargetAllocationContext());

        executionPlan.getActions().add(deployAction);
        executionPlan.getActions().add(migrateStateAction);
        executionPlan.getActions().add(connectAction);
        executionPlan.getActions().add(blockRequestsAction);
        executionPlan.getActions().add(finishAction);
        executionPlan.getActions().add(disconnectAction);
        executionPlan.getActions().add(undeployAction);
    }

    private void generateAtomicActions(final AllocateAction allocateAction, final ExecutionPlan executionPlan) {
        // Allocate Node
        final AllocateNodeAction allocateNodeAction = AtomicActionFactory
                .generateAllocateNodeAction(allocateAction.getTargetResourceContainer());

        // Connect Node
        final ConnectNodeAction connectNodeAction = AtomicActionFactory.generateConnectNodeAction(
                allocateAction.getTargetResourceContainer(), allocateAction.getTargetLinkingResources());

        executionPlan.getActions().add(allocateNodeAction);
        executionPlan.getActions().add(connectNodeAction);
    }

    private void generateAtomicActions(final DeallocateAction deallocateAction, final ExecutionPlan executionPlan) {
        // Disconnect Node
        final DisconnectNodeAction disconnectNodeAction = AtomicActionFactory.generateDisonnectNodeAction(
                deallocateAction.getTargetResourceContainer(), deallocateAction.getTargetLinkingResources());

        // Deallocate Node
        final DeallocateNodeAction deallocateNodeAction = AtomicActionFactory
                .generateDeallocateNodeAction(deallocateAction.getTargetResourceContainer());

        executionPlan.getActions().add(disconnectNodeAction);
        executionPlan.getActions().add(deallocateNodeAction);
    }
}
