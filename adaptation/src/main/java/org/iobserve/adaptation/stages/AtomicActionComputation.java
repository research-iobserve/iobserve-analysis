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
import teetime.stage.MultipleInstanceOfFilter;
import teetime.stage.basic.merger.Merger;

import org.iobserve.adaptation.executionplan.AtomicAction;
import org.iobserve.adaptation.executionplan.ExecutionPlan;
import org.iobserve.adaptation.stages.transformations.AllocateAction2AtomicActions;
import org.iobserve.adaptation.stages.transformations.AtomicActions2ExecutionPlan;
import org.iobserve.adaptation.stages.transformations.ChangeComponentAction2AtomicActions;
import org.iobserve.adaptation.stages.transformations.DeallocateAction2AtomicActions;
import org.iobserve.adaptation.stages.transformations.DereplicateAction2AtomicActions;
import org.iobserve.adaptation.stages.transformations.MigrateAction2AtomicActions;
import org.iobserve.adaptation.stages.transformations.ReplicateAction2AtomicActions;
import org.iobserve.adaptation.stages.transformations.SystemAdaptationModel2ComposedActions;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.ComposedAction;
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
public class AtomicActionComputation extends CompositeStage {

    private final SystemAdaptationModel2ComposedActions systemAdaptationModel2ComposedActions;
    private final AtomicActions2ExecutionPlan atomicActions2ExecutionPlan;

    public AtomicActionComputation() {
        final MultipleInstanceOfFilter<ComposedAction> instanceOfFilter = new MultipleInstanceOfFilter<>();
        final ReplicateAction2AtomicActions replicate2Atomic = new ReplicateAction2AtomicActions();
        final DereplicateAction2AtomicActions dereplicate2Atomic = new DereplicateAction2AtomicActions();
        final MigrateAction2AtomicActions migrate2Atomic = new MigrateAction2AtomicActions();
        final ChangeComponentAction2AtomicActions changeComponent2Atomic = new ChangeComponentAction2AtomicActions();
        final AllocateAction2AtomicActions allocation2Atomic = new AllocateAction2AtomicActions();
        final DeallocateAction2AtomicActions deallocation2Atomic = new DeallocateAction2AtomicActions();
        final Merger<AtomicAction> merger = new Merger<>();

        this.systemAdaptationModel2ComposedActions = new SystemAdaptationModel2ComposedActions();
        this.atomicActions2ExecutionPlan = new AtomicActions2ExecutionPlan();

        // system adaptation model -> single composed actions
        this.connectPorts(this.systemAdaptationModel2ComposedActions.getOutputPort(), instanceOfFilter.getInputPort());

        // instanceof filter -> composed to atomic action transformations
        this.connectPorts(instanceOfFilter.getOutputPortForType(ReplicateAction.class),
                replicate2Atomic.getInputPort());
        this.connectPorts(instanceOfFilter.getOutputPortForType(DereplicateAction.class),
                dereplicate2Atomic.getInputPort());
        this.connectPorts(instanceOfFilter.getOutputPortForType(MigrateAction.class), migrate2Atomic.getInputPort());
        this.connectPorts(instanceOfFilter.getOutputPortForType(ChangeRepositoryComponentAction.class),
                changeComponent2Atomic.getInputPort());
        this.connectPorts(instanceOfFilter.getOutputPortForType(AllocateAction.class),
                allocation2Atomic.getInputPort());
        this.connectPorts(instanceOfFilter.getOutputPortForType(DeallocateAction.class),
                deallocation2Atomic.getInputPort());

        // composed to atomic action transformations -> merger
        this.connectPorts(replicate2Atomic.getOutputPort(), merger.getNewInputPort());
        this.connectPorts(dereplicate2Atomic.getOutputPort(), merger.getNewInputPort());
        this.connectPorts(migrate2Atomic.getOutputPort(), merger.getNewInputPort());
        this.connectPorts(changeComponent2Atomic.getOutputPort(), merger.getNewInputPort());
        this.connectPorts(allocation2Atomic.getOutputPort(), merger.getNewInputPort());
        this.connectPorts(deallocation2Atomic.getOutputPort(), merger.getNewInputPort());

        // merger -> single atomic actions to execution plan
        this.connectPorts(merger.getOutputPort(), this.atomicActions2ExecutionPlan.getInputPort());
    }

    public InputPort<SystemAdaptation> getInputPort() {
        return this.systemAdaptationModel2ComposedActions.getInputPort();
    }

    public OutputPort<ExecutionPlan> getOutputPort() {
        return this.atomicActions2ExecutionPlan.getOutputPort();
    }
}
