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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.analysis.AnalysisExperimentLogging;
import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.common.record.EventTypes;
import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.InvocationException;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.service.privacy.violation.data.Warnings;
import org.iobserve.service.privacy.violation.transformation.analysisgraph.Edge;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.allocation.AllocationPackage;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.Interface;
import org.palladiosimulator.pcm.repository.OperationInterface;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.ProvidedRole;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.repository.RepositoryPackage;
import org.palladiosimulator.pcm.system.System;

/**
 * Model level controller for probes. The filter receives a list of warnings and computes a list of
 * all relevant probes to be updated. No information for (de-)activation of probes is given.
 *
 * @author Marc Adolf -- first complete class
 * @author Reiner Jung -- initial
 *
 */
public class NonAdaptiveModelProbeController extends AbstractConsumerStage<Warnings> {

    private final OutputPort<ProbeManagementData> outputPort = this.createOutputPort(ProbeManagementData.class);
    private final ModelResource<Allocation> allocationResource;
    private final ModelResource<System> systemModelResource;
    private final ModelResource<Repository> repositoryResource;

    /**
     * Create an initialize the model probe controller.
     */
    public NonAdaptiveModelProbeController(final ModelResource<Allocation> allocationResource,
            final ModelResource<System> systemModelResource, final ModelResource<Repository> repositoryResource) {
        this.allocationResource = allocationResource;
        this.systemModelResource = systemModelResource;
        this.repositoryResource = repositoryResource;
    }

    @Override
    protected void execute(final Warnings element) throws Exception {
        if (element.getEvent() instanceof PCMDeployedEvent) {
            AnalysisExperimentLogging.logEvent(element.getEvent().getTimestamp(), EventTypes.DEPLOYMENT,
                    "compute-probe-configuration");
        } else {
            AnalysisExperimentLogging.logEvent(element.getEvent().getTimestamp(), EventTypes.UNDEPLOYMENT,
                    "compute-probe-configuration");
        }

        final Map<AllocationContext, Set<OperationSignature>> receivedWarnings = this.computeReceivedWarnings(element);

        // no probe should be changed
        final ProbeManagementData probeMethodInformation = new ProbeManagementData(
                new HashMap<AllocationContext, Set<OperationSignature>>(),
                new HashMap<AllocationContext, Set<OperationSignature>>());

        final Map<AllocationContext, Set<OperationSignature>> methodsToUpdate = this.computeAvailableProbes();

        probeMethodInformation.setWarnedMethods(receivedWarnings);
        probeMethodInformation.setMethodsToUpdate(methodsToUpdate);
        this.outputPort.send(probeMethodInformation);
    }

    private Map<AllocationContext, Set<OperationSignature>> computeReceivedWarnings(final Warnings element) {
        final Map<AllocationContext, Set<OperationSignature>> receivedWarnings = new HashMap<>();

        if (!(element.getWarningEdges() == null) || element.getWarningEdges().isEmpty()) {

            for (final Edge edge : element.getWarningEdges()) {
                // multiple methods per allocation possible
                final AllocationContext allocation = edge.getSource().getAllocationContext();
                Set<OperationSignature> methodSignatures = receivedWarnings.get(allocation);
                // if not present, add new entry
                if (methodSignatures == null) {
                    methodSignatures = new HashSet<>();
                }
                if (edge.getOperationSignature() != null) {
                    methodSignatures.add(edge.getOperationSignature());
                    receivedWarnings.put(allocation, methodSignatures);
                } else {
                    this.logger.debug("Recevied warning without operation signature");
                }

            }
        }
        return receivedWarnings;
    }

    private Map<AllocationContext, Set<OperationSignature>> computeAvailableProbes() throws DBException {

        final Repository repositoryModel = this.repositoryResource.getModelRootNode(Repository.class,
                RepositoryPackage.Literals.REPOSITORY);

        final List<AllocationContext> allocations = this.allocationResource
                .collectAllObjectsByType(AllocationContext.class, AllocationPackage.Literals.ALLOCATION_CONTEXT);
        final Map<AllocationContext, Set<OperationSignature>> availableProbes = new HashMap<>();
        this.logger.debug("Found " + allocations.size() + " allocation context entries");

        for (final AllocationContext allocation : allocations) {
            final Set<OperationSignature> allocationMethods = new LinkedHashSet<>();

            try {
                final AssemblyContext assemblyContext = this.systemModelResource
                        .resolve(allocation.getAssemblyContext_AllocationContext());
                final RepositoryComponent component = this.repositoryResource
                        .resolve(assemblyContext.getEncapsulatedComponent__AssemblyContext());

                final List<ProvidedRole> providingRoles = component.getProvidedRoles_InterfaceProvidingEntity();

                for (final ProvidedRole providedRole : providingRoles) {
                    this.logger.debug("Providing roles: " + providingRoles);
                    final String roleName = providedRole.getEntityName();
                    for (final Interface iface : repositoryModel.getInterfaces__Repository()) {
                        if (iface instanceof OperationInterface) {
                            if (roleName.contains(iface.getEntityName())) {
                                this.logger.debug("Matching operation interfaces: "
                                        + ((OperationInterface) iface).getSignatures__OperationInterface());
                                // found interface
                                allocationMethods
                                        .addAll(((OperationInterface) iface).getSignatures__OperationInterface());
                            }
                        }
                    }
                }

            } catch (InvocationException | DBException e) {
                this.logger.error(
                        "Could not resolve elements of the PCM during the computation of the available probes", e);
            }
            availableProbes.put(allocation, allocationMethods);
        }

        return availableProbes;

    }

    public OutputPort<ProbeManagementData> getOutputPort() {
        return this.outputPort;
    }

}
