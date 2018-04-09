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
import org.iobserve.planning.systemadaptation.DereplicateAction;

/**
 * Creates the necessary atomic actions to execute a dereplication.
 *
 * @author Lars Bluemke
 *
 */
public class DereplicateAction2AtomicActions extends AbstractTransformation<DereplicateAction, AtomicAction> {

    @Override
    protected void execute(final DereplicateAction dereplicateAction) throws Exception {
        // Block incoming requests
        this.outputPort.send(AtomicActionFactory.generateBlockRequestsToComponentAction(
                dereplicateAction.getTargetAllocationContext(),
                dereplicateAction.getTargetRequiringAllocationContexts()));

        // Finish running transactions
        this.outputPort.send(
                AtomicActionFactory.generateFinishComponentAction(dereplicateAction.getTargetAllocationContext()));

        // Disconnect component instance
        this.outputPort.send(
                AtomicActionFactory.generateDisconnectComponentAction(dereplicateAction.getTargetAllocationContext(),
                        dereplicateAction.getTargetProvidingAllocationContexts(),
                        dereplicateAction.getTargetRequiringAllocationContexts()));

        // Undeploy component instance
        this.outputPort.send(
                AtomicActionFactory.generateUndeployComponentAction(dereplicateAction.getTargetAllocationContext()));

    }

}
