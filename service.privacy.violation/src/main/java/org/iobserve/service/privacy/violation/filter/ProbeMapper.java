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
import java.util.List;
import java.util.Map;
import java.util.Set;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.eclipse.emf.common.util.EList;
import org.iobserve.analysis.deployment.DeploymentLock;
import org.iobserve.common.record.ObservationPoint;
import org.iobserve.model.correspondence.AllocationEntry;
import org.iobserve.model.correspondence.AssemblyEntry;
import org.iobserve.model.correspondence.ComponentEntry;
import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.DataTypeEntry;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.IModelResource;
import org.iobserve.model.persistence.neo4j.InvocationException;
import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.service.privacy.violation.exceptions.ControlEventCreationFailedException;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;
import org.iobserve.utility.tcp.events.TcpActivationControlEvent;
import org.iobserve.utility.tcp.events.TcpActivationParameterControlEvent;
import org.iobserve.utility.tcp.events.TcpDeactivationControlEvent;
import org.iobserve.utility.tcp.events.TcpUpdateParameterEvent;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.CollectionDataType;
import org.palladiosimulator.pcm.repository.CompositeDataType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.Parameter;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryComponent;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.system.System;

/**
 * Translate model level {@link ProbeManagementData} events to code level events. Gets real system
 * information through model entries.
 *
 *
 * @author Marc Adolf
 *
 */
public class ProbeMapper extends AbstractConsumerStage<ProbeManagementData> {
    private static final int PORT = 5791;

    private final IModelResource<CorrespondenceModel> correspondenceResource;
    private final IModelResource<Repository> repositoryResource;
    private final IModelResource<ResourceEnvironment> resourceEnvironmentResource;
    private final IModelResource<System> assemblyResource;
    private final IModelResource<Allocation> allocationResource;

    private final OutputPort<AbstractTcpControlEvent> outputPort = this.createOutputPort();

    /**
     * Initialize probe mapper from model to code level.
     *
     * @param correspondenceResource
     *            correspondence model resource
     * @param repositoryResource
     *            repository model resource
     * @param resourceEnvironmentResource
     *            resource environment model resource
     * @param assemblyResource
     *            assembly model resource
     * @param allocationResource
     *            allocation model resource
     */
    public ProbeMapper(final IModelResource<CorrespondenceModel> correspondenceResource,
            final IModelResource<Repository> repositoryResource,
            final IModelResource<ResourceEnvironment> resourceEnvironmentResource,
            final IModelResource<System> assemblyResource, final IModelResource<Allocation> allocationResource) {
        this.correspondenceResource = correspondenceResource;
        this.repositoryResource = repositoryResource;
        this.resourceEnvironmentResource = resourceEnvironmentResource;
        this.assemblyResource = assemblyResource;
        this.allocationResource = allocationResource;
    }

    public OutputPort<AbstractTcpControlEvent> getOutputPort() {
        return this.outputPort;
    }

    @Override
    protected void execute(final ProbeManagementData element) throws Exception {
        PrivacyExperimentLogger.measure(element, ObservationPoint.PROBE_MODEL_TO_CODE_ENTRY);
        DeploymentLock.lock();
        this.createMethodsToActivate(element);
        this.createMethodsToDeactivate(element);
        this.createMethodsToUpdate(element);
        DeploymentLock.unlock();
        PrivacyExperimentLogger.measure(element, ObservationPoint.PROBE_MODEL_TO_CODE_EXIT);
    }

