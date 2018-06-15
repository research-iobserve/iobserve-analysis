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

import java.util.ArrayList;
import java.util.List;

import org.iobserve.adaptation.executionplan.AtomicAction;
import org.iobserve.adaptation.executionplan.AtomicActionFactory;
import org.iobserve.planning.systemadaptation.ReplicateAction;

/**
 * Creates the necessary atomic actions to execute a replication.
 *
 * @author Lars Bluemke
 *
 */
public class ReplicateAction2AtomicActions implements IComposed2AtomicAction<ReplicateAction> {

    @Override
    public List<AtomicAction> transform(final ReplicateAction replicateAction) {
        final List<AtomicAction> atomicActions = new ArrayList<>();

        // Deploy new component instance
        atomicActions
                .add(AtomicActionFactory.generateDeployComponentAction(replicateAction.getTargetAllocationContext()));

        // Migrate state
        atomicActions.add(AtomicActionFactory.generateMigrateComponentStateAction(
                replicateAction.getSourceAllocationContext(), replicateAction.getTargetAllocationContext()));

        // Connect replication
        atomicActions.add(AtomicActionFactory.generateConnectComponentAction(
                replicateAction.getTargetAllocationContext(), replicateAction.getTargetProvidingAllocationContexts(),
                replicateAction.getTargetRequiringAllocationContexts()));

        return atomicActions;
    }

}
