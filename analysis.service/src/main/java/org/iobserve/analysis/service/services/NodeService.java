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
     * empty default constructor
     */
    public NodeService() {

    }

    /**
     * Builds data for creating a node for the deployment visualization.
     *
     * @param resourceContainer
     * @param systemId
     * @param nodegroupId
     * @return
     */
    public JsonObject createNode(final ResourceContainer resourceContainer, final String systemId,
            final String nodegroupId) {
        this.nodeId = resourceContainer.getId();
        // TODO Ist das nicht auch nodeName?
        this.hostname = resourceContainer.getEntityName();

        final JsonObject node = Json.createObjectBuilder().add("type", "node").add("id", this.nodeId)
                .add("systemId", systemId).add("nodeGroupId", nodegroupId).add("hostname", this.hostname).build();

        return node;
    }

    public String getNodeId() {
        return this.nodeId;
    }

}
