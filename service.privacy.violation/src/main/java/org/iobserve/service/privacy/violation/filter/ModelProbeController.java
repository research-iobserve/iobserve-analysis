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
import org.palladiosimulator.pcm.allocation.Allocation;

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
    private Map<Allocation, Set<String>> currentWarnings = new HashMap<>();

    /**
     * Create an initialize the model probe controller.
     */
    public ModelProbeController() {
    }

    @Override
    protected void execute(final Warnings element) throws Exception {
        final Map<Allocation, Set<String>> receivedWarnings = new HashMap<>();
        for (final Edge edge : element.getWarningEdges()) {
            // multiple methods per allocation possible
            final Allocation allocation = edge.getSource().getAllocation();
            Set<String> methodSignatures = receivedWarnings.get(allocation);
            // if not present, add new entry
            if (methodSignatures == null) {
                methodSignatures = new HashSet<>();
                receivedWarnings.put(allocation, methodSignatures);
            }
            // TODO get Methodsignature
            methodSignatures.add("TODO");
            // this way?
            // methodSignatures.add(edge.getInterfaceName());

        }
        final ProbeManagementData probeMethodInformation = this.computeWarningDifferences(receivedWarnings);

        this.currentWarnings = this.computeNewWarningMap(probeMethodInformation.getMethodsToActivate(),
                probeMethodInformation.getMethodsToDeactivate());

        this.outputPort.send(probeMethodInformation);
    }

    private ProbeManagementData computeWarningDifferences(final Map<Allocation, Set<String>> receivedWarnings) {
        Map<Allocation, Set<String>> missingWarnings = new HashMap<>();
        Map<Allocation, Set<String>> addedWarnings = new HashMap<>();

        missingWarnings = new HashMap<>();
        addedWarnings = new HashMap<>();
        final Map<Allocation, Set<String>> newAllocationWarnings = new HashMap<>(receivedWarnings);
        // already existing allocations
        for (final Allocation allocation : this.currentWarnings.keySet()) {
            final Set<String> newMethodSet = receivedWarnings.get(allocation);
            final Set<String> currentMethods = this.currentWarnings.get(allocation);
            // no new allocation -> all old methods have to be deactivated
            if (newMethodSet == null) {
                missingWarnings.put(allocation, currentMethods);
            } else {
                // current - new = missing methods = deactivate
                final Set<String> missingMethods = new HashSet<>(currentMethods);
                missingMethods.removeAll(newMethodSet);
                // new - current = added methods = activate
                final Set<String> addedMethods = new HashSet<>(newMethodSet);
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

    private Map<Allocation, Set<String>> computeNewWarningMap(final Map<Allocation, Set<String>> addedWarnings,
            final Map<Allocation, Set<String>> missingWarnings) {
        final Map<Allocation, Set<String>> newWarningMap = new HashMap(this.currentWarnings);
        for (final Allocation allocation : missingWarnings.keySet()) {
            final Set<String> currentMethodSet = new HashSet<>(this.currentWarnings.get(allocation));
            currentMethodSet.removeAll(missingWarnings.get(allocation));
            if (currentMethodSet.isEmpty()) {
                newWarningMap.remove(allocation);
            } else {
                newWarningMap.put(allocation, currentMethodSet);
            }
        }
        for (final Allocation allocation : addedWarnings.keySet()) {
            final Set<String> currentMethodSet = new HashSet<>(this.currentWarnings.get(allocation));
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
