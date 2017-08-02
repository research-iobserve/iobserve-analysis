package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;

public class CommunicationInstanceService {

    private String communicationInstanceId;
    private String sourceId;
    private String targetId;

    public CommunicationInstanceService() {

    }

    // TODO
    public JsonObject createCommunicationInstance(final AssemblyConnector assemblyConnector, final String systemId,
            final String communicationId) {
        this.communicationInstanceId = "ci" + assemblyConnector.getId();

        this.sourceId = "si" + assemblyConnector.getProvidingAssemblyContext_AssemblyConnector().getId();
        this.targetId = "si" + assemblyConnector.getRequiringAssemblyContext_AssemblyConnector().getId();

        final JsonObject communicationInst = Json.createObjectBuilder().add("type", "communicationInstance")
                .add("id", this.communicationInstanceId).add("systemId", systemId)
                .add("communicationId", communicationId).add("workload", "10").add("sourceId", this.sourceId)
                .add("targetId", this.targetId).build();

        final JsonObject communicationInstObject = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", communicationInst).build();

        return communicationInstObject;
    }

    // TODO
    public JsonObject deleteCommunicationInstance(final AssemblyConnector assemblyConnector) {
        this.communicationInstanceId = "ci" + assemblyConnector.getId();

        final JsonObject communicationInst = Json.createObjectBuilder().add("type", "communicationInstance")
                .add("id", this.communicationInstanceId).build();
        final JsonObject communicationInstObject = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "DELETE").add("data", communicationInst).build();

        return communicationInstObject;
    }

    public String getCommunicationInstanceId() {
        return this.communicationInstanceId;
    }

}
