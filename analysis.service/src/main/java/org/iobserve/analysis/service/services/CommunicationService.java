package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;

/**
 * This class prepares data such that the visualization element communication is ready to be send to
 * the deployment visualization.
 *
 * @author jweg
 *
 */
public class CommunicationService {

    private String communicationId;
    private String technology;
    private String sourceId;
    private String targetId;

    public CommunicationService() {

    }

    /**
     * Builds a changelog for creating a communication for the deployment visualization.
     *
     * @param assemblyConnector
     * @param systemId
     * @param technology
     * @return
     */
    public JsonObject createCommunication(final AssemblyConnector assemblyConnector, final String systemId,
            final String technology) {
        // TODO source und target richtig herum?
        this.communicationId = assemblyConnector.getId();
        this.technology = technology;
        this.sourceId = assemblyConnector.getProvidingAssemblyContext_AssemblyConnector().getId();
        this.targetId = assemblyConnector.getRequiringAssemblyContext_AssemblyConnector().getId();

        final JsonObject communication = Json.createObjectBuilder().add("type", "communication")
                .add("id", this.communicationId).add("systemId", systemId).add("technology", this.technology)
                .add("sourceId", this.sourceId).add("targetId", this.targetId).build();

        final JsonObject communicationObject = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", communication).build();

        return communicationObject;
    }

    public String getCommunicationId() {
        return this.communicationId;
    }

}
