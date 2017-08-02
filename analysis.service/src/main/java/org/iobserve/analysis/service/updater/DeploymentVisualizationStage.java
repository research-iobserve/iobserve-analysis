/***************************************************************************
 * Copyright (C) 2016 iObserve Project (https://www.iobserve-devops.net)
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
package org.iobserve.analysis.service.updater;

import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.service.services.ServiceService;
import org.iobserve.analysis.service.services.ServiceInstanceService;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import teetime.framework.AbstractConsumerStage;
import util.SendHttpRequest;

/**
 * This stage is triggered by an analysis deployment update.
 *
 * @author Reiner Jung
 * @author jweg
 *
 */
public class DeploymentVisualizationStage extends AbstractConsumerStage<IDeploymentRecord> {

    private final ServiceService serviceService = new ServiceService();
    private final ServiceInstanceService serviceinstanceService = new ServiceInstanceService();

    private final URL outputURL;
    private final String systemId;
    private final ModelProvider<ResourceContainer> resourceContainerModelProvider;
    private final ModelProvider<AssemblyContext> allocationModelProvider;
    private final ICorrespondence correspondenceModel;

    private String entityName;

    /**
     * Output visualization configuration.
     *
     * @param outputURL
     *            the output URL
     */
    public DeploymentVisualizationStage(final URL outputURL, final String systemId,
            final ModelProvider<ResourceContainer> resourceContainerModelProvider,
            final ModelProvider<AssemblyContext> allocationModelProvider, final ICorrespondence correspondenceModel) {
        this.outputURL = outputURL;
        this.systemId = systemId;
        this.resourceContainerModelProvider = resourceContainerModelProvider;
        this.allocationModelProvider = allocationModelProvider;
        this.correspondenceModel = correspondenceModel;
    }

    @Override
    protected void execute(final IDeploymentRecord deployment) throws Exception {
        if (deployment instanceof ServletDeployedEvent) {
            SendHttpRequest.post(this.createData((ServletDeployedEvent) deployment), this.outputURL);
        } else if (deployment instanceof EJBDeployedEvent) {
            SendHttpRequest.post(this.createData((EJBDeployedEvent) deployment), this.outputURL);
        }

    }

    private JsonArray createData(final ServletDeployedEvent deployment) {

        final String serverName = deployment.getSerivce();
        final String context = deployment.getContext();
        final String nodeId = this.resourceContainerModelProvider
                .readOnlyComponentByName(ResourceContainer.class, serverName).get(0).getId();

        Opt.of(this.correspondenceModel.getCorrespondent(context)).ifPresent()
                .apply(correspondent -> this.entityName = correspondent.getPcmEntityName())
                .elseApply(() -> System.out.printf(
                        "This should not happen, because the service was created and the models were updated before."));
        final String asmContextName = this.entityName + "_" + serverName;
        final AssemblyContext assemblyContext = this.allocationModelProvider
                .readOnlyComponentByName(AssemblyContext.class, asmContextName).get(0);

        final JsonObject serviceObject = this.serviceService.createService(assemblyContext, this.systemId);
        final JsonObject serviceinstanceObject = this.serviceinstanceService.createServiceInstance(assemblyContext,
                this.systemId, nodeId, this.serviceService.getServiceId());
        final JsonArray dataArray = Json.createArrayBuilder().add(serviceObject).add(serviceinstanceObject).build();
        return dataArray;
    }

    private JsonArray createData(final EJBDeployedEvent deployment) {

        final String serverName = deployment.getSerivce();
        final String context = deployment.getContext();
        final String nodeId = this.resourceContainerModelProvider
                .readOnlyComponentByName(ResourceContainer.class, serverName).get(0).getId();

        Opt.of(this.correspondenceModel.getCorrespondent(context)).ifPresent()
                .apply(correspondent -> this.entityName = correspondent.getPcmEntityName())
                .elseApply(() -> System.out.printf(
                        "This should not happen, because the service was created and the models were updated before."));

        final String asmContextName = this.entityName + "_" + serverName;
        final AssemblyContext assemblyContext = this.allocationModelProvider
                .readOnlyComponentByName(AssemblyContext.class, asmContextName).get(0);

        final JsonObject serviceObject = this.serviceService.createService(assemblyContext, this.systemId);
        final JsonObject serviceinstanceObject = this.serviceinstanceService.createServiceInstance(assemblyContext,
                this.systemId, nodeId, this.serviceService.getServiceId());
        final JsonArray dataArray = Json.createArrayBuilder().add(serviceObject).add(serviceinstanceObject).build();
        return dataArray;
    }
}
