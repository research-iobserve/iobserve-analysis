package org.iobserve.analysis.service.services;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.modelneo4j.ModelProvider;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * This class prepares data such that the visualization element serviceInstance is ready to be send
 * to the deployment visualization.
 *
 * @author jweg
 *
 */
public class ServiceInstanceService {

    private final CommunicationInstanceService communicationInstanceService = new CommunicationInstanceService();

    private String serviceInstanceName;
    private String serviceInstanceId;

    public ServiceInstanceService() {

    }

    /**
     * Builds a changelog for creating a serviceInstance for the deployment visualization.
     *
     * @param assemblyContext
     * @param systemId
     * @param nodeId
     * @param serviceId
     * @return
     */
    public JsonObject createServiceInstance(final AssemblyContext assemblyContext, final String systemId,
            final String nodeId, final String serviceId) {
        this.serviceInstanceName = assemblyContext.getEntityName();
        this.serviceInstanceId = "si" + assemblyContext.getId();

        // TODO serviceInstance-id
        final JsonObject serviceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", this.serviceInstanceId).add("systemId", systemId).add("name", this.serviceInstanceName)
                .add("serviceId", serviceId).add("nodeId", nodeId).build();
        final JsonObject serviceInstanceObject = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "CREATE").add("data", serviceInstance).build();

        return serviceInstanceObject;
    }

    /**
     * Builds a changelog for deleting a serviceInstance for the deployment visualization.
     *
     * @param assemblyContext
     * @param systemId
     * @param nodeId
     * @param serviceId
     * @param systemModelGraphProvider
     * @return
     */

    public JsonObject deleteServiceInstance(final AssemblyContext assemblyContext, final String systemId,
            final String nodeId,
            final ModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider) {
        // nachgucken, ob this.serviceInstanceId in communicationinstance als sourceId oder targetId
        // vorkommt; wenn ja, diese communicationinstance löschen

        this.serviceInstanceId = "si" + assemblyContext.getId();
        // check whether this serviceInstance is referenced by communicationInstances
        final List<EObject> maybeAssemblyConnectors = systemModelGraphProvider
                .readOnlyReferencingComponentsById(AssemblyContext.class, assemblyContext.getId());

        if (!maybeAssemblyConnectors.isEmpty()) {
            for (int i = 0; i < maybeAssemblyConnectors.size(); i++) {
                if (maybeAssemblyConnectors.get(i) instanceof AssemblyConnector) {
                    this.communicationInstanceService
                            .deleteCommunicationInstance((AssemblyConnector) maybeAssemblyConnectors.get(i));
                }
            }
        }

        final JsonObject serviceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", this.serviceInstanceId).add("systemId", systemId).add("serviceId", assemblyContext.getId())
                .add("nodeId", nodeId).build();
        final JsonObject removingServiceInstanceObject = Json.createObjectBuilder().add("type", "changelog")
                .add("operation", "DELETE").add("data", serviceInstance).build();

        return removingServiceInstanceObject;

    }

    public String getServiceInstanceId() {
        return this.serviceInstanceId;
    }

}