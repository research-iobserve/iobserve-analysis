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
import org.iobserve.analysis.service.util.ChangelogHelper;
import org.iobserve.analysis.service.util.SendHttpRequestUtils;
import org.iobserve.analysis.sink.landscape.ServiceInstanceService;
import org.iobserve.analysis.sink.landscape.ServiceService;
import org.iobserve.model.persistence.DBException;
import org.iobserve.model.persistence.neo4j.Neo4JModelResource;
import org.palladiosimulator.pcm.allocation.Allocation;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.core.composition.CompositionPackage;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;
import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;
import org.palladiosimulator.pcm.resourceenvironment.ResourceenvironmentPackage;

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
    private final Neo4JModelResource<ResourceEnvironment> resourceEnvironmentModelResource;
    private final Neo4JModelResource<Allocation> allocationModelProvider;

    /**
     * Output visualization configuration.
     *
     * @param outputURL
     *            the output URL
     * @param systemId
     *            system id
     * @param resourceEnvironmentModelResource
     *            model provider for the part of the resource environment model about resource
     *            container
     * @param allocationModelProvider
     *            model provider for the allocation model
     */
    public DeploymentVisualizationStage(final URL outputURL, final String systemId,
            final Neo4JModelResource<ResourceEnvironment> resourceEnvironmentModelResource,
            final Neo4JModelResource<Allocation> allocationModelProvider) {
        this.outputURL = outputURL;
        this.systemId = systemId;
        this.resourceEnvironmentModelResource = resourceEnvironmentModelResource;
        this.allocationModelProvider = allocationModelProvider;
    }

    @Override
    protected void execute(final PCMDeployedEvent deployment) throws Exception {
        SendHttpRequestUtils.post(this.createData(deployment), this.outputURL);
    }

    /**
     * Collects information for creating a service and a service instance element in the
     * visualization. As the order of the changelogs matters, the elements are added to an array in
     * the right order.
     *
     * @param deployment
     *            servlet deployed event
     * @return array that contains changelogs for creating a service and a service instance
     * @throws DBException
     */
    private JsonArray createData(final PCMDeployedEvent deployment) throws DBException {
        final String serverName = deployment.getService();
        final String nodeId = this.resourceEnvironmentModelResource
                .findObjectsByTypeAndProperty(ResourceContainer.class,
                        ResourceenvironmentPackage.Literals.RESOURCE_CONTAINER, "entityName", serverName)
                .get(0).getId();

        final String asmContextName = deployment.getResourceContainer().getEntityName() + "_" + serverName;

        final List<AssemblyContext> contexts = this.allocationModelProvider.findObjectsByTypeAndProperty(
                AssemblyContext.class, CompositionPackage.Literals.ASSEMBLY_CONTEXT, "entityName", asmContextName);
        final AssemblyContext assemblyContext = contexts.get(0);

        final JsonObject serviceObject = ChangelogHelper
                .create(this.serviceService.createService(assemblyContext, this.systemId));
        final JsonObject serviceinstanceObject = ChangelogHelper.create(this.serviceinstanceService
                .createServiceInstance(assemblyContext, this.systemId, nodeId, this.serviceService.getServiceId()));

        return Json.createArrayBuilder().add(serviceObject).add(serviceinstanceObject).build();
    }

}
