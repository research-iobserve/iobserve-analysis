package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyConnector;

/**
 * This class prepares data such that the visualization element communicationInstance is created or
 * deleted. It has to be added to a changelog in order to be send to the deployment visualization.
 *
 * @author jweg
 *
 */

public class CommunicationInstanceService {

    private String communicationInstanceId;
    private String sourceId;
    private String targetId;
    private String workload;

    /**
     * empty default constructor
     */
    public CommunicationInstanceService() {

    }

    /**
     * Builds a communicationInstance for the deployment visualization.
     *
     * @param assemblyConnector
     * @param systemId
     * @param communicationId
     * @return
     */
    public JsonObject createCommunicationInstance(final AssemblyConnector assemblyConnector, final String systemId,
            final String communicationId) {
        this.communicationInstanceId = "ci" + assemblyConnector.getId();

        this.sourceId = "si" + assemblyConnector.getProvidingAssemblyContext_AssemblyConnector().getId();
        this.targetId = "si" + assemblyConnector.getRequiringAssemblyContext_AssemblyConnector().getId();

        // It is not possible yet to calculate the workload. We use a default value 1 here, so that
        // the communications between serviceInstances in the deployment visualization become
        // visible.
        this.workload = "1";

        final JsonObject communicationInst = Json.createObjectBuilder().add("type", "communicationInstance")
                .add("id", this.communicationInstanceId).add("systemId", systemId)
                .add("communicationId", communicationId).add("workload", this.workload).add("sourceId", this.sourceId)
                .add("targetId", this.targetId).build();

        return communicationInst;
    }

    /**
     * Builds a communicationInstance to be deleted in the deployment visualization.
     *
     * @param assemblyConnector
     * @return
     */
    public JsonObject deleteCommunicationInstance(final AssemblyConnector assemblyConnector) {
        this.communicationInstanceId = "ci" + assemblyConnector.getId();

        final JsonObject communicationInst = Json.createObjectBuilder().add("type", "communicationInstance")
                .add("id", this.communicationInstanceId).build();

        return communicationInst;
    }

    public String getCommunicationInstanceId() {
        return this.communicationInstanceId;
    }

}
