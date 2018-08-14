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

import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.correspondence.CorrespondencePackage;
import org.iobserve.model.correspondence.DataTypeEntry;
import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.InvocationException;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.service.privacy.exceptions.ControlEventCreationFailedException;
import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;
import org.iobserve.utility.tcp.events.TcpActivationControlEvent;
import org.iobserve.utility.tcp.events.TcpActivationParameterControlEvent;
import org.iobserve.utility.tcp.events.TcpDeactivationControlEvent;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.repository.CollectionDataType;
import org.palladiosimulator.pcm.repository.CompositeDataType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.OperationSignature;
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

    private final ModelResource<CorrespondenceModel> correspondenceResource;
    private final ModelResource<Repository> repositoryResource;
    private final ModelResource<ResourceEnvironment> resourceEnvironmentResource;
    private final ModelResource<System> assemblyResource;

    private final OutputPort<AbstractTcpControlEvent> outputPort = this.createOutputPort();

    /**
     * Initialize probe mapper from model to code level.
     *
     * @param correspondenceResource
     * @param repositoryResource
     * @param resourceEnvironmentResource
     */
    public ProbeMapper(final ModelResource<CorrespondenceModel> correspondenceResource,
            final ModelResource<Repository> repositoryResource,
            final ModelResource<ResourceEnvironment> resourceEnvironmentResource,
            final ModelResource<System> assemblyResource) {
        this.correspondenceResource = correspondenceResource;
        this.repositoryResource = repositoryResource;
        this.resourceEnvironmentResource = resourceEnvironmentResource;
        this.assemblyResource = assemblyResource;

    }

    @Override
    protected void execute(final ProbeManagementData element) throws Exception {
        final Map<AllocationContext, Set<OperationSignature>> methodsToActivate = element.getMethodsToActivate();
        if (!(methodsToActivate == null)) {
            for (final AllocationContext allocation : methodsToActivate.keySet()) {
                for (final OperationSignature operationSignature : methodsToActivate.get(allocation)) {
                    try {
                        final String pattern = this.assembleCompleteMethodSignature(allocation, operationSignature);
                        final TcpActivationControlEvent currentEvent = this.createActivationEvent(pattern,
                                element.getWhitelist());
                        this.fillTcpControlEvent(currentEvent, allocation);
                        this.outputPort.send(currentEvent);
                    } catch (final ControlEventCreationFailedException e) {
                        this.logger.error("Could not construct activation event for: " + operationSignature.toString(),
                                e);
                    }
                }
            }
        }
        final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate = element.getMethodsToDeactivate();
        if (!(methodsToDeactivate == null)) {
            for (final AllocationContext allocation : methodsToDeactivate.keySet()) {
                for (final OperationSignature operationSignature : methodsToDeactivate.get(allocation)) {
                    try {
                        final String pattern = this.assembleCompleteMethodSignature(allocation, operationSignature);
                        // deactivation -> no parameters needed
                        final TcpDeactivationControlEvent currentEvent = new TcpDeactivationControlEvent(pattern);
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

    public OutputPort<AbstractTcpControlEvent> getOutputPort() {
        return this.outputPort;
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

    private String assembleCompleteMethodSignature(final AllocationContext allocation,
            final OperationSignature operationSignature) throws Exception {
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
        final String codeLevelComponentIdentifier = repositoryComponent.getEntityName();

        final String completeMethodSignature = modifier + " " + codeLevelReturnType + " " + codeLevelComponentIdentifier
                + "." + methodSignature + "(" + parameterString + ")";

        if (this.logger.isDebugEnabled()) {
            this.logger.debug("Constructed method string: " + completeMethodSignature);
        }

        return completeMethodSignature;
    }

    private String getCodeLevelDataType(final List<DataTypeEntry> dataTypes, final DataType dataType)
            throws ControlEventCreationFailedException {
        if (dataType == null) {
            return "void";
        }
        for (final DataTypeEntry currentDataType : dataTypes) {
            if ((this.getTypeName(currentDataType.getDataTypeEntry()).equals(this.getTypeName(dataType)))) {
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

    private TcpActivationControlEvent createActivationEvent(final String pattern, final List<String> whitelist) {
        if (whitelist == null) {
            return new TcpActivationControlEvent(pattern);
        } else {
            final Map<String, List<String>> parameters = new HashMap<>();
            parameters.put("whitelist", whitelist);
            return new TcpActivationParameterControlEvent(pattern, parameters);
        }
    }

}
