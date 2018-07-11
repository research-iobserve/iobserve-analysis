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
import org.iobserve.analysis.service.util.Changelog;
import org.iobserve.analysis.service.util.SendHttpRequest;
import org.iobserve.analysis.sink.landscape.ServiceInstanceService;
import org.iobserve.model.persistence.neo4j.ModelResource;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

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
    private final ModelResource resourceContainerModelGraphProvider;
    private final ModelResource assemblyContextModelGraphProvider;
    private final ModelResource systemModelGraphProvider;

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
            final ModelResource resourceContainerModelGraphProvider,
            final ModelResource assemblyContextModelGraphProvider, final ModelResource systemModelGraphProvider) {
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
                .findObjectsByTypeAndName(ResourceContainer.class, "entityName", serverName).get(0).getId();

        final String asmContextName = undeployment.getResourceContainer().getEntityName() + "_" + serverName;
        final AssemblyContext assemblyContext = this.assemblyContextModelGraphProvider
                .findObjectsByTypeAndName(AssemblyContext.class, "entityName", asmContextName).get(0);

        final JsonObject serviceInstanceObject = Changelog.delete(this.serviceInstanceService
                .deleteServiceInstance(assemblyContext, this.systemId, nodeId, this.systemModelGraphProvider));
        return Json.createArrayBuilder().add(serviceInstanceObject).build();
    }

}
