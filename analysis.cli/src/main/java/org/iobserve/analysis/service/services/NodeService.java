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
package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 * This class prepares data such that the visualization element node is created. It has to be added
 * to a changelog in order to be send to the deployment visualization.
 *
 * @author jweg
 *
 */
public class NodeService {

    private String nodeId;
    private String hostname;

    /**
     * empty default constructor.
     */
    public NodeService() {

    }

    /**
     * Builds data for creating a node for the deployment visualization.
     *
     * @param resourceContainer
     *            resource container
     * @param systemId
     *            system id
     * @param nodegroupId
     *            node group id
     * @return JsonObject for creating a node
     */
    public JsonObject createNode(final ResourceContainer resourceContainer, final String systemId,
            final String nodegroupId) {
        this.nodeId = resourceContainer.getId();
        this.hostname = resourceContainer.getEntityName();

        final JsonObject node = Json.createObjectBuilder().add("type", "node").add("id", this.nodeId)
                .add("systemId", systemId).add("nodeGroupId", nodegroupId).add("hostname", this.hostname).build();

        return node;
    }

    public String getNodeId() {
        return this.nodeId;
    }

}
