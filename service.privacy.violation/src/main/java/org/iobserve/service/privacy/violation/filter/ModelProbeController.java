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

import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.service.privacy.violation.data.IProbeManagement;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.iobserve.stages.data.Warnings;
import org.palladiosimulator.pcm.allocation.Allocation;

/**
 * Model level controller for probes. The filter receives a list of warnings and computes a list of
 * probe activation and deactivation. Therefore, it requires internal knowledge of previous probe
 * activations.
 *
 * @author Reiner Jung -- initial
 *
 */
public class ModelProbeController extends AbstractConsumerStage<Warnings> {

    private final OutputPort<IProbeManagement> outputPort = this.createOutputPort(IProbeManagement.class);
    private final ModelResource allocationModelResource;
    private final ModelResource systemModelResource;
    private final ModelResource resourceEnvironmentModelResource;

    // Allocation and list of method names
    private final Map<Allocation, Set<String>> currentWarnings = new HashMap<>();
    private Map<Allocation, Set<String>> missingWarnings = new HashMap<>();
    private Map<Allocation, Set<String>> addedWarnings = new HashMap<>();

    /**
     * Create an initialize the model probe controller.
     *
     * @param allocationModelResource
     *            allocation model provider
     * @param systemModelResource
     *            system model provider
     * @param resourceEnvironmentModelResource
     *            resource environment model provider
     */
    public ModelProbeController(final ModelResource allocationModelResource, final ModelResource systemModelResource,
            final ModelResource resourceEnvironmentModelResource) {
        this.allocationModelResource = allocationModelResource;
        this.systemModelResource = systemModelResource;
        this.resourceEnvironmentModelResource = resourceEnvironmentModelResource;
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
        }
        this.computeWarningDifferences(receivedWarnings);

        // TODO
        this.computeNewWarningMap();
    }

    private void computeWarningDifferences(final Map<Allocation, Set<String>> receivedWarnings) {
        this.missingWarnings = new HashMap<>(this.currentWarnings);
        this.addedWarnings = new HashMap<>(this.currentWarnings);
        final Map<Allocation, Set<String>> newAllocationWarnings = new HashMap<>(receivedWarnings);
        // already existing allocations
        for (final Allocation allocation : this.currentWarnings.keySet()) {
            final Set<String> newMethodSet = receivedWarnings.get(allocation);
            final Set<String> currentMethods = this.currentWarnings.get(allocation);
            // no new allocation -> all old methods have to be deactivated
            if (newMethodSet == null) {
                this.missingWarnings.put(allocation, currentMethods);
            } else {
                // current - new = missing methods = deactivate
                final Set<String> missingMethods = new HashSet<>(currentMethods);
                missingMethods.removeAll(newMethodSet);
                // new - current = added methods = activate
                final Set<String> addedMethods = new HashSet<>(newMethodSet);
                addedMethods.removeAll(currentMethods);
                if (!missingMethods.isEmpty()) {
                    this.missingWarnings.put(allocation, missingMethods);
                }
                if (!addedMethods.isEmpty()) {
                    this.addedWarnings.put(allocation, addedMethods);
                }
            }

            // the remaining entries are completly new warnings and allocations
            newAllocationWarnings.remove(allocation);

        }

        this.addedWarnings.putAll(newAllocationWarnings);

    }

    private void computeNewWarningMap() {
        // TODO Auto-generated method stub

    }

    public OutputPort<IProbeManagement> getOutputPort() {
        return this.outputPort;
    }

}