    private void createMethodsToActivate(final ProbeManagementData element)
            throws ControlEventCreationFailedException, InvocationException, DBException {
        final Map<AllocationContext, Set<OperationSignature>> methodsToActivate = element.getMethodsToActivate();
        if (methodsToActivate != null) {
            this.logger.debug("methods to activate");
            for (final AllocationContext allocation : methodsToActivate.keySet()) {
                this.logger.debug("AllocationContext to activate {}", allocation.getEntityName());
                for (final OperationSignature operationSignature : methodsToActivate.get(allocation)) {
                    this.logger.debug("AllocationContext activate operation {}", operationSignature.getEntityName());
                    try {
                        final String pattern = this.computeAllocationComponentIdentifierPattern(allocation,
                                operationSignature);
                        this.logger.debug("AllocationContext activate operation {} -- {}",
                                operationSignature.getEntityName(), pattern);
                        final TcpActivationControlEvent currentEvent = this.createActivationEvent(pattern,
                                element.getTriggerTime(), element.getWhitelist());
                        this.fillTcpControlEvent(currentEvent, allocation);
                        this.outputPort.send(currentEvent);
                    } catch (final ControlEventCreationFailedException e) {
                        this.logger.error("Could not construct activation event for: " + operationSignature.toString(),
                                e);
                    }
                }
            }
        }
    }

    private void createMethodsToDeactivate(final ProbeManagementData element)
            throws ControlEventCreationFailedException, InvocationException, DBException {
        final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate = element.getMethodsToDeactivate();
        if (methodsToDeactivate != null) {
            this.logger.debug("methods to deactivate");
            for (final AllocationContext allocation : methodsToDeactivate.keySet()) {
                this.logger.debug("AllocationContext to deactivate {}", allocation.getEntityName());
                for (final OperationSignature operationSignature : methodsToDeactivate.get(allocation)) {
                    try {
                        final String pattern = this.computeAllocationComponentIdentifierPattern(allocation,
                                operationSignature);
                        this.logger.debug("AllocationContext deactivate operation {} -- {}",
                                operationSignature.getEntityName(), pattern);
                        // deactivation -> no parameters needed
                        final TcpDeactivationControlEvent currentEvent = new TcpDeactivationControlEvent(pattern,
                                element.getTriggerTime());
                        this.fillTcpControlEvent(currentEvent, allocation);
                        this.outputPort.send(currentEvent);
                    } catch (final ControlEventCreationFailedException e) {
                        this.logger.error(
                                "Could not construct deactivation event for: " + operationSignature.toString(), e);
                    }
                }
            }
        }
    }

    private void createMethodsToUpdate(final ProbeManagementData element)
            throws ControlEventCreationFailedException, InvocationException, DBException {
        final Map<AllocationContext, Set<OperationSignature>> methodsToUpdate = element.getMethodsToUpdate();
        if (methodsToUpdate != null && element.getWhitelist() != null) {
            for (final AllocationContext allocation : methodsToUpdate.keySet()) {
                this.logger.debug("AllocationContext to update {}", allocation.getEntityName());
                for (final OperationSignature operationSignature : methodsToUpdate.get(allocation)) {
                    try {
                        final String pattern = this.computeAllocationComponentIdentifierPattern(allocation,
                                operationSignature);
                        this.logger.debug("AllocationContext update operation {} -- {}",
                                operationSignature.getEntityName(), pattern);
                        final Map<String, List<String>> parameters = new HashMap<>();
                        parameters.put("whitelist", element.getWhitelist());
                        final TcpUpdateParameterEvent currentEvent = new TcpUpdateParameterEvent(pattern,
                                element.getTriggerTime(), parameters);
                        this.fillTcpControlEvent(currentEvent, allocation);
                        this.outputPort.send(currentEvent);
                    } catch (final ControlEventCreationFailedException e) {
                        this.logger.error("Could not construct activation event for: {} {}",
                                operationSignature.toString(), e.getLocalizedMessage());
                    }
                }
            }
        }
    }

