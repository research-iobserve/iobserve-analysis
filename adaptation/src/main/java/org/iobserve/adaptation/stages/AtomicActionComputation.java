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

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.stages.transformations.AtomicActions2ExecutionPlan;
import org.iobserve.adaptation.stages.transformations.ComposedAction2AtomicActions;
import org.iobserve.adaptation.stages.transformations.SystemAdaptationModel2ComposedActions;
import org.iobserve.planning.systemadaptation.SystemAdaptation;

/**
 * Receives a {@link SystemAdaptation} model containing a list of composed adaptation actions and
 * computes the required atomic adaptation actions - the execution plan - which is passed to the
 * output port.
 *
 * @author Lars Bluemke
 *
 */
public class AtomicActionComputation extends CompositeStage {

    private final SystemAdaptationModel2ComposedActions systemAdaptationModel2ComposedActions;
    private final AtomicActions2ExecutionPlan atomicActions2ExecutionPlan;

    public AtomicActionComputation() {
        final ComposedAction2AtomicActions composed2AtomicActions = new ComposedAction2AtomicActions();

        this.systemAdaptationModel2ComposedActions = new SystemAdaptationModel2ComposedActions();
        this.atomicActions2ExecutionPlan = new AtomicActions2ExecutionPlan();

        this.connectPorts(this.systemAdaptationModel2ComposedActions.getOutputPort(),
                composed2AtomicActions.getInputPort());
        this.connectPorts(composed2AtomicActions.getOutputPort(), this.atomicActions2ExecutionPlan.getInputPort());
    }

    public InputPort<SystemAdaptation> getInputPort() {
        return this.systemAdaptationModel2ComposedActions.getInputPort();
    }

    public OutputPort<ExecutionPlan> getOutputPort() {
        return this.atomicActions2ExecutionPlan.getOutputPort();
    }
}
