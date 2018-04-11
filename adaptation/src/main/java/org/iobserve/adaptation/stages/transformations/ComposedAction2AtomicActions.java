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

import java.util.List;

import teetime.stage.basic.AbstractTransformation;

import org.iobserve.adaptation.executionplan.AtomicAction;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.ComposedAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.DereplicateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;

/**
 *
 * @author Lars Bluemke
 *
 */
public class ComposedAction2AtomicActions extends AbstractTransformation<ComposedAction, AtomicAction> {

    IComposed2AtomicAction<ReplicateAction> replicationTransformer = new ReplicateAction2AtomicActions();
    IComposed2AtomicAction<DereplicateAction> dereplicationTransformer = new DereplicateAction2AtomicActions();
    IComposed2AtomicAction<MigrateAction> migrationTransformer = new MigrateAction2AtomicActions();
    IComposed2AtomicAction<ChangeRepositoryComponentAction> changeComponentTransformer = new ChangeComponentAction2AtomicActions();
    IComposed2AtomicAction<AllocateAction> allocationTransformer = new AllocateAction2AtomicActions();
    IComposed2AtomicAction<DeallocateAction> deallocationTransformer = new DeallocateAction2AtomicActions();

    @Override
    protected void execute(final ComposedAction composedAction) throws Exception {
        List<AtomicAction> atomicActions = null;

        if (composedAction instanceof ReplicateAction) {
            atomicActions = this.replicationTransformer.transform((ReplicateAction) composedAction);
        } else if (composedAction instanceof DereplicateAction) {
            atomicActions = this.dereplicationTransformer.transform((DereplicateAction) composedAction);
        } else if (composedAction instanceof MigrateAction) {
            atomicActions = this.migrationTransformer.transform((MigrateAction) composedAction);
        } else if (composedAction instanceof ChangeRepositoryComponentAction) {
            atomicActions = this.changeComponentTransformer.transform((ChangeRepositoryComponentAction) composedAction);
        } else if (composedAction instanceof AllocateAction) {
            atomicActions = this.allocationTransformer.transform((AllocateAction) composedAction);
        } else if (composedAction instanceof DeallocateAction) {
            atomicActions = this.deallocationTransformer.transform((DeallocateAction) composedAction);
        }

        for (final AtomicAction a : atomicActions) {
            this.outputPort.send(a);
        }
    }

}
