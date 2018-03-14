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
package org.iobserve.model.test.data;

import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationFactory;

/**
 * @author Reiner Jung
 *
 */
public final class AllocationDataFactory {
    public static final Allocation ALLOCATION = AllocationDataFactory.createAllocation();

    /**
     * Empty factory.
     */
    private AllocationDataFactory() {
        // empty factory constructor.
    }

    private static Allocation createAllocation() {
        return AllocationFactory.eINSTANCE.createAllocation();
    }
}
