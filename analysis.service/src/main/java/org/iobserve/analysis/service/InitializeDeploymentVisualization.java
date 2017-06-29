package org.iobserve.analysis.service;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.iobserve.analysis.model.SystemModelProvider;
import org.neo4j.graphdb.GraphDatabaseService;

public class InitializeDeploymentVisualization {

    /** reference to allocation model graph. */
    private final GraphDatabaseService allocationModelGraph;
    /** reference to system model graph. */
    private final GraphDatabaseService systemModelGraph;
    /** reference to resource environment model graph. */
    private final GraphDatabaseService resourceEnvironmentModelGraph;

    private final SystemModelProvider oldSystemModelProvider;

    // final ModelProvider<System> systemModelProvider = new ModelProvider<>(this.systemModelGraph);
    // final ModelProvider<ResourceContainer> resourceEnvironmentModelProvider = new
    // ModelProvider<>(
    // this.resourceEnvironmentModelGraph);
    // final ModelProvider<Allocation> allocationModelProvider = new
    // ModelProvider<>(this.allocationModelGraph);

    /**
     * constructor
     *
     * @param allocationModelProvider
     * @param systemModelProvider
     * @param resourceEnvironmentModelProvider
     */
    public InitializeDeploymentVisualization(final GraphDatabaseService allocationModelGraph,
            final GraphDatabaseService systemModelGraph, final SystemModelProvider oldSystemModelProvider,
            final GraphDatabaseService resourceEnvironmentModelGraph) {
        // get all model references
        this.allocationModelGraph = allocationModelGraph;
        this.systemModelGraph = systemModelGraph;
        this.resourceEnvironmentModelGraph = resourceEnvironmentModelGraph;
        this.oldSystemModelProvider = oldSystemModelProvider;
    }

    private JsonArray createData() {
        final String systemId = this.oldSystemModelProvider.getModel().getId();
        final String systemName = this.oldSystemModelProvider.getModel().getEntityName();

        final JsonObject system = Json.createObjectBuilder().add("type", "system").add("id", systemId)
                .add("name", systemName).build();

        return null;

    }

}
