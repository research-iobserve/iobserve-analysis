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
import org.iobserve.analysis.filter.TDeployment;
import org.iobserve.analysis.model.correspondence.ICorrespondence;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.service.services.ServiceInstanceService;
import org.iobserve.analysis.utils.Opt;
import org.iobserve.common.record.EJBUndeployedEvent;
import org.iobserve.common.record.IUndeploymentRecord;
import org.iobserve.common.record.ServletUndeployedEvent;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import teetime.framework.AbstractConsumerStage;
import util.Changelog;
import util.SendHttpRequest;

/**
 * This stage is triggered by an analysis undeployment update.
 *
 * @author jweg
 *
 */
public class UndeploymentVisualizationStage extends AbstractConsumerStage<IUndeploymentRecord> {

    private static final Logger LOGGER = LogManager.getLogger(TDeployment.class);

    private final ServiceInstanceService serviceinstanceService = new ServiceInstanceService();

    private final URL outputURL;
    private final String systemId;
    private final ModelProvider<ResourceContainer> resourceContainerModelGraphProvider;
    private final ModelProvider<AssemblyContext> assemblyContextModelGraphProvider;
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;
    private final ICorrespondence correspondenceModel;

    private String entityName;

    /**
     *
     * @param outputURL
     * @param systemId
     * @param resourceContainerModelGraphProvider
     * @param assemblyContextModelGraphProvider
     * @param systemModelGraphProvider
     * @param correspondenceModel
     */
    public UndeploymentVisualizationStage(final URL outputURL, final String systemId,
            final ModelProvider<ResourceContainer> resourceContainerModelGraphProvider,
            final ModelProvider<AssemblyContext> assemblyContextModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider,
            final ICorrespondence correspondenceModel) {
        this.outputURL = outputURL;
        this.systemId = systemId;
        this.resourceContainerModelGraphProvider = resourceContainerModelGraphProvider;
        this.assemblyContextModelGraphProvider = assemblyContextModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
        this.correspondenceModel = correspondenceModel;
    }

    @Override
    protected void execute(final IUndeploymentRecord undeployment) throws Exception {
        if (undeployment instanceof ServletUndeployedEvent) {
            SendHttpRequest.post(this.createData((ServletUndeployedEvent) undeployment), this.outputURL);
        } else if (undeployment instanceof EJBUndeployedEvent) {
            SendHttpRequest.post(this.createData((EJBUndeployedEvent) undeployment), this.outputURL);
        }

    }

    /**
     *
     * @param undeployment
     * @return
     */
    private JsonArray createData(final ServletUndeployedEvent undeployment) {

        final String serverName = undeployment.getSerivce();
        final String context = undeployment.getContext();
        final String nodeId = this.resourceContainerModelGraphProvider
                .readOnlyComponentByName(ResourceContainer.class, serverName).get(0).getId();

        Opt.of(this.correspondenceModel.getCorrespondent(context)).ifPresent()
                .apply(correspondent -> this.entityName = correspondent.getPcmEntityName())
                .elseApply(() -> UndeploymentVisualizationStage.LOGGER.info(
                        "This should not happen, because the service was created and the models were updated before."));
        final String asmContextName = this.entityName + "_" + serverName;
        final AssemblyContext assemblyContext = this.assemblyContextModelGraphProvider
                .readOnlyComponentByName(AssemblyContext.class, asmContextName).get(0);

        final JsonObject serviceInstanceObject = Changelog.delete(this.serviceinstanceService
                .deleteServiceInstance(assemblyContext, this.systemId, nodeId, this.systemModelGraphProvider));
        final JsonArray dataArray = Json.createArrayBuilder().add(serviceInstanceObject).build();
        return dataArray;
    }

    /**
     *
     * @param undeployment
     * @return
     */
    private JsonArray createData(final EJBUndeployedEvent undeployment) {

        final String serverName = undeployment.getSerivce();
        final String context = undeployment.getContext();
        final String nodeId = this.resourceContainerModelGraphProvider
                .readOnlyComponentByName(ResourceContainer.class, serverName).get(0).getId();

        Opt.of(this.correspondenceModel.getCorrespondent(context)).ifPresent()
                .apply(correspondent -> this.entityName = correspondent.getPcmEntityName())
                .elseApply(() -> UndeploymentVisualizationStage.LOGGER.info(
                        "This should not happen, because the service was created and the models were updated before."));

        final String asmContextName = this.entityName + "_" + serverName;
        final AssemblyContext assemblyContext = this.assemblyContextModelGraphProvider
                .readOnlyComponentByName(AssemblyContext.class, asmContextName).get(0);

        // final JsonObject serviceObject = this.serviceService.createService(assemblyContext,
        // this.systemId);
        final JsonObject serviceInstanceObject = Changelog.delete(this.serviceinstanceService
                .deleteServiceInstance(assemblyContext, this.systemId, nodeId, this.systemModelGraphProvider));
        final JsonArray dataArray = Json.createArrayBuilder().add(serviceInstanceObject).build();
        return dataArray;
    }

}
