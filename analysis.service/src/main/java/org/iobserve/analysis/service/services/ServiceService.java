package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;

import util.Changelog;

/**
 * This class prepares data such that the visualization element service is created. It has to be
 * added to a {@link Changelog} in order to be send to the deployment visualization.
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
     * Builds data for creating a service for the deployment visualization.
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

        return service;
    }

    public String getServiceId() {
        return this.serviceId;
    }

}
