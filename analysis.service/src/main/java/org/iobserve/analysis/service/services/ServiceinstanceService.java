package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 *
 * @author jweg
 *
 */
public class ServiceinstanceService {

    private String serviceinstanceName;
    private String serviceinstanceId;

    public ServiceinstanceService() {

    }

    public JsonArray createServiceInstance(final AssemblyContext assemblyContext, final String systemId,
            final String nodeId, final String serviceId) {
        this.serviceinstanceName = assemblyContext.getEntityName();
        this.serviceinstanceId = "si" + assemblyContext.getId();

        final JsonArrayBuilder nodeArrayBuilder = Json.createArrayBuilder();

        // TODO serviceInstance-id
        final JsonObject serviceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", this.serviceinstanceId).add("systemId", systemId).add("name", this.serviceinstanceName)
                .add("serviceId", serviceId).add("nodeId", nodeId).build();
        final JsonObject serviceInstanceData = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", serviceInstance).build();

        nodeArrayBuilder.add(serviceInstanceData);
        final JsonArray dataArray = nodeArrayBuilder.build();
        return dataArray;
    }

    // TODO
    public void deleteServiceInstance() {

    }

    public String getServiceinstanceName() {
        return this.serviceinstanceName;
    }

    public String getServiceinstanceId() {
        return this.serviceinstanceId;
    }

}
