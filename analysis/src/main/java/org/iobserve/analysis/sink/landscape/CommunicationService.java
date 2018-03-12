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
 * This class prepares data such that the visualization element communication is created. It has to
 * be added to a changelog in order to be send to the deployment visualization.
 *
 * @author Josefine Wegert
 *
 */
public class CommunicationService {

    private String communicationId;

    /**
     * empty default constructor.
     */
    public CommunicationService() {
        // nothing to be done here
    }

    /**
     * Builds a communication for the deployment visualization.
     *
     * @param assemblyConnector
     *            a link between two assembly contexts (services)
     * @param systemId
     *            system id
     * @param technology
     *            name of linking resource
     * @return JsonObject for creating a communication
     */
    public JsonObject createCommunication(final AssemblyConnector assemblyConnector, final String systemId,
            final String technology) {
        this.communicationId = assemblyConnector.getId();
        final String sourceId = assemblyConnector.getRequiringAssemblyContext_AssemblyConnector().getId();
        final String targetId = assemblyConnector.getProvidingAssemblyContext_AssemblyConnector().getId();

        return Json.createObjectBuilder().add("type", "communication").add("id", this.communicationId)
                .add("systemId", systemId).add("technology", technology).add("sourceId", sourceId)
                .add("targetId", targetId).build();
    }

    public String getCommunicationId() {
        return this.communicationId;
    }

}
