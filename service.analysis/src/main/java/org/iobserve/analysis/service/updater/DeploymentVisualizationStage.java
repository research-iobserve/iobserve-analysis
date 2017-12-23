/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.iobserve.analysis.deployment.DeploymentModelUpdater;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.service.services.ServiceInstanceService;
import org.iobserve.analysis.service.services.ServiceService;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.EJBDeployedEvent;
import org.iobserve.common.record.IDeploymentRecord;
import org.iobserve.common.record.ServletDeployedEvent;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import teetime.framework.AbstractConsumerStage;
import util.Changelog;
import util.SendHttpRequest;

/**
 * This stage is triggered by an analysis deployment update.
 *
 * @author Reiner Jung
 * @author jweg
 *
 */
public class DeploymentVisualizationStage extends AbstractConsumerStage<IDeploymentRecord> {

    private static final Logger LOGGER = LogManager.getLogger(DeploymentModelUpdater.class);

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
     * @param systemId
     *            system id
     * @param resourceContainerModelProvider
     *            model provider for the part of the resource environment model about resource
     *            container
     * @param allocationModelProvider
     *            model provider for the allocation model
     * @param correspondenceModel
     *            correspondence model
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

    /**
     * Collects information for creating a service and a service instance element in the
     * visualization. As the order of the changelogs matters, the elements are added to an array in
     * the right order.
     *
     * @param deployment
     *            servlet deployed event
     * @return array that contains changelogs for creating a service and a service instance
     */
    private JsonArray createData(final ServletDeployedEvent deployment) {

        final String serverName = deployment.getSerivce();
        final String context = deployment.getContext();
        final String nodeId = this.resourceContainerModelProvider
                .readOnlyComponentByName(ResourceContainer.class, serverName).get(0).getId();

        Opt.of(this.correspondenceModel.getCorrespondent(context)).ifPresent()
                .apply(correspondent -> this.entityName = correspondent.getPcmEntityName())
                .elseApply(() -> DeploymentVisualizationStage.LOGGER.info(
                        "This should not happen, because the service was created and the models were updated before."));

        final String asmContextName = this.entityName + "_" + serverName;
        final AssemblyContext assemblyContext = this.allocationModelProvider
                .readOnlyComponentByName(AssemblyContext.class, asmContextName).get(0);

        final JsonObject serviceObject = Changelog
                .create(this.serviceService.createService(assemblyContext, this.systemId));
        final JsonObject serviceinstanceObject = Changelog.create(this.serviceinstanceService
                .createServiceInstance(assemblyContext, this.systemId, nodeId, this.serviceService.getServiceId()));

        final JsonArray dataArray = Json.createArrayBuilder().add(serviceObject).add(serviceinstanceObject).build();
        return dataArray;
    }

    /**
     * Collects information for building a service and a service instance element for the
     * visualization. As the order of the changelogs matters, the elements are added to an array in
     * the right order.
     *
     * @param deployment
     *            ejb deployed event
     * @return array that contains changelogs for creating a service and a service instance
     */
    private JsonArray createData(final EJBDeployedEvent deployment) {

        final String serverName = deployment.getSerivce();
        final String context = deployment.getContext();
        final String nodeId = this.resourceContainerModelProvider
                .readOnlyComponentByName(ResourceContainer.class, serverName).get(0).getId();

        Opt.of(this.correspondenceModel.getCorrespondent(context)).ifPresent()
                .apply(correspondent -> this.entityName = correspondent.getPcmEntityName())
                .elseApply(() -> DeploymentVisualizationStage.LOGGER.info(
                        "This should not happen, because the service was created and the models were updated before."));

        final String asmContextName = this.entityName + "_" + serverName;
        final AssemblyContext assemblyContext = this.allocationModelProvider
                .readOnlyComponentByName(AssemblyContext.class, asmContextName).get(0);

        final JsonObject serviceObject = Changelog
                .create(this.serviceService.createService(assemblyContext, this.systemId));
        final JsonObject serviceinstanceObject = Changelog.create(this.serviceinstanceService
                .createServiceInstance(assemblyContext, this.systemId, nodeId, this.serviceService.getServiceId()));

        final JsonArray dataArray = Json.createArrayBuilder().add(serviceObject).add(serviceinstanceObject).build();
        return dataArray;
    }
}
