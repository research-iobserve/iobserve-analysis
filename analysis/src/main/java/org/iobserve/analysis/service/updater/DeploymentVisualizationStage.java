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
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import teetime.framework.AbstractConsumerStage;

import org.iobserve.analysis.deployment.data.PCMDeployedEvent;
import org.iobserve.analysis.service.util.Changelog;
import org.iobserve.analysis.service.util.SendHttpRequest;
import org.iobserve.analysis.sink.landscape.ServiceInstanceService;
import org.iobserve.analysis.sink.landscape.ServiceService;
import org.iobserve.model.persistence.neo4j.IModelProvider;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * This stage is triggered by an analysis deployment update.
 *
 * @author Reiner Jung
 * @author Josefine Wegert
 *
 */
public class DeploymentVisualizationStage extends AbstractConsumerStage<PCMDeployedEvent> {

    private final ServiceService serviceService = new ServiceService();
    private final ServiceInstanceService serviceinstanceService = new ServiceInstanceService();

    private final URL outputURL;
    private final String systemId;
    private final IModelProvider<ResourceContainer> resourceContainerModelProvider;
    private final IModelProvider<AssemblyContext> allocationModelProvider;

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
     */
    public DeploymentVisualizationStage(final URL outputURL, final String systemId,
            final IModelProvider<ResourceContainer> resourceContainerModelProvider,
            final IModelProvider<AssemblyContext> allocationModelProvider) {
        this.outputURL = outputURL;
        this.systemId = systemId;
        this.resourceContainerModelProvider = resourceContainerModelProvider;
        this.allocationModelProvider = allocationModelProvider;
    }

    @Override
    protected void execute(final PCMDeployedEvent deployment) throws Exception {
        SendHttpRequest.post(this.createData(deployment), this.outputURL);
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
    private JsonArray createData(final PCMDeployedEvent deployment) {
        final String serverName = deployment.getService();
        final String nodeId = this.resourceContainerModelProvider
                .getObjectsByTypeAndName(ResourceContainer.class, serverName).get(0).getId();

        final String asmContextName = deployment.getResourceContainer().getEntityName() + "_" + serverName;

        final List<AssemblyContext> contexts = this.allocationModelProvider
                .getObjectsByTypeAndName(AssemblyContext.class, asmContextName);
        final AssemblyContext assemblyContext = contexts.get(0);

        final JsonObject serviceObject = Changelog
                .create(this.serviceService.createService(assemblyContext, this.systemId));
        final JsonObject serviceinstanceObject = Changelog.create(this.serviceinstanceService
                .createServiceInstance(assemblyContext, this.systemId, nodeId, this.serviceService.getServiceId()));

        return Json.createArrayBuilder().add(serviceObject).add(serviceinstanceObject).build();
    }

}
