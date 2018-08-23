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

/**
 * This class prepares data such that the visualization element system is ready to be send to the
 * deployment visualization.
 *
 * @author jweg
 *
 */

public class SystemService {

    private String systemId;

    /**
     * empty default constructor.
     */
    public SystemService() {
        // nothing to do
    }

    /**
     * Builds data for creating a system in the deployment visualization.
     *
     * @param systemModel
     *            system model
     * @return JsonObject for creating a system
     */
    public JsonObject createSystem(final org.palladiosimulator.pcm.system.System systemModel) {
        final String systemName = systemModel.getEntityName();
        this.systemId = systemModel.getId();

        return Json.createObjectBuilder().add("type", "system").add("id", this.systemId).add("name", systemName)
                .build();
    }

    public String getSystemId() {
        return this.systemId;
    }

}
