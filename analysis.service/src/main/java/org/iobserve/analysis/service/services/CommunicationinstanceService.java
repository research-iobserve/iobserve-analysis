package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

public class CommunicationinstanceService {

    public CommunicationinstanceService() {

    }

    // TODO
    public JsonArray createCommunicationinstance() {

        final JsonObject communicationInst = Json.createObjectBuilder().add("type", "communicationInstance")
                .add("id", "communicationInst-id-analysis" + Math.random()).add("systemId", "CoCoME")
                .add("communicationId", "communication-id-analysis").add("workload", "17")
                .add("sourceId", "serviceInstance-id-analysis").add("targetId", "test-CoCoME-serviceInstance-1")
                .build();

        final JsonObject communicationInstData = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", communicationInst).build();

        final JsonArray dataArray = Json.createArrayBuilder().add(communicationInstData).build();

        return dataArray;
    }

    // TODO
    public void deleteCommunicationinstance() {

    }

}
