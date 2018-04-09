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
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;

/**
 * Creates the necessary atomic actions to execute a change repository component action.
 *
 * @author Lars Bluemke
 *
 */
public class ChangeComponentAction2AtomicActions
        extends AbstractTransformation<ChangeRepositoryComponentAction, AtomicAction> {

    @Override
    protected void execute(final ChangeRepositoryComponentAction changeComponentAction) throws Exception {
        // Deploy new component instance
        this.outputPort.send(
                AtomicActionFactory.generateDeployComponentAction(changeComponentAction.getTargetAllocationContext()));

        // Migrate state
        this.outputPort.send(AtomicActionFactory.generateMigrateComponentStateAction(
                changeComponentAction.getSourceAllocationContext(),
                changeComponentAction.getTargetAllocationContext()));

        // Connect replication
        this.outputPort.send(
                AtomicActionFactory.generateConnectComponentAction(changeComponentAction.getTargetAllocationContext(),
                        changeComponentAction.getTargetProvidingAllocationContexts(),
                        changeComponentAction.getTargetRequiringAllocationContexts()));

        // Block incoming requests
        this.outputPort.send(AtomicActionFactory.generateBlockRequestsToComponentAction(
                changeComponentAction.getTargetAllocationContext(),
                changeComponentAction.getTargetRequiringAllocationContexts()));

        // Finish running transactions
        this.outputPort.send(
                AtomicActionFactory.generateFinishComponentAction(changeComponentAction.getTargetAllocationContext()));

        // Disconnect component instance
        this.outputPort.send(AtomicActionFactory.generateDisconnectComponentAction(
                changeComponentAction.getTargetAllocationContext(),
                changeComponentAction.getTargetProvidingAllocationContexts(),
                changeComponentAction.getTargetRequiringAllocationContexts()));

        // Undeploy component instance
        this.outputPort.send(AtomicActionFactory
                .generateUndeployComponentAction(changeComponentAction.getTargetAllocationContext()));
    }

}
