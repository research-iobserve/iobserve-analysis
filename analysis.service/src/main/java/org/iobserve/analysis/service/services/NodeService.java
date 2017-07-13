package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

public class NodeService {

    private String nodeId;

    public NodeService() {

    }

    public JsonArray createNode(final ResourceContainer resourceContainer, final String systemId,
            final String nodegroupId) {
        final JsonArrayBuilder nodeArrayBuilder = Json.createArrayBuilder();

        this.nodeId = resourceContainer.getId();
        final String hostname = resourceContainer.getEntityName();

        final JsonObject node = Json.createObjectBuilder().add("type", "node").add("id", this.nodeId)
                .add("systemId", systemId).add("nodeGroupId", nodegroupId).add("hostname", hostname).build();
        final JsonObject nodeData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", node).build();

        nodeArrayBuilder.add(nodeData);
        final JsonArray dataArray = nodeArrayBuilder.build();
        return dataArray;
    }

    public String getNodeId() {
        return this.nodeId;
    }

}
