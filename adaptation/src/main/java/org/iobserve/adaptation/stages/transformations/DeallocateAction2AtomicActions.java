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
import org.iobserve.planning.systemadaptation.DeallocateAction;

/**
 * Creates the necessary atomic actions to execute a deallocation.
 *
 * @author Lars Bluemke
 *
 */
public class DeallocateAction2AtomicActions implements IComposed2AtomicAction<DeallocateAction> {

    @Override
    public List<AtomicAction> transform(final DeallocateAction deallocateAction) {
        final List<AtomicAction> atomicActions = new ArrayList<>();

        // Disconnect Node
        atomicActions.add(AtomicActionFactory.generateDisonnectNodeAction(deallocateAction.getTargetResourceContainer(),
                deallocateAction.getTargetLinkingResources()));

        // Deallocate Node
        atomicActions
                .add(AtomicActionFactory.generateDeallocateNodeAction(deallocateAction.getTargetResourceContainer()));

        return atomicActions;
    }

}
