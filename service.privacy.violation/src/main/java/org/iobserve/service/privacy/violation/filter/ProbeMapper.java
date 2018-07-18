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

import java.util.Map;
import java.util.Set;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.iobserve.model.correspondence.CorrespondenceModel;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.iobserve.service.privacy.violation.data.ProbeManagementData;
import org.iobserve.utility.tcp.events.AbstractTcpControlEvent;
import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.repository.OperationSignature;

/**
 * Translate model level {@link ProbeManagementData} events to code level events. Gets real system
 * information through model entries.
 *
 *
 * @author Marc Adolf
 *
 */
public class ProbeMapper extends AbstractConsumerStage<ProbeManagementData> {

    private final OutputPort<AbstractTcpControlEvent> outputPort = this.createOutputPort();

    /**
     * Initialize probe mapper from model to code level.
     *
     * @param rac
     *            correspondence model used for mapping
     */
    public ProbeMapper(final ModelResource<CorrespondenceModel> correspondenceModelResource) {
    }

    @Override
    protected void execute(final ProbeManagementData element) throws Exception {
        final Map<AllocationContext, Set<OperationSignature>> methodsToActivate = element.getMethodsToActivate();
        for (final AllocationContext allocation : methodsToActivate.keySet()) {
            if (allocation.eIsProxy()) {
                this.logger.debug("is proxy");
            }
        }
    }

    public OutputPort<AbstractTcpControlEvent> getOutputPort() {
        return this.outputPort;
    }

    private String assembleTcpInformation(final AllocationContext allocation) {
        // TODO resolve; entity name = ip
        allocation.getResourceContainer_AllocationContext().getEntityName();
        // TODO
        return null;
    }

    private String assembleCompleteMethodSignature(final AllocationContext allocation,
            final OperationSignature operationSignature) {
        // there are only interfaces on model level -> therefore only public methods (no
        // getModifiers available)
        final String modifier = "public";
        final String returnType = operationSignature.getReturnType__OperationSignature().toString();
        final String methodSignature = operationSignature.getEntityName();
        // TODO parameters
        final String parameterString = "*";

        // TODO component of method -> x.x.x.method
        final String componentIdentifier = allocation.getAssemblyContext_AllocationContext()
                .getEncapsulatedComponent__AssemblyContext().getId();

        return modifier + " " + returnType + " " + componentIdentifier + "." + methodSignature + "(" + parameterString
                + ")";

    }

}
