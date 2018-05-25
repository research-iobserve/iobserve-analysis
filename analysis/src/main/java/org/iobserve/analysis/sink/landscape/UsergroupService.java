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
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

import org.palladiosimulator.pcm.core.composition.AssemblyContext;

/**
 * This class prepares data such that the visualization element user group is created. It has to be
 * added to a changelog in order to be send to the deployment visualization.
 *
 * @author jweg
 *
 */
public class UsergroupService {
    private final String usergroupId = "test_usergroup_id" + Math.random();

    /**
     * empty default constructor.
     */
    public UsergroupService() {
        // empty constructor
    }

    /**
     * Builds data for creating a user group for the deployment visualization.
     *
     * @param systemId
     *            .
     * @param userInvokedServices
     *            .
     * @return JSon object for the creation of a user group
     */
    public JsonObject createUsergroup(final String systemId, final List<AssemblyContext> userInvokedServices) {
        final JsonArrayBuilder builder = Json.createArrayBuilder();

        // build array of targetIds
        for (int i = 0; i < userInvokedServices.size(); i++) {
            final String serviceId = userInvokedServices.get(i).getId();
            // invokedServiceObject = Json.createObjectBuilder().add("usergroupId",
            // this.usergroupId)
            // .add("serviceId", serviceId).build();
            builder.add(serviceId);
            // invokedServiceObject = this.service.createService(userInvokedServices.get(i),
            // systemId);
            // builder.add(invokedServiceObject);
        }
        final JsonArray invokedServicesArray = builder.build();

        return Json.createObjectBuilder().add("type", "userGroup").add("id", this.usergroupId).add("systemId", systemId)
                .add("name", "test-usergroup" + Math.random()).add("services", invokedServicesArray).build();
    }

}
