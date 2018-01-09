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

import teetime.framework.AbstractConsumerStage;

import org.iobserve.analysis.deployment.data.PCMUndeployedEvent;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.service.services.ServiceInstanceService;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import util.Changelog;
import util.SendHttpRequest;

/**
 * This stage is triggered by an analysis undeployment update.
 *
 * @author Josefine Wegert
 * @author Reiner Jung
 *
 */
public class UndeploymentVisualizationStage extends AbstractConsumerStage<PCMUndeployedEvent> {

    private final ServiceInstanceService serviceInstanceService = new ServiceInstanceService();

    private final URL outputURL;
    private final String systemId;
    private final ModelProvider<ResourceContainer> resourceContainerModelGraphProvider;
    private final ModelProvider<AssemblyContext> assemblyContextModelGraphProvider;
    private final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider;

    /**
     * Output visualization configuration.
     *
     * @param outputURL
     *            the output URL
     * @param systemId
     *            system id
     * @param resourceContainerModelGraphProvider
     *            model provider for the part of the resource environment model about resource
     *            container
     * @param assemblyContextModelGraphProvider
     *            model provider for the part of the resource environment model about assembly
     *            context
     * @param systemModelGraphProvider
     *            provider for system model
     */
    public UndeploymentVisualizationStage(final URL outputURL, final String systemId,
            final ModelProvider<ResourceContainer> resourceContainerModelGraphProvider,
            final ModelProvider<AssemblyContext> assemblyContextModelGraphProvider,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider) {
        this.outputURL = outputURL;
        this.systemId = systemId;
        this.resourceContainerModelGraphProvider = resourceContainerModelGraphProvider;
        this.assemblyContextModelGraphProvider = assemblyContextModelGraphProvider;
        this.systemModelGraphProvider = systemModelGraphProvider;
    }

    @Override
    protected void execute(final PCMUndeployedEvent undeployment) throws Exception {
        SendHttpRequest.post(this.createData(undeployment), this.outputURL);
    }

    /**
     ** Collects information for deleting a service instance element in the visualization.
     *
     * @param undeployment
     *            servlet undeployed event
     * @return array that contains a changelog for deleting a service instance
     */
    private JsonArray createData(final PCMUndeployedEvent undeployment) {

        final String serverName = undeployment.getService();

        final String nodeId = this.resourceContainerModelGraphProvider
                .readOnlyComponentByName(ResourceContainer.class, serverName).get(0).getId();

        final String asmContextName = undeployment.getResourceContainer().getEntityName() + "_" + serverName;
        final AssemblyContext assemblyContext = this.assemblyContextModelGraphProvider
                .readOnlyComponentByName(AssemblyContext.class, asmContextName).get(0);

        final JsonObject serviceInstanceObject = Changelog.delete(this.serviceInstanceService
                .deleteServiceInstance(assemblyContext, this.systemId, nodeId, this.systemModelGraphProvider));
        final JsonArray dataArray = Json.createArrayBuilder().add(serviceInstanceObject).build();

        return dataArray;
    }

}
