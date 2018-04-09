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
package org.iobserve.adaptation.stages.transformations;

import teetime.stage.basic.AbstractTransformation;

import org.iobserve.adaptation.executionplan.AtomicAction;
import org.iobserve.adaptation.executionplan.AtomicActionFactory;
import org.iobserve.planning.systemadaptation.MigrateAction;

/**
 * Creates the necessary atomic actions to execute a migration.
 *
 * @author Lars Bluemke
 *
 */
public class MigrateAction2AtomicActions extends AbstractTransformation<MigrateAction, AtomicAction> {

    @Override
    protected void execute(final MigrateAction migrateAction) throws Exception {
        // Deploy new component instance
        this.outputPort
                .send(AtomicActionFactory.generateDeployComponentAction(migrateAction.getTargetAllocationContext()));

        // Migrate state
        this.outputPort.send(AtomicActionFactory.generateMigrateComponentStateAction(
                migrateAction.getSourceAllocationContext(), migrateAction.getTargetAllocationContext()));

        // Connect replication
        this.outputPort.send(AtomicActionFactory.generateConnectComponentAction(
                migrateAction.getTargetAllocationContext(), migrateAction.getTargetProvidingAllocationContexts(),
                migrateAction.getTargetRequiringAllocationContexts()));

        // Block incoming requests
        this.outputPort.send(AtomicActionFactory.generateBlockRequestsToComponentAction(
                migrateAction.getTargetAllocationContext(), migrateAction.getTargetRequiringAllocationContexts()));

        // Finish running transactions
        this.outputPort
                .send(AtomicActionFactory.generateFinishComponentAction(migrateAction.getTargetAllocationContext()));

        // Disconnect component instance
        this.outputPort.send(AtomicActionFactory.generateDisconnectComponentAction(
                migrateAction.getTargetAllocationContext(), migrateAction.getTargetProvidingAllocationContexts(),
                migrateAction.getTargetRequiringAllocationContexts()));

        // Undeploy component instance
        this.outputPort
                .send(AtomicActionFactory.generateUndeployComponentAction(migrateAction.getTargetAllocationContext()));
    }

}
