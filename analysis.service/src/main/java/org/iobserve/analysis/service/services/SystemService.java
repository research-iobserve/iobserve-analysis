package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

/**
 *
 * @author jweg
 *
 */

public class SystemService {

    private String systemName;
    private String systemId;

    /**
     * constructor
     */
    public SystemService() {

    }

    public JsonObject createSystem(final org.palladiosimulator.pcm.system.System systemModel) {
        this.systemName = systemModel.getEntityName();
        this.systemId = systemModel.getId();

        final JsonObject systemObject = Json.createObjectBuilder().add("type", "system").add("id", this.systemId)
                .add("name", this.systemName).build();

        return systemObject;
    }

    // TODO: check whether all nodeGroups belonging to the system are deleted, then send
    // DELETE-request
    protected void deleteSystem(final org.palladiosimulator.pcm.system.System systemModel) {

    }

    public String getSystemName() {
        return this.systemName;
    }

    public String getSystemId() {
        return this.systemId;
    }

}
