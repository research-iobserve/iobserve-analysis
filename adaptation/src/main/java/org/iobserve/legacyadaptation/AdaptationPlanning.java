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
package org.iobserve.legacyadaptation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import teetime.stage.basic.AbstractTransformation;

import org.iobserve.adaptation.data.AdaptationData;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.AssemblyContextAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.ComposedAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.DereplicateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;
import org.iobserve.planning.systemadaptation.ResourceContainerAction;

/**
 * This stage orderes the adaptation {@link Action}s into an executable sequence.
 *
 * @author Philipp Weimann
 * @author Lars Blümke (Refactoring of system adaptation model: terminology: "(de-)allocate" ->
 *         "(de-)replicate", "aquire/terminate" -> "(de-)allocate", changes to sources and targets
 *         of actions)
 *
 */
public class AdaptationPlanning extends AbstractTransformation<AdaptationData, AdaptationData> {

    private Collection<ResourceContainerAction> allocations;
    private Collection<ResourceContainerAction> deallocations;

    private Collection<AssemblyContextAction> replications;
    private Collection<AssemblyContextAction> migrations;
    private Collection<AssemblyContextAction> changes;
    private Collection<AssemblyContextAction> dereplications;

    public AdaptationPlanning() { // NOCS missing comment
        // empty constructor
    }

    @Override
    protected void execute(final AdaptationData element) throws Exception {

        SystemAdaptation.LOG.info("Planning adaptation order");

        this.init(element);

        final List<ComposedAction> adaptionSteps = new ArrayList<>();
        adaptionSteps.addAll(this.allocations);
        adaptionSteps.addAll(this.dereplications);
        adaptionSteps.addAll(this.replications);
        adaptionSteps.addAll(this.changes);
        adaptionSteps.addAll(this.migrations);
        adaptionSteps.addAll(this.deallocations);

        element.setExecutionOrder(adaptionSteps);

        this.printAdaptionSequence(adaptionSteps);
        this.outputPort.send(element);
    }

    private void init(final AdaptationData data) {
        this.allocations = data.getRcActions().stream().filter(s -> s instanceof AllocateAction)
                .collect(Collectors.toSet());
        this.deallocations = data.getRcActions().stream().filter(s -> s instanceof DeallocateAction)
                .collect(Collectors.toSet());

        this.replications = data.getAcActions().stream().filter(s -> s instanceof ReplicateAction)
                .collect(Collectors.toSet());
        this.migrations = data.getAcActions().stream().filter(s -> s instanceof MigrateAction)
                .collect(Collectors.toSet());
        this.changes = data.getAcActions().stream().filter(s -> s instanceof ChangeRepositoryComponentAction)
                .collect(Collectors.toSet());
        this.dereplications = data.getAcActions().stream().filter(s -> s instanceof DereplicateAction)
                .collect(Collectors.toSet());
    }

    private void printAdaptionSequence(final List<ComposedAction> adaptionSteps) {
        final StringBuilder sb = new StringBuilder(50);
        sb.append("Adaption sequence:\n");

        for (int i = 0; i < adaptionSteps.size(); i++) {
            sb.append(i + "\t" + this.printAction(adaptionSteps.get(i)) + "\n");
        }

        SystemAdaptation.LOG.info(sb.toString());
    }

    private String printAction(final ComposedAction action) {
        final StringBuilder sb = new StringBuilder(200);

        if (action instanceof AllocateAction) {
            final AllocateAction allocate = (AllocateAction) action;
            sb.append("Allocate:\t").append(allocate.getTargetResourceContainer().getEntityName());
            sb.append("\tID: ").append(allocate.getTargetResourceContainer().getId());

        } else if (action instanceof DeallocateAction) {
            final DeallocateAction deallocate = (DeallocateAction) action;
            sb.append("Deallocate:\t").append(deallocate.getTargetResourceContainer().getEntityName());
            sb.append("\tID: ").append(deallocate.getTargetResourceContainer().getId());

        } else if (action instanceof ReplicateAction) {
            final ReplicateAction replicate = (ReplicateAction) action;
            sb.append("Replicate:\t").append(
                    replicate.getSourceAllocationContext().getAssemblyContext_AllocationContext().getEntityName());
            sb.append("\tID: ")
                    .append(replicate.getSourceAllocationContext().getAssemblyContext_AllocationContext().getId());
            sb.append("\t ------- ");
            sb.append("\t->\t").append(replicate.getTargetAllocationContext().getEntityName());

        } else if (action instanceof MigrateAction) {
            final MigrateAction migrate = (MigrateAction) action;
            sb.append("Migrate:\t").append(
                    migrate.getSourceAllocationContext().getAssemblyContext_AllocationContext().getEntityName());
            sb.append("\tID: ")
                    .append(migrate.getSourceAllocationContext().getAssemblyContext_AllocationContext().getId());
            sb.append('\t').append(
                    migrate.getSourceAllocationContext().getResourceContainer_AllocationContext().getEntityName());
            sb.append("\t->\t").append(
                    migrate.getTargetAllocationContext().getResourceContainer_AllocationContext().getEntityName());

        } else if (action instanceof ChangeRepositoryComponentAction) {
            final ChangeRepositoryComponentAction change = (ChangeRepositoryComponentAction) action;
            sb.append("ChangeComp:\t")
                    .append(change.getTargetAllocationContext().getAssemblyContext_AllocationContext().getEntityName());
            sb.append("\tID: ")
                    .append(change.getTargetAllocationContext().getAssemblyContext_AllocationContext().getId());
            sb.append('\t').append(change.getSourceAllocationContext().getAssemblyContext_AllocationContext()
                    .getEncapsulatedComponent__AssemblyContext().getEntityName());
            sb.append("\t->\t").append(change.getTargetAllocationContext().getAssemblyContext_AllocationContext()
                    .getEncapsulatedComponent__AssemblyContext().getEntityName());

        } else if (action instanceof DereplicateAction) {
            final DereplicateAction dereplicate = (DereplicateAction) action;
            sb.append("Dereplicate:\t").append(
                    dereplicate.getTargetAllocationContext().getAssemblyContext_AllocationContext().getEntityName());
            sb.append("\tID: ")
                    .append(dereplicate.getTargetAllocationContext().getAssemblyContext_AllocationContext().getId());

        } else {
            sb.append("UNKOWN:\t" + " ------------------------------------ ");
            sb.append("\tID: " + " ------------------------------------ ");
        }

        return sb.toString();
    }

}