    private void fillTcpControlEvent(final AbstractTcpControlEvent event, final AllocationContext allocation)
            throws InvocationException, DBException {
        final String ip = this.resourceEnvironmentResource.resolve(allocation.getResourceContainer_AllocationContext())
                .getEntityName();
        this.logger.debug("IP for the control event is: " + ip);
        final String hostname = allocation.getEntityName();

        event.setIp(ip);
        event.setHostname(hostname);
        // TODO dynamic port (currently not supported in the model)
        event.setPort(ProbeMapper.PORT);

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("IP is set to: " + ip + "; Hostname ist set to: " + hostname + "; Port is set to: "
                    + ProbeMapper.PORT);
        }

    }

    private String computeAllocationComponentIdentifierPattern(final AllocationContext allocation,
            final OperationSignature operationSignature)
            throws ControlEventCreationFailedException, InvocationException, DBException {
        final AllocationEntry entry = this.findAllocationEntry(allocation);

        switch (entry.getTechnology()) {
        case SERVLET:
            return this.computeAllocationComponentServletId(entry, allocation, operationSignature);
        case EJB:
        case ASPECT_J:
            return this.computeAllocationComponentJavaSignature(allocation, operationSignature);
        case DB:
            return this.computeAllocationComponentDBSignature(allocation, operationSignature);
        default:
            throw new InternalError("Technology " + entry.getTechnology().getLiteral() + " not supported.");
        }

    }

    private String computeAllocationComponentDBSignature(final AllocationContext allocation,
            final OperationSignature operationSignature) {
        return "db://" + allocation.getEntityName() + operationSignature.getEntityName();
    }

    /**
     * Find the corresponding AllocationEntry for an AllocationContext.
     *
     * @param allocation
     *            the allocation context
     * @return returns the AllocationEntry
     * @throws InvocationException
     * @throws DBException
     */
    private AllocationEntry findAllocationEntry(final AllocationContext allocation)
            throws InvocationException, DBException {
        final List<AllocationEntry> allocations = this.correspondenceResource
                .collectAllObjectsByType(AllocationEntry.class, CorrespondencePackage.Literals.ALLOCATION_ENTRY);

        for (final AllocationEntry entry : allocations) {
            final AllocationContext entryAllocation = this.allocationResource.resolve(entry.getAllocation());
            this.logger.debug("XXXXX entry id {} name {} / ac id {} name {}", entryAllocation.getId(),
                    entryAllocation.getEntityName(), allocation.getId(), allocation.getEntityName());
            if (entryAllocation.getId().equals(allocation.getId())) {
                return entry;
            }
        }

        throw new InternalError("Correspondence entry missing for " + allocation.getEntityName());
    }

    /**
     * Compute allocation component servlet id.
     *
     * @param entry
     * @param allocation
     * @param operationSignature
     * @return
     * @throws InvocationException
     * @throws DBException
     */
    private String computeAllocationComponentServletId(final AllocationEntry entry, final AllocationContext allocation,
            final OperationSignature operationSignature) throws InvocationException, DBException {
        final EList<Parameter> parameterList = operationSignature.getParameters__OperationSignature();
        String parameters = null;
        for (final Parameter parameter : parameterList) {
            if (parameters == null) {
                parameters = parameter.getParameterName();
            } else {
                parameters += ", " + parameter.getParameterName();
            }
        }

        AssemblyContext assemblyContext = this.assemblyResource
                .resolve(allocation.getAssemblyContext_AllocationContext());

        assemblyContext = this.assemblyResource.resolve(assemblyContext);

        String component = "ERROR";

        for (final AssemblyEntry assemblyEntry : this.correspondenceResource
                .collectAllObjectsByType(AssemblyEntry.class, CorrespondencePackage.Literals.ASSEMBLY_ENTRY)) {
            final AssemblyContext entryRelatedContext = this.assemblyResource.resolve(assemblyEntry.getAssembly());
            // this.logger.debug(entryRelatedContext.getId() + " is proxy? " +
            // entryRelatedContext.eIsProxy()
            // + " <- compared to -> " + assemblyContext.getId() + " is proxy? " +
            // assemblyContext.eIsProxy());
            if (entryRelatedContext.getId().equals(assemblyContext.getId())) {
                component = assemblyEntry.getImplementationId();
            }
        }

        final String methodId = String.format("%s.%s (%s)", component, operationSignature.getEntityName(), parameters);

        this.logger.debug("Constructed method string: {}", methodId);

        return methodId;
    }

    private String computeAllocationComponentJavaSignature(final AllocationContext allocation,
            final OperationSignature operationSignature)
            throws ControlEventCreationFailedException, InvocationException, DBException {
        // there are only interfaces on model level -> therefore only public methods (no
        // getModifiers available)
        final String modifier = "public";
        final DataType returnType = operationSignature.getReturnType__OperationSignature();
        final List<DataTypeEntry> dataTypeEntries = this.correspondenceResource
                .collectAllObjectsByType(DataTypeEntry.class, CorrespondencePackage.Literals.DATA_TYPE_ENTRY);
        final String codeLevelReturnType = this.getCodeLevelDataType(dataTypeEntries, returnType);

        final String methodSignature = operationSignature.getEntityName();
        // TODO parameters
        final String parameterString = "*";

        final AssemblyContext assemblyContext = this.assemblyResource
                .resolve(allocation.getAssemblyContext_AllocationContext());
        final RepositoryComponent repositoryComponent = this.repositoryResource
                .resolve(assemblyContext.getEncapsulatedComponent__AssemblyContext());

        final List<ComponentEntry> componentEntries = this.correspondenceResource
                .collectAllObjectsByType(ComponentEntry.class, CorrespondencePackage.Literals.COMPONENT_ENTRY);
        final String codeLevelComponentIdentifier = this.getCodeLevelComponent(componentEntries, repositoryComponent);

        final String completeMethodSignature = modifier + " " + codeLevelReturnType + " " + codeLevelComponentIdentifier
                + "." + methodSignature + "(" + parameterString + ")";

        this.logger.debug("Constructed method string: {}", completeMethodSignature);

        return completeMethodSignature;
    }

    private String getCodeLevelComponent(final List<ComponentEntry> componentEntries,
            final RepositoryComponent repositoryComponent) throws ControlEventCreationFailedException {
        for (final ComponentEntry component : componentEntries) {
            if (component.getComponent().equals(repositoryComponent)) {
                return component.getImplementationId();
            }
        }

        throw new ControlEventCreationFailedException(
                "No matching code level component entry was found for: " + repositoryComponent);
    }

    private String getCodeLevelDataType(final List<DataTypeEntry> dataTypes, final DataType dataType)
            throws ControlEventCreationFailedException {
        if (dataType == null) {
            return "void";
        }
        for (final DataTypeEntry currentDataType : dataTypes) {
            if (this.getTypeName(currentDataType.getDataTypeEntry()).equals(this.getTypeName(dataType))) {
                return currentDataType.getImplementationId();
            }
        }
        throw new ControlEventCreationFailedException("No matching code level data type was found for: " + dataType);
    }

    private String getTypeName(final DataType dataType) throws ControlEventCreationFailedException {
        if (dataType instanceof PrimitiveDataType) {
            return ((PrimitiveDataType) dataType).getType().getLiteral(); // oder name
        } else if (dataType instanceof CollectionDataType) {
            return ((CollectionDataType) dataType).getEntityName();
        } else if (dataType instanceof CompositeDataType) {
            return ((CompositeDataType) dataType).getEntityName();
        } else {
            throw new ControlEventCreationFailedException("Could not find name for data type: " + dataType);
        }

    }

    private TcpActivationControlEvent createActivationEvent(final String pattern, final long triggerTime,
            final List<String> whitelist) {
        if (whitelist == null) {
            return new TcpActivationControlEvent(pattern, triggerTime);
        } else {
            final Map<String, List<String>> parameters = new HashMap<>();
            parameters.put("whitelist", whitelist);
            return new TcpActivationParameterControlEvent(pattern, triggerTime, parameters);
        }
    }

}
