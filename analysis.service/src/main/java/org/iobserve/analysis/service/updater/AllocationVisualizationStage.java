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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.iobserve.analysis.service.services.NodeService;
import org.iobserve.analysis.service.services.NodegroupService;
import org.iobserve.common.record.ContainerAllocationEvent;
import org.iobserve.common.record.IAllocationRecord;
import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

import teetime.framework.AbstractConsumerStage;
import util.Changelog;
import util.SendHttpRequest;

/**
 * This stage is triggered by an analysis allocation update (resource container added) and updates
 * the deployment visualization by adding a node. Therefore it gets the information about the added
 * resource container from the resource environment model.
 *
 * @author jweg
 *
 */
public class AllocationVisualizationStage extends AbstractConsumerStage<IAllocationRecord> {

    /** services for creating visualization elements */
    private final NodegroupService nodegroupService = new NodegroupService();
    private final NodeService nodeService = new NodeService();

    /** constructor parameters */
    private final URL outputURL;
    private final String systemId;
    private final ModelProvider<ResourceContainer> resourceContainerModelProvider;

    /**
     * @param outputURL
     *            the output URL
     * @param systemId
     *            the system id
     * @param resourceEnvironmentModelProvider
     *            the resource environment model graph
     */
    public AllocationVisualizationStage(final URL outputURL, final String systemId,
            final ModelProvider<ResourceContainer> resourceContainerModelProvider) {
        this.outputURL = outputURL;
        this.resourceContainerModelProvider = resourceContainerModelProvider;
        this.systemId = systemId;
    }

    @Override
    protected void execute(final IAllocationRecord allocation) throws Exception {

        if (allocation instanceof ContainerAllocationEvent) {

            SendHttpRequest.post(this.createData(allocation), this.outputURL);
        }

    }

    /**
     * This method is triggered for every container allocation event.
     *
     * @param allocation
     *            the container allocation event
     * @return JsonArray holding necessary information to create a nodegroup and a node in the
     *         deployment visualization
     * @throws MalformedURLException
     */
    private JsonArray createData(final IAllocationRecord allocation) throws MalformedURLException {
        final URL url = new URL(allocation.toArray()[0].toString());
        final String hostname = url.getHost();

        final List<ResourceContainer> resourceContainerHostname = this.resourceContainerModelProvider
                .readOnlyComponentByName(ResourceContainer.class, hostname);

        final ResourceContainer resourceContainer = resourceContainerHostname.get(0);

        // Each node has its own nodegroup now. This can/should change in future.
        final JsonObject nodegroupObject = Changelog.create(this.nodegroupService.createNodegroup(this.systemId));
        final JsonObject nodeObject = Changelog.create(
                this.nodeService.createNode(resourceContainer, this.systemId, this.nodegroupService.getNodegroupId()));
        final JsonArray dataArray = Json.createArrayBuilder().add(nodegroupObject).add(nodeObject).build();

        return dataArray;
    }

}
