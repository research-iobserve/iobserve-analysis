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
package org.iobserve.service.privacy.violation.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 * Container for transportation of the information, which methods (probes) monitoring should be
 * (de-)activated.
 *
 * @author Marc Adolf
 *
 */
public class ProbeManagementData {
    private final Map<AllocationContext, Set<OperationSignature>> methodsToActivate;
    private final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate;
    private Map<AllocationContext, Set<OperationSignature>> methodsToUpdate;

    private List<String> whitelist = null;

    public ProbeManagementData(final Map<AllocationContext, Set<OperationSignature>> methodsToActivate,
            final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate) {
        this(methodsToActivate, methodsToDeactivate, new HashMap<AllocationContext, Set<OperationSignature>>());
    }

    public ProbeManagementData(final Map<AllocationContext, Set<OperationSignature>> methodsToActivate,
            final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate,
            final Map<AllocationContext, Set<OperationSignature>> methodsToUpdate) {
        this.methodsToActivate = methodsToActivate;
        this.methodsToDeactivate = methodsToDeactivate;
        this.methodsToUpdate = methodsToUpdate;
    }

    public Map<AllocationContext, Set<OperationSignature>> getMethodsToActivate() {
        return this.methodsToActivate;
    }

    public Map<AllocationContext, Set<OperationSignature>> getMethodsToDeactivate() {
        return this.methodsToDeactivate;
    }

    public void setMethodsToUpdate(final Map<AllocationContext, Set<OperationSignature>> methodsToUpdate) {
        this.methodsToUpdate = methodsToUpdate;
    }

    public Map<AllocationContext, Set<OperationSignature>> getMethodsToUpdate() {
        return this.methodsToUpdate;
    }

    /**
     *
     * @return null, if there is no whitelist
     */
    public List<String> getWhitelist() {
        return this.whitelist;
    }

    public void setWhitelist(final List<String> whitelist) {
        this.whitelist = whitelist;
    }

}
