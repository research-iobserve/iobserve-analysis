package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 *
 * @author jweg
 *
 */
public class NodegroupService {

    public String nodegroupId;

    /**
     * constructor
     */
    public NodegroupService() {

    }

    // return type JsonArray or JsonObject?
    // what name?
    public JsonArray createNodegroup(final String systemId) {
        // as there is no grouping element available, each node gets its own nodegroup with a
        // random id
        final JsonArrayBuilder nodeArrayBuilder = Json.createArrayBuilder();
        this.nodegroupId = "nodeGroup-" + Math.random();

        final JsonObject nodeGroup = Json.createObjectBuilder().add("type", "nodeGroup").add("id", this.nodegroupId)
                .add("systemId", systemId).add("name", "nodeGroupName").build();
        final JsonObject nodeGroupData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", nodeGroup).build();
        nodeArrayBuilder.add(nodeGroupData);

        final JsonArray dataArray = nodeArrayBuilder.build();
        return dataArray;

    }

    // TODO
    public void deleteNodegroup() {

    }

    public String getNodegroupId() {
        return this.nodegroupId;
    }

}
