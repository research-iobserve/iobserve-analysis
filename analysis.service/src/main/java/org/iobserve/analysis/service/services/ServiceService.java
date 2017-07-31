package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 *
 * @author jweg
 *
 */
public class ServiceService {

    private String serviceId;
    private String serviceName;

    public ServiceService() {

    }

    public JsonObject createService(final AssemblyContext assemblyContext, final String systemId) {

        this.serviceId = assemblyContext.getId();
        this.serviceName = assemblyContext.getEntityName();

        final JsonObject service = Json.createObjectBuilder().add("type", "service").add("id", this.serviceId)
                .add("systemId", systemId).add("name", this.serviceName).build();
        final JsonObject serviceObject = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", service).build();

        return serviceObject;
    }

    // TODO
    public void deleteService() {

    }

    public String getServiceId() {
        return this.serviceId;
    }

    public String getServiceName() {
        return this.serviceName;
    }

}
