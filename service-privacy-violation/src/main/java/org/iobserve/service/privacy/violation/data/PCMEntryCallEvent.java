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
package org.iobserve.service.privacy.violation.data;

import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.RepositoryComponent;

/**
 * @author Reiner Jung
 *
 */
public class PCMEntryCallEvent {
    private final long entryTime;
    private final long exitTime;
    private final RepositoryComponent component;
    private final OperationSignature operationSignature;
    private final AssemblyContext assemblyContext;
    private final AllocationContext allocationContext;

    /**
     * Create a new PCM entry call.
     *
     * @param entryTime
     *            entry time
     * @param exitTime
     *            exit time
     * @param component
     *            related component
     * @param operationSignature
     *            related operation signature
     * @param assemblyContext
     *            where this component is located in the architecture
     * @param allocationContext
     *            allocation
     */
    public PCMEntryCallEvent(final long entryTime, final long exitTime, final RepositoryComponent component,
            final OperationSignature operationSignature, final AssemblyContext assemblyContext,
            final AllocationContext allocationContext) {
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.component = component;
        this.operationSignature = operationSignature;
        this.allocationContext = allocationContext;
        this.assemblyContext = assemblyContext;
    }

    public final long getEntryTime() {
        return this.entryTime;
    }

    public final long getExitTime() {
        return this.exitTime;
    }

    public final RepositoryComponent getComponent() {
        return this.component;
    }

    public final OperationSignature getOperationSignature() {
        return this.operationSignature;
    }

    public final AssemblyContext getAssemblyContext() {
        return this.assemblyContext;
    }

    public final AllocationContext getAllocationContext() {
        return this.allocationContext;
    }

}
