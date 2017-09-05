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

/**
 * This class prepares data such that the visualization element nodegroup is created. It has to be
 * added to a changelog in order to be send to the deployment visualization.
 *
 * @author jweg
 *
 */
public class NodegroupService {

    private String nodegroupId;

    /**
     * empty default constructor.
     */
    public NodegroupService() {

    }

    /**
     * Builds data for creating a nodegroup for the deployment visualization.
     *
     * @param systemId
     *            system id
     * @return JsonObject for creating a nodegroup
     */
    public JsonObject createNodegroup(final String systemId) {
        // as there is no grouping mechanism available yet, each node gets its own nodegroup with a
        // random id
        this.nodegroupId = "nodeGroup-" + Math.random();

        final JsonObject nodeGroup = Json.createObjectBuilder().add("type", "nodeGroup").add("id", this.nodegroupId)
                .add("systemId", systemId).add("name", "nodeGroupName").build();

        return nodeGroup;

    }

    public String getNodegroupId() {
        return this.nodegroupId;
    }

}
