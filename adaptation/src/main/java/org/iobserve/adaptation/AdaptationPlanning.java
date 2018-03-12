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
package org.iobserve.adaptation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import teetime.stage.basic.AbstractTransformation;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AcquireAction;
import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;
import org.iobserve.planning.systemadaptation.TerminateAction;

/**
 * This stage orderes the adaptation {@link Action}s into an executable sequence.
 *
 * @author Philipp Weimann
 *
 */
public class AdaptationPlanning extends AbstractTransformation<AdaptationData, AdaptationData> {

    private Collection<ResourceContainerAction> aquires;
    private Collection<ResourceContainerAction> terminates;

    private Collection<AssemblyContextAction> allocations;
    private Collection<AssemblyContextAction> migrations;
    private Collection<AssemblyContextAction> changes;
    private Collection<AssemblyContextAction> deallocations;

    @Override
    protected void execute(final AdaptationData element) throws Exception {

        SystemAdaptation.LOG.info("Planning adaptation order");

        this.init(element);

        final List<Action> adaptionSteps = new ArrayList<>();
        adaptionSteps.addAll(this.aquires);
        adaptionSteps.addAll(this.deallocations);
        adaptionSteps.addAll(this.allocations);
        adaptionSteps.addAll(this.changes);
        adaptionSteps.addAll(this.migrations);
        adaptionSteps.addAll(this.terminates);

        element.setExecutionOrder(adaptionSteps);

        this.printAdaptionSequence(adaptionSteps);
        this.outputPort.send(element);
    }

    private void init(final AdaptationData data) {
        this.aquires = data.getRcActions().stream().filter(s -> s instanceof AcquireAction).collect(Collectors.toSet());
        this.terminates = data.getRcActions().stream().filter(s -> s instanceof TerminateAction)
                .collect(Collectors.toSet());

        this.allocations = data.getAcActions().stream().filter(s -> s instanceof AllocateAction)
                .collect(Collectors.toSet());
        this.migrations = data.getAcActions().stream().filter(s -> s instanceof MigrateAction)
                .collect(Collectors.toSet());
        this.changes = data.getAcActions().stream().filter(s -> s instanceof ChangeRepositoryComponentAction)
                .collect(Collectors.toSet());
        this.deallocations = data.getAcActions().stream().filter(s -> s instanceof DeallocateAction)
                .collect(Collectors.toSet());
    }

    private void printAdaptionSequence(final List<Action> adaptionSteps) {
        final StringBuilder sb = new StringBuilder(50);
        sb.append("Adaption sequence:\n");

        for (int i = 0; i < adaptionSteps.size(); i++) {
            sb.append(i + "\t" + this.printAction(adaptionSteps.get(i)) + "\n");
        }

        SystemAdaptation.LOG.info(sb.toString());
    }

    private String printAction(final Action action) {
        final StringBuilder sb = new StringBuilder(200);

        if (action instanceof AcquireAction) {
            final AcquireAction acquire = (AcquireAction) action;
            sb.append("Acquire:\t").append(acquire.getSourceResourceContainer().getEntityName());
            sb.append("\tID: ").append(acquire.getSourceResourceContainer().getId());

        } else if (action instanceof TerminateAction) {
            final TerminateAction terminate = (TerminateAction) action;
            sb.append("Terminate:\t").append(terminate.getSourceResourceContainer().getEntityName());
            sb.append("\tID: ").append(terminate.getSourceResourceContainer().getId());

        } else if (action instanceof AllocateAction) {
            final AllocateAction allocate = (AllocateAction) action;
            sb.append("Allocate:\t").append(allocate.getSourceAssemblyContext().getEntityName());
            sb.append("\tID: ").append(allocate.getSourceAssemblyContext().getId());
            sb.append("\t ------- ");
            sb.append("\t->\t").append(allocate.getNewAllocationContext().getEntityName());

        } else if (action instanceof MigrateAction) {
            final MigrateAction migrate = (MigrateAction) action;
            sb.append("Migrate:\t").append(migrate.getSourceAssemblyContext().getEntityName());
            sb.append("\tID: ").append(migrate.getSourceAssemblyContext().getId());
            sb.append('\t').append(
                    migrate.getSourceAllocationContext().getResourceContainer_AllocationContext().getEntityName());
            sb.append("\t->\t")
                    .append(migrate.getNewAllocationContext().getResourceContainer_AllocationContext().getEntityName());

        } else if (action instanceof ChangeRepositoryComponentAction) {
            final ChangeRepositoryComponentAction change = (ChangeRepositoryComponentAction) action;
            sb.append("ChangeComp:\t").append(change.getSourceAssemblyContext().getEntityName());
            sb.append("\tID: ").append(change.getSourceAssemblyContext().getId());
            sb.append('\t').append(
                    change.getSourceAssemblyContext().getEncapsulatedComponent__AssemblyContext().getEntityName());
            sb.append("\t->\t").append(change.getNewRepositoryComponent().getEntityName());

        } else if (action instanceof DeallocateAction) {
            final DeallocateAction deAllocate = (DeallocateAction) action;
            sb.append("Deallocate:\t").append(deAllocate.getSourceAssemblyContext().getEntityName());
            sb.append("\tID: ").append(deAllocate.getSourceAssemblyContext().getId());

        } else {
            sb.append("UNKOWN:\t" + " ------------------------------------ ");
            sb.append("\tID: " + " ------------------------------------ ");
        }

        return sb.toString();
    }

}
