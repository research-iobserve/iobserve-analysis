package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
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

    public JsonArray createSystem(final org.palladiosimulator.pcm.system.System systemModel) {
        this.systemName = systemModel.getEntityName();
        this.systemId = systemModel.getId();

        final JsonObject system = Json.createObjectBuilder().add("type", "system").add("id", this.systemId)
                .add("name", this.systemName).build();
        final JsonArray systemData = Json.createArrayBuilder().add(system).build();

        return systemData;
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
