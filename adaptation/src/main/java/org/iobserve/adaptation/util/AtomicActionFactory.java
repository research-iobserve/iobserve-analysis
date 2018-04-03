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
package org.iobserve.adaptation.util;

import org.iobserve.planning.systemadaptation.Action;
import org.iobserve.planning.systemadaptation.AllocateAction;
import org.iobserve.planning.systemadaptation.ChangeRepositoryComponentAction;
import org.iobserve.planning.systemadaptation.DeallocateAction;
import org.iobserve.planning.systemadaptation.DereplicateAction;
import org.iobserve.planning.systemadaptation.MigrateAction;
import org.iobserve.planning.systemadaptation.ReplicateAction;

/**
 * Factory class for the creation of atomic adaptation actions as used in the execution plan.
 *
 * @author Lars Bluemke
 *
 */
public final class AtomicActionFactory {

    private AtomicActionFactory() {
    }

    public static void generateAtomicActions(final Action action, final Object executionPlan) {
        if (action instanceof ReplicateAction) {
            AtomicActionFactory.generateAtomicActions((ReplicateAction) action, executionPlan);
        } else if (action instanceof DereplicateAction) {
            AtomicActionFactory.generateAtomicActions((DereplicateAction) action, executionPlan);
        } else if (action instanceof MigrateAction) {
            AtomicActionFactory.generateAtomicActions((MigrateAction) action, executionPlan);
        } else if (action instanceof ChangeRepositoryComponentAction) {
            AtomicActionFactory.generateAtomicActions((ChangeRepositoryComponentAction) action, executionPlan);
        } else if (action instanceof AllocateAction) {
            AtomicActionFactory.generateAtomicActions((AllocateAction) action, executionPlan);
        } else if (action instanceof DeallocateAction) {
            AtomicActionFactory.generateAtomicActions((DeallocateAction) action, executionPlan);
        }
    }

    public static void generateAtomicActions(final ReplicateAction action, final Object executionPlan) {
        // Deploy new component instance
        // Migrate state
        // Connect replication
    }

    public static void generateAtomicActions(final DereplicateAction action, final Object executionPlan) {
        // Block incoming requests
        // Finish running transactions
        // Disconnect component instance
        // Undeploy component instance
    }

    public static void generateAtomicActions(final MigrateAction action, final Object executionPlan) {
        // Deploy new component instance
        // Migrate state
        // Connect replication
        // Block incoming requests
        // Finish running transactions
        // Disconnect component instance
        // Undeploy component instance
    }

    public static void generateAtomicActions(final ChangeRepositoryComponentAction action, final Object executionPlan) {
        // Deploy new component instance
        // Migrate state
        // Connect replication
        // Block incoming requests
        // Finish running transactions
        // Disconnect component instance
        // Undeploy component instance
    }

    public static void generateAtomicActions(final AllocateAction action, final Object executionPlan) {
        // Allocate Node
        // Connect Node
    }

    public static void generateAtomicActions(final DeallocateAction action, final Object executionPlan) {
        // Disconnect Node
        // Deallocate Node
    }
}
