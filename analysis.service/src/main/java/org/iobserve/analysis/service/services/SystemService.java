package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * This class prepares data such that the visualization element system is ready to be send to the
 * deployment visualization.
 *
 * @author jweg
 *
 */

public class SystemService {

    private String systemName;
    private String systemId;

    public SystemService() {

    }

    /**
     * Builds data for creating a system in the deployment visualization.
     *
     * @param systemModel
     * @return
     */
    public JsonObject createSystem(final org.palladiosimulator.pcm.system.System systemModel) {
        this.systemName = systemModel.getEntityName();
        this.systemId = systemModel.getId();

        final JsonObject systemObject = Json.createObjectBuilder().add("type", "system").add("id", this.systemId)
                .add("name", this.systemName).build();

        return systemObject;
    }

    public String getSystemId() {
        return this.systemId;
    }

}
