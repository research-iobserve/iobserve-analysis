/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package org.iobserve.analysis.sink.landscape;

import java.util.List;

import javax.json.Json;
import javax.json.JsonObject;

import org.eclipse.emf.ecore.EObject;
import org.iobserve.analysis.service.util.Changelog;
import org.iobserve.model.provider.neo4j.IModelProvider;
import org.palladiosimulator.pcm.core.composition.AssemblyConnector;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * This class prepares data such that the visualization element serviceInstance is created. It has
 * to be added to a changelog in order to be send to the deployment visualization.
 *
 * @author jweg
 *
 */
public class ServiceInstanceService {

    private final CommunicationInstanceService communicationInstanceService = new CommunicationInstanceService();

    private String serviceInstanceName;
    private String serviceInstanceId;

    /**
     * empty default constructor.
     */
    public ServiceInstanceService() {

    }

    /**
     * Builds data for creating a serviceInstance for the deployment visualization.
     *
     * @param assemblyContext
     *            assembly context
     * @param systemId
     *            system id
     * @param nodeId
     *            resource container id
     * @param serviceId
     *            assembly context id
     * @return JsonObject for creating a service instance
     */
    public JsonObject createServiceInstance(final AssemblyContext assemblyContext, final String systemId,
            final String nodeId, final String serviceId) {
        this.serviceInstanceName = assemblyContext.getEntityName();
        this.serviceInstanceId = "si" + assemblyContext.getId();

        final JsonObject serviceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", this.serviceInstanceId).add("systemId", systemId).add("name", this.serviceInstanceName)
                .add("serviceId", serviceId).add("nodeId", nodeId).build();

        return serviceInstance;
    }

    /**
     * Builds data for deleting a serviceInstance for the deployment visualization.
     *
     * @param assemblyContext
     *            assembly context
     * @param systemId
     *            system id
     * @param nodeId
     *            resource container id
     * @param systemModelGraphProvider
     *            provider for the system model
     * @return JsonObject for deleting a service instance
     */

    public JsonObject deleteServiceInstance(final AssemblyContext assemblyContext, final String systemId,
            final String nodeId,
            final IModelProvider<org.palladiosimulator.pcm.system.System> systemModelGraphProvider) {
        this.serviceInstanceId = "si" + assemblyContext.getId();
        // check whether this serviceInstance is referenced by communicationInstances
        final List<EObject> maybeAssemblyConnectors = systemModelGraphProvider
                .readOnlyReferencingComponentsById(AssemblyContext.class, assemblyContext.getId());
        // if so, delete all communicationInstances
        if (!maybeAssemblyConnectors.isEmpty()) {
            for (int i = 0; i < maybeAssemblyConnectors.size(); i++) {
                if (maybeAssemblyConnectors.get(i) instanceof AssemblyConnector) {
                    Changelog.delete(this.communicationInstanceService
                            .deleteCommunicationInstance((AssemblyConnector) maybeAssemblyConnectors.get(i)));
                }
            }
        }

        final JsonObject serviceInstance = Json.createObjectBuilder().add("type", "serviceInstance")
                .add("id", this.serviceInstanceId).add("systemId", systemId).add("serviceId", assemblyContext.getId())
                .add("nodeId", nodeId).build();

        return serviceInstance;

    }

    public String getServiceInstanceId() {
        return this.serviceInstanceId;
    }

}
