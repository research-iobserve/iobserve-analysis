package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;

import org.palladiosimulator.pcm.resourceenvironment.ResourceEnvironment;

public class CommunicationService {

    public CommunicationService() {

    }

    // TODO
    public JsonArray createCommunication(final ResourceEnvironment resourceEnvironmentModel, final String systemId) {
        final JsonArrayBuilder nodeArrayBuilder = Json.createArrayBuilder();

        final JsonArray dataArray = nodeArrayBuilder.build();
        return dataArray;
    }

    // TODO
    public void deleteCommunication() {

    }

}
