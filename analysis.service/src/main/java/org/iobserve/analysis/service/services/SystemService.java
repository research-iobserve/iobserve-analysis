package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class SystemService {

    private String systemId;

    /**
     * constructor
     */
    public SystemService() {

    }

    public JsonArray createSystem(final org.palladiosimulator.pcm.system.System systemModel) {
        final String systemName = systemModel.getEntityName();
        this.systemId = systemModel.getId();

        final JsonObject system = Json.createObjectBuilder().add("type", "system").add("id", this.systemId)
                .add("name", systemName).build();
        final JsonArray systemData = Json.createArrayBuilder().add(system).build();

        return systemData;
    }

    // TODO
    protected void deleteSystem(final org.palladiosimulator.pcm.system.System systemModel) {

    }

    public String getSystemId() {
        return this.systemId;
    }

}
