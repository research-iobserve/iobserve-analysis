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
import org.iobserve.model.persistence.neo4j.DBException;
import org.iobserve.model.persistence.neo4j.InvocationException;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;
import org.iobserve.utility.tcp.events.TcpActivationControlEvent;
import org.iobserve.utility.tcp.events.TcpActivationParameterControlEvent;
import org.iobserve.utility.tcp.events.TcpDeactivationControlEvent;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.repository.CollectionDataType;
import org.palladiosimulator.pcm.repository.CompositeDataType;
import org.palladiosimulator.pcm.repository.DataType;
import org.palladiosimulator.pcm.repository.OperationSignature;
import org.palladiosimulator.pcm.repository.PrimitiveDataType;
import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

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
            final ModelResource<ResourceEnvironment> resourceEnvironmentResource) {
        this.correspondenceResource = correspondenceResource;
        this.repositoryResource = repositoryResource;
        this.resourceEnvironmentResource = resourceEnvironmentResource;

    }

    @Override
    protected void execute(final ProbeManagementData element) throws Exception {
        final Map<AllocationContext, Set<OperationSignature>> methodsToActivate = element.getMethodsToActivate();
        if (!(methodsToActivate == null)) {
            for (final AllocationContext allocation : methodsToActivate.keySet()) {
                for (final OperationSignature operationSignature : methodsToActivate.get(allocation)) {
                    final String pattern = this.assembleCompleteMethodSignature(allocation, operationSignature);
                    final TcpActivationControlEvent currentEvent = this.createActivationEvent(pattern,
                            element.getWhitelist());
                    this.fillTcpControlEvent(currentEvent, allocation);
                    this.outputPort.send(currentEvent);
                }
            }
        }
        final Map<AllocationContext, Set<OperationSignature>> methodsToDeactivate = element.getMethodsToDeactivate();
        if (!(methodsToDeactivate == null)) {
            for (final AllocationContext allocation : methodsToDeactivate.keySet()) {
                for (final OperationSignature operationSignature : methodsToDeactivate.get(allocation)) {
                    final String pattern = this.assembleCompleteMethodSignature(allocation, operationSignature);
                    // deactivation -> no parameters needed
                    final TcpDeactivationControlEvent currentEvent = new TcpDeactivationControlEvent(pattern);
                    this.fillTcpControlEvent(currentEvent, allocation);
                    this.outputPort.send(currentEvent);
                }
            }
        }
    }

    public OutputPort<AbstractTcpControlEvent> getOutputPort() {
        return this.outputPort;
    }

    private void fillTcpControlEvent(final AbstractTcpControlEvent event, final AllocationContext allocation)
            throws InvocationException, DBException {
        // TODO resolve; entity name = ip?
        final String ip = this.resourceEnvironmentResource.resolve(allocation.getResourceContainer_AllocationContext())
                .getEntityName();
        this.logger.debug("IP for the control event is: " + ip);
        // TODO real hostname and dynamic port (currently not supported in the model)
        final String hostname = allocation.getEntityName();

        event.setIp(ip);
        event.setHostname(hostname);
        event.setPort(ProbeMapper.PORT);
    }

    private String assembleCompleteMethodSignature(final AllocationContext allocation,
            final OperationSignature operationSignature) {
        // there are only interfaces on model level -> therefore only public methods (no
        // getModifiers available)
        final String modifier = "public";
        final DataType returnType = operationSignature.getReturnType__OperationSignature();
        // TODO map model level returnType to code level returnType
        // TODO toString is not a good idea
        final List<DataTypeEntry> dataTypeEntries = this.correspondenceResource.collectAllObjectsByType(clazz, eClass);
        final String codeLevelReturnType = this.getCodeLevelReturnType(dataTypeEntries, returnType);

        final String methodSignature = operationSignature.getEntityName();
        // TODO parameters
        final String parameterString = "*";

        // TODO right identifier? and must resolve
        final String componentIdentifier = allocation.getAssemblyContext_AllocationContext()
                .getEncapsulatedComponent__AssemblyContext().getRepository__RepositoryComponent().getEntityName();

        // TODO map component identifier

        return modifier + " " + codeLevelReturnType + " " + componentIdentifier + "." + methodSignature + "("
                + parameterString + ")";
    }

    private String getCodeLevelReturnType(final List<DataTypeEntry> dataTypes, final DataType returnType) {
        for (final DataTypeEntry dataType : dataTypes) {
            if (dataType.getOperation().getEntityName().equals(this.getTypeName(returnType))) {
                return dataType.getImplementationId();
            }
        }
    }

    private String getTypeName(final DataType returnType) throws Exception {
        if (returnType instanceof PrimitiveDataType) {
            return ((PrimitiveDataType) returnType).getType().getLiteral(); // oder name
        } else if (returnType instanceof CollectionDataType) {
            return ((CollectionDataType) returnType).getEntityName();
        } else if (returnType instanceof CompositeDataType) {
            return ((CompositeDataType) returnType).getEntityName();
        } else {
            throw new Exception("XXXXX");
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
