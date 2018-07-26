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
package org.iobserve.service.privacy.violation.filter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.service.privacy.violation.data.Warnings;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 * Model level controller for probes. The filter receives a list of warnings and computes a list of
 * probe activation and deactivation. Therefore, it requires internal knowledge of previous probe
 * activations.
 *
 * @author Marc Adolf -- first complete class
 * @author Reiner Jung -- initial
 *
 */
public class ModelProbeController extends AbstractConsumerStage<Warnings> {

    private final OutputPort<ProbeManagementData> outputPort = this.createOutputPort(ProbeManagementData.class);

    // Allocation and list of method names
    private Map<AllocationContext, Set<OperationSignature>> currentActiveWarnings = new HashMap<>();

    /**
     * Create an initialize the model probe controller.
     */
    public ModelProbeController() {
    }

    @Override
    protected void execute(final Warnings element) throws Exception {
        final Map<AllocationContext, Set<OperationSignature>> receivedWarnings = new HashMap<>();
        final Map<AllocationContext, Set<OperationSignature>> currentWarnings = new HashMap<>(
                this.currentActiveWarnings);

        for (final Edge edge : element.getWarningEdges()) {
            // multiple methods per allocation possible
            final AllocationContext allocation = edge.getSource().getAllocationContext();
            Set<OperationSignature> methodSignatures = receivedWarnings.get(allocation);
            // if not present, add new entry
            if (methodSignatures == null) {
                methodSignatures = new HashSet<>();
                receivedWarnings.put(allocation, methodSignatures);
            }
            methodSignatures.add(edge.getOperationSignature());

        }
        final ProbeManagementData probeMethodInformation = this.computeWarningDifferences(currentWarnings,
                receivedWarnings);

        this.currentActiveWarnings = this.computeNewWarningMap(currentWarnings,
                probeMethodInformation.getMethodsToActivate(), probeMethodInformation.getMethodsToDeactivate());

        this.outputPort.send(probeMethodInformation);
    }

    private ProbeManagementData computeWarningDifferences(
            final Map<AllocationContext, Set<OperationSignature>> currentWarnings,
            final Map<AllocationContext, Set<OperationSignature>> receivedWarnings) {
        Map<AllocationContext, Set<OperationSignature>> missingWarnings = new HashMap<>();
        Map<AllocationContext, Set<OperationSignature>> addedWarnings = new HashMap<>();

        missingWarnings = new HashMap<>();
        addedWarnings = new HashMap<>();
        final Map<AllocationContext, Set<OperationSignature>> newAllocationWarnings = new HashMap<>(receivedWarnings);
        // already existing allocations
        for (final AllocationContext allocation : this.currentActiveWarnings.keySet()) {
            final Set<OperationSignature> newMethodSet = receivedWarnings.get(allocation);
            final Set<OperationSignature> currentMethods = this.currentActiveWarnings.get(allocation);
            // no new allocation -> all old methods have to be deactivated
            if (newMethodSet == null) {
                missingWarnings.put(allocation, currentMethods);
            } else {
                // current - new = missing methods = deactivate
                final Set<OperationSignature> missingMethods = new HashSet<>(currentMethods);
                missingMethods.removeAll(newMethodSet);
                // new - current = added methods = activate
                final Set<OperationSignature> addedMethods = new HashSet<>(newMethodSet);
                addedMethods.removeAll(currentMethods);
                if (!missingMethods.isEmpty()) {
                    missingWarnings.put(allocation, missingMethods);
                }
                if (!addedMethods.isEmpty()) {
                    addedWarnings.put(allocation, addedMethods);
                }
            }

            // the remaining entries are completly new warnings and allocations
            newAllocationWarnings.remove(allocation);

        }

        addedWarnings.putAll(newAllocationWarnings);

        return new ProbeManagementData(addedWarnings, missingWarnings);

    }

    private Map<AllocationContext, Set<OperationSignature>> computeNewWarningMap(
            final Map<AllocationContext, Set<OperationSignature>> currentWarnings,
            final Map<AllocationContext, Set<OperationSignature>> addedWarnings,
            final Map<AllocationContext, Set<OperationSignature>> missingWarnings) {
        final Map<AllocationContext, Set<OperationSignature>> newWarningMap = new HashMap<>(currentWarnings);
        for (final AllocationContext allocation : missingWarnings.keySet()) {
        	Set<OperationSignature> currentMethodSet = new HashSet<OperationSignature>();
        	if (!currentWarnings.isEmpty()) {
            currentMethodSet = new HashSet<>(currentWarnings.get(allocation));
        	}
            currentMethodSet.removeAll(missingWarnings.get(allocation));
            if (currentMethodSet.isEmpty()) {
                newWarningMap.remove(allocation);
            } else {
                newWarningMap.put(allocation, currentMethodSet);
            }
        }
        for (final AllocationContext allocation : addedWarnings.keySet()) {
        	Set<OperationSignature> currentMethodSet = new HashSet<OperationSignature>();
          	if (!currentWarnings.isEmpty()) {
              currentMethodSet = new HashSet<>(currentWarnings.get(allocation));
          	}
          	currentMethodSet.addAll(addedWarnings.get(allocation));
            if (currentMethodSet.isEmpty()) {
                newWarningMap.remove(allocation);
            } else {
                newWarningMap.put(allocation, currentMethodSet);
            }
        }
        return newWarningMap;
    }

    public OutputPort<ProbeManagementData> getOutputPort() {
        return this.outputPort;
    }

}
