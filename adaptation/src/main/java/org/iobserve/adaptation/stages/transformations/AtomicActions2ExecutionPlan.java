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

import teetime.framework.InputPort;
import teetime.framework.signal.ISignal;
import teetime.stage.basic.AbstractTransformation;

import org.iobserve.adaptation.executionplan.AtomicAction;
import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.executionplan.ExecutionplanFactory;
import org.iobserve.adaptation.stages.signal.AllActionsSentSignal;

/**
 * Collects the incoming atomic actions into an execution plan.
 *
 * @author Lars Bluemke
 *
 */
public class AtomicActions2ExecutionPlan extends AbstractTransformation<AtomicAction, ExecutionPlan> {

    private ExecutionPlan executionPlan = ExecutionplanFactory.eINSTANCE.createExecutionPlan();

    @Override
    protected void execute(final AtomicAction atomicAction) throws Exception {
        this.executionPlan.getActions().add(atomicAction);
    }

    @Override
    public void onSignal(final ISignal signal, final InputPort<?> inputPort) {
        if (signal instanceof AllActionsSentSignal) {
            this.outputPort.send(this.executionPlan);
            this.executionPlan = ExecutionplanFactory.eINSTANCE.createExecutionPlan();
        } else {
            super.onSignal(signal, inputPort);
        }
    }
}
