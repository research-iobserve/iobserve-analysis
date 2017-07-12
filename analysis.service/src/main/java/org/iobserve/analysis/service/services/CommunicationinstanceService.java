package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

public class CommunicationinstanceService {

    public CommunicationinstanceService() {

    }

    // TODO
    public JsonArray createCommunicationinstance() {
        final JsonArrayBuilder nodeArrayBuilder = Json.createArrayBuilder();

        final JsonArray dataArray = nodeArrayBuilder.build();
        return dataArray;
    }

    // TODO
    public void deleteCommunicationinstance() {

    }

}
