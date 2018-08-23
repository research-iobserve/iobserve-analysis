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
     * empty default constructor.
     */
    public CommunicationInstanceService() {
        // empty constructor
    }

    /**
     * Builds a communicationInstance for the deployment visualization.
     *
     * @param assemblyConnector
     *            a link between two assembly contexts (services)
     * @param systemId
     *            system id
     * @param communicationId
     *            assembly connector id
     * @return JsonObject for creating a communication instance
     */
    public JsonObject createCommunicationInstance(final AssemblyConnector assemblyConnector, final String systemId,
            final String communicationId) {
        this.communicationInstanceId = "ci" + assemblyConnector.getId();

        this.sourceId = "si" + assemblyConnector.getRequiringAssemblyContext_AssemblyConnector().getId();
        this.targetId = "si" + assemblyConnector.getProvidingAssemblyContext_AssemblyConnector().getId();

        // It is not possible yet to calculate the workload. We use a default value 1 here, so that
        // the communications between serviceInstances in the deployment visualization become
        // visible.
        this.workload = "1";

        return Json.createObjectBuilder().add("type", "communicationInstance").add("id", this.communicationInstanceId)
                .add("systemId", systemId).add("communicationId", communicationId).add("workload", this.workload)
                .add("sourceId", this.sourceId).add("targetId", this.targetId).build();
    }

    /**
     * Builds a communicationInstance to be deleted in the deployment visualization.
     *
     * @param assemblyConnector
     *            a link between two assembly contexts (services)
     * @return JsonObject for deleting a communication instance
     */
    public JsonObject deleteCommunicationInstance(final AssemblyConnector assemblyConnector) {
        this.communicationInstanceId = "ci" + assemblyConnector.getId();

        return Json.createObjectBuilder().add("type", "communicationInstance").add("id", this.communicationInstanceId)
                .build();
    }

    public String getCommunicationInstanceId() {
        return this.communicationInstanceId;
    }

}
