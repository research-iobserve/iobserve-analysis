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
import java.util.List;
import java.util.Map;
import java.util.Set;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.deployment.data.IPCMDeploymentEvent;
import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.service.privacy.violation.data.WarningModel;
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
public class ModelProbeController extends AbstractConsumerStage<WarningModel> {

    private final OutputPort<ProbeManagementData> outputPort = this.createOutputPort(ProbeManagementData.class);

    // Allocation and list of method names
    private Map<AllocationContext, Set<OperationSignature>> currentActiveWarnings = new HashMap<>();

    /**
     * Create an initialize the model probe controller.
     */
    public ModelProbeController() {
        // empty
    }

    @Override
    protected void execute(final WarningModel element) throws Exception {
        if (element.getWarningEdges() == null || element.getWarningEdges().isEmpty()) {
            this.logger.error("Received warning with empty edge list");
            return;
        } else {
            // ExperimentLoggingUtils.logEvent(element.getEvent().getTimestamp(), EventTypes.NONE,
            // ObservationPoint.COMPUTE_PROBE_CONFIGURATION_ENTRY);
            final Map<AllocationContext, Set<OperationSignature>> receivedWarnings = this
                    .computeReceivedWarnings(element.getWarningEdges());
            final Map<AllocationContext, Set<OperationSignature>> currentWarnings = new HashMap<>(
                    this.currentActiveWarnings);

            final ProbeManagementData probeMethodInformation = this.computeWarningDifferences(currentWarnings,
                    receivedWarnings, element.getEvent());

            this.currentActiveWarnings = this.computeNewWarningMap(currentWarnings,
                    probeMethodInformation.getMethodsToActivate(), probeMethodInformation.getMethodsToDeactivate());

            probeMethodInformation.setProtectedOperations(receivedWarnings);
            probeMethodInformation.setOperationsToUpdate(this.currentActiveWarnings);

            // ExperimentLoggingUtils.logEvent(element.getEvent().getTimestamp(), EventTypes.NONE,
            // ObservationPoint.COMPUTE_PROBE_CONFIGURATION_EXIT);
            this.outputPort.send(probeMethodInformation);
        }
    }

    private Map<AllocationContext, Set<OperationSignature>> computeReceivedWarnings(final List<Edge> edges) {
        final Map<AllocationContext, Set<OperationSignature>> receivedWarnings = new HashMap<>();

        this.logger.debug("Compute received warnings {}", edges.size());

        for (final Edge edge : edges) {
            this.logger.debug("Compute received warnings for edge={}", edge.getName());
            // multiple methods per allocation possible
            final AllocationContext allocation = edge.getSource().getAllocationContext();
            if (allocation != null) {
                this.logger.debug("Compute received warnings for {} {}", allocation.getEntityName(),
                        edge.getOperationSignature().getEntityName());

                Set<OperationSignature> methodSignatures = receivedWarnings.get(allocation);
                // if not present, add new entry
                if (methodSignatures == null) {
                    methodSignatures = new HashSet<>();
                }
                if (edge.getOperationSignature() != null) {
                    methodSignatures.add(edge.getOperationSignature());
                    receivedWarnings.put(allocation, methodSignatures);
                } else {
                    this.logger.debug("Received warning without operation signature");
                }
            } else {
                this.logger.debug("Compute received warnings No ALLOCATION for the specified edge {}", edge.getName());
            }
        }

        return receivedWarnings;
    }

    private ProbeManagementData computeWarningDifferences(
            final Map<AllocationContext, Set<OperationSignature>> currentWarnings,
            final Map<AllocationContext, Set<OperationSignature>> receivedWarnings,
            final IPCMDeploymentEvent ipcmDeploymentEvent) {
        Map<AllocationContext, Set<OperationSignature>> missingWarnings = new HashMap<>();
        Map<AllocationContext, Set<OperationSignature>> addedWarnings = new HashMap<>();

        missingWarnings = new HashMap<>();
        addedWarnings = new HashMap<>();
        final Map<AllocationContext, Set<OperationSignature>> newAllocationWarnings = new HashMap<>(receivedWarnings);
        // already existing allocations
        for (final AllocationContext allocation : currentWarnings.keySet()) {
            final Set<OperationSignature> newMethodSet = receivedWarnings.get(allocation);
            final Set<OperationSignature> currentMethods = currentWarnings.get(allocation);
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

            // the remaining entries are completely new warnings and allocations
            newAllocationWarnings.remove(allocation);

        }

        addedWarnings.putAll(newAllocationWarnings);

        final ProbeManagementData result = new ProbeManagementData(addedWarnings, missingWarnings);
        result.setTriggerTime(ipcmDeploymentEvent.getTimestamp());
        return result;

    }

    private Map<AllocationContext, Set<OperationSignature>> computeNewWarningMap(
            final Map<AllocationContext, Set<OperationSignature>> currentWarnings,
            final Map<AllocationContext, Set<OperationSignature>> addedWarnings,
            final Map<AllocationContext, Set<OperationSignature>> missingWarnings) {
        final Map<AllocationContext, Set<OperationSignature>> newWarningMap = new HashMap<>(currentWarnings);

        for (final AllocationContext allocation : missingWarnings.keySet()) {
            Set<OperationSignature> currentMethodSet = new HashSet<>();
            if (currentWarnings.get(allocation) != null) {
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
            Set<OperationSignature> currentMethodSet = new HashSet<>();
            if (currentWarnings.get(allocation) != null) {
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
