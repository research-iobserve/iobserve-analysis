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

    public String nodegroupId;

    public NodegroupService() {

    }

    /**
     * Builds data for creating a nodegroup for the deployment visualization.
     *
     * @param systemId
     * @return
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
