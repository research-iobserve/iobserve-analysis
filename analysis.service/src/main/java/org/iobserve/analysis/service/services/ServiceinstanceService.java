package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

public class ServiceinstanceService {

    public ServiceinstanceService() {

    }

    public JsonArray createServiceInstance(final String systemId, final String nodeId, final String serviceId) {
        final JsonArrayBuilder nodeArrayBuilder = Json.createArrayBuilder();

        // TODO serviceInstance-id
        final JsonObject serviceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", "serviceInstance-id-analysis" + Math.random()).add("systemId", systemId)
                .add("name", "analysis-serviceInstance").add("serviceId", serviceId).add("nodeId", nodeId).build();
        final JsonObject serviceInstanceData = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", serviceInstance).build();

        nodeArrayBuilder.add(serviceInstanceData);
        final JsonArray dataArray = nodeArrayBuilder.build();
        return dataArray;
    }

    // TODO
    public void deleteServiceInstance() {

    }
}
