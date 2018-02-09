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
package org.iobserve.analysis.service.services;

import javax.json.Json;
import javax.json.JsonObject;

import org.iobserve.analysis.service.util.Changelog;
import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * This class prepares data such that the visualization element service is created. It has to be
 * added to a {@link Changelog} in order to be send to the deployment visualization.
 *
 * @author jweg
 *
 */
public class ServiceService {

    private String serviceId;
    private String serviceName;

    /**
     * empty default constructor.
     */
    public ServiceService() {

    }

    /**
     * Builds data for creating a service for the deployment visualization.
     *
     * @param assemblyContext
     *            assembly context
     * @param systemId
     *            system id
     * @return JsonObject for creating a service
     */
    public JsonObject createService(final AssemblyContext assemblyContext, final String systemId) {

        this.serviceId = assemblyContext.getId();
        this.serviceName = assemblyContext.getEntityName();

        final JsonObject service = Json.createObjectBuilder().add("type", "service").add("id", this.serviceId)
                .add("systemId", systemId).add("name", this.serviceName).build();

        return service;
    }

    public String getServiceId() {
        return this.serviceId;
    }

}
