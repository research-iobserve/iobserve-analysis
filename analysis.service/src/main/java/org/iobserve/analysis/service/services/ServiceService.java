package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.allocation.AllocationContext;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

public class ServiceService {

    private String serviceId;

    public String getServiceId() {
        return this.serviceId;
    }

    public ServiceService() {

    }

    public JsonArray createService(final AllocationContext allocationContext, final String systemId) {
        final JsonArrayBuilder nodeArrayBuilder = Json.createArrayBuilder();

        final AssemblyContext assemblyContext = allocationContext.getAssemblyContext_AllocationContext();

        this.serviceId = assemblyContext.getId();
        final String serviceName = assemblyContext.getEntityName();

        final JsonObject service = Json.createObjectBuilder().add("type", "service").add("id", this.serviceId)
                .add("systemId", systemId).add("name", serviceName).build();
        final JsonObject serviceData = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", service).build();

        nodeArrayBuilder.add(serviceData);
        final JsonArray dataArray = nodeArrayBuilder.build();
        return dataArray;
    }

    // TODO
    public void deleteService() {

    }

}
