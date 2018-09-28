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
package org.iobserve.execution.stages;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.adaptation.executionplan.AllocateNodeAction;
import org.iobserve.adaptation.executionplan.AtomicAction;
import org.iobserve.adaptation.executionplan.BlockRequestsToComponentAction;
import org.iobserve.adaptation.executionplan.ConnectComponentAction;
import org.iobserve.adaptation.executionplan.ConnectNodeAction;
import org.iobserve.adaptation.executionplan.DeallocateNodeAction;
import org.iobserve.adaptation.executionplan.DeployComponentAction;
import org.iobserve.adaptation.executionplan.DisconnectComponentAction;
import org.iobserve.adaptation.executionplan.DisconnectNodeAction;
import org.iobserve.adaptation.executionplan.FinishComponentAction;
import org.iobserve.adaptation.executionplan.MigrateComponentStateAction;
import org.iobserve.adaptation.executionplan.UndeployComponentAction;

/**
 * Executes the incoming actions in the order they arrive by invoking the correspondent executor.
 *
 * @author Lars Bluemke
 *
 */
public class AtomicActionExecution extends AbstractConsumerStage<AtomicAction> {

    private final IExecutor<DeployComponentAction> deploymentExecutor;
    private final IExecutor<UndeployComponentAction> undeploymentExecutor;
    private final IExecutor<MigrateComponentStateAction> stateMigrationExecutor;
    private final IExecutor<ConnectComponentAction> connectionExecutor;
    private final IExecutor<BlockRequestsToComponentAction> requestBlockingExecutor;
    private final IExecutor<DisconnectComponentAction> disconnectionExecutor;
    private final IExecutor<FinishComponentAction> finishingExecutor;
    private final IExecutor<AllocateNodeAction> nodeAllocationExecutor;
    private final IExecutor<DeallocateNodeAction> nodeDeallocationExecutor;
    private final IExecutor<ConnectNodeAction> nodeConnectionExecutor;
    private final IExecutor<DisconnectNodeAction> nodeDisconnectionExecutor;

    /**
     * Creates a new execution instance. Requires executors supporting a concrete cloud api.
     *
     * @param deploymentExecutor
     *            Executor for deployments
     * @param undeploymentExecutor
     *            Executor for undeployments
     * @param stateMigrationExecutor
     *            Executor for state migrations
     * @param connectionExecutor
     *            Executor for connecting components
     * @param requestBlockingExecutor
     *            Executor for blocking incoming requests
     * @param disconnectionExecutor
     *            Executor for disconnecting components
     * @param finishingExecutor
     *            Executor for finishing running transactions
     * @param nodeAllocationExecutor
     *            Executor for node allocations
     * @param nodeDeallocationExecutor
     *            Executor for node deallocations
     * @param nodeConnectionExecutor
     *            Executor for connecting nodes
     * @param nodeDisconnectionExecutor
     *            Executor for disconnecting nodes
     */
    public AtomicActionExecution(final IExecutor<DeployComponentAction> deploymentExecutor,
            final IExecutor<UndeployComponentAction> undeploymentExecutor,
            final IExecutor<MigrateComponentStateAction> stateMigrationExecutor,
            final IExecutor<ConnectComponentAction> connectionExecutor,
            final IExecutor<BlockRequestsToComponentAction> requestBlockingExecutor,
            final IExecutor<DisconnectComponentAction> disconnectionExecutor,
            final IExecutor<FinishComponentAction> finishingExecutor,
            final IExecutor<AllocateNodeAction> nodeAllocationExecutor,
            final IExecutor<DeallocateNodeAction> nodeDeallocationExecutor,
            final IExecutor<ConnectNodeAction> nodeConnectionExecutor,
            final IExecutor<DisconnectNodeAction> nodeDisconnectionExecutor) {
        this.deploymentExecutor = deploymentExecutor;
        this.undeploymentExecutor = undeploymentExecutor;
        this.stateMigrationExecutor = stateMigrationExecutor;
        this.connectionExecutor = connectionExecutor;
        this.requestBlockingExecutor = requestBlockingExecutor;
        this.disconnectionExecutor = disconnectionExecutor;
        this.finishingExecutor = finishingExecutor;
        this.nodeAllocationExecutor = nodeAllocationExecutor;
        this.nodeDeallocationExecutor = nodeDeallocationExecutor;
        this.nodeConnectionExecutor = nodeConnectionExecutor;
        this.nodeDisconnectionExecutor = nodeDisconnectionExecutor;
    }

    @Override
    protected void execute(final AtomicAction atomicAction) throws Exception {
        if ((this.deploymentExecutor != null) && (atomicAction instanceof DeployComponentAction)) {
            this.deploymentExecutor.execute((DeployComponentAction) atomicAction);
        } else if ((this.undeploymentExecutor != null) && (atomicAction instanceof UndeployComponentAction)) {
            this.undeploymentExecutor.execute((UndeployComponentAction) atomicAction);
        } else if ((this.stateMigrationExecutor != null) && (atomicAction instanceof MigrateComponentStateAction)) {
            this.stateMigrationExecutor.execute((MigrateComponentStateAction) atomicAction);
        } else if ((this.connectionExecutor != null) && (atomicAction instanceof ConnectComponentAction)) {
            this.connectionExecutor.execute((ConnectComponentAction) atomicAction);
        } else if ((this.requestBlockingExecutor != null) && (atomicAction instanceof BlockRequestsToComponentAction)) {
            this.requestBlockingExecutor.execute((BlockRequestsToComponentAction) atomicAction);
        } else if ((this.disconnectionExecutor != null) && (atomicAction instanceof DisconnectComponentAction)) {
            this.disconnectionExecutor.execute((DisconnectComponentAction) atomicAction);
        } else if ((this.finishingExecutor != null) && (atomicAction instanceof FinishComponentAction)) {
            this.finishingExecutor.execute((FinishComponentAction) atomicAction);
        } else if ((this.nodeAllocationExecutor != null) && (atomicAction instanceof AllocateNodeAction)) {
            this.nodeAllocationExecutor.execute((AllocateNodeAction) atomicAction);
        } else if ((this.nodeDeallocationExecutor != null) && (atomicAction instanceof DeallocateNodeAction)) {
            this.nodeDeallocationExecutor.execute((DeallocateNodeAction) atomicAction);
        } else if ((this.nodeConnectionExecutor != null) && (atomicAction instanceof ConnectNodeAction)) {
            this.nodeConnectionExecutor.execute((ConnectNodeAction) atomicAction);
        } else if ((this.nodeDisconnectionExecutor != null) && (atomicAction instanceof DisconnectNodeAction)) {
            this.nodeDisconnectionExecutor.execute((DisconnectNodeAction) atomicAction);
        }
    }

}
