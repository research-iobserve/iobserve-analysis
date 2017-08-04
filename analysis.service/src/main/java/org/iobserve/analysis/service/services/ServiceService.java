package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * This class prepares data such that the visualization element service is ready to be send to the
 * deployment visualization.
 *
 * @author jweg
 *
 */
public class ServiceService {

    private String serviceId;
    private String serviceName;

    public ServiceService() {

    }

    /**
     * Builds a changelog for creating a service for the deployment visualization.
     *
     * @param assemblyContext
     * @param systemId
     * @return
     */
    public JsonObject createService(final AssemblyContext assemblyContext, final String systemId) {

        this.serviceId = assemblyContext.getId();
        this.serviceName = assemblyContext.getEntityName();

        final JsonObject service = Json.createObjectBuilder().add("type", "service").add("id", this.serviceId)
                .add("systemId", systemId).add("name", this.serviceName).build();
        final JsonObject serviceObject = Json.createObjectBuilder().add("type", "changelog").add("operation", "CREATE")
                .add("data", service).build();

        return serviceObject;
    }

    public String getServiceId() {
        return this.serviceId;
    }

}
