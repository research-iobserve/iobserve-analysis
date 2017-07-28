package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;

public class CommunicationService {

    private String technology;
    private String sourceId;
    private String targetId;

    public CommunicationService() {

    }

    public JsonArray createCommunication(final AssemblyConnector assemblyConnector, final String systemId,
            final String technology) {
        // TODO source und target richtig herum?
        this.communicationId = assemblyConnector.getId();
        this.technology = technology;
        this.sourceId = assemblyConnector.getProvidingAssemblyContext_AssemblyConnector().getId();
        this.targetId = assemblyConnector.getRequiringAssemblyContext_AssemblyConnector().getId();

        final JsonObject communication = Json.createObjectBuilder().add("type", "communication")
                .add("id", this.communicationId).add("systemId", systemId).add("technology", this.technology)
                .add("sourceId", this.sourceId).add("targetId", this.targetId).build();

        final JsonObject communicationData = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", communication).build();

        final JsonArray dataArray = Json.createArrayBuilder().add(communicationData).build();
        return dataArray;
    }

    // TODO
    public void deleteCommunication() {

    }

    private String communicationId;

    public String getCommunicationId() {
        return this.communicationId;
    }

    public void setCommunicationId(final String communicationId) {
        this.communicationId = communicationId;
    }

}
