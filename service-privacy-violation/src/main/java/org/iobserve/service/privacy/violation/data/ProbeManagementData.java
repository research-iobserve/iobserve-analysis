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
 * Model to remember which monitoring probes should be (de-)activated for certain methods.
 *
 * @author Marc Adolf
 *
 */
public class ProbeManagementData {

    private final Map<AllocationContext, Set<OperationSignature>> methodsToActivate;

    private final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate;

    private Map<AllocationContext, Set<OperationSignature>> operationsToUpdate;

    private Map<AllocationContext, Set<OperationSignature>> protectedOperations;

    private List<String> whitelist = null; // NOPMD document null

    private long triggerTime;

    /**
     * Create an empty probe management model.
     */
    public ProbeManagementData() {
        this(new HashMap<AllocationContext, Set<OperationSignature>>(),
                new HashMap<AllocationContext, Set<OperationSignature>>());
    }

    /**
     * Create a probe management model.
     *
     * @param methodsToActivate
     *            set of operations to activate
     * @param methodsToDeactivate
     *            set of operations to deactivate
     */
    public ProbeManagementData(final Map<AllocationContext, Set<OperationSignature>> methodsToActivate,
            final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate) {
        this(methodsToActivate, methodsToDeactivate, new HashMap<AllocationContext, Set<OperationSignature>>(),
                new HashMap<AllocationContext, Set<OperationSignature>>());
    }

    public ProbeManagementData(final Map<AllocationContext, Set<OperationSignature>> methodsToActivate,
            final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate,
            final Map<AllocationContext, Set<OperationSignature>> protectedOperations) {
        this(methodsToActivate, methodsToDeactivate, protectedOperations,
                new HashMap<AllocationContext, Set<OperationSignature>>());
    }

    private ProbeManagementData(final Map<AllocationContext, Set<OperationSignature>> methodsToActivate,
            final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate,
            final Map<AllocationContext, Set<OperationSignature>> protectedOperations,
            final Map<AllocationContext, Set<OperationSignature>> operationsToUpdate) {
        this.methodsToActivate = methodsToActivate;
        this.methodsToDeactivate = methodsToDeactivate;
        this.protectedOperations = protectedOperations;
        this.operationsToUpdate = operationsToUpdate;
    }

    public Map<AllocationContext, Set<OperationSignature>> getMethodsToActivate() {
        return this.methodsToActivate;
    }

    public Map<AllocationContext, Set<OperationSignature>> getMethodsToDeactivate() {
        return this.methodsToDeactivate;
    }

    public Map<AllocationContext, Set<OperationSignature>> getProtectedOperations() {
        return this.protectedOperations;
    }

    public void setProtectedOperations(final Map<AllocationContext, Set<OperationSignature>> protectedOperations) {
        this.protectedOperations = protectedOperations;
    }

    public void setOperationsToUpdate(final Map<AllocationContext, Set<OperationSignature>> operationsToUpdate) {
        this.operationsToUpdate = operationsToUpdate;
    }

    public Map<AllocationContext, Set<OperationSignature>> getMethodsToUpdate() {
        return this.operationsToUpdate;
    }

    /**
     *
     * @return returns the whitelist or null, if there is no whitelist.
     */
    public List<String> getWhitelist() {
        return this.whitelist;
    }

    public void setWhitelist(final List<String> whitelist) {
        this.whitelist = whitelist;
    }

    public final long getTriggerTime() {
        return this.triggerTime;
    }

    public final void setTriggerTime(final long triggerTime) {
        this.triggerTime = triggerTime;
    }

}
