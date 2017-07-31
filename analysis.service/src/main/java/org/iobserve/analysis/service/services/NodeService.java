package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.resourceenvironment.ResourceContainer;

/**
 *
 * @author jweg
 *
 */
public class NodeService {

    private String nodeId;

    public NodeService() {

    }

    public JsonObject createNode(final ResourceContainer resourceContainer, final String systemId,
            final String nodegroupId) {
        this.nodeId = resourceContainer.getId();
        final String hostname = resourceContainer.getEntityName();

        final JsonObject node = Json.createObjectBuilder().add("type", "node").add("id", this.nodeId)
                .add("systemId", systemId).add("nodeGroupId", nodegroupId).add("hostname", hostname).build();
        final JsonObject nodeObject = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", node).build();

        return nodeObject;
    }

    // TODO
    public void deleteNode() {

    }

    public String getNodeId() {
        return this.nodeId;
    }

}
