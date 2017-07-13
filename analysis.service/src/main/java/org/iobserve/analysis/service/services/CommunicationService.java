package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;

public class CommunicationService {

    private String communicationId;
    private String technology;
    private String sourceId;
    private String targetId;

    public CommunicationService() {

    }

    // TODO
    public JsonArray createCommunication(final AssemblyConnector assemblyConnector, final String systemId) {

        this.communicationId = assemblyConnector.getId();
        // this.technology = assemblyConnector.get
        this.sourceId = assemblyConnector.getProvidingAssemblyContext_AssemblyConnector().getId();
        this.targetId = assemblyConnector.getRequiringAssemblyContext_AssemblyConnector().getId();

        final JsonObject communication = Json.createObjectBuilder().add("type", "communication")
                .add("id", this.communicationId).add("systemId", systemId).add("technology", "TCP/IP")
                .add("sourceId", this.sourceId).add("targetId", this.targetId).build();

        final JsonObject communicationData = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", communication).build();

        final JsonArray dataArray = Json.createArrayBuilder().add(communicationData).build();
        return dataArray;
    }

    // TODO
    public void deleteCommunication() {

    }

}
